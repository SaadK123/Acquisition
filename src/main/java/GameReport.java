public class GameReport {

    private final  double totalProfit;

    private final double totalExpenses;

    private final double netProfit;

    public GameReport(double totalProfit,double totalExpenses) {
        this.totalProfit = totalProfit;
        this.totalExpenses = totalExpenses;
        this.netProfit = totalProfit - totalExpenses;
    }
}
