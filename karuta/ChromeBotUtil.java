package karuta;

/*
    NOTE: This class is now used to hold helper classes as to not
        clutter up the other Karuta classes, and derail them
        from their main purpose(s).
 */

import javax.annotation.Nonnull;

public class ChromeBotUtil
{
    private static final String KARUTA_ID = "646937666251915264";

    //  NOTE: Helper class to translate path into emojis.
    public static String emojifyPath(@Nonnull String incPath)
    {
        //  ASSUMPTION: Input will be a path that exists, not null.
        String newPath = "";

        //  NOTE: Split path (by whitespace) into separate Strings, and convert them accordingly
        //      to respective emojis/emotes.
        for(String spot : incPath.split(" "))
        {
            switch(spot)
            {
                case "UP":
                    newPath += ":arrow_up: ";
                    break;
                case "RIGHT":
                    newPath += ":arrow_right: ";
                    break;
                case "DOWN":
                    newPath += ":arrow_down: ";
                    break;
                case "LEFT":
                    newPath += ":arrow_left: ";
                    break;
                case "BAR":
                    newPath += ":tropical_drink: ";
                    break;
                case "GARDEN":
                    newPath += ":blossom: ";
                    break;
                case "ITALIAN":
                    newPath += ":spaghetti: ";
                    break;
                case "TACO":
                    newPath += ":taco: ";
                    break;
                case "SANDWICH":
                    newPath += ":sandwich: ";
                    break;
                case "JUICE":
                    newPath += ":beverage_box: ";
                    break;
                case "FAIR":
                    newPath += ":ferris_wheel: ";
                    break;
                case "THEATER":
                    newPath += ":performing_arts: ";
                    break;
                case "MALL":
                    newPath += ":shopping_bags: ";
                    break;
                case "COFFEE":
                    newPath += ":coffee: ";
                    break;
                case "HOME":
                    newPath += ":house: ";
                    break;
                case "BALLROOM":
                    newPath += ":dancer: ";
                    break;
                case "RING":
                    newPath += ":ring: ";
                    break;
                case "GAS1":
                case "GAS2":
                case "GAS3":
                    newPath += ":fuelpump: ";
                    break;

                default:
                    //  newPath += ":grey_question: ";
                    break;
            }
        }

        return newPath;
    }

    //  NOTE: Helper method to check if an incoming event comes from an entity
    //      that is 'recent' enough.
    public static boolean isEntityRecent()
    {


        return false;
    }

    //  NOTE: Check if ID is Karuta Bot's
    public static boolean isIdFromKaruta(String id)     {   return id.equals(KARUTA_ID);     }
}
