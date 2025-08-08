package U2ACT4AST;

/**
 * File: MainProgram.java
 * Author: Lucas Wu
 * Date: 2025-08-08
 *
 * Description: Main program for Supermarket Inventory Management System.
 * This program allows users to manage a store's inventory by searching for products,
**/

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.util.regex.*;

public class MainProgram 
{
    // Constants
    private static final String FILENAME = "U2ACT4AST\\inventory.txt";
    private static final int MAX_NAME_LENGTH = 20;
    private static final DecimalFormat dblFormat = new DecimalFormat("0.00");
    
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        ArrayList<Item> itemList = new ArrayList<>();
        
        // Load existing inventory
        loadInventory(itemList);
        
        // Main menu loop
        int intChoice;
        do {
            clearConsole();
            System.out.println("===== Supermarket Inventory Management System =====");
            System.out.println("1. Search Product");
            System.out.println("2. Add Product");
            System.out.println("3. Save Data");
            System.out.println("4. Exit System");
            System.out.print("Please select operation: ");
            
            intChoice = getValidInt(input, 1, 4);
            
            switch(intChoice) 
            {
                case 1:
                    searchItem(itemList, input);
                    break;
                case 2:
                    addItem(itemList, input);
                    break;
                case 3:
                    saveInventory(itemList);
                    System.out.println("\nData saved successfully!");
                    pause(input);
                    break;
                case 4:
                    System.out.println("\nThank you for using the system!");
                    break;
            }
        } while(intChoice != 4);
    }


    /**
     * Loads inventory from file
     * @param itemList - list to populate with items
     * @return void - updates itemList with loaded items
     * If the file does not exist, it initializes an empty inventory.
     */
    private static void loadInventory(ArrayList<Item> itemList) 
    {
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) 
        {
            String strLine;
            while ((strLine = br.readLine()) != null) 
            {
                String[] strItemInfoArr = strLine.split(",");
                if(strItemInfoArr.length == 10) 
                {
                    Item item = new Item(
                        strItemInfoArr[0], strItemInfoArr[1], strItemInfoArr[2],
                        Integer.parseInt(strItemInfoArr[3]), Integer.parseInt(strItemInfoArr[4]),
                        Double.parseDouble(strItemInfoArr[5]), Double.parseDouble(strItemInfoArr[6]),
                        Double.parseDouble(strItemInfoArr[7]), Double.parseDouble(strItemInfoArr[8]),
                        Double.parseDouble(strItemInfoArr[9])
                    );
                    itemList.add(item);
                }
            }
            System.out.println("\nInventory loaded! Items: " + itemList.size());
        } catch (FileNotFoundException e) {
            System.out.println("\nInfo: Creating new inventory...");
        } catch (IOException e) 
        {
            System.out.println("\nError: " + e.getMessage());
        }
        pause(null);
    }


    /**
     * Searches for an item by SKU or name
     * @param itemList - list of items to search
     * @param input - Scanner for user input
     * @return void - displays item details or error message
     */
    private static void searchItem(ArrayList<Item> itemList, Scanner input) 
    {
        clearConsole();
        System.out.println("===== Product Search =====");
        System.out.println("1. Search by SKU");
        System.out.println("2. Search by Name");
        System.out.print("Select search method: ");
        
        int intSearchChoice = getValidInt(input, 1, 2);
        input.nextLine(); 
        
        if(intSearchChoice == 1) 
        {
            System.out.print("\nEnter SKU (format: ABC-1234): ");
            String strSku = input.nextLine().toUpperCase();
            
            boolean isFound = false;
            for(Item item : itemList) 
            {
                if(item.getSku().equals(strSku)) 
                {
                    displayItemDetails(item);
                    isFound = true;
                    break;
                }
            }
            if(!isFound) 
            {
                System.out.println("\nError: SKU not found!");
            }
        } else 
        {
            System.out.print("\nEnter product name: ");
            String strName = input.nextLine();
            
            boolean isFound = false;
            for(Item item : itemList) 
            {
                if(item.getName().equalsIgnoreCase(strName)) 
                {
                    displayItemDetails(item);
                    isFound = true;
                    break;
                }
            }
            if(!isFound) {
                System.out.println("\nError: Product not found!");
            }
        }
        pause(input);
    }


    /**
     * Displays item details
     * @param item - item to display
     * @return void - prints item details to console
     */
    private static void displayItemDetails(Item item) 
    {
        System.out.println("\n===== Product Details =====");
        System.out.println("SKU: " + item.getSku());
        System.out.println("Name: " + item.getName());
        System.out.println("Category: " + item.getCategory());
        System.out.println("Quantity: " + item.getQuantity());
        System.out.println("Min Quantity: " + item.getMinQuantity());
        System.out.println("Supplier Price: $" + dblFormat.format(item.getVendorPrice()));
        System.out.println("Markup: " + dblFormat.format(item.getMarkupPercentage()) + "%");
        System.out.println("Regular Price: $" + dblFormat.format(item.getRegularPrice()));
        System.out.println("Discount: " + dblFormat.format(item.getCurrentDiscount()) + "%");
        System.out.println("Current Price: $" + dblFormat.format(item.getCurrentPrice()));
    }


    /**
     * Adds a new item to the inventory
     * @param itemList - list to add the new item
     * @param input - Scanner for user input
     * @return void - updates itemList with the new item
     */
    private static void addItem(ArrayList<Item> itemList, Scanner input) 
    {
        clearConsole();
        System.out.println("===== Add New Product =====");
        
        // Validate name input
        String strName;
        do {
            System.out.print("Product name (max " + MAX_NAME_LENGTH + " chars): ");
            strName = input.nextLine();
            if (strName.trim().isEmpty()) 
            {
                System.out.println("Error: Name cannot be empty!");
                continue;
            }
            if(strName.length() > MAX_NAME_LENGTH) 
            {
                System.out.println("Error: Name too long!");
            }
        } while(strName.trim().isEmpty() || strName.length() > MAX_NAME_LENGTH);
        

        // Validate category input
        String strCategory;
        do {
            System.out.print("Category (FRUIT/VEGETABLE/MEAT): ");
            strCategory = input.nextLine().toUpperCase();
            if(!strCategory.equals("FRUIT") && !strCategory.equals("VEGETABLE") && 
               !strCategory.equals("MEAT")) 
                {
                System.out.println("Error: Invalid category!");
                }
        } while(!strCategory.equals("FRUIT") && !strCategory.equals("VEGETABLE") && 
                !strCategory.equals("MEAT"));
        
        // Validate numeric inputs
        System.out.print("Quantity: ");
        int intQuantity = getValidInt(input, 0, Integer.MAX_VALUE);
        
        // Validate minimum quantity input
        System.out.print("Min Quantity: ");
        int intMinQuantity = getValidInt(input, 0, Integer.MAX_VALUE);
        
        // Validate vendor price, markup, and discount inputs
        System.out.print("Supplier Price: ");
        double dblVendorPrice = getValidDouble(input, 0, Double.MAX_VALUE);
        
        // Validate markup percentage input
        System.out.print("Markup % (0-100): ");
        double dblMarkupPercentage = getValidDouble(input, 0, 100);
        
        // Validate current discount input
        System.out.print("Discount % (0-100): ");
        double dblCurrentDiscount = getValidDouble(input, 0, 100);
        
        // Calculate regular and current prices
        double dblRegularPrice = dblVendorPrice * (1 + dblMarkupPercentage / 100);
        double dblCurrentPrice = dblRegularPrice * (1 - dblCurrentDiscount / 100);
        
        // Generate SKU
        String strSku = generateSKU(strCategory, itemList);
        
        // Create new item and add to list
        Item newItem = new Item(
            strSku, strName, strCategory, intQuantity, intMinQuantity,
            dblVendorPrice, dblMarkupPercentage, dblRegularPrice,
            dblCurrentDiscount, dblCurrentPrice
        );
        itemList.add(newItem);
        
        System.out.println("\nProduct added! SKU: " + strSku);
        pause(input);
    }

    
    /**
     * Generates a unique SKU based on category
     * @param strCategory - product category
     * @param itemList - current list of items
     * @return - generated SKU in format ABC-1234
     * SKU format: First three letters of category + hyphen + 4-digit number
     */
    private static String generateSKU(String strCategory, ArrayList<Item> itemList) 
    {
        String strPrefix = strCategory.length() >= 3 ? strCategory.substring(0, 3) + "-" : strCategory + "-";
        int intMaxSku = 0;
        
        Pattern pattern = Pattern.compile("^" + strPrefix + "(\\d{4})$");
        
        for(Item item : itemList) 
        {
            if (item.getCategory().equals(strCategory))
            {
                String strSku = item.getSku();
                Matcher matcher = pattern.matcher(strSku);
                if(matcher.matches()) 
                {
                    int intSkuNum = Integer.parseInt(matcher.group(1));
                    if(intSkuNum > intMaxSku) 
                    {
                        intMaxSku = intSkuNum;
                    }
                }
            }
        }
        
        return strPrefix + String.format("%04d", intMaxSku + 1);
    }

    /**
     * Saves inventory to file
     * @param itemList - list of items to save
     * @return void - writes item details to file
     * Each item is saved in a comma-separated format.
     */
    private static void saveInventory(ArrayList<Item> itemList) 
    {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILENAME))) 
        {
            for(Item item : itemList) 
            {
                pw.println(item.toString());
            }
        } catch (IOException e) 
        {
            System.out.println("Error saving: " + e.getMessage());
        }
    }


    /**
     * Validates integer input within a range
     * @param input - Scanner for user input
     * @param min - minimum valid value
     * @param max - maximum valid value
     * @return valid integer input
     * Prompts user until a valid integer within the specified range is entered.
     */
    private static int getValidInt(Scanner input, int min, int max) 
    {
        while (true) 
        {
            try 
            {
                int intValue = input.nextInt();
                input.nextLine();
                if (intValue >= min && intValue <= max) 
                {
                    return intValue;
                }
                System.out.print("Range: " + min + "-" + max + ". Re-enter: ");
            } catch (InputMismatchException e) 
            {
                System.out.print("Invalid integer. Re-enter: ");
                input.nextLine();
            }
        }
    }


    /**
     * Validates double input within a rangew
     * @param input - Scanner for user input
     * @param min - minimum valid value
     * @param max - maximum valid value
     * @return - valid double input
     * Prompts user until a valid double within the specified range is entered.
     */
    private static double getValidDouble(Scanner input, double min, double max) 
    {
        while(true) 
        {
            try 
            {
                double dblValue = input.nextDouble();
                input.nextLine();
                if(dblValue >= min && dblValue <= max) 
                {
                    return dblValue;
                }
                System.out.print("Range: " + min + "-" + max + ". Re-enter: ");
            } catch (InputMismatchException e) 
            {
                System.out.print("Invalid number. Re-enter: ");
                input.nextLine();
            }
        }
    }


    /**
     * Clears the console screen
     * Note: This method may not work in all environments.
     */
    private static void clearConsole() 
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    /**
     * Pauses execution until user presses Enter
     * @param input - Scanner for user input
     * If input is null, it skips waiting for user input.
     */
    private static void pause(Scanner input) 
    {
        if(input != null) 
        {
            System.out.print("\nPress Enter to continue...");
            input.nextLine();
        }
    }
}