import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayingService {

    TokenService tokenService;

     public PayingService(TokenService tokenService) {
        this.tokenService = tokenService;
     }

     public PlayerReport payPlayer(RequestDTO requestDTO) {
        Token token =  tokenService.findToken(requestDTO);
        Player player = token.getPlayerRaw();

    }

    private PlayerReport getPlayerReport() {

    }

    private List<InvestmentReport> getAllInvestmentsReport(List<Investment> investments) {
        List<InvestmentReport> investmentReports = new ArrayList<>();
        for(var investment : investments) {
            investmentReports.add(investment.report());

        }
        return investmentReports;
    }



}
