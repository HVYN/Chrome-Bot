
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
import java.util.LinkedList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//  MAIN KARUTA CHANNEL TO LISTEN TO
//  949133688954826752

//  KARUTA BOT'S ID
//  646937666251915264

public class ChromeBotListener extends ListenerAdapter
{
    private final String DEVELOPER_ID = "209879011818471435";

    private final String USER_REGEX = "<@(\\d{1,})>";
    //  private final String USER_REPLACE_REGEX = "/[<@!>]/g";

    //  private Pattern userRegex = Pattern.compile(USER_REGEX);

    @Override
    public void onButtonInteraction(ButtonInteractionEvent buttonInteractionEvent)
    {
        /*
        if(buttonInteractionEvent.getComponentId().equals("testOne"))
        {
            buttonInteractionEvent.deferEdit().queue();

            System.out.println("TEST ONE BUTTON PRESSED.");

            buttonInteractionEvent.getMessage().getEmbeds().get(0).
            buttonInteractionEvent.getMessage().suppressEmbeds(false).queue();
            //  buttonInteractionEvent.editMessageEmbeds(buttonInteractionEvent.getMessage().getEmbeds().get(0)).queue();
        }
        else if(buttonInteractionEvent.getComponentId().equals("testTwo"))
        {
            buttonInteractionEvent.deferEdit().queue();

            System.out.println("TEST TWO BUTTON PRESSED.");

            buttonInteractionEvent.getMessage().suppressEmbeds(false).queue();
            //  buttonInteractionEvent.editMessageEmbeds(buttonInteractionEvent.getMessage().getEmbeds().get(1)).queue();
        }

         */

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
            message.reply("Hello!").queue(helloMessage -> {
                EmbedBuilder testEmbedOneBuilder = new EmbedBuilder();
                EmbedBuilder testEmbedTwoBuilder = new EmbedBuilder();

                testEmbedOneBuilder.setDescription("TEST ONE!");
                testEmbedTwoBuilder.setDescription("TEST TWO!");

                LinkedList<MessageEmbed> embedMessages = new LinkedList<>();
                    embedMessages.add(testEmbedOneBuilder.build());
                    embedMessages.add(testEmbedTwoBuilder.build());

                helloMessage.suppressEmbeds(true).queue();

                helloMessage.editMessageEmbeds(embedMessages)
                        .setActionRow(Button.primary("testOne", "TEST ONE"), Button.primary("testTwo", "TEST TWO"))
                        .queue();

            });
        }

         */

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

    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent messageReactionAddEvent)
    {
        Guild reactionGuild = messageReactionAddEvent.getGuild();
        MessageChannel reactionChannel = messageReactionAddEvent.getChannel();
        MessageReaction.ReactionEmote reactionEmote = messageReactionAddEvent.getReactionEmote();


    }

    //  CHECK IF ID IS MINE
    private boolean fromDeveloper(String id)    {   return id.equals(DEVELOPER_ID); }

}
