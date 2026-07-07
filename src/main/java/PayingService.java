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

    }



    /**
    SECTION 1 :
    CONTAINS BUILDING REPORTS

    BUILDING HAVE MODIFIERS SO THESE TWO METHODS ARE LINKED TO THE SAME
    SECTION.
     */
    private List<BuildingReport> buildingReports(List<Building> buildings) {
         List<BuildingReport> buildingReports = new ArrayList<>();
         for(Building building : buildings) {
             
         }
    }

    private Tuple<Double,List<ModifierReport>>  modifiersReports(List<Modifier> modifiers,boolean isIncome) {
        List<ModifierReport> modifierReports = new ArrayList<>();

        double netProfitBuilding = 0;
         for(Modifier modifier : modifiers) {
             double valuePercentage = Utilitaries.randomChance();

             double totalWon = valuePercentage * modifier.getValue();

             netProfitBuilding += modifier.isIncome() ? totalWon:-totalWon;

             double totalLost = totalWon / valuePercentage;

             modifierReports.add(new ModifierReport(totalWon,totalLost,valuePercentage,modifier));
         }

         return new Tuple<>(netProfitBuilding,modifierReports);
    }

    /* END OF SECTION 1*/







}
