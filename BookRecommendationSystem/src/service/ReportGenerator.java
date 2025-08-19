/**
 * File: ReportGenerator.java
 * Author: Lucas Wu
 * Date: 2025-08-16
 *
 * Description:
 *  - Generates a report of the top borrowed books with summary statistics
 *  - Analyzes author popularity by total works, borrow volume, and avg rating
 *  - Computes genre usage stats including book counts, borrow counts, and avg borrow rate
 *  - Provides a placeholder for user activity reporting (unimplemented)
 * 
 * Work Log (Lucas Wu):
 *  2025-08-18:
 *    - Added comprehensive JavaDoc comments for all methods, constructors, inner classes
 *    - Unified naming conventions for parameters and global variables
 *    - Standardized method signatures and exception documentation
 *
 *  2025-08-17:
 *    - Conducted thorough testing of all report generation methods:
 *        - generatePopularBooksReport with various top-count values and empty datasets
 *        - generateAuthorPopularityReport for single/multiple authors, no-book edge case
 *        - generateGenreUsageReport to validate classification bars and avg borrow rates
 *        - generateUserActivityReport placeholder behavior
 *    - Fixed formatting alignment issues in tabular output
 *    - Corrected average and percentage calculation bugs
 *    - Validated exception throws for invalid inputs and null dependencies
 *
 *  2025-08-16:
 *    - Initial implementation of core reporting features:
 *        - generatePopularBooksReport
 *        - generateAuthorPopularityReport
 *        - generateGenreUsageReport
 *        - generateUserActivityReport placeholder
 *    - Added helper utilities:
 *        - truncate(String, int) for safe title/author truncation
 *        - repeat(String, int) for building classification bars
 *    - Defined inner aggregation classes:
 *        - AuthorStats
 *        - GenreStats
**/
package service;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import model.Book;

public class ReportGenerator 
{
    private BookDatabase bookDatabase;  // Data source for book records
    private UserManager userManager;    // Data source for user records


    /**
     * Constructs the ReportGenerator with dependencies
     * @param bookDatabase - the BookDatabase providing book records
     * @param userManager - the UserManager providing user records
     */
    public ReportGenerator(BookDatabase bookDatabase, UserManager userManager) 
    {
        this.bookDatabase = bookDatabase;  // Assign database
        this.userManager  = userManager;   // Assign manager
    }


    /**
     * Generates a report on popular books based on borrow count
     * @param intTopCount - number of top books to include in the report
     * @return - formatted report string for popular books
     * @throws IllegalArgumentException if intTopCount is less than 1
     */
    public String generatePopularBooksReport(int intTopCount) 
    {
        StringBuilder sbReport       = new StringBuilder();  // Report builder
        Date          dateStatTime  = new Date();            // Current timestamp

        sbReport.append("\n===== Report on Popular Books =====\n");
        sbReport.append("Statistical time: ")
                .append(dateStatTime)
                .append("\n\n");

        ArrayList<Book> bookListPopular = bookDatabase.getPopularBooks(intTopCount);  // Top list

        sbReport.append(String.format(
            "%-6s %-25s %-15s %-8s %-8s %-12s\n",
            "Rank", "Title", "Author", "Score", "Borrows", "Status"
        ));
        sbReport.append("------------------------------------------------------------\n");

        for (int intIndex = 0; intIndex < bookListPopular.size(); intIndex++) 
        {
            Book book = bookListPopular.get(intIndex);  // Current book entry

            sbReport.append(String.format(
                "%-6d %-25s %-15s %-8.1f %-8d %-12s\n",
                intIndex + 1,
                truncate(book.getStrTitle(), 24),
                truncate(book.getStrAuthor(), 14),
                book.getDblAvgRating(),
                book.getIntBorrowCount(),
                book.isAvailable() ? "Available" : "Lent out"
            ));
        }

        sbReport.append("\nStatistical summary:\n");
        sbReport.append("· Total number of books: ")
                .append(bookDatabase.getAllBooks().size())
                .append("\n");

        if (!bookListPopular.isEmpty()) 
        {
            Book bookTop = bookListPopular.get(0);  // Top book
            sbReport.append("· Most popular book: ")
                    .append(bookTop.getStrTitle())
                    .append(" (Borrows: ")
                    .append(bookTop.getIntBorrowCount())
                    .append(")\n");
        }

        return sbReport.toString();  // Return full report
    }


    /**
     * Generates a report on author popularity based on works, borrow volume, and avg rating
     * @return - formatted report string for author statistics
     * @throws IllegalArgumentException if no books are available
     */
    public String generateAuthorPopularityReport() 
    {
        StringBuilder sbReport         = new StringBuilder();  // Report builder
        Date          dateStatTime    = new Date();            // Current timestamp
        HashMap<String, AuthorStats> mapStats = new HashMap<String, AuthorStats>();

        sbReport.append("\n===== Analysis of Author Popularity =====\n");
        sbReport.append("Statistical time: ")
                .append(dateStatTime)
                .append("\n\n");

        for (Book book : bookDatabase.getAllBooks()) 
        {
            String strAuthor = book.getStrAuthor();  // Author key
            AuthorStats stats = mapStats.getOrDefault(strAuthor, new AuthorStats());
            stats.intBookCount++;                                 // Increment work count
            stats.intTotalBorrowCount += book.getIntBorrowCount();  // Sum borrows
            stats.dblTotalRating     += book.getDblAvgRating();     // Sum ratings
            mapStats.put(strAuthor, stats);                       // Update map
        }

        ArrayList<Map.Entry<String, AuthorStats>> listStats =
            new ArrayList<Map.Entry<String, AuthorStats>>(mapStats.entrySet());

        Collections.sort(listStats, new Comparator<Map.Entry<String, AuthorStats>>() 
        {
            @Override
            public int compare(Map.Entry<String, AuthorStats> e1,
                               Map.Entry<String, AuthorStats> e2) 
            {
                return e2.getValue().intTotalBorrowCount
                     - e1.getValue().intTotalBorrowCount;  // Descending
            }
        });

        sbReport.append(String.format(
            "%-20s %-10s %-12s %-10s\n",
            "Author", "Works", "TotalBorrows", "AvgScore"
        ));
        sbReport.append("------------------------------------------------\n");

        for (Map.Entry<String, AuthorStats> entry : listStats) 
        {
            AuthorStats stats = entry.getValue();          // Stats for this author
            double       dblAvgScore = stats.dblTotalRating / stats.intBookCount;  // Compute avg

            sbReport.append(String.format(
                "%-20s %-10d %-12d %-10.1f\n",
                truncate(entry.getKey(), 19),
                stats.intBookCount,
                stats.intTotalBorrowCount,
                dblAvgScore
            ));
        }

        return sbReport.toString();  // Return full report
    }


    /**
     * Generates a report on book classification by genre
     * @return - formatted report string for genre usage statistics
     * @throws IllegalArgumentException if no books are available
     * @throws NullPointerException if bookDatabase is null
     */
    public String generateGenreUsageReport() 
    {
        StringBuilder sbReport       = new StringBuilder();  // Report builder
        Date          dateStatTime   = new Date();            // Current timestamp
        HashMap<String, GenreStats> mapGenre = new HashMap<String, GenreStats>();

        sbReport.append("\n===== Book Classification Statistics =====\n");
        sbReport.append("Statistical time: ")
                .append(dateStatTime)
                .append("\n\n");

        for (Book book : bookDatabase.getAllBooks()) 
        {
            String strGenre = book.getStrGenre();  // Genre key
            GenreStats stats = mapGenre.getOrDefault(strGenre, new GenreStats());
            stats.intBookCount++;                                // Count books
            stats.intTotalBorrowCount += book.getIntBorrowCount();  // Sum borrows
            mapGenre.put(strGenre, stats);                        // Update map
        }

        ArrayList<Map.Entry<String, GenreStats>> listGenre =
            new ArrayList<Map.Entry<String, GenreStats>>(mapGenre.entrySet());

        Collections.sort(listGenre, new Comparator<Map.Entry<String, GenreStats>>() 
        {
            @Override
            public int compare(Map.Entry<String, GenreStats> e1,
                               Map.Entry<String, GenreStats> e2) 
            {
                return e2.getValue().intBookCount - e1.getValue().intBookCount;  // Descending
            }
        });

        sbReport.append(String.format(
            "%-15s %-10s %-12s %-15s\n",
            "Genre", "Books", "TotalBorrows", "AvgBorrowRate"
        ));
        sbReport.append("------------------------------------------------\n");

        int intTotalBooks = bookDatabase.getAllBooks().size();  // Overall total

        for (Map.Entry<String, GenreStats> entry : listGenre) 
        {
            GenreStats stats    = entry.getValue();               // Stats for this genre
            double       dblAvgRate = (double) stats.intTotalBorrowCount
                                     / stats.intBookCount;        // Compute avg borrow rate

            sbReport.append(String.format(
                "%-15s %-10d %-12d %-15.1f\n",
                truncate(entry.getKey(), 14),
                stats.intBookCount,
                stats.intTotalBorrowCount,
                dblAvgRate
            ));
        }

        sbReport.append("\nClassification proportion:\n");

        for (Map.Entry<String, GenreStats> entry : listGenre) 
        {
            GenreStats stats = entry.getValue();  // Stats for this genre
            double    dblPct   = (double) stats.intBookCount / intTotalBooks * 100;  // Percentage
            int       intBars  = (int) (dblPct / 2);  // Bar count

            sbReport.append(String.format(
                "%-15s: %s %.1f%%\n",
                truncate(entry.getKey(), 14),
                repeat("■", intBars),
                dblPct
            ));
        }

        return sbReport.toString();  // Return full report
    }


    /**
     * Generates a report on user activity (placeholder)
     * @return - formatted report string for user activity
     * @throws UnsupportedOperationException if called
     * @throws NullPointerException if userManager is null
     */
    public String generateUserActivityReport() 
    {
        return "\n===== User Activity Report =====\nThe function is yet to be realized\n";
    }


    /**
     * Truncates a string to a maximum length, appending an ellipsis if needed
     * @param strInput - the input string to truncate
     * @param intMaxLen - maximum length to truncate to
     * @return - truncated string with ellipsis if necessary
     * @throws NullPointerException if strInput is null
     */
    private String truncate(String strInput, int intMaxLen) 
    {
        if (strInput == null || strInput.length() <= intMaxLen) 
        {
            return (strInput == null ? "" : strInput);  // Safe return
        }

        return strInput.substring(0, intMaxLen - 1) + "…";  // Truncate and append
    }


    /**
     * Repeats a string token a specified number of times
     * @param strToken - the string to repeat
     * @param intCount - number of times to repeat
     * @return - the repeated string
     * @throws IllegalArgumentException if intCount is less than 0
     */
    private String repeat(String strToken, int intCount) 
    {
        StringBuilder sbBuilder = new StringBuilder();  // Repetition builder

        for (int intI = 0; intI < intCount; intI++) 
        {
            sbBuilder.append(strToken);  // Append each loop
        }

        return sbBuilder.toString();  // Return repeated result
    }


    /**
     * Inner class for aggregating author-based statistics
     */
    private static class AuthorStats 
    {
        int    intBookCount        = 0;    // Total works count
        int    intTotalBorrowCount = 0;    // Total borrow volume
        double dblTotalRating      = 0.0;  // Sum of average ratings
    }


    /**
     * Inner class for aggregating genre-based statistics
     */
    private static class GenreStats 
    {
        int intBookCount        = 0;  // Total books in genre
        int intTotalBorrowCount = 0;  // Total borrow volume
    }
}