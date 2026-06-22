import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "expenses")

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Modifiers {


    @Id
    private String key;


    @Column
    private double value;

}
