/**
* File: Book.java
* Author: YE
* Date: 2025-08-18
*
* Description:
*  - Represents a book entity with identification, metadata, and status
*  - Provides getters for ID, title, author, genre, average rating, availability, and borrow count
*  - Provides setters to update all book fields
*  - Validates book data format (ID pattern, non-empty fields, rating range)
*  - Overrides toString to display formatted book information
**/
package model;

public class Book 
{
    private String strId;            // Unique book identifier (e.g., B001)
    private String strTitle;         // Book title
    private String strAuthor;        // Book author
    private String strGenre;         // Book genre/category
    private double dblAvgRating;     // Average rating between 0.0 and 5.0
    private boolean isAvailable;     // Availability status
    private int intBorrowCount;      // Total times borrowed


    /**
    * Constructs a Book with all properties
    * @param strId          unique book ID following pattern B### 
    * @param strTitle       title of the book
    * @param strAuthor      author of the book
    * @param strGenre       genre or category of the book
    * @param dblAvgRating   average rating from 0.0 to 5.0
    * @param isAvailable    availability flag
    * @param intBorrowCount number of times this book has been borrowed
    **/
    public Book(String strId, String strTitle, String strAuthor, String strGenre,
                double dblAvgRating, boolean isAvailable, int intBorrowCount) 
    {
        this.strId          = strId;
        this.strTitle       = strTitle;
        this.strAuthor      = strAuthor;
        this.strGenre       = strGenre;
        this.dblAvgRating   = dblAvgRating;
        this.isAvailable    = isAvailable;
        this.intBorrowCount = intBorrowCount;
    }


    /**
    * Gets the book ID
    * @return the book's unique identifier
    **/
    public String getStrId() 
    {
        return strId;  
    }


    /**
    * Gets the book title
    * @return the title
    **/
    public String getStrTitle() 
    {
        return strTitle;  
    }


    /**
    * Gets the author name
    * @return the author
    **/
    public String getStrAuthor() 
    {
        return strAuthor;  
    }


    /**
    * Gets the genre
    * @return the genre
    **/
    public String getStrGenre() 
    {
        return strGenre;  
    }


    /**
    * Gets the average rating
    * @return the average rating
    **/
    public double getDblAvgRating() 
    {
        return dblAvgRating;  
    }


    /**
    * Checks availability
    * @return true if available; false otherwise
    **/
    public boolean isAvailable() 
    {
        return isAvailable;  
    }


    /**
    * Gets the borrow count
    * @return total borrow count
    **/
    public int getIntBorrowCount() 
    {
        return intBorrowCount;  
    }


    /**
    * Sets the book ID
    * @param strId the new book ID
    **/
    public void setStrId(String strId) 
    {
        this.strId = strId;  
    }


    /**
    * Sets the book title
    * @param strTitle the new title
    **/
    public void setStrTitle(String strTitle) 
    {
        this.strTitle = strTitle;  
    }


    /**
    * Sets the author name
    * @param strAuthor the new author
    **/
    public void setStrAuthor(String strAuthor) 
    {
        this.strAuthor = strAuthor;  
    }


    /**
    * Sets the genre
    * @param strGenre the new genre
    **/
    public void setStrGenre(String strGenre) 
    {
        this.strGenre = strGenre;  
    }


    /**
    * Sets the average rating
    * @param dblAvgRating the new average rating
    **/
    public void setDblAvgRating(double dblAvgRating) 
    {
        this.dblAvgRating = dblAvgRating;  
    }


    /**
    * Sets availability status
    * @param isAvailable the new availability flag
    **/
    public void setAvailable(boolean isAvailable) 
    {
        this.isAvailable = isAvailable;  
    }


    /**
    * Sets the borrow count
    * @param intBorrowCount the new borrow count
    **/
    public void setIntBorrowCount(int intBorrowCount) 
    {
        this.intBorrowCount = intBorrowCount;  
    }


    /**
    * Validates the book's properties:
    *  - ID matches B\\d{3}
    *  - Title, author, genre are non-null and non-empty
    *  - Average rating between 0.0 and 5.0
    * @return true if valid; false otherwise
    **/
    public boolean validateBook() 
    {
        if (strId == null || !strId.matches("B\\d{3}"))  // ID format check
        {
            return false;  
        }

        if (strTitle == null || strTitle.isEmpty()        // Title non-empty
            || strAuthor == null || strAuthor.isEmpty()   // Author non-empty
            || strGenre == null || strGenre.isEmpty())    // Genre non-empty
        {
            return false;  
        }

        if (dblAvgRating < 0.0 || dblAvgRating > 5.0)     // Rating range
        {
            return false;  
        }

        return true;  
    }


    /**
    * Formats the book details for display
    * @return formatted string representing the book
    **/
    @Override
    public String toString() 
    {
        String strStatus = isAvailable ? "Available" : "Checked out";  // Status text
        return String.format(
            "Book[%s] %s by %s — %s, Rating: %.1f, Borrowed %d times, %s",
            strId, strTitle, strAuthor, strGenre,
            dblAvgRating, intBorrowCount, strStatus
        );
    }
}