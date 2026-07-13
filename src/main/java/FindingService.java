import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class FindingService {


    EntityManager entityManager;
    public Player findPlayer(String playerId) {
        try {
          return entityManager.createQuery("select Player p from Player where p.id =: playerId",Player.class)
                  .setParameter("playerId",playerId).getSingleResult();
        } catch (Exception e) {
            throw new AcquisitionException("the player doesn't exist");
        }
    }
}
