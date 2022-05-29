
//  CHROME-BOT (Listener Class)
//  04/03/2022

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
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
    private final String USER_REGEX = "<@(\\d{1,})>";
    //  private final String USER_REPLACE_REGEX = "/[<@!>]/g";

    //  private Pattern numberRegex = Pattern.compile("(\\d{1,2})");
    //  private Pattern eggRegex = Pattern.compile(EGG_REGEX);
    //  private Pattern eggNameRegex = Pattern.compile(EGG_NAME_REGEX);
    private Pattern userRegex = Pattern.compile(USER_REGEX);

    //  private LinkedList<Long> whitelistedMessageIds = new LinkedList<>();
    //  private LinkedList<String> userMentions = new LinkedList<>();

    //  private HashMap<String, LinkedList<Integer>> userEggsMissing = new HashMap<>();

    //  OVERRIDE: 'Listen' for button interactions (clicks).
    @Override
    public void onButtonInteraction(ButtonInteractionEvent buttonInteractionEvent)
    {

    }

    //  OVERRIDE: Bend this method to my will by customizing what I want the bot
    //      to do when it hears a message.
    @Override
    public void onMessageReceived(MessageReceivedEvent messageReceivedEvent)
    {
        Guild messageGuild = messageReceivedEvent.getGuild();
        MessageChannel messageChannel = messageReceivedEvent.getChannel();
        User messageSender = messageReceivedEvent.getAuthor();
        Message message = messageReceivedEvent.getMessage();

        String rawMessageContent = message.getContentRaw();
        String messageSenderId = messageSender.getId();

        /*
        if(fromDeveloper(messageSenderId))
        {
            Matcher userMatcher = userRegex.matcher(message.getContentRaw());
            userMatcher.find();

            messageGuild.retrieveMemberById(userMatcher.group(0)).queue(member -> {
                message.reply(member.getUser().getAsTag()).queue();
            });
        }

         */

        //  NOTE: Check to see if the message received (in the channel)
        //      is from Karuta bot.
        if(fromKaruta(messageSenderId))
        {
            //  ASSUMPTION: KARUTA MESSAGES WITH 0 LENGTH TYPICALLY ARE
            //      EMBED MESSAGES.
            if (rawMessageContent.length() == 0)
            {
                if(!message.getEmbeds().isEmpty())
                {
                    //  IF THERE ARE EMBED ELEMENTS, BEGIN PARSING THEM HERE.
                    MessageEmbed embedMessage = message.getEmbeds().get(0);

                    if(embedMessage.getTitle().equals("Date Minigame"))
                        processDateMiniGame(message, embedMessage);
                }

            }
            //  CHECK IF WISHLIST IS DROPPING.
            else if (wishListIsDropping(messageReceivedEvent))
                messageChannel.sendMessage("**SOMEONE'S WISHLIST IS DROPPING!** :scream: "
                        + messageGuild.getRoleById(EVENT_ROLE_ID).getAsMention())
                        .queue();

        }
        else
        {
            //  NOTE: Check messages from other users/authors.
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

    //  OVERRIDE: Main way for bot to respond to messages being updated.
    @Override
    public void onMessageUpdate(MessageUpdateEvent messageUpdateEvent)
    {
        //  Guild messageGuild = messageUpdateEvent.getGuild();
        //  MessageChannel messageChannel = messageUpdateEvent.getChannel();
        User messageSender = messageUpdateEvent.getAuthor();
        Message message = messageUpdateEvent.getMessage();

        String rawMessageContent = message.getContentRaw();
        String messageSenderId = messageSender.getId();

        //  NOTE: Check from Karuta; when users click the date option,
        //      Karuta Bot will edit its own message.
        if(fromKaruta(messageSenderId))
        {
            //  NOTE: Check for date mini-game embed from Karuta, which
            //      always has no 'raw' content, only embed content.
            if(rawMessageContent.length() == 0)
            {
                if(!message.getEmbeds().isEmpty())
                {
                    //  IF THERE ARE EMBED ELEMENTS, BEGIN PARSING THEM HERE.
                    MessageEmbed embedMessage = message.getEmbeds().get(0);

                    if(embedMessage.getTitle().equals("Date Minigame"))
                        processDateMiniGame(message, embedMessage);
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


    }

    //  SCAN MESSAGE FOR WISHLIST DROP
    private boolean wishListIsDropping(MessageReceivedEvent messageReceivedEvent)
    {
        return (fromKaruta(messageReceivedEvent.getAuthor().getId()) &&
                messageReceivedEvent.getMessage().getContentRaw().contains("A card from your wishlist is dropping!"));
    }

    //  CHECK IF ID IS KARUTA BOT'S
    private boolean fromKaruta(String id)   {   return id.equals(KARUTA_ID);    }

    //  CHECK IF ID IS MINE
    private boolean fromDeveloper(String id)    {   return id.equals(DEVELOPER_ID); }

    //  HELPER: Since this series of actions is called when a message is edited and/or sent,
    //      I moved it to a separate function to make readability better, and stop
    //      spam.
    private void processDateMiniGame(Message message, MessageEmbed embedMessage)
    {
        //  NOTE: Use Matcher class, with pre-defined 'USER_REGEX' Pattern object to
        //      find user ID; we then put the user's name/tag into the footer of the
        //      embed message we return (unless the parser fails, in which case we
        //      return an error message in the footer).
        Matcher userMatcher = userRegex.matcher(embedMessage.getDescription());
        userMatcher.find();

        try
        {
            URL mapImageURL = new URL(embedMessage.getImage().getProxyUrl());

            BufferedImage mapImage = ImageIO.read(mapImageURL);

            ChromeBotMapSolver mapSolver = new ChromeBotMapSolver();

            EmbedBuilder dateSolveMessageBuilder = new EmbedBuilder()
                    .setTitle("Date Solution")
                    .setColor(new Color(0x5D5C5B));

            if(!ChromeBotMapParser.hasDateBegun(mapImage))
            {
                if (ChromeBotMapParser.checkStartDirection(mapImage, mapSolver))
                {
                    //  NOTE: Store error message from parsing process.
                    String errorMessage = ChromeBotMapParser.parseMapImage(mapImage, mapSolver);

                    if (errorMessage.equals(""))
                    {
                        //  NOTE: Once the player direction is determined, and the critical resources
                        //      have been identified, proceed to solving the date.
                        mapSolver.solveDate();

                        //  DEBUG: Print highest result to console for debugging purposes.
                        mapSolver.displayHighestResult();

                        String description;

                        //  NOTE: If there isn't even a single result marked as 'SUCCESS', then the
                        //      date board is un-winnable.
                        if(mapSolver.getHighestResult() != null)
                        {
                            //  NOTE: Create description for embed message.
                            description = mapSolver.getPathAsEmotes(mapSolver.getHighestResult().getPath()) +
                                    "\n**AP: " + (mapSolver.getHighestResult().getAffectionPoints() - 1) + "-" + (mapSolver.getHighestResult().getAffectionPoints() + 1)
                                    + "**";

                            if (mapSolver.getHighestResult().getPath().contains("MALL"))
                                description += " `" + (mapSolver.getHighestResult().getAffectionPoints() - 1 - 30) + "-" + (mapSolver.getHighestResult().getAffectionPoints() + 1)
                                        + " + :shopping_bags";

                            dateSolveMessageBuilder.setColor(new Color(0xBABABA));
                        }
                        else
                            description = "Date could not be solved!\n**AP: 0**";

                        //  NOTE: Create footer for embed message.
                        String footer = "For " + message.getGuild().getMemberById(userMatcher.group(1)).getUser().getAsTag();

                        dateSolveMessageBuilder.setDescription(description);
                        dateSolveMessageBuilder.setFooter(footer);

                        //  NOTE: In other words, there was no ring to get to begin with.
                        if(mapSolver.getHighestResultWithRing() == null)
                        {
                            message.replyEmbeds(dateSolveMessageBuilder.build()).setActionRow(
                                    Button.secondary("ring", Emoji.fromMarkdown("\uD83D\uDC8D")).asDisabled()
                            ).queue();
                        }
                        else
                        {
                            message.replyEmbeds(dateSolveMessageBuilder.build()).setActionRow(
                                    Button.secondary("ring", Emoji.fromMarkdown("\uD83D\uDC8D")).asDisabled()
                            ).queue();
                        }

                        return;
                    }

                    dateSolveMessageBuilder.setDescription("I'm sorry, something went wrong while trying to find the best path!");
                    dateSolveMessageBuilder.setFooter("ERROR: " + errorMessage);

                    //  NOTE: Reply to Karuta date-board
                    message.replyEmbeds(dateSolveMessageBuilder.build()).queue();
                }
            }
        }
        catch (IOException ioe)
        {
            System.out.println("ERROR: Image can't load for some reason.");
            ioe.printStackTrace();
        }
    }

}
