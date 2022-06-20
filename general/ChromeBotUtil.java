package general;

/*
    NOTE: This class is now used to hold helper classes as to not
        clutter up the other Karuta classes, and derail them
        from their main purpose(s).
 */

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import javax.annotation.Nonnull;

public class ChromeBotUtil
{
    private static final String KARUTA_ID = "646937666251915264";

    //  NOTE: Helper class to translate path into emojis.
    public static String emojifyPath(@Nonnull String incPath)
    {
        //  ASSUMPTION: Input will be a path that exists, not null.
        StringBuilder newPathBuilder = new StringBuilder();

        //  NOTE: Split path (by whitespace) into separate Strings, and convert them accordingly
        //      to respective emojis/emotes.
        for(String spot : incPath.split(" "))
        {
            newPathBuilder.append(
                    switch(spot)
                    {
                        case "UP":
                            yield ":arrow_up: ";
                        case "RIGHT":
                            yield ":arrow_right: ";
                        case "DOWN":
                            yield ":arrow_down: ";
                        case "LEFT":
                            yield ":arrow_left: ";
                        case "BAR":
                            yield ":tropical_drink: ";
                        case "GARDEN":
                            yield ":blossom: ";
                        case "ITALIAN":
                            yield ":spaghetti: ";
                        case "TACO":
                            yield ":taco: ";
                        case "SANDWICH":
                            yield ":sandwich: ";
                        case "JUICE":
                            yield ":beverage_box: ";
                        case "FAIR":
                            yield ":ferris_wheel: ";
                        case "THEATER":
                            yield ":performing_arts: ";
                        case "COFFEE":
                            yield ":coffee: ";
                        case "BALLROOM":
                            yield ":dancer: ";
                        case "GAS1", "GAS2", "GAS3":
                            yield ":fuelpump: ";

                        case "RING":
                            yield ":ring: ";
                        case "AIRPORT":
                            yield ":airplane: ";
                        case "MALL":
                            yield ":shopping_bags: ";
                        case "HOME":
                            yield ":house: ";

                        default:
                            System.out.println("ERROR: UNKNOWN PATH OPTION PARSED, CANNOT EMOJIFY!");
                            yield "";
                    });

        }

        return newPathBuilder.toString();
    }

    //  NOTE: Helper method to check if an incoming event comes from an entity
    //      that is 'recent' enough.
    //  CURRENT THRESHOLD: 3 MINUTES
    public static boolean isInteractionRecent(ButtonInteractionEvent buttonInteractionEvent, Message message)
    {
        if(!buttonInteractionEvent.getTimeCreated().isBefore(message.getTimeCreated().plusMinutes(3)))
        {
            System.out.println("MESSAGE IS OUTDATED! (>3 MINUTES OLD)");
            return false;
        }

        System.out.println("MESSAGE IS STILL RECENT! (<3 MINUTES OLD)");
        return true;
    }

    //  NOTE: Check if ID is Karuta Bot's
    public static boolean isIdFromKaruta(String id)     {   return id.equals(KARUTA_ID);     }
}
