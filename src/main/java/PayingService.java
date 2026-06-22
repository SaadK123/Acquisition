import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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



    }

    private List<InvestmentReport> getAllInvestmentsReport() {

    }


    private List<BuildingReport> getAllBuildingsIncomeAndExpenses(List<Building> buildings) {
        List<BuildingReport> buildingReports = new ArrayList<>();

        for(Building building : buildings) {
          buildingReports.add(getAllBuildingIncomeAndExpense(building));
        }
        return buildingReports;
    }


    private BuildingReport getAllBuildingIncomeAndExpense(Building building) {
         var tuple_expenses = getBuildingIncomeOrExpense(building.getCosts(),false);
         var tuple_profits = getBuildingIncomeOrExpense(building.getUpgrades(),true);

         double totalMoney = tuple_profits.second - tuple_expenses.second;

         building.getPlayer().addMoney(totalMoney);
         

         return new BuildingReport(building.getId(), building.getOriginName(),tuple_profits.first,
                 tuple_expenses.first, tuple_profits.second, tuple_expenses.second, totalMoney);
    }


    private Tuple<List<Report>,Double> getBuildingIncomeOrExpense(List<Modifiers> modifiers,boolean isIncome) {
        double totalMoney = 0;
        List<Report> reports = new ArrayList<>();
        for(var modifier : modifiers) {
          Report report =  getIncome(modifier,isIncome);
          totalMoney += report.moneyWon();
          reports.add(report);
        }
        return new Tuple<>(reports,totalMoney);
    }

    private Report getIncome(Modifiers modifiers,boolean isIncome) {
        double lostPercentage = randomChance();

        double lostMoney = lostPercentage * modifiers.getValue();

        double totalMoney = isIncome ? modifiers.getValue() - lostMoney : modifiers.getValue() + lostMoney;

        String message = (isIncome ? "you gained " : "you paid ") + totalMoney + "$" + " from " + modifiers.getKey() ;

        if (lostMoney > 0) {
            message += "you" + (isIncome ? " lost money from this gain":"you paid more this time") + lostMoney + "$";
        }

        return new Report(totalMoney,message,lostMoney, modifiers.getKey());

    }
    final static Random rnd = new Random();

    private static double randomChance() {
        int random = rnd.nextInt(0,100);
        double gainPercentage;
        if(random < 70) {
            gainPercentage = 0;
        }else if(random < 85) {
            gainPercentage = 0.10;
        } else if (random < 95) {
            gainPercentage = 0.40;
        }else {
            gainPercentage = 0.80;
        }
        return gainPercentage;
    }


}
