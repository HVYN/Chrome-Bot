
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
        //  TOKEN:
        //  OTYwMjk1NDQxMTQ3MjYwOTk5.YkoW0g.8WryDhDx67_sHwWXWsr7a74zWnc

        //  BOT being BUILT
        JDA jda = JDABuilder.createDefault("OTYwMjk1NDQxMTQ3MjYwOTk5.YkoW0g.8WryDhDx67_sHwWXWsr7a74zWnc")
                //.enableIntents(GatewayIntent.GUILD_MEMBERS)
                //.setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new ChromeBotListener())
                .build();

    }
}
