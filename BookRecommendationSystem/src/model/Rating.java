package model;


public class Rating 
{
    private String strUserId;    
    private String strBookId;    
    private int    intRating;   


    public Rating(String strUserId, String strBookId, int intRating) 
    {
        if (intRating < 1 || intRating > 5) 
        {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.strUserId = strUserId;
        this.strBookId = strBookId;
        this.intRating = intRating;
    }

    public String getStrUserId()  
    { 
        return strUserId; 
    }

    public String getStrBookId()  
    { 
        return strBookId; 
    }

    public int    getIntRating()  
    { 
        return intRating; 
    }

    @Override
    public String toString() 
    {
        return String.format("Rating[user=%s, book=%s, score=%d]",
                             strUserId, strBookId, intRating);
    }
}
