import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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



    @ManyToOne
    private Player player;



    @Column
    private double price;

    @OneToMany
   private  List<Modifiers> modifiers = new ArrayList<>();



    @Override
    public BuildingDTO toDto() {
        return new BuildingDTO(id,price,modifiers);
    }
}
