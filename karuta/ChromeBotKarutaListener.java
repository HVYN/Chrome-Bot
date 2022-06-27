package karuta;

/*
    NOTE: After the first listener class 'general.ChromeBotListener' started getting
        more cluttered when working on Karuta Bot functions, I moved all Karuta
        related things to another listener instead.
 */

import general.ChromeBotUtil;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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

    private final String USER_REGEX = "<@(\\d+)>";
    private final String VALUE_REGEX = "(.+):(.+)";
    private final String AIRPORT_REGEX = "([\\s?\\w+]+):\\n(\\w+ . \\d+)\\n(\\w+ . \\d+)\\n(\\w+ . \\d+)\\n(\\w+ . \\d+)\\n(\\w+ . \\d+)";
    private final String BUTTON_KEY_REGEX = "(\\w+):(\\d+)";

    private final Pattern USER_REGEX_PATTERN = Pattern.compile(USER_REGEX);
    private final Pattern VALUE_REGEX_PATTERN = Pattern.compile(VALUE_REGEX);
    private final Pattern AIRPORT_REGEX_PATTERN = Pattern.compile(AIRPORT_REGEX);
    private final Pattern BUTTON_KEY_REGEX_PATTERN = Pattern.compile(BUTTON_KEY_REGEX);

    private final RedisCommands<String, String> redisSyncCommands;

    //  CONSTRUCTOR TAKES REDIS CONNECTION TO WORK WITH THE DATABASE.
    public ChromeBotKarutaListener(StatefulRedisConnection<String, String> redisConnection)
    {
        //  NOTE: This variable allows us to work with Redis commands thru the Lettuce library.
        redisSyncCommands = redisConnection.sync();
    }

    //  OVERRIDE: Bend this method to my will by customizing what I want the bot
    //      to do when it hears a message.
    @Override
    public void onMessageReceived(MessageReceivedEvent messageReceivedEvent)
    {
        User messageSender = messageReceivedEvent.getAuthor();
        Message message = messageReceivedEvent.getMessage();

        String rawMessageContent = message.getContentRaw();
        String messageSenderId = messageSender.getId();

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
                        //  NOTE: Once the date is finished, the bot will wipe any solution messages
                        //      related to the user from the DB.
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
        //  DEBUG: Log to console which button was pressed.
        System.out.println("\nBUTTON " + buttonInteractionEvent.getComponentId() + " WAS PRESSED BY " + buttonInteractionEvent.getUser().getId());

        //  NOTE: If button interaction is from a message that is relatively old, ignore it.
        if(!ChromeBotUtil.isInteractionRecent(buttonInteractionEvent, buttonInteractionEvent.getMessage()))
            return;

        if(!buttonInteractionEvent.isAcknowledged())
            buttonInteractionEvent.deferEdit().queue();

        Matcher buttonKeyMatcher = BUTTON_KEY_REGEX_PATTERN.matcher(buttonInteractionEvent.getComponentId());

        if(!buttonKeyMatcher.find())
            return;

        String buttonType = buttonKeyMatcher.group(1);
        String userId = buttonKeyMatcher.group(2);

        //  NOTE: Check if user that is clicking the button is the right user (i.e. do not let random
        //      people interfere with the message).
        buttonInteractionEvent.getGuild()
                .retrieveMemberById(userId)
                .queue(member ->
                {
                    //  NOTE: Check to make sure that whoever is clicking the button is the correct user.
                    if(buttonInteractionEvent.getUser().getId().equals(userId))
                    {
                        Matcher valueMatcher = VALUE_REGEX_PATTERN.matcher(redisSyncCommands.get(buttonInteractionEvent.getComponentId()));
                            valueMatcher.find();

                        String footerText = buttonInteractionEvent.getMessage()
                                .getEmbeds().get(0)
                                .getFooter().getText();

                        //  NOTE: Check if button even exists in the DB, which it should.
                        //      Still good to check just in case.
                        if(!redisSyncCommands.get(buttonInteractionEvent.getComponentId()).isBlank())
                        {
                            EmbedBuilder newEmbedMessage = new EmbedBuilder()
                                    .setFooter(footerText);

                            Button newButton = null;

                            //  GOAL: When user clicks on the button, it should toggle between a solution
                            //      with the Ring included, a path to the Airport, or the default one.
                            switch(buttonType)
                            {
                                case "default", "ring" ->
                                {
                                    //  NOTE: At first, parsing 'default' and 'ring' paths/AP is mostly the same.
                                    String affectionPoints = valueMatcher.group(1);
                                    String path = valueMatcher.group(2);

                                    newEmbedMessage.setDescription(ChromeBotUtil.emojifyPath(path) + "\n**AP: " + affectionPoints + "**");

                                    if (path.contains("MALL"))
                                        newEmbedMessage.appendDescription(" (" + affectionPoints + " + :shopping_bags:)");

                                    //  NOTE: Branch off depending on what type of solution the button is for.
                                    switch (buttonType)
                                    {
                                        case "default" ->
                                        {
                                            newEmbedMessage.setTitle("Date Solution")
                                                    .setColor(0xBABABA);

                                            if (buttonInteractionEvent.getMessage().getButtons().get(0).getEmoji().equals(Emoji.fromMarkdown("\uD83D\uDC8D")))
                                                newButton = Button.secondary("ring:" + userId, Emoji.fromMarkdown("\uD83D\uDC8D"));
                                            else if (buttonInteractionEvent.getMessage().getButtons().get(0).getEmoji().equals(Emoji.fromMarkdown("✈️")))
                                                newButton = Button.secondary("airplane:" + userId, Emoji.fromMarkdown("✈️"));
                                        }
                                        case "ring" ->
                                        {
                                            newEmbedMessage.setTitle("Ring Solution")
                                                    .setColor(0x7eaede);

                                            newButton = Button.danger("default:" + userId, Emoji.fromMarkdown("\uD83D\uDC8D"));
                                        }
                                    }
                                }
                                case "airplane" ->
                                {
                                    Matcher airplaneMatcher = AIRPORT_REGEX_PATTERN.matcher(redisSyncCommands.get(buttonInteractionEvent.getComponentId()));
                                    airplaneMatcher.find();

                                    String airplanePath = airplaneMatcher.group(1);
                                    String fuel = airplaneMatcher.group(2),
                                            hunger = airplaneMatcher.group(3),
                                            thirst = airplaneMatcher.group(4),
                                            happiness = airplaneMatcher.group(5),
                                            time = airplaneMatcher.group(6);

                                    newEmbedMessage.setTitle("Airplane Path")
                                            .setColor(0x5D5C5B)
                                            .setDescription(ChromeBotUtil.emojifyPath(airplanePath) +
                                                    "\n\n`" + fuel + "`\n`" + hunger + "`\n`" + thirst + "`\n`" + happiness + "`\n`" + time + "`");

                                    newButton = Button.danger("default:" + userId, Emoji.fromMarkdown("✈️"));
                                }

                            }

                            //  NOTE: After building the message, change the original embed for the user's request and
                            //      the button.
                            buttonInteractionEvent.getHook()
                                    .editOriginalEmbeds(newEmbedMessage.build())
                                    .queue();
                            buttonInteractionEvent.getHook()
                                    .editOriginalComponents()
                                    .setActionRow(newButton)
                                    .queue();
                        }
                        else
                            System.out.println("ERROR: Button leads to missing Key-Value.");
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

        long currentTime = System.currentTimeMillis();

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
                    message.replyEmbeds(tempSolvingMessage).queue(tempMessage ->
                    {
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
                                    uploadToRedis("default:" + member.getId(),
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

                                    mapSolver.displayHighestResultWithRing();

                                    //  NOTE: If there is a result that ends with a ring, build other embed message as well.
                                    if(highestResultRing != null)
                                    {
                                        uploadToRedis("ring:" + member.getId(),
                                                Math.ceil(highestResultRing.getAffectionPoints()) + ":" + highestResultRing.getPath());

                                        tempMessage.editMessageComponents().setActionRow(
                                                Button.secondary("ring:" + member.getId(), Emoji.fromMarkdown("\uD83D\uDC8D"))
                                        ).queue();

                                    }

                                }
                                //  NOTE: If it can reach the airport successfully, return the embed message with the path.
                                else if(mapSolver.getHighestResultWithAirport() != null)
                                {
                                    DateResult highestResultWithAirport = mapSolver.getHighestResultWithAirport();

                                    mapSolver.displayHighestResultWithAirport();

                                    uploadToRedis("airplane:" + member.getId(),
                                            highestResultWithAirport.getPath() + ":" + highestResultWithAirport.getResources());

                                    tempMessage.editMessageComponents().setActionRow(
                                            Button.secondary("airplane:" + member.getId(), Emoji.fromMarkdown("✈️")
                                            )).queue();
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
        catch (NullPointerException noe)
        {
            System.out.println("ERROR: NullPointerException (Most likely from fetching Discord Proxy URL).");
            noe.printStackTrace();
        }
        catch (IOException ioe)
        {
            System.out.println("ERROR: Image can't load for some reason.");
            ioe.printStackTrace();
        }

        //  DEBUG: Track how long processing takes.
        System.out.println("\nPROCESSING MINI-GAME TOOK: " + (System.currentTimeMillis() - currentTime) + "ms\n");
    }

    //  HELPER: Push data onto Redis DB.
    //      We can just use the same method for all of them (regarding Karuta stuff), because they all are more or less
    //      the same.
    private void uploadToRedis(String solutionId, String solutionApPath)
    {
        //  NOTE: This Key-Value pair will automatically be wiped in 10 minutes, to conserve space.
        redisSyncCommands.setex(solutionId, 600, solutionApPath);

        System.out.println("SET " + solutionId + " TO " + solutionApPath);
    }

    //  DEBUG: CHECK IF ID IS MINE
    private boolean fromDeveloper(String id)    {   return id.equals(DEVELOPER_ID); }
}
