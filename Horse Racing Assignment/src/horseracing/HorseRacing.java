package horseracing;

import java.util.Scanner;

public class HorseRacing {

     public static void main(String[] args) {
        Scanner in = new Scanner(System.in);    
        HorseRacingHelper.prepareHorseRacingSimulation();
        boolean gameOver = false;
        while(!gameOver){
            HorseRacingHelper.clearConsole();

            int numHorsesInRace = (int)(Math.random()*7)+5;
            int raceLength;
            int raceLengthRandom = (int)((Math.random()*3)); //gets random number from 0 to 2
            if (raceLengthRandom == 0)
                raceLength = HorseRacingHelper.SHORT;
            else if (raceLengthRandom == 1)
                raceLength = HorseRacingHelper.MIDDLE;
            else
                raceLength = HorseRacingHelper.LONG;

            int raceTerrain;
            int raceTerrainRandom = (int)((Math.random()*3)); //gets random number from 0 to 2
            if (raceTerrainRandom == 0)
                raceTerrain = HorseRacingHelper.GRASS;
            else if (raceTerrainRandom == 1)
                raceTerrain = HorseRacingHelper.DIRT;
            else
                raceTerrain = HorseRacingHelper.MUD;

            
// betting
            Race race = HorseRacingHelper.createRace(numHorsesInRace, raceLength, raceTerrain);
            race.displayRaceInfo();
//update wallet
            race.placeBets();
            race.startRace();
            System.out.println("Race is Over");
            gameOver = playAgain(in);
        }

        
    }

    private static boolean playAgain(Scanner in) {
        System.out.print("\u001B[?25l");  // Hide the cursor

        System.out.print("Play Again: (y/n): ");
        String result = in.nextLine();

        if (result.equals("n"))
            return true;

        return false;

    }
}
