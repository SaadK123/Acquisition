import org.hibernate.dialect.aggregate.HANAAggregateSupport;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@Service
public class TokenLocalService extends TokenService {

    private AtomicBoolean inUse;


    // key
    private String tokenId;

    private String playerId;





    public interface Decode {

        ///  0 : for continue ; 1 : make new value ; null for no data in the file (file size = 0)
        default Integer decodeFile(StringBuilder str,int addedData,int position) {
            if(position == 0 && addedData == (char)-1) {
                 return null;
            }
            if(addedData == (int) ':') {
                return 1;
            }

            if(addedData != ' ') {
                str.append((char) addedData);
            }
            return  0;
        }
    }


    public void lock() {
        boolean hasChanged = inUse.compareAndSet(false,true);

        if(!hasChanged) {
            throw  new AcquisitionException("the token is currently in use");
        }
    }

    public String createAuthentication(RequestDTO requestDTO) {
          lock();

          if(tokenId == null) {

              tokenId = generateToken();
              playerId = requestDTO.authenticatorId;
          }

        return tokenId;
    }

    public String findPlayerWithAuth(RequestDTO requestDTO) {
        lock();

        return playerId;
    }


    // unlock
    public void UnSetFlag(String authenticator) {
       inUse.compareAndSet(true,false);
    }
}
