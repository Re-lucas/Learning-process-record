/**
 * File: Circle.java
 * Author: Lucas Wu
 * Date: 2025-08-07
 *
 * Description: Represents circular tiles.
 * Extends the Shape class to provide specific implementations for circles.
 * This class includes methods to calculate area and provide string representation.
 * It is part of a tile management system that handles different tile shapes.
**/
public class Circle extends Shape 
{
    /**
     * Creates new Circle instance
     * @param dblRadius Radius
     */
    public Circle(double dblRadius) 
    {
        dblDimensionsArr = new double[]{dblRadius};
    }
    

    /**
     * Calculates the area of the circle.
     * @return Area of the circle
     * Area is calculated as π * radius²
     */
    @Override
    public double getArea() 
    {
        return Math.PI * dblDimensionsArr[0] * dblDimensionsArr[0];  // Area = πr²
    }
    

    /**
     * Returns a string representation of the circle.
     * @return String description of the circle
     * Includes radius and area information
     */
    @Override
    public String toString() 
    {
        return String.format("Circle: Radius=%.2f, Area=%.2f", 
                            dblDimensionsArr[0], getArea());
    }
}