import jakarta.persistence.*;

@Table(name = "investements")
public class Investement {



    @Id
    private String id;



    @ManyToOne
    private MarketInvestment marketInvestment;


    @Column
    private double stockBought;



}
