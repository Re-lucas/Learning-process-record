/**
 * File: Main.java
 * Author: Lucas Wu
 * Date: 2025-08-16
 *
 * Description:
 *  - Serves as the command-line interface for the Book Recommendation System
 *  - Manages user authentication, registration, and password recovery
 *  - Displays distinct menus and workflows for regular users versus administrators
 *  - Delegates search, recommendation, borrowing, and reporting tasks to dedicated services
 * 
 * Work Log (Lucas Wu):
 *  2025-08-19:
 *    - Performed additional code refinements and optimizations:
 *        • Enhanced error messages for better user guidance
 *        • Standardized user prompts across all interactions
 *        • Improved code documentation consistency
 *        • Conducted final system integration testing
 *  
 *  2025-08-18:
 *    - Conducted comprehensive code refactoring and optimization:
 *        • Renamed variables for consistency (currentUserObj → objCurrentUser, isAdminFlag → isAdmin)
 *        • Renamed methods for clarity (doSearch → performSearch, showPopular → showPopularBooks)
 *        • Standardized user prompts across all interactions
 *        • Enhanced error messages with more descriptive content
 *        • Optimized code structure to Allman style for readability
 *        • Normalized inline comments and documentation
 *    - Added detailed JavaDoc comments for all methods and classes
 *  
 *  2025-08-17:
 *    - Completed implementation of the command-line interface:
 *        • User authentication, registration, and password recovery flows
 *        • Book search, borrowing, and recommendation features
 *        • Administrator reporting functions
 *        • Menu navigation system for both user and admin roles
 *    - Conducted end-to-end system testing and validation
**/
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
    private UserManager userManager;          // Manages user accounts and authentication
    private BookDatabase bookDatabase;       // Stores and retrieves book records
    private RecommendationEngine recommendationEngine;  // Generates personalized recommendations
    private SearchService searchService;      // Provides keyword-based book search
    private ReportGenerator reportGenerator; // Produces various analytical reports
    private User objCurrentUser;             // Holds currently logged-in user (null if none)
    private boolean isAdmin = false;         // Flag for administrator session
    private Scanner input = new Scanner(System.in);  // Reads input from command line


    /**
     * Program entry point
     * Instantiates Main and starts the run loop
     */
    public static void main(String[] args) 
    {
        new Main().run();
    }


    /**
     * Main application loop
     * Initializes system components and displays welcome screen
     * Handles user login, registration, and menu navigation
     */
    public void run() 
    {
        initializeSystem();
        showWelcomeScreen();

        while (true) 
        {
            if (objCurrentUser == null) 
            {
                showLoginMenu();
            } 
            else if (isAdmin) 
            {
                showAdminMenu();
            } 
            else 
            {
                showUserMenu();
            }
        }
    }


    /**
    * Initializes all system components
    **/
    private void initializeSystem() 
    {
        userManager          = new UserManager();
        bookDatabase         = new BookDatabase();
        recommendationEngine = new RecommendationEngine(bookDatabase);
        searchService        = new SearchService(bookDatabase);
        reportGenerator      = new ReportGenerator(bookDatabase, userManager);
        objCurrentUser       = null;
        isAdmin              = false;
    }


    /**
     * Displays welcome screen with system title and description
     */
    private void showWelcomeScreen() 
    {
        System.out.println("\n\n===== Book Recommendation System =====");
        System.out.println("   Wise Reading · Personality Discovery\n");
    }


    /**
     * Displays login menu with authentication options
     */
    private void showLoginMenu() 
    {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Forget Password");
        System.out.println("4. Exit System");
        
        int intChoice = readInt("Select operation: ", 1, 4);

        switch (intChoice) 
        {
            case 1: 
                loginUser(); 
                break;
                
            case 2: 
                registerUser(); 
                break;
                
            case 3: 
                recoverPassword(); 
                break;
                
            case 4:
                System.out.println("\nThank you for using our system. Goodbye!");
                System.exit(0);
        }
    }


    /**
     * Authenticates user and sets session state
     */
    private void loginUser() 
    {
        String strUsername = readInputLine("Username: ");
        String strPassword = readInputLine("Password: ");

        // Hardcoded administrator credentials
        if ("admin".equals(strUsername) && "admin123".equals(strPassword)) 
        {
            objCurrentUser = new User("admin", "admin123", "", "");
            isAdmin        = true;
            System.out.println("\nAdministrator login successful!");
            return;
        }

        // Regular user login
        User objUser = userManager.loginUser(strUsername, strPassword);
        if (objUser == null) 
        {
            System.out.println("\nLogin failed: Incorrect credentials");
        } 
        else 
        {
            objCurrentUser = objUser;
            isAdmin        = false;
            System.out.println("\nWelcome back " + strUsername + "!");
        }
    }


    /**
     * Registers new user with security information
     */
    private void registerUser() 
    {
        System.out.println("\n--- New User Registration ---");
        String strUsername = readInputLine("Set username: ");
        String strPassword = readInputLine("Set password: ");
        String strSecurityQ = readInputLine("Security question: ");
        String strSecurityA = readInputLine("Security answer: ");

        if (userManager.registerUser(strUsername, strPassword, strSecurityQ, strSecurityA)) 
        {
            System.out.println("\nRegistration successful!");
        } 
        else 
        {
            System.out.println("\nRegistration failed: Username already exists");
        }
    }


    /**
     * Recovers password using security question
     */
    private void recoverPassword() 
    {
        System.out.println("\n--- Password Recovery ---");
        String strUsername = readInputLine("Username: ");
        String strSecurityA = readInputLine("Security answer: ");

        String strNewPwd = userManager.recoverPassword(strUsername, strSecurityA);
        if (strNewPwd != null) 
        {
            System.out.println("\nPassword reset successful! Temporary password: " + strNewPwd);
            System.out.println("Please change your password after login");
        } 
        else 
        {
            System.out.println("\nRecovery failed: Invalid credentials");
        }
    }


    /**
     * Displays regular user menu with operational options
     */
    private void showUserMenu() 
    {
        System.out.println("\n--- User Menu ---");
        System.out.println("1. Search Books");
        System.out.println("2. Popular Books");
        System.out.println("3. Personalized Recommendations");
        System.out.println("4. Collection List (Coming Soon)");
        System.out.println("5. Borrow Books");
        System.out.println("6. Account Settings (Coming Soon)");
        System.out.println("7. Log Out");
        
        int intChoice = readInt("Select operation: ", 1, 7);

        switch (intChoice) 
        {
            case 1: 
                performSearch(); 
                break;
                
            case 2: 
                showPopularBooks(); 
                break;
                
            case 3: 
                showPersonalizedRecommendations(); 
                break;
                
            case 4: 
                System.out.println("\nFeature coming soon"); 
                break;
                
            case 5: 
                borrowBook(); 
                break;
                
            case 6: 
                System.out.println("\nFeature coming soon"); 
                break;
                
            case 7:
                objCurrentUser = null;
                System.out.println("\nSuccessfully logged out");
        }
    }


    /**
     * Displays administrator menu with reporting options
     */
    private void showAdminMenu() 
    {
        System.out.println("\n--- Administrator Console ---");
        System.out.println("1. Popular Books Report");
        System.out.println("2. Author Popularity Analysis");
        System.out.println("3. Genre Usage Statistics");
        System.out.println("4. User Activity Report");
        System.out.println("5. Export Reports (Coming Soon)");
        System.out.println("6. Return to Login");
        
        int intChoice = readInt("Select operation: ", 1, 6);

        switch (intChoice) 
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
                System.out.println("\nExport feature coming soon");
                break;
                
            case 6:
                objCurrentUser = null;
                isAdmin = false;
                System.out.println("\nReturned to login screen");
        }
    }


    /**
     * Performs book search using keywords
     */
    private void performSearch() 
    {
        String strKeyword = readInputLine("Enter search keywords: ");
        ArrayList<Book> bookList = searchService.smartSearch(strKeyword);

        System.out.println("\nFound " + bookList.size() + " books:");
        for (Book book : bookList) 
        {
            System.out.println("  - " + book);
        }
    }


    /**
     * Displays top popular books
     */
    private void showPopularBooks() 
    {
        ArrayList<Book> popularBookList = bookDatabase.getPopularBooks(10);

        System.out.println("\nTOP 10 Popular Books");
        for (Book book : popularBookList) 
        {
            System.out.println("  - " + book);
        }
    }


    /**
     * Displays personalized recommendations
     */
    private void showPersonalizedRecommendations() 
    {
        ArrayList<Book> recommendationList = (ArrayList<Book>) recommendationEngine.generateRecommendations(
            objCurrentUser.getStrUsername(), 5);

        System.out.println("\nRecommended For You:");
        for (Book book : recommendationList) 
        {
            System.out.println("  - " + book);
        }
    }


    /**
     * Handles book borrowing process
     */
    private void borrowBook() 
    {
        String strBookId = readInputLine("Enter book ID to borrow: ");
        if (bookDatabase.borrowBook(strBookId)) 
        {
            int intRating = readInt("Rate this book (1-5): ", 1, 5);
            recommendationEngine.addRating(
                objCurrentUser.getStrUsername(), 
                strBookId, 
                intRating
            );
            System.out.println("\nBorrowing successful!");
        } 
        else 
        {
            System.out.println("\nBorrowing failed: Book unavailable or invalid ID");
        }
    }


    /**
     * Reads validated integer within range
     * @param strPrompt - message to display
     * @param intMin - inclusive lower bound
     * @param intMax - inclusive upper bound
     * @return - validated integer
     */
    private int readInt(String strPrompt, int intMin, int intMax) 
    {
        while (true) 
        {
            try 
            {
                System.out.print(strPrompt);
                int intValue = Integer.parseInt(input.nextLine());
                
                if (intValue >= intMin && intValue <= intMax) 
                {
                    return intValue;  // Valid input
                }
                
                System.out.println("Please enter a number between " + intMin + " and " + intMax);
            } 
            catch (NumberFormatException e) 
            {
                System.out.println("Invalid input. Please enter integers only");
            }
        }
    }


    /**
    * Reads trimmed input line
    * @param strPrompt message to display
    * @return user input string
    **/
    private String readInputLine(String strPrompt) 
    {
        System.out.print(strPrompt);
        return input.nextLine().trim();  // Remove leading/trailing whitespace
    }
}