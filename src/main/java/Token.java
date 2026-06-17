import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Table(name = "token")
@Entity
public class Token {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Getter
    @Column(name = "expired_at")
    private long expiredAt;

    public Token(Player player) {
        expiredAt = Utilitaries.now() + Utilitaries.timeToken;
        this.player =  player;
    }


    public Token() {

    }



    // todo maybe in the near future use a ManyToOne (no conflict between tokens from web and c#)
    @OneToOne(optional = false)
    private Player player;


    public Player getPlayerRaw()  throws IllegalArgumentException {
        if(Utilitaries.now() >= expiredAt) {
          return null;
        }
      return player;
    }


    public boolean isTokenValid() {
        return getPlayerRaw() != null;
    }
}
