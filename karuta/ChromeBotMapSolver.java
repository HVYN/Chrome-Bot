package karuta;

//  ORIGINAL: DateMap.java

//  THIS CLASS IS THE MAIN SEQUENCE OF ACTIONS AIMED
//      AT SOLVING THE DATE MINIGAME.

//  (05/25/2022) MOVED/ALTERED THE ORIGINAL FILE TO NO LONGER
//      TAKE TEXT INPUT, SINCE THE 'karuta.ChromeBotMapParser' class
//      DOES THE PARSING WORK NOW.

import java.util.ArrayList;

public class ChromeBotMapSolver
{
    private final ArrayList<DateNode> nodeArray;

    private final int startingNodeNumber;

    //  DEBUG: When I was first programming the solver, I had the program store
    //      every result (Success and Failure), to check if it was being thorough.
    //  private LinkedList<karuta.DateResult> results;

    private DateResult highestResult;
    private DateResult highestResultWithRing;
    private DateResult highestResultWithAirport;

    private String playerDirection;

    private boolean ring;

    public ChromeBotMapSolver()
    {
        nodeArray = new ArrayList<>();

        //  NOTE: I used a Set to store paths because I thought it wouldn't
        //      store duplicates, but that doesn't apply because even if it's the
        //      same score, it takes a different path, so I was misguided in my
        //      attempts at conserving heap space.
        //  results = new LinkedList<>();
        //  pathsTaken = new LinkedList<>();

        highestResultWithRing = highestResult = highestResultWithAirport = null;

        //  STARTING NODE IS ALWAYS AT THE SAME LOCATION, SO WE CAN
        //      DEFINE IT EARLY
        startingNodeNumber = 79;

        ring = false;

        constructNodes();
        linkNodes();
    }

    //  NOTE: Starter function, jump starts its other counterpart.
    public void solveDate()
    {
        System.out.println("FACING " + playerDirection);

        solveDate(nodeArray.get(startingNodeNumber), playerDirection, 1,
                100, 50, 50, 75, 100,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                false, false, false,
                "");
    }

    //  NOTE: This version of 'solveDate' is the actual workhorse of the
    //      program, being called recursively to brute-force its way to the
    //      answer.

    /*
        NOTE: This overridden version of 'solveDate()' is the real Backtracking method that
            calls itself to find the best path.

            (Best paths are saved in their respective 'DateResult' variables)

            Parameters:
                'currentNode' (DateNode)
                    Representation of the current node the algorithm is at, complete with information
                    about the node's surroundings.
                'currentDirection' (String)
                    Identifier as to what direction the algorithm is currently facing; helps with
                    determining what paths/resources it can access at the moment.
                'turnNumber' (int)
                    Keep track of turn number (A dating game cannot go past 25 turns).
                'fuel', 'hunger', 'thirst', 'happiness', 'time' (int)
                    Counters for critical resources and time remaining in the game.
                REFRESH TIMERS (int)
                    Counters that serve as timers for resource cooldowns.
                'gardenVisited', 'mallVisited', 'ringVisited' (boolean)
                    Markers that indicate if the algorithm accessed these resources in its
                    pathfinding attempt(s).
                'path' (String)
                    Representation of the path the algorithm has taken.
     */
    public void solveDate(DateNode currentNode, String currentDirection, int turnNumber,
                          int fuel, int hunger, int thirst, int happiness, int time,
                          int juiceRefresh, int coffeeRefresh, int sandwichRefresh, int fairRefresh,
                          int spaghettiRefresh, int barRefresh, int theaterRefresh, int tacoRefresh,
                          int ballroomRefresh, int gasOneRefresh, int gasTwoRefresh, int gasThreeRefresh,
                          boolean gardenVisited, boolean mallVisited, boolean ringVisited,
                          String path)
    {
        //  MAX RESOURCE CAP IS 100; ADJUST IF THEY ARE OVER THE LIMIT TO
        //      AVOID ANY MISCALCULATIONS GOING FORWARD.
        if(hunger > 100)
            hunger = 100;
        if(thirst > 100)
            thirst = 100;
        if(happiness > 100)
            happiness = 100;

        //  IF RESOURCE IS ON COOLDOWN, DECREMENT BY ONE EVERY TURN.
        if(juiceRefresh > 0)
            juiceRefresh--;
        if(coffeeRefresh > 0)
            coffeeRefresh--;
        if(sandwichRefresh > 0)
            sandwichRefresh--;
        if(fairRefresh > 0)
            fairRefresh--;
        if(spaghettiRefresh > 0)
            spaghettiRefresh--;
        if(barRefresh > 0)
            barRefresh--;
        if(theaterRefresh > 0)
            theaterRefresh--;
        if(tacoRefresh > 0)
            tacoRefresh--;
        if(ballroomRefresh > 0)
            ballroomRefresh--;
        if(gasOneRefresh > 0)
            gasOneRefresh--;
        if(gasTwoRefresh > 0)
            gasTwoRefresh--;
        if(gasThreeRefresh > 0)
            gasThreeRefresh--;

        //  NOTE: This checks if the bot can still go on.
        //      a. Resources have not been depleted
        //      b. There is still time to run down
        if(time > 0 && fuel > 0 && happiness > 0 &&
                hunger > 0 && thirst > 0)
        {
            //  NOTE: IF bot is currently facing RIGHT/EAST
            //      The bot, in this state, can move UP/NORTH, RIGHT/EAST, or DOWN/SOUTH
            switch(currentDirection)
            {
                case "RIGHT" ->
                {
                    if (!(currentNode.getRightNorthNode() == null) &&
                            currentNode.getRightNorthNode().isAccessible())
                        solveDate(currentNode.getRightNorthNode(), "UP", turnNumber + 1,
                                fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                                juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                                spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                                ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                                gardenVisited, mallVisited, ringVisited,
                                //  path + " UP [" + currentNode.getRightNorthNode().getNodeNumber() + "] ");
                                path + " UP");

                    if (!(currentNode.getCentralEastNode() == null) &&
                            currentNode.getCentralEastNode().isAccessible())
                        solveDate(currentNode.getCentralEastNode(), currentDirection, turnNumber + 1,
                                fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                                juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                                spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                                ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                                gardenVisited, mallVisited, ringVisited,
                                //  path + " RIGHT [" + currentNode.getCentralEastNode().getNodeNumber() + "] ");
                                path + " RIGHT");

                    if (!(currentNode.getRightSouthNode() == null) &&
                            currentNode.getRightSouthNode().isAccessible())
                        solveDate(currentNode.getRightSouthNode(), "DOWN", turnNumber + 1,
                                fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                                juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                                spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                                ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                                gardenVisited, mallVisited, ringVisited,
                                //  path + " DOWN [" + currentNode.getRightSouthNode().getNodeNumber() + "] ");
                                path + " DOWN");
                }

                case "LEFT" ->
                {
                    if (!(currentNode.getLeftNorthNode() == null) &&
                            currentNode.getLeftNorthNode().isAccessible())
                        solveDate(currentNode.getLeftNorthNode(), "UP", turnNumber + 1,
                                fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                                juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                                spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                                ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                                gardenVisited, mallVisited, ringVisited,
                                //  path + " UP [" + currentNode.getLeftNorthNode().getNodeNumber() + "] ");
                                path + " UP");

                    if (!(currentNode.getCentralWestNode() == null) &&
                            currentNode.getCentralWestNode().isAccessible())
                        solveDate(currentNode.getCentralWestNode(), currentDirection, turnNumber + 1,
                                fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                                juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                                spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                                ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                                gardenVisited, mallVisited, ringVisited,
                                //   path + " LEFT [" + currentNode.getCentralWestNode().getNodeNumber() + "] ");
                                path + " LEFT");

                    if (!(currentNode.getLeftSouthNode() == null) &&
                            currentNode.getLeftSouthNode().isAccessible())
                        solveDate(currentNode.getLeftSouthNode(), "DOWN", turnNumber + 1,
                                fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                                juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                                spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                                ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                                gardenVisited, mallVisited, ringVisited,
                                //  path + " DOWN [" + currentNode.getLeftSouthNode().getNodeNumber() + "] ");
                                path + " DOWN");

                }

                case "UP" ->
                {
                    if (!(currentNode.getTopWestNode() == null) &&
                            currentNode.getTopWestNode().isAccessible())
                        solveDate(currentNode.getTopWestNode(), "LEFT", turnNumber + 1,
                                fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                                juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                                spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                                ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                                gardenVisited, mallVisited, ringVisited,
                                //  path + " LEFT [" + currentNode.getTopWestNode().getNodeNumber() + "] ");
                                path + " LEFT");

                    if (!(currentNode.getCentralNorthNode() == null) &&
                            currentNode.getCentralNorthNode().isAccessible())
                        solveDate(currentNode.getCentralNorthNode(), currentDirection, turnNumber + 1,
                                fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                                juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                                spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                                ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                                gardenVisited, mallVisited, ringVisited,
                                //  path + " UP [" + currentNode.getCentralNorthNode().getNodeNumber() + "] ");
                                path + " UP");

                    if (!(currentNode.getTopEastNode() == null) &&
                            currentNode.getTopEastNode().isAccessible())
                        solveDate(currentNode.getTopEastNode(), "RIGHT", turnNumber + 1,
                                fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                                juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                                spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                                ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                                gardenVisited, mallVisited, ringVisited,
                                //  path + " RIGHT [" + currentNode.getTopEastNode().getNodeNumber() + "] ");
                                path + " RIGHT");

                }

                case "DOWN" ->
                {
                    if (!(currentNode.getBottomWestNode() == null) &&
                            currentNode.getBottomWestNode().isAccessible())
                        solveDate(currentNode.getBottomWestNode(), "LEFT", turnNumber + 1,
                                fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                                juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                                spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                                ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                                gardenVisited, mallVisited, ringVisited,
                                //  path + " LEFT [" + currentNode.getBottomWestNode().getNodeNumber() + "] ");
                                path + " LEFT");

                    if (!(currentNode.getCentralSouthNode() == null) &&
                            currentNode.getCentralSouthNode().isAccessible())
                        solveDate(currentNode.getCentralSouthNode(), currentDirection, turnNumber + 1,
                                fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                                juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                                spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                                ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                                gardenVisited, mallVisited, ringVisited,
                                //  path + " DOWN [" + currentNode.getCentralSouthNode().getNodeNumber() + "] ");
                                path + " DOWN");
                    if (!(currentNode.getBottomEastNode() == null) &&
                            currentNode.getBottomEastNode().isAccessible())
                        solveDate(currentNode.getBottomEastNode(), "RIGHT", turnNumber + 1,
                                fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                                juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                                spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                                ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                                gardenVisited, mallVisited, ringVisited,
                                //  path + " RIGHT [" + currentNode.getBottomEastNode().getNodeNumber() + "] ");
                                path + " RIGHT");

                }
            }

            //  OBSERVE RESOURCES SURROUNDING CURRENT NODE
            //  AND ACT UPON THEM
            if(currentNode.hasResources())
            {
                //  JUICE RESOURCE
                if (currentNode.isNextToJuice() &&
                        juiceRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger - 4, thirst + 60 - 6, happiness - 8, time - 4,
                            11, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited, ringVisited,
                            path + " JUICE");
                }

                //  COFFEE RESOURCE
                if (currentNode.isNextToCoffee() &&
                        coffeeRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger - 4, thirst + 60 - 6, happiness - 8, time - 4,
                            juiceRefresh, 11, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited, ringVisited,
                            path + " COFFEE");
                }

                //  GAS (ONE) RESOURCE
                if (currentNode.isNextToGasOne() &&
                        gasOneRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            100, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, 11, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited, ringVisited,
                            path + " GAS1");
                }

                //  GAS (TWO) RESOURCE
                if (currentNode.isNextToGasTwo() &&
                        gasTwoRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            100, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, 11, gasThreeRefresh,
                            gardenVisited, mallVisited, ringVisited,
                            path + " GAS2");
                }

                //  GAS (THREE) RESOURCE
                if (currentNode.isNextToGasThree() &&
                        gasThreeRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            100, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, 11,
                            gardenVisited, mallVisited, ringVisited,
                            path + " GAS3");
                }

                //  SANDWICH RESOURCE
                if (currentNode.isNextToSandwich() &&
                        sandwichRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger + 40 - 4, thirst + 20 - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, 11, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited, ringVisited,
                            path + " SANDWICH");
                }

                //  FAIR RESOURCE
                if (currentNode.isNextToFair() &&
                        fairRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger + 20 - 4, thirst + 20 - 6, happiness + 40 - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, 11,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited, ringVisited,
                            path + " FAIR");
                }

                //  SPAGHETTI RESOURCE
                if (currentNode.isNextToSpaghetti() &&
                        spaghettiRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger + 60 - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            11, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited, ringVisited,
                            path + " ITALIAN");
                }

                //  BAR RESOURCE
                if (currentNode.isNextToBar() &&
                        barRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger - 4, thirst + 40 - 6, happiness + 40 - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, 11, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited, ringVisited,
                            path + " BAR");
                }

                //  THEATER RESOURCE
                if (currentNode.isNextToTheater() &&
                        theaterRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger - 4, thirst - 6, happiness + 60 - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, 11, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited, ringVisited,
                            path + " THEATER");
                }

                //  TACO RESOURCE
                if (currentNode.isNextToTaco() &&
                        tacoRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger + 60 - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, 11,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited, ringVisited,
                            path + " TACO");
                }

                //  BALLROOM RESOURCE
                if (currentNode.isNextToBallroom() &&
                        ballroomRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger - 10 - 4, thirst - 15 - 6, happiness + 100 - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            11, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited, ringVisited,
                            path + " BALLROOM");
                }

                //  GARDEN RESOURCE
                if (currentNode.isNextToGarden() &&
                        !gardenVisited)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger - 4, thirst - 6, happiness + 100 - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            true, mallVisited, ringVisited,
                            path + " GARDEN");
                }

                //  MALL RESOURCE
                if(currentNode.isNextToMall() &&
                        !mallVisited)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, true, ringVisited,
                            path + " MALL");
                }

                //  RING RESOURCE
                if(currentNode.isNextToRing() &&
                        !ringVisited)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited, true,
                            path + " RING");
                }
            }

            //  HOME PATH
            //  NOTE: CONSIDER HOME ENDING AS WELL, IN CASE IT GIVES THE HIGHEST POSSIBLE AP.
            if (currentNode.isNextToHome())
            {
                DateResult homeResult = new DateResult(ResultType.RETURNED_HOME, fuel, hunger, thirst, happiness, time, mallVisited, path + " HOME");

                if(ringVisited && (highestResultWithRing == null || homeResult.getAffectionPoints() > highestResultWithRing.getAffectionPoints()))
                {
                    highestResultWithRing = homeResult;
                    return;
                }

                if(highestResult == null || homeResult.getAffectionPoints() > highestResult.getAffectionPoints())
                    highestResult = homeResult;

            }

            //  AIRPORT PATH
            //  NOTE: CONSIDER AIRPORT ENDING, IN CASE THE BOARD DOES NOT HAVE A RING/THE USER
            //      WISHES TO RE-ROLL THE BOARD.
            if(currentNode.isNextToAirport())
            {
                //  DateResult airportResult = new DateResult(ResultType.REACHED_AIRPORT, calculateAffectionPoints(hunger, thirst, happiness, time, mallVisited), path + " AIRPORT");
                DateResult airportResult = new DateResult(ResultType.REACHED_AIRPORT, fuel, hunger - 4, thirst - 6, happiness - 8 - 10, time - 4, mallVisited, path + " AIRPORT");

                if((hunger - 4 > 12) && (thirst - 6 > 18) &&
                        (happiness - 8 - 10 > 24) && (fuel > 30))
                {
                    if(highestResultWithAirport == null || airportResult.getTime() > highestResultWithAirport.getTime())
                        highestResultWithAirport = airportResult;
                }
            }
        }
        else if(time <= 0)
        {
            //  NOTE: Running down the clock is the goal here, but if the user runs
            //      out of any resource after doing so, it will be a failure as well.
            if(fuel > 0 && happiness > 0 && hunger > 0 && thirst > 0)
            {
                DateResult successResult = new DateResult(ResultType.SUCCESS, fuel, hunger, thirst, happiness, time, mallVisited, path);

                if(ringVisited && (highestResultWithRing == null || successResult.getAffectionPoints() > highestResultWithRing.getAffectionPoints()))
                {
                    highestResultWithRing = successResult;
                    return;
                }

                if(highestResult == null || successResult.getAffectionPoints() > highestResult.getAffectionPoints())
                    highestResult = successResult;

            }

        }
    }

    //  NOTE: Instantiate all 82 nodes -> place them in ArrayList
    //      for ease of access/organization.
    private void constructNodes()
    {
        for(int node = 0; node < 82; node++)
            nodeArray.add(new DateNode(node));
    }

    //  NOTE: Link nodes together/make nodes 'see' their neighbors.
    //      (Whether or not the node is inaccessible or not will be
    //      defined/set later).
    private void linkNodes()
    {
        for(int index = 0; index < nodeArray.size(); index++)
        {
            // System.out.println("LINKING NODE " + index);

            DateNode currentNode = nodeArray.get(index);

            //  DIFFERENT LINKING PROCEDURES BASED ON WHETHER
            //  THE NODE IS A VERTICAL OR HORIZONTAL ROAD
            // if(isHorizontalNode(index))
            if(currentNode.isHorizontalNode())
            {
                if(index - 6 >= 0)
                    nodeArray.get(index).setLeftNorthNode(nodeArray.get(index - 6));
                if(index - 5 >= 0)
                    nodeArray.get(index).setRightNorthNode(nodeArray.get(index - 5));

                if(index + 5 <= 81)
                    nodeArray.get(index).setLeftSouthNode(nodeArray.get(index + 5));
                if(index + 6 <= 81)
                    nodeArray.get(index).setRightSouthNode(nodeArray.get(index + 6));

                if(index - 1 >= 0)
                    nodeArray.get(index).setCentralWestNode(nodeArray.get(index - 1));
                if(index + 1 <= 81)
                    nodeArray.get(index).setCentralEastNode(nodeArray.get(index + 1));
            }
            else if(currentNode.isVerticalNode())
            {
                if(index - 6 >= 0)
                    nodeArray.get(index).setTopWestNode(nodeArray.get(index - 6));
                if(index - 5 >= 0)
                    nodeArray.get(index).setTopEastNode(nodeArray.get(index - 5));

                if(index + 5 <= 81)
                    nodeArray.get(index).setBottomWestNode(nodeArray.get(index + 5));
                if(index + 6 <= 81)
                    nodeArray.get(index).setBottomEastNode(nodeArray.get(index + 6));

                if(index - 11 >= 0)
                    nodeArray.get(index).setCentralNorthNode(nodeArray.get(index - 11));
                if(index + 11 <= 81)
                    nodeArray.get(index).setCentralSouthNode(nodeArray.get(index + 11));
            }

            //  DESIGNATE OUT OF BOUNDS ADJACENCY
            //  IS NODE THE MOST NORTH OR SOUTH
            if(currentNode.isNodeMostNorth())
            {
                nodeArray.get(index).setLeftNorthNode(null);
                nodeArray.get(index).setCentralNorthNode(null);
                nodeArray.get(index).setRightNorthNode(null);
            }
            else if(currentNode.isNodeMostSouth())
            {
                nodeArray.get(index).setLeftSouthNode(null);
                nodeArray.get(index).setCentralSouthNode(null);
                nodeArray.get(index).setRightSouthNode(null);
            }

            //  IS NODE THE MOST EAST OR WEST
            if(currentNode.isNodeMostEast())
            {
                nodeArray.get(index).setTopEastNode(null);
                nodeArray.get(index).setCentralEastNode(null);
                nodeArray.get(index).setBottomEastNode(null);
            }
            else if(currentNode.isNodeMostWest())
            {
                nodeArray.get(index).setTopWestNode(null);
                nodeArray.get(index).setCentralWestNode(null);
                nodeArray.get(index).setBottomWestNode(null);
            }
        }
    }

    //  HELPER: GET HIGHEST RESULT
    public DateResult getHighestResult()    {   return highestResult;   }

    //  HELPER: GET HIGHEST RESULT W/ RING
    public DateResult getHighestResultWithRing()    {   return highestResultWithRing;   }

    //  HELPER: GET HIGHEST RESULT W/ AIRPORT
    public DateResult getHighestResultWithAirport() {   return highestResultWithAirport;    }

    //  HELPER: DISPLAY HIGHEST RESULT
    public void displayHighestResult()
    {
        System.out.println("HIGHEST RESULT:");

        if(highestResult == null)
            System.out.println("NO RESULTS FOUND!");
        else
            System.out.println(highestResult);
    }

    //  HELPER: DISPLAY HIGHEST RESULT W/ RING
    public void displayHighestResultWithRing()
    {
        System.out.println("HIGHEST RESULT (WITH RING):");

        if(highestResultWithRing == null)
            System.out.println("NO RESULTS (WITH RING) FOUND!");
        else
            System.out.println(highestResultWithRing);
    }

    //  HELPER: DISPLAY HIGHEST RESULT W/ AIRPORT
    public void displayHighestResultWithAirport()
    {
        System.out.println("HIGHEST RESULT (WITH AIRPORT):");

        if(highestResultWithAirport == null)
            System.out.println("NO RESULTS (WITH AIRPORT) FOUND!");
        else
            System.out.println(highestResultWithAirport);
    }

    //  SETTER: Set (STARTING) Player Direction
    public void setPlayerDirection(String playerDirection)    {   this.playerDirection = playerDirection;   }

    //  GETTER: Access node array
    //  public ArrayList<DateNode> getNodeArray()   {   return nodeArray;   }

    //  GETTER: Access individual nodes, from their ID/number
    public DateNode getNode(int nodeNumber)     {   return nodeArray.get(nodeNumber);   }

    public boolean hasRing()            {   return ring;         }
    public void setRing(boolean ring)   {   this.ring = ring;    }

}
