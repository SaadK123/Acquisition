import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class ConnectionService {

    EntityManager entityManager;
    PasswordEncoder passwordEncoder;
    TokenService tokenService;


    @Transactional
    public Response connectingThroughJWT(RequestDTO requestDTO) {
        Token token = tokenService.findToken(requestDTO);

        Player player = token.getPlayerRaw();

        return new Response(player.report(),new Status(200,"connected"));
    }



    @Transactional
    public Response connectingThroughAuth(String username,String password) {
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



        HashMap<String,Object> container = new HashMap<>();


        container.put("player",player.report());


        Token token = new Token(player);

        container.put("token",token.getId());
        return new Response(container, new Status(200,"connected"));
    }


    @Transactional
    public Response logOut(RequestDTO requestDTO) {
     Token token = tokenService.findToken(requestDTO);
     entityManager.remove(token);

     return new Response(null,new Status(200,"log out sucessfully"));
    }



}
