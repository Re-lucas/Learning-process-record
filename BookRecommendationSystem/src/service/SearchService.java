package service;

import java.util.ArrayList;
import model.Book;

/**
 * File: SearchService.java
 * Author: [团队名称]
 * Date: [当前日期]
 *
 * Description: 智能搜索服务，实现模糊搜索和拼写纠正
 */
public class SearchService 
{
    private BookDatabase bookDatabase;
    private ArrayList<String> dictionary;

    public SearchService(BookDatabase bookDatabase) 
    {
        this.bookDatabase = bookDatabase;
        this.dictionary   = new ArrayList<>();
        buildDictionary();
    }

    /**
     * 构建搜索词典，只提取标题和作者中的词
     */
    private void buildDictionary() 
    {
        if (!dictionary.isEmpty()) return;

        for (Book book : bookDatabase.getAllBooks()) 
        {
            addToDictionary(book.getStrTitle());
            addToDictionary(book.getStrAuthor());
        }
        System.out.println("The search dictionary has been constructed: " + dictionary.size() + " entry");
    }

    /**
     * 拆分并清洗词条，加入词典（去重）
     */
    private void addToDictionary(String term) 
    {
        if (term == null) return;
        for (String word : term.split("\\s+")) 
        {
            String clean = word
                .toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "");

            if (!clean.isEmpty() && !dictionary.contains(clean)) 
            {
                dictionary.add(clean);
            }
        }
    }

    /**
     * 基础模糊搜索：按书名或作者字段匹配子串
     */
    public ArrayList<Book> searchBooks(String query) 
    {
        ArrayList<Book> result = new ArrayList<>();
        if (query == null) return result;

        String lower = query.toLowerCase();
        for (Book book : bookDatabase.getAllBooks()) 
        {
            if (book.getStrTitle().toLowerCase().contains(lower) ||
                book.getStrAuthor().toLowerCase().contains(lower)) 
            {
                result.add(book);
            }
        }
        return result;
    }

    /**
     * 智能搜索：先尝试拼写纠正，再执行模糊搜索
     */
    public ArrayList<Book> smartSearch(String query) 
    {
        if (query == null) return new ArrayList<>();

        String corrected = correctSpelling(query);
        String finalQuery = (corrected != null) ? corrected : query;
        System.out.println("Search for the final keywords: " + finalQuery);
        return searchBooks(finalQuery);
    }

    /**
     * 拼写纠正：对每个单词尝试最小编辑距离匹配
     * @return 纠正后的整句，或 null 表示无需纠正
     */
    public String correctSpelling(String query) 
    {
        String[] words = query.split("\\s+");
        StringBuilder sb = new StringBuilder();
        boolean corrected = false;

        for (String word : words) 
        {
            String clean = word
                .toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "");

            if (!clean.isEmpty() && !dictionary.contains(clean)) 
            {
                String suggestion = findClosestWord(clean);
                if (suggestion != null) 
                {
                    sb.append(suggestion).append(" ");
                    corrected = true;
                    continue;
                }
            }
            sb.append(word).append(" ");
        }

        return corrected ? sb.toString().trim() : null;
    }

    /**
     * 在词典中挑选与输入词最接近（编辑距离 ≤2）的词
     */
    private String findClosestWord(String word) 
    {
        if (word.length() <= 2) return null;

        String best = null;
        int minDist = Integer.MAX_VALUE;

        for (String candidate : dictionary) 
        {
            int dist = calculateEditDistance(word, candidate);
            if (dist < minDist && dist <= 2) 
            {
                minDist = dist;
                best    = candidate;
            }
        }
        return best;
    }

    /**
     * 计算 Levenshtein 编辑距离
     */
    private int calculateEditDistance(String s1, String s2) 
    {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) 
        {
            for (int j = 1; j <= m; j++) 
            {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                    Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[n][m];
    }

    /**
     * 根据前缀生成最多 5 条搜索建议
     */
    public ArrayList<String> generateSuggestions(String partial) 
    {
        ArrayList<String> suggestions = new ArrayList<>();
        if (partial == null) return suggestions;

        String lower = partial.toLowerCase();
        for (String w : dictionary) 
        {
            if (w.startsWith(lower)) 
            {
                suggestions.add(w);
                if (suggestions.size() >= 5) break;
            }
        }
        return suggestions;
    }
}
