
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.Duration;

@Service
public class AuthenticateCloudService extends TokenService {



    RedisTemplate<String,String> redisTemplate;


    String luaCodeFind = """
            local function split(val)
             
             local array = {}
             
             local currentStart = 1
             
             local length = #val
             
             for i = 1,length do
             
             local isWhiteSpace = string.sub(val,i,i) == " ";
             
             if isWhiteSpace then
             
              if i == length then
             
               return array
             
               end
               if i ~= currentStart  then 
               
               local sub = string.sub(val,currentStart,i-1)
               
               table.insert(array,sub)
                
               end
               
                
               currentStart = i+1 
              end
             
             
             
             end
             
             if currentStart <= length then
             local lastSub = string.sub(val,currentStart,length)
             table.insert(array,lastSub)
             
             end
             return array
             
             end
             
             
            local tokenId = ARGV[1]
            
            local isWeb = ARGV[2]
            
             local tokenValuesRaw = redis.call('GET',tokenId)
             
            
             if tokenValuesRaw   then
             
         
            local arrayVal =  split(tokenValuesRaw)
            
            
            local isWebFromDb = arrayVal[2]
            
            
            if isWebFromDb == isWeb then 
            
            return arrayVal[1]
            
            end
            
            end
            
            
            return nil
            
            
                   
            """;

    public String findPlayerWithAuth(RequestDTO requestDTO) {

         String authenticatorId =  requestDTO.authenticatorId;

         RequestCloudDto requestCloudDto = (RequestCloudDto) requestDTO;


         redisTemplate.execute();

         return data.playerId;
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


    public String createAuthentication(RequestDTO requestDTO) {



        String playerId = requestDTO.authenticatorId;


        RequestCloudDto requestCloudDto = (RequestCloudDto) requestDTO;

        boolean isWeb = requestCloudDto.isWeb;

        String authenticator;

        while(true) {
            authenticator = generateToken();

             boolean hasSet = TokenMetaData.saveSerializedToken(authenticator,
                     new TokenMetaData(playerId,isWeb), redisTemplate);
             if(hasSet) {
                 break;
             }
        }
        addToken(authenticator,playerId,isWeb);
        return authenticator;
    }



    private void addToken(String token,String playerId,boolean isWeb) {


        // 1 : first find if player id has already a used token for either web or unity

         String tokensRaw = redisTemplate.opsForValue().get(playerId);

         if(tokensRaw == null) {
             redisTemplate.opsForValue().set(playerId,token);
             return;
         }

         String[] tokensIds = tokensRaw.split(" ");

         StringBuilder compositeKey = new StringBuilder(token + " ");

         for(String tokenId : tokensIds) {
             String rawValue = redisTemplate.opsForValue().get(tokenId);

             TokenMetaData currentToken = TokenMetaData.deserialize(rawValue);

             if(currentToken == null) { continue;}

             if(currentToken.isWeb() == isWeb) {
              TokenMetaData.deleteTokenBasedMap(tokenId, redisTemplate);
             }else {
                 compositeKey.append(tokenId);
             }


         }

        redisTemplate.opsForValue().set(playerId,compositeKey.toString());
    }




}
