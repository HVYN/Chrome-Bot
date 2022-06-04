package karuta;

/*
    NOTE: After the first listener class 'ChromeBotListener' started getting
        more cluttered when working on Karuta Bot functions, I moved all Karuta
        related things to another listener instead.
 */

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
    private final String KARUTA_ID = "646937666251915264";
    private final String DEVELOPER_ID = "209879011818471435";
    private final String EVENT_ROLE_ID = "959261568430911539";
    private final String USER_REGEX = "<@(\\d{1,})>";

    private final Pattern userRegex = Pattern.compile(USER_REGEX);

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

    //  OVERRIDE: 'Listen' for button interactions (clicks).
    @Override
    public void onButtonInteraction(ButtonInteractionEvent buttonInteractionEvent)
    {
        //  NOTE: Check if user clicks on 'Ring' button on the date solution
        //      embed message.
        if(buttonInteractionEvent.getComponentId().equals("ring"))
        {
            System.out.println("BUTTON (ring) WAS PRESSED!");
            //  GOAL: When user clicks on the button, it should toggle between a solution
            //      WITH the Ring included, and one without.

        }
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
        Matcher userMatcher = userRegex.matcher(embedMessage.getDescription());
        userMatcher.find();

        try
        {
            URL mapImageURL = new URL(embedMessage.getImage().getProxyUrl());

            BufferedImage mapImage = ImageIO.read(mapImageURL);

            ChromeBotMapSolver mapSolver = new ChromeBotMapSolver();

            //  EmbedBuilder dateSolveMessageBuilder = new EmbedBuilder().setTitle("Date Solution").setColor(new Color(0x5D5C5B));

            if(!ChromeBotMapParser.hasDateBegun(mapImage))
            {
                if (ChromeBotMapParser.checkStartDirection(mapImage, mapSolver))
                {
                    //  NOTE: Store error message from parsing process.
                    String errorMessage = ChromeBotMapParser.parseMapImage(mapImage, mapSolver);

                    //  NOTE: To let the user know that SOMETHING's happening at all, send a temporary embed message that says
                    //      the bot is working on it.
                    EmbedBuilder tempSolvingMessageBuilder = new EmbedBuilder().setDescription("Attempting to solve!").setColor(new Color(0x96b59e));

                    //  NOTE: Once a result/outcome is reached/identified, edit the temporary embed message to display the highest result/error message.
                    message.replyEmbeds(tempSolvingMessageBuilder.build()).queue(tempMessage -> {
                        if (errorMessage.equals(""))
                        {
                            message.getGuild().retrieveMemberById(userMatcher.group(1)).queue(member ->
                            {
                                EmbedBuilder dateSolveMessageBuilder = new EmbedBuilder().setTitle("Date Solution")
                                        .setColor(new Color(0x5D5C5B));
                                EmbedBuilder dateSolveRingMessageBuilder = new EmbedBuilder().setTitle("Date Solution (w/ Ring)")
                                        .setColor(new Color(0x5D5C5B));

                                //  NOTE: Once the player direction is determined, and the critical resources
                                //      have been identified, proceed to solving the date.
                                mapSolver.solveDate();

                                //  DEBUG: Print highest result(s) to console for debugging purposes.
                                mapSolver.displayHighestResult();
                                mapSolver.displayHighestResultWithRing();

                                //  NOTE: If there isn't even a single result marked as 'SUCCESS', then the
                                //      date board is un-winnable.
                                if(mapSolver.getHighestResult() != null)
                                {
                                    String dateSolution = mapSolver.getPathAsEmotes(mapSolver.getHighestResult().getPath()) +
                                            "\n**AP: " + (mapSolver.getHighestResult().getAffectionPoints() - 1) + "-" + (mapSolver.getHighestResult().getAffectionPoints() + 1)
                                            + "**";
                                    /*
                                    //  NOTE: Create description for embed message.
                                    dateSolveMessageBuilder.setDescription(mapSolver.getPathAsEmotes(mapSolver.getHighestResult().getPath()) +
                                            "\n**AP: " + (mapSolver.getHighestResult().getAffectionPoints() - 1) + "-" + (mapSolver.getHighestResult().getAffectionPoints() + 1)
                                            + "**");

                                     */

                                    if (mapSolver.getHighestResult().getPath().contains("MALL"))
                                        dateSolution += " (" + (mapSolver.getHighestResult().getAffectionPoints() - 1 - 30) + "-" + (mapSolver.getHighestResult().getAffectionPoints() + 1 - 30)
                                                + " + :shopping_bags:)";
                                        //  dateSolveMessageBuilder.setDescription("\n`" + (mapSolver.getHighestResult().getAffectionPoints() - 1 - 30) + "-" + (mapSolver.getHighestResult().getAffectionPoints() + 1)
                                        //          + " + :shopping_bags");

                                    //  NOTE: If there is a result that ends with a ring, build other embed message as well.
                                    if(mapSolver.getHighestResultWithRing() != null)
                                    {
                                        String dateSolutionRing = mapSolver.getPathAsEmotes(mapSolver.getHighestResultWithRing().getPath()) +
                                                "\n**AP: " + (mapSolver.getHighestResultWithRing().getAffectionPoints() - 1) + "-" + (mapSolver.getHighestResultWithRing().getAffectionPoints() + 1)
                                                + "**";

                                        if (mapSolver.getHighestResultWithRing().getPath().contains("MALL"))
                                            dateSolutionRing += " (" + (mapSolver.getHighestResultWithRing().getAffectionPoints() - 1 - 30) + "-" + (mapSolver.getHighestResultWithRing().getAffectionPoints() + 1 - 30)
                                                    + " + :shopping_bags:)";


                                        dateSolveRingMessageBuilder.setDescription(dateSolutionRing)
                                                .setColor(new Color(0x7eaede));
                                    }

                                    dateSolveMessageBuilder.setDescription(dateSolution)
                                        .setColor(new Color(0xBABABA));
                                }
                                else
                                    dateSolveMessageBuilder.setDescription("Date could not be solved!\n**AP: 0**");

                                //  NOTE: Set Footer of the embed message, using the (local) variable 'member' (lambda).
                                dateSolveMessageBuilder.setFooter("For " + member.getUser().getAsTag());
                                dateSolveRingMessageBuilder.setFooter("For " + member.getUser().getAsTag());

                                MessageEmbed highestResultEmbed = dateSolveMessageBuilder.build();

                                //  NOTE: In other words, there was no ring to get to begin with.
                                if(mapSolver.getHighestResultWithRing() == null)
                                    tempMessage.editMessageEmbeds(highestResultEmbed).queue();
                                else
                                {
                                    MessageEmbed highestResultRingEmbed = dateSolveRingMessageBuilder.build();

                                    LinkedList<MessageEmbed> embedMessages = new LinkedList<>();
                                        embedMessages.add(highestResultEmbed);
                                        embedMessages.add(highestResultRingEmbed);

                                    tempMessage.editMessageEmbeds(embedMessages)
                                                .queue();
                                }
                            });
                        }
                        else
                        {
                            //  NOTE: If there is an error (since 'errorMessage' is not empty), then do not attempt to solve the date,
                            //      and let the user know something went wrong.
                            EmbedBuilder dateSolveMessageBuilder = new EmbedBuilder().setTitle("Date Solution").setColor(new Color(0x5D5C5B));

                            dateSolveMessageBuilder.setDescription("I'm sorry, something went wrong while trying to find the best path!");
                            dateSolveMessageBuilder.setFooter("ERROR: " + errorMessage);

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
        return (fromKaruta(messageReceivedEvent.getAuthor().getId()) &&
                messageReceivedEvent.getMessage().getContentRaw().contains("A card from your wishlist is dropping!"));
    }

    //  CHECK IF ID IS KARUTA BOT'S
    private boolean fromKaruta(String id)   {   return id.equals(KARUTA_ID);    }

    //  CHECK IF ID IS MINE
    private boolean fromDeveloper(String id)    {   return id.equals(DEVELOPER_ID); }
}
