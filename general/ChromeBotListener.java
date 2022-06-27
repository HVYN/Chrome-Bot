package general;

//  CHROME-BOT (Listener Class)
//  04/03/2022

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.util.ArrayList;
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
        //  if(!slashCommandInteractionEvent.isAcknowledged())
        slashCommandInteractionEvent.deferReply().queue();

        EmbedBuilder commandReplyEmbedBuilder;

        switch(slashCommandInteractionEvent.getName())
        {
            case "coinflip" ->
            {
                commandReplyEmbedBuilder = new EmbedBuilder()
                        .setColor(0xdcf7f2);

                switch(new Random().nextInt(2))
                {
                    case 0 -> commandReplyEmbedBuilder.setDescription(":sparkles:  **HEADS!**  :sparkles:");
                    case 1 -> commandReplyEmbedBuilder.setDescription(":boom:  **TAILS!**  :boom:");

                    default -> System.out.println("COINFLIP ERROR: How do you even end up here?");
                }

                slashCommandInteractionEvent.getHook()
                        .editOriginalEmbeds(commandReplyEmbedBuilder.build())
                        .queue();
            }
            case "help" ->
            {
                commandReplyEmbedBuilder = new EmbedBuilder()
                        .setColor(0xBABABA);

                commandReplyEmbedBuilder.setDescription("Select an option below for more information.");

                //  NOTE: Set the reply message to Ephemeral, as to not clutter up whichever channel
                //      it is replying in.
                slashCommandInteractionEvent.getHook().setEphemeral(true);

                slashCommandInteractionEvent.getHook()
                        .sendMessageEmbeds(commandReplyEmbedBuilder.build())
                        .addActionRow(
                                SelectMenu.create("helpMenu")
                                        .addOption("Karuta Date Mini-game Solver", "helpOptionDate", Emoji.fromMarkdown("\uD83E\uDDEE"))
                                        .build()
                        )
                        .queue();
            }

        }
    }

    //  OVERRIDE: Create a listener category to handle the help menu.
    @Override
    public void onSelectMenuInteraction(SelectMenuInteractionEvent selectMenuInteractionEvent)
    {
        if(!selectMenuInteractionEvent.isAcknowledged())
            selectMenuInteractionEvent.deferEdit().queue();

        if(selectMenuInteractionEvent.getComponentId().equals("helpMenu"))
        {
            //  NOTE: After user selects an option, remove the Select Menu component from the message.
            selectMenuInteractionEvent.getHook()
                    .editMessageComponentsById(selectMenuInteractionEvent.getMessageId(), new ArrayList<>())
                    .queue();

            if(selectMenuInteractionEvent.getSelectedOptions().get(0).getValue().equals("helpOptionDate"))
            {
                EmbedBuilder selectMenuReplyEmbedBuilder = new EmbedBuilder()
                        .setColor(0xb5ad82);

                selectMenuReplyEmbedBuilder.setTitle("Karuta Date Mini-game Solver")
                        .setDescription("""
                                This bot will automatically attempt to solve date mini-games posted by Karuta bot.

                                The default solution will be the path that nets the user the most amount of Affection Points.

                                If a ring is on the board, the bot will also find a path to the ring with the most Affection Points, and can be displayed by clicking the button with the :ring: emoji on it.

                                If there is **no** ring on the board, the bot will instead have an optional path to an Airport available.
                                The given Airport solution found is by comparing different Airport paths by their Time remaining. 
                                Additionally, the Airport solution will guarantee the user enough resources for at least **3** moves after using the Airport. (Excluding Time)
                                This Airport solution can be displayed by clicking the button with the :airplane: emoji on it.

                                Of course, the Ring and Airport solutions will only be available if it can be reached without failing.""");

                selectMenuInteractionEvent.getHook()
                        .editOriginalEmbeds(selectMenuReplyEmbedBuilder.build())
                        .queue();
            }
        }
    }

    //  OVERRIDE: Bend this method to my will by customizing what I want the bot
    //      to do when it hears a message.
    @Override
    public void onMessageReceived(MessageReceivedEvent messageReceivedEvent)
    {
        /*
        if(fromDeveloper(messageSenderId))
        {
            System.out.println(message.getGuild().getId());
        }

         */
    }


}
