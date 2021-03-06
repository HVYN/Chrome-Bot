package karuta;

/*
    NOTE: Representation of where the player can be at.
 */

public class DateNode
{
    private final int nodeNumber;

    private DateNode leftNorthNode, rightNorthNode,
            topEastNode, bottomEastNode,
            leftSouthNode, rightSouthNode,
            topWestNode, bottomWestNode,
            centralNorthNode, centralSouthNode,
            centralWestNode, centralEastNode;

    private boolean nextToGasOne, nextToTheater, nextToFair, nextToJuice,
            nextToSandwich, nextToCoffee, nextToTaco, nextToGarden,
            nextToBar, nextToAirport, nextToBallroom, nextToSpaghetti,
            nextToHome, nextToGasTwo, nextToGasThree, nextToMall,
            nextToRing, accessible;

    public DateNode(int nodeNumber)
    {
        this.nodeNumber = nodeNumber;

        leftNorthNode = rightNorthNode = topEastNode = bottomEastNode =
                leftSouthNode = rightSouthNode = topWestNode = bottomWestNode =
                        centralNorthNode = centralEastNode = centralSouthNode = centralWestNode = null;

        accessible = true;
    }

    //  NOTE: Link this node to given resource.
    public void linkNodeToResource(String resource)
    {
        switch(resource)
        {
            case "JUICE" -> setNextToJuice(true);
            case "MALL" -> setNextToMall(true);
            case "SPAGHETTI" -> setNextToSpaghetti(true);
            case "FAIR" -> setNextToFair(true);
            case "SANDWICH" -> setNextToSandwich(true);
            case "BAR" -> setNextToBar(true);
            case "AIRPLANE" -> setNextToAirport(true);
            case "THEATER" -> setNextToTheater(true);
            case "TACO" -> setNextToTaco(true);
            case "COFFEE" -> setNextToCoffee(true);
            case "GARDEN" -> setNextToGarden(true);
            case "BALLROOM" -> setNextToBallroom(true);
            case "HOME" -> setNextToHome(true);
            case "GAS1" -> setNextToGasOne(true);
            case "GAS2" -> setNextToGasTwo(true);
            case "GAS3" -> setNextToGasThree(true);
            case "RING" -> setNextToRing(true);
        }

    }

    //  NOTE: During parsing phase, use these methods to link together nodes.
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

    //  NOTE: Getter methods return respective, adjacent nodes.
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

    //  SETTER: Used during parsing phase; set node's accessibility status.
    public void setAccessible(boolean accessible)   {   this.accessible = accessible;   }

    //  GETTER: Return accessibility of node.
    public boolean isAccessible()     {   return accessible;    }

    //  SETTER: Used during the parsing phase; set node's adjacent resources.
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
    public void setNextToRing(boolean nextToRing)           {   this.nextToRing = nextToRing;           }

    //  GETTER: Whether node is next to one of these resources below.
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
    //  public boolean isNextToAirport()    {   return nextToAirport;   }
    public boolean isNextToBallroom()   {   return nextToBallroom;  }
    public boolean isNextToSpaghetti()  {   return nextToSpaghetti; }
    public boolean isNextToHome()       {   return nextToHome;      }
    public boolean isNextToMall()       {   return nextToMall;      }
    public boolean isNextToRing()       {   return nextToRing;      }
    public boolean isNextToAirport()    {   return nextToAirport;   }

    //  NOTE: Check function, to certain portions of solving work if node is
    //      neighboring any resources.
    public boolean hasResources()
    {
        return isNextToGasOne() || isNextToTheater() || isNextToFair() || isNextToJuice() ||
                isNextToSandwich() || isNextToCoffee() || isNextToTaco() || isNextToGarden() ||
                isNextToBar() || isNextToBallroom() || isNextToSpaghetti() || isNextToGasTwo() ||
                isNextToGasThree() || isNextToMall() || isNextToRing();
    }

    //  NOTE: (HARD-CODED) Returns whether the node is bordering the left-most edge of the map board.
    public boolean isNodeMostWest()
    {
        return nodeNumber == 0 || nodeNumber == 5 || nodeNumber == 11 || nodeNumber == 16 ||
                nodeNumber == 22 || nodeNumber == 27 || nodeNumber == 33 || nodeNumber == 38 ||
                nodeNumber == 44 || nodeNumber == 49 || nodeNumber == 55 || nodeNumber == 60 ||
                nodeNumber == 66 || nodeNumber == 71 || nodeNumber == 77;
    }

    //  NOTE: (HARD-CODED) Returns whether the node is bordering the top edge of the map board.
    public boolean isNodeMostNorth()
    {
        return nodeNumber == 0 || nodeNumber == 1 || nodeNumber == 2 || nodeNumber == 3 ||
                nodeNumber == 4;
    }

    //  NOTE: (HARD-CODED) Returns whether the node is bordering the right-most edge of the map board.
    public boolean isNodeMostEast()
    {
        return nodeNumber == 4 || nodeNumber == 10 || nodeNumber == 15 || nodeNumber == 21 ||
                nodeNumber == 26 || nodeNumber == 32 || nodeNumber == 37 || nodeNumber == 43 ||
                nodeNumber == 48 || nodeNumber == 54 || nodeNumber == 59 || nodeNumber == 65 ||
                nodeNumber == 70 || nodeNumber == 76 || nodeNumber == 81;
    }

    //  NOTE: (HARD-CODED) Returns whether the node is bordering the bottom edge of the map board.
    public boolean isNodeMostSouth()
    {
        return nodeNumber == 77 || nodeNumber == 78 || nodeNumber == 79 || nodeNumber == 80 ||
                nodeNumber == 81;
    }

    //  NOTE: (HARD-CODED) Returns whether the node is oriented vertically based on its number.
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

    //  NOTE: (HARD-CODED) Returns whether the node is oriented horizontally based on its number.
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

    @Override
    public String toString()
    {
        return "(NODE " + nodeNumber + ")";
    }

    //  DEBUG: Return node number
    //  public int getNodeNumber()  {   return nodeNumber;  }

}