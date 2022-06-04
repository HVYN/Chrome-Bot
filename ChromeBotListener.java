
//  CHROME-BOT (Listener Class)
//  04/03/2022

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//  MAIN KARUTA CHANNEL TO LISTEN TO
//  949133688954826752

//  KARUTA BOT'S ID
//  646937666251915264

public class ChromeBotListener extends ListenerAdapter
{
    private final String DEVELOPER_ID = "209879011818471435";

    //  private final String USER_REGEX = "<@(\\d{1,})>";
    //  private final String USER_REPLACE_REGEX = "/[<@!>]/g";

    //  private Pattern userRegex = Pattern.compile(USER_REGEX);

    //  **TESTING**
    //  Looking to create a Rock-Paper-Scissors command, but instead of doing it
    //      via text-parsing, use new built-in slash commands, to save headache and
    //      to get used to it, since bots have to do this by August (rate-limit protocol).
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
                    .setDescription(slashCommandInteractionEvent.getUser().getAsTag() + " vs. " + opponent.getAsUser().getAsTag())
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

    @Override
    public void onButtonInteraction(ButtonInteractionEvent buttonInteractionEvent)
    {
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
        else if(buttonInteractionEvent.getComponentId().startsWith("rock:") ||
            buttonInteractionEvent.getComponentId().startsWith("paper:") ||
            buttonInteractionEvent.getComponentId().startsWith("scissor:"))
        {
            Matcher userIdMatcher = Pattern.compile("\\w{1,}:(\\d{1,}):(\\d{1,}):([rpsn]):([rpsn])")
                    .matcher(buttonInteractionEvent.getComponentId());
            userIdMatcher.find();

            System.out.println("BUTTON COMPONENT ID:");
            System.out.println(buttonInteractionEvent.getComponentId() + "\n");

            String playerOneId = userIdMatcher.group(1);
            String playerTwoId = userIdMatcher.group(2);

            //  Member playerOne = buttonInteractionEvent.getGuild().retrieveMembersByIds(playerOneId, playerTwoId).get().get(0);
            //  Member playerTwo = buttonInteractionEvent.getGuild().retrieveMembersByIds(playerOneId, playerTwoId).get().get(1);

            buttonInteractionEvent.getGuild().retrieveMemberById(playerOneId).queue(playerOne -> {
                buttonInteractionEvent.getGuild().retrieveMemberById(playerTwoId).queue(playerTwo -> {

                    String playerOneChoice = userIdMatcher.group(3);
                    String playerTwoChoice = userIdMatcher.group(4);

                    if(buttonInteractionEvent.getUser().getId().equals(playerOneId))
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
                    else if(buttonInteractionEvent.getUser().getId().equals(playerTwoId))
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

                    System.out.println(playerOneChoice + " " + playerTwoChoice);

                    String playerOneDescription = playerOne.getUser().getAsTag() + " chose ";
                    String playerTwoDescription = playerTwo.getUser().getAsTag() + " chose ";

                    switch(playerOneChoice)
                    {
                        case "r":
                            playerOneDescription += "*ROCK*";
                            break;
                        case "p":
                            playerOneDescription += "*PAPER*";
                            break;
                        case "s":
                            playerOneDescription += "*SCISSOR*";
                            break;

                        default:
                            System.out.println("PLAYER ONE HAS NOT CHOSEN ANYTHING YET.");
                            break;
                    }

                    switch(playerTwoChoice)
                    {
                        case "r":
                            playerTwoDescription += "*ROCK*";
                            break;
                        case "p":
                            playerTwoDescription += "*PAPER*";
                            break;
                        case "s":
                            playerTwoDescription += "*SCISSOR*";
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
                                        .setDescription(playerOneDescription + "\n" + playerTwoDescription + "\n" + playerOne.getUser().getAsTag() + " wins!")
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
                                        .setDescription(playerOneDescription + "\n" + playerTwoDescription + "\n" + playerTwo.getUser().getAsTag() + " wins!")
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
                                        .setDescription(playerOneDescription + "\n" + playerTwoDescription + "\n" + playerTwo.getUser().getAsTag() + " wins!")
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
                                        .setDescription(playerOneDescription + "\n" + playerTwoDescription + "\n" + playerOne.getUser().getAsTag() + " wins!")
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
/*
        //  PERSON CLICKED ROCK
        else if(buttonInteractionEvent.getComponentId().startsWith("rock:"))
        {
            Matcher userIdMatcher = Pattern.compile("rock:(\\d{1,}):(\\d{1,}):([rpsn]):([rpsn])")
                    .matcher(buttonInteractionEvent.getComponentId());
            userIdMatcher.find();

            System.out.println("(ROCK OPTION) ROCK-PAPER-SCISSORS");
            System.out.println(userIdMatcher.group(0));
            System.out.println(userIdMatcher.group(1));
            System.out.println(userIdMatcher.group(2) + "\n");

            //  PLAYER ONE
            if(buttonInteractionEvent.getUser().getId().equals(userIdMatcher.group(1)))
            {
                buttonInteractionEvent.deferEdit().queue();

                switch(userIdMatcher.group(3))
                {
                    case "n":
                        //  PLAYER ONE CHOSE 'ROCK'
                        //  CHECK PLAYER TWO'S CHOICE BEFORE CONTINUING
                        switch(userIdMatcher.group(4))
                        {
                            case "r":
                                //  RESULT IN TIE
                                tieRockPaperScissors(buttonInteractionEvent, "r");
                                break;
                            case "p":
                                //  RESULT IN PLAYER TWO WINNING
                                MessageEmbed playerTwoWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(userIdMatcher.group(2) + " has won!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerTwoWonMessageEmbed)
                                        .queue();
                                break;
                            case "s":
                                //  RESULT IN PLAYER ONE WINNING
                                MessageEmbed playerOneWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(userIdMatcher.group(1) + " has won!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerOneWonMessageEmbed)
                                        .queue();
                                break;

                            default:
                                //  PLAYER TWO HASN'T CHOSEN ANYTHING YET.
                                buttonInteractionEvent.getHook().editOriginalComponents().setActionRow(
                                        Button.secondary("rock:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":r:" + userIdMatcher.group(4), Emoji.fromMarkdown("\uD83D\uDDFF")),
                                        Button.secondary("paper:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":r:" + userIdMatcher.group(4), Emoji.fromMarkdown("\uD83D\uDCDC")),
                                        Button.secondary("scissor:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":r:" + userIdMatcher.group(4), Emoji.fromMarkdown("✂️")),
                                        Button.danger("cancelGame:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2),"X"))
                                        .queue();

                                break;
                        }

                        break;
                    default:
                        System.out.println("PLAYER ONE ALREADY CHOSE!");
                        break;
                }

            }
            //  PLAYER TWO
            else if(buttonInteractionEvent.getUser().getId().equals(userIdMatcher.group(2)))
            {
                buttonInteractionEvent.deferEdit().queue();

                switch(userIdMatcher.group(4))
                {
                    case "n":
                        //  PLAYER TWO CHOSE 'ROCK'
                        //  CHECK PLAYER ONE'S CHOICE BEFORE CONTINUING
                        switch(userIdMatcher.group(3))
                        {
                            case "r":
                                //  RESULT IN TIE
                                tieRockPaperScissors(buttonInteractionEvent, "r");
                                break;
                            case "p":
                                //  RESULT IN PLAYER ONE WINNING
                                MessageEmbed playerTwoWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(userIdMatcher.group(1) + " has won!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerTwoWonMessageEmbed)
                                        .queue();
                                break;
                            case "s":
                                //  RESULT IN PLAYER TWO WINNING
                                MessageEmbed playerOneWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(userIdMatcher.group(2) + " has won!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerOneWonMessageEmbed)
                                        .queue();
                                break;

                            default:
                                //  PLAYER ONE HASN'T CHOSEN ANYTHING YET.
                                buttonInteractionEvent.getHook().editOriginalComponents().setActionRow(
                                                Button.secondary("rock:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":" + userIdMatcher.group(3) + ":r", Emoji.fromMarkdown("\uD83D\uDDFF")),
                                                Button.secondary("paper:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":r:" + userIdMatcher.group(3) + ":r", Emoji.fromMarkdown("\uD83D\uDCDC")),
                                                Button.secondary("scissor:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":r:" + userIdMatcher.group(3) + ":r", Emoji.fromMarkdown("✂️")),
                                                Button.danger("cancelGame:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2),"X"))
                                        .queue();

                                break;
                        }

                        break;
                    default:
                        System.out.println("PLAYER TWO ALREADY CHOSE!");
                        break;
                }
            }

        }
        //  PERSON CLICKED PAPER
        else if(buttonInteractionEvent.getComponentId().startsWith("paper:"))
        {
            Matcher userIdMatcher = Pattern.compile("paper:(\\d{1,}):(\\d{1,}):([rpsn]):([rpsn])")
                    .matcher(buttonInteractionEvent.getComponentId());
            userIdMatcher.find();

            System.out.println("(PAPER OPTION) ROCK-PAPER-SCISSORS");
            System.out.println(userIdMatcher.group(0));
            System.out.println(userIdMatcher.group(1));
            System.out.println(userIdMatcher.group(2) + "\n");

            //  PLAYER ONE
            if(buttonInteractionEvent.getUser().getId().equals(userIdMatcher.group(1)))
            {
                buttonInteractionEvent.deferEdit().queue();

                switch(userIdMatcher.group(3))
                {
                    case "n":
                        //  PLAYER ONE CHOSE 'PAPER'
                        //  CHECK PLAYER TWO'S CHOICE BEFORE CONTINUING
                        switch(userIdMatcher.group(4))
                        {
                            case "r":
                                //  RESULT IN PLAYER ONE WINNING
                                MessageEmbed playerOneWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(userIdMatcher.group(1) + " has won!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerOneWonMessageEmbed)
                                        .queue();
                                break;
                            case "p":
                                //  RESULT IN TIE
                                tieRockPaperScissors(buttonInteractionEvent, "p");
                                break;
                            case "s":
                                //  RESULT IN PLAYER TWO WINNING
                                MessageEmbed playerTwoWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(userIdMatcher.group(2) + " has won!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerTwoWonMessageEmbed)
                                        .queue();
                                break;

                            default:
                                //  PLAYER TWO HASN'T CHOSEN ANYTHING YET.
                                buttonInteractionEvent.getHook().editOriginalComponents().setActionRow(
                                                Button.secondary("rock:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":p:" + userIdMatcher.group(4), Emoji.fromMarkdown("\uD83D\uDDFF")),
                                                Button.secondary("paper:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":p:" + userIdMatcher.group(4), Emoji.fromMarkdown("\uD83D\uDCDC")),
                                                Button.secondary("scissor:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":p:" + userIdMatcher.group(4), Emoji.fromMarkdown("✂️")),
                                                Button.danger("cancelGame:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2),"X"))
                                        .queue();

                                break;
                        }

                        break;
                    default:
                        System.out.println("PLAYER ONE ALREADY CHOSE!");
                        break;
                }
            }
            //  PLAYER TWO
            else if(buttonInteractionEvent.getUser().getId().equals(userIdMatcher.group(2)))
            {
                buttonInteractionEvent.deferEdit().queue();

                switch(userIdMatcher.group(4))
                {
                    case "n":
                        //  PLAYER TWO CHOSE 'PAPER'
                        //  CHECK PLAYER ONE'S CHOICE BEFORE CONTINUING
                        switch(userIdMatcher.group(3))
                        {
                            case "r":
                                //  RESULT IN PLAYER TWO WINNING
                                MessageEmbed playerOneWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(userIdMatcher.group(2) + " has won!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerOneWonMessageEmbed)
                                        .queue();
                                break;
                            case "p":
                                //  RESULT IN TIE
                                tieRockPaperScissors(buttonInteractionEvent, "p");
                                break;
                            case "s":
                                //  RESULT IN PLAYER ONE WINNING
                                MessageEmbed playerTwoWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(userIdMatcher.group(1) + " has won!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerTwoWonMessageEmbed)
                                        .queue();
                                break;

                            default:
                                //  PLAYER ONE HASN'T CHOSEN ANYTHING YET.
                                buttonInteractionEvent.getHook().editOriginalComponents().setActionRow(
                                                Button.secondary("rock:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":" + userIdMatcher.group(3) + ":p", Emoji.fromMarkdown("\uD83D\uDDFF")),
                                                Button.secondary("paper:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":" + userIdMatcher.group(3) + ":p", Emoji.fromMarkdown("\uD83D\uDCDC")),
                                                Button.secondary("scissor:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":" + userIdMatcher.group(3) + ":p", Emoji.fromMarkdown("✂️")),
                                                Button.danger("cancelGame:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2),"X"))
                                        .queue();

                                break;
                        }

                        break;
                    default:
                        System.out.println("PLAYER TWO ALREADY CHOSE!");
                        break;
                }
            }

        }
        //  PERSON CLICKED SCISSOR
        else if(buttonInteractionEvent.getComponentId().startsWith("scissor:"))
        {
            Matcher userIdMatcher = Pattern.compile("scissor:(\\d{1,}):(\\d{1,}):([rpsn]):([rpsn])")
                    .matcher(buttonInteractionEvent.getComponentId());
            userIdMatcher.find();

            System.out.println("(SCISSOR OPTION) ROCK-PAPER-SCISSORS");
            System.out.println(userIdMatcher.group(0));
            System.out.println(userIdMatcher.group(1));
            System.out.println(userIdMatcher.group(2) + "\n");

            //  PLAYER ONE
            if(buttonInteractionEvent.getUser().getId().equals(userIdMatcher.group(1)))
            {
                buttonInteractionEvent.deferEdit().queue();

                switch(userIdMatcher.group(3))
                {
                    case "n":
                        //  PLAYER ONE CHOSE 'SCISSOR'
                        //  CHECK PLAYER TWO'S CHOICE BEFORE CONTINUING
                        switch(userIdMatcher.group(4))
                        {
                            case "r":
                                //  RESULT IN PLAYER TWO WINNING
                                MessageEmbed playerTwoWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(userIdMatcher.group(2) + " has won!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerTwoWonMessageEmbed)
                                        .queue();

                                break;
                            case "p":
                                //  RESULT IN PLAYER ONE WINNING
                                MessageEmbed playerOneWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(userIdMatcher.group(1) + " has won!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerOneWonMessageEmbed)
                                        .queue();
                                break;
                            case "s":
                                //  RESULT IN TIE
                                tieRockPaperScissors(buttonInteractionEvent, "s");

                                break;

                            default:
                                //  PLAYER TWO HASN'T CHOSEN ANYTHING YET.
                                buttonInteractionEvent.getHook().editOriginalComponents().setActionRow(
                                                Button.secondary("rock:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":s:" + userIdMatcher.group(4), Emoji.fromMarkdown("\uD83D\uDDFF")),
                                                Button.secondary("paper:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":s:" + userIdMatcher.group(4), Emoji.fromMarkdown("\uD83D\uDCDC")),
                                                Button.secondary("scissor:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":s:" + userIdMatcher.group(4), Emoji.fromMarkdown("✂️")),
                                                Button.danger("cancelGame:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2),"X"))
                                        .queue();

                                break;
                        }

                        break;
                    default:
                        System.out.println("PLAYER ONE ALREADY CHOSE!");
                        break;
                }
            }
            //  PLAYER TWO
            else if(buttonInteractionEvent.getUser().getId().equals(userIdMatcher.group(2)))
            {
                buttonInteractionEvent.deferEdit().queue();

                switch(userIdMatcher.group(4))
                {
                    case "n":
                        //  PLAYER TWO CHOSE 'SCISSOR'
                        //  CHECK PLAYER ONE'S CHOICE BEFORE CONTINUING
                        switch(userIdMatcher.group(3))
                        {
                            case "r":
                                //  RESULT IN PLAYER ONE WINNING
                                MessageEmbed playerTwoWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(userIdMatcher.group(1) + " has won!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerTwoWonMessageEmbed)
                                        .queue();

                                break;
                            case "p":
                                //  RESULT IN PLAYER TWO WINNING
                                MessageEmbed playerOneWonMessageEmbed = new EmbedBuilder()
                                        .setColor(0xBABABA)
                                        .setTitle("Rock Paper Scissors")
                                        .setDescription(userIdMatcher.group(2) + " has won!")
                                        .build();

                                buttonInteractionEvent.getHook().editOriginalComponents(new ArrayList<>())
                                        .queue();
                                buttonInteractionEvent.getHook().editOriginalEmbeds(playerOneWonMessageEmbed)
                                        .queue();
                                break;
                            case "s":
                                //  RESULT IN TIE
                                tieRockPaperScissors(buttonInteractionEvent, "s");
                                break;

                            default:
                                //  PLAYER ONE HASN'T CHOSEN ANYTHING YET.
                                buttonInteractionEvent.getHook().editOriginalComponents().setActionRow(
                                                Button.secondary("rock:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":" + userIdMatcher.group(3) + ":s", Emoji.fromMarkdown("\uD83D\uDDFF")),
                                                Button.secondary("paper:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":" + userIdMatcher.group(3) + ":s", Emoji.fromMarkdown("\uD83D\uDCDC")),
                                                Button.secondary("scissor:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2) + ":" + userIdMatcher.group(3) + ":s", Emoji.fromMarkdown("✂️")),
                                                Button.danger("cancelGame:" + userIdMatcher.group(1) + ":" + userIdMatcher.group(2),"X"))
                                        .queue();

                                break;
                        }

                        break;
                    default:
                        System.out.println("PLAYER TWO ALREADY CHOSE!");
                        break;
                }
            }
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

        if(fromDeveloper(messageSenderId))
        {
            System.out.println(message.getGuild().getId());
        }

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
