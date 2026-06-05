
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {
    EntityManager entityManager;
    public Building findBuilding(String buildingId) {
        try {
           return  entityManager.createQuery("select b from Building b where b.id = :buildingId",Building.class)
                    .setParameter("buildingId",buildingId).getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }
}
