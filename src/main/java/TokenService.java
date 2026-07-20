public  abstract class TokenService {


    public abstract String createAuthentication(RequestDTO requestDTO);

    public abstract String findPlayerWithAuth(RequestDTO requestDTO);
}
