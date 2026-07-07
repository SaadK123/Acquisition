import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "player")
@Entity
@Setter
@Getter
public class Player {

    @Column
    private String timeStart;

    @Column
    private long lastTimeConnected;

    public Player() {

        timeStart = Utilitaries.convertLongToDate(Utilitaries.now());
    }


    @Column(unique = true)
    private String username;


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID  id;



    @OneToMany
    private List<Building> buildings = new ArrayList<>();



    @Column
    private String passwordHash;


    @Column
    private double money;


    public void addMoney(double add) {money += add;}



    @OneToMany
    private List<Investment> investments;


    public Investment addInvestment(MarketInvestment marketInvestment, double firstInvestment) {

        String idInvestment = marketInvestment.getId() + id;

        Investment investment = new Investment(idInvestment,marketInvestment,firstInvestment);
        investments.add(investment);


        return investment;
    }

    public Investment findInvestementByMarket(MarketInvestment marketInvestment) {
        for (Investment investment : investments) {
            MarketInvestment currentMarket = investment.getMarketInvestment();

            if (currentMarket.getId().equals(marketInvestment.getId())) {
                return investment;
            }
        }
        return null;
    }

}
