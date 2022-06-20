package general;

//  Every Wed/Thurs post a gif/video to text channel '983239709922836554'.

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

public class WedThurs extends Thread
{
    private JDA jda;

    private ZoneId timeZone;

    //  CONSTRUCTOR: Take JDA object, so we have some way to print
    //      out the message when it is Wednesday/Thursday.
    public WedThurs(JDA jda)
    {
        this.jda = jda;

        timeZone = ZoneId.of("America/Chicago");
    }

    @Override
    public void run()
    {
        System.out.println("'WEDTHURS' Thread is running!'");

        while(true)
        {
            try
            {
                //  ONLY CHECK EVERY 10 SECONDS IF THE DATE IS WEDNESDAY
                Thread.sleep(10000);

                if(LocalDate.now(timeZone).getDayOfWeek() == DayOfWeek.WEDNESDAY ||
                    LocalDate.now(timeZone).getDayOfWeek() == DayOfWeek.THURSDAY)
                {
                    final String PERROS_ID = "983239709922836551";

                    //  NOTE: COULD COMBINE USER MENTIONS AND FILE INTO MESSAGE (MessageBuilder)

                    //  FIRST SEND FILE.
                    jda.getGuildById(PERROS_ID).getTextChannelById(983239709922836554L)
                            .sendFile(new File("asukaWedThurs.mp4"))
                            .queue();

                    //  NEXT, MENTION THREE USERS. (264525243585003532) (251216227815522306) (367476926874386445)
                    jda.getGuildById(PERROS_ID).retrieveMembersByIds(264525243585003532L, 251216227815522306L, 367476926874386445L)
                            .onSuccess(members -> {

                                MessageBuilder mentionMessageBuilder = new MessageBuilder();

                                for(Member member : members)
                                    mentionMessageBuilder.append(member.getAsMention() + " ");

                                jda.getGuildById(PERROS_ID).getTextChannelById(983239709922836554L)
                                        .sendMessage(mentionMessageBuilder.build())
                                        .queue();
                            });
                }

                System.out.println(LocalDate.now(timeZone).getDayOfWeek());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();

                throw new RuntimeException(e);
            }
        }
    }

}
