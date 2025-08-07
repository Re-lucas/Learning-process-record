/**
 * File: Parallelogram.java
 * Author: Lucas Wu
 * Date: 2025-08-07
 *
 * Description: Represents parallelogram tiles.
 * Extends the Shape class to provide specific implementations for parallelograms.
 * This class includes methods to calculate area and provide string representation.
**/
public class Parallelogram extends Shape 
{
    /**
     * Creates new Parallelogram instance
     * @param dblBase Base length
     * @param dblHeight Height
     */
    public Parallelogram(double dblBase, double dblHeight) 
    {
        dblDimensionsArr = new double[]{dblBase, dblHeight};
    }
    

    /**
     * Calculates the area of the parallelogram.
     * @return Area of the parallelogram
     * Area is calculated as base * height
     */
    @Override
    public double getArea() 
    {
        return dblDimensionsArr[0] * dblDimensionsArr[1];  // Area = base * height
    }
    

    /**
     * Returns a string representation of the parallelogram.
     * @return String description of the parallelogram
     * Includes base, height, and area information
     */
    @Override
    public String toString() 
    {
        return String.format("Parallelogram: Base=%.2f, Height=%.2f, Area=%.2f",
                            dblDimensionsArr[0], dblDimensionsArr[1], getArea());
    }
}