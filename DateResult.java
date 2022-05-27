
//  RESULT CLASS TO STORE SUCCESS/FAILURE INFORMATION

public class DateResult
{
    private static int resultCountingNum = 0;

    //  private int fuel, hunger, thirst, happiness, time;
    private int resultNum;
    private int affectionPoints;

    //  private String status;

    private String path;

    private ResultType resultType;

    /*
    //  CONSTRUCTOR: ISSUES include OutofMemory, must found alternative
    public DateResult(int fuel, int hunger, int thirst, int happiness, int time,
                      String status, String path)
    {
        this.fuel = fuel;   this.hunger = hunger;   this.thirst = thirst;
        this.happiness = happiness;
        this.time = time;

        this.status = status;
        this.path = path;

        //  DEBUG: Assign number
        resultNum = resultCountingNum;

        //  DEBUG: Increment/Count results (Next result will use the
        //      number as its 'designation'/ID
        resultCountingNum++;
    }

     */

    //  CONSTRUCTOR: Knowing exact resource information is unnecessary at the
    //      finality, so just include ResultType for information instead
    public DateResult(ResultType resultType, int affectionPoints, String path)
    {
        this.resultType = resultType;

        this.path = path;
        this.affectionPoints = affectionPoints;

        resultNum = resultCountingNum;

        resultCountingNum++;
    }

    //  public int getFuel()        {   return fuel;        }
    //  public int getHunger()      {   return hunger;      }
    //  public int getThirst()      {   return thirst;      }
    //  public int getHappiness()   {   return happiness;   }
    //  public int getTime()        {   return time;        }

    public String getPath()     {   return path;        }

    public int getAffectionPoints() {   return affectionPoints; }

    public ResultType getResultType()   {   return resultType;  }

    /*
    //  * DEPRECATED *
    //  Calculate Affection Points using this formula below:
    //      (Affection Points) = ((Hunger + Thirst + Happiness) / 6) * ((100 - Remaining Time) / 100)
    public int getAffectionPoints()
    {
        if(status.equals("HOME") || status.equals("FAILURE"))
            return (int)Math.ceil((double)
                    ((((getHunger() + getThirst() + getHappiness()) / 6) * (100 - getTime())) / 100));
        else
            return (int)Math.ceil((double)((getHunger() + getThirst() + getHappiness()) / 6));
    }
    */

    //  public String getStatus()   {   return status;      }

    //  public static int getResultCountingNum() {  return resultCountingNum;    }

    @Override
    public String toString()
    {
        return //"[RESULT " + resultNum + " | " + getStatus() + "]\n" +
                // "\tFUEL: " + getFuel() + "\n" +
                // "\tFOOD: " + getHunger() + "\n" +
                // "\tDRINK: " + getThirst() + "\n" +
                // "\tHAPPINESS: " + getHappiness() + "\n" +
                // "\tTIME: " + getTime() + "\n\n" +
                "[RESULT " + resultNum + " | " + getResultType() + "]\n" +
                        "\t" + getPath() + "\n\n" +
                        "\tAP: " + getAffectionPoints() + "\n";
    }

}