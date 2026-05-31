import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Table(name = "player")


@Getter
public class Player {

    @Column
    private  String timeStart;


    private String lastTimeConnected;

    public Player() {

        timeStart = Utilitaries.convertLongToDate(Utilitaries.now());
    }


    @Column(unique = true)
    private String username;



    @OneToMany
    private List<Building> buildings = new ArrayList<>();

    @Column()

    private String passwordHash;


    @Column()
    private double somme ;

}
