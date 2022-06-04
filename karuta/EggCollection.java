package karuta;

//  EGG COLLECTION CLASS
//  04/05/2022

/*
    (UNUSED/(MOSTLY) DEPRECATED)

    This class helped was supposed to keep track of egg collections during the Spring Egg
        event, but since the event has passed has no real use.
 */

public class EggCollection
{
    private boolean hasEggOne, hasEggTwo, hasEggThree, hasEggFour, hasEggFive,
            hasEggSix, hasEggSeven, hasEggEight, hasEggNine, hasEggTen,
            hasEggEleven, hasEggTwelve, hasEggThirteen, hasEggFourteen, hasEggFifteen,
            hasEggSixteen, hasEggSeventeen, hasEggEighteen, hasEggNineteen, hasEggTwenty;

    //  DEFAULT CONSTRUCTOR.
    public EggCollection()
    {
        resetEggCollection();
    }

    //  RESET EGG STATUSES.
    public void resetEggCollection()
    {
        hasEggOne = hasEggTwo = hasEggThree = hasEggFour = hasEggFive =
                hasEggSix = hasEggSeven = hasEggEight = hasEggNine = hasEggTen =
                hasEggEleven = hasEggTwelve = hasEggThirteen = hasEggFourteen = hasEggFifteen =
                hasEggSixteen = hasEggSeventeen = hasEggEighteen = hasEggNineteen = hasEggTwenty = false;
    }

    //  ACQUIRED EGG => EGGS ARE HAD, SET TO TRUE.
    //  CANNOT LOSE EGGS UNTIL ALL EGGS ARE GOTTEN / CLAIMED.
    public void acquiredEggOne()        {   hasEggOne = true;       }
    public void acquiredEggTwo()        {   hasEggTwo = true;       }
    public void acquiredEggThree()      {   hasEggThree = true;     }
    public void acquiredEggFour()       {   hasEggFour = true;      }
    public void acquiredEggFive()       {   hasEggFive = true;      }
    public void acquiredEggSix()        {   hasEggSix = true;       }
    public void acquiredEggSeven()      {   hasEggSeven = true;     }
    public void acquiredEggEight()      {   hasEggEight = true;     }
    public void acquiredEggNine()       {   hasEggNine = true;      }
    public void acquiredEggTen()        {   hasEggTen = true;       }
    public void acquiredEggEleven()     {   hasEggEleven = true;    }
    public void acquiredEggTwelve()     {   hasEggTwelve = true;    }
    public void acquiredEggThirteen()   {   hasEggThirteen = true;  }
    public void acquiredEggFourteen()   {   hasEggFourteen = true;  }
    public void acquiredEggFifteen()    {   hasEggFifteen = true;   }
    public void acquiredEggSixteen()    {   hasEggSixteen = true;   }
    public void acquiredEggSeventeen()  {   hasEggSeventeen = true; }
    public void acquiredEggEighteen()   {   hasEggEighteen = true;  }
    public void acquiredEggNineteen()   {   hasEggNineteen = true;  }
    public void acquiredEggTwenty()     {   hasEggTwenty = true;    }
}
