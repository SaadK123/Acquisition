
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {


    EntityManager entityManager;

    FindingService findingService;

    @Transactional
    public Building findBuilding(String buildingId) {
        try {
           return  entityManager.createQuery("select b from Building b where b.id = :buildingId",Building.class)
                    .setParameter("buildingId",buildingId).getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    @Transactional

    public Response addBuildingToPlayer(Player player,Building building) {
        if(building.getPlayer() == null) {
           player.getBuildings().add(building);
           building.setPlayer(player);

           entityManager.flush();

           return new Response(new BuyingBuildingDTO(building.getPrice(), building.getId()),
                   new Status(200,"was added"));
        }

        return new Response("building was not added",new Status(400,"error business"));
    }



    TokenCloudService tokenService;

    public Response getBuildingsFromPlayer(RequestDTO requestDTO) {
        String playerId = tokenService.findPlayerWithToken(requestDTO.tokenId(),requestDTO.forWeb());

        Player player = findingService.findPlayer(playerId);

        return new Response(player.getAllBuildingsProfile(),new Status(200,"sucess"));
    }



}
