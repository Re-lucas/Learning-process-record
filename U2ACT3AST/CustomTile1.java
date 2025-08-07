/**
 * File: CustomTile1.java
 * Author: Lucas Wu
 * Date: 2025-08-07
 *
 * Description: Custom hexagonal tile implementation.
 * Extends the Shape class to provide specific implementations for hexagonal tiles.
 * This class includes methods to calculate area and provide string representation.
**/
public class CustomTile1 extends Shape 
{
    /**
     * Creates new hexagon tile
     * @param dblSide Side length
     */
    public CustomTile1(double dblSide) 
    {
        dblDimensionsArr = new double[]{dblSide};
    }
    

    /**
     * Calculates the area of the hexagon.
     * @return Area of the hexagon
     * Area is calculated as (3√3/2) * side²
     */
    @Override
    public double getArea() 
    {
        return (3 * Math.sqrt(3) * Math.pow(dblDimensionsArr[0], 2)) / 2;  // Area = (3√3/2)s²
    }
    

    /**
     * Returns a string representation of the hexagon.
     * @return String description of the hexagon
     * Includes side length and area information
     */
    @Override
    public String toString() 
    {
        return String.format("Hexagon: Side=%.2f, Area=%.2f", 
                            dblDimensionsArr[0], getArea());
    }
}