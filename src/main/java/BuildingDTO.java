import java.util.List;

public record BuildingDTO(String id, double price, List<Modifiers> expenses,String type) {
}
