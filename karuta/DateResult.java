package karuta;

//  RESULT CLASS TO STORE SUCCESS/FAILURE INFORMATION

public class DateResult
{
    private static int resultCountingNum = 0;

    private boolean mallVisited;

    private int resultNum;

    private int fuel, hunger, thirst, happiness, time;

    private String path;

    private ResultType resultType;

    //  NEW CONSTRUCTOR: Take individual resource parameters, and return parameters if
    //      the result ended at an Airport, and return Affection Points otherwise.
    public DateResult(ResultType resultType, int fuel, int hunger, int thirst, int happiness, int time, boolean mallVisited, String path)
    {
        this.resultType = resultType;

        this.path = path;

        this.fuel = fuel;
        this.hunger = hunger;
        this.thirst = thirst;
        this.happiness = happiness;
        this.time = time;

        this.mallVisited = mallVisited;

        resultNum = resultCountingNum;

        resultCountingNum++;
    }

    public String getPath()     {   return path;        }

    //  NOTE: Instead of calculating the AP in the solver class and placing it in the result,
    //      just store individual resources, and calculate when needed.
    public double getAffectionPoints()
    {
        double resourceAverage = (hunger + thirst + happiness) / 6.0;

        switch(resultType)
        {
            case SUCCESS:
                if(mallVisited)
                    resourceAverage += 30;
                break;
            case RETURNED_HOME:
            case REACHED_AIRPORT:
                double timeWeight = (100 - time - 4) / 100.0;

                resourceAverage *= timeWeight;

                if(mallVisited)
                    resourceAverage += 30;

                break;

            default:
                System.out.println("ERROR: UNKNOWN RESULT TYPE TRYING TO GET AP!");
                break;
        }

        return resourceAverage;
    }

    public ResultType getResultType()   {   return resultType;  }

    //  NOTE: This return method is for displaying resources in the embed message
    //      late for the user. (FOR AIRPORT RESULT USE)
    public String getResources()
    {
        return "\nFUEL | " + fuel +
                "\nFOOD | " + hunger + "\nDRINK | " + thirst + "\nHAPPINESS | " + happiness +
                "\nTIME | " + time;
    }

    @Override
    public String toString()
    {
        return "[RESULT " + resultNum + " | " + getResultType() + "]\n" +
                "\t" + getPath() + "\n\n" +
                "\tAP: " + getAffectionPoints() + "\n";
    }

}