import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BuyingService {

    TokenService tokenService;

    BuildingService buildingService;

    EntityManager entityManager;

    public Response buyInvestment(RequestDTO tokenRaw,String market) {
        Token token = tokenService.findToken(tokenRaw);
        MarketInvestment marketInvestment;
        try {
         marketInvestment = entityManager.
                    createQuery("select m from MarketInvestment m where m.id = :market"
                            ,MarketInvestment.class)
                    .setParameter("market",market).getSingleResult();
        } catch (Exception e) {
            return null;
        }


    }
    @Transactional
    public Response buyBuilding(RequestDTO tokenRaw,String buildingId) {
      Token token = tokenService.findToken(tokenRaw);

      Player player = token.getPlayerRaw();

      Building building = buildingService.findBuilding(buildingId);
      double price = building.getPrice();
      if(price > player.getMoney()) {
        return new Response(false,new Status(401,"not enough money"));
      }
      return buildingService.addBuildingToPlayer(player,building);
    }
}
