import org.apache.el.parser.Token;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TokenLocalService extends TokenService {

    private AtomicBoolean inUse;


    // key
    private String tokenId;

    private String playerId;


    /**
     * used in oneTime
     * @param
     * @return
     */


    public boolean setToBusy() {
        return inUse.compareAndSet(false,true);
    }

    public String createAuthentication(RequestDTO requestDTO) {


        if(setToBusy()) {
            tokenId = generateToken();
            playerId = requestDTO.authenticatorId;
        }

        return tokenId;
    }

    public String findPlayerWithAuth(RequestDTO requestDTO) {
        if(setToBusy()) {
            return playerId;
        }
        return  null;
    }


    public void UnSetFlag(String authenticator) {
       inUse.compareAndSet(true,false);
    }
}
