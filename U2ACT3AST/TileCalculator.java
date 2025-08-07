/**
 * File: TileCalculator.java
 * Author: Lucas Wu
 * Date: 2025-08-07
 *
 * Description: Main application for tile price calculation.
 * Provides menu-driven interface for managing floor tile configurations.
 * This program allows users to create, delete, and view various tile shapes,
 * set a global unit price, and calculate total area and cost of tiles.
 * Key features:
 * - Create/delete various tile shapes
 * - Set global unit price
 * - Calculate total area and cost
 * - Clear all shapes
**/
import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;

public class TileCalculator 
{
    private static ArrayList<Shape> arrShapeList = new ArrayList<>();  // Shape storage
    private static Scanner input = new Scanner(System.in);              // Standard scanner
    
    
    public static void main(String[] args) 
    {
        int intChoice = 0;  // User menu choice
        
        do 
        {
            clearConsole();
            printMenu();
            
            try 
            {
                intChoice = input.nextInt();
                input.nextLine();  // Consume newline
                
                // Handle menu choice
                switch(intChoice) 
                {
                    case 1: createShape(); break;   // Create new shape
                    case 2: deleteShape(); break;   // Delete existing shape
                    case 3: displayShapes(); break; // View all shapes
                    case 4: setUnitPrice(); break;  // Set unit price for tiles
                    case 5: calculateTotalCost(); break;    // Calculate total cost of tiles
                    case 6: calculateTotalArea(); break;    // Calculate total area of tiles
                    case 7: clearShapes(); break;   // Clear all shapes from inventory
                    case 8: setMeasurementUnits(); break; // Set measurement units
                    case 9: System.out.println("Exiting program..."); break; // Exit program
                    default: System.out.println("Invalid choice. Try 1-9."); // Handle invalid input
                }
                
                // Pause after non-exit operations
                if(intChoice != 9) 
                {
                    System.out.print("\nPress Enter to continue...");
                    input.nextLine();
                }
            } 
            catch(InputMismatchException e) 
            {
                System.out.println("Error: Numbers only. Please try again.");
                input.nextLine();  // Clear invalid input
            }
        } while(intChoice != 9);
    }


    /**
     * Sets measurement units for length and area
     * Options: Meters or Feet
     * Updates static properties in Shape class
     * @return None - units are set in Shape class
     */
    private static void setMeasurementUnits() 
    {
        System.out.println("\n--- MEASUREMENT UNITS ---");
        System.out.println("1. Meters (m)");
        System.out.println("2. Feet (ft)");
        System.out.print("Select unit system (1-2): ");
        
        try 
        {
            int intUnitChoice = input.nextInt();
            input.nextLine();  // Consume newline
            
            if(intUnitChoice == 1) 
            {
                Shape.strLengthUnit = "meters";
                Shape.strAreaUnit = "square meters";
                Shape.dblConversionFactor = 1.0;
                System.out.println("Measurement units set to meters");
            } 
            else if(intUnitChoice == 2) 
            {
                Shape.strLengthUnit = "feet";
                Shape.strAreaUnit = "square feet";
                Shape.dblConversionFactor = 0.3048; // 1 foot = 0.3048 meters
                System.out.println("Measurement units set to feet");
            } 
            else 
            {
                System.out.println("Invalid choice. Units unchanged.");
            }
        } 
        catch(InputMismatchException e) 
        {
            System.out.println("Invalid input. Numbers only.");
            input.nextLine();
        }
    }
    
    
    /**
     * @return - Prints the main menu options
     */
    private static void printMenu() 
    {
        System.out.println("===== TILE CALCULATOR MENU =====");
        System.out.println("1. ADD New Shape");
        System.out.println("2. REMOVE Existing Shape");
        System.out.println("3. VIEW All Shapes");
        System.out.println("4. SET Unit Price");
        System.out.println("5. CALCULATE Total Cost");
        System.out.println("6. CALCULATE Total Area");
        System.out.println("7. CLEAR All Shapes");
        System.out.println("8. SET Measurement Units (Current: " + Shape.strLengthUnit + ")");
        System.out.println("9. EXIT Program");
        System.out.print("Enter your choice (1-9): ");
    }
    
    
    /**
     * Clears the console by printing empty lines
     * This is a simple way to simulate console clearing
     * @return None - console is cleared visually
     */
    private static void clearConsole() 
    {
        for(int i = 0; i < 50; i++) 
        {
            System.out.println();
        }
    }
    
    
    /**
     * Creates a new shape based on user input
     * Prompts user for shape type and dimensions
     * Validates dimensions before creating shape
     * @return None - new shape is added to arrShapeList
     */
    private static void createShape() 
    {
        System.out.println("\n--- AVAILABLE SHAPE TYPES ---");
        System.out.println("1. Rectangle");
        System.out.println("2. Parallelogram");
        System.out.println("3. Triangle");
        System.out.println("4. Circle");
        System.out.println("5. Donut");
        System.out.println("6. Hexagon (Custom 1)");
        System.out.println("7. Ellipse (Custom 2)");
        System.out.print("Select shape type (1-7): ");
        
        try 
        {
            int intType = input.nextInt();
            Shape newShape = null;
            
            // Prompt for dimensions based on shape type
            String unitPrompt = " (in " + Shape.strLengthUnit + "): ";
            
            switch(intType) 
            {
                case 1:  // Rectangle
                    System.out.print("Enter length" + unitPrompt);
                    double dblLength = input.nextDouble();
                    System.out.print("Enter width" + unitPrompt);
                    double dblWidth = input.nextDouble();
                    if(validateDimensions(dblLength, dblWidth)) 
                        newShape = new Rectangle(dblLength, dblWidth);
                    break;
                    
                case 2:  // Parallelogram
                    System.out.print("Enter base" + unitPrompt);
                    double dblBase = input.nextDouble();
                    System.out.print("Enter height" + unitPrompt);
                    double dblHeight = input.nextDouble();
                    if(validateDimensions(dblBase, dblHeight)) 
                        newShape = new Parallelogram(dblBase, dblHeight);
                    break;
                    
                case 3:  // Triangle
                    System.out.print("Enter base" + unitPrompt);
                    double dblTriBase = input.nextDouble();
                    System.out.print("Enter height" + unitPrompt);
                    double dblTriHeight = input.nextDouble();
                    if(validateDimensions(dblTriBase, dblTriHeight)) 
                        newShape = new Triangle(dblTriBase, dblTriHeight);
                    break;
                    
                case 4:  // Circle
                    System.out.print("Enter radius" + unitPrompt);
                    double dblRadius = input.nextDouble();
                    if(validateDimensions(dblRadius)) 
                        newShape = new Circle(dblRadius);
                    break;
                    
                case 5:  // Donut
                    System.out.print("Enter outer radius" + unitPrompt);
                    double dblOuter = input.nextDouble();
                    System.out.print("Enter inner radius" + unitPrompt);
                    double dblInner = input.nextDouble();
                    if(validateDimensions(dblOuter, dblInner)) 
                        newShape = new Donut(dblOuter, dblInner);
                    break;
                    
                case 6:  // Hexagon (Custom 1)
                    System.out.print("Enter side length" + unitPrompt);
                    double dblSide = input.nextDouble();
                    if(validateDimensions(dblSide)) 
                        newShape = new CustomTile1(dblSide);
                    break;
                    
                case 7:  // Ellipse (Custom 2)
                    System.out.print("Enter major axis" + unitPrompt);
                    double dblMajor = input.nextDouble();
                    System.out.print("Enter minor axis" + unitPrompt);
                    double dblMinor = input.nextDouble();
                    if(validateDimensions(dblMajor, dblMinor)) 
                        newShape = new CustomTile2(dblMajor, dblMinor);
                    break;
                    
                default:
                    System.out.println("Invalid shape type");
            }
            
            if(newShape != null) 
            {
                arrShapeList.add(newShape);
                System.out.println("Shape added successfully");
            }
        } 
        catch(InputMismatchException e) 
        {
            System.out.println("Invalid input. Numbers only.");
            input.nextLine();
        }
    }
    
    
    /**
     * Validates dimensions to ensure they are greater than zero
     * @param dblValues Variable number of dimension values
     * @return true if all dimensions are valid, false otherwise
     */
    private static boolean validateDimensions(double... dblValues) 
    {
        for(double val : dblValues) 
        {
            if(val <= 0) 
            {
                System.out.println("Dimensions must be greater than zero");
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * Deletes a shape based on user input
     * Displays current shapes and prompts for index to delete
     * Validates index before removing shape
     * @return True if deletion was successful, false otherwise
     */
    private static void deleteShape() 
    {
        if(arrShapeList.isEmpty()) 
        {
            System.out.println("No shapes to delete");
            return;
        }
        
        displayShapes();
        System.out.print("Enter index to delete: ");
        
        try 
        {
            int intIndex = input.nextInt();
            if(intIndex >= 0 && intIndex < arrShapeList.size()) 
            {
                arrShapeList.remove(intIndex);
                System.out.println("Shape removed");
            } 
            else 
            {
                System.out.println("Invalid index");
            }
        } 
        catch(InputMismatchException e) 
        {
            System.out.println("Invalid input. Numbers only.");
            input.nextLine();
        }
    }
    
    
    /**
     * Displays all shapes in inventory
     * Lists each shape with its index and details
     * If no shapes exist, informs the user
     * @return None - shapes are printed to consoles
     */
    private static void displayShapes() 
    {
        if(arrShapeList.isEmpty()) 
        {
            System.out.println("No shapes in inventory");
            return;
        }
        
        System.out.println("\n--- CURRENT SHAPES ---");
        for(int i = 0; i < arrShapeList.size(); i++) 
        {
            System.out.println("[" + i + "] " + arrShapeList.get(i));
        }
    }
    
    
    /**
     * Sets the unit price for all shapes
     * Prompts user for price and validates input
     * Updates global price if valid
     * @return None - unit price is set in Shape class
     */
    private static void setUnitPrice() 
    {
        System.out.print("Enter price per " + Shape.strAreaUnit + ": ");
        
        try 
        {
            double dblPrice = input.nextDouble();
            if(dblPrice > 0) 
            {
                Shape.dblUnitPrice = dblPrice;
                System.out.println("Unit price updated to $" + dblPrice + " per " + Shape.strAreaUnit);
            } 
            else 
            {
                System.out.println("Price must be positive");
            }
        } 
        catch(InputMismatchException e) 
        {
            System.out.println("Invalid input. Numbers only.");
            input.nextLine();
        }
    }
    
    
    /**
     * Calculates total cost of all shapes
     * Iterates through shape list, calculates individual costs
     * Displays detailed cost breakdown and total cost
     * Requires unit price to be set beforehand
     * @return Total cost of all shapes
     */
    private static void calculateTotalCost() 
    {
        if(Shape.dblUnitPrice == 0) 
        {
            System.out.println("Unit price not set. Use option 4 first.");
            return;
        }
        
        System.out.println("\n--- COST BREAKDOWN ---");
        System.out.printf("Unit Price: $%.2f per %s%n", Shape.dblUnitPrice, Shape.strAreaUnit);
        System.out.println("----------------------");
        
        double dblTotal = 0;
        int intCount = 1;
        
        // Iterate through shapes and calculate costs
        for(Shape shape : arrShapeList) 
        {
            double dblShapeCost = shape.getArea() * Shape.dblUnitPrice;
            dblTotal += dblShapeCost;
            
            // Get shape type name
            String strShapeType = shape.getClass().getSimpleName();

            // Map custom class names to friendly names
            if(strShapeType.equals("CustomTile1")) strShapeType = "Hexagon";
            else if(strShapeType.equals("CustomTile2")) strShapeType = "Ellipse";
            
            System.out.printf("%2d. %-15s Area: %-8.2f %-15s Cost: $%.2f%n",
                              intCount++,
                              strShapeType,
                              shape.getArea(),
                              Shape.strAreaUnit,
                              dblShapeCost);
        }
        
        System.out.println("----------------------");
        System.out.printf("TOTAL COST: $%.2f%n", dblTotal);
    }
    
    
    
    /**
     * Calculates total area of all shapes
     * Iterates through shape list and sums areas
     * Displays total area formatted to two decimal places
     * @return Total area of all shapes
     */
    private static void calculateTotalArea() 
    {
        double dblTotalArea = 0;
        for(Shape shape : arrShapeList) 
        {
            dblTotalArea += shape.getArea();
        }
        System.out.printf("Total area: %.2f %s%n", dblTotalArea, Shape.strAreaUnit);
    }
    
    
    /**
     * Clears all shapes from the inventory
     * Resets the shape list and informs the user
     * This method is used to reset the application state
     * @return None - all shapes are removed from the list
     */
    private static void clearShapes() 
    {
        arrShapeList.clear();
        System.out.println("All shapes removed");
    }
}