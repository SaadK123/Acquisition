import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "ameliorations")

@Getter
@Setter
public class Amelioration {


    @Id
    private String cle;


    @Column
    private double valeur;

}
