package horseracing;

import java.util.ArrayList;
import java.util.List;

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

    public void displayHorseTable(){
        System.out.printf("|%-15s|%-20s|%-15s|%-15s|%-15s|%-15s|%-15s|%-15s|%-15s|\n", "#","Horse Name", "Dirt Rating (%)", "Grass Rating (%)", "Mud Rating (%)", "Preferred Length", "Odds for Win", "Odds for Place", "Odds for Show");
        for (int i = 0; i < horses.size(); i++) {   // iterates through the horses list
            Horse horse = horses.get(i);
            String s0 = "" + (i + 1);
            String s1 = "" + horse.getName();
            String s2 = "" + horse.getDirtRating();
            String s3 = "" + horse.getGrassRating();
            String s4 = "" + horse.getMudRating();
            String s5 = "" + horse.getPreferredLength();
            String s6 = "" + fraction(odds(horse));
            String s7 = "" ;
            String s8 = "";

            
            System.out.println("+---------------+--------------------+---------------+----------------+---------------+---------------+--------------+---------------+---------------+");
            System.out.printf("|%-15s|%-20s|%-15s|%-15s|%-15s|%-15s|\n", s0, s1, s2, s3, s4, s5, s6, s7, s8);
        }
        System.out.println("+---------------+--------------------+---------------+----------------+---------------+---------------+--------------+---------------+---------------+");
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
    public String fraction ( int oddsValue){

    return oddsValue + "-1";
    }

   
    private void resetHorses() {
        for (Horse horse : horses) {
            horse.resetCurrenPosition();
        }
    }
}
