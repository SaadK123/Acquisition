import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "investements")

@Getter
@Entity
public class Investement {



    @Id
    private String id;

    @ManyToOne
    private MarketInvestment marketInvestment;


    private double firstInvestment;

    @Column
    private double totalInvestment;




}
