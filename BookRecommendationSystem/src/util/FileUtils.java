package util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Book;
import model.User;
import model.Rating;

/**
 * File: FileUtils.java
 * Author: 
 * Date: [当前日期]
 *
 * Description: 文件操作工具类，处理CSV读写、备份与校验
 */
public class FileUtils 
{
    // 数据目录（CodeHS 环境下一般是 /work/data/）
    private static final String BASE_DIR = "/work/data/";

    // 默认文件路径
    public static String getBookFilePath()   { return BASE_DIR + "books.csv"; }
    public static String getUserFilePath()   { return BASE_DIR + "users.csv"; }
    public static String getRatingFilePath() { return BASE_DIR + "ratings.csv"; }

    // ======== 公有加载接口 ========

    public static ArrayList<Book> loadBooksFromCSV() {
        return loadBooksFromCSV(getBookFilePath());
    }

    public static ArrayList<User> loadUsersFromCSV() {
        return loadUsersFromCSV(getUserFilePath());
    }

    public static ArrayList<Rating> loadRatingsFromCSV() {
        return loadRatingsFromCSV(getRatingFilePath());
    }

    // ======== 公有保存接口 ========

    public static void saveBooksToCSV(ArrayList<Book> list) {
        saveBooksToCSV(list, getBookFilePath());
    }

    public static void saveUsersToCSV(ArrayList<User> list) {
        saveUsersToCSV(list, getUserFilePath());
    }

    public static void saveRatingsToCSV(ArrayList<Rating> list) {
        saveRatingsToCSV(list, getRatingFilePath());
    }

    // ======== 备份与校验 ========

    public static boolean backupFile(String path) {
        File orig = new File(path);
        if (!orig.exists()) return false;

        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(new Date());
        String bak = path.replace(".csv", "_" + ts + ".bak");

        try (
            BufferedReader br = new BufferedReader(new FileReader(orig));
            PrintWriter    pw = new PrintWriter(new FileWriter(bak))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                pw.println(line);
            }
            return true;
        } catch (IOException e) {
            System.out.println("备份失败: " + e.getMessage());
            return false;
        }
    }

    public static boolean validateCSVFile(String path, int expectedCols) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int    row = 0;
            while ((line = br.readLine()) != null) {
                row++;
                String[] cols = line.split(",");
                if (cols.length != expectedCols) {
                    System.out.println("格式错误第 " + row + " 行: " + line);
                    return false;
                }
            }
            return row > 1;
        } catch (IOException e) {
            System.out.println("Verification failed: " + e.getMessage());
            return false;
        }
    }

    // ======== 私有通用加载/保存实现 ========

    private static ArrayList<Book> loadBooksFromCSV(String path) {
        if (!validateCSVFile(path, 7)) {
            System.out.println("图书文件格式错误，返回空列表");
            return new ArrayList<>();
        }
        ArrayList<Book> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] f = line.split(",");
                Book book = new Book(
                    f[0],
                    f[1],
                    f[2],
                    f[3],
                    Double.parseDouble(f[4]),
                    Boolean.parseBoolean(f[5]),
                    Integer.parseInt(f[6])
                );
                if (book.validateBook()) list.add(book);
            }
        } catch (IOException e) {
            System.out.println("载入书籍失败: " + e.getMessage());
        }
        return list;
    }

    private static ArrayList<User> loadUsersFromCSV(String path) {
        // users.csv 有五列：username,password,securityQ,securityA,preferences
        if (!validateCSVFile(path, 5)) {
            System.out.println("用户文件格式错误，返回空列表");
            return new ArrayList<>();
        }
        ArrayList<User> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] f = line.split(",");
                User u = new User(f[0], f[1], f[2], f[3]);
                u.setStrPreferences(f[4]);
                list.add(u);
            }
        } catch (IOException e) {
            System.out.println("载入用户失败: " + e.getMessage());
        }
        return list;
    }

    private static ArrayList<Rating> loadRatingsFromCSV(String path) {
        // ratings.csv 有三列：user_id,book_id,rating
        if (!validateCSVFile(path, 3)) {
            System.out.println("评分文件格式错误，返回空列表");
            return new ArrayList<>();
        }
        ArrayList<Rating> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] f = line.split(",");
                list.add(new Rating(f[0], f[1], Integer.parseInt(f[2])));
            }
        } catch (IOException e) {
            System.out.println("载入评分失败: " + e.getMessage());
        }
        return list;
    }

    private static void saveBooksToCSV(ArrayList<Book> list, String path) {
        backupFile(path);
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("id,title,author,genre,rating,isAvailable,borrowCount");
            for (Book b : list) {
                pw.println(String.join(",",
                    b.getStrId(),
                    b.getStrTitle(),
                    b.getStrAuthor(),
                    b.getStrGenre(),
                    String.valueOf(b.getDblAvgRating()),
                    String.valueOf(b.isAvailable()),
                    String.valueOf(b.getIntBorrowCount())
                ));
            }
        } catch (IOException e) {
            System.out.println("保存书籍失败: " + e.getMessage());
        }
    }

    private static void saveUsersToCSV(ArrayList<User> list, String path) {
        // 不备份用户文件
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("username,password,securityQ,securityA,preferences");
            for (User u : list) {
                pw.println(String.join(",",
                    u.getStrUsername(),
                    u.getStrPassword(),
                    u.getStrSecurityQ(),
                    u.getStrSecurityA(),
                    u.getStrPreferences()
                ));
            }
        } catch (IOException e) {
            System.out.println("保存用户失败: " + e.getMessage());
        }
    }

    private static void saveRatingsToCSV(ArrayList<Rating> list, String path) {
        // 不备份评分文件
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("user_id,book_id,rating");
            for (Rating r : list) {
                pw.println(String.join(",",
                    r.getStrUserId(),
                    r.getStrBookId(),
                    String.valueOf(r.getIntRating())
                ));
            }
        } catch (IOException e) {
            System.out.println("保存评分失败: " + e.getMessage());
        }
    }
}
