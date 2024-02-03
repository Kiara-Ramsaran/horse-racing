package horseracing;

public class Bet {
    private int betType;
    private double betAmount;
    private int horseSelected;
    
    public Bet(int betType, double betAmount, int horseSelected){
this.betType = betType;
this.betAmount = betAmount;
this.horseSelected = horseSelected;
    }
    public int getBetType() {
        return betType;
    }

    public int getSelectedHorse() {
        return horseSelected;
    }

    public double getAmount() {
        return betAmount;
    }
}


