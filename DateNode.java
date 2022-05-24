
//  Node Class to represent areas of the Map

public class DateNode
{
    private int nodeNumber;
    private String adjacentAttributes;

    private DateNode leftNorthNode, rightNorthNode,
            topEastNode, bottomEastNode,
            leftSouthNode, rightSouthNode,
            topWestNode, bottomWestNode,
            centralNorthNode, centralSouthNode,
            centralWestNode, centralEastNode;

    private boolean currentPlayerNode,
            nextToGasOne, nextToTheater, nextToFair, nextToJuice,
            nextToSandwich, nextToCoffee, nextToTaco, nextToGarden,
            nextToBar, nextToAirport, nextToBallroom, nextToSpaghetti,
            nextToHome, nextToGasTwo, nextToGasThree, nextToMall,
            inaccessible;

    public DateNode(int nodeNumber, String adjacentAttributes)
    {
        this.nodeNumber = nodeNumber;

        this.adjacentAttributes = adjacentAttributes;

        leftNorthNode = rightNorthNode = topEastNode = bottomEastNode =
                leftSouthNode = rightSouthNode = topWestNode = bottomWestNode =
                        centralNorthNode = centralEastNode = centralSouthNode = centralWestNode = null;

        currentPlayerNode = false;  inaccessible = false;

        processAdjacentAttributes(adjacentAttributes);
    }

    public void processAdjacentAttributes(String adjacentAttributes)
    {
        for(int index = 0; index < adjacentAttributes.length(); index++)
        {
            switch(adjacentAttributes.charAt(index))
            {
                case('G'):
                    if(adjacentAttributes.charAt(index + 1) == '1')
                    {
                        setNextToGasOne(true);
                        break;
                    }

                    if(adjacentAttributes.charAt(index + 1) == '2')
                    {
                        setNextToGasTwo(true);
                        break;
                    }

                    if(adjacentAttributes.charAt(index + 1) == '3')
                    {
                        setNextToGasThree(true);
                        break;
                    }

                    break;
                case('X'):
                    setInaccessible(true);
                    break;
                case('F'):
                    setNextToFair(true);
                    break;
                case('M'):
                    setNextToTheater(true);
                    break;
                case('C'):
                    setNextToCoffee(true);
                    break;
                case('J'):
                    setNextToJuice(true);
                    break;
                case('S'):
                    setNextToSandwich(true);
                    break;
                case('A'):
                    setNextToAirport(true);
                    break;
                case('Q'):
                    setNextToGarden(true);
                    break;
                case('B'):
                    setNextToBar(true);
                    break;
                case('D'):
                    setNextToBallroom(true);
                    break;
                case('P'):
                    setNextToTaco(true);
                    break;
                case('I'):
                    setNextToSpaghetti(true);
                    break;
                case('H'):
                    setNextToHome(true);
                    break;
                case('U'):
                    setNextToMall(true);
                    break;
                default:
                    break;
            }
        }
    }

    public void setCentralNorthNode(DateNode centralNorthNode)  {   this.centralNorthNode = centralNorthNode;   }
    public void setCentralEastNode(DateNode centralEastNode)    {   this.centralEastNode = centralEastNode;     }
    public void setCentralSouthNode(DateNode centralSouthNode)  {   this.centralSouthNode = centralSouthNode;   }
    public void setCentralWestNode(DateNode centralWestNode)    {   this.centralWestNode = centralWestNode;     }

    public void setLeftNorthNode(DateNode leftNorthNode)    {   this.leftNorthNode = leftNorthNode;     }
    public void setRightNorthNode(DateNode rightNorthNode)  {   this.rightNorthNode = rightNorthNode;   }
    public void setTopEastNode(DateNode topEastNode)        {   this.topEastNode = topEastNode;         }
    public void setBottomEastNode(DateNode bottomEastNode)  {   this.bottomEastNode = bottomEastNode;   }
    public void setLeftSouthNode(DateNode leftSouthNode)    {   this.leftSouthNode = leftSouthNode;     }
    public void setRightSouthNode(DateNode rightSouthNode)  {   this.rightSouthNode = rightSouthNode;   }
    public void setTopWestNode(DateNode topWestNode)        {   this.topWestNode = topWestNode;         }
    public void setBottomWestNode(DateNode bottomWestNode)  {   this.bottomWestNode = bottomWestNode;   }

    public DateNode getLeftNorthNode()  {   return leftNorthNode;   }
    public DateNode getRightNorthNode() {   return rightNorthNode;  }
    public DateNode getTopEastNode()    {   return topEastNode;     }
    public DateNode getBottomEastNode() {   return bottomEastNode;  }
    public DateNode getLeftSouthNode()  {   return leftSouthNode;   }
    public DateNode getRightSouthNode() {   return rightSouthNode;  }
    public DateNode getTopWestNode()    {   return topWestNode;     }
    public DateNode getBottomWestNode() {   return bottomWestNode;  }

    public DateNode getCentralNorthNode()   {   return centralNorthNode;    }
    public DateNode getCentralEastNode()    {   return centralEastNode;     }
    public DateNode getCentralSouthNode()   {   return centralSouthNode;    }
    public DateNode getCentralWestNode()    {   return centralWestNode;     }

    // public void addAdjacentNodeNumbers(int adjacentNodeNumber)
    // {
    //     adjacentNodeNumbers.add(adjacentNodeNumber);
    // }

    // public ArrayList<Integer> getAdjacentNodes()
    // {
    //     return adjacentNodeNumbers;
    // }

    public void setInaccessible(boolean inaccessible)   {   this.inaccessible = inaccessible;   }
    public boolean isInaccessible()     {   return inaccessible;    }

    public void setCurrentPlayerNode(boolean currentPlayerNode) {   this.currentPlayerNode = currentPlayerNode; }
    public boolean isCurrentPlayerNode()    {   return currentPlayerNode;   }

    public void setNextToGasOne(boolean nextToGasOne)       {   this.nextToGasOne = nextToGasOne;       }
    public void setNextToGasTwo(boolean nextToGasTwo)       {   this.nextToGasTwo = nextToGasTwo;       }
    public void setNextToGasThree(boolean nextToGasThree)   {   this.nextToGasThree = nextToGasThree;   }
    public void setNextToTheater(boolean nextToTheater)     {   this.nextToTheater = nextToTheater;     }
    public void setNextToFair(boolean nextToFair)           {   this.nextToFair = nextToFair;           }
    public void setNextToJuice(boolean nextToJuice)         {   this.nextToJuice = nextToJuice;         }
    public void setNextToSandwich(boolean nextToSandwich)   {   this.nextToSandwich = nextToSandwich;   }
    public void setNextToCoffee(boolean nextToCoffee)       {   this.nextToCoffee = nextToCoffee;       }
    public void setNextToTaco(boolean nextToTaco)           {   this.nextToTaco = nextToTaco;           }
    public void setNextToGarden(boolean nextToGarden)       {   this.nextToGarden = nextToGarden;       }
    public void setNextToBar(boolean nextToBar)             {   this.nextToBar = nextToBar;             }
    public void setNextToAirport(boolean nextToAirport)     {   this.nextToAirport = nextToAirport;     }
    public void setNextToBallroom(boolean nextToBallroom)   {   this.nextToBallroom = nextToBallroom;   }
    public void setNextToSpaghetti(boolean nextToSpaghetti) {   this.nextToSpaghetti = nextToSpaghetti; }
    public void setNextToHome(boolean nextToHome)           {   this.nextToHome = nextToHome;           }
    public void setNextToMall(boolean nextToMall)           {   this.nextToMall = nextToMall;           }

    public boolean isNextToGasOne()     {   return nextToGasOne;       }
    public boolean isNextToGasTwo()     {   return nextToGasTwo;    }
    public boolean isNextToGasThree()   {   return nextToGasThree;  }
    public boolean isNextToTheater()    {   return nextToTheater;   }
    public boolean isNextToFair()       {   return nextToFair;      }
    public boolean isNextToJuice()      {   return nextToJuice;     }
    public boolean isNextToSandwich()   {   return nextToSandwich;  }
    public boolean isNextToCoffee()     {   return nextToCoffee;    }
    public boolean isNextToTaco()       {   return nextToTaco;      }
    public boolean isNextToGarden()     {   return nextToGarden;    }
    public boolean isNextToBar()        {   return nextToBar;       }
    public boolean isNextToAirport()    {   return nextToAirport;   }
    public boolean isNextToBallroom()   {   return nextToBallroom;  }
    public boolean isNextToSpaghetti()  {   return nextToSpaghetti; }
    public boolean isNextToHome()       {   return nextToHome;      }
    public boolean isNextToMall()       {   return nextToMall;      }

    public boolean hasNoResources()
    {
        return !(isNextToGasOne() || isNextToTheater() || isNextToFair() || isNextToJuice() ||
                isNextToSandwich() || isNextToCoffee() || isNextToTaco() || isNextToGarden() ||
                isNextToBar() || isNextToBallroom() || isNextToSpaghetti() || isNextToGasTwo() ||
                isNextToGasThree() || isNextToMall());
    }

    public boolean isNodeMostWest()
    {
        return nodeNumber == 0 || nodeNumber == 5 || nodeNumber == 11 || nodeNumber == 16 ||
                nodeNumber == 22 || nodeNumber == 27 || nodeNumber == 33 || nodeNumber == 38 ||
                nodeNumber == 44 || nodeNumber == 49 || nodeNumber == 55 || nodeNumber == 60 ||
                nodeNumber == 66 || nodeNumber == 71 || nodeNumber == 77;
    }

    public boolean isNodeMostNorth()
    {
        return nodeNumber == 0 || nodeNumber == 1 || nodeNumber == 2 || nodeNumber == 3 ||
                nodeNumber == 4;
    }

    public boolean isNodeMostEast()
    {
        return nodeNumber == 4 || nodeNumber == 10 || nodeNumber == 15 || nodeNumber == 21 ||
                nodeNumber == 26 || nodeNumber == 32 || nodeNumber == 37 || nodeNumber == 43 ||
                nodeNumber == 48 || nodeNumber == 54 || nodeNumber == 59 || nodeNumber == 65 ||
                nodeNumber == 70 || nodeNumber == 76 || nodeNumber == 81;
    }

    public boolean isNodeMostSouth()
    {
        return nodeNumber == 77 || nodeNumber == 78 || nodeNumber == 79 || nodeNumber == 80 ||
                nodeNumber == 81;
    }

    public boolean isVerticalNode()
    {
        return nodeNumber == 5 || nodeNumber == 6 || nodeNumber == 7 || nodeNumber == 8 || nodeNumber == 9 || nodeNumber == 10 ||
                nodeNumber == 16 || nodeNumber == 17 || nodeNumber == 18 || nodeNumber == 19 || nodeNumber == 20 || nodeNumber == 21 ||
                nodeNumber == 27 || nodeNumber == 28 || nodeNumber == 29 || nodeNumber == 30 || nodeNumber == 31 || nodeNumber == 32 ||
                nodeNumber == 38 || nodeNumber == 39 || nodeNumber == 40 || nodeNumber == 41 || nodeNumber == 42 || nodeNumber == 43 ||
                nodeNumber == 49 || nodeNumber == 50 || nodeNumber == 51 || nodeNumber == 52 || nodeNumber == 53 || nodeNumber == 54 ||
                nodeNumber == 60 || nodeNumber == 61 || nodeNumber == 62 || nodeNumber == 63 || nodeNumber == 64 || nodeNumber == 65 ||
                nodeNumber == 71 || nodeNumber == 72 || nodeNumber == 73 || nodeNumber == 74 || nodeNumber == 75 || nodeNumber == 76;
    }

    public boolean isHorizontalNode()
    {
        return nodeNumber == 0 || nodeNumber == 1 || nodeNumber == 2 || nodeNumber == 3 || nodeNumber == 4 ||
                nodeNumber == 11 || nodeNumber == 12 || nodeNumber == 13 || nodeNumber == 14 || nodeNumber == 15 ||
                nodeNumber == 22 || nodeNumber == 23 || nodeNumber == 24 || nodeNumber == 25 || nodeNumber == 26 ||
                nodeNumber == 33 || nodeNumber == 34 || nodeNumber == 35 || nodeNumber == 36 || nodeNumber == 37 ||
                nodeNumber == 44 || nodeNumber == 45 || nodeNumber == 46 || nodeNumber == 47 || nodeNumber == 48 ||
                nodeNumber == 55 || nodeNumber == 56 || nodeNumber == 57 || nodeNumber == 58 || nodeNumber == 59 ||
                nodeNumber == 66 || nodeNumber == 67 || nodeNumber == 68 || nodeNumber == 69 || nodeNumber == 70 ||
                nodeNumber == 77 || nodeNumber == 78 || nodeNumber == 79 || nodeNumber == 80 || nodeNumber == 81;
    }

    public int getNodeNumber()  {   return nodeNumber;  }

    public void displayAdjacentNodes()
    {
        System.out.println("\n\tLEFT NORTH NODE: " + getLeftNorthNode() + "\n" +
                "\tCENTRAL NORTH NODE: " + getCentralNorthNode() + "\n" +
                "\tRIGHT NORTH NODE: " + getRightNorthNode() + "\n" +
                "\tTOP EAST NODE: " + getTopEastNode() + "\n" +
                "\tCENTRAL EAST NODE: " + getCentralEastNode() + "\n" +
                "\tBOTTOM EAST NODE: " + getBottomEastNode() + "\n" +
                "\tLEFT SOUTH NODE: " + getLeftSouthNode() + "\n" +
                "\tCENTRAL SOUTH NODE: " + getCentralSouthNode() + "\n" +
                "\tRIGHT SOUTH NODE: " + getRightSouthNode() + "\n" +
                "\tTOP WEST NODE: " + getTopWestNode() + "\n" +
                "\tCENTRAL WEST NODE: " + getCentralWestNode() + "\n" +
                "\tBOTTOM WEST NODE: " + getBottomWestNode() + "\n");
    }

    @Override
    public String toString()
    {
        return "(NODE " + nodeNumber + " | " + adjacentAttributes + ")";
    }

}