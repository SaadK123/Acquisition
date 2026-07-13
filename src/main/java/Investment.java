import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "investments")

@Getter
@Entity

@NoArgsConstructor
public class Investment {



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



}
