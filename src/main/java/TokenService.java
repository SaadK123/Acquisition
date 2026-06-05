import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;

@Service
public class TokenService {


    EntityManager entityManager;


    // TODO ALL NULL MUST BE REPLACED BY Exceptions
    @Transactional
    public Token findToken(RequestDTO tokenRaw) {
        // 1 test if the time is t0o far away
        if(Utilitaries.getDeltaTime(tokenRaw.timestamp()) >= 5000) {
            return null;
        }

        // 2 verify if the token is valid
         Token token;
        try {
            token = entityManager.createQuery("select t from Token t where t.id = :tokenId",Token.class)
                    .setParameter("tokenId",tokenRaw.tokenId()).getSingleResult();

            if(!token.isTokenValid()) {return null;}
        } catch (Exception e) {
            return null;
        }

         Mac mac = Utilitaries.mac;
        
    }
}
