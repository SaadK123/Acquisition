import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
/*
 * note :
 * THIS CLASS WILL CONTAIN A REDIS DB CONNECTION SO THAT IT CAN KEEP
 * TRACK OF ALL THREADS ACROSS ALL INSTANCES OF EVERY SERVER
 */

public class TokenBusyService {


    StringRedisTemplate stringRedisTemplate;

    public void verifyAndSetBusyToken(String tokenId) {

       boolean hasSet =  stringRedisTemplate.opsForValue().setIfAbsent(tokenId,"BUSY");

       if(!hasSet) {
           throw new AcquisitionException("impossible to make an action while the token is busy");
       }


    }


}
