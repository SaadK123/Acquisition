import java.util.List;

public class PlayerProfile {

    List<BuildingProfile> buildingProfiles;


    List<InvestmentProfile> investmentProfiles;

    public PlayerProfile(Player player) {
        this.buildingProfiles = player.getAllBuildingsProfile();

    }
}
