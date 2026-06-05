import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "buildings")
@Getter
@Setter

@Entity
public class Building {


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
   private  List<Expense> ameliorations = new ArrayList<>();



}
