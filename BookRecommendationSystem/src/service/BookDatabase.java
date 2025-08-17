package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.Book;
import util.FileUtils;

/**
 * File: BookDatabase.java
 * Author: [团队名称]
 * Date: [当前日期]
 *
 * Description: 图书数据管理服务，处理加载、搜索、推荐和状态管理
 */
public class BookDatabase 
{
    private ArrayList<Book> bookList;  // 内存中的图书列表

    public BookDatabase() 
    {
        this.bookList = new ArrayList<>();
        loadBooks();  
    }

    /**
     * 从 CSV 加载图书
     */
    private void loadBooks() 
    {
        bookList = FileUtils.loadBooksFromCSV();
        System.out.println("Loaded " + bookList.size() + " books from database");
    }

    /**
     * 按 ID 查找图书
     */
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

    /**
     * 模糊搜索（标题或作者）
     */
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

    /**
     * 获取按借阅次数排序的热门图书
     */
    public ArrayList<Book> getPopularBooks(int intCount) 
    {
        ArrayList<Book> sorted = new ArrayList<>(bookList);
        Collections.sort(sorted, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                return b2.getIntBorrowCount() - b1.getIntBorrowCount();
            }
        });
        return new ArrayList<>(
            sorted.subList(0, Math.min(intCount, sorted.size()))
        );
    }

    /**
     * 获取同类或同作者的相似图书
     */
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

    /**
     * 借阅图书（更新状态并保存）
     */
    public boolean borrowBook(String strBookId) 
    {
        Book book = findBookById(strBookId);
        if (book == null || !book.isAvailable()) {
            return false;
        }
        book.setAvailable(false);
        book.setIntBorrowCount(book.getIntBorrowCount() + 1);
        saveBooks();
        return true;
    }

    /**
     * 归还图书（更新状态并保存）
     */
    public void returnBook(String strBookId) 
    {
        Book book = findBookById(strBookId);
        if (book != null) 
        {
            book.setAvailable(true);
            saveBooks();
        }
    }

    /**
     * 将当前内存中的图书列表写回 CSV
     */
    public void saveBooks() 
    {
        FileUtils.saveBooksToCSV(bookList);
    }

    /**
     * 获取所有图书的副本（不允许外部直接修改内部列表）
     */
    public ArrayList<Book> getAllBooks() 
    {
        return new ArrayList<>(bookList);
    }
}
