import java.util.List;

public class InvestmentProfile {

    double moneyInvested;

    double stockBought;


    double currentPricePower;

    double lastPricePower;

    public InvestmentProfile(Investment investment) {
       this.moneyInvested = investment.getInvestmentMoney();
       this.stockBought = investment.getStockBought();


       this.currentPricePower = investment.getStockBought() * investment.getMarketInvestment().getCurrentPricePerStock();
       this.lastPricePower = investment.getStockBought() * investment.getMarketInvestment().getLastPrices().getLast();


    }
}
