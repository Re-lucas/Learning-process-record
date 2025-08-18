package service;

import java.util.ArrayList;
import model.Book;

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


    public ArrayList<Book> smartSearch(String query) 
    {
        if (query == null) return new ArrayList<>();

        String corrected = correctSpelling(query);
        String finalQuery = (corrected != null) ? corrected : query;
        System.out.println("Search for the final keywords: " + finalQuery);
        return searchBooks(finalQuery);
    }


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
