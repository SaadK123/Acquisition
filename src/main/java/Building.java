import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "buildings")
@Getter
@Setter

@Entity
public class Building implements IDto<BuildingReport> {


    // nom du joueur + nom du building
    @Id
    private String id;


    @Column
    private String originName;

    public Building() {
    originName = id.split("-")[1];
    }

    @Column
    private String type;


    @Column
    private double price;

    @OneToMany
    private List<Modifier> upgrades = new ArrayList<>();


    @OneToMany

    private List<Modifier> costs = new ArrayList<>();

    @ManyToOne
    private Player player;





    public BuildingReport toDto() {
        var tuple_expenses = getBuildingIncomeOrExpense(costs);
        var tuple_profits = getBuildingIncomeOrExpense(upgrades);

        double totalMoney = tuple_profits.second - tuple_expenses.second;

        player.addMoney(totalMoney);


        return new BuildingReport(id,originName,tuple_profits.first,
                tuple_expenses.first, tuple_profits.second, tuple_expenses.second, totalMoney);
    }


    private Tuple<List<ModifierReport>,Double> getBuildingIncomeOrExpense(List<Modifier> modifiers) {
        double totalMoney = 0;
        List<ModifierReport> reports = new ArrayList<>();
        for(var modifier : modifiers) {
            ModifierReport report =  modifier.toDto();
            totalMoney += report.moneyWon();
            reports.add(report);
        }
        return new Tuple<>(reports,totalMoney);
    }

}
