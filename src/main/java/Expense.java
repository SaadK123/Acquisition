import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "expenses")

@Getter
@Setter
@Entity
public class Expense {


    @Id
    private String key;


    @Column
    private double value;

}
