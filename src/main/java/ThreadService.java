import org.springframework.stereotype.Service;

@Service
public class ThreadService {


    private record  NotificationThread(String threadId,long timeConnection) {}
}
