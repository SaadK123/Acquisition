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
    TokenService tokenService;


    @Transactional
    public PlayerDTO connectingThroughJWT(RequestDTO requestDTO) {
        Token token = tokenService.findToken(requestDTO);

        Player player = token.getPlayerRaw();

        if(player == null) {
            return null;
        }
        return player.getPlayerInformations(null);
    }



    @Transactional
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


    @Transactional
    public void logOut(RequestDTO requestDTO) {
     Token token = tokenService.findToken(requestDTO);
     entityManager.remove(token);
    }



}
