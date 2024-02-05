package horseracing;

import java.util.Scanner;

public class HorseRacing {

     public static void main(String[] args) {
        Scanner in = new Scanner(System.in);    
        HorseRacingHelper.prepareHorseRacingSimulation();
        double wallet = 0.0; // Represents a user's wallet accross all races.
        boolean askedWalletAmount = false;
        boolean gameOver = false;
        while(!gameOver){
            HorseRacingHelper.clearConsole();


            // Determining random values for number of horses in the race, race length and race terrain. 
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

            
            Race race = HorseRacingHelper.createRace(numHorsesInRace, raceLength, raceTerrain);
            race.displayRaceInfo();
            // To ensure the user is asked for wallet amount the first time played only.
            if (!askedWalletAmount){
                wallet = askWalletAmount(); 
                askedWalletAmount = true;
            }
            // placeBets collects and validates betting input from the user.
            wallet = race.placeBets(wallet);
            race.startRace();
            System.out.println("Race is Over");
            // betResult calculates the result of the bet
            wallet = race.betResult(wallet);
            System.out.printf("Curent wallet amount is $%.2f.\n", wallet);
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
    // This method asks the user for wallet starting amount and validates the input ensuring a double value. 
    // Checks for non-numerical values and values less than 0.
    public static double askWalletAmount(){
        Scanner in = new Scanner(System.in);  
        boolean isDouble = false;
        double tempWallet = 0;
        double amountForWallet = 0;
        while(!isDouble){
            System.out.print("How much starting money do you want in your wallet?: $");
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
        amountForWallet = tempWallet;
        amountForWallet = ((int)(amountForWallet * 100)) / 100.00; // Forcing all decimals to be 2 decimal places.
        System.out.printf("Your wallet balance is $%.2f\n\n" , amountForWallet);
        return amountForWallet;
    }
}
