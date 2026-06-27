import java.util.List;

public record BuildingDTO(String id, double price, List<Modifier> upgrades, List<Modifier> costs, String type) {
}
