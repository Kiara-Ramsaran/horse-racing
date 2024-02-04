package horseracing;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Race {
    private List<Horse> horses;
    private double raceLength; // in furlongs
    private String raceSurface; // "grass", "dirt", or "mud" (Uses HorseRacingHelper constants)
    private int currentHorse;

    private List<Horse> results;
    private Bet userBet; 


    public Race(List<Horse> horses, double raceLength, String raceSurface) {
        this.horses = horses;
        this.raceLength = raceLength;
        this.raceSurface = raceSurface;
        this.currentHorse = 0;
        this.results = new ArrayList<Horse>();
        this.userBet = new Bet(0, 0, 0); 
    }


    public List<Horse> getHorses() {
        return horses;
    }


    public int numHorses(){
        return horses.size();
    }


    public Horse getCurrentHorse(){
        return horses.get(currentHorse);
    }


    public Horse getNextHorse(){
        if (currentHorse == horses.size())
            currentHorse = 0;
        
        return horses.get(currentHorse++);
    }


    public double getRaceLength() {
        return raceLength;
    }


    public String getRaceSurface() {
        return raceSurface;
    }


    public void displayHorseTable(){
        //Title and Headings
        System.out.println("+-------------------------+-----+-----+-----+-----+-----+-----+-----+");//Format for table in beginning
        System.out.printf("|%-25s|%5s|%5s|%5s|%5s|%5s|%5s|%5s|\n", "Horse Name", "Grass", "Mud", "Dirt", "Leng", "Win", "Place", "Show");

        for (int i = 0; i < horses.size(); i++) {   // iterates through the horses list
            Horse horse = horses.get(i);
            int winOddsValue = winOdds(horse);
            int placeOddsValue = placeOdds(horse);
            int showOddsValue = showOdds(horse);
            int c0 = horse.getNumber();
            String c1 = horse.getName();
            double c2 = horse.getPreferredLength();
            String c3 = "" + horse.getGrassRating();
            String c4 = "" + horse.getMudRating();
            String c5 = "" + horse.getDirtRating();
            String c6 = "" + winOddsValue + "-1"; 
            String c7 = "" + placeOddsValue + "-1";
            String c8 = "" + showOddsValue + "-1";
            System.out.println("+-------------------------+-----+-----+-----+-----+-----+-----+-----+");
            System.out.printf("|%-25s|%5s|%5s|%5s|%5s|%5s|%5s|%5s|\n",c0+". "+c1, c3, c4, c5, c2, c6, c7, c8 );
        }
        System.out.println("+-------------------------+-----+-----+-----+-----+-----+-----+-----+");
    }


    public void displayRaceInfo() {
        System.out.println("Race Information:");
        System.out.println("Race Surface: " + raceSurface);
        System.out.println("Race Length: " + raceLength + " furlongs");
        System.out.println("List of Horses:");
        // for (Horse horse : horses) {
        //     System.out.println("- " + horse.getName());
        // }
        displayHorseTable();
    }


    public void displayResults(){
        System.out.println("\n\nRace Results");
        System.out.println("------------");
        for(int i=0; i<results.size(); i++){
            System.out.println((i+1) + ": " + results.get(i).getName() + "("+results.get(i).getNumber()+")");
        }
    }


    public void startRace(){
        resetHorses();
        int numSpaces = (int)(raceLength*10);
        boolean done = false;
        HorseRacingHelper.pauseForMilliseconds(1000);
        HorseRacingHelper.playBackgroundMusicAndWait("Race.wav");
        HorseRacingHelper.playBackgroundMusic("horse_gallop.wav", true);

        
        while(!done){
            HorseRacingHelper.pauseForMilliseconds(40);
            HorseRacingHelper.clearConsole();
            HorseRacingHelper.updateTrack(numSpaces, horses);
            Horse horse = getNextHorse();
           

            if(!horse.raceFinished() && horse.getCurrentPosition() >= numSpaces){
                results.add(horse);
                horse.setRaceFinished(true);
            } else if(!horse.raceFinished()){
                horse.incrementPosition(getIncrementForHorse(horse));
            }

            displayResults();

            if (results.size() == horses.size())
                done = true;
        }
//call bet display. Display odds and display how much they won
        HorseRacingHelper.stopMusic();
    }
    // Other methods for simulating the race, calculating winners, etc., can be added as needed


    public int getIncrementForHorse(Horse horse) {

        int d = (int)(7 - Math.abs(horse.getPreferredLength() - this.raceLength));

        if (raceSurface.equalsIgnoreCase("grass"))
            d += horse.getGrassRating() / 2;
        else if (raceSurface.equalsIgnoreCase("dirt"))
            d += horse.getDirtRating() / 2;
        else if (raceSurface.equalsIgnoreCase("mud"))
            d += horse.getMudRating() / 2;
        // this.raceSurface
       return (int)((Math.random() * d)+1);
    }


    public int winOdds(Horse horse) { 
        
        int c = (int)(7 - Math.abs(horse.getPreferredLength() - this.raceLength));

        if (raceSurface.equalsIgnoreCase("grass"))
            c += horse.getGrassRating() * 10 / 2; 
        else if (raceSurface.equalsIgnoreCase("dirt"))
            c += horse.getDirtRating() * 10 / 2;
        else if (raceSurface.equalsIgnoreCase("mud"))
            c += horse.getMudRating() * 10 / 2;
        double total =  (c + (horse.getGrassRating() * 10 + horse.getDirtRating() * 10 + horse.getMudRating() * 10) / 12 ); //Multiplying ratings by 10 to properly do calculation
        double winOddsValue =  Math.round((2 * Math.pow(100 / total, 2)) - 1); //Calculating win odds
        if (winOddsValue < 1.0){ 
            winOddsValue = 1.0;
            }
        return (int) winOddsValue;
    }

    
    public int placeOdds (Horse horse){
    int winoddsValue = winOdds(horse);
    double placeOddsValue = (winoddsValue * 0.8);
    if (placeOddsValue < 1.0){ //Making sure the odds aren't 0 or less
    placeOddsValue = 1.0;
    }
    return (int) placeOddsValue;
    }
    

    public int showOdds (Horse horse){
    int winoddsValue = winOdds(horse);
    double showOddsValue = (winoddsValue * 0.6);
    if (showOddsValue < 1.0){ 
        showOddsValue = 1.0;
    }
    return (int) showOddsValue;
    }
   

    private void resetHorses() {
        for (Horse horse : horses) {
            horse.resetCurrenPosition();
        }
    }


    public double betResult(double wallet){
        Bet userBet = this.userBet;
     for (Horse horse : getHorses()){
        if (horse.getNumber() == userBet.getSelectedHorse() && results.indexOf(horse) == 0 && userBet.getBetType() == 1){
        int winOddsValue = winOdds(horse);
        wallet += winOddsValue * userBet.getAmount();
         } else if (horse.getNumber() == userBet.getSelectedHorse() && results.indexOf(horse) <= 1 && userBet.getBetType() == 2){
            int placeOddsValue = placeOdds(horse);
            wallet += placeOddsValue * userBet.getAmount();
         } else if (horse.getNumber() == userBet.getSelectedHorse() && results.indexOf(horse) <= 2 && userBet.getBetType() == 3){
            int showOddsValue = showOdds(horse);
            wallet += showOddsValue * userBet.getAmount();
        }
      } 
    return wallet;
    }


    public double placeBets(double wallet){
        boolean continueBetting = true;
        String betType = "";
        String horseChoice = "";
        double betAmount = 0.0;
        Scanner in = new Scanner(System.in);
        System.out.println("Types of Bets:");
        System.out.println("1. Win: A bet on a horse to finish first.");
        System.out.println("2. Place: A bet placed on a horse to finish either first or second.");
        System.out.println("3. Show: A bet placed on a horse to finish first, second, or third.");
        System.out.println();
        if(wallet<=0){
        System.out.println("Your wallet is empty, sorry you can't place a bet.");
        }
        while (continueBetting&&wallet>0) {
            System.out.print("Do you want to make a bet (y/n)?: ");
            String yn = in.nextLine();
            if (yn.equalsIgnoreCase("y")||yn.equalsIgnoreCase("n")){
                if (yn.equalsIgnoreCase("n")){
                    continueBetting = false;
                }
                if (yn.equalsIgnoreCase("y")){
                    boolean isValid = false;
                    System.out.println();
                    while(!isValid){
                        System.out.print("What type of bet do you want to make?: ");
                        betType = in.nextLine();
                        if (!betType.equals("1")&&!betType.equals("2")&&!betType.equals("3")){
                            System.out.println("Invalid Input. Must input 1,2,or 3.");
                        }
                        else{
                            isValid = true;
                        }
                    }
                    boolean validEntry=false;
                    System.out.println();
                    while(!validEntry){
                        System.out.print("Which horse do you want to bet on?: ");
                        horseChoice = in.nextLine();
                        for(int i = 1 ; i<=numHorses()&&!validEntry;i++){
                            if(horseChoice.equals(""+i)){
                                validEntry = true;
                             }
                        }
                        if (!validEntry){
                            System.out.println("Invalid Input horse does not exist.");
                        }
                    }
                    boolean isDouble = false;
                    System.out.println();
                    while(!isDouble){
                        System.out.print("How much would you like to bet?: $");
                        isDouble = in.hasNextDouble();
                        if (!isDouble){
                            System.out.println("Invalid Input. Bet amount must be a numerical value.");
                            in.nextLine();
                        }
                        else if (isDouble){
                            betAmount = in.nextDouble();
                            if (betAmount<=0){
                                System.out.println("Invalid Input. Bet amount must be greater than $0.");
                                isDouble=false;
                                in.nextLine();
                            }
                            else if(betAmount>wallet){
                                System.out.println("Insufficient Funds! Bet amount must be less than or equal to $" + wallet + ".");
                                isDouble=false;
                                in.nextLine(); 
                            }
                        }
                    }
                    //Updating bet object with user input for bet type, horse choice, and bet amount.
                    userBet.setBetType(Integer.parseInt(betType));
                    userBet.setSelectedHorse(Integer.parseInt(horseChoice));
                    userBet.setAmount(betAmount);
                    wallet-=betAmount;
                    System.out.printf("Your wallet balance is now $%.2f\n\n" , wallet);
                    wallet = ((int)(wallet * 100)) / 100.00; // Forcing all decimals to be 2 decimal places.
                    in.nextLine();
                    continueBetting = false;
                }
            }
            else{
                System.out.println("Invalid Input. Must be y or n.");
            }
            
        }
        return wallet;
    }
}
