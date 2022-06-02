
//  CHROME-BOT (Driver Class)
//  04/03/2022

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class ChromeBotDriver
{
    public static void main(String[] args) throws LoginException
    {
        //  FRANKY BOT TOKEN:
        //  ODg3ODAwNDcxNDk3MTEzNjkx.GM6zmn.RSv1awKuAwjGkMXRpunL2vzsI6WNwLcEmHvvcU

        //  CHROME BOT TOKEN:
        //  OTYwMjk1NDQxMTQ3MjYwOTk5.YkoW0g.8WryDhDx67_sHwWXWsr7a74zWnc

        final String FRANKY_BOT_TOKEN = "ODg3ODAwNDcxNDk3MTEzNjkx.GM6zmn.RSv1awKuAwjGkMXRpunL2vzsI6WNwLcEmHvvcU";
        final String CHROME_BOT_TOKEN = "OTYwMjk1NDQxMTQ3MjYwOTk5.YkoW0g.8WryDhDx67_sHwWXWsr7a74zWnc";

        //  BOT being BUILT
        JDA jda = JDABuilder.createDefault(FRANKY_BOT_TOKEN)
                //.enableIntents(GatewayIntent.GUILD_MEMBERS)
                //.setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new ChromeBotListener())
                .addEventListeners(new ChromeBotKarutaListener())
                .build();

    }
}
