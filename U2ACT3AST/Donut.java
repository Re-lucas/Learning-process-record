package U2ACT3AST;
/**
 * File: Donut.java
 * Author: Lucas Wu
 * Date: 2025-08-07
 *
 * Description: Represents donut-shaped tiles.
 * Extends the Shape class to provide specific implementations for donuts.
 * This class includes methods to calculate area and provide string representation.
 * It is part of a tile management system that handles different tile shapes.
**/
public class Donut extends Shape 
{
    /**
     * Creates new Donut instance
     * @param dblOuterRadius Outer radius
     * @param dblInnerRadius Inner radius
    */
    public Donut(double dblOuterRadius, double dblInnerRadius) 
    {
        dblDimensionsArr = new double[]{dblOuterRadius, dblInnerRadius};
    }
    
    
    /**
     * Calculates the area of the donut.
     * @return Area of the donut
     * Area is calculated as π * (R² - r²)
     * where R is the outer radius and r is the inner radius
     */
    @Override
    public double getArea() 
    {
        double r1 = dblDimensionsArr[0];
        double r2 = dblDimensionsArr[1];
        return Math.PI * (r1*r1 - r2*r2);  // Area = π(R² - r²)
    }
    

    /**
     * Returns a string representation of the donut.
     * @return String description of the donut
     * Includes outer radius, inner radius, and area information
     */
    @Override
    public String toString() 
    {
        return String.format("Donut: Outer R=%.2f, Inner R=%.2f, Area=%.2f",
                            dblDimensionsArr[0], dblDimensionsArr[1], getArea());
    }
}