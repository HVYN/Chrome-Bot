
//  Side Project
//  Goal - Solve Dates given an input

//  GOAL REACHED -> DATES ARE ALL BEING SOLVED (05/21/2022)
//  NEW GOALS ->
//      a. OPTIMIZE ALGORITHM (To conserve HEAP memory)
//      b. IMPLEMENT IMAGE READER (So people literally don't have to input a cipher to use the bot)
//      c. CONVERT TO JAVASCRIPT? (Spread usable code everywhere for free is funny)

/*
    REPRESENT NODES / VERTICES / LOCATIONS
        WITH LETTERS AND NUMBERS

    LETTERS WILL REPRESENT ADJACENT ATTRIBUTES TO 'NODES'
        NODES ARE PLACES THE PLAYER CAN OCCUPY

    . - (TREES)
    X - (INACCESSIBLE)

    REFILL GAS
        G1, G2, G3 - (GAS) -> +100 GAS

    ONLY FOOD
        I - (ITALIAN) -> +60 FOOD
        P - (TACO) -> +60 FOOD

    ONLY DRINK
        J - (JUICE) -> +60 DRINK
        C - (COFFEE) -> +60 DRINK

    ONLY ENTERTAINMENT
        Q - (GARDEN) -> +100 ENTERTAINMENT
        M - (THEATER) -> +60 ENTERTAINMENT

    HYBRID
        D - (DANCE) -> +100 ENTERTAINMENT, -10 FOOD, -15 DRINK
        S - (SANDWICH) -> +40 FOOD, +20 DRINK
        B - (BAR) -> +40 DRINK, +40 ENTERTAINMENT
        F - (FAIR) -> +20 FOOD, +20 DRINK, +40 ENTERTAINMENT

    SPECIAL
        U - (MALL) -> +30 AFFECTION POINTS
        W - (RING) -> UNLOCK MARRIAGE OPTION
        H - (HOME) -> END DATE EARLY (AP DEDUCTED BASED ON TIME REMAINING)
        A - (AIRPORT) -> REROLL, -10 ENTERTAINMENT

 */

import java.util.Scanner;

public class DateFinisherDriver
{
    public static void main(String[] args)
    {
        Scanner userReader = new Scanner(System.in);

        while(true)
        {
            //  Prev. called 'mapEncoding'
            String userInput = "";

            //  Read input from the user.
            //      The user has the choice to enter the map encoding, or typing
            //      'quit' to exit the program.
            System.out.println("[*] PLEASE ENTER MAP ENCODING, OR 'QUIT' TO EXIT.");
            userInput = userReader.nextLine();

            //  Simple break statement to exit the program.
            if(userInput.toUpperCase().equals("QUIT"))
                break;

            DateMap dateMap;

            int mapEncodingNodeAmount = 0;

            for (String node : userInput.split("\\s+"))
                mapEncodingNodeAmount++;

            //  The number we do NOT want 'mapEncodingNodeAmount' to be equal to
            //      represents how many arguments we are expecting.
            while (mapEncodingNodeAmount < 83)
            {
                mapEncodingNodeAmount = 0;
                System.out.println("\n[*] MISSING ARGUMENTS! RE-INPUT MAP CODE ->");

                userInput = userReader.nextLine();

                for (String node : userInput.split("\\s+"))
                    mapEncodingNodeAmount++;
            }

            //  DEBUG: I printed out how many arguments there were
            //      because I'm bad at typing
            //  System.out.println(mapEncodingNodeAmount);

            dateMap = new DateMap(userInput);

            dateMap.solveDate();
            dateMap.displayHighestResult();
        }

        System.out.println("\n[*] EXITING.");
    }
}
