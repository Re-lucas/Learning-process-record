/**
 * File: BookDatabase.java
 * Author: Lucas Wu
 * Date: 2025-08-17
 *
 * Description:
 *  - Loads book records from CSV at initialization
 *  - Provides lookup by ID, full-text search by title/author
 *  - Retrieves popular books sorted by borrow count
 *  - Finds similar books by genre or author
 *  - Supports borrowing and returning, persisting changes to CSV
 * 
 * Work Log:
 *  2025-08-18 (Lucas Wu): 
 *    - Added comprehensive JavaDoc comments for all class methods
 *    - Refactored variable names to comply with coding style standards
 *  
 *  2025-08-17 (Lucas Wu):
 *    - Implemented CRUD operations framework
 *    - Added book borrowing/returning functionality
 *    - Integrated CSV persistence layer
 *  
 *  2025-08-16 (Lucas Wu):
 *    - Created initial class structure
 *    - Implemented core functionality:
 *        • CSV data loading
 *        • ID-based book lookup
 *        • Title/author search
 *        • Popular books ranking
 *        • Similar books recommendation
 * 
**/
package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.Book;
import util.FileUtils;

public class BookDatabase 
{
    private ArrayList<Book> bookList;  // List of all books

    /**
     * Constructs and initializes the book database
     */
    public BookDatabase() 
    {
        bookList = new ArrayList<Book>();  // Initialize empty list
        loadBooks();
    }


    /**
     * Loads books from CSV via FileUtils
     */
    private void loadBooks() 
    {
        bookList = FileUtils.loadBooksFromCSV();  // Load records
        System.out.println("Loaded " + bookList.size() + " books from database");
    }


    /**
     * Finds a book by its unique identifier
     * @param strBookId - the ID of the book to locate
     * @return - the matching Book or null if not found
     */
    public Book findBookById(String strBookId) 
    {
        for (Book book : bookList) 
        {
            if (book.getStrId().equalsIgnoreCase(strBookId))  // ID match
            {
                return book;
            }
        }

        return null;
    }


    /**
     * Searches books whose title or author contains the given query
     * @param strQuery - keyword to search for
     * @return - list of matching Book objects
     */
    public ArrayList<Book> searchBooks(String strQuery) 
    {
        ArrayList<Book> bookListResult = new ArrayList<Book>();  // Results
        String strLowerQuery = strQuery.toLowerCase();            // Normalize

        for (Book book : bookList) 
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
     * Retrieves top N books sorted by descending borrow count
     * @param intCount - number of books to return
     * @return - list of top borrowed books
     */
    public ArrayList<Book> getPopularBooks(int intCount) 
    {
        ArrayList<Book> bookListSorted = new ArrayList<Book>(bookList);  // Working copy

        Collections.sort(bookListSorted, new Comparator<Book>() 
        {
            @Override
            public int compare(Book book1, Book book2) 
            {
                return book2.getIntBorrowCount() - book1.getIntBorrowCount();  // Descending
            }
        });

        int intSubEnd = Math.min(intCount, bookListSorted.size());  // Ensure bounds
        return new ArrayList<Book>(bookListSorted.subList(0, intSubEnd));
    }


    /**
     * Finds books similar by genre or author to the reference book
     * @param bookRefObj - reference Book for similarity
     * @param intCount  - maximum number of similar books
     * @return - list of similar Book objects
     */
    public ArrayList<Book> getSimilarBooks(Book bookRefObj, int intCount) 
    {
        ArrayList<Book> bookListResult = new ArrayList<Book>();  // Results

        for (Book book : bookList) 
        {
            if (book.getStrId().equals(bookRefObj.getStrId()))  // Skip same book
            {
                continue;
            }

            if (book.getStrGenre().equals(bookRefObj.getStrGenre())  // Same genre
                || book.getStrAuthor().equals(bookRefObj.getStrAuthor()))  // Or same author
            {
                bookListResult.add(book);

                if (bookListResult.size() >= intCount)  // Enough results
                {
                    break;
                }
            }
        }

        return bookListResult;
    }


    /**
     * Borrows a book if available, increments borrow count, and saves
     * @param strBookId - ID of the book to borrow
     * @return - true if borrow succeeds; false otherwise
     */
    public boolean borrowBook(String strBookId) 
    {
        Book book = findBookById(strBookId);  // Locate book

        if (book == null || !book.isAvailable())  // Availability check
        {
            return false;
        }

        book.setAvailable(false);                             // Mark as lent
        book.setIntBorrowCount(book.getIntBorrowCount() + 1); // Increment count

        saveBooks();  // Persist update
        return true;
    }


    /**
     * Returns a borrowed book and saves the updated state
     * @param strBookId ID of the book to return
     */
    public void returnBook(String strBookId) 
    {
        Book book = findBookById(strBookId);  // Locate book

        if (book != null) 
        {
            book.setAvailable(true);  // Mark as available
            saveBooks();              // Persist update
        }
    }


    /**
     * Saves all book records back to CSV
     */
    public void saveBooks() 
    {
        FileUtils.saveBooksToCSV(bookList);  // Write to storage
    }


    /**
     * Retrieves a defensive copy of all books
     * @return list of all Book objects
     */
    public ArrayList<Book> getAllBooks() 
    {
        return new ArrayList<Book>(bookList);  // Return copy
    }
}