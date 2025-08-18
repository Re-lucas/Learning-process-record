package model;

public class Book 
{
    private String strId;          
    private String strTitle;       
    private String strAuthor;      
    private String strGenre;       
    private double dblAvgRating;   
    private boolean isAvailable;   
    private int intBorrowCount;    


    public Book(String strId, String strTitle, String strAuthor, 
                String strGenre, double dblAvgRating, 
                boolean isAvailable, int intBorrowCount) 
    {
        this.strId           = strId;
        this.strTitle        = strTitle;
        this.strAuthor       = strAuthor;
        this.strGenre        = strGenre;
        this.dblAvgRating    = dblAvgRating;
        this.isAvailable     = isAvailable;
        this.intBorrowCount  = intBorrowCount;
    }

    public String  getStrId()           
    { 
        return strId; 
    }

    public String  getStrTitle()        
    { 
        return strTitle; 
    }

    public String  getStrAuthor()       
    { 
        return strAuthor; 
    }

    public String  getStrGenre()        
    { 
        return strGenre; 
    }

    public double  getDblAvgRating()    
    { 
        return dblAvgRating; 
    }

    public boolean isAvailable()        
    { 
        return isAvailable; 
    }

    public int     getIntBorrowCount()  
    { 
        return intBorrowCount; 
    }

    public void setStrId(String strId)                  
    { 
        this.strId = strId; 
    }

    public void setStrTitle(String strTitle)            
    { 
        this.strTitle = strTitle; 
    }

    public void setStrAuthor(String strAuthor)          
    { 
        this.strAuthor = strAuthor; 
    }

    public void setStrGenre(String strGenre)            
    { 
        this.strGenre = strGenre; 
    }

    public void setDblAvgRating(double dblAvgRating)    
    { 
        this.dblAvgRating = dblAvgRating; 
    }

    public void setAvailable(boolean isAvailable)       
    { 
        this.isAvailable = isAvailable; 
    }

    public void setIntBorrowCount(int intBorrowCount)   
    { 
        this.intBorrowCount = intBorrowCount; 
    }



    public boolean validateBook() 
    {
        if (strId == null || !strId.matches("B\\d{3}")) {
            return false;
        }
        if (strTitle == null || strTitle.isEmpty() ||
            strAuthor == null || strAuthor.isEmpty() ||
            strGenre  == null || strGenre.isEmpty()) 
        {
            return false;
        }
        if (dblAvgRating < 0.0 || dblAvgRating > 5.0) {
            return false;
        }
        return true;
    }



    @Override
    public String toString() 
    {
        return String.format(
            "Book[%s] %s by %s — %s, Rating: %.1f, Borrowed %d times, %s",
            strId, strTitle, strAuthor, strGenre,
            dblAvgRating, intBorrowCount,
            (isAvailable ? "Available" : "Checked out")
        );
    }
}
