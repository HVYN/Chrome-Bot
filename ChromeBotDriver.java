
//  CHROME-BOT (Driver Class)
//  04/03/2022

import karuta.ChromeBotKarutaListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.security.auth.login.LoginException;

public class ChromeBotDriver
{
    public static void main(String[] args) throws LoginException, InterruptedException
    {
        //  TEST SERVER (ID)
        //  295301099512922114

        //  FRANKY BOT TOKEN:
        //  ODg3ODAwNDcxNDk3MTEzNjkx.GM6zmn.RSv1awKuAwjGkMXRpunL2vzsI6WNwLcEmHvvcU

        //  CHROME BOT TOKEN:
        //  OTYwMjk1NDQxMTQ3MjYwOTk5.YkoW0g.8WryDhDx67_sHwWXWsr7a74zWnc

        final String TEST_SERVER_ID = "295301099512922114";

        final String FRANKY_BOT_TOKEN = "ODg3ODAwNDcxNDk3MTEzNjkx.GM6zmn.RSv1awKuAwjGkMXRpunL2vzsI6WNwLcEmHvvcU";
        final String CHROME_BOT_TOKEN = "OTYwMjk1NDQxMTQ3MjYwOTk5.YkoW0g.8WryDhDx67_sHwWXWsr7a74zWnc";

        //  BOT being BUILT
        JDA jda = JDABuilder.createDefault(FRANKY_BOT_TOKEN)
                //.enableIntents(GatewayIntent.GUILD_MEMBERS)
                //.setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new ChromeBotListener())
                .addEventListeners(new ChromeBotKarutaListener())
                .build();

        //  WAIT FOR JDA OBJECT TO ACTUALLY BE READY BEFORE DOING ANYTHING.
        jda.awaitReady();

        //  Guild testServerGuild = jda.getGuildById(TEST_SERVER_ID);
        jda.getGuildById(TEST_SERVER_ID).upsertCommand("rockpaperscissors", "Challenge someone to a game of Rock-Paper-Scissors!")
                .addOption(OptionType.USER, "opponent", "Username of the opponent.", true)
                    .queue();


    }
}
