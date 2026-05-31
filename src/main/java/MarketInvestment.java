import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Table(name = "market_investement")
public class MarketInvestment {

    @Id
    private String id;

    @Column(name = "price_per_stock")
    private double pricePerStock;


    public MarketInvestment(String id,double initialPrice) {
        setPrice(initialPrice);
        this.id = id;
    }


    public void setPrice(double price) {
        pricePerStock = price;
    }
}
