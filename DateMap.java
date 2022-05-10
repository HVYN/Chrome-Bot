
//  Map Class for Date Finisher

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class DateMap
{
    private ArrayList<DateNode> nodeMap;

    private Set<DateResult> results;

    private Set<String> pathsTaken;

    private String[] mapEncodingArray;

    private String playerDirection;

    // private int fuel, hunger, thirst, happiness, time;

    private int startingNodeNumber;

    private int juiceRefreshTime, coffeeRefreshTime, gasOneRefreshTime, sandwichRefreshTime,
        gasTwoRefreshTime, gasThreeRefreshTime, fairRefreshTime, spaghettiRefreshTime,
        barRefreshTime, theaterRefreshTime, tacoRefreshTime, ballroomRefreshTime;

    // Test Constructor - Using String Encoding
    public DateMap(String mapEncoding)
    {
        mapEncodingArray = mapEncoding.split("\\s+");

        nodeMap = new ArrayList<>();

        results = new LinkedHashSet<>();

        pathsTaken = new LinkedHashSet<>();

        startingNodeNumber = 79;

        constructNodes();
        linkNodes();
    }

    public void solveDate()
    {
        solveDate(nodeMap.get(startingNodeNumber), playerDirection, 1,
                100, 50, 50, 75, 100,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                false, false,
                "[" + Integer.toString(startingNodeNumber) + "]");
    }

    public void solveDate(DateNode currentNode, String currentDirection, int turnNumber,
                          int fuel, int hunger, int thirst, int happiness, int time,
                          int juiceRefresh, int coffeeRefresh, int sandwichRefresh, int fairRefresh,
                          int spaghettiRefresh, int barRefresh, int theaterRefresh, int tacoRefresh,
                          int ballroomRefresh, int gasOneRefresh, int gasTwoRefresh, int gasThreeRefresh,
                          boolean gardenVisited, boolean homeVisited,
                          String path)
    {
        /*
            juiceRefreshTime, coffeeRefreshTime, gasOneRefreshTime, sandwichRefreshTime,
            gasTwoRefreshTime, gasThreeRefreshTime, fairRefreshTime, spaghettiRefreshTime,
            barRefreshTime, theaterRefreshTime, tacoRefreshTime, ballroomRefreshTime;
         */

        // System.out.println(" * * *  SOLVING DATE  * * * ");

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

        if(time > 0 && fuel > 0 && happiness > 0 &&
            hunger > 0 && thirst > 0 &&
            !homeVisited)
        {
            // System.out.println("TEST");

            if(currentDirection.equals("RIGHT"))
            {
                if(!(currentNode.getRightNorthNode() == null) &&
                    !currentNode.getRightNorthNode().isInaccessible())
                    solveDate(currentNode.getRightNorthNode(), "UP", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " --UP-> [" + currentNode.getRightNorthNode().getNodeNumber() + "] ");

                if(!(currentNode.getCentralEastNode() == null) &&
                    !currentNode.getCentralEastNode().isInaccessible())
                    solveDate(currentNode.getCentralEastNode(), currentDirection, turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " --RIGHT-> [" + currentNode.getCentralEastNode().getNodeNumber() + "] ");

                if(!(currentNode.getRightSouthNode() == null) &&
                    !currentNode.getRightSouthNode().isInaccessible())
                    solveDate(currentNode.getRightSouthNode(), "DOWN", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " --DOWN-> [" + currentNode.getRightSouthNode().getNodeNumber() + "] ");

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
                            gardenVisited, homeVisited,
                            path + " --UP-> [" + currentNode.getLeftNorthNode().getNodeNumber() + "] ");

                if(!(currentNode.getCentralWestNode() == null) &&
                        !currentNode.getCentralWestNode().isInaccessible())
                    solveDate(currentNode.getCentralWestNode(), currentDirection, turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " --LEFT-> [" + currentNode.getCentralWestNode().getNodeNumber() + "] ");

                if(!(currentNode.getLeftSouthNode() == null) &&
                        !currentNode.getLeftSouthNode().isInaccessible())
                    solveDate(currentNode.getLeftSouthNode(), "DOWN", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " --DOWN-> [" + currentNode.getLeftSouthNode().getNodeNumber() + "] ");

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
                            gardenVisited, homeVisited,
                            path + " --LEFT-> [" + currentNode.getTopWestNode().getNodeNumber() + "] ");

                if(!(currentNode.getCentralNorthNode() == null) &&
                        !currentNode.getCentralNorthNode().isInaccessible())
                    solveDate(currentNode.getCentralNorthNode(), currentDirection, turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " --UP-> [" + currentNode.getCentralNorthNode().getNodeNumber() + "] ");

                if(!(currentNode.getTopEastNode() == null) &&
                        !currentNode.getTopEastNode().isInaccessible())
                    solveDate(currentNode.getTopEastNode(), "RIGHT", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " --RIGHT-> [" + currentNode.getTopEastNode().getNodeNumber() + "] ");

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
                            gardenVisited, homeVisited,
                            path + " --LEFT-> [" + currentNode.getBottomWestNode().getNodeNumber() + "] ");

                if(!(currentNode.getCentralSouthNode() == null) &&
                        !currentNode.getCentralSouthNode().isInaccessible())
                    solveDate(currentNode.getCentralSouthNode(), currentDirection, turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " --DOWN-> [" + currentNode.getCentralSouthNode().getNodeNumber() + "] ");

                if(!(currentNode.getBottomEastNode() == null) &&
                        !currentNode.getBottomEastNode().isInaccessible())
                    solveDate(currentNode.getBottomEastNode(), "RIGHT", turnNumber + 1,
                            fuel - 10, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " --RIGHT-> [" + currentNode.getBottomEastNode().getNodeNumber() + "] ");

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
                            10, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " JUICE");
                }

                //  COFFEE RESOURCE
                if (currentNode.isNextToCoffee() &&
                        coffeeRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger - 4, thirst + 60 - 6, happiness - 8, time - 4,
                            juiceRefresh, 10, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
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
                            ballroomRefresh, 10, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " GAS-ONE");
                }

                //  GAS (TWO) RESOURCE
                if (currentNode.isNextToGasTwo() &&
                        gasTwoRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            100, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, 10, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " GAS-TWO");
                }

                //  GAS (THREE) RESOURCE
                if (currentNode.isNextToGasThree() &&
                        gasThreeRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            100, hunger - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, 10,
                            gardenVisited, homeVisited,
                            path + " GAS-THREE");
                }

                //  SANDWICH RESOURCE
                if (currentNode.isNextToSandwich() &&
                        sandwichRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger + 40 - 4, thirst + 20 - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, 10, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " SANDWICH");
                }

                //  FAIR RESOURCE
                if (currentNode.isNextToFair() &&
                        fairRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger + 20 - 4, thirst + 20 - 6, happiness + 40 - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, 10,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " FAIR");
                }

                //  SPAGHETTI RESOURCE
                if (currentNode.isNextToSpaghetti() &&
                        spaghettiRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger + 60 - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            10, barRefresh, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " ITALIAN");
                }

                //  BAR RESOURCE
                if (currentNode.isNextToBar() &&
                        barRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger - 4, thirst + 40 - 6, happiness + 40 - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, 10, theaterRefresh, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " BAR");
                }

                //  THEATER RESOURCE
                if (currentNode.isNextToTheater() &&
                        theaterRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger - 4, thirst - 6, happiness + 60 - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, 10, tacoRefresh,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " THEATER");
                }

                //  TACO RESOURCE
                if (currentNode.isNextToTaco() &&
                        tacoRefresh == 0)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger + 60 - 4, thirst - 6, happiness - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, 10,
                            ballroomRefresh, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
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
                            10, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            gardenVisited, homeVisited,
                            path + " BALLROOM");
                }

                //  GARDEN RESOURCE
                if (currentNode.isNextToGarden() &&
                        !gardenVisited)
                {
                    solveDate(currentNode, currentDirection, turnNumber + 1,
                            fuel, hunger - 10 - 4, thirst - 15 - 6, happiness + 100 - 8, time - 4,
                            juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                            spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                            10, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                            !gardenVisited, homeVisited,
                            path + " GARDEN");
                }
            }

            //  HOME PATH
            if (currentNode.isNextToHome() &&
                    !homeVisited)
            {
                solveDate(currentNode, currentDirection, turnNumber + 1,
                        fuel, hunger - 10 - 4, thirst - 15 - 6, happiness + 100 - 8, time - 4,
                        juiceRefresh, coffeeRefresh, sandwichRefresh, fairRefresh,
                        spaghettiRefresh, barRefresh, theaterRefresh, tacoRefresh,
                        10, gasOneRefresh, gasTwoRefresh, gasThreeRefresh,
                        !gardenVisited, !homeVisited,
                        path + " HOME");
            }

        }
        else if(time <= 0)
        {
            if(fuel <= 0 || happiness <= 0 || hunger <= 0 || thirst <= 0)
                results.add(new DateResult(fuel, hunger, thirst, happiness, time, "FAILURE", path));
            else
                results.add(new DateResult(fuel, hunger, thirst, happiness, time, "SUCCESS", path));
        }
        else if(homeVisited)
            results.add(new DateResult(fuel, hunger, thirst, happiness, time, "HOME", path));
        else
            results.add(new DateResult(fuel, hunger, thirst, happiness, time, "FAILURE", path));

        return;
    }

    //  CONSTRUCT NODES - INITIALIZE/INSTANTIATE, PUT THEM IN A LIST
    private void constructNodes()
    {
        int nodeNumberIncrement = 0;

        for(String nodeEncoding : mapEncodingArray)
        {
            if(nodeNumberIncrement < 82)
                nodeMap.add(new DateNode(nodeNumberIncrement++, nodeEncoding));
            else
                playerDirection = nodeEncoding;

        }
    }

    //  LINK NODES - LINK ALL THE NODES TOGETHER APPROPRIATELY, ACCORDING
    //  TO USER INPUT
    private void linkNodes()
    {
        //  VERSION 2
        //  SPECIFY CARDINAL DIRECTIONS, MANUALLY ATTACH
        //  EACH NODE TO ONE ANOTHER USING DIRECTIONS.

        for(int index = 0; index < nodeMap.size(); index++)
        {
            // System.out.println("LINKING NODE " + index);

            DateNode currentNode = nodeMap.get(index);

            //  DIFFERENT LINKING PROCEDURES BASED ON WHETHER
            //  THE NODE IS A VERTICAL OR HORIZONTAL ROAD
            // if(isHorizontalNode(index))
            if(currentNode.isHorizontalNode())
            {
                if(index - 6 >= 0)
                    nodeMap.get(index).setLeftNorthNode(nodeMap.get(index - 6));
                if(index - 5 >= 0)
                    nodeMap.get(index).setRightNorthNode(nodeMap.get(index - 5));

                if(index + 5 <= 81)
                    nodeMap.get(index).setLeftSouthNode(nodeMap.get(index + 5));
                if(index + 6 <= 81)
                    nodeMap.get(index).setRightSouthNode(nodeMap.get(index + 6));

                if(index - 1 >= 0)
                    nodeMap.get(index).setCentralWestNode(nodeMap.get(index - 1));
                if(index + 1 <= 81)
                    nodeMap.get(index).setCentralEastNode(nodeMap.get(index + 1));
            }
            else if(currentNode.isVerticalNode())
            {
                if(index - 6 >= 0)
                    nodeMap.get(index).setTopWestNode(nodeMap.get(index - 6));
                if(index - 5 >= 0)
                    nodeMap.get(index).setTopEastNode(nodeMap.get(index - 5));

                if(index + 5 <= 81)
                    nodeMap.get(index).setBottomWestNode(nodeMap.get(index + 5));
                if(index + 6 <= 81)
                    nodeMap.get(index).setBottomEastNode(nodeMap.get(index + 6));

                if(index - 11 >= 0)
                    nodeMap.get(index).setCentralNorthNode(nodeMap.get(index - 11));
                if(index + 11 <= 81)
                    nodeMap.get(index).setCentralSouthNode(nodeMap.get(index + 11));
            }

            //  DESIGNATE OUT OF BOUNDS ADJACENCY
            //  IS NODE THE MOST NORTH OR SOUTH
            if(currentNode.isNodeMostNorth())
            {
                nodeMap.get(index).setLeftNorthNode(null);
                nodeMap.get(index).setCentralNorthNode(null);
                nodeMap.get(index).setRightNorthNode(null);
            }
            else if(currentNode.isNodeMostSouth())
            {
                nodeMap.get(index).setLeftSouthNode(null);
                nodeMap.get(index).setCentralSouthNode(null);
                nodeMap.get(index).setRightSouthNode(null);
            }

            //  IS NODE THE MOST EAST OR WEST
            if(currentNode.isNodeMostEast())
            {
                nodeMap.get(index).setTopEastNode(null);
                nodeMap.get(index).setCentralEastNode(null);
                nodeMap.get(index).setBottomEastNode(null);
            }
            else if(currentNode.isNodeMostWest())
            {
                nodeMap.get(index).setTopWestNode(null);
                nodeMap.get(index).setCentralWestNode(null);
                nodeMap.get(index).setBottomWestNode(null);
            }

            // nodeMap.get(index).displayAdjacentNodes();
        }
    }

    //  DISPLAY FUNCTION TO ITERATE THROUGH NODES IN THE MAP
    private void displayNodes()
    {
        for(DateNode node : nodeMap)
        {
            System.out.print("[NODE " + node.getNodeNumber() + "] ");

            if(!node.isInaccessible())
            {
                if (node.isNextToAirport())
                    System.out.print("| NEXT TO AIRPORT ");
                if (node.isNextToBallroom())
                    System.out.print("| NEXT TO BALLROOM ");
                if (node.isNextToFair())
                    System.out.print("| NEXT TO FAIR ");
                if (node.isNextToBar())
                    System.out.print("| NEXT TO BAR ");
                if (node.isNextToCoffee())
                    System.out.print("| NEXT TO COFFEE ");
                if (node.isNextToGarden())
                    System.out.print("| NEXT TO FLOWER GARDEN ");
                if (node.isNextToGasOne())
                    System.out.print("| NEXT TO GAS (ONE)");
                if (node.isNextToGasTwo())
                    System.out.print("| NEXT TO GAS (TWO)");
                if (node.isNextToGasThree())
                    System.out.print("| NEXT TO GAS (THREE)");
                if (node.isNextToJuice())
                    System.out.print("| NEXT TO JUICE ");
                if (node.isNextToSandwich())
                    System.out.print("| NEXT TO SANDWICH");
                if (node.isNextToTaco())
                    System.out.print("| NEXT TO TACO ");
                if (node.isNextToTheater())
                    System.out.print("| NEXT TO THEATER ");
                if (node.isNextToSpaghetti())
                    System.out.print("| NEXT TO SPAGHETTI ");
                if (node.isNextToHome())
                    System.out.print("| NEXT TO HOME ");
            }
            else
                System.out.print("| INACCESSIBLE ");

            node.displayAdjacentNodes();

            System.out.println();
        }

        // System.out.println(nodeMap);
    }

    /*
         juiceRefreshTime, coffeeRefreshTime, gasOneRefreshTime, sandwichRefreshTime,
         gasTwoRefreshTime, gasThreeRefreshTime, fairRefreshTime, spaghettiRefreshTime,
         barRefreshTime, theaterRefreshTime, tacoRefreshTime, ballroomRefreshTime;
    */

    //  DISPLAY RESULTS FUNCTION - ITERATE THROUGH LIST OF RESULTS
    public void displayResults()
    {
        for(DateResult result : results)
        {
            // if(result.getStatus().equals("SUCCESS") && result.getAffectionPoints() >= 0)
            //     System.out.println(result);
        }
    }

}

