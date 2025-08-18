package util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Book;
import model.User;
import model.Rating;


public class FileUtils 
{
    private static final String BASE_DIR = "/work/data/";

    public static String getBookFilePath()   { return BASE_DIR + "books.csv"; }
    public static String getUserFilePath()   { return BASE_DIR + "users.csv"; }
    public static String getRatingFilePath() { return BASE_DIR + "ratings.csv"; }


    public static ArrayList<Book> loadBooksFromCSV() {
        return loadBooksFromCSV(getBookFilePath());
    }

    public static ArrayList<User> loadUsersFromCSV() {
        return loadUsersFromCSV(getUserFilePath());
    }

    public static ArrayList<Rating> loadRatingsFromCSV() {
        return loadRatingsFromCSV(getRatingFilePath());
    }


    public static void saveBooksToCSV(ArrayList<Book> list) {
        saveBooksToCSV(list, getBookFilePath());
    }

    public static void saveUsersToCSV(ArrayList<User> list) {
        saveUsersToCSV(list, getUserFilePath());
    }

    public static void saveRatingsToCSV(ArrayList<Rating> list) {
        saveRatingsToCSV(list, getRatingFilePath());
    }


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
            System.out.println("Backup failed: " + e.getMessage());
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
                    System.out.println("Format error " + row + " line: " + line);
                    return false;
                }
            }
            return row > 1;
        } catch (IOException e) {
            System.out.println("Verification failed: " + e.getMessage());
            return false;
        }
    }


    private static ArrayList<Book> loadBooksFromCSV(String path) {
        if (!validateCSVFile(path, 7)) {
            System.out.println("The file format of the book is incorrect. Return an empty list");
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
            System.out.println("Failed to load the book: " + e.getMessage());
        }
        return list;
    }

    private static ArrayList<User> loadUsersFromCSV(String path) {
        if (!validateCSVFile(path, 5)) {
            System.out.println("The user file format is incorrect, and an empty list is returned");
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
            System.out.println("Failed to load the user: " + e.getMessage());
        }
        return list;
    }

    private static ArrayList<Rating> loadRatingsFromCSV(String path) {
        if (!validateCSVFile(path, 3)) {
            System.out.println("The format of the scoring file is incorrect, and an empty list is returned");
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
            System.out.println("Failed to load the score: " + e.getMessage());
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
            System.out.println("Failed to save the book: " + e.getMessage());
        }
    }



    private static void saveUsersToCSV(ArrayList<User> list, String path) {
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
            System.out.println("Failed to save the user: " + e.getMessage());
        }
    }



    private static void saveRatingsToCSV(ArrayList<Rating> list, String path) {
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
            System.out.println("Failed to save the score: " + e.getMessage());
        }
    }
}
