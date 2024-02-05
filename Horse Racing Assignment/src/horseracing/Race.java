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
        this.userBet = new Bet(0, 0, 0); // Creates new bet from bet class
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
            String c6 = "" + winOddsValue + "-1"; // Gets oddsValue and displays in table in the form of int-1
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

    /* Determines how quickly a horse moves across the race track.
    *  Influences are the horse's rating on the current race terrain, preferred length reletive to the race length.
    *  The higher the rating and the lower the different between preferred length and race length, the quicker the hrse will move.
    */
    public int getIncrementForHorse(Horse horse) {

        int d = (int)(7 - Math.abs(horse.getPreferredLength() - this.raceLength));
        // using the horse's rating for the current surface type
        if (raceSurface.equalsIgnoreCase("grass"))
            d += horse.getGrassRating() / 2;
        else if (raceSurface.equalsIgnoreCase("dirt"))
            d += horse.getDirtRating() / 2;
        else if (raceSurface.equalsIgnoreCase("mud"))
            d += horse.getMudRating() / 2;
       return (int)((Math.random() * d)+1);
    }


    public int winOdds(Horse horse) { //Calculations for win odds
        
        int c = (int)(7 - Math.abs(horse.getPreferredLength() - this.raceLength));

        if (raceSurface.equalsIgnoreCase("grass"))
            c += horse.getGrassRating() * 10 / 2; 
        else if (raceSurface.equalsIgnoreCase("dirt"))
            c += horse.getDirtRating() * 10 / 2;
        else if (raceSurface.equalsIgnoreCase("mud"))
            c += horse.getMudRating() * 10 / 2;
        double total =  (c + (horse.getGrassRating() * 10 + horse.getDirtRating() * 10 + horse.getMudRating() * 10) / 12 ); //Multiplying ratings by 10 to properly do calculation
        double winOddsValue =  Math.round((2 * Math.pow(100 / total, 2)) - 1); // Equation found by trying different numbers from the example odds 
        if (winOddsValue < 1.0){ 
            winOddsValue = 1.0;
            } // Making sure the odds aren't 0 or less
        return (int) winOddsValue; // Returning the odds as an int, so it will be in the form of int - 1
    }

    
    public int placeOdds (Horse horse){ // Calculations for place odds
    int winoddsValue = winOdds(horse);
    double placeOddsValue = (winoddsValue * 0.8); // Calculating placeOdds by getting 80% of winOdds
    if (placeOddsValue < 1.0){ 
    placeOddsValue = 1.0;
    }
    return (int) placeOddsValue;
    }
    

    public int showOdds (Horse horse){ // Calculations for show odds
    int winoddsValue = winOdds(horse);
    double showOddsValue = (winoddsValue * 0.6); // Calculating showOdds by getting 60% of winOdds 
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
//Calculates how much money the user earns from their bets, based on if they chose win, place or show
// It then adds the money earned to wallet, then returns it to the placeBets method to be updated 

    public double betAmount(double wallet){ 
        Bet userBet = this.userBet; //New bet from bet class
     for (Horse horse : getHorses()){ // Gets the list of horses
        if (horse.getNumber() == userBet.getSelectedHorse() && results.indexOf(horse) == 0 && userBet.getBetType() == 1){//Checks if the horse the user selected has won and if the user has selected winodds
        int winOddsValue = winOdds(horse);
        wallet += winOddsValue * userBet.getAmount();// If the following is true, adds the winOdds x  amount the user bet to the wallet
         } else if (horse.getNumber() == userBet.getSelectedHorse() && results.indexOf(horse) <= 1 && userBet.getBetType() == 2){ // Checks if the horse user selected placed first or second and has selected placeodds
            int placeOddsValue = placeOdds(horse);
            wallet += placeOddsValue * userBet.getAmount(); // If the following is true, adds the placeOdds x  amount the user bet to the wallet
         } else if (horse.getNumber() == userBet.getSelectedHorse() && results.indexOf(horse) <= 2 && userBet.getBetType() == 3){ // Checks if horse user selected was first, second or third, and user selected showodds
            int showOddsValue = showOdds(horse); 
            wallet += showOddsValue * userBet.getAmount();// If the following is true, adds showodds x amount the user bet to the wallet
        }
      } 
    return wallet; // returns wallet to placeBets method
    }

    /* placeBets method interacts with the user to determine the bet type, horse, and amount of money they would like to place.  
     * Displays all bet types with explanation.
     * Validates all input. bet type must be option 1,2, or 3. Placing a bet must be y or n. Horse choice must be a number assigned to a horse. Bet ammount must be a number greater than 0.
     * Updates wallet amount after bet has been placed. Validates user has enough money in their wallet to place a bet. If not they can only view the race.
     */
    public double placeBets(double wallet){
        boolean continueToGetBet = true;
        String betType = "";
        String horseChoice = "";
        double betAmount = 0.0;
        Scanner in = new Scanner(System.in);
        // Displaying a description of win, place, show.
        System.out.println("Types of Bets:");
        System.out.println("1. Win: A bet on a horse to finish first.");
        System.out.println("2. Place: A bet placed on a horse to finish either first or second.");
        System.out.println("3. Show: A bet placed on a horse to finish first, second, or third.");
        System.out.println();
        // Checking if the user has any money to place a bet, if not they can view the race without placing a bet.
        if(wallet<=0){
        System.out.println("Your wallet is empty, sorry you can't place a bet.");
        System.out.println("Sit back and enjoy viewing the race anyway.");
        }
        // Prompt the user for bet information unless they choose not to make a bet or we have already collected all information for a bet.
        while (continueToGetBet&&wallet>0) {
            System.out.print("Do you want to make a bet (y/n)?: ");
            String yn = in.nextLine();
            // Checking if user entered y or n, which is a valid input.
            if (yn.equalsIgnoreCase("y")||yn.equalsIgnoreCase("n")){
                if (yn.equalsIgnoreCase("n")){
                    continueToGetBet = false; // User chooses not to make a bet.
                }
                // User chooses to make a bet.
                if (yn.equalsIgnoreCase("y")){
                    boolean isValid = false; // Type of bet is not valid until user input is validated.
                    System.out.println();
                    while(!isValid){
                        System.out.print("What type of bet do you want to make?: ");
                        betType = in.nextLine();
                        // If bet type does not equal 1,2, or 3, input is invalid.
                        if (!betType.equals("1")&&!betType.equals("2")&&!betType.equals("3")){
                            System.out.println("Invalid Input. Must input 1,2,or 3.");
                        }
                        else{
                            isValid = true;
                        }
                    }
                    // 
                    isValid = false; // Horse selected is not valid until user input is validated.
                    System.out.println();
                    while(!isValid){
                        System.out.print("Which horse do you want to bet on?: ");
                        horseChoice = in.nextLine();
                        // Checking if the user input matches a valid horse selection from displayRaceTable().
                        for(int i = 1 ; i<=numHorses()&&!isValid;i++){
                            if(horseChoice.equals(""+i)){
                                isValid = true;
                             }
                        }
                        if (!isValid){
                            System.out.println("Invalid Input horse does not exist.");
                        }
                    }
                    isValid = false; // Bet amount is not valid until the user input is validated.
                    System.out.println();
                    while(!isValid){
                        System.out.printf("Your wallet has $%.2f. How much would you like to bet?: $", wallet);
                        isValid = in.hasNextDouble();
                        // Checking if the user input is a numerical value.
                        if (!isValid){
                            System.out.println("Invalid Input. Bet amount must be a numerical value.");
                            in.nextLine();
                        }
                        // At this point the user input is a numerical value. Now validate numerical value.
                        else if (isValid){
                            betAmount = in.nextDouble();
                            // Checking if the user placed a bet less than of equal to 0.
                            if (betAmount<=0){
                                System.out.println("Invalid Input. Bet amount must be greater than $0.");
                                isValid=false;
                                in.nextLine();
                            }
                            // Checking if the user is trying to bet more than they have in their wallet.
                            else if(betAmount>wallet){
                                System.out.printf("Insufficient Funds! Bet amount must be less than or equal to $%.2f.\n", wallet);
                                isValid=false;
                                in.nextLine(); 
                            }
                        }
                    }
                    //Updating bet object with user input for bet type, horse choice, and bet amount.
                    userBet.setBetType(Integer.parseInt(betType));
                    userBet.setSelectedHorse(Integer.parseInt(horseChoice));
                    userBet.setAmount(betAmount);
                    // Deducting the bet placed amount from the user's wallet.
                    wallet-=betAmount;
                    System.out.printf("Your wallet balance is now $%.2f\n\n" , wallet);
                    wallet = ((int)(wallet * 100)) / 100.00; // Forcing all decimals to be 2 decimal places.
                    continueToGetBet = false; // We now have validated user input for bet type, horse choice, and bet amount.
                }
            }
            else{
                System.out.println("Invalid Input. Must be y or n.");
            }
            
        }
        return wallet;
    }
}
