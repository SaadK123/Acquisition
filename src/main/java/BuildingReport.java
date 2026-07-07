import lombok.AllArgsConstructor;

import java.util.List;


public class BuildingReport   {

    private final BuildingProfile buildingProfile;


    private final double totalProfit,totalExpenses,netProfit;

    public BuildingReport(BuildingProfile buildingProfile,double totalProfit,
                          double totalExpenses) {
        this.totalProfit = totalProfit;
        this.totalExpenses = totalExpenses;


        netProfit = totalProfit - totalExpenses;

        this.buildingProfile = buildingProfile;
    }
}
