package service;

import java.util.ArrayList;
import java.util.Random;

import model.User;
import util.FileUtils;

/**
 * File: UserManager.java
 * Author: [团队名称]
 * Date: [当前日期]
 *
 * Description: 用户管理服务，处理注册/登录/密码恢复
 */
public class UserManager 
{
    private ArrayList<User> userList;

    public UserManager() 
    {
        // 从 CSV 加载所有用户（含 preferences 列）
        userList = FileUtils.loadUsersFromCSV();
        System.out.println("Loaded " + userList.size() + " users");
    }

    /**
     * 新用户注册
     * @return 注册是否成功（用户名唯一性）
     */
    public boolean registerUser(String strUsername, String strPassword, 
                                String strSecurityQ, String strSecurityA) 
    {
        if (findUserByUsername(strUsername) != null) {
            return false;  // 用户名已存在
        }
        User u = new User(strUsername, strPassword, strSecurityQ, strSecurityA);
        userList.add(u);
        FileUtils.saveUsersToCSV(userList);
        return true;
    }

    /**
     * 用户登录
     * @return 匹配的 User 对象，失败返回 null
     */
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

    /**
     * 密码恢复
     * @return 新密码字符串，失败返回 null
     */
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

    /**
     * 根据用户名查找 User 对象
     */
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

    // ===== 私有辅助方法 =====

    /**
     * 生成 8 位随机临时密码（不含易混字符）
     */
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
