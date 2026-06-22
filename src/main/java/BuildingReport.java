import java.util.List;

public record BuildingReport(String buildingId,
                             String buildingName,
                             List<Report> profits,
                             List<Report> expenses,
                             double totalProfit,
                             double totalExpenses,
                             double netProfit
) {
}
