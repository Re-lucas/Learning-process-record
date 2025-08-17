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

/**
 * File: RecommendationEngine.java
 * Author: [团队名称]
 * Date: [当前日期]
 *
 * Description: 图书推荐引擎，实现匹配度计算和动态权重调整
 */
public class RecommendationEngine 
{
    private BookDatabase bookDatabase;
    private List<Rating> ratingList;

    // 权重配置（默认值）
    private double dblGenreWeight      = 0.4;
    private double dblAuthorWeight     = 0.3;
    private double dblRatingWeight     = 0.2;
    private double dblPopularityWeight = 0.1;

    public RecommendationEngine(BookDatabase bookDatabase) 
    {
        this.bookDatabase = bookDatabase;
        // 从 CSV 加载评分数据
        this.ratingList   = FileUtils.loadRatingsFromCSV();
        System.out.println("Loaded " + ratingList.size() + " ratings");
    }

    /**
     * 为用户生成个性化推荐
     */
    public List<Book> generateRecommendations(String strUserId, int intCount) 
    {
        List<Rating> userRatings = getUserRatings(strUserId);
        if (userRatings.isEmpty()) {
            return bookDatabase.getPopularBooks(intCount);
        }

        String prefGenre  = calculatePreferredGenre(userRatings);
        String prefAuthor = calculatePreferredAuthor(userRatings);

        // 计算所有未评分图书的匹配分数
        Map<Book, Double> scoreMap = new HashMap<>();
        for (Book book : bookDatabase.getAllBooks()) 
        {
            if (hasRatedBook(userRatings, book.getStrId())) continue;
            double score = calculateMatchScore(book, prefGenre, prefAuthor);
            scoreMap.put(book, score);
        }

        return sortBooksByScore(scoreMap, intCount);
    }

    /**
     * 添加或更新用户评分
     */
    public void addRating(String strUserId, String strBookId, int intRating) 
    {
        // 移除旧评分
        ratingList.removeIf(r ->
            r.getStrUserId().equals(strUserId) &&
            r.getStrBookId().equals(strBookId)
        );

        // 添加新评分
        ratingList.add(new Rating(strUserId, strBookId, intRating));
        FileUtils.saveRatingsToCSV(new ArrayList<>(ratingList));

        // 更新该书的平均评分并持久化
        updateBookRating(strBookId);
    }

    /**
     * 动态调整权重（基于用户正/负反馈）
     */
    public void adjustWeights(boolean isPositive) 
    {
        double delta = isPositive ? 0.05 : -0.05;
        dblGenreWeight      = clamp(dblGenreWeight + delta,  0.1, 0.6);
        dblAuthorWeight     = clamp(dblAuthorWeight + delta, 0.1, 0.5);

        // 重新归一化剩余权重
        double total = dblGenreWeight + dblAuthorWeight + dblRatingWeight + dblPopularityWeight;
        dblRatingWeight     = (dblRatingWeight / total)     * (1 - dblGenreWeight - dblAuthorWeight);
        dblPopularityWeight = 1 - dblGenreWeight - dblAuthorWeight - dblRatingWeight;

        System.out.println(String.format(
            "Weights adjusted → Genre: %.2f, Author: %.2f, Rating: %.2f, Popularity: %.2f",
            dblGenreWeight, dblAuthorWeight, dblRatingWeight, dblPopularityWeight
        ));
    }

    // ===== 私有辅助方法 =====

    private double calculateMatchScore(Book book, String genre, String author) 
    {
        double score = 0.0;
        if (book.getStrGenre().equals(genre))   score += dblGenreWeight;
        if (book.getStrAuthor().equals(author)) score += dblAuthorWeight;

        score += (book.getDblAvgRating() / 5.0) * dblRatingWeight;

        int maxBorrow = bookDatabase.getAllBooks()
                                    .stream()
                                    .mapToInt(Book::getIntBorrowCount)
                                    .max()
                                    .orElse(1);
        score += ((double) book.getIntBorrowCount() / maxBorrow) * dblPopularityWeight;

        return score;
    }

    private String calculatePreferredGenre(List<Rating> userRatings) 
    {
        Map<String, Integer> count = new HashMap<>();
        for (Rating r : userRatings) 
        {
            if (r.getIntRating() < 4) continue;
            Book b = bookDatabase.findBookById(r.getStrBookId());
            if (b != null) {
                count.merge(b.getStrGenre(), 1, Integer::sum);
            }
        }
        return maxKey(count);
    }

    private String calculatePreferredAuthor(List<Rating> userRatings) 
    {
        Map<String, Integer> count = new HashMap<>();
        for (Rating r : userRatings) 
        {
            if (r.getIntRating() < 4) continue;
            Book b = bookDatabase.findBookById(r.getStrBookId());
            if (b != null) {
                count.merge(b.getStrAuthor(), 1, Integer::sum);
            }
        }
        return maxKey(count);
    }

    private List<Rating> getUserRatings(String userId) 
    {
        List<Rating> list = new ArrayList<>();
        for (Rating r : ratingList) {
            if (r.getStrUserId().equals(userId)) {
                list.add(r);
            }
        }
        return list;
    }

    private boolean hasRatedBook(List<Rating> ratings, String bookId) 
    {
        for (Rating r : ratings) {
            if (r.getStrBookId().equals(bookId)) {
                return true;
            }
        }
        return false;
    }

    private List<Book> sortBooksByScore(Map<Book, Double> map, int count) 
    {
        List<Map.Entry<Book, Double>> entries = new ArrayList<>(map.entrySet());
        entries.sort(Map.Entry.<Book, Double>comparingByValue(Comparator.reverseOrder()));

        List<Book> top = new ArrayList<>();
        for (int i = 0; i < Math.min(count, entries.size()); i++) {
            top.add(entries.get(i).getKey());
        }
        return top;
    }

    private String maxKey(Map<String, Integer> map) 
    {
        return map.entrySet()
                  .stream()
                  .max(Map.Entry.comparingByValue())
                  .map(Map.Entry::getKey)
                  .orElse("未知");
    }

    private double clamp(double v, double min, double max) 
    {
        return v < min ? min : (v > max ? max : v);
    }

    private void updateBookRating(String bookId) 
    {
        int total = 0, cnt = 0;
        for (Rating r : ratingList) 
        {
            if (r.getStrBookId().equals(bookId)) {
                total += r.getIntRating();
                cnt++;
            }
        }
        if (cnt > 0) 
        {
            Book book = bookDatabase.findBookById(bookId);
            if (book != null) 
            {
                double avg = (double) total / cnt;
                book.setDblAvgRating(Math.round(avg * 10) / 10.0);
                bookDatabase.saveBooks();
            }
        }
    }
}
