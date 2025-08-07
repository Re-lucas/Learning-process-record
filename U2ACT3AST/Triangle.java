/**
 * File: Triangle.java
 * Author: Lucas Wu
 * Date: 2025-08-07
 *
 * Description: Represents triangular tiles.
 * Extends the Shape class to provide specific implementations for triangles.
 * This class includes methods to calculate area and provide string representation.
 * It is part of a tile management system that handles different tile shapes.
**/
public class Triangle extends Shape 
{
    /**
     * Creates new Triangle instance
     * @param dblBase Base length
     * @param dblHeight Height
     * This constructor initializes the dimensions of the triangle.
     */
    public Triangle(double dblBase, double dblHeight) 
    {
        dblDimensionsArr = new double[]{dblBase, dblHeight};
    }
    

    /**
     * Calculates the area of the triangle.
     * @return Area of the triangle
     * Area is calculated as (base * height) / 2
     * This method overrides the abstract getArea method from the Shape class.
     * It provides the specific implementation for triangle area calculation.
     */
    @Override
    public double getArea() 
    {
        return 0.5 * dblDimensionsArr[0] * dblDimensionsArr[1];  // Area = (base * height)/2
    }
    

    /**
     * Returns a string representation of the triangle.
     * @return String description of the triangle
     * This method overrides the abstract toString method from the Shape class.
     * It provides a formatted string with base, height, and area information.
     */
    @Override
    public String toString() 
    {
        return String.format("Triangle: Base=%.2f, Height=%.2f, Area=%.2f",
                            dblDimensionsArr[0], dblDimensionsArr[1], getArea());
    }
}