

public class ModifierReport  {



    private  String message;

    private double totalWon;

    private double totalLost;

    private double valuePercentage;

    private final Modifier modifier;


    public ModifierReport(double totalWon,double totalLost,double valuePercentage,Modifier modifier) {
        this.totalWon = totalWon;
        this.totalLost = totalLost;
        this.valuePercentage = valuePercentage;
        this.modifier = modifier;

        createMessage();
    }

    private void createMessage() {
        boolean ic =modifier.isIncome();
        message =  (ic ?  " you have just gained ":" you have paid ") + totalWon + " $, also"
                + (ic ? " you have lost from the profit ":" you have gained from paying ") + "this amount" + totalLost
        + " from the section called " + modifier.getKey();
    }





}
