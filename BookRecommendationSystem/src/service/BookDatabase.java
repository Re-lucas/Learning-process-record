package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.Book;
import util.FileUtils;


public class BookDatabase 
{
    private ArrayList<Book> bookList; 

    public BookDatabase() 
    {
        this.bookList = new ArrayList<>();
        loadBooks();  
    }


    private void loadBooks() 
    {
        bookList = FileUtils.loadBooksFromCSV();
        System.out.println("Loaded " + bookList.size() + " books from database");
    }


    public Book findBookById(String strBookId) 
    {
        for (Book book : bookList) 
        {
            if (book.getStrId().equalsIgnoreCase(strBookId)) 
            {
                return book;
            }
        }
        return null;
    }


    public ArrayList<Book> searchBooks(String strQuery) 
    {
        ArrayList<Book> result = new ArrayList<>();
        String lower = strQuery.toLowerCase();

        for (Book book : bookList) 
        {
            if (book.getStrTitle().toLowerCase().contains(lower) ||
                book.getStrAuthor().toLowerCase().contains(lower)) 
            {
                result.add(book);
            }
        }
        return result;
    }


    public ArrayList<Book> getPopularBooks(int intCount) 
    {
        ArrayList<Book> sorted = new ArrayList<>(bookList);
        Collections.sort(sorted, new Comparator<Book>() 
        {
            @Override
            public int compare(Book b1, Book b2) 
            {
                return b2.getIntBorrowCount() - b1.getIntBorrowCount();
            }
        });
        return new ArrayList<>(
            sorted.subList(0, Math.min(intCount, sorted.size()))
        );
    }


    public ArrayList<Book> getSimilarBooks(Book bookRefObj, int intCount) 
    {
        ArrayList<Book> result = new ArrayList<>();

        for (Book b : bookList) 
        {
            if (b.getStrId().equals(bookRefObj.getStrId())) continue;

            if (b.getStrGenre().equals(bookRefObj.getStrGenre()) ||
                b.getStrAuthor().equals(bookRefObj.getStrAuthor())) 
            {
                result.add(b);
                if (result.size() >= intCount) break;
            }
        }
        return result;
    }


    public boolean borrowBook(String strBookId) 
    {
        Book book = findBookById(strBookId);
        if (book == null || !book.isAvailable()) 
        {
            return false;
        }
        book.setAvailable(false);
        book.setIntBorrowCount(book.getIntBorrowCount() + 1);
        saveBooks();
        return true;
    }


    public void returnBook(String strBookId) 
    {
        Book book = findBookById(strBookId);
        if (book != null) 
        {
            book.setAvailable(true);
            saveBooks();
        }
    }


    public void saveBooks() 
    {
        FileUtils.saveBooksToCSV(bookList);
    }


    public ArrayList<Book> getAllBooks() 
    {
        return new ArrayList<>(bookList);
    }
}
