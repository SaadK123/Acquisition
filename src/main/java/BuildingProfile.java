import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;

import java.util.List;

public record BuildingProfile(String buildingId,String buildingName,List<ModifierReport> modifiersReport)
{ }
