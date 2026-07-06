import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Table(name = "buildings")
@Getter
@Setter

@Entity
public class Building implements GameStateDTO<BuildingReport> {


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



    public BuildingReport report() {
        var tuple_expenses = getBuildingIncomeOrExpense(costs);
        var tuple_profits = getBuildingIncomeOrExpense(upgrades);
        return new BuildingReport(createReport(tuple_profits.first,tuple_expenses.first),
                tuple_profits.second, tuple_expenses.second);
    }

    private BuildingProfile createReport(List<ModifierReport> upgrades,List<ModifierReport> costs) {

        List<ModifierReport> combined = Stream.concat(upgrades.stream(), costs.stream())
                .toList();
        return new BuildingProfile(id,originName,combined);
    }


    private Tuple<List<ModifierReport>,Double> getBuildingIncomeOrExpense(List<Modifier> modifiers) {
        double totalMoney = 0;
        List<ModifierReport> reports = new ArrayList<>();
        for(var modifier : modifiers) {
            ModifierReport report =  modifier.report();
            totalMoney += report.moneyWon();
            reports.add(report);
        }
        return new Tuple<>(reports,totalMoney);
    }

}
