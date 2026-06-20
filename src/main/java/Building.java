import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Table(name = "buildings")
@Getter
@Setter

@Entity
public class Building implements IDto<BuildingDTO> {


    // nom du joueur + nom du building
    @Id
    private String id;


    @Column
    private String type;


    @Column
    private double price;

    @OneToMany
    private List<Modifiers> upgrades = new ArrayList<>();


    @OneToMany

    private List<Modifiers> costs = new ArrayList<>();

    @ManyToOne
    private Player player;



    @Override
    public BuildingDTO toDto() {
        return new BuildingDTO(id,price,upgrades,costs,type);
    }


    private static final Random rnd = new Random();
    private List<Tuple<Double,String>> profits() {

        List<Tuple<Double,String>> profits = new ArrayList<>();
        for(Modifiers profit : upgrades) {
            profits.add(calculateRevenu(profit));
        }
        return profits;
    }


    private Tuple<Double,String> expenses() {

    }



    private static Tuple<Double,String> calculateRevenu(Modifiers profit) {
        double random = randomChance();

        return getResultFromRevenu(random,profit);

    }

    private static double randomChance() {
        int random = rnd.nextInt(0,100);
        double lostPercentage;
        if(random < 70) {
            lostPercentage = 0;
        }else if(random < 85) {
            lostPercentage = 0.10;
        } else if (random < 95) {
            lostPercentage = 0.40;
        }else {
            lostPercentage = 0.80;
        }
        return lostPercentage;
    }

    private static Tuple<Double,String> getResultFromRevenu(double lostPercentage,Modifiers profit) {
        double lostMoney = profit.getValue() * lostPercentage;

        double totalWon = profit.getValue() - lostMoney;


        return lostMoney > 0  ? new Tuple<>(totalWon,"you lost " + lostMoney + "$ from " + profit.getKey()) :
                new Tuple<>(totalWon,"you won "+ totalWon + "$ from " + profit.getKey());
    }


}
