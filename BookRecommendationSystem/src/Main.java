/**
 * File: Main.java
 * Author: YE
 * Date: 2025-08-18
 *
 * Description:
 *  - Serves as the command-line interface for the Book Recommendation System.
 *  - Manages user authentication, registration, and password recovery.
 *  - Displays distinct menus and workflows for regular users versus administrators.
 *  - Delegates search, recommendation, borrowing, and reporting tasks to dedicated services.
 */
import java.util.ArrayList;
import java.util.Scanner;

import model.User;
import model.Book;
import service.UserManager;
import service.BookDatabase;
import service.RecommendationEngine;
import service.SearchService;
import service.ReportGenerator;

public class Main 
{
    // Manages user accounts and authentication
    private UserManager userManager;

    // Stores and retrieves book records
    private BookDatabase bookDatabase;

    // Generates personalized recommendations
    private RecommendationEngine recommendationEngine;

    // Provides keyword-based book search
    private SearchService searchService;

    // Produces various analytical reports
    private ReportGenerator reportGenerator;

    // Holds the currently logged-in user (null if none)
    private User currentUserObj;

    // Flag to indicate if the current session is an administrator
    private boolean isAdminFlag = false;

    // Reads input from the command line
    private Scanner input = new Scanner(System.in);

    /**
     * Program entry point.
     * Instantiates Main and starts the run loop.
     */
    public static void main(String[] args) 
    {
        new Main().run();
    }


    /**
     * Main application loop.
     * Initializes system components and displays the welcome screen.
     * Handles user login, registration, and menu navigation.
     */
    public void run() 
    {
        initializeSystem();
        showWelcomeScreen();

        while (true) 
        {
            if (currentUserObj == null) {
                showLoginMenu();
            } else if (isAdminFlag) {
                showAdminMenu();
            } else {
                showUserMenu();
            }
        }
    }


    /**
     * Initializes all system components:
     *  - UserManager for user accounts
     */
    private void initializeSystem() 
    {
        userManager          = new UserManager();
        bookDatabase         = new BookDatabase();
        recommendationEngine = new RecommendationEngine(bookDatabase);
        searchService        = new SearchService(bookDatabase);
        reportGenerator      = new ReportGenerator(bookDatabase, userManager);
        currentUserObj       = null;
        isAdminFlag          = false;
    }


    /**
     * Displays the welcome screen with system title and description.
     * Provides a brief introduction to the Book Recommendation System.
     */
    private void showWelcomeScreen() 
    {
        System.out.println("\n\n===== Book Recommendation System =====");
        System.out.println("   Wise Reading · Personality Discovery\n");
    }


    /**
     * Displays the login menu with options for:
     *  - User login
     */
    private void showLoginMenu() 
    {
        System.out.println("--- Main menu ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Forget the password");
        System.out.println("4. Exit the system");
        int choice = readInt("Please select the operation: ", 1, 4);

        switch (choice) 
        {
            case 1: loginUser(); break;
            case 2: registerUser(); break;
            case 3: recoverPassword(); break;
            case 4:
                System.out.println("Thank you for using it. Goodbye!");
                System.exit(0);
        }
    }


    /**
     * Prompts for username and password, authenticates the user.
     * If successful, sets currentUserObj and isAdminFlag.
     * If admin credentials are entered, grants admin access.
     */
    private void loginUser() 
    {
        String user = readInputLine("User name: ");
        String pwd  = readInputLine("Password: ");

        // Hardcoded administrator credentials
        if ("admin".equals(user) && "admin123".equals(pwd)) 
        {
            currentUserObj = new User("admin", "admin123", "", "");
            isAdminFlag    = true;
            System.out.println("The administrator has logged in successfully!");
            return;
        }

        // Regular user login
        User u = userManager.loginUser(user, pwd);
        if (u == null) {
            System.out.println("Login failed: Incorrect username or password");
        } else {
            currentUserObj = u;
            isAdminFlag    = false;
            System.out.println("Welcome back " + user + "!");
        }
    }


    /**
     * Registers a new user with username, password, security question, and answer.
     * If successful, adds to user list and persists to CSV.
     * If username already exists, informs the user.
     */
    private void registerUser() 
    {
        System.out.println("\n--- New user registration ---");
        String user = readInputLine("Set the user name: ");
        String pwd  = readInputLine("Set a password: ");
        String q    = readInputLine("Safety issues: ");
        String a    = readInputLine("Answer to the question: ");

        if (userManager.registerUser(user, pwd, q, a)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed: The username has been used");
        }
    }


    /**
     * Prompts for username and security answer to recover password.
     * If successful, resets the password and informs the user.
     */
    private void recoverPassword() 
    {
        System.out.println("\n--- Password recovery ---");
        String user = readInputLine("User name: ");
        String a    = readInputLine("Answer to safety question: ");

        String newPwd = userManager.recoverPassword(user, a);
        if (newPwd != null) {
            System.out.println("The password has been reset! New password: " + newPwd);
            System.out.println("Please change your password as soon as possible after logging in");
        } else {
            System.out.println("Recovery failed: The username or answer is incorrect");
        }
    }

    /**
     * Displays the regular user menu with options for search, popular books,
     * personalized recommendations, borrowing, and logout.
     */
    private void showUserMenu() 
    {
        System.out.println("\n--- User Menu ---");
        System.out.println("1. Search for books");
        System.out.println("2. Popular books");
        System.out.println("3. Personalized recommendation");
        System.out.println("4. Collection List (Not yet implemented)");
        System.out.println("5. Borrow books");
        System.out.println("6. Account Settings (not yet implemented)");
        System.out.println("7. Log out");
        int choice = readInt("Please select the operation: ", 1, 7);

        switch (choice) 
        {
            case 1: doSearch(); break;
            case 2: showPopular(); break;
            case 3: showRecommendations(); break;
            case 4: System.out.println("Functions to be expanded"); break;
            case 5: doBorrow(); break;
            case 6: System.out.println("Functions to be expanded"); break;
            case 7:
                currentUserObj = null;
                System.out.println("Logged out");
        }
    }

    /**
     * Displays the administrator menu for generating various reports.
     * Options include popular books, author stats, genre stats, and user activity.
     */
    private void showAdminMenu() 
    {
        System.out.println("\n--- Administrator Console ---");
        System.out.println("1. View the report on popular books");
        System.out.println("2. Analysis of author popularity");
        System.out.println("3. Book classification statistics");
        System.out.println("4. User Activity report");
        System.out.println("5. Export report to file (not yet implemented)");
        System.out.println("6. Return to the login interface");
        int choice = readInt("Please select the operation: ", 1, 6);

        switch (choice) 
        {
            case 1:
                System.out.println(reportGenerator.generatePopularBooksReport(10));
                break;
            case 2:
                System.out.println(reportGenerator.generateAuthorPopularityReport());
                break;
            case 3:
                System.out.println(reportGenerator.generateGenreUsageReport());
                break;
            case 4:
                System.out.println(reportGenerator.generateUserActivityReport());
                break;
            case 5:
                System.out.println("The export function is yet to be implemented");
                break;
            case 6:
                currentUserObj = null;
                isAdminFlag    = false;
        }
    }

    /**
     * Prompts for a keyword, performs a smart search, and lists matching books.
     */
    private void doSearch() 
    {
        String kw = readInputLine("Enter keywords: ");
        ArrayList<Book> list = searchService.smartSearch(kw);

        System.out.println("Found " + list.size() + " books:");
        for (Book b : list) {
            System.out.println("  - " + b);
        }
    }

    /**
     * Retrieves and prints the top N popular books from the database.
     */
    private void showPopular() 
    {
        var list = bookDatabase.getPopularBooks(10);

        System.out.println("TOP10 Popular Books");
        for (Book b : list) {
            System.out.println("  - " + b);
        }
    }

    /**
     * Generates personalized recommendations for the current user and prints them.
     */
    private void showRecommendations() 
    {
        var list = recommendationEngine.generateRecommendations(
            currentUserObj.getStrUsername(), 5);

        System.out.println("For your recommendation:");
        for (Book b : list) {
            System.out.println("  - " + b);
        }
    }

    /**
     * Handles borrowing a book by ID. Upon success, captures user rating.
     */
    private void doBorrow() 
    {
        String id = readInputLine("Enter the ID of the book you want to borrow: ");
        if (bookDatabase.borrowBook(id)) {
            int rate = readInt("Your rating (1-5): ", 1, 5);
            recommendationEngine.addRating(currentUserObj.getStrUsername(), id, rate);
            System.out.println("Borrowing successful!");
        } else {
            System.out.println("Borrowing failed: The ID does not exist or cannot be borrowed");
        }
    }

    /**
     * Reads an integer from the console within the specified bounds.
     * Re-prompts until a valid integer is entered.
     *
     * @param prompt message to display
     * @param min    inclusive lower bound
     * @param max    inclusive upper bound
     * @return validated integer
     */
    private int readInt(String prompt, int min, int max) 
    {
        while (true) 
        {
            try 
            {
                System.out.print(prompt);
                int v = Integer.parseInt(input.nextLine());
                if (v >= min && v <= max) return v;

                System.out.println("Please enter a number between " + min + " and " + max);
            } 
            catch (NumberFormatException e) 
            {
                System.out.println("Please enter valid numbers!");
            }
        }
    }

    /**
     * Reads a line of text from the console, trims leading/trailing whitespace.
     *
     * @param prompt message to display
     * @return user input string
     */
    private String readInputLine(String prompt) 
    {
        System.out.print(prompt);
        return input.nextLine().trim();
    }
}
