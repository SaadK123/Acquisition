
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.stringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuthenticateCloudService extends TokenService {



    StringRedisTemplate stringRedisTemplate;


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

         RequestCloudDto requestCloudDto = (RequestCloudDto) requestDTO;

         String tokenId = requestCloudDto.authenticatorId;

         String isWeb = requestCloudDto.isWeb + "";

        List<String> strings = new ArrayList<>() {{
            add(tokenId);
            add(isWeb);
        }};

        RedisScript<String> script = RedisScript.of(luaCodeFind, String.class);

        return stringRedisTemplate.execute(script, Collections.emptyList(),strings);
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

             boolean hasSet = stringRedisTemplate.opsForValue().setIfAbsent(authenticator,playerId, Duration.ofDays(3));
             if(hasSet) {
                 break;
             }
        }
        addToken(authenticator,playerId,isWeb);
        return authenticator;
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
              TokenMetaData.deleteTokenBasedMap(tokenId, stringRedisTemplate);
             }else {
                 compositeKey.append(tokenId);
             }


         }

        stringRedisTemplate.opsForValue().set(playerId,compositeKey.toString());
    }




}
