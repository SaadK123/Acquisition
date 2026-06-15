import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BuyingService {

    TokenService tokenService;

    BuildingService buildingService;

    EntityManager entityManager;
    @Transactional
    public Response buyInvestment(RequestDTO tokenRaw,String market,double price) {
       Token token = tokenService.findToken(tokenRaw);

       Player player = token.getPlayerRaw();

       for(Investement investement : player.getInvestements()) {
           String marketId = investement.getMarketInvestment().getId();
           if(marketId.equals(market)) {
               investement.addMoney(price);
               return new Response(investement,new Status(200,"success"));
           }
       }

        MarketInvestment potentialNewMarket;
        try {
          potentialNewMarket = entityManager.createQuery("select m from MarketInvestment m where m.id = :market",
                           MarketInvestment.class)
                   .setParameter("market",market).getSingleResult();
       } catch (Exception e) {
           return new Response("market not found",new Status(404,"error not found"));
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
