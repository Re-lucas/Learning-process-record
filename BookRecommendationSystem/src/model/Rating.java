/**
* File: Rating.java
* Author: Lucas Wu
* Date: 2025-08-16
*
* Description: 
*  - Represents a single user rating for a book
*  - Validates that the rating is between 1 and 5 upon construction
*  - Exposes getters for user ID, book ID, and the rating value
*  - Provides a formatted toString for display
**/
package model;

public class Rating 
{
    private String strUserId;    // ID of the user who rated
    private String strBookId;    // ID of the book being rated
    private int    intRating;    // Rating value (1–5)


    /**
     * Constructs a new Rating instance
     * @param strUserId - ID of the user who rated
     * @param strBookId - ID of the book being rated
     * @param intRating - Rating value (1–5)
     */
    public Rating(String strUserId, String strBookId, int intRating) 
    {
        if (intRating < 1 || intRating > 5)  // Verify rating range
        {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        this.strUserId = strUserId;
        this.strBookId = strBookId;
        this.intRating = intRating;
    }

    /**
     * Gets the user ID who submitted the rating
     * @return - the user ID
     */
    public String getStrUserId() 
    {
        return strUserId;
    }


    /**
    * Gets the book ID that was rated
    * @return the book ID
    **/
    public String getStrBookId() 
    {
        return strBookId;
    }


    /**
    * Gets the numeric rating value
    * @return the rating (1–5)
    **/
    public int getIntRating() 
    {
        return intRating;
    }


    /**
    * Returns a string representation of this Rating
    * @return formatted string
    **/
    @Override
    public String toString() 
    {
        return String.format(
            "Rating[user=%s, book=%s, score=%d]",
            strUserId,
            strBookId,
            intRating
        );
    }
}