
//  RESULT CLASS TO STORE SUCCESS/FAILURE INFORMATION

public class DateResult
{
    private int fuel, hunger, thirst, happiness, time;
    private int resultNum;

    private String status;

    private String path;

    private static int resultCountingNum = 0;

    public DateResult(int fuel, int hunger, int thirst, int happiness, int time,
                      String status, String path)
    {
        this.fuel = fuel;   this.hunger = hunger;   this.thirst = thirst;
        this.happiness = happiness;
        this.time = time;

        this.status = status;
        this.path = path;

        resultNum = resultCountingNum;

        resultCountingNum++;
    }

    public int getFuel()        {   return fuel;        }
    public int getHunger()      {   return hunger;      }
    public int getThirst()      {   return thirst;      }
    public int getHappiness()   {   return happiness;   }
    public int getTime()        {   return time;        }

    public String getPath()     {   return path;        }

    public int getAffectionPoints()
    {
        if(status.equals("HOME") || status.equals("FAILURE"))
            return (int)Math.ceil((double)
                    ((((getHunger() + getThirst() + getHappiness()) / 6) * (100 - getTime())) / 100));
        else
            return (int)Math.ceil((double)((getHunger() + getThirst() + getHappiness()) / 6));
    }

    public String getStatus()   {   return status;      }

    public static int getResultCountingNum() {  return resultCountingNum;    }

    @Override
    public String toString()
    {
        return "[RESULT " + resultNum + " | " + getStatus() + "]\n" +
                "\tFUEL: " + getFuel() + "\n" +
                "\tFOOD: " + getHunger() + "\n" +
                "\tDRINK: " + getThirst() + "\n" +
                "\tHAPPINESS: " + getHappiness() + "\n" +
                "\tTIME: " + getTime() + "\n\n" +
                "\t" + getPath() + "\n\n" +
                "\tAP: " + getAffectionPoints() + "\n";
    }

}
