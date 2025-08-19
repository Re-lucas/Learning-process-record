/**
 * File: User.java
 * Author: Lucas Wu
 * Date: 2025-08-16
 *
 * Description:
 *  - Represents an application user with credentials and security information
 *  - Provides methods for authenticating password and validating security answer
 *  - Allows updating password and managing user preferences
 *  - Overrides toString to display username and preference summary
 * 
 *  Work Log:
 *  2025-08-18(Lucas Wu):
 *    - Added comprehensive JavaDoc comments for all methods and fields
 *    - Refactored variable names for consistency and clarity
 *    - Enhanced method documentation for authentication and validation
 *  
 *  2025-08-17 (Lucas Wu):
 *    - Conducted thorough testing of all user operations:
 *        - Password authentication
 *        - Security answer validation
 *        - Preference management
 *    - Verified correct behavior of toString representation
 *  
 *  2025-08-16 (Lucas Wu):
 *    - Initial implementation of core user model:
 *        - User credential storage (username/password)
 *        - Security question/answer mechanism
 *        - Preference management system
 *        - Authentication and validation methods**/
package model;

public class User 
{
    private String strUsername;    // User's unique username
    private String strPassword;    // User's password
    private String strSecurityQ;   // Security question text
    private String strSecurityA;   // Security question answer
    private String strPreferences; // User's preference string


    /**
     * Constructs a new User with provided credentials and security info
     * @param strUsername   - the user's username
     * @param strPassword   - the user's password
     * @param strSecurityQ  - the security question
     * @param strSecurityA  - the security question answer
     */
    public User(String strUsername, String strPassword, String strSecurityQ, String strSecurityA) 
    {
        this.strUsername    = strUsername;
        this.strPassword    = strPassword;
        this.strSecurityQ   = strSecurityQ;
        this.strSecurityA   = strSecurityA;
        this.strPreferences = "";  // Default: no preferences
    }


    /**
     * Gets the user's username
     * @return - the username
     */
    public String getStrUsername() 
    {
        return strUsername;
    }


    /**
     * Gets the user's password
     * @return - the password
     */
    public String getStrPassword() 
    {
        return strPassword;
    }


    /**
     * Gets the security question for password recovery
     * @return - the security question
     */
    public String getStrSecurityQ() 
    {
        return strSecurityQ;
    }


    /**
     * Gets the security answer for password recovery
     * @return - the security answer
     */
    public String getStrSecurityA() 
    {
        return strSecurityA;
    }


    /**
     * Gets the user's preferences
     * @return - the preferences string or empty if none
     */
    public String getStrPreferences() 
    {
        return strPreferences;
    }


    /**
     * Updates the user's password
     * @param strPassword - the new password to set
     */
    public void setStrPassword(String strPassword) 
    {
        this.strPassword = strPassword;
    }


    /**
    * Updates the user's preference string
    * @param strPreferences - the new preferences
    **/
    public void setStrPreferences(String strPreferences) 
    {
        this.strPreferences = strPreferences;
    }


    /**
     * Authenticates the user by comparing input password
     * @param strInputPassword - the password provided for authentication
     * @return - true if passwords match; false otherwise
     */
    public boolean authenticate(String strInputPassword) 
    {
        return strPassword.equals(strInputPassword);  // Verify exact match
    }


    /**
     * Validates the user's security answer (case-insensitive)
     * @param strInputAnswer - the answer provided for security question
     * @return - true if answers match; false otherwise
     */
    public boolean validateSecurityAnswer(String strInputAnswer) 
    {
        return strSecurityA.equalsIgnoreCase(strInputAnswer);  // Ignore case differences
    }


    /**
     * Returns a formatted string representation of the user
     * @return - string showing username and preferences summary
     */
    @Override
    public String toString() 
    {
        String strPrefsDisplay = strPreferences.isEmpty() ? "None" : strPreferences;  // Fallback display
        return String.format("User[%s], Prefs: %s", strUsername, strPrefsDisplay);
    }
}