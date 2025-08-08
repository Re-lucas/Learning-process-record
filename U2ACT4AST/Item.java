package U2ACT4AST;

/**
 * File: Item.java
 * Author: Lucas Wu
 * Date: 2025-08-08
 *
 * Description: Represents an item in a store inventory.
 * This class encapsulates properties such as SKU, name, category, quantity,
**/
import java.text.DecimalFormat;

public class Item {
    // Item properties
    private String strSku;
    private String strName;
    private String strCategory;
    private int intQuantity;
    private int intMinQuantity;
    private double dblVendorPrice;
    private double dblMarkupPercentage;
    private double dblRegularPrice;
    private double dblCurrentDiscount;
    private double dblCurrentPrice;
    
    // Decimal format for prices
    private static final DecimalFormat dblFormat = new DecimalFormat("0.00");


    /**
     * Constructor to initialize an Item object
     * @param strSku - Stock Keeping Unit
     * @param strName - Item name
     * @param strCategory - Item category
     * @param intQuantity - Current stock quantity
     * @param intMinQuantity - Minimum stock quantity
     * @param dblVendorPrice - Price from vendor
     * @param dblMarkupPercentage - Markup percentage
     * @param dblRegularPrice - Regular selling price
     * @param dblCurrentDiscount - Current discount on the item
     * @param dblCurrentPrice - Current selling price after discount
     */
    public Item(String strSku, String strName, String strCategory, int intQuantity, 
                int intMinQuantity, double dblVendorPrice, double dblMarkupPercentage, 
                double dblRegularPrice, double dblCurrentDiscount, double dblCurrentPrice) {
        this.strSku = strSku;
        this.strName = strName;
        this.strCategory = strCategory;
        this.intQuantity = intQuantity;
        this.intMinQuantity = intMinQuantity;
        this.dblVendorPrice = dblVendorPrice;
        this.dblMarkupPercentage = dblMarkupPercentage;
        this.dblRegularPrice = dblRegularPrice;
        this.dblCurrentDiscount = dblCurrentDiscount;
        this.dblCurrentPrice = dblCurrentPrice;
    }


    /**
     * Accessor methods
     * @return - respective property value
     */
    public String getSku() 
    { 
        return strSku; 
    }


    /**
     * Get item name
     * @return Item name
     */
    public String getName() 
    { 
        return strName; 
    }


    /**
     * Get item category
     * @return - Item category
     */
    public String getCategory() 
    { 
        return strCategory; 
    }


    /**
     * Get item quantity
     * @return - Item quantity
     */
    public int getQuantity() 
    { 
        return intQuantity; 
    }


    /**
     * Get minimum quantity
     * @return - Minimum quantity
     */
    public int getMinQuantity() 
    { 
        return intMinQuantity; 
    }


    /**
     * Get vendor price
     * @return - Vendor price
     */
    public double getVendorPrice() 
    { 
        return dblVendorPrice; 
    }


    /**
     * Get markup percentage
     * @return - Markup percentage
     */
    public double getMarkupPercentage() 
    { 
        return dblMarkupPercentage; 
    }


    /**
     * Get regular price
     * @return - Regular price
     */
    public double getRegularPrice() 
    { 
        return dblRegularPrice; 
    }


    /**
     * Get current discount
     * @return - Current discount
     */
    public double getCurrentDiscount() 
    {
         return dblCurrentDiscount; 
    }


    /**
     * Get current price
     * @return - Current price
     */
    public double getCurrentPrice() 
    { 
        return dblCurrentPrice; 
    }

    /**
     * Mutator methods
     * @param strSku - Stock Keeping Unit
     * @param strName - Item name
     * @param strCategory - Item category
     * @param intQuantity - Current stock quantity
     * @param intMinQuantity - Minimum stock quantity
     * @param dblVendorPrice - Price from vendor
     * @param dblMarkupPercentage - Markup percentage
     * @param dblRegularPrice - Regular selling price
     * @param dblCurrentDiscount - Current discount on the item
     * @param dblCurrentPrice - Current selling price after discount
     */
    public void setSku(String strSku) { this.strSku = strSku; }
    public void setName(String strName) { this.strName = strName; }
    public void setCategory(String strCategory) { this.strCategory = strCategory; }
    public void setQuantity(int intQuantity) { this.intQuantity = intQuantity; }
    public void setMinQuantity(int intMinQuantity) { this.intMinQuantity = intMinQuantity; }
    public void setVendorPrice(double dblVendorPrice) { this.dblVendorPrice = dblVendorPrice; }
    public void setMarkupPercentage(double dblMarkupPercentage) { this.dblMarkupPercentage = dblMarkupPercentage; }
    public void setRegularPrice(double dblRegularPrice) { this.dblRegularPrice = dblRegularPrice; }
    public void setCurrentDiscount(double dblCurrentDiscount) { this.dblCurrentDiscount = dblCurrentDiscount; }
    public void setCurrentPrice(double dblCurrentPrice) { this.dblCurrentPrice = dblCurrentPrice; }

    /**
     * Returns a CSV string representation of the item
     * @return - CSV formatted string of item properties
     */
    @Override
    public String toString() {
        return strSku + "," + strName + "," + strCategory + "," + 
               intQuantity + "," + intMinQuantity + "," + 
               dblFormat.format(dblVendorPrice) + "," + 
               dblFormat.format(dblMarkupPercentage) + "," + 
               dblFormat.format(dblRegularPrice) + "," + 
               dblFormat.format(dblCurrentDiscount) + "," + 
               dblFormat.format(dblCurrentPrice);
    }
}