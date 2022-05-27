
//  ORIGINAL: DateMap.java

//  THIS CLASS IS THE MAIN SEQUENCE OF ACTIONS AIMED
//      AT SOLVING THE DATE MINIGAME.

//  (05/25/2022) MOVED/ALTERED THE ORIGINAL FILE TO NO LONGER
//      TAKE TEXT INPUT, SINCE THE 'ChromeBotMapParser' class
//      DOES THE PARSING WORK NOW.

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class ChromeBotMapSolver
{
    private ArrayList<DateNode> nodeArray;

    private LinkedList<DateResult> results;

    private String playerDirection;

    private int startingNodeNumber;

    private int currentHighestScore;

    private int juiceRefreshTime, coffeeRefreshTime, gasOneRefreshTime, sandwichRefreshTime,
            gasTwoRefreshTime, gasThreeRefreshTime, fairRefreshTime, spaghettiRefreshTime,
            barRefreshTime, theaterRefreshTime, tacoRefreshTime, ballroomRefreshTime;

    public ChromeBotMapSolver()
    {
        //  mapEncodingArray = mapEncoding.split("\\s+");

        nodeArray = new ArrayList<>();

        //  NOTE: I used a Set to store paths because I thought it wouldn't
        //      store duplicates, but that doesn't apply because even if it's the
        //      same score, it takes a different path, so I was misguided in my
        //      attempts at conserving heap space.
        results = new LinkedList<>();
        //  pathsTaken = new LinkedList<>();

        //  STARTING NODE IS ALWAYS AT THE SAME LOCATION, SO WE CAN
        //      DEFINE IT EARLY
        startingNodeNumber = 79;

        currentHighestScore = -1;

        constructNodes();
        linkNodes();
    }

    //  NOTE: Starter function, jump starts its other counterpart.
    public void solveDate()
    {
        System.out.println(playerDirection);

        solveDate(nodeArray.get(startingNodeNumber), playerDirection, 1,
                100, 50, 50, 75, 100,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                false, false,
                "");
    }

    //  NOTE: This version of 'solveDate' is the actual workhorse of the
    //      program, being called recursively to brute-force its way to the
    //      answer.
    public void solveDate(DateNode currentNode, String currentDirection, int turnNumber,
                          int fuel, int hunger, int thirst, int happiness, int time,
                          int juiceRefresh, int coffeeRefresh, int sandwichRefresh, int fairRefresh,
                          int spaghettiRefresh, int barRefresh, int theaterRefresh, int tacoRefresh,
                          int ballroomRefresh, int gasOneRefresh, int gasTwoRefresh, int gasThreeRefresh,
                          boolean gardenVisited, boolean mallVisited,
                          String path)
    {
        /*
            juiceRefreshTime, coffeeRefreshTime, gasOneRefreshTime, sandwichRefreshTime,
            gasTwoRefreshTime, gasThreeRefreshTime, fairRefreshTime, spaghettiRefreshTime,
            barRefreshTime, theaterRefreshTime, tacoRefreshTime, ballroomRefreshTime;
         */

        //  MAX RESOURCE CAP IS 100; ADJUST IF THEY ARE OVER THE LIMIT TO
        //      AVOID ANY MISCALCULATIONS GOING FORWARD
        if(hunger > 100)
            hunger = 100;
        if(thirst > 100)
            thirst = 100;
        if(happiness > 100)
            happiness = 100;

        juiceRefresh--; coffeeRefresh--; sandwichRefresh--; fairRefresh--;
        spaghettiRefresh--; barRefresh--; theaterRefresh--; tacoRefresh--;
        ballroomRefresh--; gasOneRefresh--; gasTwoRefresh--; gasThreeRefresh--;

        if(juiceRefresh < 0)
            juiceRefresh = 0;
        if(coffeeRefresh < 0)
            coffeeRefresh = 0;
        if(sandwichRefresh < 0)
            sandwichRefresh = 0;
        if(fairRefresh < 0)
            fairRefresh = 0;
        if(spaghettiRefresh < 0)
            spaghettiRefresh = 0;
        if(barRefresh < 0)
            barRefresh = 0;
        if(theaterRefresh < 0)
            theaterRefresh = 0;
        if(tacoRefresh < 0)
            tacoRefresh = 0;
        if(ballroomRefresh < 0)
            ballroomRefresh = 0;
        if(gasOneRefresh < 0)
            gasOneRefresh = 0;
        if(gasTwoRefresh < 0)
            gasTwoRefresh = 0;
        if(gasThreeRefresh < 0)
            gasThreeRefresh = 0;

        //  NOTE: This checks if the bot can still go on.
        //      a. Resources have not been depleted
        //      b. There is still time to run down
        if(time > 0 && fuel > 0 && happiness > 0 &&
                hunger > 0 && thirst > 0)
        {
            //  NOTE: IF bot is currently facing RIGHT/EAST
            //      The bot, in this state, can move UP/NORTH, RIGHT/EAST, or DOWN/SOUTH
            if(currentDirection.equals("RIGHT"))
            {
                if(!(currentNode.getRightNorthNode() == null) &&
                        !currentNode.getRightNorthNode().isInaccessible())
                    solveDate(currentNode.getRightNorthNode(), "UP", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited,
                            //  path + " UP [" + currentNode.getRightNorthNode().getNodeNumber() + "] ");
                            path + " UP");

                if(!(currentNode.getCentralEastNode() == null) &&
                        !currentNode.getCentralEastNode().isInaccessible())
                    solveDate(currentNode.getCentralEastNode(), currentDirection, turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited,
                            //  path + " RIGHT [" + currentNode.getCentralEastNode().getNodeNumber() + "] ");
                            path + " RIGHT");

                if(!(currentNode.getRightSouthNode() == null) &&
                        !currentNode.getRightSouthNode().isInaccessible())
                    solveDate(currentNode.getRightSouthNode(), "DOWN", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited,
                            //  path + " DOWN [" + currentNode.getRightSouthNode().getNodeNumber() + "] ");
                            path + " DOWN");
            }
            else if(currentDirection.equals("LEFT"))
            {

                if(!(currentNode.getLeftNorthNode() == null) &&
                        !currentNode.getLeftNorthNode().isInaccessible())
                    solveDate(currentNode.getLeftNorthNode(), "UP", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited,
                            //  path + " UP [" + currentNode.getLeftNorthNode().getNodeNumber() + "] ");
                            path + " UP");

                if(!(currentNode.getCentralWestNode() == null) &&
                        !currentNode.getCentralWestNode().isInaccessible())
                    solveDate(currentNode.getCentralWestNode(), currentDirection, turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited,
                            //   path + " LEFT [" + currentNode.getCentralWestNode().getNodeNumber() + "] ");
                            path + " LEFT");

                if(!(currentNode.getLeftSouthNode() == null) &&
                        !currentNode.getLeftSouthNode().isInaccessible())
                    solveDate(currentNode.getLeftSouthNode(), "DOWN", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited,
                            //  path + " DOWN [" + currentNode.getLeftSouthNode().getNodeNumber() + "] ");
                            path + " DOWN");
            }
            else if(currentDirection.equals("UP"))
            {

                if(!(currentNode.getTopWestNode() == null) &&
                        !currentNode.getTopWestNode().isInaccessible())
                    solveDate(currentNode.getTopWestNode(), "LEFT", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited,
                            //  path + " LEFT [" + currentNode.getTopWestNode().getNodeNumber() + "] ");
                            path + " LEFT");

                if(!(currentNode.getCentralNorthNode() == null) &&
                        !currentNode.getCentralNorthNode().isInaccessible())
                    solveDate(currentNode.getCentralNorthNode(), currentDirection, turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited,
                            //  path + " UP [" + currentNode.getCentralNorthNode().getNodeNumber() + "] ");
                            path + " UP");

                if(!(currentNode.getTopEastNode() == null) &&
                        !currentNode.getTopEastNode().isInaccessible())
                    solveDate(currentNode.getTopEastNode(), "RIGHT", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited,
                            //  path + " RIGHT [" + currentNode.getTopEastNode().getNodeNumber() + "] ");
                            path + " RIGHT");
            }
            else if(currentDirection.equals("DOWN"))
            {

                if(!(currentNode.getBottomWestNode() == null) &&
                        !currentNode.getBottomWestNode().isInaccessible())
                    solveDate(currentNode.getBottomWestNode(), "LEFT", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited,
                            //  path + " LEFT [" + currentNode.getBottomWestNode().getNodeNumber() + "] ");
                            path + " LEFT");

                if(!(currentNode.getCentralSouthNode() == null) &&
                        !currentNode.getCentralSouthNode().isInaccessible())
                    solveDate(currentNode.getCentralSouthNode(), currentDirection, turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited,
                            //  path + " DOWN [" + currentNode.getCentralSouthNode().getNodeNumber() + "] ");
                            path + " DOWN");
                if(!(currentNode.getBottomEastNode() == null) &&
                        !currentNode.getBottomEastNode().isInaccessible())
                    solveDate(currentNode.getBottomEastNode(), "RIGHT", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, mallVisited,
                            //  path + " RIGHT [" + currentNode.getBottomEastNode().getNodeNumber() + "] ");
                            path + " RIGHT");
            }

            //  OBSERVE RESOURCES SURROUNDING CURRENT NODE
            //  AND ACT UPON THEM (Recursion?)
            if(!currentNode.hasNoResources())
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
                            gardenVisited, mallVisited,
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
                            gardenVisited, mallVisited,
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
                            gardenVisited, mallVisited,
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
                            gardenVisited, mallVisited,
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
                            gardenVisited, mallVisited,
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
                            gardenVisited, mallVisited,
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
                            gardenVisited, mallVisited,
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
                            gardenVisited, mallVisited,
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
                            gardenVisited, mallVisited,
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
                            gardenVisited, mallVisited,
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
                            gardenVisited, mallVisited,
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
                            gardenVisited, mallVisited,
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
                            true, mallVisited,
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
                            gardenVisited, true,
                            path + " MALL");
                }
            }

            //  HOME PATH
            //  NOTE: WHEN DEBUGGING, WE WANT EVERY RESULT, BUT FOR NOW,
            //      LOGGING A HOME PATH WITH THE DESIRED AFFECTION POINTS IS ENOUGH
            if (currentNode.isNextToHome())
                results.add(new DateResult(ResultType.RETURNED_HOME, calculateAffectionPoints(hunger, thirst, happiness, time, mallVisited), path + " HOME"));

        }
        else if(time <= 0)
        {
            //  NOTE: Running down the clock is the goal here, but if the user runs
            //      out of any resource after doing so, it will be a failure as well.
            if(fuel > 0 && happiness > 0 && hunger > 0 && thirst > 0)
                results.add(new DateResult(ResultType.SUCCESS, calculateAffectionPoints(hunger, thirst, happiness, mallVisited), path));

            //  NOTE: TO CONSERVE MEMORY IN THE HEAP, I HAVE TO STOP STORING RESULTS
            //      THAT END IN FAILURE.

            /*
            if(fuel <= 0 || happiness <= 0 || hunger <= 0 || thirst <= 0)
                // results.add(new DateResult(fuel, hunger, thirst, happiness, time, "FAILURE", path));
                results.add(new DateResult(ResultType.OUT_OF_RESOURCE, 0, path));
            else if(meetsAPThreshold(hunger, thirst, happiness))
                // results.add(new DateResult(fuel, hunger, thirst, happiness, time, "SUCCESS", path));
                results.add(new DateResult(ResultType.SUCCESS, calculateAffectionPoints(hunger, thirst, happiness), path));
             */
        }
        // else
        // results.add(new DateResult(fuel, hunger, thirst, happiness, time, "FAILURE", path));
        // results.add(new DateResult(ResultType.OUT_OF_RESOURCE, 0, path));

        return;
    }

    //  HELPER: CALCULATE AFFECTION POINTS USING A FORMULA
    private int calculateAffectionPoints(int hunger, int thirst, int happiness, boolean mallVisited)
    {
        int affectionPoints = (int)((double)(hunger + thirst + happiness) / 6);

        if(mallVisited)
            affectionPoints += 30;

        return affectionPoints;
    }

    //  HELPER: CALCULATE AFFEECTION POINTS W/ TIME
    private int calculateAffectionPoints(int hunger, int thirst, int happiness, int time, boolean mallVisited)
    {
        int affectionPoints = (int)(((double)(hunger + thirst + happiness) / 6) * ((double)(100 - time) / 100));

        if(mallVisited)
            affectionPoints += 30;

        return affectionPoints;
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
    public DateResult getHighestResult()
    {
        DateResult highestResult = results.get(0);

        for(DateResult result : results)
        {
            if(result.getAffectionPoints() <= highestResult.getAffectionPoints())
                continue;

            highestResult = result;
        }

        return highestResult;
    }

    //  HELPER: DISPLAY HIGHEST RESULT
    public void displayHighestResult()
    {
        System.out.println("HIGHEST RESULT:");

        DateResult highestResult = null;

        for(DateResult result : results)
        {
            if(highestResult != null)
            {
                if(result.getAffectionPoints() <= highestResult.getAffectionPoints())
                    continue;
            }

            highestResult = result;
        }

        if(highestResult == null)
            System.out.println("NO RESULTS FOUND!");
        else
            System.out.println(highestResult);
    }

    //  SETTER: Set (STARTING) Player Direction
    public void setPlayerDirection(String playerDirection)    {   this.playerDirection = playerDirection;   }

    //  GETTER: Access node array
    //  public ArrayList<DateNode> getNodeArray()   {   return nodeArray;   }

    //  GETTER: Access individual nodes, from their ID/number
    public DateNode getNode(int nodeNumber)     {   return nodeArray.get(nodeNumber);   }

}
