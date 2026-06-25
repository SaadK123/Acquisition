import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "market_investement")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class MarketInvestment {

    @Id

    private String  id;

    @Column(name = "price_per_stock")
    private double currentPricePerStock;


    @ElementCollection
    private List<Double> lastPrices;


    private int maxLastPrice = 5;



    public MarketInvestment(String id) {
        this.id = id;
        setPrice(100);
    }

    @Column(name = "growth_percentage")
    private double growthPercentage;

    @JsonIgnore
    public void setPrice(double price) {
        double difference =  price - currentPricePerStock;
        growthPercentage = difference / currentPricePerStock * 100;


        if(lastPrices.size() == maxLastPrice) {
            lastPrices.removeFirst();
        }
        lastPrices.add(currentPricePerStock);

        currentPricePerStock = price;
    }






}
