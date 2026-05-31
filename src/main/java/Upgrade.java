import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "upgrades")

@Getter
@Setter
public class Upgrade {


    @Id
    private String cle;


    @Column
    private double valeur;

}
