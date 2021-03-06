package general;

/*
    NOTE: This class has everything for the Rock-Paper-Scissors game.

        Moved from the default listener (general.ChromeBotListener), for organization
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
                                    Emoji.fromMarkdown("??????")),
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
        if(!ChromeBotUtil.isInteractionRecent(buttonInteractionEvent, buttonInteractionEvent.getMessage()))
            return;

        if(!buttonInteractionEvent.isAcknowledged())
            buttonInteractionEvent.deferEdit().queue();

        /*
            NOTE: For rock-paper-scissors, every button will have the id format like this following
                example:
                    cancelGame:209879011818471435:936067923091533835
            The prefix is what action it is, followed by two user IDs.
         */
        Matcher interactionIdMatcher = Pattern.compile("(\\w+):(\\d+):(\\d+)")
                .matcher(buttonInteractionEvent.getComponentId());

        //  NOTE: If button is not of the RPS format, just return.
        if(!interactionIdMatcher.find())
            return;

        //  NOTE: Put the important information from the button component ID into readable String(s).
        String buttonType = interactionIdMatcher.group(1);
        String playerOneId = interactionIdMatcher.group(2);
        String playerTwoId = interactionIdMatcher.group(3);

        //  NOTE: Switch/Case Jump table for different button types.
        switch(buttonType)
        {
            //  NOTE: Make sure that the game can only be cancelled by one of the
            //      two parties.
            case "cancelGame" ->
            {
                //  Matcher userIdMatcher = Pattern.compile("cancelGame:(\\d{1,}):(\\d{1,})")
                //          .matcher(buttonInteractionEvent.getComponentId());
                //  userIdMatcher.find();

                System.out.println("(CANCEL) ROCK-PAPER-SCISSORS");
                System.out.println(interactionIdMatcher.group(0));
                System.out.println(playerOneId);
                System.out.println(playerTwoId + "\n");

                if (buttonInteractionEvent.getUser().getId().equals(playerOneId) ||
                        buttonInteractionEvent.getUser().getId().equals(playerTwoId)) {
                    System.out.println("VALID PERSON REQUESTED TO CANCEL GAME!\n");

                    cancelRockPaperScissorsGame(buttonInteractionEvent);
                }
            }
            //  NOTE: All options for RPS can be parsed the same way.
            case "rock", "paper", "scissor" ->
            {
                Matcher rpsChoiceMatcher = Pattern.compile("\\w+:\\d+:\\d+:([rpsn]):([rpsn])")
                        .matcher(buttonInteractionEvent.getComponentId());
                rpsChoiceMatcher.find();

                System.out.println("\nBUTTON COMPONENT ID:");
                System.out.println(buttonInteractionEvent.getComponentId() + "\n");

                buttonInteractionEvent.getGuild().retrieveMemberById(playerOneId).queue(playerOne ->
                        buttonInteractionEvent.getGuild().retrieveMemberById(playerTwoId).queue(playerTwo -> {

                            String playerOneChoice = rpsChoiceMatcher.group(1);
                            String playerTwoChoice = rpsChoiceMatcher.group(2);

                            //  NOTE: Determine which player clicked the button.

                            //  BUTTON PRESSER IS PLAYER ONE (CHALLENGER)
                            if (buttonInteractionEvent.getUser().getId().equals(playerOneId))
                            {
                                if (playerOneChoice.equals("n"))
                                    playerOneChoice = buttonType.substring(0, 1);
                                else
                                    System.out.println("PLAYER ONE ALREADY CHOSE SOMETHING, OR CHOICE IS UNIDENTIFIABLE!");

                                System.out.println("PLAYER ONE CHOICE: " + playerOneChoice);
                                System.out.println("PLAYER TWO CHOICE: " + playerTwoChoice);

                                //  NOTE: In the event the match ends at this interaction, we must have the finale messages
                                //      ready to go.
                                String playerOneDescription = playerOne.getUser().getAsMention() + " chose " + parsePlayerChoice(playerOneChoice);
                                String playerTwoDescription = playerTwo.getUser().getAsMention() + " chose " + parsePlayerChoice(playerTwoChoice);

                                //  NOTE: After parsing player choices, at the end, we must decide the outcome
                                //      of the match (if both players have chosen something).
                                switch (comparePlayerChoices(playerOneChoice, playerTwoChoice))
                                {
                                    case 0 -> tieRockPaperScissors(buttonInteractionEvent, playerOneChoice);
                                    case 1 ->
                                    {
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
                                    }
                                    case -1 ->
                                    {
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
                                    }
                                    //  PLAYER TWO HAS NOT CHOSEN ANYTHING YET.
                                    default -> buttonInteractionEvent.getHook().editOriginalComponents().setActionRow(
                                                    Button.secondary("rock:" + playerOneId + ":" + playerTwoId + ":" + playerOneChoice + ":" + playerTwoChoice,
                                                            Emoji.fromMarkdown("\uD83D\uDDFF")),
                                                    Button.secondary("paper:" + playerOneId + ":" + playerTwoId + ":" + playerOneChoice + ":" + playerTwoChoice,
                                                            Emoji.fromMarkdown("\uD83D\uDCDC")),
                                                    Button.secondary("scissor:" + playerOneId + ":" + playerTwoId + ":" + playerOneChoice + ":" + playerTwoChoice,
                                                            Emoji.fromMarkdown("??????")),
                                                    Button.danger("cancelGame:" + playerOneId + ":" + playerTwoId, "X"))
                                            .queue();
                                }

                            }
                            //  BUTTON PRESSER IS PLAYER TWO (CHALLENGED)
                            else if (buttonInteractionEvent.getUser().getId().equals(playerTwoId))
                            {
                                if (playerTwoChoice.equals("n"))
                                    playerTwoChoice = buttonType.substring(0, 1);
                                else
                                    System.out.println("PLAYER TWO ALREADY CHOSE SOMETHING, OR CHOICE WAS UNIDENTIFIABLE!");

                                System.out.println("PLAYER ONE CHOICE: " + playerOneChoice);
                                System.out.println("PLAYER TWO CHOICE: " + playerTwoChoice);

                                //  NOTE: In the event the match ends at this interaction, we must have the finale messages
                                //      ready to go.
                                String playerOneDescription = playerOne.getUser().getAsMention() + " chose " + parsePlayerChoice(playerOneChoice);
                                String playerTwoDescription = playerTwo.getUser().getAsMention() + " chose " + parsePlayerChoice(playerTwoChoice);

                                //  NOTE: After parsing player choices, at the end, we must decide the outcome
                                //      of the match (if both players have chosen something).
                                switch (comparePlayerChoices(playerTwoChoice, playerOneChoice))
                                {
                                    case 0 -> tieRockPaperScissors(buttonInteractionEvent, playerOneChoice);
                                    case 1 ->
                                    {
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
                                    }
                                    case -1 ->
                                    {
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
                                    }
                                    //  PLAYER ONE HAS NOT CHOSEN ANYTHING YET.
                                    default -> buttonInteractionEvent.getHook().editOriginalComponents().setActionRow(
                                                    Button.secondary("rock:" + playerOneId + ":" + playerTwoId + ":" + playerOneChoice + ":" + playerTwoChoice,
                                                            Emoji.fromMarkdown("\uD83D\uDDFF")),
                                                    Button.secondary("paper:" + playerOneId + ":" + playerTwoId + ":" + playerOneChoice + ":" + playerTwoChoice,
                                                            Emoji.fromMarkdown("\uD83D\uDCDC")),
                                                    Button.secondary("scissor:" + playerOneId + ":" + playerTwoId + ":" + playerOneChoice + ":" + playerTwoChoice,
                                                            Emoji.fromMarkdown("??????")),
                                                    Button.danger("cancelGame:" + playerOneId + ":" + playerTwoId, "X"))
                                            .queue();
                                }
                            }

                        }));

            }

            default ->  System.out.println("UNKNOWN BUTTON INTERACTION WAS DETECTED!");
        }
    }

    //  HELPER: Parse player choice, and return equivalent word.
    private String parsePlayerChoice(String playerChoice)
    {
        switch(playerChoice)
        {
            case "r":
                return ":moyai:";
            case "p":
                return ":scroll:";
            case "s":
                return ":scissors:";

            default:
                System.out.println("Player has not made a choice, or choice was unidentifiable!");

                return "[UNKNOWN CHOICE]";
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
            case "r" -> description += " :moyai:)";
            case "p" -> description += " :scroll:)";
            case "s" -> description += " :scissors:)";

            default -> System.out.println("(ERROR) | Unknown 'choice' detected.");
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
