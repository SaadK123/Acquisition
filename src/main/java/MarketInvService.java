import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class MarketInvService {


    EntityManager entityManager;

    public MarketInvestment findMarket(String marketId) {
        try {
          return entityManager.createQuery("select m from MarketInvestment m where m.id = :id"
                  ,MarketInvestment.class).setParameter(marketId,"id").getSingleResult();
        }catch (Exception _) {}
       return null;
    }
}
