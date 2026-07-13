
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.Duration;

@Service
public class TokenService {



    StringRedisTemplate stringRedisTemplate;



    public String findPlayerWithToken(String tokenId,boolean isWeb) {


         String rawDataToken = stringRedisTemplate.opsForValue().get(tokenId);

         TokenMetaData data = TokenMetaData.deserialize(rawDataToken);

         if(data == null || data.isWeb != isWeb) {
             throw new AcquisitionException("token has expired or not use in the good format");
         }

         boolean hasSet = stringRedisTemplate.opsForValue().setIfAbsent("BUSY"+tokenId,"",Duration.ofMinutes(10));

         if(!hasSet) {
             throw new AcquisitionException("token is already being in use");
         }

         return data.playerId;
    }

    private String findPlayerWithToken(String tokenId) {
        return stringRedisTemplate.opsForValue().get(tokenId);
    }




    private record TokenMetaData(String playerId,boolean isWeb) {
        private static String serialize(TokenMetaData tmd) {
            return tmd.playerId + " " + tmd.isWeb;
        }


        public static boolean saveSerializedToken(String tokenId, TokenMetaData tokenMetaData,
                                               StringRedisTemplate redisTemplate) {

           return redisTemplate.opsForValue().setIfAbsent(tokenId, serialize(tokenMetaData), Duration.ofDays(5));
        }



        public static TokenMetaData deserialize(String serializedMetaData) {
            if(serializedMetaData == null) {return null;}
            String[] serializedValues = serializedMetaData.split(" ");

            String booleanValueRaw  = serializedValues[1];

            boolean isWeb = booleanValueRaw.equals("true");

            booleanValueRaw = serializedValues[2];



            return new TokenMetaData(serializedValues[0],isWeb);
        }

        public static void deleteTokenBasedMap(String tokenId,StringRedisTemplate template) {
            template.delete(tokenId);
        }
    }







    private static final SecureRandom rnd = new SecureRandom();


    private String generateToken() {

        StringBuilder token = new StringBuilder();

        for(int i = 0; i < 300; ++i) {
           int randChar =  rnd.nextInt(33,127);

           char c = (char) randChar;

           token.append(c);
        }
        return token.toString();
    }


    public String createToken(String playerId,boolean isWeb) {
        String token;

        while(true) {
            token = generateToken();

             boolean hasSet = TokenMetaData.saveSerializedToken(token,
                     new TokenMetaData(playerId,isWeb),stringRedisTemplate);
             if(hasSet) {
                 break;
             }
        }
        addToken(token,playerId,isWeb);
        return token;
    }



    private void addToken(String token,String playerId,boolean isWeb) {


        // 1 : first find if player id has already a used token for either web or unity

         String tokensRaw = stringRedisTemplate.opsForValue().get(playerId);

         if(tokensRaw == null) {
             stringRedisTemplate.opsForValue().set(playerId,token);
             return;
         }

         String[] tokensIds = tokensRaw.split(" ");

         StringBuilder compositeKey = new StringBuilder(token + " ");

         for(String tokenId : tokensIds) {
             String rawValue = stringRedisTemplate.opsForValue().get(tokenId);

             TokenMetaData currentToken = TokenMetaData.deserialize(rawValue);

             if(currentToken == null) { continue;}

             if(currentToken.isWeb() == isWeb) {
              TokenMetaData.deleteTokenBasedMap(tokenId,stringRedisTemplate);
             }else {
                 compositeKey.append(tokenId);
             }


         }

        stringRedisTemplate.opsForValue().set(playerId,compositeKey.toString());
    }




}
