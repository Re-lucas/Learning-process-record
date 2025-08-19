/**
 * File: RecommendationEngine.java
 * Author: Lucas Wu
 * Date: 2025-08-18
 *
 * Description:
 *  - Loads existing user ratings from CSV on initialization
 *  - Generates personalized book recommendations based on:
 *      - User's preferred genre and author
 *      - Book average rating
 *      - Book popularity (borrow count)
 *  - Persists new or updated ratings back to CSV and updates book average ratings
 *  - Allows dynamic adjustment of recommendation weights for fine-tuning
 * 
 * Work Log (Lucas Wu):
 *  2025-08-18:
 *    - Added comprehensive JavaDoc comments for all methods
 *    - Refactored variable names to improve code clarity and consistency
 *    - Enhanced documentation of recommendation algorithm
 *  
 *  2025-08-17:
 *    - Debugged and optimized recommendation algorithm
 *    - Implemented rating persistence and book rating updates
 *    - Added dynamic weight adjustment functionality
 *    - Conducted thorough testing to ensure correct operation
 *  
 *  2025-08-16:
 *    - Initial implementation of core recommendation engine:
 *        • Personalized recommendation generation
 *        • User rating management system
 *        • Preference calculation (genre/author)
 *        • Book scoring algorithm
 *        • Weight-based recommendation tuning
 * 
**/
package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Book;
import model.Rating;
import util.FileUtils;

public class RecommendationEngine 
{
    private BookDatabase bookDatabase;      // Source of book data
    private List<Rating> ratingList;        // All user ratings

    private double dblGenreWeight      = 0.4;  // Weight for matching genre
    private double dblAuthorWeight     = 0.3;  // Weight for matching author
    private double dblRatingWeight     = 0.2;  // Weight for book's average rating
    private double dblPopularityWeight = 0.1;  // Weight for borrow count popularity


    /**
     * Constructs the recommendation engine and loads ratings
     * @param bookDatabase - the BookDatabase instance to use
     */
    public RecommendationEngine(BookDatabase bookDatabase) 
    {
        this.bookDatabase = bookDatabase;
        this.ratingList   = FileUtils.loadRatingsFromCSV();  // Load ratings from storage
        System.out.println("Loaded " + ratingList.size() + " ratings");
    }


    /**
     * Generates top-N book recommendations for a user
     * @param strUserId - the user's unique identifier
     * @param intCount - the maximum number of recommendations
     * @return - list of recommended books
     */
    public List<Book> generateRecommendations(String strUserId, int intCount) 
    {
        List<Rating> userRatingList = getUserRatings(strUserId);  // Fetch user's past ratings

        if (userRatingList.isEmpty())  // Cold start: no history
        {
            return bookDatabase.getPopularBooks(intCount);  // Fallback to popular books
        }

        String strPrefGenre  = calculatePreferredGenre(userRatingList);
        String strPrefAuthor = calculatePreferredAuthor(userRatingList);

        Map<Book, Double> mapScoreByBook = new HashMap<Book, Double>();  // Scoring map

        for (Book book : bookDatabase.getAllBooks()) 
        {
            if (hasRatedBook(userRatingList, book.getStrId()))  // Skip already rated
            {
                continue;
            }

            double dblScore = calculateMatchScore(book, strPrefGenre, strPrefAuthor);
            mapScoreByBook.put(book, dblScore);
        }

        return sortBooksByScore(mapScoreByBook, intCount);
    }


    /**
     * Adds or updates a user's rating and persists it
     * @param strUserId the user's unique identifier
     * @param strBookId the book's unique identifier
     * @param intRating the new rating (1–5)
     */
    public void addRating(String strUserId, String strBookId, int intRating) 
    {
        ratingList.removeIf(rating -> 
            rating.getStrUserId().equals(strUserId) 
            && rating.getStrBookId().equals(strBookId)
        );  // Remove any existing rating for this user/book

        ratingList.add(new Rating(strUserId, strBookId, intRating));  // Add new rating
        FileUtils.saveRatingsToCSV(new ArrayList<Rating>(ratingList));  // Persist to CSV

        updateBookRating(strBookId);  // Recalculate book's average rating
    }


    /**
     * Adjusts recommendation weights up or down by a fixed delta
     * @param isPositive - true to increase genre/author weights; false to decrease
     */
    public void adjustWeights(boolean isPositive) 
    {
        double dblDelta = isPositive ? 0.05 : -0.05;  // Direction of adjustment
        dblGenreWeight  = clamp(dblGenreWeight + dblDelta,  0.1, 0.6);  // Clamp bounds
        dblAuthorWeight = clamp(dblAuthorWeight + dblDelta, 0.1, 0.5);

        double dblTotal = dblGenreWeight + dblAuthorWeight + dblRatingWeight + dblPopularityWeight;
        dblRatingWeight     = (dblRatingWeight / dblTotal)     * (1 - dblGenreWeight - dblAuthorWeight);
        dblPopularityWeight = 1 - dblGenreWeight - dblAuthorWeight - dblRatingWeight;

        System.out.println(String.format(
            "Weights adjusted → Genre: %.2f, Author: %.2f, Rating: %.2f, Popularity: %.2f",
            dblGenreWeight, dblAuthorWeight, dblRatingWeight, dblPopularityWeight
        ));
    }


    /**
     * Calculates the match score for a book based on multiple factors
     * @param book the Book to score
     * @param strPrefGenre - the user's preferred genre
     * @param strPrefAuthor - the user's preferred author
     * @return - combined match score (higher is better)
     */
    private double calculateMatchScore(Book book, String strPrefGenre, String strPrefAuthor) 
    {
        double dblScore = 0.0;  // Accumulator

        if (book.getStrGenre().equals(strPrefGenre))    // Genre match
        {
            dblScore += dblGenreWeight;
        }

        if (book.getStrAuthor().equals(strPrefAuthor))  // Author match
        {
            dblScore += dblAuthorWeight;
        }

        dblScore += (book.getDblAvgRating() / 5.0) * dblRatingWeight;  // Normalized rating

        int intMaxBorrow = bookDatabase.getAllBooks()
                                       .stream()
                                       .mapToInt(Book::getIntBorrowCount)
                                       .max()
                                       .orElse(1);  // Avoid division by zero

        dblScore += ((double) book.getIntBorrowCount() / intMaxBorrow) * dblPopularityWeight;  // Popularity

        return dblScore;
    }


    /**
     * Determines the user's most frequently high-rated genre
     * @param listRatings the user's past ratings
     * @return genre string with highest count; "Unknown" if none
     */
    private String calculatePreferredGenre(List<Rating> listRatings) 
    {
        Map<String, Integer> mapCount = new HashMap<String, Integer>();  // Genre → count

        for (Rating rating : listRatings) 
        {
            if (rating.getIntRating() < 4)  // Only consider positive ratings
            {
                continue;
            }

            Book book = bookDatabase.findBookById(rating.getStrBookId());

            if (book != null) 
            {
                mapCount.merge(book.getStrGenre(), 1, Integer::sum);  // Increment
            }
        }

        return maxKey(mapCount);
    }


    /**
     * Determines the user's most frequently high-rated author
     * @param listRatings - the user's past ratings
     * @return - author string with highest count; "Unknown" if none
     */
    private String calculatePreferredAuthor(List<Rating> listRatings) 
    {
        Map<String, Integer> mapCount = new HashMap<String, Integer>();  // Author → count

        for (Rating rating : listRatings) 
        {
            if (rating.getIntRating() < 4)  // Only consider positive ratings
            {
                continue;
            }

            Book book = bookDatabase.findBookById(rating.getStrBookId());

            if (book != null) 
            {
                mapCount.merge(book.getStrAuthor(), 1, Integer::sum);  // Increment
            }
        }

        return maxKey(mapCount);
    }


    /**
     * Filters the complete rating list for a specific user
     * @param strUserId - the user's unique identifier
     * @return - list of this user's ratings
     */
    private List<Rating> getUserRatings(String strUserId) 
    {
        List<Rating> listResult = new ArrayList<Rating>();  // User's ratings

        for (Rating rating : ratingList) 
        {
            if (rating.getStrUserId().equals(strUserId)) 
            {
                listResult.add(rating);
            }
        }

        return listResult;
    }


    /**
     * Checks whether the user already rated a given book
     * @param - listRatings the user's ratings
     * @param - strBookId the book's unique identifier
     * @return - true if found; false otherwise
     */
    private boolean hasRatedBook(List<Rating> listRatings, String strBookId) 
    {
        for (Rating rating : listRatings) 
        {
            if (rating.getStrBookId().equals(strBookId))  // Match found
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Sorts books by score descending and returns top N
     * @param mapScoreByBook - map of Book to score
     * @param intCount - max number of books to return
     * @return - list of top-scoring books
     */
    private List<Book> sortBooksByScore(Map<Book, Double> mapScoreByBook, int intCount) 
    {
        List<Map.Entry<Book, Double>> listEntries = 
            new ArrayList<Map.Entry<Book, Double>>(mapScoreByBook.entrySet());

        Collections.sort(listEntries, new Comparator<Map.Entry<Book, Double>>() 
        {
            @Override
            public int compare(Map.Entry<Book, Double> e1, Map.Entry<Book, Double> e2) 
            {
                return Double.compare(e2.getValue(), e1.getValue());  // Descending
            }
        });

        List<Book> listTop = new ArrayList<Book>();  // Top results

        for (int intI = 0; intI < Math.min(intCount, listEntries.size()); intI++) 
        {
            listTop.add(listEntries.get(intI).getKey());
        }

        return listTop;
    }


    /**
     * Retrieves the key with the highest integer value in the map
     * @param mapCount - the map of keys to counts
     * @return - key string with max count; "Unknown" if map empty
     */
    private String maxKey(Map<String, Integer> mapCount) 
    {
        return mapCount.entrySet()
                       .stream()
                       .max(Map.Entry.comparingByValue())
                       .map(Map.Entry::getKey)
                       .orElse("Unknown");
    }


    /**
     * Clamps a value within the inclusive range [min, max]
     * @param v - the value to clamp
     * @param min - the lower bound
     * @param max - the upper bound
     * @return - clamped value
     */
    private double clamp(double v, double min, double max) 
    {
        if (v < min)  // Below lower bound
        {
            return min;
        }
        else if (v > max)  // Above upper bound
        {
            return max;
        }

        return v;  // Within range
    }


    /**
     * Updates a book's average rating based on all stored ratings
     * @param strBookId - the book's unique identifier
     */
    private void updateBookRating(String strBookId) 
    {
        int intTotal = 0;  // Sum of ratings
        int intCount = 0;  // Number of ratings

        for (Rating rating : ratingList) 
        {
            if (rating.getStrBookId().equals(strBookId)) 
            {
                intTotal += rating.getIntRating();  // Accumulate
                intCount++;
            }
        }

        if (intCount > 0)  // Avoid division by zero
        {
            Book book = bookDatabase.findBookById(strBookId);

            if (book != null) 
            {
                double dblAvg = (double) intTotal / intCount;  // Compute average
                book.setDblAvgRating(Math.round(dblAvg * 10) / 10.0);  // Round one decimal
                bookDatabase.saveBooks();  // Persist updated book data
            }
        }
    }
}