import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BuyingService {

    TokenService tokenService;

    BuildingService buildingService;

    MarketInvService marketInvService;

    EntityManager entityManager;

    @Transactional
    public Response buyInvestment(RequestDTO tokenRaw,String market,double amount) {
        Token token = tokenService.findToken(tokenRaw
         );

        Player player = token.getPlayerRaw();

        entityManager.refresh(player, LockModeType.PESSIMISTIC_WRITE);

        if (amount <= 0) {
            return new Response("invalid amount", new Status(400, "amount must be positive"));
        }

        if(player.getMoney() < amount) {
            return new Response("not enough money",new Status(400,"illegal business action"));
        }


        MarketInvestment marketInvestment =  marketInvService.findMarket(market);



        if(marketInvestment == null) {
            return new Response("market invalid",new Status(400,"no such thing"));
        }


        Investement investement = player.findInvestementByMarket(marketInvestment);


        if(investement != null) {
           investement.addMoney(amount);
        }else {
            // if no investemnt exist
            investement =  player.addInvestment(marketInvestment, amount);
        }
        player.addMoney(-amount);
        return new Response(investement,new Status(200,"investment was created"));
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
