import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;

import java.util.List;

public class BuildingProfile {

    private String originalName;

    private List<Modifier> modifiers;


    public BuildingProfile(Building building) {
     this.originalName = building.getOriginName();
     this.modifiers = building.getModifiers();
    }
}

