/**
 * File: SearchService.java
 * Author: Lucas Wu
 * Date: 2025-08-18
 *
 * Description:
 *  - Builds a dictionary of searchable terms from book titles and authors
 *  - Provides basic full-text search by title or author
 *  - Implements spelling correction using edit distance and dictionary lookup
 *  - Supports prefix-based suggestion generation for autocomplete
**/
package service;

import java.util.ArrayList;
import model.Book;

public class SearchService 
{
    private BookDatabase bookDatabase;         // Data source for books
    private ArrayList<String> strDictionaryList; // List of normalized search terms


    /**
     * Constructs the SearchService and initializes the search dictionary
     * @param bookDatabase - the BookDatabase providing book records
     */
    public SearchService(BookDatabase bookDatabase) 
    {
        this.bookDatabase       = bookDatabase;
        this.strDictionaryList  = new ArrayList<String>();  // Initialize term list
        buildDictionary();
    }


    /**
     * Builds the dictionary from all book titles and authors
     * Skips rebuilding if dictionary is already populated
     */
    private void buildDictionary() 
    {
        if (!strDictionaryList.isEmpty())  // Dictionary already built
        {
            return;
        }

        for (Book book : bookDatabase.getAllBooks()) 
        {
            addToDictionary(book.getStrTitle());
            addToDictionary(book.getStrAuthor());
        }

        System.out.println("The search dictionary has been constructed: " 
            + strDictionaryList.size() + " entries");
    }


    /**
     * Splits a term into words, normalizes them, and adds unique entries
     * @param strTerm - the raw title or author string
     */
    private void addToDictionary(String strTerm) 
    {
        if (strTerm == null)  // Nothing to add
        {
            return;
        }

        String[] strWordArr = strTerm.split("\\s+");

        for (String strWord : strWordArr) 
        {
            String strClean = strWord
                .toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "");

            if (!strClean.isEmpty() && !strDictionaryList.contains(strClean))  // Unique term
            {
                strDictionaryList.add(strClean);
            }
        }
    }


    /**
     * Performs a basic search for books whose title or author contains the query
     * @param strQuery - the user's search keyword
     * @return - list of books matching the query
     */
    public ArrayList<Book> searchBooks(String strQuery) 
    {
        ArrayList<Book> bookListResult = new ArrayList<Book>();  // Search results

        if (strQuery == null)  // No query provided
        {
            return bookListResult;
        }

        String strLowerQuery = strQuery.toLowerCase();  // Normalize case

        for (Book book : bookDatabase.getAllBooks()) 
        {
            if (book.getStrTitle().toLowerCase().contains(strLowerQuery)  // Title match
                || book.getStrAuthor().toLowerCase().contains(strLowerQuery))  // Author match
            {
                bookListResult.add(book);
            }
        }

        return bookListResult;
    }


    /**
     * Attempts to correct spelling in the query, then performs search
     * @param strQuery - the raw user input
     * @return - list of books matching either the corrected or original query
     */
    public ArrayList<Book> smartSearch(String strQuery) 
    {
        if (strQuery == null)  // No input
        {
            return new ArrayList<Book>();
        }

        String strCorrected = correctSpelling(strQuery);
        String strFinalQuery = (strCorrected != null) ? strCorrected : strQuery;

        System.out.println("Search for the final keywords: " + strFinalQuery);

        return searchBooks(strFinalQuery);
    }


    /**
     * Corrects each word in the query against the dictionary using edit distance
     * @param strQuery - the raw input to correct
     * @return - corrected query if any corrections made; null otherwise
     */
    public String correctSpelling(String strQuery) 
    {
        String[] strWordArr = strQuery.split("\\s+");
        StringBuilder sbCorrected = new StringBuilder();
        boolean isCorrected = false;  // Flag if any correction applied

        for (String strWord : strWordArr) 
        {
            String strClean = strWord
                .toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "");

            if (!strClean.isEmpty() && !strDictionaryList.contains(strClean))  // Unknown term
            {
                String strSuggestion = findClosestWord(strClean);

                if (strSuggestion != null)  // Found a close match
                {
                    sbCorrected.append(strSuggestion).append(" ");
                    isCorrected = true;
                    continue;
                }
            }

            sbCorrected.append(strWord).append(" ");
        }

        return isCorrected ? sbCorrected.toString().trim() : null;
    }


    /**
     * Finds the closest dictionary term to the given word within edit distance ≤ 2
     * @param strWord  - the cleaned input token
     * @return - the best matching term or null if none within threshold
     */
    private String findClosestWord(String strWord) 
    {
        if (strWord.length() <= 2)  // Too short to correct
        {
            return null;
        }

        String strBest = null;       // Best candidate
        int    intMinDist = Integer.MAX_VALUE;  // Smallest distance found

        for (String strCandidate : strDictionaryList) 
        {
            int intDist = calculateEditDistance(strWord, strCandidate);

            if (intDist < intMinDist && intDist <= 2)  // Within allowed bounds
            {
                intMinDist = intDist;
                strBest    = strCandidate;
            }
        }

        return strBest;
    }


    /**
     * Computes the Levenshtein edit distance between two strings
     * @param strS1 - first string
     * @param strS2 - second string
     * @return - the edit distance (number of insertions, deletions, substitutions)
     */
    private int calculateEditDistance(String strS1, String strS2) 
    {
        int intLen1 = strS1.length();  // Length of first string
        int intLen2 = strS2.length();  // Length of second string
        int[][] intArrDp = new int[intLen1 + 1][intLen2 + 1];  // DP table

        for (int intI = 0; intI <= intLen1; intI++)  
        {
            intArrDp[intI][0] = intI;  // Deletion cost
        }

        for (int intJ = 0; intJ <= intLen2; intJ++)  
        {
            intArrDp[0][intJ] = intJ;  // Insertion cost
        }

        for (int intI = 1; intI <= intLen1; intI++) 
        {
            for (int intJ = 1; intJ <= intLen2; intJ++) 
            {
                int intCost = (strS1.charAt(intI - 1) == strS2.charAt(intJ - 1)) ? 0 : 1;  // Substitution cost

                intArrDp[intI][intJ] = Math.min(
                    Math.min(intArrDp[intI - 1][intJ] + 1,    // Deletion
                    intArrDp[intI][intJ - 1] + 1),           // Insertion
                    intArrDp[intI - 1][intJ - 1] + intCost   // Substitution
                );
            }
        }

        return intArrDp[intLen1][intLen2];
    }


    /**
     * Generates up to 5 autocomplete suggestions matching the input prefix
     * @param strPartial - the user's partial input
     * @return - list of suggestion terms
     */
    public ArrayList<String> generateSuggestions(String strPartial) 
    {
        ArrayList<String> strSuggestionList = new ArrayList<String>();  // Suggestions

        if (strPartial == null)  // No prefix provided
        {
            return strSuggestionList;
        }

        String strLower = strPartial.toLowerCase();  // Normalize

        for (String strCandidate : strDictionaryList) 
        {
            if (strCandidate.startsWith(strLower))  // Prefix match
            {
                strSuggestionList.add(strCandidate);

                if (strSuggestionList.size() >= 5)  // Limit suggestions
                {
                    break;
                }
            }
        }

        return strSuggestionList;
    }
}