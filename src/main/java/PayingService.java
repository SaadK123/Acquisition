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
        Token token =  tokenService.findPlayerWithToken(requestDTO);
        Player player = token.getPlayerRaw();

        var tuple = buildingReports(player.getBuildings());

        var investmentReports = investmentReports(player.getInvestments());

        var totalMoney = tuple.first;


        return new PlayerReport(tuple.second,investmentReports,totalMoney);
    }



    /**
    SECTION 1 :
    CONTAINS BUILDING REPORTS

    NOTE :
    BUILDING HAVE MODIFIERS SO THESE TWO METHODS ARE LINKED TO THE SAME
    SECTION.
     */
    private Tuple<Double,List<BuildingReport>> buildingReports(List<Building> buildings) {
         List<BuildingReport> buildingReports = new ArrayList<>();

         double totalNetProfit = 0;
         for(Building building : buildings) {
             var tuple = modifiersReports(building.getModifiers());

             var salesInfo = tuple.first;

             var buildingReport = new BuildingReport(new BuildingProfile(building),
                     salesInfo.totalProfit, salesInfo.totalExpenses,tuple.second);

             totalNetProfit += buildingReport.getNetProfit();

             buildingReports.add(buildingReport);
         }
         return new Tuple<>(totalNetProfit,buildingReports);
    }


    private record SalesInfo(double totalProfit, double totalExpenses){}

    private Tuple<SalesInfo,List<ModifierReport>>  modifiersReports(List<Modifier> modifiers) {
        List<ModifierReport> modifierReports = new ArrayList<>();

        double totalExpenses = 0;

        double totalProfits = 0;

        for(Modifier modifier : modifiers) {
             double valuePercentage = Utilitaries.randomChance();

             var modifierReport  = new ModifierReport(valuePercentage,modifier);

             var totalMoney = modifierReport.getTotalMoney();

             if(modifier.isIncome()) {
                 totalProfits += totalMoney;
             }else {
                 totalExpenses += totalMoney;
             }


             modifierReports.add(modifierReport);
         }

         return new Tuple<>(new SalesInfo(totalProfits,totalExpenses),modifierReports);
    }

    /* END OF SECTION 1*/


    /**
     SECTION 2:
     CONTAINS INVESTMENTS REPORTS

     NOTE :
     N/A
     */

    private List<InvestmentReport> investmentReports(List<Investment> investments) {
        List<InvestmentReport> investmentReports = new ArrayList<>();

        for(Investment investment : investments) investmentReports.add(new InvestmentReport(investment));

        return investmentReports;
    }

    /*END OF SECTION 2*/


}
