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


    public Race(List<Horse> horses, double raceLength, String raceSurface) {
        this.horses = horses;
        this.raceLength = raceLength;
        this.raceSurface = raceSurface;
        this.currentHorse = 0;
        this.results = new ArrayList<Horse>();
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
/*
    public void displayHorseTable(){
        System.out.printf("|%-10s|%-20s|%-15s|%-15s|%-15s|%-15s|%-15s|%-15s|%-15s|\n", "#","Horse Name", "Dirt Rating (%)", "Grass Rating (%)", "Mud Rating (%)", "Preferred Length", "Odds for Win", "Odds for Place", "Odds for Show");
        
        for (int i = 0; i < horses.size(); i++) {   // iterates through the horses list
            Horse horse = horses.get(i);
            int oddsValue = odds(horse);
            String s0 = "" + (i + 1);
            String s1 = "" + horse.getName();
            String s2 = "" + horse.getDirtRating();
            String s3 = "" + horse.getGrassRating();
            String s4 = "" + horse.getMudRating();
            String s5 = "" + horse.getPreferredLength();
            String s6 = "" + fraction(oddsValue);
            String s7 = "";
            String s8 = "";

        
    
            System.out.println("+----------+--------------------+---------------+----------------+---------------+---------------+--------------+---------------+---------------+");
            System.out.printf("|%-10s|%-20s|%-15s|%-15s|%-15s|%-15s|%-15s|%-15s|%-15s|\n", s0, s1, s2, s3, s4, s5, s6, s7, s8);
        }
        System.out.println("+----------+--------------------+---------------+----------------+---------------+---------------+--------------+---------------+---------------+");
    }
*/



     public void displayHorseTable(){
        //Title and Headings
        int terrainRating = 0;
        System.out.println("+-------------------------+-----+-----+-----+-----+-----+");
        System.out.printf("|%-25s|%5s|%5s|%5s|%5s|%5s|\n", "Horse Name", raceSurface, "Leng", "Win", "Place", "Show");

        for (int i = 0; i < horses.size(); i++) {   // iterates through the horses list
            Horse horse = horses.get(i);
            int oddsValue = odds(horse);
            int c0 = horse.getNumber();
            String c1 = horse.getName();
            double c3 = horse.getPreferredLength();
           String c4 = "" + fraction(oddsValue); 
           //String c5 = "" + Odds for Place
           //String c6 = "" + Odds for Show
            if (raceSurface.equalsIgnoreCase("grass"))
                terrainRating = horse.getGrassRating();
            else if (raceSurface.equalsIgnoreCase("dirt"))
                terrainRating = horse.getDirtRating();
            else if (raceSurface.equalsIgnoreCase("mud"))
                terrainRating = horse.getMudRating();

            System.out.println("+-------------------------+-----+-----+-----+-----+-----+");
            //Add s6, s7, and s8 below
            System.out.printf("|%-25s|%5s|%5s|%5s|%5s|%5s|\n",c0+". "+c1, terrainRating, c3, c4, "place", "show");
        }
        System.out.println("+-------------------------+-----+-----+-----+-----+-----+");
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
       return (int)(Math.random() * d);
    }
   public int odds(Horse horse) {
        
        int c = (int)(7 - Math.abs(horse.getPreferredLength() - this.raceLength));

        if (raceSurface.equalsIgnoreCase("grass"))
            c += horse.getGrassRating() * 10 / 2;
        else if (raceSurface.equalsIgnoreCase("dirt"))
            c += horse.getDirtRating() * 10 / 2;
        else if (raceSurface.equalsIgnoreCase("mud"))
            c += horse.getMudRating() * 10 / 2;
        float total =  (c + (horse.getGrassRating() * 10 + horse.getDirtRating() * 10 + horse.getMudRating() * 10) / 12 );
        float oddsValue =  Math.round((2 * Math.pow(100 / total, 2)) - 1);
    return (int) oddsValue;
       
    }
    public String fraction(int oddsValue) {
        return oddsValue + "-1";
    }
   
    private void resetHorses() {
        for (Horse horse : horses) {
            horse.resetCurrenPosition();
        }
    }
    public void placeBets(){
        boolean continueBetting = true;
        Scanner in = new Scanner(System.in);
        boolean isDouble = false;
        double tempWallet = 0;
        double wallet = 0;
        while(!isDouble){
            System.out.print("How much starting money do you want in your wallet ?: $");
            isDouble = in.hasNextDouble();
            if (!isDouble){
                System.out.println("Invalid Input. Wallet amount must be a numerical value.");
                in.nextLine();
            }
            else if (isDouble){
                tempWallet = in.nextDouble();
                if (tempWallet<=0){
                    System.out.println("Invalid Input. Wallet amount must be greater than $0.");
                    isDouble=false;
                    in.nextLine();
                }
            }
        }
        wallet = tempWallet;
        System.out.printf("Your wallet balance is $%.2f\n" , wallet);
        wallet = ((int)(wallet * 100)) / 100.00; // Forcing all decimals to be 2 decimal places.
        in.nextLine(); 
        System.out.println("Types of Bets:");
        System.out.println("1. Win: A bet on a horse to finish first.");
        System.out.println("2. Place: A bet placed on a horse to finish either first or second.");
        System.out.println("3. Show: A bet placed on a horse to finish first, second, or third.");
        while (continueBetting) {
            System.out.print("Do you want to make a bet (y/n)?: ");
            String yn = in.nextLine();
            System.out.println("yn = '"+ yn + "'");
            if (yn.equalsIgnoreCase("y")||yn.equalsIgnoreCase("n")){
                if (yn.equalsIgnoreCase("n")){
                    continueBetting = false;
                }
                //start
                if (yn.equalsIgnoreCase("y")){
                    String betType;
                    boolean isValid = false;
                    while(!isValid){
                        System.out.print("What type of bet do you want to make?");
                        betType = in.nextLine();
                        if (!betType.equals("1")&&!betType.equals("2")&&!betType.equals("3")){
                            System.out.println("Invalid Input. Must input 1,2,or 3.");
                        }
                        else{
                            isValid = true;
                        }
                    }
                    boolean validEntry=false;
                    while(!validEntry){
                        System.out.print("Which horse do you want to bet on? : ");
                        String input = in.nextLine();
                        for(int i = 1 ; i<=numHorses()&&!validEntry;i++){
                            if(input.equals(""+i)){
                                validEntry = true;
                             }
                        }
                        if (!validEntry){
                            System.out.println("Invalid Input horse does not exist.");
                        }
                    }
                }
            }
            else{
                System.out.println("Invalid Input. Must be y or n.");
            }
        }
    }
}
