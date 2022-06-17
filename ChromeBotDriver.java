
//  CHROME-BOT (Driver Class)
//  04/03/2022

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import karuta.ChromeBotKarutaListener;

import io.lettuce.core.RedisClient;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.Command;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ChromeBotDriver
{
    public static void main(String[] args) throws LoginException, InterruptedException, FileNotFoundException
    {
        final String TEST_SERVER_ID;
        final String FRANKY_BOT_TOKEN, CHROME_BOT_TOKEN;
        final String REDIS_PASSWORD, REDIS_HOST_PORT;

        //  NOTE: Read important information like tokens from config file.
        Scanner configReader = new Scanner(new File("config.txt"));

        TEST_SERVER_ID = configReader.next().substring(15);

        FRANKY_BOT_TOKEN = configReader.next().substring(17);
        CHROME_BOT_TOKEN = configReader.next().substring(17);

        REDIS_PASSWORD = configReader.next().substring(15);
        REDIS_HOST_PORT = configReader.next().substring(16);

        //  INITIALIZE REDIS CLIENT
        RedisClient redisClient = RedisClient.create("redis://" + REDIS_PASSWORD + "@" + REDIS_HOST_PORT);
            StatefulRedisConnection<String, String> redisConnection = redisClient.connect();
            RedisCommands<String, String> redisSyncCommands = redisConnection.sync();

        //  IF CONNECTION IS NOT SUCCESSFUL, STOP EVERYTHING.
        if(redisConnection.isOpen())
        {
            System.out.println("CONNECTION TO REDIS SUCCESSFUL.");

            System.out.println("! DISPLAYING REDIS DB !");

            for(String key : redisSyncCommands.scan().getKeys())
                System.out.println(key + "\n\t" + redisSyncCommands.get(key));

            //  BOT being BUILT
            JDA jda = JDABuilder.createDefault(FRANKY_BOT_TOKEN)
                    //.enableIntents(GatewayIntent.GUILD_MEMBERS)
                    //.setMemberCachePolicy(MemberCachePolicy.ALL)
                    .addEventListeners(new ChromeBotListener())
                    .addEventListeners(new ChromeBotRPSListener())
                    .addEventListeners(new ChromeBotKarutaListener(redisConnection))
                    .build();

            //  WAIT FOR JDA OBJECT TO ACTUALLY BE READY BEFORE DOING ANYTHING.
            jda.awaitReady();

            if(jda.getToken().equals(CHROME_BOT_TOKEN))
            {
                //  NOW SINCE JDA OBJECT IS READY, START THREAD WITH THAT OBJECT.
                WedThurs WTPrinter = new WedThurs(jda);
                WTPrinter.start();
            }

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
        else
            System.out.println("ERROR: SOMETHING WENT WRONG WHILE TRYING TO CONNECT TO REDIS!");

    }
}
