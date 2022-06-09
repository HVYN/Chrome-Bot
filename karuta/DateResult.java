package karuta;
//  RESULT CLASS TO STORE SUCCESS/FAILURE INFORMATION

public class DateResult
{
    private static int resultCountingNum = 0;

    private int resultNum;

    private double affectionPoints;

    private String path;

    private ResultType resultType;

    //  CONSTRUCTOR: Knowing exact resource information is unnecessary at the
    //      finality, so just include karuta.ResultType for information instead
    public DateResult(ResultType resultType, double affectionPoints, String path)
    {
        this.resultType = resultType;

        this.path = path;
        this.affectionPoints = affectionPoints;

        resultNum = resultCountingNum;

        resultCountingNum++;
    }

    public String getPath()     {   return path;        }

    public double getAffectionPoints() {   return affectionPoints; }

    public ResultType getResultType()   {   return resultType;  }

    @Override
    public String toString()
    {
        return "[RESULT " + resultNum + " | " + getResultType() + "]\n" +
                "\t" + getPath() + "\n\n" +
                "\tAP: " + getAffectionPoints() + "\n";
    }

}