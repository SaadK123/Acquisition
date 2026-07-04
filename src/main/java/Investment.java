import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "investments")

@Getter
@Entity

@NoArgsConstructor
public class Investment implements GameStateDTO<InvestmentReport> {



    @Id
    private String id;

    @ManyToOne
    private MarketInvestment marketInvestment;

    public Investment(String id, MarketInvestment marketInvestment, double firstInvestment) {
        this.id = id;
        this.marketInvestment  = marketInvestment;
        addStock(firstInvestment);
    }

    @Column(name = "money_invested")
    private double stockBought;




    public void addStock(double money) {
        double addStock = money / marketInvestment.getCurrentPricePerStock();
        this.stockBought += addStock;
    }


    @Override
    public InvestmentReport report() {
        double lastPreviousPrice =  marketInvestment.getLastPrices().getLast();

        double currentPrice =  marketInvestment.getCurrentPricePerStock();

        double previousNetWorth = lastPreviousPrice * stockBought,
                currentNetWorth = currentPrice * stockBought;

        double netIncome = currentNetWorth - previousNetWorth;

        double growth = netIncome / previousNetWorth * 100;

        return new InvestmentReport(id,netIncome, previousNetWorth, currentNetWorth,growth);
    }
}
