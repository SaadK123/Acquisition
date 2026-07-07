import lombok.Getter;

public class BuildingReport   {

    private final BuildingProfile buildingProfile;


    @Getter
    private final double totalProfit,totalExpenses,netProfit;

    public BuildingReport(BuildingProfile buildingProfile,double totalProfit,
                          double totalExpenses) {
        this.totalProfit = totalProfit;
        this.totalExpenses = totalExpenses;


        netProfit = totalProfit - totalExpenses;

        this.buildingProfile = buildingProfile;
    }

}
