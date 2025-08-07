/**
 * File: Rectangle.java
 * Author: Lucas Wu
 * Date: 2025-08-07
 *
 * Description: Represents rectangular tiles.
 * Extends the Shape class to provide specific implementations for rectangles.
 * This class includes methods to calculate area and provide string representation.
**/
public class Rectangle extends Shape 
{
    /**
     * Creates new Rectangle instance
     * @param dblLength Length of rectangle
     * @param dblWidth Width of rectangle
     * @return New Rectangle object
     */
    public Rectangle(double dblLength, double dblWidth) 
    {
        dblDimensionsArr = new double[]{dblLength, dblWidth};
    }
    

    /**
     * Calculates the area of the rectangle.
     * @return Area of the rectangle
     */
    @Override
    public double getArea() 
    {
        return dblDimensionsArr[0] * dblDimensionsArr[1];  // Area = length * width
    }
    

    /**
     * Returns a string representation of the rectangle.
     * @return String description of the rectangle
     */
    @Override
    public String toString() 
    {
        return String.format("Rectangle: Length=%.2f, Width=%.2f, Area=%.2f",
                            dblDimensionsArr[0], dblDimensionsArr[1], getArea());
    }
}