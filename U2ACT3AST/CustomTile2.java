package U2ACT3AST;
/**
 * File: CustomTile2.java
 * Author: Lucas Wu
 * Date: 2025-08-07
 *
 * Description: Custom elliptical tile implementation.
 * Extends the Shape class to provide specific implementations for elliptical tiles.
 * This class includes methods to calculate area and provide string representation.
**/
public class CustomTile2 extends Shape 
{
    /**
     * Creates new elliptical tile
     * @param dblMajorAxis Major axis length
     * @param dblMinorAxis Minor axis length
     */
    public CustomTile2(double dblMajorAxis, double dblMinorAxis) 
    {
        dblDimensionsArr = new double[]{dblMajorAxis, dblMinorAxis};
    }
    

    /**
     * Calculates the area of the ellipse.
     * @return Area of the ellipse
     * Area is calculated as π * major * minor / 4
     */
    @Override
    public double getArea() 
    {
        return Math.PI * dblDimensionsArr[0] * dblDimensionsArr[1] / 4;  // Area = πab/4
    }
    

    /**
     * Returns a string representation of the ellipse.
     * @return String description of the ellipse
     * Includes major axis, minor axis, and area information
     */
    @Override
    public String toString() 
    {
        return String.format("Ellipse: Major=%.2f, Minor=%.2f, Area=%.2f",
                            dblDimensionsArr[0], dblDimensionsArr[1], getArea());
    }
}