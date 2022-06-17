package karuta;

/*
    NOTE: After the first listener class 'ChromeBotListener' started getting
        more cluttered when working on Karuta Bot functions, I moved all Karuta
        related things to another listener instead.
 */

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChromeBotKarutaListener extends ListenerAdapter
{
    private final String DEVELOPER_ID = "209879011818471435";

    private final String USER_REGEX = "<@(\\d{1,})>";
    private final String VALUE_REGEX = "(.{1,}):(.{1,})";
    private final String BUTTON_KEY_REGEX = "\\w{1,}:(\\d{1,})";

    private final Pattern USER_REGEX_PATTERN = Pattern.compile(USER_REGEX);
    private final Pattern VALUE_REGEX_PATTERN = Pattern.compile(VALUE_REGEX);
    private final Pattern BUTTON_KEY_REGEX_PATTERN = Pattern.compile(BUTTON_KEY_REGEX);

    private RedisCommands<String, String> redisSyncCommands;

    //  CONSTRUCTOR TAKES REDIS CONNECTION TO WORK WITH THE DATABASE.
    public ChromeBotKarutaListener(StatefulRedisConnection<String, String> redisConnection)
    {
        redisSyncCommands = redisConnection.sync();
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

            messageGuild.retrieveMemberById(userMatcher.group(1)).queue(member -> {
                message.reply(member.getUser().getAsTag()).queue();
            });
        }

         */

        //  NOTE: Check to see if the message received (in the channel)
        //      is from Karuta bot.
        if(ChromeBotUtil.isIdFromKaruta((messageSenderId)))
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
            /*
            //  CHECK IF WISHLIST IS DROPPING.
            else if (wishListIsDropping(messageReceivedEvent))
                messageChannel.sendMessage("**SOMEONE'S WISHLIST IS DROPPING!** :scream: "
                                + messageGuild.getRoleById(EVENT_ROLE_ID).getAsMention())
                        .queue();

             */

        }
    }

    //  OVERRIDE: Main Listener for bot to respond to messages being updated.
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
        if(ChromeBotUtil.isIdFromKaruta(messageSenderId))
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
                    else if(embedMessage.getTitle().equals("Visit Character"))
                    {
                        Matcher userMatcher = USER_REGEX_PATTERN.matcher(embedMessage.getDescription());
                            userMatcher.find();

                        for(String key : redisSyncCommands.scan().getKeys())
                        {
                            if(key.contains(userMatcher.group(1)))
                                redisSyncCommands.del(key);
                        }
                    }
                }
            }
        }
    }

    //  OVERRIDE: 'Listen' for button interactions (clicks).
    @Override
    public void onButtonInteraction(ButtonInteractionEvent buttonInteractionEvent)
    {
        System.out.println("\nBUTTON " + buttonInteractionEvent.getComponentId() + " WAS PRESSED!");

        if(!buttonInteractionEvent.getTimeCreated().isBefore(buttonInteractionEvent.getMessage().getTimeCreated().plusMinutes(3)))
        {
            System.out.println("MESSAGE IS OUTDATED (>3 Minutes).");
            return;
        }

        System.out.println("MESSAGE IS STILL RECENT (<3 Minutes).");

        Matcher buttonKeyMatcher = BUTTON_KEY_REGEX_PATTERN.matcher(buttonInteractionEvent.getComponentId());
            buttonKeyMatcher.find();

        if(!buttonInteractionEvent.isAcknowledged())
            buttonInteractionEvent.deferEdit().queue();

        //  NOTE: Check if user that is clicking the button is the right user (i.e. do not let random
        //      people interfere with the message).
        buttonInteractionEvent.getGuild()
                .retrieveMemberById(buttonKeyMatcher.group(1))
                .queue(member -> {

                    if(buttonInteractionEvent.getUser().getId().equals(buttonKeyMatcher.group(1)))
                    {
                        //  GOAL: When user clicks on the button, it should toggle between a solution
                        //      WITH the Ring included, and one without.

                        //  NOTE: Check if user clicks on 'Ring' button on the date solution
                        //      embed message.
                        if(buttonInteractionEvent.getComponentId().startsWith("ring"))
                        {
                            if(!redisSyncCommands.get(buttonInteractionEvent.getComponentId()).isBlank())
                            {
                                Matcher valueMatcher = VALUE_REGEX_PATTERN.matcher(redisSyncCommands.get(buttonInteractionEvent.getComponentId()));
                                valueMatcher.find();

                                String footerText = buttonInteractionEvent.getMessage()
                                        .getEmbeds().get(0)
                                        .getFooter().getText();

                                String ringAffectionPoints = valueMatcher.group(1);
                                String ringPath = valueMatcher.group(2);

                                EmbedBuilder ringEmbedMessage = new EmbedBuilder()
                                        .setTitle("Ring Solution")
                                        .setColor(new Color(0x7eaede))
                                        .setDescription(ChromeBotUtil.emojifyPath(ringPath) + "\n**AP: " + ringAffectionPoints + "**")
                                        .setFooter(footerText);

                                if(ringPath.contains("MALL"))
                                    ringEmbedMessage.appendDescription(" (" + valueMatcher.group(1) + " :shopping_bags:)");

                                buttonInteractionEvent.getHook()
                                        .editOriginalEmbeds(ringEmbedMessage.build())
                                        .queue();
                                buttonInteractionEvent.getHook()
                                        .editOriginalComponents()
                                        .setActionRow(
                                                Button.danger("default:" + buttonKeyMatcher.group(1), Emoji.fromMarkdown("\uD83D\uDC8D"))
                                        )
                                        .queue();
                            }
                            else
                                System.out.println("RING PATH IS MISSING FROM REDIS DB!");
                        }
                        else if(buttonInteractionEvent.getComponentId().startsWith("airplane"))
                        {
                            if(!redisSyncCommands.get(buttonInteractionEvent.getComponentId()).isBlank())
                            {
                                Matcher valueMatcher = VALUE_REGEX_PATTERN.matcher(redisSyncCommands.get(buttonInteractionEvent.getComponentId()));
                                valueMatcher.find();

                                String footerText = buttonInteractionEvent.getMessage()
                                        .getEmbeds().get(0)
                                        .getFooter().getText();

                                String airplanePath = valueMatcher.group(2);

                                EmbedBuilder airplaneEmbedMessage = new EmbedBuilder()
                                        .setTitle("Airplane Path")
                                        .setColor(new Color(0x5D5C5B))
                                        .setDescription(ChromeBotUtil.emojifyPath(airplanePath))
                                        .setFooter(footerText);

                                buttonInteractionEvent.getHook()
                                        .editOriginalEmbeds(airplaneEmbedMessage.build())
                                        .queue();
                                buttonInteractionEvent.getHook()
                                        .editOriginalComponents()
                                        .setActionRow(
                                                Button.danger("default:" + buttonKeyMatcher.group(1), Emoji.fromMarkdown("✈️"))
                                        )
                                        .queue();
                            }
                            else
                                System.out.println("AIRPLANE PATH IS MISSING FROM REDIS DB!");
                        }
                        else if(buttonInteractionEvent.getComponentId().startsWith("default"))
                        {
                            if(!redisSyncCommands.get(buttonInteractionEvent.getComponentId()).isBlank())
                            {
                                Matcher valueMatcher = VALUE_REGEX_PATTERN.matcher(redisSyncCommands.get(buttonInteractionEvent.getComponentId()));
                                valueMatcher.find();

                                String footerText = buttonInteractionEvent.getMessage()
                                        .getEmbeds().get(0)
                                        .getFooter().getText();

                                String defaultAffectionPoints = valueMatcher.group(1);
                                String defaultPath = valueMatcher.group(2);

                                EmbedBuilder defaultEmbedMessage = new EmbedBuilder()
                                        .setTitle("Date Solution")
                                        .setColor(new Color(0xBABABA))
                                        .setDescription(ChromeBotUtil.emojifyPath(defaultPath) + "\n**AP: " + defaultAffectionPoints + "**")
                                        .setFooter(footerText);

                                if(defaultPath.contains("MALL"))
                                    defaultEmbedMessage.appendDescription(" (" + valueMatcher.group(1) + " :shopping_bags:)");

                                buttonInteractionEvent.getHook()
                                        .editOriginalEmbeds(defaultEmbedMessage.build())
                                        .queue();

                                if(buttonInteractionEvent.getMessage().getButtons().get(0).getEmoji().equals(Emoji.fromMarkdown("\uD83D\uDC8D")))
                                {
                                    buttonInteractionEvent.getHook()
                                            .editOriginalComponents()
                                            .setActionRow(
                                                    Button.secondary("ring:" + buttonKeyMatcher.group(1), Emoji.fromMarkdown("\uD83D\uDC8D"))
                                            )
                                            .queue();
                                }
                                else if(buttonInteractionEvent.getMessage().getButtons().get(0).getEmoji().equals(Emoji.fromMarkdown("✈️")))
                                {
                                    buttonInteractionEvent.getHook()
                                            .editOriginalComponents()
                                            .setActionRow(
                                                    Button.secondary("airplane:" + buttonKeyMatcher.group(1), Emoji.fromMarkdown("✈️"))
                                            )
                                            .queue();

                                }
                            }
                            else
                                System.out.println("DEFAULT PATH IS MISSING FROM REDIS DB!");

                        }
                    }
                });

    }

    //  HELPER: Since this series of actions is called when a message is edited and/or sent,
    //      I moved it to a separate function to make readability better, and stop
    //      spam.
    private void processDateMiniGame(Message message, MessageEmbed embedMessage)
    {
        //  NOTE: Use Matcher class, with pre-defined 'USER_REGEX' Pattern object to
        //      find user ID; we then put the user's name/tag into the footer of the
        //      embed message we return (unless the parser fails, in which case we
        //      return an error message in the footer).
        Matcher userMatcher = USER_REGEX_PATTERN.matcher(embedMessage.getDescription());
            userMatcher.find();

        try
        {
            URL mapImageURL = new URL(embedMessage.getImage().getProxyUrl());

            BufferedImage mapImage = ImageIO.read(mapImageURL);

            ChromeBotMapSolver mapSolver = new ChromeBotMapSolver();

            if(!ChromeBotMapParser.hasDateBegun(mapImage))
            {
                if (ChromeBotMapParser.checkStartDirection(mapImage, mapSolver))
                {
                    //  NOTE: Store error message from parsing process.
                    String errorMessage = ChromeBotMapParser.parseMapImage(mapImage, mapSolver);

                    //  NOTE: To let the user know that SOMETHING's happening at all, send a temporary embed message that says
                    //      the bot is working on it.
                    MessageEmbed tempSolvingMessage = new EmbedBuilder().setDescription("Attempting to solve!")
                            .setColor(new Color(0x96b59e))
                            .build();

                    //  NOTE: Once a result/outcome is reached/identified, edit the temporary embed message to display the highest result/error message.
                    message.replyEmbeds(tempSolvingMessage).queue(tempMessage -> {

                        if (errorMessage.equals(""))
                        {
                            message.getGuild().retrieveMemberById(userMatcher.group(1)).queue(member ->
                            {
                                //  NOTE: Once the player direction is determined, and the critical resources
                                //      have been identified, proceed to solving the date.
                                mapSolver.solveDate();

                                /*
                                    FROM THIS POINT ONWARD, THE MAP-SOLVER SHOULD HAVE ITS ANSWER
                                        FOR THE HIGHEST RESULT(S).
                                 */

                                DateResult highestResult = mapSolver.getHighestResult();

                                //  NOTE: Collection of embed messages to put on the edited message
                                //      afterwards.
                                //      The first embed will be the result w/out a ring, the 2nd will
                                //      contain the result w/ a ring.
                                LinkedList<MessageEmbed> embedMessages = new LinkedList<>();

                                //  NOTE: Set Footer of the embed message, using the (local) variable 'member' (lambda).
                                EmbedBuilder dateSolveMessageBuilder = new EmbedBuilder().setTitle("Date Solution")
                                        .setDescription("Date could not be solved!\n**AP: 0**")
                                        .setFooter("For " + member.getUser().getAsTag())
                                        .setColor(new Color(0x5D5C5B));

                                //  DEBUG: Print highest result to console for debugging purposes.
                                mapSolver.displayHighestResult();

                                //  NOTE: If there isn't even a single result marked as 'SUCCESS', then the
                                //      date board is un-winnable.
                                if(highestResult != null)
                                {
                                    System.out.println("\nSET User ID TO highestResult Path.");
                                    redisSyncCommands.set("default:" + member.getId(),
                                            Math.ceil(highestResult.getAffectionPoints()) + ":" + highestResult.getPath());
                                    System.out.println("SET default:" + member.getId() + " TO " +
                                            Math.ceil(highestResult.getAffectionPoints()) + ":" + highestResult.getPath());

                                    //  NOTE: USE VALUE MATCHER TO PARSE AP AND PATH
                                    Matcher valueMatcher = VALUE_REGEX_PATTERN.matcher(redisSyncCommands.get("default:" + member.getId()));
                                        valueMatcher.find();

                                    String dateSolution = ChromeBotUtil.emojifyPath(valueMatcher.group(2)) +
                                            "\n**AP: " + valueMatcher.group(1) + "**";

                                    if (highestResult.getPath().contains("MALL"))
                                        dateSolution += " (" + valueMatcher.group(1) + " + :shopping_bags:)";

                                    dateSolveMessageBuilder.setDescription(dateSolution)
                                            .setColor(new Color(0xBABABA));
                                }

                                embedMessages.add(dateSolveMessageBuilder.build());

                                //  NOTE: If the map had a ring in it, check if a (successful) ring path was found, then
                                //      build the appropriate embed message.
                                if(mapSolver.hasRing())
                                {
                                    DateResult highestResultRing = mapSolver.getHighestResultWithRing();

                                    //  NOTE: Set Footer of the embed message, using the (local) variable 'member' (lambda).
                                    EmbedBuilder dateSolveRingMessageBuilder = new EmbedBuilder().setTitle("Date Solution (w/ Ring)")
                                            .setDescription("Date could not get :ring: and end safely!")
                                            .setFooter("For " + member.getUser().getAsTag())
                                            .setColor(new Color(0x5D5C5B));

                                    mapSolver.displayHighestResultWithRing();

                                    //  NOTE: If there is a result that ends with a ring, build other embed message as well.
                                    if(highestResultRing != null)
                                    {
                                        System.out.println("\nSET User ID TO highestResultRing Path.");
                                        redisSyncCommands.set("ring:" + member.getId(),
                                                Math.ceil(highestResultRing.getAffectionPoints()) + ":" + highestResultRing.getPath());
                                        System.out.println("SET ring:" + member.getId() +
                                                Math.ceil(highestResultRing.getAffectionPoints()) + ":" + highestResultRing.getPath());

                                        tempMessage.editMessageComponents().setActionRow(
                                                Button.secondary("ring:" + member.getId(), Emoji.fromMarkdown("\uD83D\uDC8D"))
                                        ).queue();

                                        //  NOTE: USE VALUE MATCHER TO PARSE AP AND PATH
                                        //  Matcher valueMatcher = VALUE_REGEX_PATTERN.matcher(redisSyncCommands.get("ring:" + member.getId()));
                                        //      valueMatcher.find();

                                        //  String dateSolutionRing = ChromeBotUtil.emojifyPath(valueMatcher.group(2)) +
                                        //          "\n**AP: " + valueMatcher.group(1) + "**";

                                        //  if (highestResultRing.getPath().contains("MALL"))
                                        //      dateSolutionRing += " (" + valueMatcher.group(1) + " + :shopping_bags:)";

                                        //  dateSolveRingMessageBuilder.setDescription(dateSolutionRing)
                                        //          .setColor(new Color(0x7eaede));
                                    }

                                    //  embedMessages.add(dateSolveRingMessageBuilder.build());
                                }
                                //  NOTE: If it can reach the airport successfully, return the embed message with the path.
                                else if(mapSolver.getHighestResultWithAirport() != null)
                                {
                                    DateResult highestResultWithAirport = mapSolver.getHighestResultWithAirport();

                                    //  NOTE: Set Footer of the embed message, using the (local) variable 'member' (lambda).
                                    //  EmbedBuilder dateSolveAirportMessageBuilder = new EmbedBuilder().setTitle("Date Solution (w/ Airport)")
                                    //          .setDescription("Date could not get :airplane: and end safely!")
                                    //          .setFooter("For " + member.getUser().getAsTag())
                                    //          .setColor(new Color(0x5D5C5B));

                                    System.out.println("\nSET User ID TO highestResultWithAirport Path.");
                                    redisSyncCommands.set("airplane:" + member.getId(),
                                            Math.ceil(highestResultWithAirport.getAffectionPoints()) + ":" + highestResultWithAirport.getPath());
                                    System.out.println("SET airplane:" + member.getId() + " TO " +
                                            Math.ceil(highestResultWithAirport.getAffectionPoints()) + ":" + highestResultWithAirport.getPath());

                                    tempMessage.editMessageComponents().setActionRow(
                                            Button.secondary("airplane:" + member.getId(), Emoji.fromMarkdown("✈️")
                                            )).queue();

                                    //  NOTE: USE VALUE MATCHER TO PARSE AP AND PATH
                                    //  Matcher valueMatcher = VALUE_REGEX_PATTERN.matcher(redisSyncCommands.get("airplane:" + member.getId()));
                                    //      valueMatcher.find();

                                    //  String dateSolutionAirport = ChromeBotUtil.emojifyPath(valueMatcher.group(2));

                                    //  if (highestResultWithAirport.getPath().contains("MALL"))
                                    //      dateSolutionAirport += " (" + valueMatcher.group(1) + " + :shopping_bags:)";

                                    //  dateSolveAirportMessageBuilder.setDescription(dateSolutionAirport)
                                    //          .setColor(new Color(0xd5dfed));

                                    //  embedMessages.add(dateSolveAirportMessageBuilder.build());
                                }

                                //  NOTE: Final step, edit the message to include the solution(s).
                                tempMessage.editMessageEmbeds(embedMessages).queue();
                            });
                        }
                        else
                        {
                            //  NOTE: If there is an error (since 'errorMessage' is not empty), then do not attempt to solve the date,
                            //      and let the user know something went wrong.
                            EmbedBuilder dateSolveMessageBuilder = new EmbedBuilder().setTitle("Date Solution").setColor(new Color(0x5D5C5B));

                            dateSolveMessageBuilder.setDescription("I'm sorry, something went wrong while trying to find the best path!")
                                .setFooter("ERROR: " + errorMessage);

                            //  NOTE: Reply to Karuta date-board
                            tempMessage.editMessageEmbeds(dateSolveMessageBuilder.build()).queue();
                        }
                    });

                }
            }
        }
        catch (IOException ioe)
        {
            System.out.println("ERROR: Image can't load for some reason.");
            ioe.printStackTrace();
        }
    }

    //  SCAN MESSAGE FOR WISHLIST DROP
    private boolean wishListIsDropping(MessageReceivedEvent messageReceivedEvent)
    {
        return (ChromeBotUtil.isIdFromKaruta(messageReceivedEvent.getAuthor().getId()) &&
                messageReceivedEvent.getMessage().getContentRaw().contains("A card from your wishlist is dropping!"));
    }

    //  CHECK IF ID IS MINE
    private boolean fromDeveloper(String id)    {   return id.equals(DEVELOPER_ID); }
}
