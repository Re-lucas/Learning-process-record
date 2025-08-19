/**
 * File: FileUtils.java
 * Author: Lucas Wu
 * Date: 2025-08-16
 * 
 * Description：
 *  - Manages CSV file paths for books, users and ratings
 *  - Validates CSV format and creates timestamped backups
 *  - Loads Book, User, and Rating data from CSV into ArrayList
 *  - Saves Book, User, and Rating lists back to CSV (books include backup)
 * 
 * Work Log (Lucas Wu):
 *  2025-08-18:
 *    - Added comprehensive JavaDoc comments for all methods and classes
 *    - Enhanced documentation of CSV validation and backup processes
 *    - Standardized method signatures and parameter descriptions
 *    - Improved error handling messages for better user guidance
 *  
 *  2025-08-17:
 *    - Conducted thorough testing of all CSV operations:
 *        • CSV format validation for books, users and ratings
 *        • Backup creation with timestamped filenames
 *        • Data loading and saving functionality
 *    - Fixed edge cases in CSV parsing and serialization
 *  
 *  2025-08-16:
 *    - Initial implementation of core file management utilities:
 *        • Path management for books.csv, users.csv, and ratings.csv
 *        • CSV validation with column count checking
 *        • Timestamped backup creation
 *        • Book/User/Rating loading from CSV
 *        • Book/User/Rating saving to CSV
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Book;
import model.Rating;
import model.User;

public class FileUtils 
{
    private static final String strBaseDir = "/work/data/";  // Base directory for CSV files


    /**
     * Gets the books.csv file path
     * @return - the path to books.csv
     */ 
    public static String getBookFilePath()
    {
        return strBaseDir + "books.csv";
    }


    /**
     * Gets the users.csv file path
     * @return - the path to users.csv
     */
    public static String getUserFilePath()
    {
        return strBaseDir + "users.csv";
    }


    /**
     * Gets the ratings.csv file path
     * @return - the path to ratings.csv
     */
    public static String getRatingFilePath()
    {
        return strBaseDir + "ratings.csv";
    }


    /**
     * Loads books from the default CSV
     * @return - ArrayList<Book> loaded books
     */
    public static ArrayList<Book> loadBooksFromCSV()
    {
        return loadBooksFromCSV(getBookFilePath());
    }


    /**
     * Loads users from the default CSV
     * @return - ArrayList<User> loaded users
     */
    public static ArrayList<User> loadUsersFromCSV()
    {
        return loadUsersFromCSV(getUserFilePath());
    }


    /**
     * Loads ratings from the default CSV
     * @return - ArrayList<Rating> loaded ratings
     * @throws IllegalArgumentException if the rating format is invalid
     */
    public static ArrayList<Rating> loadRatingsFromCSV()
    {
        return loadRatingsFromCSV(getRatingFilePath());
    }


    /**
     * Saves books to the default CSV
     * @param bookList - list of Book objects to save
     */
    public static void saveBooksToCSV(ArrayList<Book> bookList)
    {
        saveBooksToCSV(bookList, getBookFilePath());
    }


    /**
     * Saves users to the default CSV
     * @param userList - list of User objects to save
     */
    public static void saveUsersToCSV(ArrayList<User> userList)
    {
        saveUsersToCSV(userList, getUserFilePath());
    }


    /**
     * Saves ratings to the default CSV
     * @param ratingList - list of Rating objects to save
     * @throws IllegalArgumentException if the rating format is invalid
     */
    public static void saveRatingsToCSV(ArrayList<Rating> ratingList)
    {
        saveRatingsToCSV(ratingList, getRatingFilePath());
    }


    /**
     * Creates a timestamped backup of the specified file
     * @param strPath - path to the file to back up
     * @return - true if backup succeeded; false if file does not exist
     * @throws IOException if file read/write fails
     */
    public static boolean backupFile(String strPath)
    {
        File fileOrig = new File(strPath);  // Original file

        if (!fileOrig.exists())  // File must exist
        {
            return false;  
        }

        String strTimeStamp   = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());  // Timestamp
        String strBackupPath  = strPath.replace(".csv", "_" + strTimeStamp + ".bak");         // Backup path

        try (BufferedReader br = new BufferedReader(new FileReader(fileOrig));  // File reader
             PrintWriter    pw = new PrintWriter(new FileWriter(strBackupPath))) // File writer
        {
            String strLine = null;  // Current line

            while ((strLine = br.readLine()) != null)  // Copy each line
            {
                pw.println(strLine);
            }

            return true;
        }
        catch (IOException e)  // Backup failure
        {
            System.out.println("Backup failed. Please check file permissions.");
            return false;
        }
    }


    /**
     * Validates that a CSV file has the expected number of columns
     * @param strPath - path to the CSV file
     * @param intExpectedCols - expected number of columns per row
     * @return - true if format is valid; false otherwise
     * @throws IOException if file read fails
     */
    public static boolean validateCSVFile(String strPath, int intExpectedCols)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(strPath)))  // File reader
        {
            String strLine = null;  
            int    intRow  = 0;     // Line counter

            while ((strLine = br.readLine()) != null)  // Read rows
            {
                intRow++;  
                String[] strColsArr = strLine.split(",");  // Split columns

                if (strColsArr.length != intExpectedCols)  // Column count mismatch
                {
                    System.out.println("Format error at line " + intRow + ".");
                    return false;
                }
            }

            return (intRow > 1);  // Must have header + data
        }
        catch (IOException e)  // Validation failure
        {
            System.out.println("Failed to verify CSV file. Please ensure file exists.");
            return false;
        }
    }


    /**
     * Internal: loads books from specified CSV path
     * @param strPath - CSV file path for books
     * @return - ArrayList<Book> loaded books; empty list if failure
     */
    private static ArrayList<Book> loadBooksFromCSV(String strPath)
    {
        ArrayList<Book> bookList = new ArrayList<Book>();  // Result list

        if (!validateCSVFile(strPath, 7))  // Verify format
        {
            System.out.println("The book CSV format is incorrect.");
            return bookList;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(strPath)))  // File reader
        {
            String  strLine      = null;
            boolean isFirstLine  = true;  // Skip header flag

            while ((strLine = br.readLine()) != null)  // Read rows
            {
                if (isFirstLine)  // Skip header row
                {
                    isFirstLine = false;
                    continue;
                }

                String[] strFieldArr = strLine.split(",");  // Parse fields

                Book book = new Book(
                    strFieldArr[0],
                    strFieldArr[1],
                    strFieldArr[2],
                    strFieldArr[3],
                    Double.parseDouble(strFieldArr[4]),
                    Boolean.parseBoolean(strFieldArr[5]),
                    Integer.parseInt(strFieldArr[6])
                );  // Construct Book

                if (book.validateBook())  // Only add valid books
                {
                    bookList.add(book);
                }
            }
        }
        catch (IOException e)  // Loading failure
        {
            System.out.println("Failed to load books. Please check the file.");
        }

        return bookList;  // Return result
    }


    /**
     * Internal: loads users from specified CSV path
     * @param strPath - CSV file path for users
     * @return - ArrayList<User> loaded users; empty list if failure
     */
    private static ArrayList<User> loadUsersFromCSV(String strPath)
    {
        ArrayList<User> userList = new ArrayList<User>();  // Result list

        if (!validateCSVFile(strPath, 5))  // Verify format
        {
            System.out.println("The user CSV format is incorrect.");
            return userList;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(strPath)))  // File reader
        {
            String  strLine      = null;
            boolean isFirstLine  = true;  // Skip header flag

            while ((strLine = br.readLine()) != null)  // Read rows
            {
                if (isFirstLine)  // Skip header row
                {
                    isFirstLine = false;
                    continue;
                }

                String[] strFieldArr = strLine.split(",");  // Parse fields

                User user = new User(
                    strFieldArr[0],
                    strFieldArr[1],
                    strFieldArr[2],
                    strFieldArr[3]
                );  // Construct User

                user.setStrPreferences(strFieldArr[4]);  // Restore preferences
                userList.add(user);
            }
        }
        catch (IOException e)  // Loading failure
        {
            System.out.println("Failed to load users. Please check the file.");
        }

        return userList;  // Return result
    }


    /**
     * Internal: loads ratings from specified CSV path
     * @param strPath - CSV file path for ratings
     * @return - ArrayList<Rating> loaded ratings; empty list if failure
     */
    private static ArrayList<Rating> loadRatingsFromCSV(String strPath)
    {
        ArrayList<Rating> ratingList = new ArrayList<Rating>();  // Result list

        if (!validateCSVFile(strPath, 3))  // Verify format
        {
            System.out.println("The rating CSV format is incorrect.");
            return ratingList;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(strPath)))  // File reader
        {
            String  strLine      = null;
            boolean isFirstLine  = true;  // Skip header flag

            while ((strLine = br.readLine()) != null)  // Read rows
            {
                if (isFirstLine)  // Skip header row
                {
                    isFirstLine = false;
                    continue;
                }

                String[] strFieldArr = strLine.split(",");  // Parse fields

                ratingList.add(new Rating(
                    strFieldArr[0],
                    strFieldArr[1],
                    Integer.parseInt(strFieldArr[2])
                ));
            }
        }
        catch (IOException e)  // Loading failure
        {
            System.out.println("Failed to load ratings. Please check the file.");
        }

        return ratingList;  // Return result
    }


    /**
     * Internal: saves books list to a specified CSV path
     * @param bookList - the list of Book objects
     * @param strPath  - the file path to write
     */
    private static void saveBooksToCSV(ArrayList<Book> bookList, String strPath)
    {
        backupFile(strPath);  // Backup existing file

        try (PrintWriter pw = new PrintWriter(new FileWriter(strPath)))  // File writer
        {
            pw.println("id,title,author,genre,rating,isAvailable,borrowCount");  // Header

            for (Book book : bookList)  // Write rows
            {
                pw.println(String.join(",",
                    book.getStrId(),
                    book.getStrTitle(),
                    book.getStrAuthor(),
                    book.getStrGenre(),
                    String.valueOf(book.getDblAvgRating()),
                    String.valueOf(book.isAvailable()),
                    String.valueOf(book.getIntBorrowCount())
                ));
            }
        }
        catch (IOException e)  // Write failure
        {
            System.out.println("Failed to save books. Please check file permissions.");
        }
    }


    /**
     * Internal: saves users list to a specified CSV path
     * @param userList - the list of User objects
     * @param strPath - the file path to write
     */
    private static void saveUsersToCSV(ArrayList<User> userList, String strPath)
    {
        try (PrintWriter pw = new PrintWriter(new FileWriter(strPath)))  // File writer
        {
            pw.println("username,password,securityQ,securityA,preferences");  // Header

            for (User user : userList)  // Write rows
            {
                pw.println(String.join(",",
                    user.getStrUsername(),
                    user.getStrPassword(),
                    user.getStrSecurityQ(),
                    user.getStrSecurityA(),
                    user.getStrPreferences()
                ));
            }
        }
        catch (IOException e)  // Write failure
        {
            System.out.println("Failed to save users. Please check file permissions.");
        }
    }


    /**
     * Internal: saves ratings list to a specified CSV path
     * @param ratingList - the list of Rating objects
     * @param strPath - the file path to write
     * @throws IllegalArgumentException if the rating format is invalid
     */
    private static void saveRatingsToCSV(ArrayList<Rating> ratingList, String strPath)
    {
        try (PrintWriter pw = new PrintWriter(new FileWriter(strPath)))  // File writer
        {
            pw.println("user_id,book_id,rating");  // Header

            for (Rating rating : ratingList)  // Write rows
            {
                pw.println(String.join(",",
                    rating.getStrUserId(),
                    rating.getStrBookId(),
                    String.valueOf(rating.getIntRating())
                ));
            }
        }
        catch (IOException e)  // Write failure
        {
            System.out.println("Failed to save ratings. Please check file permissions.");
        }
    }
}