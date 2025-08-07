/**
 * File: Shape.java
 * Author: Lucas Wu
 * Date: 2025-08-07
 *
 * Description: Abstract base class for all tile shapes.
 * Defines shared properties and abstract methods.
 * This class is part of a tile management system.
 * It provides a common interface for different tile shapes.
**/

public abstract class Shape 
{
    protected static double dblUnitPrice;  // Global price per unit area
    protected double[] dblDimensionsArr;   // Shape-specific dimensions

    // Static properties for unit management
    public static String strLengthUnit = "meters";  
    public static String strAreaUnit = "square meters";  
    public static double dblConversionFactor = 1.0;  
    
    /**
     * Sets the unit price for all shapes.
     * @param dblPrice Price per unit area
     * @return True if successful, false otherwise
     */
    public abstract double getArea();
    
    /**
     * Sets the unit price for all shapes.
     * @param dblPrice Price per unit area
     * @return True if successful, false otherwise
     */
    @Override
    public abstract String toString();
}