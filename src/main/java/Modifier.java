import com.mongodb.client.model.ValidationAction;
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
public class Modifier  implements IDto<ModifierReport> {


    @Id
    private String key;


    @Column
    private double value;


    @Column

    private boolean isIncome;

    public ModifierReport toDto() {
        double lostPercentage = Utilitaries.randomChance();

        double lostMoney = lostPercentage * value;

        double totalMoney = isIncome ? value - lostMoney : value + lostMoney;

        String message = (isIncome ? "you gained " : "you paid ") + totalMoney + "$" + " from " + key ;

        if (lostMoney > 0) {
            message += "you" + (isIncome ? " lost money from this gain":"you paid more this time") + lostMoney + "$";
        }

        return new ModifierReport(totalMoney,message,lostMoney, key);
    }

}
