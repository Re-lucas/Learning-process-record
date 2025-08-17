import java.util.ArrayList;
import java.util.Scanner;

import model.User;
import model.Book;
import service.UserManager;
import service.BookDatabase;
import service.RecommendationEngine;
import service.SearchService;
import service.ReportGenerator;

/**
 * File: Main.java
 * Author: [团队名称]
 * Date: [当前日期]
 *
 * Description: 程序入口，管理用户界面与系统流程
 */
public class Main 
{
    // 系统组件
    private UserManager       userManager;
    private BookDatabase      bookDatabase;
    private RecommendationEngine recommendationEngine;
    private SearchService     searchService;
    private ReportGenerator   reportGenerator;

    private User    currentUserObj;   // 当前登录用户
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
        String user = readInputLine("用户名: ");
        String pwd  = readInputLine("密码: ");

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
        System.out.println("\n--- 密码恢复 ---");
        String user = readInputLine("用户名: ");
        String a    = readInputLine("安全问题答案: ");

        String newPwd = userManager.recoverPassword(user, a);
        if (newPwd != null) {
            System.out.println("密码已重置！新密码：" + newPwd);
            System.out.println("请登录后尽快修改密码");
        } else {
            System.out.println("恢复失败：用户名或答案不正确");
        }
    }

    private void showUserMenu() 
    {
        System.out.println("\n--- 用户菜单 ---");
        System.out.println("1. 搜索图书");
        System.out.println("2. 热门书籍");
        System.out.println("3. 个性化推荐");
        System.out.println("4. 收藏列表（暂未实现）");
        System.out.println("5. 借阅图书");
        System.out.println("6. 账户设置（暂未实现）");
        System.out.println("7. 退出登录");
        int choice = readInt("请选择操作: ", 1, 7);

        switch (choice) 
        {
            case 1: doSearch();      break;
            case 2: showPopular();   break;
            case 3: showRecommendations(); break;
            case 4: System.out.println("功能待扩展"); break;
            case 5: doBorrow();      break;
            case 6: System.out.println("功能待扩展"); break;
            case 7:
                currentUserObj = null;
                System.out.println("已退出登录");
        }
    }

    private void showAdminMenu() 
    {
        System.out.println("\n--- 管理员控制台 ---");
        System.out.println("1. 查看热门书籍报告");
        System.out.println("2. 作者受欢迎度分析");
        System.out.println("3. 图书分类统计");
        System.out.println("4. 用户活跃度报告");
        System.out.println("5. 导出报告到文件（暂未实现）");
        System.out.println("6. 返回登录界面");
        int choice = readInt("请选择操作: ", 1, 6);

        switch (choice) 
        {
            case 1: System.out.println(reportGenerator.generatePopularBooksReport(10)); break;
            case 2: System.out.println(reportGenerator.generateAuthorPopularityReport()); break;
            case 3: System.out.println(reportGenerator.generateGenreUsageReport()); break;
            case 4: System.out.println(reportGenerator.generateUserActivityReport()); break;
            case 5: System.out.println("导出功能待实现"); break;
            case 6:
                currentUserObj = null;
                isAdminFlag    = false;
        }
    }

    // === 操作实现示例 ===

    private void doSearch() 
    {
        String kw = readInputLine("输入关键字: ");
        ArrayList<Book> list = searchService.smartSearch(kw);
        System.out.println("找到 " + list.size() + " 本书：");
        for (Book b : list) {
            System.out.println("  - " + b);
        }
    }

    private void showPopular() 
    {
        var list = bookDatabase.getPopularBooks(10);
        System.out.println("热门书籍 TOP10：");
        for (Book b : list) {
            System.out.println("  - " + b);
        }
    }

    private void showRecommendations() 
    {
        var list = recommendationEngine.generateRecommendations(
            currentUserObj.getStrUsername(), 5);
        System.out.println("为您推荐：");
        for (Book b : list) {
            System.out.println("  - " + b);
        }
    }

    private void doBorrow() 
    {
        String id = readInputLine("输入要借阅的书籍 ID: ");
        if (bookDatabase.borrowBook(id)) {
            // 记一条评分示例
            int rate = readInt("您的评分 (1–5): ", 1, 5);
            recommendationEngine.addRating(currentUserObj.getStrUsername(), id, rate);
            System.out.println("借阅成功！");
        } else {
            System.out.println("借阅失败：ID 不存在或不可借");
        }
    }

    // === 工具方法 ===

    private int readInt(String prompt, int min, int max) 
    {
        while (true) 
        {
            try 
            {
                System.out.print(prompt);
                int v = Integer.parseInt(scanner.nextLine());
                if (v >= min && v <= max) return v;
                System.out.println("请输入 " + min + " 到 " + max + " 之间的数字");
            } 
            catch (NumberFormatException e) 
            {
                System.out.println("请输入有效的数字！");
            }
        }
    }

    private String readInputLine(String prompt) 
    {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
