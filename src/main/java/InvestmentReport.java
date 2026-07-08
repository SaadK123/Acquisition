import lombok.Getter;

@Getter
public class InvestmentReport {
        String investmentId;
        double netIncome;
        double lastMoney;
        double currentMoney;
        double growthPercentage;


        public InvestmentReport(Investment investment) {
               this.investmentId = investment.getId();

               MarketInvestment marketInvestment = investment.getMarketInvestment();

               currentMoney = investment.getStockBought() * marketInvestment.getCurrentPricePerStock();


               lastMoney = investment.getStockBought() * marketInvestment.getLastPrices().getLast();


               netIncome = currentMoney - lastMoney;


               growthPercentage = (currentMoney - lastMoney) / lastMoney;
        }
}
