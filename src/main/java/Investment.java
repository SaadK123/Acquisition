import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "investements")

@Getter
@Entity

@NoArgsConstructor
public class Investment implements IDto<InvestmentReport> {



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
    public InvestmentReport toDto() {
        double lastPreviousPrice =  marketInvestment.getLastPrices().getLast();

        double currentPrice =  marketInvestment.getCurrentPricePerStock();

        double previousStockPower = lastPreviousPrice * stockBought,
                currentStockPower = currentPrice * stockBought;

        double netIncome = currentStockPower - previousStockPower;

        double growth = netIncome / previousStockPower * 100;

        return new InvestmentReport(id,netIncome,previousStockPower,currentStockPower,growth);
    }
}
