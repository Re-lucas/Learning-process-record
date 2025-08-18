package service;

import java.util.ArrayList;
import java.util.Random;

import model.User;
import util.FileUtils;


public class UserManager 
{
    private ArrayList<User> userList;

    public UserManager() 
    {
        userList = FileUtils.loadUsersFromCSV();
        System.out.println("Loaded " + userList.size() + " users");
    }


    public boolean registerUser(String strUsername, String strPassword, 
                                String strSecurityQ, String strSecurityA) 
    {
        if (findUserByUsername(strUsername) != null) {
            return false;  
        }
        User u = new User(strUsername, strPassword, strSecurityQ, strSecurityA);
        userList.add(u);
        FileUtils.saveUsersToCSV(userList);
        return true;
    }


    public User loginUser(String strUsername, String strPassword) 
    {
        for (User u : userList) 
        {
            if (u.getStrUsername().equalsIgnoreCase(strUsername) &&
                u.authenticate(strPassword)) 
            {
                return u;
            }
        }
        return null;
    }


    public String recoverPassword(String strUsername, String strSecurityA) 
    {
        User u = findUserByUsername(strUsername);
        if (u != null && u.validateSecurityAnswer(strSecurityA)) 
        {
            String newPwd = generateTempPassword();
            u.setStrPassword(newPwd);
            FileUtils.saveUsersToCSV(userList);
            return newPwd;
        }
        return null;
    }

    public User findUserByUsername(String strUsername) 
    {
        for (User u : userList) 
        {
            if (u.getStrUsername().equalsIgnoreCase(strUsername)) {
                return u;
            }
        }
        return null;
    }


    private String generateTempPassword() 
    {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder(8);
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
