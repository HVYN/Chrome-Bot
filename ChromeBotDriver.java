
//  CHROME-BOT (Driver Class)
//  04/03/2022

import karuta.ChromeBotKarutaListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ChromeBotDriver
{
    public static void main(String[] args) throws LoginException, InterruptedException, FileNotFoundException
    {
        final String TEST_SERVER_ID;
        final String FRANKY_BOT_TOKEN, CHROME_BOT_TOKEN;

        //  NOTE: Read important information like tokens from config file.
        Scanner configReader = new Scanner(new File("config.txt"));

        TEST_SERVER_ID = configReader.next().substring(15);

        FRANKY_BOT_TOKEN = configReader.next().substring(17);
        CHROME_BOT_TOKEN = configReader.next().substring(17);

        //  BOT being BUILT
        JDA jda = JDABuilder.createDefault(FRANKY_BOT_TOKEN)
                //.enableIntents(GatewayIntent.GUILD_MEMBERS)
                //.setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new ChromeBotListener())
                .addEventListeners(new ChromeBotRPSListener())
                .addEventListeners(new ChromeBotKarutaListener())
                .build();

        //  WAIT FOR JDA OBJECT TO ACTUALLY BE READY BEFORE DOING ANYTHING.
        jda.awaitReady();

        /*
        jda.upsertCommand("rockpaperscissors", "Challenge someone to Rock-Paper-Scissors!")
                .addOption(OptionType.USER, "opponent", "Username of the opponent.", true)
                .queue();
        jda.upsertCommand("coinflip", "Flip a coin!")
                .queue();

         */
        
        //  DEBUG: Display all commands the bot has in the TEST SERVER.
        jda.getGuildById(TEST_SERVER_ID).retrieveCommands().queue(commands -> {
            for(Command command : commands)
                System.out.println(command.getName() + " " + command.getApplicationId() + " " + command.getId());
        });
    }
}
