import lombok.Getter;

@Getter
public class InvestmentReport {
        String investmentId;
        double netIncome;
        double lastMoney;
        double currentMoney;
        double growthPercentage;


        public InvestmentReport(Investment investment) {
               this.investmentId = investment;

               
        }
}
