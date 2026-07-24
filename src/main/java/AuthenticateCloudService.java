
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class AuthenticateCloudService extends TokenService {


  StringRedisTemplate stringRedisTemplate;

  static   String luaCodeSplit = """
               local function split(val)
             
             local array = {}
             
             local currentStart = 1
             
             local length = #val
             
             for i = 1,length do
             
             local isWhiteSpace = string.sub(val,i,i) == " ";
             
             if isWhiteSpace then
             
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
             """;


    static final String luaSetBusyFlag = luaCodeSplit + """
                 
                 
                 local function setFlag(isBusy,tokenId)
                 
                 local valueRaw = redis.call('GET',tokenId)
                 
                 local listValues = split(valueRaw)
                 
                 local newValue = listValues[1] .. " " .. listValues[2] .. " " .. isBusy
                 
                 redis.call('SET',tokenId,newValue,"KEEPTTL")
                 
                 end
                 """;

        static    String luaCodeFind =   luaSetBusyFlag + luaCodeSplit + """
         
            local tokenId = ARGV[1]
            
            local isWeb = ARGV[2]
            
            local tokenValuesRaw = redis.call('GET',tokenId)
             
            
             if tokenValuesRaw   then
             
         
            local arrayVal =  split(tokenValuesRaw)
            
            
            local isWebFromDb = arrayVal[2]
            
            local isBusy = arrayVal[3]
            
            if isWebFromDb == isWeb and  isBusy == "false" then 
            
            setFlag("true",tokenId)
            
            return arrayVal[1]
            
            end
            
            end
            
            
            return nil
               
            """;

    public String findPlayerWithAuth(RequestDTO requestDTO) {

         RequestCloudDto requestCloudDto = (RequestCloudDto) requestDTO;

         String tokenId = requestCloudDto.authenticatorId;

         String isWeb = requestCloudDto.isWeb + "";





        return stringRedisTemplate.execute(scriptFind, Collections.emptyList(),tokenId,isWeb);
    }



    private static final SecureRandom rnd = new SecureRandom();





    public String createAuthentication(RequestDTO requestDTO) {



        String playerId = requestDTO.authenticatorId;


        RequestCloudDto requestCloudDto = (RequestCloudDto) requestDTO;

        boolean isWeb = requestCloudDto.isWeb;

        String authenticator;

        while(true) {
            authenticator = generateToken();

            String tokenData = playerId + " " + requestCloudDto.isWeb + " " + "true";
             boolean hasSet = stringRedisTemplate.opsForValue().setIfAbsent(authenticator,tokenData, Duration.ofDays(3));
             if(hasSet) {
                 break;
             }
        }

        List<String> strings = List.of(authenticator,isWeb+"",playerId);

        stringRedisTemplate.execute(scriptAdd,Collections.emptyList(),strings);
        return authenticator;
    }



    static final String addTokenInRedis = luaCodeSplit + """
            
            local function getTokenData(val) 
            
            return redis.call('GET',val)
            
            end
            
            local tokenId = ARGV[1]
            local source = ARGV[2]
            
            local playerId = ARGV[3]
            
             local rawValues =  redis.call('GET',playerId)
            
             if not rawValues  or #rawValues == 0 then
             
             
             redis.call('SET',playerId,tokenId)
             
             return 
             end
             
              local listValue =  split(rawValues)
              
              
              local finalValue = tokenId
              
              for i = 1 , #listValue do
              
               local tokenDataRaw =  getTokenData(listValue[i])
               
               if tokenDataRaw  then
               
               -- retrieve token data 1 element will be the player id second element will be the source ex : true if web
               
                local tokenData =  split(tokenDataRaw)  
               
                
                   if tokenData[2] ~= source   then
                   finalValue = finalValue .. " " .. listValue[i]
                   
                   else 
                   
                   redis.call('DEL',listValue[i])
                   
                   end
                   
                end
                
                
               end
               
               redis.call('SET',playerId,finalValue)
          
            """;


    public void UnSetFlag(String tokenId) {
        stringRedisTemplate.execute(scriptSetUnBusy,Collections.emptyList(),tokenId);
    }

   private final static String unBusy = luaSetBusyFlag + """
           
           local tokenId = ARGV[1]
           
           return setFlag("false",tokenId)
           """;
   private final static RedisScript<String> scriptSetUnBusy = RedisScript.of(unBusy,String.class);
   private final  static RedisScript<String> scriptAdd = RedisScript.of(addTokenInRedis,String.class);
   private final static RedisScript<String> scriptFind = RedisScript.of(luaCodeFind, String.class);
}
