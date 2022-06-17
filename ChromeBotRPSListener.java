
/*
    NOTE: This class has everything for the Rock-Paper-Scissors game.

        Moved from the default listener (ChromeBotListener), for organization
        purposes.
 */

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChromeBotRPSListener extends ListenerAdapter

{
    /*
        NOTE: Main 'listener' for RPS Game, through built-in slash command.
     */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent slashCommandInteractionEvent)
    {
        //  NOTE: Listen for rock-paper-scissors challenge slash command.
        if(slashCommandInteractionEvent.getName().equals("rockpaperscissors"))
        {
            slashCommandInteractionEvent.deferReply()
                    .queue();

            System.out.println("ROCK-PAPER-SCISSORS PARTIES:");
            System.out.println(slashCommandInteractionEvent.getUser().getId());
            System.out.println(slashCommandInteractionEvent.getOption("opponent").getAsUser().getId());

            //  NOTE: Put argument ('opponent') field into a variable for parsing later.
            //      THIS ARGUMENT IS REQUIRED, NOT OPTIONAL.
            OptionMapping opponent = slashCommandInteractionEvent.getOption("opponent");

            //  NOTE: Overall reply message builder.
            MessageBuilder replyBuilder = new MessageBuilder();

            //  NOTE: Create embed message, then add it to reply builder.
            MessageEmbed replyEmbedMessage = new EmbedBuilder()
                    .setColor(0xBABABA)
                    .setTitle("Rock Paper Scissors")
                    .setDescription(slashCommandInteractionEvent.getUser().getAsMention() + " vs. " + opponent.getAsUser().getAsMention())
                    .build();
            replyBuilder.setEmbeds(replyEmbedMessage);

            //  NOTE: Since we deferred the reply, we have to use the hook to get back, and
            //      answer the command.
            slashCommandInteractionEvent.getHook()
                    .editOriginalEmbeds(replyEmbedMessage)
                    .setActionRow(
                            Button.secondary("rock:" + slashCommandInteractionEvent.getUser().getId() + ":" + slashCommandInteractionEvent.getOption("opponent").getAsUser().getId() + ":n:n",
                                    Emoji.fromMarkdown("\uD83D\uDDFF")),
                            Button.secondary("paper:" + slashCommandInteractionEvent.getUser().getId() + ":" + slashCommandInteractionEvent.getOption("opponent").getAsUser().getId() + ":n:n",
                                    Emoji.fromMarkdown("\uD83D\uDCDC")),
                            Button.secondary("scissor:" + slashCommandInteractionEvent.getUser().getId() + ":" + slashCommandInteractionEvent.getOption("opponent").getAsUser().getId() + ":n:n",
                                    Emoji.fromMarkdown("✂️")),
                            Button.danger("cancelGame:" + slashCommandInteractionEvent.getUser().getId() + ":" + slashCommandInteractionEvent.getOption("opponent").getAsUser().getId(),
                                    "X"))
                    .queue(message -> {
                        //  AFTER  90 SECONDS (ARBITRARY), THE MESSAGE WILL TIME OUT/BUTTONS WILL BE REMOVED.
                        message.editMessageComponents(new ArrayList<>())
                                .queueAfter(90, TimeUnit.SECONDS);
                    });

        }
    }

    //  NOTE: Button Listener for RPS Game.
    @Override
    public void onButtonInteraction(ButtonInteractionEvent buttonInteractionEvent)
    {
        if(buttonInteractionEvent.getTimeCreated().isBefore(buttonInteractionEvent.getMessage().getTimeCreated().plusMinutes(3)))
            System.out.println("MESSAGE IS STILL RECENT (<3 Minutes).");
        else
        {
            System.out.println("MESSAGE IS OUTDATED (>3 Minutes).");
            return;
        }

        buttonInteractionEvent.deferEdit()
                .queue();

        //  NOTE: For rock-paper-scissors, every button will have the id format like this following
        //      example:
        //          cancelGame:209879011818471435:936067923091533835
        //  The prefix is what action it is, followed by two user IDs.

        //  NOTE: Make sure that the game can only be cancelled by one of the
        //      two parties.
        if(buttonInteractionEvent.getComponentId().startsWith("cancelGame:"))
        {
            Matcher userIdMatcher = Pattern.compile("cancelGame:(\\d{1,}):(\\d{1,})")
                    .matcher(buttonInteractionEvent.getComponentId());
            userIdMatcher.find();

            System.out.println("(CANCEL) ROCK-PAPER-SCISSORS");
            System.out.println(userIdMatcher.group(0));
            System.out.println(userIdMatcher.group(1));
            System.out.println(userIdMatcher.group(2) + "\n");

            if(buttonInteractionEvent.getUser().getId().equals(userIdMatcher.group(1)) ||
                    buttonInteractionEvent.getUser().getId().equals(userIdMatcher.group(2)))
            {
                System.out.println("CORRECT PERSON CLICKED CANCEL!\n");

                cancelRockPaperScissorsGame(buttonInteractionEvent);
            }
        }
        //  NOTE: Branch off if clicker chose an option instead of cancelling.
        else if(buttonInteractionEvent.getComponentId().startsWith("rock:") ||
                buttonInteractionEvent.getComponentId().startsWith("paper:") ||
                buttonInteractionEvent.getComponentId().startsWith("scissor:"))
        {
            Matcher userIdMatcher = Pattern.compile("\\w{1,}:(\\d{1,}):(\\d{1,}):([rpsn]):([rpsn])")
                    .matcher(buttonInteractionEvent.getComponentId());
            userIdMatcher.find();

            System.out.println("\nBUTTON COMPONENT ID:");
            System.out.println(buttonInteractionEvent.getComponentId() + "\n");

            String playerOneId = userIdMatcher.group(1);
            String playerTwoId = userIdMatcher.group(2);

            buttonInteractionEvent.getGuild().retrieveMemberById(playerOneId).queue(playerOne -> {
                buttonInteractionEvent.getGuild().retrieveMemberById(playerTwoId).queue(playerTwo -> {

                    String playerOneChoice = userIdMatcher.group(3);
                    String playerTwoChoice = userIdMatcher.group(4);

                    //  BUTTON PRESSER IS PLAYER ONE (CHALLENGER)
                    if(buttonInteractionEvent.getUser().getId().equals(playerOneId))
                    {
                        if(playerOneChoice.equals("n"))
                        {
                            if(buttonInteractionEvent.getComponentId().startsWith("rock"))
                                playerOneChoice = "r";
                            else if(buttonInteractionEvent.getComponentId().startsWith("paper"))
                                playerOneChoice = "p";
                            else if(buttonInteractionEvent.getComponentId().startsWith("scissor"))
                                playerOneChoice = "s";
                            else
                                System.out.println("PLAYER ONE CHOICE ERROR.");
                        }
                        else
                            System.out.println("PLAYER ONE ALREADY CHOSE SOMETHING!");

                    }
                    //  BUTTON PRESSER IS PLAYER TWO (CHALLENGED)
                    else if(buttonInteractionEvent.getUser().getId().equals(playerTwoId))
                    {
                        if(playerTwoChoice.equals("n"))
                        {
                            if(buttonInteractionEvent.getComponentId().startsWith("rock"))
                                playerTwoChoice = "r";
                            else if(buttonInteractionEvent.getComponentId().startsWith("paper"))
                                playerTwoChoice = "p";
                            else if(buttonInteractionEvent.getComponentId().startsWith("scissor"))
                                playerTwoChoice = "s";
                            else
                                System.out.println("PLAYER TWO CHOICE ERROR.");
                        }
                        else
                            System.out.println("PLAYER TWO ALREADY CHOSE SOMETHING!");
                    }

                    System.out.println(playerOneChoice + " " + playerTwoChoice);

                    String playerOneDescription = playerOne.getUser().getAsMention() + " chose ";
                    String playerTwoDescription = playerTwo.getUser().getAsMention() + " chose ";

                    switch(playerOneChoice)
                    {
                        case "r":
                            playerOneDescription += ":moyai:";
                            break;
                        case "p":
                            playerOneDescription += ":scroll:";
                            break;
                        case "s":
                            playerOneDescription += ":scissors:";
                            break;

                        default:
                            System.out.println("PLAYER ONE HAS NOT CHOSEN ANYTHING YET.");
                            break;
                    }

                    switch(playerTwoChoice)
                    {
                        case "r":
                            playerTwoDescription += ":moyai:";
                            break;
                        case "p":
                            playerTwoDescription += ":scroll:";
                            break;
                        case "s":
                            playerTwoDescription += ":scissors:";
                            break;

                        default:
                            System.out.println("PLAYER TWO HAS NOT CHOSEN ANYTHING YET.");
                            break;
                    }

                    //  NOTE: If button interaction comes from player one.
                    if(buttonInteractionEvent.getUser().getId().equals(playerOneId))
                    {
                        switch(comparePlayerChoices(playerOneChoice, playerTwoChoice))
                        {
                            case 0:
                                tieRockPaperScissors(buttonInteractionEvent, playerOneChoice);
                                break;
                            case 1:
                                //  PLAYER ONE WINS.
                                MessageEmbed playerOneWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(playerOneDescription + "\n" + playerTwoDescription + "\n\n" + playerOne.getUser().getAsTag() + " wins!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerOneWonMessageEmbed)
                                        .queue();
                                break;
                            case -1:
                                //  PLAYER ONE LOSES.
                                MessageEmbed playerOneLosesMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(playerOneDescription + "\n" + playerTwoDescription + "\n\n" + playerTwo.getUser().getAsTag() + " wins!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerOneLosesMessageEmbed)
                                        .queue();

                                break;
                            default:
                                //  PLAYER TWO HAS NOT CHOSEN ANYTHING YET.
                                buttonInteractionEvent.getHook().editOriginalComponents().setActionRow(
                                                Button.secondary("rock:" + playerOneId + ":" + playerTwoId + ":" + playerOneChoice + ":" + playerTwoChoice,
                                                        Emoji.fromMarkdown("\uD83D\uDDFF")),
                                                Button.secondary("paper:" + playerOneId + ":" + playerTwoId + ":" + playerOneChoice + ":" + playerTwoChoice,
                                                        Emoji.fromMarkdown("\uD83D\uDCDC")),
                                                Button.secondary("scissor:" + playerOneId + ":" + playerTwoId + ":" + playerOneChoice + ":" + playerTwoChoice,
                                                        Emoji.fromMarkdown("✂️")),
                                                Button.danger("cancelGame:" + playerOneId + ":" + playerTwoId,"X"))
                                        .queue();
                                break;
                        }
                    }
                    //  NOTE: If button interaction comes from player two.
                    else if(buttonInteractionEvent.getUser().getId().equals(playerTwoId))
                    {
                        switch(comparePlayerChoices(playerTwoChoice, playerOneChoice))
                        {
                            case 0:
                                tieRockPaperScissors(buttonInteractionEvent, playerOneChoice);
                                break;
                            case 1:
                                //  PLAYER TWO WINS.
                                MessageEmbed playerTwoWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(playerOneDescription + "\n" + playerTwoDescription + "\n\n" + playerTwo.getUser().getAsTag() + " wins!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerTwoWonMessageEmbed)
                                        .queue();
                                break;
                            case -1:
                                //  PLAYER TWO LOSES.
                                MessageEmbed playerTwoLosesMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(playerOneDescription + "\n" + playerTwoDescription + "\n\n" + playerOne.getUser().getAsTag() + " wins!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerTwoLosesMessageEmbed)
                                        .queue();
                                break;
                            default:
                                //  PLAYER ONE HAS NOT CHOSEN ANYTHING YET.
                                buttonInteractionEvent.getHook().editOriginalComponents().setActionRow(
                                                Button.secondary("rock:" + playerOneId + ":" + playerTwoId + ":" + playerOneChoice + ":" + playerTwoChoice,
                                                        Emoji.fromMarkdown("\uD83D\uDDFF")),
                                                Button.secondary("paper:" + playerOneId + ":" + playerTwoId + ":" + playerOneChoice + ":" + playerTwoChoice,
                                                        Emoji.fromMarkdown("\uD83D\uDCDC")),
                                                Button.secondary("scissor:" + playerOneId + ":" + playerTwoId + ":" + playerOneChoice + ":" + playerTwoChoice,
                                                        Emoji.fromMarkdown("✂️")),
                                                Button.danger("cancelGame:" + playerOneId + ":" + playerTwoId,"X"))
                                        .queue();
                                break;
                        }
                    }


                });
            });
        }
    }

    //  NOTE: If the button clicker is the first Player, grab their choice, then compare it to
    //      the other Player's choice.
    /*
        RETURN:
            1 - Clicker's choice wins the game.
            0 - Both choices has led to a draw.
           -1 - Clicker's choice loses the game.
           -2 - Other person has not chosen anything yet.
     */
    private int comparePlayerChoices(String clickerChoice, String otherChoice)
    {
        //  DRAW OPTION
        if(clickerChoice.equals(otherChoice))
            return 0;

        //  OTHER OPTIONS
        switch(clickerChoice)
        {
            case "r":
                switch(otherChoice)
                {
                    case "p":
                        return -1;
                    case "s":
                        return 1;
                    default:
                        break;
                }
                break;
            case "p":
                switch(otherChoice)
                {
                    case "r":
                        return 1;
                    case "s":
                        return -1;
                    default:
                        break;
                }
                break;
            case "s":
                switch(otherChoice)
                {
                    case "p":
                        return 1;
                    case "r":
                        return -1;
                    default:
                        break;
                }
                break;

            default:
                break;
        }

        //  NOTE: -2 Represents that the other player has not chosen anything yet (otherwise known as choice 'n').
        return -2;
    }

    //  NOTE: Function to help build message, retrieve member info, etc. when the RPS game
    //      results in a tie.
    private void tieRockPaperScissors(ButtonInteractionEvent buttonInteractionEvent, String choice)
    {
        EmbedBuilder tieMessageBuilder = new EmbedBuilder()
                .setColor(0xBABABA)
                .setTitle("Rock Paper Scissors");

        String description = "**TIE!** (Both players chose ";

        //  NOTE: Check what choice the players made, and return an appropriate message off of that.
        switch(choice)
        {
            case "r":
                description += " *ROCK*)";
                break;
            case "p":
                description += " *PAPER*)";
                break;
            case "s":
                description += " *SCISSOR*)";
                break;

            default:
                System.out.println("(ERROR) | Unknown 'choice' detected.");
                break;
        }

        //  NOTE: After determining what the players tied on, tack the description
        //      onto the embed message builder.
        tieMessageBuilder.setDescription(description);

        //  NOTE: Since the game has ended, remove the buttons, and edit message
        //      appropriately.
        buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                .queue();
        buttonInteractionEvent.getHook().editOriginalEmbeds(tieMessageBuilder.build())
                .queue();
    }

    //  NOTE: Helper function to help with rock-paper-scissors decision tree.
    private void cancelRockPaperScissorsGame(ButtonInteractionEvent buttonInteractionEvent)
    {
        MessageEmbed cancelEmbedMessage = new EmbedBuilder()
                .setColor(0xdb2114)
                .setTitle("Game Cancelled")
                .setDescription(buttonInteractionEvent.getUser().getAsMention() + " cancelled the game!")
                .build();

        //  Change embed message to let the user know that the game is
        //      cancelled.
        buttonInteractionEvent.getHook()
                .editOriginalEmbeds(cancelEmbedMessage)
                .queue();
        //  REMOVE all the buttons, by replacing with empty list.
        buttonInteractionEvent.getHook()
                .editOriginalComponents(new ArrayList<>())
                .queue();
    }
}
