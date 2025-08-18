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
    private UserManager       userManager;
    private BookDatabase      bookDatabase;
    private RecommendationEngine recommendationEngine;
    private SearchService     searchService;
    private ReportGenerator   reportGenerator;

    private User    currentUserObj;   
    private boolean isAdminFlag = false;

    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) 
    {
        new Main().run();
    }

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

    private void initializeSystem() 
    {
        userManager         = new UserManager();
        bookDatabase        = new BookDatabase();
        recommendationEngine= new RecommendationEngine(bookDatabase);
        searchService       = new SearchService(bookDatabase);
        reportGenerator     = new ReportGenerator(bookDatabase, userManager);
        currentUserObj      = null;
    }

    private void showWelcomeScreen() 
    {
        System.out.println("\n\n===== Book Recommendation System =====");
        System.out.println("   Wise Reading · Personality Discovery\n");
    }

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
            case 1: loginUser();    break;
            case 2: registerUser(); break;
            case 3: recoverPassword(); break;
            case 4:
                System.out.println("Thank you for using it. Goodbye!");
                System.exit(0);
        }
    }

    private void loginUser() 
    {
        String user = readInputLine("User name: ");
        String pwd  = readInputLine("Password: ");

        if ("admin".equals(user) && "admin123".equals(pwd)) 
        {
            currentUserObj = new User("admin", "admin123", "", "");
            isAdminFlag    = true;
            System.out.println("The administrator has logged in successfully!");
            return;
        }

        User u = userManager.loginUser(user, pwd);
        if (u == null) {
            System.out.println("Login failed: Incorrect username or password");
        } else {
            currentUserObj = u;
            isAdminFlag    = false;
            System.out.println("Welcome back" + user + "!");
        }
    }

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

    private void recoverPassword() 
    {
        System.out.println("\n--- Password recovery ---");
        String user = readInputLine("User name: ");
        String a    = readInputLine("Answers to safety questions: ");

        String newPwd = userManager.recoverPassword(user, a);
        if (newPwd != null) {
            System.out.println("The password has been reset! New password" + newPwd);
            System.out.println("Please change your password as soon as possible after logging in");
        } else {
            System.out.println("Recovery failed: The username or answer is incorrect");
        }
    }

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
            case 1: doSearch();      break;
            case 2: showPopular();   break;
            case 3: showRecommendations(); break;
            case 4: System.out.println("Functions to be expanded"); break;
            case 5: doBorrow();      break;
            case 6: System.out.println("Functions to be expanded"); break;
            case 7:
                currentUserObj = null;
                System.out.println("Logged out");
        }
    }

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
            case 1: System.out.println(reportGenerator.generatePopularBooksReport(10)); break;
            case 2: System.out.println(reportGenerator.generateAuthorPopularityReport()); break;
            case 3: System.out.println(reportGenerator.generateGenreUsageReport()); break;
            case 4: System.out.println(reportGenerator.generateUserActivityReport()); break;
            case 5: System.out.println("The export function is yet to be implemented"); break;
            case 6:
                currentUserObj = null;
                isAdminFlag    = false;
        }
    }

    private void doSearch() 
    {
        String kw = readInputLine("Enter keywords: ");
        ArrayList<Book> list = searchService.smartSearch(kw);
        System.out.println("Found " + list.size() + " books:");
        for (Book b : list) {
            System.out.println("  - " + b);
        }
    }

    private void showPopular() 
    {
        var list = bookDatabase.getPopularBooks(10);
        System.out.println("TOP10 Popular Books");
        for (Book b : list) {
            System.out.println("  - " + b);
        }
    }

    private void showRecommendations() 
    {
        var list = recommendationEngine.generateRecommendations(
            currentUserObj.getStrUsername(), 5);
        System.out.println("For your recommendation:");
        for (Book b : list) {
            System.out.println("  - " + b);
        }
    }

    private void doBorrow() 
    {
        String id = readInputLine("Enter the ID of the book you want to borrow ");
        if (bookDatabase.borrowBook(id)) {
            int rate = readInt("Your rating (1-5): ", 1, 5);
            recommendationEngine.addRating(currentUserObj.getStrUsername(), id, rate);
            System.out.println("Borrowing successful!");
        } else {
            System.out.println("Borrowing failed: The ID does not exist or cannot be borrowed");
        }
    }


    private int readInt(String prompt, int min, int max) 
    {
        while (true) 
        {
            try 
            {
                System.out.print(prompt);
                int v = Integer.parseInt(scanner.nextLine());
                if (v >= min && v <= max) return v;
                System.out.println("Please enter " + min + " to " + max + " numbers between them");
            } 
            catch (NumberFormatException e) 
            {
                System.out.println("Please enter valid numbers!");
            }
        }
    }

    private String readInputLine(String prompt) 
    {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
