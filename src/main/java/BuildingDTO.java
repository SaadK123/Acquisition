import java.util.List;

public record BuildingDTO(String id, double price, List<Modifiers> upgrades,List<Modifiers> costs,String type) {
}
