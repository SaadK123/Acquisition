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
public class Building {


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
    private List<Modifier> modifiers = new ArrayList<>();


    public BuildingProfile getBuildingProfile() {


    }
    @ManyToOne
    private Player player;


}
