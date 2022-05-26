
//  SUIKA BOT LISTENER
//  04/03/2022

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//  MAIN KARUTA CHANNEL TO LISTEN TO
//  949133688954826752

//  KARUTA BOT'S ID
//  646937666251915264

public class ChromeBotListener extends ListenerAdapter
{
    private final String KARUTA_ID = "646937666251915264";
    private final String DEVELOPER_ID = "209879011818471435";
    private final String EVENT_ROLE_ID = "959261568430911539";

    //  private final String EGG_REGEX = "(stEgg\\d{1,2}[ab])";
    //  private final String EGG_NAME_REGEX = "(Springtide Egg #\\d{1,})";
    //  private final String USER_REGEX = "(@\\d{1,})";
    //  private final String USER_REPLACE_REGEX = "/[<@!>]/g";

    //  private Pattern numberRegex = Pattern.compile("(\\d{1,2})");
    //  private Pattern eggRegex = Pattern.compile(EGG_REGEX);
    //  private Pattern eggNameRegex = Pattern.compile(EGG_NAME_REGEX);
    //  private Pattern userRegex = Pattern.compile(USER_REGEX);

    //  private LinkedList<Long> whitelistedMessageIds = new LinkedList<>();
    //  private LinkedList<String> userMentions = new LinkedList<>();

    //  private HashMap<String, LinkedList<Integer>> userEggsMissing = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent messageReceivedEvent)
    {
        Guild messageGuild = messageReceivedEvent.getGuild();
        MessageChannel messageChannel = messageReceivedEvent.getChannel();
        User messageSender = messageReceivedEvent.getAuthor();
        Message message = messageReceivedEvent.getMessage();

        String rawMessageContent = message.getContentRaw();
        String messageSenderId = messageSender.getId();

        //  PARSE KARUTA MESSAGES
        if(fromKaruta(messageSenderId))
        {
            //  ASSUMPTION: KARUTA MESSAGES WITH 0 LENGTH TYPICALLY ARE
            //  EMBED MESSAGES.
            if (rawMessageContent.length() == 0)
            {
                if(!message.getEmbeds().isEmpty())
                {
                    //  IF THERE ARE EMBED ELEMENTS, BEGIN PARSING THEM HERE.
                    MessageEmbed embedMessage = message.getEmbeds().get(0);

                    if(embedMessage.getTitle().equals("Date Minigame"))
                    {
                        try
                        {
                            URL mapImageURL = new URL(embedMessage.getImage().getProxyUrl());

                            BufferedImage mapImage = ImageIO.read(mapImageURL);

                            ChromeBotMapSolver mapSolver = new ChromeBotMapSolver();

                            ChromeBotMapParser.parseMapImage(mapImage, mapSolver);

                            mapSolver.solveDate();

                            mapSolver.displayHighestResult();
                            messageChannel.sendMessage(mapSolver.getHighestResult().getPath()).queue();
                        }
                        catch (IOException ioe)
                        {
                            ioe.printStackTrace();
                        }

                    }
                }

                /*
                if (!messageReceivedEvent.getMessage().getEmbeds().isEmpty())
                {
                    MessageEmbed embedMessage = messageReceivedEvent.getMessage().getEmbeds().get(0);

                    if (embedMessage.getTitle().equals("Hamako's Springtide Shack"))
                    {
                        //  MAGNIFYING GLASS EMOTE REACT
                        messageReceivedEvent.getMessage().addReaction("\uD83D\uDD0E")
                                .queue();

                        //  WHITE-LIST THIS MESSAGE.
                        whitelistedMessageIds.add(messageReceivedEvent.getMessageIdLong());
                    }
                }

                 */
            }
            //  CHECK IF WISHLIST IS DROPPING.
            else if (wishListIsDropping(messageReceivedEvent))
                messageChannel.sendMessage("**SOMEONE'S WISHLIST IS DROPPING!** :scream: "
                        + messageGuild.getRoleById(EVENT_ROLE_ID).getAsMention())
                        .queue();
            /*
            else if (rawMessageContent.contains("into your basket!"))
            {
                Matcher userMentionMatcher = userRegex.matcher(rawMessageContent);
                Matcher eggNumberMatcher = numberRegex.matcher(rawMessageContent.substring(rawMessageContent.length() - 40));

                userMentionMatcher.find();
                eggNumberMatcher.find();

                if (userEggsMissing.containsKey(userMentionMatcher.group(0).substring(1)))
                {
                    System.out.println("BEFORE REMOVAL: " + userEggsMissing.get(userMentionMatcher.group(0).substring(1)));
                    System.out.println("EGG NUMBER: " + eggNumberMatcher.group(0));

                    for (int index = 0; index < userEggsMissing.get(userMentionMatcher.group(0).substring(1)).size(); index++)
                    {
                        if (userEggsMissing.get(userMentionMatcher.group(0).substring(1)).get(index) ==
                                Integer.parseInt(eggNumberMatcher.group(0)))
                        {
                            userEggsMissing.get(userMentionMatcher.group(0).substring(1)).remove(index);

                            break;
                        }
                    }

                    System.out.println("AFTER REMOVAL: " + userEggsMissing.get(userMentionMatcher.group(0).substring(1)));
                }

            }

             */
        }
        else
        {
            if(rawMessageContent.length() >= 1)
            {
                //  if(messageSenderId.equals(DEVELOPER_ID))
                //      System.out.println(messageReceivedEvent.getMember().getId().replace(USER_REPLACE_REGEX, ""));

                if(rawMessageContent.substring(0, 1).equals("t") && !messageSender.isBot())
                {
                    if (rawMessageContent.length() > 8 &&
                            rawMessageContent.substring(1, 9).toUpperCase().equals("COINFLIP"))
                    {
                        switch (new Random().nextInt(2))
                        {
                            case 0:
                                messageChannel.sendMessage(":sparkles:  **HEADS!**  :sparkles:")
                                        .queue();
                                break;
                            case 1:
                                messageChannel.sendMessage(":boom:   **TAILS!**  :boom: ")
                                        .queue();
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }

    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent messageReactionAddEvent)
    {
        Guild reactionGuild = messageReactionAddEvent.getGuild();
        MessageChannel reactionChannel = messageReactionAddEvent.getChannel();
        MessageReaction.ReactionEmote reactionEmote = messageReactionAddEvent.getReactionEmote();

        /*
        //  CHECK IF REACTOR IS IN THE GAMBLING CHANNEL
        //      AND IF IT IS KARUTA BOT.
        //  THEN CHECK IF IT IS AN EGG.
        if(reactionIsEgg(messageReactionAddEvent))
        {
            userMentions.clear();

            Matcher eggNoMatcher = numberRegex.matcher(reactionEmote.getName());
                eggNoMatcher.find();

            String eggMentions = "";

            int eggReactionNumber = Integer.parseInt(eggNoMatcher.group(0));
            System.out.println("EGG REACTION NUMBER: " + eggReactionNumber);

            for(String userId : userEggsMissing.keySet())
            {
                for(Integer missingEgg : userEggsMissing.get(userId))
                {
                    if(missingEgg == eggReactionNumber)
                    {
                        System.out.println("MISSING EGG FROM A USER DETECTED. " + userId);

                        reactionGuild.retrieveMemberById(userId).queue(member ->
                        {
                            System.out.println("MENTION: " + member.getAsMention());

                            userMentions.add(member.getAsMention());
                        });

                        break;
                    }
                }
            }

            System.out.println("USER MENTIONS ARRAY: " + userMentions);
            for(String mention : userMentions)
                eggMentions += (mention + " ");

            if(eggMentions.equals(""))
            {
                eggMentions += reactionGuild.getRoleById(EVENT_ROLE_ID).getAsMention();

                reactionChannel.sendMessage(":melon:  __Don't forget to type__ `k!event` __and click on the__ :mag: !  :melon:")
                        .queue();
            }

            reactionChannel.sendMessage(eggMentions + ", :mag_right:  **I FOUND AN EGG!**  :mag:")
                    .queue();
        }
        else if(reactionIsGlass(messageReactionAddEvent) &&
                !messageReactionAddEvent.getUser().isBot())
        {
            reactionChannel.retrieveMessageById(messageReactionAddEvent.getMessageId())
                    .queue((message ->
                    {
                        if(!message.getEmbeds().isEmpty() && message.getAuthor().getId().equals(KARUTA_ID)
                            && whitelistedMessageIds.contains(message.getIdLong()))
                        {
                            if(!message.getEmbeds().get(0).getFields().isEmpty())
                            {
                                Matcher eggMatcher = eggRegex.matcher(message.getEmbeds().get(0)
                                        .getFields().get(0).getValue());

                                String missingEggs = "**Egg(s)**: ";

                                String reactorId = messageReactionAddEvent.getUserId().replace(USER_REPLACE_REGEX, "");

                                System.out.println(reactorId);

                                if(!userEggsMissing.containsKey(reactorId))
                                    userEggsMissing.put(reactorId, new LinkedList<>());

                                //  FRESH RESTART FOR MISSING EGGS.
                                userEggsMissing.get(reactorId).clear();

                                while(eggMatcher.find())
                                {
                                    String egg = eggMatcher.group(0);

                                    if(egg.contains("b"))
                                    {
                                        userEggsMissing.get(reactorId).add(Integer.parseInt(egg.substring(5, egg.length() - 1)));

                                        missingEggs += " [" + egg.substring(5, egg.length() - 1) + "]";
                                    }
                                }

                                message.reply(messageReactionAddEvent.getUser().getAsMention() + " *Looks like you're missing some eggs! Let me help with that!*  :sunglasses:").queue();

                                System.out.println(userEggsMissing.get(reactorId));

                                //  DON'T LISTEN TO THE MESSAGE ANYMORE.
                                whitelistedMessageIds.remove(message.getIdLong());
                            }
                        }
                    }));
        }

         */
    }

    //  SCAN MESSAGE FOR WISHLIST DROP
    private boolean wishListIsDropping(MessageReceivedEvent messageReceivedEvent)
    {
        return (fromKaruta(messageReceivedEvent.getAuthor().getId()) &&
                messageReceivedEvent.getMessage().getContentRaw().contains("A card from your wishlist is dropping!"));
    }

    //  CHECK IF ID IS KARUTA BOT'S
    private boolean fromKaruta(String id)   {   return id.equals(KARUTA_ID);    }

    /*
    //  SCAN REACTIONS FOR EGGS
    private boolean reactionIsEgg(MessageReactionAddEvent messageReactionAddEvent)
    {
        if(messageReactionAddEvent.getReactionEmote().getName().length() < 5)
            return false;

        return (fromKaruta(messageReactionAddEvent.getUser().getId()) &&
                messageReactionAddEvent.getReactionEmote().getName().substring(0, 5).equals("stEgg"));
    }
     */

    /*
    //  SCAN REACTIONS FOR MAGNIFYING GLASS
    private boolean reactionIsGlass(MessageReactionAddEvent messageReactionAddEvent)
        {   return messageReactionAddEvent.getReactionEmote().getName().equals("\uD83D\uDD0E");    }

     */

}
