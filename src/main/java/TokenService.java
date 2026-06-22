import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

        // 3 Verify if the signature is valid with the good secret
        boolean verifySignature = verifySignature(tokenRaw);

        if(!verifySignature){
            return null;
        }
     return token;
    }


    String algorithm = "HmacSHA256";
    private boolean verifySignature(RequestDTO tokenRaw) {
        Mac mac;

        try {
           mac = Mac.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
          return false;
        }
        String secret = System.getenv("SpringBoot");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),algorithm);


        try {
            mac.init(secretKeySpec);
        } catch (Exception e) {
           return false;
        }

        String signatureRaw = tokenRaw.tokenId() + tokenRaw.timestamp();
        byte[] hmacBytes = mac.doFinal(signatureRaw.getBytes(StandardCharsets.UTF_8));

       return  MessageDigest.isEqual(hmacBytes,tokenRaw.signature().getBytes(StandardCharsets.UTF_8));
    }
}
