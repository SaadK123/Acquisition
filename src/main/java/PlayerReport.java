import java.util.List;

public record PlayerReport(List<BuildingReport> buildingReports,List<InvestmentReport> investmentReports,double total) {
}
