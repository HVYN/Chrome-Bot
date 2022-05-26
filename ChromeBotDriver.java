
//  CHROME BOT MAIN
//  04/03/2022

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class ChromeBotDriver
{
    public static void main(String[] args) throws LoginException
    {
        //  TOKEN:
        //  OTYwMjk1NDQxMTQ3MjYwOTk5.YkoW0g.8WryDhDx67_sHwWXWsr7a74zWnc

        //  BOT being BUILT
        JDA jda = JDABuilder.createDefault("OTYwMjk1NDQxMTQ3MjYwOTk5.YkoW0g.8WryDhDx67_sHwWXWsr7a74zWnc")
                .addEventListeners(new ChromeBotListener())
                .build();

    }
}
