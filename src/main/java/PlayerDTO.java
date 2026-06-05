import java.util.List;

public record PlayerDTO(
        String id,
        String username,
        double money,
        long lastTimeConnected,
        String firstTimeConnected,
        List<Building> buildings,
        List<Investement> investements,
        String tokenId) {
}