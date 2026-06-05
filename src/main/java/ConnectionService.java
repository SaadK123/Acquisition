import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class ConnectionService {

    EntityManager entityManager;
    PasswordEncoder passwordEncoder;

    @Transactional
    public PlayerDTO connectingThroughJWT(String tokenId) {
        Token token = findToken(tokenId);

        Player player = token.getPlayerRaw();

        if(player == null) {
            return null;
        }
        return player.getPlayerInformations(null);
    }


    private Token findToken(String tokenId) {
        Token token;
        try {
            token = entityManager.createQuery("select t from Token t where  t.id = :id",Token.class)
                    .setParameter("id",tokenId).getSingleResult();
        }catch (NoResultException _) {
            throw new AcquisitionException("token has not been found");
        }
        return token;
    }


    public PlayerDTO connectingThroughAuth(String username,String password) {
        Player player;
        try {
          player   = entityManager.createQuery("select p from Player p where p.username = :username",Player.class)
                    .setParameter("username",username).getSingleResult();
        } catch (NoResultException e) {
          throw new AcquisitionException("username or/and password is wrong");
        }

        boolean isSamePassword = passwordEncoder.matches(password, player.getPasswordHash());


        if(!isSamePassword) {
            throw new AcquisitionException("username or/and password is wrong");
        }

        return player.getPlayerInformations(UUID.randomUUID().toString());
    }


    public void logOutJWT(String tokenId) {
     
    }
}
