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
        if(!findHome(mapImage, mapSolver))
            return "HOME NOT FOUND";
        if(!findBallroom(mapImage, mapSolver))
            return "BALLROOM NOT FOUND";
        if(!findGas(mapImage, mapSolver))
            return "GAS NOT FOUND";
        if(!findGarden(mapImage, mapSolver))
            return "GARDEN NOT FOUND";
        if(!findCoffee(mapImage, mapSolver))
            return "COFFEE NOT FOUND";
        if(!findTaco(mapImage, mapSolver))
            return "TACO NOT FOUND";
        if(!findTheater(mapImage, mapSolver))
            return "THEATER NOT FOUND";
        if(!findBar(mapImage, mapSolver))
            return "BAR NOT FOUND";
        if(!findSandwich(mapImage, mapSolver))
            return "SANDWICH NOT FOUND";
        if(!findFair(mapImage, mapSolver))
            return "FAIR NOT FOUND";
        if(!findSpaghetti(mapImage, mapSolver))
            return "SPAGHETTI NOT FOUND";
        if(!findJuice(mapImage, mapSolver))
            return "JUICE NOT FOUND";

        //  NOTE: Shopping Mall is NOT a CRITICAL resource.
        findMall(mapImage, mapSolver);

        //  NOTE: Airport is NOT a CRITICAL resource.
        findAirplane(mapImage, mapSolver);

        //  NOTE: Ring is NOT a CRITICAL resource.
        mapSolver.setRing(findRing(mapImage, mapSolver));

        //  NOTE: MARK Accessible nodes
        findAccessibleNodes(mapImage, mapSolver);

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

        return timeBar.equals(new Color(0, 0, 0));
    }

    /*
        NOTE: The below 'find' methods all basically search for certain patterns of pixels
            for respective resources.
     */

    private static boolean findJuice(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color straw = new Color(mapImage.getRGB(x, y));

                //  NOTE: The common denominator/common pixel that both the normal and mirror forms of this pattern
                //      is on the same pixel, for the straw color.
                if(isStrawColorCorrect(straw))
                {
                    //  CHECK NORMAL FORM.
                    if (straw.equals(new Color(mapImage.getRGB(x + 1, y - 1))) &&
                            straw.equals(new Color(mapImage.getRGB(x + 3, y - 2))) &&
                            checkRectanglePattern(straw, mapImage, x + 6, y, 0, 3))
                    {
                        Color redArt = new Color(mapImage.getRGB(x + 10, y + 12));

                        if (isRedArtColorCorrect(redArt) &&
                                checkRectanglePattern(redArt, mapImage, x + 10, y + 12, 4, 0))
                        {
                            Color juiceBoxDark = new Color(mapImage.getRGB(x + 1, y + 6));

                            if (isJuiceBoxDarkColorCorrect(juiceBoxDark) &&
                                    checkRectanglePattern(juiceBoxDark, mapImage, x + 1, y + 6, 1, 15))
                            {
                                System.out.println("JUICE DETECTED.");
                                determineSquareNumber(x, y, mapSolver, "JUICE");
                                return true;
                            }
                        }

                    }

                    //  CHECK MIRROR FORM IF NORMAL FORM IS UNSUCCESSFUL.
                    if(straw.equals(new Color(mapImage.getRGB(x - 1, y - 1))) &&
                            straw.equals(new Color(mapImage.getRGB(x - 3, y - 2))) &&
                            checkRectanglePattern(straw, mapImage, x - 6, y, 0, 3))
                    {
                        Color redArt = new Color(mapImage.getRGB(x - 14, y + 12));

                        if(isRedArtColorCorrect(redArt) &&
                                checkRectanglePattern(redArt, mapImage, x - 14, y + 12, 4, 0))
                        {
                            Color juiceBoxDark = new Color(mapImage.getRGB(x - 2, y + 6));

                            if(isJuiceBoxDarkColorCorrect(juiceBoxDark) &&
                                    checkRectanglePattern(juiceBoxDark, mapImage, x - 2, y + 6, 1, 15))
                            {
                                System.out.println("JUICE DETECTED. (MIRROR)");
                                determineSquareNumber(x, y, mapSolver, "JUICE");
                                return true;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("ERROR: JUICE NOT FOUND");
        return false;
    }

    private static boolean findSpaghetti(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color cheese = new Color(mapImage.getRGB(x, y));

                if(isCheeseColorCorrect(cheese))
                {
                    //  CHECK NORMAL FORM.
                    if(checkRectanglePattern(cheese, mapImage, x, y, 3, 0) &&
                            checkRectanglePattern(cheese, mapImage, x - 11, y - 7, 0, 1))
                    {
                        Color sauce = new Color(mapImage.getRGB(x + 2, y - 9));

                        if(isSauceColorCorrect(sauce) &&
                                checkRectanglePattern(sauce, mapImage, x + 2, y - 9, 4, 0))
                        {
                            Color fork = new Color(mapImage.getRGB(x + 14, y - 13));

                            if(isForkColorCorrect(fork) &&
                                    checkRectanglePattern(fork, mapImage, x + 13, y - 14, 2, 0) &&
                                    checkRectanglePattern(fork, mapImage, x + 13, y - 15, 2, 0))
                            {
                                System.out.println("SPAGHETTI DETECTED.");
                                determineSquareNumber(x, y, mapSolver, "SPAGHETTI");
                                return true;
                            }
                        }
                    }

                    //  CHECK FOR MIRROR FORM.
                    if(checkRectanglePattern(cheese, mapImage, x, y, 3, 0) &&
                            checkRectanglePattern(cheese, mapImage, x + 14, y - 7, 0, 1))
                    {
                        Color sauce = new Color(mapImage.getRGB(x - 3, y - 9));

                        if(isSauceColorCorrect(sauce) &&
                                checkRectanglePattern(sauce, mapImage, x - 3, y - 9, 4, 0))
                        {
                            Color fork = new Color(mapImage.getRGB(x - 14, y - 13));

                            if(isForkColorCorrect(fork) &&
                                    checkRectanglePattern(fork, mapImage, x - 14, y - 14, 2, 0) &&
                                    checkRectanglePattern(fork, mapImage, x - 14, y - 15, 2, 0))
                            {
                                System.out.println("SPAGHETTI DETECTED. (MIRROR)");
                                determineSquareNumber(x, y, mapSolver, "SPAGHETTI");
                                return true;
                            }

                        }
                    }
                }
            }
        }

        System.out.println("ERROR: SPAGHETTI NOT FOUND");
        return false;
    }

    private static boolean findFair(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
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

                System.out.println("FAIR DETECTED.");
                determineSquareNumber(x, y, mapSolver, "FAIR");
                return true;
            }
        }

        System.out.println("ERROR: FAIR NOT FOUND");
        return false;
    }

    private static boolean findSandwich(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
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

                System.out.println("SANDWICH DETECTED.");
                determineSquareNumber(x, y, mapSolver, "SANDWICH");
                return true;
            }
        }

        System.out.println("ERROR: SANDWICH NOT FOUND");
        return false;
    }

    private static boolean findBar(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color umbrellaShaft = new Color(mapImage.getRGB(x, y));

                if(isUmbrellaShaftColorCorrect(umbrellaShaft) &&
                        umbrellaShaft.equals(new Color(mapImage.getRGB(x, y + 1))))
                {
                    //  CHECK NORMAL FORM.
                    Color liquid = new Color(mapImage.getRGB(x - 2, y + 8));

                    if(isLiquidColorCorrect(liquid) &&
                            checkRectanglePattern(liquid, mapImage, x - 2, y + 8, 12, 0) &&
                            checkRectanglePattern(liquid, mapImage, x - 1, y + 9, 10, 0) &&
                            checkRectanglePattern(liquid, mapImage, x + 3, y + 10, 2, 0))
                    {
                        System.out.println("BAR DETECTED.");
                        determineSquareNumber(x, y, mapSolver, "BAR");
                        return true;
                    }

                    //  CHECK MIRROR FORM.
                    liquid = new Color(mapImage.getRGB(x + 2, y + 8));

                    if(isLiquidColorCorrect(liquid) &&
                            checkRectanglePattern(liquid, mapImage, x + 2, y + 8, 12, 0) &&
                            checkRectanglePattern(liquid, mapImage, x + 1, y + 9, 10, 0) &&
                            checkRectanglePattern(liquid, mapImage, x + 3, y + 10, 2, 0))
                    {
                        System.out.println("BAR DETECTED. (MIRROR)");
                        determineSquareNumber(x, y, mapSolver, "BAR");
                        return true;
                    }
                }
            }

            //  if(!umbrellaShaft.equals(new Color(mapImage.getRGB(x, y + 1))))
            //      continue;
        }

        System.out.println("ERROR: BAR NOT FOUND");
        return false;
    }

    private static boolean findTheater(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color eyeMouth = new Color(mapImage.getRGB(x, y));

                if(isTheaterColorCorrect(eyeMouth))
                {
                    //  CHECK NORMAL FORM.
                    if(eyeMouth.equals(new Color(mapImage.getRGB(x + 7, y))) &&
                            eyeMouth.equals(new Color(mapImage.getRGB(x + 1, y + 8))) &&
                            eyeMouth.equals(new Color(mapImage.getRGB(x + 2, y + 8))) &&
                            eyeMouth.equals(new Color(mapImage.getRGB(x + 2, y + 7))) &&
                            eyeMouth.equals(new Color(mapImage.getRGB(x + 3, y + 7))) &&
                            eyeMouth.equals(new Color(mapImage.getRGB(x + 4, y + 7))))
                    {
                        System.out.println("THEATER DETECTED.");
                        determineSquareNumber(x, y, mapSolver, "THEATER");
                        return true;
                    }

                    //  CHECK THEATER FORM.
                    if(eyeMouth.equals(new Color(mapImage.getRGB(x - 7, y))) &&
                            eyeMouth.equals(new Color(mapImage.getRGB(x - 1, y + 8))) &&
                            eyeMouth.equals(new Color(mapImage.getRGB(x - 2, y + 8))) &&
                            eyeMouth.equals(new Color(mapImage.getRGB(x - 2, y + 7))) &&
                            eyeMouth.equals(new Color(mapImage.getRGB(x - 3, y + 7))) &&
                            eyeMouth.equals(new Color(mapImage.getRGB(x - 4, y + 7))))
                    {
                        System.out.println("THEATER DETECTED. (MIRROR)");
                        determineSquareNumber(x, y, mapSolver, "THEATER");
                        return true;
                    }

                }
            }

        }

        System.out.println("ERROR: THEATER NOT FOUND");
        return false;
    }

    private static boolean findTaco(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color shellDark = new Color(mapImage.getRGB(x, y));
                if(isShellDarkCorrect(shellDark))
                {
                    //  CHECK NORMAL FORM.
                    if(shellDark.equals(new Color(mapImage.getRGB(x + 4, y - 3))) &&
                            shellDark.equals(new Color(mapImage.getRGB(x - 1, y + 5))) &&
                            shellDark.equals(new Color(mapImage.getRGB(x - 6, y + 5))) &&
                            shellDark.equals(new Color(mapImage.getRGB(x, y + 9))))
                    {
                        System.out.println("TACO DETECTED.");
                        determineSquareNumber(x, y, mapSolver, "TACO");
                        return true;
                    }

                    //  CHECK MIRROR FORM.
                    if(shellDark.equals(new Color(mapImage.getRGB(x - 4, y - 3))) &&
                            shellDark.equals(new Color(mapImage.getRGB(x + 1, y + 5))) &&
                            shellDark.equals(new Color(mapImage.getRGB(x + 6, y + 5))) &&
                            shellDark.equals(new Color(mapImage.getRGB(x, y + 9))))
                    {
                        System.out.println("TACO DETECTED. (MIRROR)");
                        determineSquareNumber(x, y, mapSolver, "TACO");
                        return true;
                    }
                }
            }

        }

        System.out.println("ERROR: TACO NOT FOUND");
        return false;
    }

    private static boolean findGarden(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
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

                System.out.println("GARDEN DETECTED.");
                determineSquareNumber(x, y, mapSolver, "GARDEN");
                return true;
            }
        }

        System.out.println("ERROR: GARDEN NOT FOUND");
        return false;
    }

    private static boolean findGas(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
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

                System.out.println("GAS PUMP DETECTED.");

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

    private static boolean findBallroom(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color outwardLeg = new Color(mapImage.getRGB(x, y));

                if(isOutwardLegColorCorrect(outwardLeg))
                {
                    //  CHECK NORMAL FORM.
                    Color standLeg = new Color(mapImage.getRGB(x + 12, y + 11));
                    Color forehead = new Color(mapImage.getRGB(x + 12, y - 10));

                    if(outwardLeg.equals(standLeg) &&
                            outwardLeg.equals(forehead))
                    {
                        System.out.println("BALLROOM DETECTED.");
                        determineSquareNumber(x, y, mapSolver, "BALLROOM");
                        return true;
                    }

                    //  CHECK MIRROR FORM.
                    standLeg = new Color(mapImage.getRGB(x - 12, y + 11));
                    forehead = new Color(mapImage.getRGB(x - 12, y - 10));

                    if(outwardLeg.equals(standLeg) && outwardLeg.equals(forehead))
                    {
                        System.out.println("BALLROOM DETECTED. (MIRROR)");
                        determineSquareNumber(x, y, mapSolver, "BALLROOM");
                        return true;
                    }
                }
            }
        }

        System.out.println("ERROR: BALLROOM NOT FOUND");
        return false;
    }

    private static boolean findCoffee(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color trail = new Color(mapImage.getRGB(x, y));

                if(isTrailColorCorrect(trail))
                {
                    //  CHECK NORMAL FORM.
                    if(trail.equals(new Color(mapImage.getRGB(x, y + 1))) &&
                            trail.equals(new Color(mapImage.getRGB(x, y + 2))) &&
                            trail.equals(new Color(mapImage.getRGB(x + 1, y + 3))) &&
                            trail.equals(new Color(mapImage.getRGB(x + 2, y + 5))) &&
                            trail.equals(new Color(mapImage.getRGB(x + 2, y + 6))))
                    {
                        System.out.println("COFFEE PATTERN DETECTED.");
                        determineSquareNumber(x, y, mapSolver, "COFFEE");
                        return true;
                    }

                    //  CHECK MIRROR FORM.
                    if(trail.equals(new Color(mapImage.getRGB(x, y + 1))) &&
                            trail.equals(new Color(mapImage.getRGB(x, y + 2))) &&
                            trail.equals(new Color(mapImage.getRGB(x - 1, y + 3))) &&
                            trail.equals(new Color(mapImage.getRGB(x - 2, y + 5))) &&
                            trail.equals(new Color(mapImage.getRGB(x - 2, y + 6))))
                    {
                        System.out.println("COFFEE PATTERN DETECTED. (MIRROR)");
                        determineSquareNumber(x, y, mapSolver, "COFFEE");
                        return true;
                    }
                }
            }
        }

        System.out.println("ERROR: COFFEE NOT FOUND");
        return false;
    }

    private static void findMall(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color brightHandle = new Color(mapImage.getRGB(x, y));
                Color bag = new Color(mapImage.getRGB(x, y + 5));

                if(isBrightHandleColorCorrect(brightHandle))
                {
                    //  CHECK NORMAL FORM.
                    if(checkRectanglePattern(brightHandle, mapImage, x, y, 0, 1) &&
                            checkRectanglePattern(brightHandle, mapImage, x + 1, y - 2, 0, 1) &&
                            checkRectanglePattern(brightHandle, mapImage, x + 3, y - 3, 1, 0) &&
                            checkRectanglePattern(brightHandle, mapImage, x + 11, y, 0, 2))
                    {
                        if(isBagColorCorrect(bag) &&
                                checkRectanglePattern(bag, mapImage, x, y + 5, 12, 16))
                        {
                            System.out.println("MALL DETECTED.");
                            determineSquareNumber(x, y, mapSolver, "MALL");
                            return;
                        }
                    }

                    //  CHECK MIRROR FORM.
                    if(checkRectanglePattern(brightHandle, mapImage, x, y, 0, 2) &&
                            checkRectanglePattern(brightHandle, mapImage, x + 11, y, 0, 1) &&
                            checkRectanglePattern(brightHandle, mapImage, x + 9, y - 2, 1, 0) &&
                            checkRectanglePattern(brightHandle, mapImage, x + 7, y - 3, 1, 0))
                    {
                        if(isBagColorCorrect(bag) &&
                                checkRectanglePattern(bag, mapImage, x, y + 5, 12, 1))
                        {
                            System.out.println("MALL DETECTED. (MIRROR)");
                            determineSquareNumber(x, y, mapSolver, "MALL");
                            return;
                        }
                    }
                }
            }

        }
    }

    private static boolean findHome(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color topWindow = new Color(mapImage.getRGB(x, y));
                if(isTopWindowColorCorrect(topWindow) &&
                        checkRectanglePattern(topWindow, mapImage, x, y, 1, 1))
                {
                    //  CHECK NORMAL FORM.
                    Color otherTopWindow = new Color(mapImage.getRGB(x + 10, y));
                    Color bottomWindow = new Color(mapImage.getRGB(x, y + 7));

                    if(checkRectanglePattern(otherTopWindow, mapImage, x + 10, y, 2, 1) &&
                            checkRectanglePattern(bottomWindow, mapImage, x, y + 7, 1, 2) &&
                            (topWindow.equals(otherTopWindow) && topWindow.equals(bottomWindow)))
                    {
                        System.out.println("HOME DETECTED.");
                        determineSquareNumber(x, y, mapSolver, "HOME");
                        return true;
                    }

                    //  CHECK MIRROR FORM.
                    otherTopWindow = new Color(mapImage.getRGB(x - 11, y));
                    bottomWindow = new Color(mapImage.getRGB(x, y + 7));

                    if(checkRectanglePattern(otherTopWindow, mapImage, x - 11, y, 2, 1) &&
                            checkRectanglePattern(bottomWindow, mapImage, x, y + 7, 1, 2) &&
                            (topWindow.equals(otherTopWindow) && topWindow.equals(bottomWindow)))
                    {
                        System.out.println("HOME PATTERN DETECTED. (MIRROR)");
                        determineSquareNumber(x, y, mapSolver, "HOME");
                        return true;
                    }

                }
            }
        }

        System.out.println("ERROR: HOME NOT FOUND");
        return false;
    }

    private static boolean findRing(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color darkShade = new Color(mapImage.getRGB(x, y));

                if(isDarkShadeColorCorrect(darkShade) &&
                        checkRectanglePattern(darkShade, mapImage, x, y, 5, 3))
                {
                    //  CHECK NORMAL FORM.
                    Color topBand = new Color(mapImage.getRGB(x - 5, y + 9));

                    if(checkRectanglePattern(darkShade, mapImage, x - 4, y + 5, 3, 2) &&
                        isTopBandColorCorrect(topBand) &&
                            checkRectanglePattern(topBand, mapImage, x - 5, y + 9, 9, 3))
                    {
                        System.out.println("RING DETECTED.");
                        determineSquareNumber(x, y, mapSolver, "RING");
                        return true;
                    }

                    //  CHECK MIRROR FORM.
                    topBand = new Color(mapImage.getRGB(x + 1, y + 9));

                    if(checkRectanglePattern(darkShade, mapImage, x + 6, y + 5, 3, 2) &&
                        isTopBandColorCorrect(topBand) &&
                            checkRectanglePattern(topBand, mapImage, x + 1, y + 9, 9, 3))
                    {
                        System.out.println("RING DETECTED. (MIRROR)");
                        determineSquareNumber(x, y, mapSolver, "RING");
                        return true;
                    }
                }
            }
        }

        return false;
    }

    //  NOTE: This method is most likely never going to actually see any use, but is still
    //      good to have just in case.
    private static boolean findAirplane(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        for(int x = 122; x < 735; x++)
        {
            for(int y = 150; y < 572; y++)
            {
                Color tailFin = new Color(mapImage.getRGB(x, y));

                if(isTailFinColorCorrect(tailFin) &&
                        checkRectanglePattern(tailFin, mapImage, x, y, 2, 1))
                {
                    //  CHECK NORMAL FORM.
                    if(checkRectanglePattern(tailFin, mapImage, x + 1, y + 2, 1, 0) &&
                            checkRectanglePattern(tailFin, mapImage, x + 6, y + 6, 0, 1) &&
                            checkRectanglePattern(tailFin, mapImage, x + 7, y + 6, 1, 2))
                    {
                        System.out.println("AIRPLANE DETECTED.");
                        determineSquareNumber(x, y, mapSolver, "AIRPLANE");
                        return true;
                    }

                    //  CHECK MIRROR FORM.
                    if(checkRectanglePattern(tailFin, mapImage, x, y + 2, 1, 0) &&
                            checkRectanglePattern(tailFin, mapImage, x - 6, y + 6, 2, 1) &&
                            checkRectanglePattern(tailFin, mapImage, x - 6, y + 8, 1, 0))

                    {
                        System.out.println("AIRPLANE DETECTED. (MIRROR)");
                        determineSquareNumber(x, y, mapSolver, "AIRPLANE");
                        return true;
                    }
                }
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
        NOTE: Pre-determined/Hard coded locations/color(s) on the map to determine whether a 'node'
            is accessible or not.
     */
    private static void findAccessibleNodes(BufferedImage mapImage, ChromeBotMapSolver mapSolver)
    {
        if(!new Color(mapImage.getRGB(306, 180)).equals(new Color(95, 96, 95)))
            mapSolver.getNode(6).setAccessible(false);
        if(!new Color(mapImage.getRGB(367, 180)).equals(new Color(96, 97, 96)))
            mapSolver.getNode(7).setAccessible(false);
        if(!new Color(mapImage.getRGB(428, 180)).equals(new Color(93, 95, 94)))
            mapSolver.getNode(8).setAccessible(false);
        if(!new Color(mapImage.getRGB(487, 180)).equals(new Color(96, 97, 96)))
            mapSolver.getNode(9).setAccessible(false);

        if(!new Color(mapImage.getRGB(290, 197)).equals(new Color(95, 97, 95)))
            mapSolver.getNode(11).setAccessible(false);
        if(!new Color(mapImage.getRGB(359, 197)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(12).setAccessible(false);
        if(!new Color(mapImage.getRGB(380, 197)).equals(new Color(94, 95, 94)))
            mapSolver.getNode(13).setAccessible(false);
        if(!new Color(mapImage.getRGB(440, 197)).equals(new Color(92, 95, 93)))
            mapSolver.getNode(14).setAccessible(false);
        if(!new Color(mapImage.getRGB(505, 197)).equals(new Color(95, 96, 94)))
            mapSolver.getNode(15).setAccessible(false);

        if(!new Color(mapImage.getRGB(304, 215)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(17).setAccessible(false);
        if(!new Color(mapImage.getRGB(364, 215)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(18).setAccessible(false);
        if(!new Color(mapImage.getRGB(429, 215)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(19).setAccessible(false);
        if(!new Color(mapImage.getRGB(496, 215)).equals(new Color(89, 91, 90)))
            mapSolver.getNode(20).setAccessible(false);

        if(!new Color(mapImage.getRGB(280, 231)).equals(new Color(93, 94, 92)))
            mapSolver.getNode(22).setAccessible(false);
        if(!new Color(mapImage.getRGB(306, 231)).equals(new Color(92, 95, 93)))
            mapSolver.getNode(23).setAccessible(false);
        if(!new Color(mapImage.getRGB(380, 231)).equals(new Color(93, 94, 92)))
            mapSolver.getNode(24).setAccessible(false);
        if(!new Color(mapImage.getRGB(450, 231)).equals(new Color(93, 95, 94)))
            mapSolver.getNode(25).setAccessible(false);
        if(!new Color(mapImage.getRGB(515, 231)).equals(new Color(94, 95, 94)))
            mapSolver.getNode(26).setAccessible(false);

        if(!new Color(mapImage.getRGB(295, 250)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(28).setAccessible(false);
        if(!new Color(mapImage.getRGB(366, 250)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(29).setAccessible(false);
        if(!new Color(mapImage.getRGB(440, 250)).equals(new Color(92, 93, 91)))
            mapSolver.getNode(30).setAccessible(false);
        if(!new Color(mapImage.getRGB(504, 250)).equals(new Color(95, 96, 95)))
            mapSolver.getNode(31).setAccessible(false);

        if(!new Color(mapImage.getRGB(250, 275)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(33).setAccessible(false);
        if(!new Color(mapImage.getRGB(330, 275)).equals(new Color(95, 97, 96)))
            mapSolver.getNode(34).setAccessible(false);
        if(!new Color(mapImage.getRGB(400, 275)).equals(new Color(93, 95, 94)))
            mapSolver.getNode(35).setAccessible(false);
        if(!new Color(mapImage.getRGB(454, 275)).equals(new Color(93, 95, 94)))
            mapSolver.getNode(36).setAccessible(false);
        if(!new Color(mapImage.getRGB(530, 275)).equals(new Color(93, 95, 94)))
            mapSolver.getNode(37).setAccessible(false);

        if(!new Color(mapImage.getRGB(278, 295)).equals(new Color(95, 96, 95)))
            mapSolver.getNode(39).setAccessible(false);
        if(!new Color(mapImage.getRGB(357, 295)).equals(new Color(95, 96, 95)))
            mapSolver.getNode(40).setAccessible(false);
        if(!new Color(mapImage.getRGB(435, 295)).equals(new Color(95, 96, 95)))
            mapSolver.getNode(41).setAccessible(false);
        if(!new Color(mapImage.getRGB(522, 295)).equals(new Color(94, 95, 94)))
            mapSolver.getNode(42).setAccessible(false);

        if(!new Color(mapImage.getRGB(232, 325)).equals(new Color(91, 92, 91)))
            mapSolver.getNode(44).setAccessible(false);
        if(!new Color(mapImage.getRGB(316, 325)).equals(new Color(91, 92, 91)))
            mapSolver.getNode(45).setAccessible(false);
        if(!new Color(mapImage.getRGB(400, 325)).equals(new Color(91, 92, 91)))
            mapSolver.getNode(46).setAccessible(false);
        if(!new Color(mapImage.getRGB(485, 325)).equals(new Color(93, 95, 93)))
            mapSolver.getNode(47).setAccessible(false);
        if(!new Color(mapImage.getRGB(565, 325)).equals(new Color(92, 93, 91)))
            mapSolver.getNode(48).setAccessible(false);

        if(!new Color(mapImage.getRGB(262, 355)).equals(new Color(91, 94, 92)))
            mapSolver.getNode(50).setAccessible(false);
        if(!new Color(mapImage.getRGB(352, 355)).equals(new Color(92, 95, 92)))
            mapSolver.getNode(51).setAccessible(false);
        if(!new Color(mapImage.getRGB(441, 355)).equals(new Color(93, 96, 93)))
            mapSolver.getNode(52).setAccessible(false);
        if(!new Color(mapImage.getRGB(536, 355)).equals(new Color(92, 95, 93)))
            mapSolver.getNode(53).setAccessible(false);

        if(!new Color(mapImage.getRGB(208, 395)).equals(new Color(92, 93, 92)))
            mapSolver.getNode(55).setAccessible(false);
        if(!new Color(mapImage.getRGB(306, 395)).equals(new Color(92, 93, 92)))
            mapSolver.getNode(56).setAccessible(false);
        if(!new Color(mapImage.getRGB(400, 395)).equals(new Color(91, 92, 91)))
            mapSolver.getNode(57).setAccessible(false);
        if(!new Color(mapImage.getRGB(495, 395)).equals(new Color(91, 92, 91)))
            mapSolver.getNode(58).setAccessible(false);
        if(!new Color(mapImage.getRGB(590, 395)).equals(new Color(92, 93, 91)))
            mapSolver.getNode(59).setAccessible(false);

        if(!new Color(mapImage.getRGB(244, 430)).equals(new Color(91, 93, 91)))
            mapSolver.getNode(61).setAccessible(false);
        if(!new Color(mapImage.getRGB(345, 430)).equals(new Color(91, 93, 91)))
            mapSolver.getNode(62).setAccessible(false);
        if(!new Color(mapImage.getRGB(447, 430)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(63).setAccessible(false);
        if(!new Color(mapImage.getRGB(548, 430)).equals(new Color(91, 94, 92)))
            mapSolver.getNode(64).setAccessible(false);

        if(!new Color(mapImage.getRGB(182, 471)).equals(new Color(92, 93, 91)))
            mapSolver.getNode(66).setAccessible(false);
        if(!new Color(mapImage.getRGB(291, 471)).equals(new Color(92, 93, 91)))
            mapSolver.getNode(67).setAccessible(false);
        if(!new Color(mapImage.getRGB(400, 471)).equals(new Color(91, 92, 91)))
            mapSolver.getNode(68).setAccessible(false);
        if(!new Color(mapImage.getRGB(510, 471)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(69).setAccessible(false);
        if(!new Color(mapImage.getRGB(620, 471)).equals(new Color(91, 93, 91)))
            mapSolver.getNode(70).setAccessible(false);

        if(!new Color(mapImage.getRGB(220, 522)).equals(new Color(91, 93, 91)))
            mapSolver.getNode(72).setAccessible(false);
        if(!new Color(mapImage.getRGB(337, 522)).equals(new Color(92, 94, 92)))
            mapSolver.getNode(73).setAccessible(false);
        if(!new Color(mapImage.getRGB(452, 522)).equals(new Color(91, 93, 91)))
            mapSolver.getNode(74).setAccessible(false);
        if(!new Color(mapImage.getRGB(570, 522)).equals(new Color(92, 95, 92)))
            mapSolver.getNode(75).setAccessible(false);

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

    //  JUICE Resource Color Checkers.
    //  HELPER: Check 'straw' color correctness.
    private static boolean isStrawColorCorrect(Color straw)
    {
        return ((straw.getRed() > 190 && straw.getRed() < 220) &&
                (straw.getGreen() > 205 && straw.getGreen() < 225) &&
                (straw.getBlue() > 205 && straw.getBlue() < 240));
    }

    //  HELPER: Check 'redArt' color correctness.
    private static boolean isRedArtColorCorrect(Color redArt)
    {
        return (redArt.getRed() > 215 &&
                (redArt.getGreen() > 70 && redArt.getGreen() < 110) &&
                (redArt.getBlue() > 75 && redArt.getBlue() < 140));
    }

    //  HELPER: Check 'juiceBoxDark' color correctness.
    private static boolean isJuiceBoxDarkColorCorrect(Color juiceBoxDark)
    {
        return (juiceBoxDark.getRed() > 185 &&
                (juiceBoxDark.getGreen() > 150 && juiceBoxDark.getGreen() < 195) &&
                juiceBoxDark.getBlue() < 50);
    }

    //  SPAGHETTI Resource Color Checkers.
    //  HELPER: Check 'cheese' color correctness.
    private static boolean isCheeseColorCorrect(Color cheese)
    {
        return (cheese.getRed() > 235 &&
                cheese.getGreen() > 210 &&
                (cheese.getBlue() > 170 && cheese.getBlue() < 210));
    }

    //  HELPER: Check 'sauce' color correctness.
    private static boolean isSauceColorCorrect(Color sauce)
    {
        return (sauce.getRed() > 225 &&
                (sauce.getGreen() > 70 && sauce.getGreen() < 100) &&
                (sauce.getBlue() > 80 && sauce.getBlue() < 150));
    }

    //  HELPER: Check 'fork' color correctness.
    private static boolean isForkColorCorrect(Color fork)
    {
        return ((fork.getRed() >= 130 && fork.getRed() <= 170) &&
                (fork.getGreen() >= 150 && fork.getGreen() <= 195) &&
                (fork.getBlue() >= 150 && fork.getBlue() <= 200));
    }

    //  BAR Resource Color Checkers.
    //  HELPER: Check 'umbrellaShaft' color correctness.
    private static boolean isUmbrellaShaftColorCorrect(Color umbrellaShaft)
    {
        return ((umbrellaShaft.getRed() > 175 && umbrellaShaft.getRed() < 220) &&
                (umbrellaShaft.getGreen() > 90 && umbrellaShaft.getGreen() < 120) &&
                (umbrellaShaft.getBlue() > 60 && umbrellaShaft.getBlue() < 110));
    }

    //  HELPER: Check 'liquid' color correctness.
    private static boolean isLiquidColorCorrect(Color liquid)
    {
        return (liquid.getRed() > 195 &&
                (liquid.getGreen() > 120 && liquid.getGreen() < 165) &&
                liquid.getBlue() < 45);
    }

    //  HELPER: Check 'eyeMouth' color correctness, when finding Theater resource.
    private static boolean isTheaterColorCorrect(Color eyeMouth)
    {
        return ((eyeMouth.getRed() > 110 && eyeMouth.getRed() < 170) &&
                (eyeMouth.getGreen() > 90 && eyeMouth.getGreen() < 120) &&
                (eyeMouth.getBlue() > 185 && eyeMouth.getBlue() < 220));
    }

    //  HELPER: Check 'shellDark' color correctness, when finding Taco resource.
    private static boolean isShellDarkCorrect(Color shellDark)
    {
        return (shellDark.getRed() > 200 &&
                (shellDark.getGreen() > 115 && shellDark.getGreen() < 180) &&
                shellDark.getBlue() < 50);
    }

    //  HELPER: Check 'outwardLeg' color correctness, when finding Ballroom resource.
    private static boolean isOutwardLegColorCorrect(Color outwardLeg)
    {
        return (outwardLeg.getRed() > 220 &&
                (outwardLeg.getGreen() > 205 && outwardLeg.getGreen() < 235) &&
                (outwardLeg.getBlue() > 80 && outwardLeg.getBlue() < 110));
    }

    //  HELPER: Check 'trail' color correctness, when finding Coffee resource.
    private static boolean isTrailColorCorrect(Color trail)
    {
        return ((trail.getRed() > 190 && trail.getRed() < 230) &&
                (trail.getGreen() > 145 && trail.getGreen() < 180) &&
                (trail.getBlue() > 110 && trail.getBlue() < 150));
    }

    //  MALL Resource Color Checkers.
    //  HELPER: Check 'brightHandle' color correctness.
    private static boolean isBrightHandleColorCorrect(Color brightHandle)
    {
        return (brightHandle.getRed() > 205 &&
                (brightHandle.getGreen() > 185 && brightHandle.getGreen() < 230) &&
                (brightHandle.getBlue() > 40 && brightHandle.getBlue() < 100));
    }

    //  HELPER: Check 'bag' color correctness.
    private static boolean isBagColorCorrect(Color bag)
    {
        return ((bag.getRed() > 80 && bag.getRed() < 115) &&
                (bag.getGreen() > 70 && bag.getGreen() < 105) &&
                (bag.getBlue() > 155 && bag.getBlue() < 190));
    }

    //  HELPER: Check 'topLeftWindow'/'topRightWindow' color correctness, when finding Home resource.
    private static boolean isTopWindowColorCorrect(Color topWindow)
    {
        return ((topWindow.getRed() > 60 && topWindow.getRed() < 115) &&
                (topWindow.getGreen() > 160 && topWindow.getGreen() < 190) &&
                topWindow.getBlue() > 210);
    }

    //  RING Resource Color Checkers.
    //  HELPER: Check 'darkShade' color correctness.
    private static boolean isDarkShadeColorCorrect(Color darkShade)
    {
        return ((darkShade.getRed() > 60 && darkShade.getRed() < 120) &&
                (darkShade.getGreen() > 150 && darkShade.getGreen() < 195) &&
                darkShade.getBlue() > 215);
    }

    //  HELPER: Check 'topBand' color correctness.
    private static boolean isTopBandColorCorrect(Color topBand)
    {
        return ((topBand.getRed() > 130 && topBand.getRed() < 180) &&
                (topBand.getGreen() > 140 && topBand.getGreen() < 200) &&
                (topBand.getBlue() > 150 && topBand.getBlue() < 210));
    }

    //  HELPER: Check 'tailFin' color correctness.
    private static boolean isTailFinColorCorrect(Color tailFin)
    {
        return ((tailFin.getRed() > 60 && tailFin.getRed() < 105) &&
                (tailFin.getGreen() > 155 && tailFin.getGreen() < 190) &&
                tailFin.getBlue() > 210);
    }

}
