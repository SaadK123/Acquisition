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
    private String originName;

    public Building() {
    originName = id.split("-")[1];
    }

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

}
