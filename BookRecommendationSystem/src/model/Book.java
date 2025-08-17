package model;

/**
 * File: Book.java
 * Author: Lucas Wu
 * Date: 2025-08-16
 *
 * Description: The book data model includes book attributes and verification methods
 * This class represents a book in the system, including its ID, title, author, genre,
 * average rating, availability status, and borrow count. It also includes methods for
 */
public class Book 
{
    private String strId;          
    private String strTitle;       
    private String strAuthor;      
    private String strGenre;       
    private double dblAvgRating;   
    private boolean isAvailable;   
    private int intBorrowCount;    

    /**
     * Constructor to initialize a book object with all attributes
     * @param strId - Book ID, must follow the format B001, B002, etc.
     * @param strTitle - Book title
     * @param strAuthor - Book author
     * @param strGenre - Book genre
     * @param dblAvgRating - Average rating (0.0 to 5.0)
     * @param isAvailable - Availability status (true if available, false if checked out)
     * @param intBorrowCount - Number of times the book has been borrowed
     */
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

    public String  getStrId()           { return strId; }
    public String  getStrTitle()        { return strTitle; }
    public String  getStrAuthor()       { return strAuthor; }
    public String  getStrGenre()        { return strGenre; }
    public double  getDblAvgRating()    { return dblAvgRating; }
    public boolean isAvailable()        { return isAvailable; }
    public int     getIntBorrowCount()  { return intBorrowCount; }

    public void setStrId(String strId)                  { this.strId = strId; }
    public void setStrTitle(String strTitle)            { this.strTitle = strTitle; }
    public void setStrAuthor(String strAuthor)          { this.strAuthor = strAuthor; }
    public void setStrGenre(String strGenre)            { this.strGenre = strGenre; }
    public void setDblAvgRating(double dblAvgRating)    { this.dblAvgRating = dblAvgRating; }
    public void setAvailable(boolean isAvailable)       { this.isAvailable = isAvailable; }
    public void setIntBorrowCount(int intBorrowCount)   { this.intBorrowCount = intBorrowCount; }


    /**
     * Validate the book attributes
     * @return - true if valid, false otherwise
     */
    public boolean validateBook() 
    {
        // The ID format must be B001, B002, etc
        if (strId == null || !strId.matches("B\\d{3}")) {
            return false;
        }
        if (strTitle == null || strTitle.isEmpty() ||
            strAuthor == null || strAuthor.isEmpty() ||
            strGenre  == null || strGenre.isEmpty()) 
        {
            return false;
        }
        // Average rating must be between 0.0 and 5.0
        if (dblAvgRating < 0.0 || dblAvgRating > 5.0) {
            return false;
        }
        return true;
    }


    /**
     * String representation of the book object
     * @return - formatted string with book details
     */
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
