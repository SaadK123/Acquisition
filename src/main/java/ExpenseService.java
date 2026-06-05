import jakarta.transaction.Transactional;
import org.apache.coyote.Request;
import org.springframework.stereotype.Service;

import java.lang.foreign.PaddingLayout;

@Service
public class ExpenseService {

    TokenService tokenService;

    BuildingService buildingService;
    @Transactional
    public Response addMoneyToPlayer(RequestDTO tokenRaw, double money) {
       Token token = tokenService.findToken(tokenRaw);
       Player player = token.getPlayerRaw();

       player.addMoney(money);

       return new Response(null,new Status(200,"successfully added" + money));
    }



    public Response buyBuilding(RequestDTO tokenRaw,String buildingId) {
      Token token = tokenService.findToken(tokenRaw);

      Player player = token.getPlayerRaw();

      Building building = buildingService.findBuilding(buildingId);

      if(building.getPrice() > player.getMoney()) {
          // return error message
      }
      // add the building and return response good 
    }
}
