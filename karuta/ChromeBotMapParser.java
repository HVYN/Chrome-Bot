package karuta;

//  ORIGINAL: TestMapParser.java

/*
    This class' purpose is to parse a given image of a Date Minigame board,
        and feed the information to the brute-force algorithm that solves
        the date.

    (05/25/2022) MOVED PARSER TO THE ACTUAL BOT (Prev. Suika/Taiju Bot)
 */

import java.awt.*;
import java.awt.image.BufferedImage;

public class ChromeBotMapParser
{
    //  For the time being, the approach is a static function
    //      so I don't have to instantiate an object -> use
    //      the object's method; just use class method.

    /*
        Parameters:
            'mapImage' (BufferedImage)
                Image file (.png) of the date board (given by Karuta bot, accessed through Discord
                Proxy URL).
            'mapSolver' (karuta.ChromeBotMapSolver)
                Reference to the instance of the class responsible for solving the actual date
                mini-game.

         Return:
            String -> Return an error message to indicate what exactly went wrong in the parsing
            process.
     */
    public static String parseMapImage(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {

        //  NOTE: IF ANY CRITICAL RESOURCE CANNOT BE FOUND, STOP SOLVING PROCESS.
        if(!checkIfHome(mapImage, mapSolver))
            return "HOME NOT FOUND";
        if(!checkIfBallroom(mapImage, mapSolver))
            return "BALLROOM NOT FOUND";
        if(!checkIfGas(mapImage, mapSolver))
            return "GAS NOT FOUND";
        if(!checkIfGarden(mapImage, mapSolver))
            return "GARDEN NOT FOUND";
        if(!checkIfCoffee(mapImage, mapSolver))
            return "COFFEE NOT FOUND";
        if(!checkIfTaco(mapImage, mapSolver))
            return "TACO NOT FOUND";
        if(!checkIfTheater(mapImage, mapSolver))
            return "THEATER NOT FOUND";
        if(!checkIfBar(mapImage, mapSolver))
            return "BAR NOT FOUND";
        if(!checkIfSandwich(mapImage, mapSolver))
            return "SANDWICH NOT FOUND";
        if(!checkIfFair(mapImage, mapSolver))
            return "FAIR NOT FOUND";
        if(!checkIfSpaghetti(mapImage, mapSolver))
            return "SPAGHETTI NOT FOUND";
        if(!checkIfJuice(mapImage, mapSolver))
            return "JUICE NOT FOUND";

        //  NOTE: Shopping Mall is NOT a CRITICAL resource.
        checkIfMall(mapImage, mapSolver);

        //  NOTE: Airport is NOT a CRITICAL resource.
        //  checkIfAirplane(mapImage, mapSolver);

        //  NOTE: Ring is NOT a CRITICAL resource.
        mapSolver.setRing(checkIfRing(mapImage, mapSolver));

        //  NOTE: MARK Inaccessible nodes
        checkInaccessibleNodes(mapImage, mapSolver);

        return "";
    }

    /*
        NOTE: Track two specific pixels (@ Node 79) to determine what direction the car/player is
          facing at the start of the date mini-game.

        Parameters:
            'mapImage' (BufferedImage)
                Image file (.png) of the date board.
            'mapSolver' (karuta.ChromeBotMapSolver)
                Reference to the instance of the class responsible for solving the actual date
                mini-game.

        Return:
            true
                The player is @ Node 79, and faces either LEFT or RIGHT.
            false
                The player is NOT @ Node 79.
     */
    public static boolean checkStartDirection(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        Color leftFacing = new Color(mapImage.getRGB(388, 571));
        Color rightFacing = new Color(mapImage.getRGB(410, 571));

        if(leftFacing.equals(new Color(241, 230, 143)))
        {
            mapSolver.setPlayerDirection("LEFT");

            return true;
        }
        else if(rightFacing.equals(new Color(241, 230, 143)))
        {
            mapSolver.setPlayerDirection("RIGHT");

            return true;
        }

        return false;
    }

    /*
        NOTE: Check if the date has already begun by looking at the Time gauge.

        Parameters:
            'mapImage' (BufferedImage)
                Image file (.png) of the date board.

        Return:
            true
                Return when the specific pixel is pure black (RGB: (0, 0, 0)), indicating
                the bar has gone down due to being started.
            false
                The date has not yet begun, and is in its starting state.
     */
    public static boolean hasDateBegun(BufferedImage mapImage)
    {
        Color timeBar = new Color(mapImage.getRGB(703, 123));

        if(timeBar.equals(new Color(0, 0, 0)))
            return true;

        return false;
    }

    /*
        NOTE: The below 'CheckIf' methods all basically check for certain patterns of pixels
            for respective resources.
     */

    private static boolean checkIfJuice(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color straw = new Color(mapImage.getRGB(x, y));
                if(!isStrawColorCorrect(straw))
                    continue;

                if(!straw.equals(new Color(mapImage.getRGB(x + 1, y - 1))))
                    continue;
                if(!straw.equals(new Color(mapImage.getRGB(x + 3, y - 2))))
                    continue;
                if(!checkRectanglePattern(straw, mapImage, x + 6, y, 0, 3))
                    continue;

                Color redArt = new Color(mapImage.getRGB(x + 10, y + 12));
                if(!isRedArtColorCorrect(redArt))
                    continue;

                if(!checkRectanglePattern(redArt, mapImage, x + 10, y + 12, 4, 0))
                    continue;

                System.out.println("JUICE DETECTED.\n");
                determineSquareNumber(x, y, mapSolver, "JUICE");
                return true;
            }

            //  CHECK FOR MIRROR FORM
            for(int y = 150; y < 572; y++)
            {
                Color straw = new Color(mapImage.getRGB(x, y));
                if(!isStrawColorCorrect(straw))
                    continue;

                if(!straw.equals(new Color(mapImage.getRGB(x - 1, y - 1))))
                    continue;
                if(!straw.equals(new Color(mapImage.getRGB(x - 3, y - 2))))
                    continue;
                if(!checkRectanglePattern(straw, mapImage, x - 6, y, 0, 3))
                    continue;

                Color redArt = new Color(mapImage.getRGB(x - 14, y + 12));
                if(!isRedArtColorCorrect(redArt))
                    continue;

                if(!checkRectanglePattern(redArt, mapImage, x - 14, y + 12, 4, 0))
                    continue;

                System.out.println("JUICE DETECTED. (MIRROR)\n");
                determineSquareNumber(x, y, mapSolver, "JUICE");
                return true;
            }
        }

        System.out.println("ERROR: JUICE NOT FOUND");
        return false;
    }

    private static boolean checkIfSpaghetti(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color cheese = new Color(mapImage.getRGB(x, y));
                if(!isCheeseColorCorrect(cheese))
                    continue;

                if(!checkRectanglePattern(cheese, mapImage, x, y, 3, 0))
                    continue;
                if(!checkRectanglePattern(cheese, mapImage, x - 11, y - 7, 0, 1))
                    continue;

                Color sauce = new Color(mapImage.getRGB(x + 2, y - 9));
                if(!isSauceColorCorrect(sauce))
                    continue;

                if(!checkRectanglePattern(sauce, mapImage, x + 2, y - 9, 4, 0))
                    continue;

                Color fork = new Color(mapImage.getRGB(x + 14, y - 13));
                if(!isForkColorCorrect(fork))
                    continue;

                if(!checkRectanglePattern(fork, mapImage, x + 13, y - 14, 2, 0))
                    continue;
                if(!checkRectanglePattern(fork, mapImage, x + 13, y - 15, 2, 0))
                    continue;

                System.out.println("SPAGHETTI DETECTED.\n");
                determineSquareNumber(x, y, mapSolver, "SPAGHETTI");
                return true;
            }

            //  CHECK FOR MIRROR FORM
            for(int y = 150; y < 572; y++)
            {
                Color cheese = new Color(mapImage.getRGB(x, y));
                if(!isCheeseColorCorrect(cheese))
                    continue;

                if(!checkRectanglePattern(cheese, mapImage, x, y, 3, 0))
                    continue;
                if(!checkRectanglePattern(cheese, mapImage, x + 14, y - 7, 0, 1))
                    continue;

                Color sauce = new Color(mapImage.getRGB(x - 3, y - 9));
                if(!isSauceColorCorrect(sauce))
                    continue;

                if(!checkRectanglePattern(sauce, mapImage, x - 3, y - 9, 4, 0))
                    continue;

                Color fork = new Color(mapImage.getRGB(x - 14, y - 13));
                if(!isForkColorCorrect(fork))
                    continue;

                if(!checkRectanglePattern(fork, mapImage, x - 14, y - 14, 2, 0))
                    continue;
                if(!checkRectanglePattern(fork, mapImage, x - 14, y - 15, 2, 0))
                    continue;

                System.out.println("SPAGHETTI DETECTED. (MIRROR)\n");
                determineSquareNumber(x, y, mapSolver, "SPAGHETTI");
                return true;
            }
        }

        System.out.println("ERROR: SPAGHETTI NOT FOUND");
        return false;
    }

    private static boolean checkIfFair(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color blueCart = new Color(mapImage.getRGB(x, y));
                if(blueCart.getRed() < 40 || blueCart.getRed() > 100)
                    continue;
                if(blueCart.getGreen() < 120 || blueCart.getGreen() > 160)
                    continue;
                if(blueCart.getBlue() < 195)
                    continue;

                if(!checkRectanglePattern(blueCart, mapImage, x, y, 3, 0))
                    continue;
                if(!checkRectanglePattern(blueCart, mapImage, x + 22, y, 3, 0))
                    continue;

                Color diagonal = new Color(mapImage.getRGB(x + 2, y + 2));
                if(diagonal.getRed() < 145 || diagonal.getRed() > 195)
                    continue;
                if(diagonal.getGreen() < 170 || diagonal.getGreen() > 200)
                    continue;
                if(diagonal.getBlue() < 170 || diagonal.getBlue() > 225)
                    continue;

                if(!diagonal.equals(new Color(mapImage.getRGB(x + 3, y + 3))))
                    continue;
                if(!diagonal.equals(new Color(mapImage.getRGB(x + 22, y + 3))))
                    continue;
                if(!diagonal.equals(new Color(mapImage.getRGB(x + 23, y + 2))))
                    continue;

                System.out.println("FAIR DETECTED.\n");
                determineSquareNumber(x, y, mapSolver, "FAIR");
                return true;
            }
        }

        System.out.println("ERROR: FAIR NOT FOUND");
        return false;
    }

    private static boolean checkIfSandwich(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color bread = new Color(mapImage.getRGB(x, y));
                if(bread.getRed() < 230)
                    continue;
                if(bread.getGreen() < 205 || bread.getGreen() > 230)
                    continue;
                if(bread.getBlue() < 120 || bread.getBlue() > 150)
                    continue;

                if(!checkRectanglePattern(bread, mapImage, x, y, 10, 8))
                    continue;

                System.out.println("SANDWICH DETECTED.\n");
                determineSquareNumber(x, y, mapSolver, "SANDWICH");
                return true;
            }
        }

        System.out.println("ERROR: SANDWICH NOT FOUND");
        return false;
    }

    private static boolean checkIfBar(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color umbrellaShaft = new Color(mapImage.getRGB(x, y));
                if(umbrellaShaft.getRed() < 175 || umbrellaShaft.getRed() > 220)
                    continue;
                if(umbrellaShaft.getGreen() < 90 || umbrellaShaft.getGreen() > 120)
                    continue;
                if(umbrellaShaft.getBlue() < 60 || umbrellaShaft.getBlue() > 110)
                    continue;

                if(!umbrellaShaft.equals(new Color(mapImage.getRGB(x, y + 1))))
                    continue;

                Color liquid = new Color(mapImage.getRGB(x - 2, y + 8));
                if(liquid.getRed() < 195)
                    continue;
                if(liquid.getGreen() < 120 || liquid.getGreen() > 165)
                    continue;
                if(liquid.getBlue() > 45)
                    continue;

                if(!checkRectanglePattern(liquid, mapImage, x - 2, y + 8, 12, 0))
                    continue;
                if(!checkRectanglePattern(liquid, mapImage, x - 1, y + 9, 10, 0))
                    continue;
                if(!checkRectanglePattern(liquid, mapImage, x + 3, y + 10, 2, 0))
                    continue;

                System.out.println("BAR DETECTED.\n");
                determineSquareNumber(x, y, mapSolver, "BAR");
                return true;
            }

            //  CHECK FOR MIRROR FORM
            for(int y = 150; y < 572; y++)
            {
                Color umbrellaShaft = new Color(mapImage.getRGB(x, y));
                if(umbrellaShaft.getRed() < 175 || umbrellaShaft.getRed() > 220)
                    continue;
                if(umbrellaShaft.getGreen() < 90 || umbrellaShaft.getGreen() > 120)
                    continue;
                if(umbrellaShaft.getBlue() < 60 || umbrellaShaft.getBlue() > 110)
                    continue;

                if(!umbrellaShaft.equals(new Color(mapImage.getRGB(x, y + 1))))
                    continue;

                Color liquid = new Color(mapImage.getRGB(x + 2, y + 8));
                if(liquid.getRed() < 195)
                    continue;
                if(liquid.getGreen() < 120 || liquid.getGreen() > 165)
                    continue;
                if(liquid.getBlue() > 45)
                    continue;

                if(!checkRectanglePattern(liquid, mapImage, x + 2, y + 8, 12, 0))
                    continue;
                if(!checkRectanglePattern(liquid, mapImage, x + 1, y + 9, 10, 0))
                    continue;
                if(!checkRectanglePattern(liquid, mapImage, x + 3, y + 10, 2, 0))
                    continue;

                System.out.println("BAR DETECTED. (MIRROR)\n");
                determineSquareNumber(x, y, mapSolver, "BAR");
                return true;
            }
        }

        System.out.println("ERROR: BAR NOT FOUND");
        return false;
    }

    private static boolean checkIfTheater(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color eyeMouth = new Color(mapImage.getRGB(x, y));
                if(eyeMouth.getRed() < 110 || eyeMouth.getRed() > 170)
                    continue;
                if(eyeMouth.getGreen() < 90 || eyeMouth.getGreen() > 120)
                    continue;
                if(eyeMouth.getBlue() < 185 || eyeMouth.getBlue() > 220)
                    continue;

                if(!eyeMouth.equals(new Color(mapImage.getRGB(x + 7, y))))
                    continue;
                if(!eyeMouth.equals(new Color(mapImage.getRGB(x + 1, y + 8))))
                    continue;
                if(!eyeMouth.equals(new Color(mapImage.getRGB(x + 2, y + 8))))
                    continue;
                if(!eyeMouth.equals(new Color(mapImage.getRGB(x + 2, y + 7))))
                    continue;
                if(!eyeMouth.equals(new Color(mapImage.getRGB(x + 3, y + 7))))
                    continue;
                if(!eyeMouth.equals(new Color(mapImage.getRGB(x + 4, y + 7))))
                    continue;

                System.out.println("THEATER DETECTED.\n");
                determineSquareNumber(x, y, mapSolver, "THEATER");
                return true;
            }

            //  CHECK FOR MIRROR FORM
            for(int y = 150; y < 572; y++)
            {
                Color eyeMouth = new Color(mapImage.getRGB(x, y));
                if(eyeMouth.getRed() < 110 || eyeMouth.getRed() > 170)
                    continue;
                if(eyeMouth.getGreen() < 90 || eyeMouth.getGreen() > 120)
                    continue;
                if(eyeMouth.getBlue() < 185 || eyeMouth.getBlue() > 220)
                    continue;

                if(!eyeMouth.equals(new Color(mapImage.getRGB(x - 7, y))))
                    continue;
                if(!eyeMouth.equals(new Color(mapImage.getRGB(x - 1, y + 8))))
                    continue;
                if(!eyeMouth.equals(new Color(mapImage.getRGB(x - 2, y + 8))))
                    continue;
                if(!eyeMouth.equals(new Color(mapImage.getRGB(x - 2, y + 7))))
                    continue;
                if(!eyeMouth.equals(new Color(mapImage.getRGB(x - 3, y + 7))))
                    continue;
                if(!eyeMouth.equals(new Color(mapImage.getRGB(x - 4, y + 7))))
                    continue;

                System.out.println("THEATER DETECTED. (MIRROR)\n");
                determineSquareNumber(x, y, mapSolver, "THEATER");
                return true;
            }
        }

        System.out.println("ERROR: THEATER NOT FOUND");
        return false;
    }

    private static boolean checkIfTaco(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color shellDark = new Color(mapImage.getRGB(x, y));
                if(shellDark.getRed() < 200)
                    continue;
                if(shellDark.getGreen() < 115 || shellDark.getGreen() > 180)
                    continue;
                if(shellDark.getBlue() > 50)
                    continue;

                if(!shellDark.equals(new Color(mapImage.getRGB(x + 4, y - 3))))
                    continue;
                if(!shellDark.equals(new Color(mapImage.getRGB(x - 1, y + 5))))
                    continue;
                if(!shellDark.equals(new Color(mapImage.getRGB(x - 6, y + 5))))
                    continue;
                if(!shellDark.equals(new Color(mapImage.getRGB(x, y + 9))))
                    continue;

                System.out.println("TACO DETECTED.\n");
                determineSquareNumber(x, y, mapSolver, "TACO");
                return true;
            }

            //  CHECK FOR MIRROR FORM
            for(int y = 150; y < 572; y++)
            {
                Color shellDark = new Color(mapImage.getRGB(x, y));
                if(shellDark.getRed() < 200)
                    continue;
                if(shellDark.getGreen() < 115 || shellDark.getGreen() > 180)
                    continue;
                if(shellDark.getBlue() > 50)
                    continue;

                if(!shellDark.equals(new Color(mapImage.getRGB(x - 4, y - 3))))
                    continue;
                if(!shellDark.equals(new Color(mapImage.getRGB(x + 1, y + 5))))
                    continue;
                if(!shellDark.equals(new Color(mapImage.getRGB(x + 6, y + 5))))
                    continue;
                if(!shellDark.equals(new Color(mapImage.getRGB(x, y + 9))))
                    continue;

                System.out.println("TACO DETECTED. (MIRROR)\n");
                determineSquareNumber(x, y, mapSolver, "TACO");
                return true;
            }
        }

        System.out.println("ERROR: TACO NOT FOUND");
        return false;
    }

    private static boolean checkIfGarden(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color face = new Color(mapImage.getRGB(x, y));
                if(face.getRed() < 195)
                    continue;
                if(face.getGreen() < 125 || face.getGreen() > 175)
                    continue;
                if(face.getBlue() > 40)
                    continue;

                if(!checkRectanglePattern(face, mapImage, x, y, 1, 0))
                    continue;
                if(!checkRectanglePattern(face, mapImage, x - 2, y + 1, 5, 3))
                    continue;
                if(!checkRectanglePattern(face, mapImage, x - 1, y + 6, 3, 0))
                    continue;

                Color stem = new Color(mapImage.getRGB(x - 4, y + 17));
                if(stem.getRed() < 90 || stem.getRed() > 160)
                    continue;
                if(stem.getGreen() < 150 || stem.getGreen() > 195)
                    continue;
                if(stem.getBlue() < 50 || stem.getBlue() > 110)
                    continue;

                if(!checkRectanglePattern(stem, mapImage, x - 4, y + 17, 9, 1))
                    continue;

                System.out.println("GARDEN DETECTED.\n");
                determineSquareNumber(x, y, mapSolver, "GARDEN");
                return true;
            }
        }

        System.out.println("ERROR: GARDEN NOT FOUND");
        return false;
    }

    private static boolean checkIfGas(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        int numberOfGasPumps = 0;

        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color display = new Color(mapImage.getRGB(x, y));
                if(display.getRed() < 90 || display.getRed() > 120)
                    continue;
                if(display.getGreen() < 100 || display.getGreen() > 125)
                    continue;
                if(display.getBlue() < 110 || display.getBlue() > 140)
                    continue;

                if(!checkRectanglePattern(display, mapImage, x, y, 15, 6))
                    continue;

                Color body = new Color(mapImage.getRGB(x, y + 8));
                if(body.getRed() < 210)
                    continue;
                if(body.getGreen() < 155 || body.getGreen() > 200)
                    continue;
                if(body.getBlue() < 30 || body.getBlue() > 75)
                    continue;

                if(!checkRectanglePattern(body, mapImage, x, y + 8, 13, 7))
                    continue;

                System.out.println("GAS PUMP DETECTED.\n");

                if(numberOfGasPumps == 0)
                    determineSquareNumber(x, y, mapSolver, "GAS1");
                else if(numberOfGasPumps == 1)
                    determineSquareNumber(x, y, mapSolver, "GAS2");
                else if(numberOfGasPumps == 2)
                    determineSquareNumber(x, y, mapSolver, "GAS3");

                numberOfGasPumps++;
            }
        }

        if(numberOfGasPumps < 3)
        {
            System.out.println("ERROR: ONE OR MORE GAS PUMP(S) NOT FOUND");
            return false;
        }

        return true;
    }

    private static boolean checkIfBallroom(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color outwardLeg = new Color(mapImage.getRGB(x, y));
                Color standLeg = new Color(mapImage.getRGB(x + 12, y + 11));
                Color forehead = new Color(mapImage.getRGB(x + 12, y - 10));

                if(outwardLeg.getRed() < 220)
                    continue;
                if(outwardLeg.getGreen() < 205 || outwardLeg.getGreen() > 235)
                    continue;
                if(outwardLeg.getBlue() < 80 || outwardLeg.getBlue() > 110)
                    continue;

                if(outwardLeg.equals(standLeg) && outwardLeg.equals(forehead))
                {
                    System.out.println("BALLROOM DETECTED.\n");
                    determineSquareNumber(x, y, mapSolver, "BALLROOM");
                    return true;
                }
            }

            //  CHECK FOR MIRROR FORM
            for(int y = 150; y < 572; y++)
            {
                Color outwardLeg = new Color(mapImage.getRGB(x, y));
                Color standLeg = new Color(mapImage.getRGB(x - 12, y + 11));
                Color forehead = new Color(mapImage.getRGB(x - 12, y - 10));

                if(outwardLeg.getRed() < 220)
                    continue;
                if(outwardLeg.getGreen() < 205 || outwardLeg.getGreen() > 235)
                    continue;
                if(outwardLeg.getBlue() < 80 || outwardLeg.getBlue() > 110)
                    continue;

                if(outwardLeg.equals(standLeg) && outwardLeg.equals(forehead))
                {
                    System.out.println("BALLROOM DETECTED. (MIRROR)\n");
                    determineSquareNumber(x, y, mapSolver, "BALLROOM");
                    return true;
                }
            }
        }

        System.out.println("ERROR: BALLROOM NOT FOUND");
        return false;
    }

    private static boolean checkIfCoffee(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color trail = new Color(mapImage.getRGB(x, y));
                if(trail.getRed() < 190 || trail.getRed() > 230)
                    continue;
                if(trail.getGreen() < 145 || trail.getGreen() > 180)
                    continue;
                if(trail.getBlue() < 110 || trail.getBlue() > 150)
                    continue;

                //  CHECK TRAIL POINTS
                if(!trail.equals(new Color(mapImage.getRGB(x, y + 1))))
                    continue;
                if(!trail.equals(new Color(mapImage.getRGB(x, y + 2))))
                    continue;
                if(!trail.equals(new Color(mapImage.getRGB(x + 1, y + 3))))
                    continue;
                if(!trail.equals(new Color(mapImage.getRGB(x + 2, y + 5))))
                    continue;
                if(!trail.equals(new Color(mapImage.getRGB(x + 2, y + 6))))
                    continue;

                System.out.println("COFFEE PATTERN DETECTED.\n");
                determineSquareNumber(x, y, mapSolver, "COFFEE");
                return true;
            }

            //  CHECK FOR MIRROR FORM
            for(int y = 150; y < 572; y++)
            {
                Color trail = new Color(mapImage.getRGB(x, y));
                if(trail.getRed() < 190 || trail.getRed() > 230)
                    continue;
                if(trail.getGreen() < 145 || trail.getGreen() > 180)
                    continue;
                if(trail.getBlue() < 110 || trail.getBlue() > 150)
                    continue;

                if(!trail.equals(new Color(mapImage.getRGB(x, y + 1))))
                    continue;
                if(!trail.equals(new Color(mapImage.getRGB(x, y + 2))))
                    continue;
                if(!trail.equals(new Color(mapImage.getRGB(x - 1, y + 3))))
                    continue;
                if(!trail.equals(new Color(mapImage.getRGB(x - 2, y + 5))))
                    continue;
                if(!trail.equals(new Color(mapImage.getRGB(x - 2, y + 6))))
                    continue;

                System.out.println("COFFEE PATTERN DETECTED. (MIRROR)\n");
                determineSquareNumber(x, y, mapSolver, "COFFEE");
                return true;
            }
        }

        System.out.println("ERROR: COFFEE NOT FOUND");
        return false;
    }

    private static void checkIfMall(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color brightHandle = new Color(mapImage.getRGB(x, y));
                if(brightHandle.getRed() < 205)
                    continue;
                if(brightHandle.getGreen() < 185 || brightHandle.getGreen() > 230)
                    continue;
                if(brightHandle.getBlue() < 40 || brightHandle.getBlue() > 100)
                    continue;

                if(!checkRectanglePattern(brightHandle, mapImage, x, y, 0, 1))
                    continue;
                if(!checkRectanglePattern(brightHandle, mapImage, x + 1, y - 2, 0, 1))
                    continue;
                if(!checkRectanglePattern(brightHandle, mapImage, x + 3, y - 3, 1, 0))
                    continue;
                if(!checkRectanglePattern(brightHandle, mapImage, x + 11, y, 0, 2))
                    continue;

                Color bag = new Color(mapImage.getRGB(x, y + 5));
                if(bag.getRed() < 80 || bag.getRed() > 115)
                    continue;
                if(bag.getGreen() < 70 || bag.getGreen() > 105)
                    continue;
                if(bag.getBlue() < 155 || bag.getBlue() > 190)
                    continue;

                if(!checkRectanglePattern(bag, mapImage, x, y + 5, 12, 16))
                    continue;

                System.out.println("MALL DETECTED.\n");
                determineSquareNumber(x, y, mapSolver, "MALL");
                return;
            }

            //  CHECK FOR MIRROR
            for(int y = 150; y < 572; y++)
            {
                Color brightHandle = new Color(mapImage.getRGB(x, y));
                if(brightHandle.getRed() < 205)
                    continue;
                if(brightHandle.getGreen() < 185 || brightHandle.getGreen() > 230)
                    continue;
                if(brightHandle.getBlue() < 40 || brightHandle.getBlue() > 100)
                    continue;

                if(!checkRectanglePattern(brightHandle, mapImage, x, y, 0, 2))
                    continue;
                if(!checkRectanglePattern(brightHandle, mapImage, x + 11, y, 0, 1))
                    continue;
                if(!checkRectanglePattern(brightHandle, mapImage, x + 9, y - 2, 1, 0))
                    continue;
                if(!checkRectanglePattern(brightHandle, mapImage, x + 7, y - 3, 1, 0))
                    continue;

                Color bag = new Color(mapImage.getRGB(x, y + 5));
                if(bag.getRed() < 80 || bag.getRed() > 140)
                    continue;
                if(bag.getGreen() < 70 || bag.getGreen() > 105)
                    continue;
                if(bag.getBlue() < 155 || bag.getBlue() > 190)
                    continue;

                if(!checkRectanglePattern(bag, mapImage, x, y + 5, 12, 1))
                    continue;

                System.out.println("MALL DETECTED. (MIRROR)\n");
                determineSquareNumber(x, y, mapSolver, "MALL");
                return;
            }
        }
    }

    private static boolean checkIfHome(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color topLeftWindow = new Color(mapImage.getRGB(x, y));
                if(topLeftWindow.getRed() < 60 || topLeftWindow.getRed() > 115)
                    continue;
                if(topLeftWindow.getGreen() < 160 || topLeftWindow.getGreen() > 190)
                    continue;
                if(topLeftWindow.getBlue() < 210 || topLeftWindow.getBlue() > 255)
                    continue;

                if(!checkRectanglePattern(topLeftWindow, mapImage, x, y, 1, 1))
                    continue;

                Color topRightWindow = new Color(mapImage.getRGB(x + 10, y));
                if(!checkRectanglePattern(topRightWindow, mapImage, x + 10, y, 2, 1))
                    continue;

                Color bottomWindowOne = new Color(mapImage.getRGB(x, y + 7));
                if(!checkRectanglePattern(bottomWindowOne, mapImage, x, y + 7, 1, 2))
                    continue;

                if(topLeftWindow.equals(topRightWindow) && topLeftWindow.equals(bottomWindowOne))
                {
                    System.out.println("HOME DETECTED.\n");
                    determineSquareNumber(x, y, mapSolver, "HOME");

                    return true;
                }
            }

            //  CHECK MIRROR FORM
            for(int y = 150; y < 572; y++)
            {
                Color topRightWindow = new Color(mapImage.getRGB(x, y));
                if(topRightWindow.getRed() < 60 || topRightWindow.getRed() > 115)
                    continue;
                if(topRightWindow.getGreen() < 160 || topRightWindow.getGreen() > 190)
                    continue;
                if(topRightWindow.getBlue() < 210 || topRightWindow.getBlue() > 255)
                    continue;

                if(!checkRectanglePattern(topRightWindow, mapImage, x, y, 1, 1))
                    continue;

                Color topLeftWindow = new Color(mapImage.getRGB(x - 11, y));
                if(!checkRectanglePattern(topLeftWindow, mapImage, x - 11, y, 2, 1))
                    continue;

                Color bottomRightWindow = new Color(mapImage.getRGB(x, y + 7));
                if(!checkRectanglePattern(bottomRightWindow, mapImage, x, y + 7, 1, 2))
                    continue;

                if(topRightWindow.equals(topLeftWindow) && topRightWindow.equals(bottomRightWindow))
                {
                    System.out.println("HOME PATTERN DETECTED. (MIRROR)\n");
                    determineSquareNumber(x, y, mapSolver, "HOME");

                    return true;
                }
            }
        }

        System.out.println("ERROR: HOME NOT FOUND");
        return false;
    }

    private static boolean checkIfRing(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color darkShade = new Color(mapImage.getRGB(x, y));
                if(darkShade.getRed() < 60 || darkShade.getRed() > 120)
                    continue;
                if(darkShade.getGreen() < 150 || darkShade.getGreen() > 195)
                    continue;
                if(darkShade.getBlue() < 215)
                    continue;

                if(!checkRectanglePattern(darkShade, mapImage, x, y, 5, 3))
                    continue;
                if(!checkRectanglePattern(darkShade, mapImage, x - 4, y + 5, 3, 2))
                    continue;

                Color topBand = new Color(mapImage.getRGB(x - 5, y + 9));
                if(topBand.getRed() < 130 || topBand.getRed() > 180)
                    continue;
                if(topBand.getGreen() < 140 || topBand.getGreen() > 200)
                    continue;
                if(topBand.getBlue() < 150 || topBand.getBlue() > 210)
                    continue;

                if(!checkRectanglePattern(topBand, mapImage, x - 5, y + 9, 9, 3))
                    continue;

                System.out.println("RING DETECTED.\n");
                determineSquareNumber(x, y, mapSolver, "RING");

                return true;
            }

            //  CHECK FOR MIRROR FORM
            for(int y = 150; y < 572; y++)
            {
                Color darkShade = new Color(mapImage.getRGB(x, y));
                if(darkShade.getRed() < 60 || darkShade.getRed() > 120)
                    continue;
                if(darkShade.getGreen() < 150 || darkShade.getGreen() > 195)
                    continue;
                if(darkShade.getBlue() < 215)
                    continue;

                if(!checkRectanglePattern(darkShade, mapImage, x, y, 5, 3))
                    continue;
                if(!checkRectanglePattern(darkShade, mapImage, x + 6, y + 5, 3, 2))
                    continue;

                Color topBand = new Color(mapImage.getRGB(x + 1, y + 9));
                if(topBand.getRed() < 130 || topBand.getRed() > 180)
                    continue;
                if(topBand.getGreen() < 140 || topBand.getGreen() > 200)
                    continue;
                if(topBand.getBlue() < 150 || topBand.getBlue() > 210)
                    continue;

                if(!checkRectanglePattern(topBand, mapImage, x + 1, y + 9, 9, 3))
                    continue;

                System.out.println("RING DETECTED. (MIRROR)\n");
                determineSquareNumber(x, y, mapSolver, "RING");

                return true;
            }
        }

        return false;
    }

    //  NOTE: This method is most likely never going to actually see any use, but is still
    //      good to have just in case.
    private static boolean checkIfAirplane(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color tailfin = new Color(mapImage.getRGB(x, y));
                if(tailfin.getRed() < 60 || tailfin.getRed() > 105)
                    continue;
                if(tailfin.getGreen() < 155 || tailfin.getGreen() > 190)
                    continue;
                if(tailfin.getBlue() < 230)
                    continue;

                if(!checkRectanglePattern(tailfin, mapImage, x, y, 2, 1))
                    continue;
                if(!checkRectanglePattern(tailfin, mapImage, x + 1, y + 2, 1, 0))
                    continue;

                if(!checkRectanglePattern(tailfin, mapImage, x + 6, y + 6, 0, 1))
                    continue;
                if(!checkRectanglePattern(tailfin, mapImage, x + 7, y + 6, 1, 2))
                    continue;

                System.out.println("AIRPLANE DETECTED.\n");
                determineSquareNumber(x, y, mapSolver, "AIRPLANE");
                return true;
            }

            //  CHECK FOR MIRROR FORM
            for(int y = 150; y < 572; y++)
            {
                Color tailfin = new Color(mapImage.getRGB(x, y));
                if(tailfin.getRed() < 60 || tailfin.getRed() > 105)
                    continue;
                if(tailfin.getGreen() < 155 || tailfin.getGreen() > 190)
                    continue;
                if(tailfin.getBlue() < 230)
                    continue;

                if(!checkRectanglePattern(tailfin, mapImage, x, y, 2, 1))
                    continue;
                if(!checkRectanglePattern(tailfin, mapImage, x, y + 2, 1, 0))
                    continue;

                if(!checkRectanglePattern(tailfin, mapImage, x - 6, y + 6, 2, 1))
                    continue;
                if(!checkRectanglePattern(tailfin, mapImage, x - 6, y + 8, 1, 0))
                    continue;

                System.out.println("AIRPLANE DETECTED. (MIRROR)\n");
                determineSquareNumber(x, y, mapSolver, "AIRPLANE");
                return true;
            }
        }

        System.out.println("ERROR: AIRPLANE NOT FOUND");
        return false;
    }

    /*
        HELPER: Given the [x, y] coordinates of a pixel, determine what square and nodes the specified
            resource is linked to/associated with.

        Parameters:
            'x' (int)
                x coordinate of a given pixel.
            'y' (int)
                y coordinate of a given pixel.
            'mapSolver' (karuta.ChromeBotMapSolver)
                Reference to the instance of the class responsible for solving the actual date
                    mini-game.
            'resource' (String)
                Name of the resource that the pixel belongs to.

     */
    private static void determineSquareNumber(int x, int y, ChromeBotMapSolver mapSolver, String resource)
    {
        System.out.print("\tROW ");

        if(y > 485)
        {
            //  ROW 6
            System.out.print(" 6, COLUMN ");

            //  COLUMN 4
            if(x > 600)
            {
                System.out.println(" 4");

                mapSolver.getNode(81).linkNodeToResource(resource);
                mapSolver.getNode(76).linkNodeToResource(resource);
                mapSolver.getNode(75).linkNodeToResource(resource);
                mapSolver.getNode(70).linkNodeToResource(resource);
            }
                //  COLUMN 3
            else if(x > 485)
            {
                System.out.println(" 3");

                mapSolver.getNode(80).linkNodeToResource(resource);
                mapSolver.getNode(75).linkNodeToResource(resource);
                mapSolver.getNode(74).linkNodeToResource(resource);
                mapSolver.getNode(69).linkNodeToResource(resource);
            }
                //  COLUMN 2
            else if(x > 365)
            {
                System.out.println(" 2");

                mapSolver.getNode(79).linkNodeToResource(resource);
                mapSolver.getNode(74).linkNodeToResource(resource);
                mapSolver.getNode(73).linkNodeToResource(resource);
                mapSolver.getNode(68).linkNodeToResource(resource);
            }
                //  COLUMN 1
            else if(x > 245)
            {
                System.out.println(" 1");

                mapSolver.getNode(78).linkNodeToResource(resource);
                mapSolver.getNode(73).linkNodeToResource(resource);
                mapSolver.getNode(72).linkNodeToResource(resource);
                mapSolver.getNode(67).linkNodeToResource(resource);
            }
                //  COLUMN 0
            else
            {
                System.out.println(" 0");

                mapSolver.getNode(77).linkNodeToResource(resource);
                mapSolver.getNode(72).linkNodeToResource(resource);
                mapSolver.getNode(71).linkNodeToResource(resource);
                mapSolver.getNode(66).linkNodeToResource(resource);
            }
        }
        else if(y > 395)
        {
            //  ROW 5
            System.out.print(" 5, COLUMN ");

            //  COLUMN 4
            if(x > 575)
            {
                System.out.println(" 4");

                mapSolver.getNode(70).linkNodeToResource(resource);
                mapSolver.getNode(65).linkNodeToResource(resource);
                mapSolver.getNode(64).linkNodeToResource(resource);
                mapSolver.getNode(59).linkNodeToResource(resource);
            }
                //  COLUMN 3
            else if(x > 475)
            {
                System.out.println(" 3");

                mapSolver.getNode(69).linkNodeToResource(resource);
                mapSolver.getNode(64).linkNodeToResource(resource);
                mapSolver.getNode(63).linkNodeToResource(resource);
                mapSolver.getNode(58).linkNodeToResource(resource);
            }
                //  COLUMN 2
            else if(x > 360)
            {
                System.out.println(" 2");

                mapSolver.getNode(68).linkNodeToResource(resource);
                mapSolver.getNode(63).linkNodeToResource(resource);
                mapSolver.getNode(62).linkNodeToResource(resource);
                mapSolver.getNode(57).linkNodeToResource(resource);
            }
                //  COLUMN 1
            else if(x > 265)
            {
                System.out.println(" 1");

                mapSolver.getNode(67).linkNodeToResource(resource);
                mapSolver.getNode(62).linkNodeToResource(resource);
                mapSolver.getNode(61).linkNodeToResource(resource);
                mapSolver.getNode(56).linkNodeToResource(resource);
            }
                //  COLUMN 0
            else
            {
                System.out.println(" 0");

                mapSolver.getNode(66).linkNodeToResource(resource);
                mapSolver.getNode(61).linkNodeToResource(resource);
                mapSolver.getNode(60).linkNodeToResource(resource);
                mapSolver.getNode(55).linkNodeToResource(resource);
            }
        }
        else if(y > 320)
        {
            //  ROW 4
            System.out.print(" 4, COLUMN ");

            //  COLUMN 4
            if(x > 555)
            {
                System.out.println(" 4");

                mapSolver.getNode(59).linkNodeToResource(resource);
                mapSolver.getNode(54).linkNodeToResource(resource);
                mapSolver.getNode(53).linkNodeToResource(resource);
                mapSolver.getNode(48).linkNodeToResource(resource);
            }
                //  COLUMN 3
            else if(x > 465)
            {
                System.out.println(" 3");

                mapSolver.getNode(58).linkNodeToResource(resource);
                mapSolver.getNode(53).linkNodeToResource(resource);
                mapSolver.getNode(52).linkNodeToResource(resource);
                mapSolver.getNode(47).linkNodeToResource(resource);
            }
                //  COLUMN 2
            else if(x > 370)
            {
                System.out.println(" 2");

                mapSolver.getNode(57).linkNodeToResource(resource);
                mapSolver.getNode(52).linkNodeToResource(resource);
                mapSolver.getNode(51).linkNodeToResource(resource);
                mapSolver.getNode(46).linkNodeToResource(resource);
            }
                //  COLUMN 1
            else if(x > 280)
            {
                System.out.println(" 1");

                mapSolver.getNode(56).linkNodeToResource(resource);
                mapSolver.getNode(51).linkNodeToResource(resource);
                mapSolver.getNode(50).linkNodeToResource(resource);
                mapSolver.getNode(45).linkNodeToResource(resource);
            }
                //  COLUMN 0
            else
            {
                System.out.println(" 0");

                mapSolver.getNode(55).linkNodeToResource(resource);
                mapSolver.getNode(50).linkNodeToResource(resource);
                mapSolver.getNode(49).linkNodeToResource(resource);
                mapSolver.getNode(44).linkNodeToResource(resource);
            }
        }
        else if(y > 260)
        {
            //  ROW 3
            System.out.print(" 3, COLUMN ");

            //  COLUMN 4
            if(x > 540)
            {
                System.out.println(" 4");

                mapSolver.getNode(48).linkNodeToResource(resource);
                mapSolver.getNode(43).linkNodeToResource(resource);
                mapSolver.getNode(42).linkNodeToResource(resource);
                mapSolver.getNode(37).linkNodeToResource(resource);
            }
                //  COLUMN 3
            else if(x > 455)
            {
                System.out.println(" 3");

                mapSolver.getNode(47).linkNodeToResource(resource);
                mapSolver.getNode(42).linkNodeToResource(resource);
                mapSolver.getNode(41).linkNodeToResource(resource);
                mapSolver.getNode(36).linkNodeToResource(resource);
            }
                //  COLUMN 2
            else if(x > 375)
            {
                System.out.println(" 2");

                mapSolver.getNode(46).linkNodeToResource(resource);
                mapSolver.getNode(41).linkNodeToResource(resource);
                mapSolver.getNode(40).linkNodeToResource(resource);
                mapSolver.getNode(35).linkNodeToResource(resource);
            }
                //  COLUMN 1
            else if(x > 295)
            {
                System.out.println(" 1");

                mapSolver.getNode(45).linkNodeToResource(resource);
                mapSolver.getNode(40).linkNodeToResource(resource);
                mapSolver.getNode(39).linkNodeToResource(resource);
                mapSolver.getNode(34).linkNodeToResource(resource);
            }
                //  COLUMN 0
            else
            {
                System.out.println(" 0");

                mapSolver.getNode(44).linkNodeToResource(resource);
                mapSolver.getNode(39).linkNodeToResource(resource);
                mapSolver.getNode(38).linkNodeToResource(resource);
                mapSolver.getNode(33).linkNodeToResource(resource);
            }
        }
        else if(y > 220)
        {
            //  ROW 2
            System.out.print(" 2, COLUMN ");

            //  COLUMN 4
            if(x > 525)
            {
                System.out.println(" 4");

                mapSolver.getNode(37).linkNodeToResource(resource);
                mapSolver.getNode(32).linkNodeToResource(resource);
                mapSolver.getNode(31).linkNodeToResource(resource);
                mapSolver.getNode(26).linkNodeToResource(resource);
            }
                //  COLUMN 3
            else if(x > 450)
            {
                System.out.println(" 3");

                mapSolver.getNode(36).linkNodeToResource(resource);
                mapSolver.getNode(31).linkNodeToResource(resource);
                mapSolver.getNode(30).linkNodeToResource(resource);
                mapSolver.getNode(25).linkNodeToResource(resource);
            }
                //  COLUMN 2
            else if(x > 380)
            {
                System.out.println(" 2");

                mapSolver.getNode(35).linkNodeToResource(resource);
                mapSolver.getNode(30).linkNodeToResource(resource);
                mapSolver.getNode(29).linkNodeToResource(resource);
                mapSolver.getNode(24).linkNodeToResource(resource);
            }
                //  COLUMN 1
            else if(x > 305)
            {
                System.out.println(" 1");

                mapSolver.getNode(34).linkNodeToResource(resource);
                mapSolver.getNode(29).linkNodeToResource(resource);
                mapSolver.getNode(28).linkNodeToResource(resource);
                mapSolver.getNode(23).linkNodeToResource(resource);
            }
                //  COLUMN 0
            else
            {
                System.out.println(" 0");

                mapSolver.getNode(33).linkNodeToResource(resource);
                mapSolver.getNode(28).linkNodeToResource(resource);
                mapSolver.getNode(27).linkNodeToResource(resource);
                mapSolver.getNode(22).linkNodeToResource(resource);
            }

        }
        else if(y > 185)
        {
            //  ROW 1
            System.out.print(" 1, COLUMN ");

            //  COLUMN 4
            if(x > 515)
            {
                System.out.println(" 4");

                mapSolver.getNode(26).linkNodeToResource(resource);
                mapSolver.getNode(21).linkNodeToResource(resource);
                mapSolver.getNode(20).linkNodeToResource(resource);
                mapSolver.getNode(15).linkNodeToResource(resource);
            }
                //  COLUMN 3
            else if(x > 445)
            {
                System.out.println(" 3");

                mapSolver.getNode(25).linkNodeToResource(resource);
                mapSolver.getNode(20).linkNodeToResource(resource);
                mapSolver.getNode(19).linkNodeToResource(resource);
                mapSolver.getNode(14).linkNodeToResource(resource);
            }
                //  COLUMN 2
            else if(x > 375)
            {
                System.out.println(" 2");

                mapSolver.getNode(24).linkNodeToResource(resource);
                mapSolver.getNode(19).linkNodeToResource(resource);
                mapSolver.getNode(18).linkNodeToResource(resource);
                mapSolver.getNode(13).linkNodeToResource(resource);
            }
                //  COLUMN 1
            else if(x > 310)
            {
                System.out.println(" 1");

                mapSolver.getNode(23).linkNodeToResource(resource);
                mapSolver.getNode(18).linkNodeToResource(resource);
                mapSolver.getNode(17).linkNodeToResource(resource);
                mapSolver.getNode(12).linkNodeToResource(resource);
            }
                //  COLUMN 0
            else
            {
                System.out.println(" 0");

                mapSolver.getNode(22).linkNodeToResource(resource);
                mapSolver.getNode(17).linkNodeToResource(resource);
                mapSolver.getNode(16).linkNodeToResource(resource);
                mapSolver.getNode(11).linkNodeToResource(resource);
            }
        }
        else
        {
            //  ROW 0
            System.out.print(" 0, COLUMN ");

            //  COLUMN 4
            if(x > 500)
            {
                System.out.println(" 4");

                mapSolver.getNode(15).linkNodeToResource(resource);
                mapSolver.getNode(10).linkNodeToResource(resource);
                mapSolver.getNode(9).linkNodeToResource(resource);
                mapSolver.getNode(4).linkNodeToResource(resource);
            }
                //  COLUMN 3
            else if(x > 435)
            {
                System.out.println(" 3");

                mapSolver.getNode(14).linkNodeToResource(resource);
                mapSolver.getNode(9).linkNodeToResource(resource);
                mapSolver.getNode(8).linkNodeToResource(resource);
                mapSolver.getNode(3).linkNodeToResource(resource);
            }
                //  COLUMN 2
            else if(x > 375)
            {
                System.out.println(" 2");

                mapSolver.getNode(13).linkNodeToResource(resource);
                mapSolver.getNode(8).linkNodeToResource(resource);
                mapSolver.getNode(7).linkNodeToResource(resource);
                mapSolver.getNode(2).linkNodeToResource(resource);
            }
                //  COLUMN 1
            else if(x > 310)
            {
                System.out.println(" 1");

                mapSolver.getNode(12).linkNodeToResource(resource);
                mapSolver.getNode(7).linkNodeToResource(resource);
                mapSolver.getNode(6).linkNodeToResource(resource);
                mapSolver.getNode(1).linkNodeToResource(resource);
            }
                //  COLUMN 0
            else
            {
                System.out.println(" 0");

                mapSolver.getNode(11).linkNodeToResource(resource);
                mapSolver.getNode(6).linkNodeToResource(resource);
                mapSolver.getNode(5).linkNodeToResource(resource);
                mapSolver.getNode(0).linkNodeToResource(resource);
            }
        }
    }

    /*
        NOTE: Pre-decided/Hard coded locations/color(s) on the map to determine whether or not a 'node'
            is inaccessible or not.
     */
    private static void checkInaccessibleNodes(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        if(!new Color(mapImage.getRGB(306, 180)).equals(new Color(95, 96, 95)))
            mapSolver.getNode(6).setInaccessible(true);
        if(!new Color(mapImage.getRGB(367, 180)).equals(new Color(96, 97, 96)))
            mapSolver.getNode(7).setInaccessible(true);
        if(!new Color(mapImage.getRGB(428, 180)).equals(new Color(93, 95, 94)))
            mapSolver.getNode(8).setInaccessible(true);
        if(!new Color(mapImage.getRGB(487, 180)).equals(new Color(96, 97, 96)))
            mapSolver.getNode(9).setInaccessible(true);

        if(!new Color(mapImage.getRGB(290, 197)).equals(new Color(95, 97, 95)))
            mapSolver.getNode(11).setInaccessible(true);
        if(!new Color(mapImage.getRGB(359, 197)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(12).setInaccessible(true);
        if(!new Color(mapImage.getRGB(380, 197)).equals(new Color(94, 95, 94)))
            mapSolver.getNode(13).setInaccessible(true);
        if(!new Color(mapImage.getRGB(440, 197)).equals(new Color(92, 95, 93)))
            mapSolver.getNode(14).setInaccessible(true);
        if(!new Color(mapImage.getRGB(505, 197)).equals(new Color(95, 96, 94)))
            mapSolver.getNode(15).setInaccessible(true);

        if(!new Color(mapImage.getRGB(304, 215)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(17).setInaccessible(true);
        if(!new Color(mapImage.getRGB(364, 215)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(18).setInaccessible(true);
        if(!new Color(mapImage.getRGB(429, 215)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(19).setInaccessible(true);
        if(!new Color(mapImage.getRGB(496, 215)).equals(new Color(89, 91, 90)))
            mapSolver.getNode(20).setInaccessible(true);

        if(!new Color(mapImage.getRGB(280, 231)).equals(new Color(93, 94, 92)))
            mapSolver.getNode(22).setInaccessible(true);
        if(!new Color(mapImage.getRGB(306, 231)).equals(new Color(92, 95, 93)))
            mapSolver.getNode(23).setInaccessible(true);
        if(!new Color(mapImage.getRGB(380, 231)).equals(new Color(93, 94, 92)))
            mapSolver.getNode(24).setInaccessible(true);
        if(!new Color(mapImage.getRGB(450, 231)).equals(new Color(93, 95, 94)))
            mapSolver.getNode(25).setInaccessible(true);
        if(!new Color(mapImage.getRGB(515, 231)).equals(new Color(94, 95, 94)))
            mapSolver.getNode(26).setInaccessible(true);

        if(!new Color(mapImage.getRGB(295, 250)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(28).setInaccessible(true);
        if(!new Color(mapImage.getRGB(366, 250)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(29).setInaccessible(true);
        if(!new Color(mapImage.getRGB(440, 250)).equals(new Color(92, 93, 91)))
            mapSolver.getNode(30).setInaccessible(true);
        if(!new Color(mapImage.getRGB(504, 250)).equals(new Color(95, 96, 95)))
            mapSolver.getNode(31).setInaccessible(true);

        if(!new Color(mapImage.getRGB(250, 275)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(33).setInaccessible(true);
        if(!new Color(mapImage.getRGB(330, 275)).equals(new Color(95, 97, 96)))
            mapSolver.getNode(34).setInaccessible(true);
        if(!new Color(mapImage.getRGB(400, 275)).equals(new Color(93, 95, 94)))
            mapSolver.getNode(35).setInaccessible(true);
        if(!new Color(mapImage.getRGB(454, 275)).equals(new Color(93, 95, 94)))
            mapSolver.getNode(36).setInaccessible(true);
        if(!new Color(mapImage.getRGB(530, 275)).equals(new Color(93, 95, 94)))
            mapSolver.getNode(37).setInaccessible(true);

        if(!new Color(mapImage.getRGB(278, 295)).equals(new Color(95, 96, 95)))
            mapSolver.getNode(39).setInaccessible(true);
        if(!new Color(mapImage.getRGB(357, 295)).equals(new Color(95, 96, 95)))
            mapSolver.getNode(40).setInaccessible(true);
        if(!new Color(mapImage.getRGB(435, 295)).equals(new Color(95, 96, 95)))
            mapSolver.getNode(41).setInaccessible(true);
        if(!new Color(mapImage.getRGB(522, 295)).equals(new Color(94, 95, 94)))
            mapSolver.getNode(42).setInaccessible(true);

        if(!new Color(mapImage.getRGB(232, 325)).equals(new Color(91, 92, 91)))
            mapSolver.getNode(44).setInaccessible(true);
        if(!new Color(mapImage.getRGB(316, 325)).equals(new Color(91, 92, 91)))
            mapSolver.getNode(45).setInaccessible(true);
        if(!new Color(mapImage.getRGB(400, 325)).equals(new Color(91, 92, 91)))
            mapSolver.getNode(46).setInaccessible(true);
        if(!new Color(mapImage.getRGB(485, 325)).equals(new Color(93, 95, 93)))
            mapSolver.getNode(47).setInaccessible(true);
        if(!new Color(mapImage.getRGB(565, 325)).equals(new Color(92, 93, 91)))
            mapSolver.getNode(48).setInaccessible(true);

        if(!new Color(mapImage.getRGB(262, 355)).equals(new Color(91, 94, 92)))
            mapSolver.getNode(50).setInaccessible(true);
        if(!new Color(mapImage.getRGB(352, 355)).equals(new Color(92, 95, 92)))
            mapSolver.getNode(51).setInaccessible(true);
        if(!new Color(mapImage.getRGB(441, 355)).equals(new Color(93, 96, 93)))
            mapSolver.getNode(52).setInaccessible(true);
        if(!new Color(mapImage.getRGB(536, 355)).equals(new Color(92, 95, 93)))
            mapSolver.getNode(53).setInaccessible(true);

        if(!new Color(mapImage.getRGB(208, 395)).equals(new Color(92, 93, 92)))
            mapSolver.getNode(55).setInaccessible(true);
        if(!new Color(mapImage.getRGB(306, 395)).equals(new Color(92, 93, 92)))
            mapSolver.getNode(56).setInaccessible(true);
        if(!new Color(mapImage.getRGB(400, 395)).equals(new Color(91, 92, 91)))
            mapSolver.getNode(57).setInaccessible(true);
        if(!new Color(mapImage.getRGB(495, 395)).equals(new Color(91, 92, 91)))
            mapSolver.getNode(58).setInaccessible(true);
        if(!new Color(mapImage.getRGB(590, 395)).equals(new Color(92, 93, 91)))
            mapSolver.getNode(59).setInaccessible(true);

        if(!new Color(mapImage.getRGB(244, 430)).equals(new Color(91, 93, 91)))
            mapSolver.getNode(61).setInaccessible(true);
        if(!new Color(mapImage.getRGB(345, 430)).equals(new Color(91, 93, 91)))
            mapSolver.getNode(62).setInaccessible(true);
        if(!new Color(mapImage.getRGB(447, 430)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(63).setInaccessible(true);
        if(!new Color(mapImage.getRGB(548, 430)).equals(new Color(91, 94, 92)))
            mapSolver.getNode(64).setInaccessible(true);

        if(!new Color(mapImage.getRGB(182, 471)).equals(new Color(92, 93, 91)))
            mapSolver.getNode(66).setInaccessible(true);
        if(!new Color(mapImage.getRGB(291, 471)).equals(new Color(92, 93, 91)))
            mapSolver.getNode(67).setInaccessible(true);
        if(!new Color(mapImage.getRGB(400, 471)).equals(new Color(91, 92, 91)))
            mapSolver.getNode(68).setInaccessible(true);
        if(!new Color(mapImage.getRGB(510, 471)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(69).setInaccessible(true);
        if(!new Color(mapImage.getRGB(620, 471)).equals(new Color(91, 93, 91)))
            mapSolver.getNode(70).setInaccessible(true);

        if(!new Color(mapImage.getRGB(220, 522)).equals(new Color(91, 93, 91)))
            mapSolver.getNode(72).setInaccessible(true);
        if(!new Color(mapImage.getRGB(337, 522)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(73).setInaccessible(true);
        if(!new Color(mapImage.getRGB(452, 522)).equals(new Color(91, 93, 91)))
            mapSolver.getNode(74).setInaccessible(true);
        if(!new Color(mapImage.getRGB(570, 522)).equals(new Color(92, 95, 92)))
            mapSolver.getNode(75).setInaccessible(true);

    }

    //  NOTE: Check for monochromatic rectangle pattern.
    private static boolean checkRectanglePattern(Color anchor, BufferedImage mapImage, int x, int y, int maxOffsetX, int maxOffsetY)
    {
        for(int xOffset = 0; xOffset < maxOffsetX; xOffset++)
        {
            for(int yOffset = 0; yOffset < maxOffsetY; yOffset++)
            {
                if(!new Color(mapImage.getRGB(x + xOffset, y + yOffset)).equals(anchor))
                    return false;
            }
        }

        return true;
    }

    //  HELPER: Check 'straw' color correctness, when finding Juice resource.
    private static boolean isStrawColorCorrect(Color straw)
    {
        //  Color straw = new Color(mapImage.getRGB(x, y));
        if(straw.getRed() < 190 || straw.getRed() > 220)
            return false;
        if(straw.getGreen() < 205 || straw.getGreen() > 225)
            return false;
        if(straw.getBlue() < 205 || straw.getBlue() > 240)
            return false;

        return true;
    }

    //  HELPER: Check 'redArt' color correctness, when finding Juice resource.
    private static boolean isRedArtColorCorrect(Color redArt)
    {
        if(redArt.getRed() < 215)
            return false;
        if(redArt.getGreen() < 70 || redArt.getGreen() > 110)
            return false;
        if(redArt.getBlue() < 75 || redArt.getBlue() > 140)
            return false;

        return true;
    }

    //  HELPER: Check 'cheese' color correctness, when finding Spaghetti resource.
    private static boolean isCheeseColorCorrect(Color cheese)
    {
        if(cheese.getRed() < 235)
            return false;
        if(cheese.getGreen() < 210)
            return false;
        if(cheese.getBlue() < 170 || cheese.getBlue() > 210)
            return false;

        return true;
    }

    //  HELPER: Check 'sauce' color correctness, when finding Spaghetti resource.
    private static boolean isSauceColorCorrect(Color sauce)
    {
        if (sauce.getRed() < 225)
            return false;
        if (sauce.getGreen() < 70 || sauce.getGreen() > 100)
            return false;
        if (sauce.getBlue() < 80 || sauce.getBlue() > 150)
            return false;

        return true;
    }

    //  HELPER: Check 'fork' color correctness, when finding Spaghetti resource.
    private static boolean isForkColorCorrect(Color fork)
    {
        if(fork.getRed() < 130 || fork.getRed() > 170)
            return false;
        if(fork.getGreen() < 150 || fork.getGreen() > 195)
            return false;
        if(fork.getBlue() < 150 || fork.getBlue() > 200)
            return false;

        return true;
    }

}
