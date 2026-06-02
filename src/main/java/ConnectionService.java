import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;




@Service
public class ConnectionService {

    EntityManager entityManager;

    @Transactional
    public PlayerDTO connectingThroughJWT(String tokenId) {
      Token token = entityManager.createQuery("select t from Token t where  t.id = :id",Token.class)
              .setParameter("id",tokenId).getSingleResult();

      Player player = token.getPlayerRaw();


      if(player == null) {
          entityManager.remove(token);
          return null;
      }
      return player.getPlayerInformations();
    }






}
