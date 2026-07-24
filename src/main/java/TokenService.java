import java.util.Random;

public  abstract class TokenService {


    public abstract String createAuthentication(RequestDTO requestDTO);

    public abstract String findPlayerWithAuth(RequestDTO requestDTO);

    public abstract void UnSetFlag(String authenticator);

      static final Random rnd = new Random();
    protected String generateToken() {

        StringBuilder token = new StringBuilder();

        for(int i = 0; i < 300; ++i) {
            int randChar =  rnd.nextInt(33,127);

            char c = (char) randChar;

            token.append(c);
        }
        return token.toString();
    }
}
