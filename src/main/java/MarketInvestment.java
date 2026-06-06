import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.service.annotation.DeleteExchange;

import java.util.List;
import java.util.Stack;

@Table(name = "market_investement")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class MarketInvestment {

    @Id

    private String  id;

    @Column(name = "price_per_stock")
    private double pricePerStock;


    @ElementCollection
    private List<Double> lastPrices;

    public MarketInvestment(String id,double initialPrice) {
        setPrice(initialPrice);
        this.id = id;
    }

    @JsonIgnore
    public void setPrice(double price) {
        pricePerStock = price;
    }
}
