package service;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import model.Book;

/**
 * File: ReportGenerator.java
 * Author: [团队名称]
 * Date: [当前日期]
 *
 * Description: 管理员报告生成器，提供系统统计和分析功能
 */
public class ReportGenerator 
{
    private BookDatabase bookDatabase;  
    private UserManager userManager;    

    public ReportGenerator(BookDatabase bookDatabase, UserManager userManager) 
    {
        this.bookDatabase = bookDatabase;
        this.userManager  = userManager;
    }

    /**
     * 生成热门书籍报告
     */
    public String generatePopularBooksReport(int intTopCount) 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== 热门书籍报告 =====\n");
        sb.append("统计时间: ").append(new Date()).append("\n\n");

        ArrayList<Book> popularBooks = bookDatabase.getPopularBooks(intTopCount);

        sb.append(String.format("%-6s %-25s %-15s %-8s %-8s %-12s\n",
            "排名", "书名",    "作者",  "评分", "借阅量", "状态"));
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
                book.isAvailable() ? "可借阅" : "已借出"
            ));
        }

        sb.append("\n统计摘要:\n");
        sb.append("· 总图书数量: ").append(bookDatabase.getAllBooks().size()).append("\n");
        if (!popularBooks.isEmpty()) {
            Book top = popularBooks.get(0);
            sb.append("· 最热门书籍: ")
              .append(top.getStrTitle())
              .append(" (借阅量: ")
              .append(top.getIntBorrowCount())
              .append(")\n");
        }

        return sb.toString();
    }

    /**
     * 生成作者受欢迎度分析
     */
    public String generateAuthorPopularityReport() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== 作者受欢迎度分析 =====\n");
        sb.append("统计时间: ").append(new Date()).append("\n\n");

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
            "作者", "作品数量", "总借阅量", "平均评分"));
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

    /**
     * 生成分类使用统计报告
     */
    public String generateGenreUsageReport() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== 图书分类统计 =====\n");
        sb.append("统计时间: ").append(new Date()).append("\n\n");

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
            "分类", "图书数量", "总借阅量", "平均借阅率"));
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

        sb.append("\n分类占比:\n");
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

    /**
     * 生成用户活跃度报告（简化版）
     */
    public String generateUserActivityReport() 
    {
        return "\n===== 用户活跃度报告 =====\n功能待实现\n";
    }

    // ===== 辅助方法及内部类 =====

    /** 截断字符串，多余部分用省略号 */
    private String truncate(String s, int maxLen) 
    {
        if (s == null || s.length() <= maxLen) return s == null ? "" : s;
        return s.substring(0, maxLen - 1) + "…";
    }

    /** 重复拼接字符串 count 次 */
    private String repeat(String str, int count) 
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(str);
        return sb.toString();
    }

    /** 作者统计结构 */
    private static class AuthorStats 
    {
        int intBookCount       = 0;
        int intTotalBorrowCount= 0;
        double dblTotalRating  = 0.0;
    }

    /** 类型统计结构 */
    private static class GenreStats 
    {
        int intBookCount        = 0;
        int intTotalBorrowCount = 0;
    }
}
