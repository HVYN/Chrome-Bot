package general;

//  CHROME-BOT (Listener Class)
//  04/03/2022

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

//  KARUTA BOT'S ID
//  646937666251915264

public class ChromeBotListener extends ListenerAdapter
{
    private final String DEVELOPER_ID = "209879011818471435";

    //  private final String USER_REGEX = "<@(\\d{1,})>";
    //  private final String USER_REPLACE_REGEX = "/[<@!>]/g";

    //  private Pattern userRegex = Pattern.compile(USER_REGEX);

    //  NOTE: Moved previous coinflip command to slash interaction to prepare
    //      for August bot overhaul.
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent slashCommandInteractionEvent)
    {
        if(slashCommandInteractionEvent.getName().equals("coinflip"))
        {
            slashCommandInteractionEvent.deferReply()
                    .queue();

            MessageBuilder coinflipMessageBuilder = new MessageBuilder();

            switch(new Random().nextInt(2))
            {
                case 0:
                    coinflipMessageBuilder.append(":sparkles:  **HEADS!**  :sparkles:");
                    break;
                case 1:
                    coinflipMessageBuilder.append(":boom:  **TAILS!**  :boom:");
                    break;

                default:
                    System.out.println("COINFLIP ERROR: How do you even end up here?");
                    break;
            }

            slashCommandInteractionEvent.getHook()
                    .editOriginal(coinflipMessageBuilder.build())
                    .queue();
        }
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
            System.out.println(message.getGuild().getId());
        }

         */
    }


}
