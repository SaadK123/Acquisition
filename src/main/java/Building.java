import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "buildings")
@Getter
@Setter
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column
    private String type;



    @ManyToOne
    private Player player;


    @ManyToMany
    @JoinTable(
     name = "ameliorations_batiments",
      joinColumns = @JoinColumn(name = "batiment_id"),
      inverseJoinColumns = @JoinColumn(name = "amelioration_id")

    )
   private  List<Amelioration> ameliorations = new ArrayList<>();



}
