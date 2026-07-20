import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class ConnectionService {

    EntityManager entityManager;
    PasswordEncoder passwordEncoder;
    TokenCloudService tokenService;


    @Transactional
    public Response connectingThroughJWT(RequestDTO requestDTO) {
        String playerId  = tokenService.findPlayerWithToken(requestDTO.tokenId(), requestDTO.forWeb());

        Player player = entityManager.createQuery("select Player p from Player  where p.id =: id",Player.class)
                .setParameter("id",playerId).getSingleResult();
        return new Response(player.,new Status(200,"connected"));
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
     Token token = tokenService.findPlayerWithToken(requestDTO);
     entityManager.remove(token);

     return new Response(null,new Status(200,"log out sucessfully"));
    }



}
