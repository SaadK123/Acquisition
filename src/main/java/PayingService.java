import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayingService {

    TokenService tokenService;

     public PayingService(TokenService tokenService) {
        this.tokenService = tokenService;
     }

     public PlayerReport payPlayer(RequestDTO requestDTO) {
        Token token =  tokenService.findToken(requestDTO);

        Player player = token.getPlayerRaw();

        List<BuildingReport> buildingReports = getAllBuildingsIncomeAndExpenses(player.getBuildings());

        List<InvestmentReport> investmentReports = getAllInvestmentsReport(player.getInvestements());


        return new PlayerReport(buildingReports,investmentReports);
    }



    private List<InvestmentReport> getAllInvestmentsReport(List<Investment> investments) {
         List<InvestmentReport> investmentReports = new ArrayList<>();
         for(var investment : investments) {
           investmentReports.add(investment.toDto());
         }
         return investmentReports;
    }


    private List<BuildingReport> getAllBuildingsIncomeAndExpenses(List<Building> buildings) {
        List<BuildingReport> buildingReports = new ArrayList<>();

        for(Building building : buildings) {
          buildingReports.add(building.toDto());
        }
        return buildingReports;
    }








}
