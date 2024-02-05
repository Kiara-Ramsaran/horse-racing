package horseracing;

public class Bet { //Initializing bet class
    private int betType; //Type of bet
    private double betAmount; // How much money the user bet
    private int horseSelected; // What horse the user selected
    
    public Bet(int betType, double betAmount, int horseSelected){// Constructor
this.betType = betType;
this.betAmount = betAmount;
this.horseSelected = horseSelected;
    }
    //Getter methods
    public int getBetType() {
        return betType;
    }

    public int getSelectedHorse() {
        return horseSelected;
    }

    public double getAmount() {
        return betAmount;
    }
    //Setter methods
    public void setBetType(int newBetType) {
        betType = newBetType;
    }

    public void setSelectedHorse(int newHorseSelected) {
        horseSelected = newHorseSelected;
    }

    public void setAmount(double newBetAmount) {
        betAmount = newBetAmount;
    }
}


