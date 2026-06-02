import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "player")
@Entity

@Getter
public class Player {

    @Column
    private String timeStart;

    @Column
    private long lastTimeConnected;

    public Player() {

        timeStart = Utilitaries.convertLongToDate(Utilitaries.now());
    }

    @Setter
    @Column(unique = true)
    private String username;


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID  id;



    @Setter
    @OneToMany
    private List<Building> buildings = new ArrayList<>();


    @Setter
    @Column
    private String passwordHash;


    @Column
    private double money;


    @OneToMany
    private List<Investement> investement;



    public PlayerDTO getPlayerInformations() {
        return new PlayerDTO();
    }
}
