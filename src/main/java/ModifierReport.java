

public class ModifierReport extends GameReport {



    private String message;



    private ModifierReport(int valuePercentage,double value,boolean isIncome,String key) {
     super();



         message = (isIncome ? "you gained " : "you paid ") + totalMoney + "$" + " from " + key ;

        if (lostMoney > 0) {
            message += "you" + (isIncome ? " lost money from this gain":"you paid more this time") + lostMoney + "$";
        }
    }



    public static ModifierReport createModifier(int valuePercentage,double value,boolean isIncome,String key) {



        double totalMoney = valuePercentage * value;

        double totalCorrectedMoney = isIncome ? totalMoney : -totalMoney;

        double totalMoneyLost = totalMoney / 

    }


}
