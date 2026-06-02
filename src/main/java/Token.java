import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Table(name = "token")
@Getter
@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;



    @Column(name = "expired_at")
    private long expiredAt;

    public Token(String requestedBy) {
        expiredAt = Utilitaries.now() + Utilitaries.timeToken;

    }


    public Token() {

    }


    @OneToOne
    private Player player;


    public Player getPlayerRaw()  throws IllegalArgumentException {
        if(Utilitaries.now() >= expiredAt) {
          throw new IllegalArgumentException("the token has expired");
        }
      return player;
    }
}
