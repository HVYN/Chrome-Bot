
//  Side Project
//  Goal - Solve Dates given an input

import java.util.ArrayList;
import java.util.Scanner;

/*
    REPRESENT NODES / VERTICES / LOCATIONS
        WITH LETTERS AND NUMBERS

    LETTERS WILL REPRESENT ADJACENT ATTRIBUTES TO 'NODES'
        NODES ARE PLACES THE PLAYER CAN OCCUPY

    T - (TREES)
    X - (FILLER)

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
        W - (RING) -> UNLOCK MARRIAGE
        H - (HOME) -> BAIL DATE
        A - (AIRPORT) -> REROLL, -10 ENTERTAINMENT

 */

public class DateFinisherDriver
{
    public static void main(String[] args)
    {
        // Initialize variables
        Scanner userReader = new Scanner(System.in);

        // DatePlayer datePlayer = new DatePlayer();

        // ArrayList abstractMap = new ArrayList();
        String mapEncoding = "";

        DateMap dateMap;

        // Read a Map Encoding from the User
        System.out.println("INPUT MAP CODE ->");
        mapEncoding = userReader.nextLine();
        int mapEncodingNodeAmount = 0;

        for(String node : mapEncoding.split("\\s+"))
            mapEncodingNodeAmount++;

        while(mapEncodingNodeAmount != 84)
        {
            mapEncodingNodeAmount = 0;
            System.out.println("MISSING ARGUMENTS! RE-INPUT MAP CODE ->");

            mapEncoding = userReader.nextLine();

            for(String node : mapEncoding.split("\\s+"))
                mapEncodingNodeAmount++;
        }

        System.out.println(mapEncodingNodeAmount);

        dateMap = new DateMap(mapEncoding);

        dateMap.solveDate();
        dateMap.displayHighestResult();
        // dateMap.displayResults();
    }
}