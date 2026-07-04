import lombok.AllArgsConstructor;

import java.util.List;


public class BuildingReport  extends GameReport  {

    private final BuildingProfile buildingProfile;


    public BuildingReport(BuildingProfile buildingProfile,double totalProfit,
                          double totalExpenses) {
        super(totalProfit,totalExpenses);
        this.buildingProfile = buildingProfile;
    }
}
