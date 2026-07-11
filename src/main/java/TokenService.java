import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {







    // TODO ALL NULL MUST BE REPLACED BY Exceptions
    @Transactional
    public String findToken(RequestDTO tokenRaw) {
        String tokenId = tokenRaw.tokenId();

        
    }




    private record TokenMetaData(String playerId,long timeExpiration,boolean isWeb) {}

    private final ConcurrentHashMap<String,TokenMetaData> tokenToDataMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String,String> playerToTokensMap = new ConcurrentHashMap<>();





    private static SecureRandom rnd = new SecureRandom();


    public String generateToken(String playerId, boolean isWeb) {

        StringBuilder token = new StringBuilder();

        for(int i = 0; i < 300; ++i) {
           int randChar =  rnd.nextInt(33,127);

           char c = (char) randChar;

           token.append(c);
        }

        String tokenStringify = token.toString();



        return tokenStringify;
    }


    private void addToken(String token,String playerId,boolean isWeb) {

        // 1 : first find if player id has already a used token for either web or unity

         String tokensRaw = playerToTokensMap.get(playerId);

         if(tokensRaw == null) {
             addTokenInRegistries(token,isWeb,playerId,token);
             return;
         }

         String[] tokensIds = tokensRaw.split(" ");

         String compositeKey =  token + " ";

         for(String tokenId : tokensIds) {
             TokenMetaData currentTokenData = tokenToDataMap.get(tokenId);

             if(currentTokenData.isWeb() == isWeb) {
                 removeTokenFromRegistries(tokenId,playerId);

             }else {
                 compositeKey += tokenId;
             }
         }

         addTokenInRegistries(token,isWeb,playerId,compositeKey);

    }
    private void addTokenInRegistries(String tokenId, boolean isWeb, String playerId, String compositeKey) {
        tokenToDataMap.put(tokenId,new TokenMetaData(playerId,Utilitaries.nowToken(), isWeb));
        playerToTokensMap.put(playerId,compositeKey);
    }

    private void removeTokenFromRegistries(String token, String playerId) {

        // remove from token to data registry

        tokenToDataMap.remove(token);

        //  get from player to tokens  registry

        String[] tokensId = playerToTokensMap.get(playerId).split(" ");


        // check if one token and if yes  remove all the registry
        if(tokensId.length == 1) {
            playerToTokensMap.remove(playerId);
        }else {
            // else keep the token out
            String keeperToken =  token.equals(tokensId[0]) ?  tokensId[1]:tokensId[0];
            playerToTokensMap.put(playerId,keeperToken);
        }

    }







}
