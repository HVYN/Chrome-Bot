package general;

//  Every Wed/Thurs post a gif/video to text channel '983239709922836554'.

import io.lettuce.core.api.sync.RedisCommands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class WedThurs extends Thread
{
    private final JDA jda;

    private final RedisCommands<String, String> redisSyncCommands;

    private final ZoneId timeZone;

    //  CONSTRUCTOR: Take JDA object, so we have some way to print
    //      out the message when it is Wednesday/Thursday.
    public WedThurs(JDA jda, RedisCommands<String, String> redisSyncCommands)
    {
        this.jda = jda;

        this.redisSyncCommands = redisSyncCommands;

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

                //  NOTE: COULD COMBINE USER MENTIONS AND FILE INTO MESSAGE (MessageBuilder)
                switch (LocalDate.now(timeZone).getDayOfWeek())
                {
                    case WEDNESDAY:

                        //  NOTE: If this bot has not posted for Wednesday yet, then post video,
                        //      and set flag to true.
                        if(!Boolean.parseBoolean(redisSyncCommands.get("aliSchizoWed")))
                        {
                            System.out.println("WEDNESDAY: POSTING VIDEO.");

                            postVideoAndNames();

                            redisSyncCommands.set("aliSchizoWed", "true");

                            //  NOTE: After posting on Wednesday, no need to keep looping, just sleep for 24 HR.
                            Thread.sleep(1000 * 60 * 60 * 24);
                        }
                        else
                            System.out.println("WEDNESDAY: VIDEO POSTED ALREADY.");

                        break;
                    case THURSDAY:

                        //  NOTE: Perform same check and flag switch for Thursday.
                        if(!Boolean.parseBoolean(redisSyncCommands.get("aliSchizoThurs")))
                        {
                            System.out.println("THURSDAY: POSTING VIDEO.");

                            postVideoAndNames();

                            redisSyncCommands.set("aliSchizoThurs", "true");

                            //  NOTE: Same as Wednesday, sleep for day after posting.
                            Thread.sleep(1000 * 60 * 60 * 24);
                        }
                        else
                            System.out.println("THURSDAY: VIDEO POSTED ALREADY.");

                        break;

                    default:

                        System.out.println(LocalDate.now(timeZone).getDayOfWeek() + " " + LocalTime.now(timeZone));

                        //  NOTE: After a successful cycle, first day after last post would be Friday.
                        redisSyncCommands.set("aliSchizoWed", "false");
                        redisSyncCommands.set("aliSchizoThurs", "false");

                        break;
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();

                throw new RuntimeException(e);
            }
        }
    }

    //  HELPER: Side function to display MP4 file and people's names (as mentions).
    private void postVideoAndNames()
    {
        final String PERROS_ID = "983239709922836551";
        final String PERROS_GENERAL_ID = "983239709922836554";

        //  FIRST SEND FILE.
        jda.getGuildById(PERROS_ID).getTextChannelById(PERROS_GENERAL_ID)
                .sendFile(new File("asukaWedThurs.mp4"))
                .queue();

        //  NEXT, MENTION THREE USERS. (264525243585003532) (251216227815522306) (367476926874386445)
        jda.getGuildById(PERROS_ID).retrieveMembersByIds("251216227815522306", "264525243585003532", "367476926874386445")
                .onSuccess(members -> {

                    MessageBuilder mentionMessageBuilder = new MessageBuilder();

                    for(Member member : members)
                    {
                        mentionMessageBuilder.append(member.getAsMention());
                        mentionMessageBuilder.append(" ");
                    }

                    jda.getGuildById(PERROS_ID).getTextChannelById(PERROS_GENERAL_ID)
                            .sendMessage(mentionMessageBuilder.build())
                            .queue();
                });

    }

}
