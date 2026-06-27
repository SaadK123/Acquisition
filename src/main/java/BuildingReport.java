import java.util.List;

public record BuildingReport(String buildingId,
                             String buildingName,
                             List<ModifierReport> profits,
                             List<ModifierReport> expenses,
                             double totalProfit,
                             double totalExpenses,
                             double netProfit
) {
}
