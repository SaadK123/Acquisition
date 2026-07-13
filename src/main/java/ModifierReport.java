import lombok.Getter;

@Getter
public class ModifierReport  {



    private  String message;

    private double totalMoney;

    private double lostValue;

    private double valuePercentage;

    private final Modifier modifier;


    public ModifierReport(double valuePercentage,Modifier modifier) {
        this.valuePercentage = valuePercentage;
        this.modifier = modifier;

        totalMoney = valuePercentage * modifier.getValue();

        lostValue = totalMoney / valuePercentage;

        createMessage();
    }

    private void createMessage() {
        boolean ic =modifier.isIncome();
        message =  (ic ?  " you have just gained ":" you have paid ") + totalMoney + " $, also"
                + (ic ? " you have lost from the profit ":" you have gained from paying ") + "this amount" + lostValue
        + " from the section called " + modifier.getKey();
    }





}
