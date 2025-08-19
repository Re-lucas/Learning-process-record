/**
 * File: UserManager.java
 * Author: Lucas Wu
 * Date: 2025-08-16
 *
 * Description:
 *  - Loads users from CSV storage at initialization
 *  - Allows registration of new users with unique username check
 *  - Authenticates existing users on login
 *  - Recovers forgotten password via security answer and generates temporary password
**/
package service;

import java.util.ArrayList;
import java.util.Random;

import model.User;
import util.FileUtils;

public class UserManager 
{
    private ArrayList<User> userList;  // List of application users


    /**
     * Constructs the UserManager and loads existing users
     */
    public UserManager() 
    {
        userList = FileUtils.loadUsersFromCSV();  // Load stored users
        System.out.println("Loaded " + userList.size() + " users");  // Debug info
    }


    /**
     * Registers a new user with unique username
     * @param strUsername - the desired username
     * @param strPassword - the user's password
     * @param strSecurityQ - the security question text
     * @param strSecurityA - the security question answer
     * @return - true if registration succeeded; false if username already exists
     * @throws IllegalArgumentException if username is empty or password is too short
     */
    public boolean registerUser(String strUsername, String strPassword, String strSecurityQ, String strSecurityA) 
    {
        if (findUserByUsername(strUsername) != null)  // Check uniqueness
        {
            return false;  
        }

        User user = new User(strUsername, strPassword, strSecurityQ, strSecurityA);  // Create user
        userList.add(user);  // Add to list
        FileUtils.saveUsersToCSV(userList);  // Persist updated list

        return true;
    }


    /**
     * Authenticates a user by username and password
     * @param strUsername - the username to check
     * @param strPassword- the password to verify
     * @return - User object if credentials match; null if not found
     */
    public User loginUser(String strUsername, String strPassword) 
    {
        for (User user : userList)  // Iterate users
        {
            if (user.getStrUsername().equalsIgnoreCase(strUsername)  // Username match
                && user.authenticate(strPassword))  // Password check
            {
                return user;  
            }
        }

        return null;  // No matching user
    }


    /**
     * Recovers a forgotten password using security answer
     * @param strUsername - the username to recover
     * @param strSecurityA - the provided security answer
     * @return - new temporary password if recovery succeeded; null if failed 
     */
    public String recoverPassword(String strUsername, String strSecurityA) 
    {
        User user = findUserByUsername(strUsername);  // Lookup user

        if (user != null && user.validateSecurityAnswer(strSecurityA))  // Validate answer
        {
            String strNewPwd = generateTempPassword();  // Generate temp password
            user.setStrPassword(strNewPwd);  // Update user password
            FileUtils.saveUsersToCSV(userList);  // Persist change

            return strNewPwd;
        }

        return null;  // Recovery failed
    }


    /**
     * Finds a user by username
     * @param strUsername - the username to search for
     * @return - User object if found; null if not
     */
    public User findUserByUsername(String strUsername) 
    {
        for (User user : userList)  // Iterate users
        {
            if (user.getStrUsername().equalsIgnoreCase(strUsername))  // Match
            {
                return user;  
            }
        }

        return null;  // Not found
    }


    /**
     * Generates an 8-character temporary password from a fixed character set
     * @return - generated temporary password
     */
    private String generateTempPassword() 
    {
        String strChars     = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";  // Allowed characters
        StringBuilder sbPwd = new StringBuilder(8);                // Password builder
        Random random       = new Random();                        // Random generator

        for (int intI = 0; intI < 8; intI++)  // Build each character
        {
            int intIndex = random.nextInt(strChars.length());  // Random index
            sbPwd.append(strChars.charAt(intIndex));           // Append char
        }

        return sbPwd.toString();  // Return generated password
    }
}