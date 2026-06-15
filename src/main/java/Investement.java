import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "investements")

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Investement {



    @Id
    private String id;

    @ManyToOne
    private MarketInvestment marketInvestment;


    private  double firstInvestment;

    @Column
    private double totalInvestment;





    public void addMoney(double money) {
        this.totalInvestment += money;
    }


}
