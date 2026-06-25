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
public class Player implements IDto<PlayerDTO> {

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
    private List<Investment> investements;





    @Override
    public PlayerDTO toDto() {
        return new PlayerDTO(id.toString(),username,money,lastTimeConnected,timeStart,
                buildings,investements);
    }




    public Investment addInvestment(MarketInvestment marketInvestment, double firstInvestment) {

        String idInvestment = marketInvestment.getId() + id;

        Investment investement = new Investment(idInvestment,marketInvestment,firstInvestment);
        investements.add(investement);


        return investement;
    }

    public Investment findInvestementByMarket(MarketInvestment marketInvestment) {
        for(int i = 0; i < investements.size(); ++i) {
            Investment investement =  investements.get(i);
            MarketInvestment currentMarket =  investement.getMarketInvestment();

            if(currentMarket.getId().equals(marketInvestment.getId())) {
                return investement;
            }
         }
        return null;
    }


}
