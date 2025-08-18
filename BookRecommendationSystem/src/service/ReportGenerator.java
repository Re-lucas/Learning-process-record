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
    private BookDatabase bookDatabase;  
    private UserManager userManager;    

    public ReportGenerator(BookDatabase bookDatabase, UserManager userManager) 
    {
        this.bookDatabase = bookDatabase;
        this.userManager  = userManager;
    }


    public String generatePopularBooksReport(int intTopCount) 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== Report on Popular Books =====\n");
        sb.append("Statistical time: ").append(new Date()).append("\n\n");

        ArrayList<Book> popularBooks = bookDatabase.getPopularBooks(intTopCount);

        sb.append(String.format("%-6s %-25s %-15s %-8s %-8s %-12s\n",
            "Ranking", "Title of Book",    "Author",  "Score", "Borrowing volume", "Status"));
        sb.append("------------------------------------------------------------\n");

        for (int i = 0; i < popularBooks.size(); i++) 
        {
            Book book = popularBooks.get(i);
            sb.append(String.format("%-6d %-25s %-15s %-8.1f %-8d %-12s\n",
                i + 1,
                truncate(book.getStrTitle(), 24),
                truncate(book.getStrAuthor(), 14),
                book.getDblAvgRating(),
                book.getIntBorrowCount(),
                book.isAvailable() ? "Available for borrowing" : "Lent out"
            ));
        }

        sb.append("\nStatistical summary:\n");
        sb.append("· Total number of books ").append(bookDatabase.getAllBooks().size()).append("\n");
        if (!popularBooks.isEmpty()) {
            Book top = popularBooks.get(0);
            sb.append("· The most popular books: ")
              .append(top.getStrTitle())
              .append(" (Borrowing volume: ")
              .append(top.getIntBorrowCount())
              .append(")\n");
        }

        return sb.toString();
    }


    public String generateAuthorPopularityReport() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== Analysis of author popularity =====\n");
        sb.append("Statistical time: ").append(new Date()).append("\n\n");

        HashMap<String, AuthorStats> statsMap = new HashMap<>();
        for (Book book : bookDatabase.getAllBooks()) 
        {
            String author = book.getStrAuthor();
            AuthorStats st = statsMap.getOrDefault(author, new AuthorStats());
            st.intBookCount++;
            st.intTotalBorrowCount += book.getIntBorrowCount();
            st.dblTotalRating     += book.getDblAvgRating();
            statsMap.put(author, st);
        }

        ArrayList<Map.Entry<String, AuthorStats>> list =
            new ArrayList<>(statsMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, AuthorStats>>() {
            @Override
            public int compare(Map.Entry<String, AuthorStats> e1,
                               Map.Entry<String, AuthorStats> e2) {
                return e2.getValue().intTotalBorrowCount
                     - e1.getValue().intTotalBorrowCount;
            }
        });

        sb.append(String.format("%-20s %-10s %-12s %-10s\n",
            "Author", "Number of works", "Total borrowing volume", "Average score"));
        sb.append("------------------------------------------------\n");

        for (Map.Entry<String, AuthorStats> entry : list) 
        {
            AuthorStats st = entry.getValue();
            double avg = st.dblTotalRating / st.intBookCount;
            sb.append(String.format("%-20s %-10d %-12d %-10.1f\n",
                truncate(entry.getKey(), 19),
                st.intBookCount,
                st.intTotalBorrowCount,
                avg
            ));
        }

        return sb.toString();
    }


    public String generateGenreUsageReport() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== Book classification statistics =====\n");
        sb.append("Statistical time: ").append(new Date()).append("\n\n");

        HashMap<String, GenreStats> genreMap = new HashMap<>();
        for (Book book : bookDatabase.getAllBooks()) 
        {
            String genre = book.getStrGenre();
            GenreStats st = genreMap.getOrDefault(genre, new GenreStats());
            st.intBookCount++;
            st.intTotalBorrowCount += book.getIntBorrowCount();
            genreMap.put(genre, st);
        }

        ArrayList<Map.Entry<String, GenreStats>> list =
            new ArrayList<>(genreMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, GenreStats>>() {
            @Override
            public int compare(Map.Entry<String, GenreStats> e1,
                               Map.Entry<String, GenreStats> e2) {
                return e2.getValue().intBookCount - e1.getValue().intBookCount;
            }
        });

        sb.append(String.format("%-15s %-10s %-12s %-15s\n",
            "Classification", "Quantity of books", "Total borrowing volume", "Average borrowing rate"));
        sb.append("------------------------------------------------\n");

        int totalBooks = bookDatabase.getAllBooks().size();
        for (Map.Entry<String, GenreStats> entry : list) 
        {
            GenreStats st = entry.getValue();
            double avgRate = (double) st.intTotalBorrowCount / st.intBookCount;
            sb.append(String.format("%-15s %-10d %-12d %-15.1f\n",
                truncate(entry.getKey(), 14),
                st.intBookCount,
                st.intTotalBorrowCount,
                avgRate
            ));
        }
        
        sb.append("\nClassification proportion:\n");
        for (Map.Entry<String, GenreStats> entry : list) 
        {
            GenreStats st = entry.getValue();
            double pct = (double) st.intBookCount / totalBooks * 100;
            int bars = (int)(pct / 2);
            sb.append(String.format("%-15s: %s %.1f%%\n",
                truncate(entry.getKey(), 14),
                repeat("■", bars),
                pct
            ));
        }

        return sb.toString();
    }


    public String generateUserActivityReport() 
    {
        return "\n===== User Activity report =====\nThe function is yet to be realized\n";
    }


    private String truncate(String s, int maxLen) 
    {
        if (s == null || s.length() <= maxLen) return s == null ? "" : s;
        return s.substring(0, maxLen - 1) + "…";
    }

    private String repeat(String str, int count) 
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(str);
        return sb.toString();
    }

    private static class AuthorStats 
    {
        int intBookCount       = 0;
        int intTotalBorrowCount= 0;
        double dblTotalRating  = 0.0;
    }


    private static class GenreStats 
    {
        int intBookCount        = 0;
        int intTotalBorrowCount = 0;
    }
}
