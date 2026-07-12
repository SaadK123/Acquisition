
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.Duration;

@Service
public class TokenService {



    StringRedisTemplate stringRedisTemplate;



    public String findPlayerWithToken(RequestDTO tokenRaw) {
        String tokenId = tokenRaw.tokenId();

         String rawDataToken = stringRedisTemplate.opsForValue().get(tokenId);

         TokenMetaData data = TokenMetaData.deserialize(rawDataToken);

         if(data == null || data.isWeb != tokenRaw.forWeb()) {
             throw new AcquisitionException("token has expired or not use in the good format");
         }

         return data.playerId;
    }




    private record TokenMetaData(String playerId,boolean isWeb) {
        private static String serialize(TokenMetaData tmd) {
            return tmd.playerId + " " + tmd.isWeb;
        }


        public static void saveSerializedToken(String tokenId, TokenMetaData tokenMetaData,
                                               StringRedisTemplate redisTemplate) {

            redisTemplate.opsForValue().setIfAbsent(tokenId, serialize(tokenMetaData), Duration.ofDays(5));
        }



        public static TokenMetaData deserialize(String serializedMetaData) {
            if(serializedMetaData == null) {return null;}
            String[] serializedValues = serializedMetaData.split(" ");

            String booleanValueRaw  = serializedValues[1];

            boolean isWeb = booleanValueRaw.equals("true");

            return new TokenMetaData(serializedValues[0],isWeb);
        }
    }







    private static final SecureRandom rnd = new SecureRandom();


    public String generateToken(String playerId, boolean isWeb) {

        StringBuilder token = new StringBuilder();

        for(int i = 0; i < 300; ++i) {
           int randChar =  rnd.nextInt(33,127);

           char c = (char) randChar;

           token.append(c);
        }

        String tokenStringify = token.toString();

        addToken(tokenStringify,playerId,isWeb);

        return tokenStringify;
    }


    private void addToken(String token,String playerId,boolean isWeb) {

        // 1 : first find if player id has already a used token for either web or unity

         String tokensRaw = stringRedisTemplate.opsForValue().get(playerId);

         if(tokensRaw == null) {
             addTokenInRegistries(token,isWeb,playerId,token);
             return;
         }

         String[] tokensIds = tokensRaw.split(" ");

         StringBuilder compositeKey = new StringBuilder(token + " ");

         for(String tokenId : tokensIds) {
             String rawValue = stringRedisTemplate.opsForValue().get(tokenId);

             TokenMetaData currentToken = TokenMetaData.deserialize(rawValue);

             if(currentToken == null || currentToken.isWeb() == isWeb) {
                continue;
             }

             compositeKey.append(tokenId);
         }

         addTokenInRegistries(token,isWeb,playerId, compositeKey.toString());
    }
    private void addTokenInRegistries(String tokenId, boolean isWeb, String playerId, String compositeKey) {

        TokenMetaData.saveSerializedToken(tokenId,new TokenMetaData(playerId,isWeb),stringRedisTemplate);
        stringRedisTemplate.opsForValue().set(playerId,compositeKey);
    }



}
