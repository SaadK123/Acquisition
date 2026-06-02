import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "buildings")
@Getter
@Setter

@Entity
public class Building {

    @Id
    private String id;


    @Column
    private String type;



    @ManyToOne
    private Player player;


    @OneToMany
   private  List<Upgrade> ameliorations = new ArrayList<>();



}
