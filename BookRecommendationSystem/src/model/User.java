package model;

/**
 * File: User.java
 * Author: [团队名称]
 * Date: [当前日期]
 *
 * Description: 用户数据模型，包含用户属性和安全验证
 */
public class User 
{
    private String strUsername;     // 用户名
    private String strPassword;     // 密码
    private String strSecurityQ;    // 安全问题
    private String strSecurityA;    // 安全答案
    private String strPreferences;  // 偏好设置

    public User(String strUsername, String strPassword, 
                String strSecurityQ, String strSecurityA) 
    {
        this.strUsername     = strUsername;
        this.strPassword     = strPassword;
        this.strSecurityQ    = strSecurityQ;
        this.strSecurityA    = strSecurityA;
        this.strPreferences  = "";         // 默认无偏好
    }

    // === Getter 方法 ===
    public String getStrUsername()      { return strUsername; }
    public String getStrPassword()      { return strPassword; }
    public String getStrSecurityQ()     { return strSecurityQ; }
    public String getStrSecurityA()     { return strSecurityA; }
    public String getStrPreferences()   { return strPreferences; }

    // === Setter 方法 ===
    public void setStrPassword(String strPassword)        { this.strPassword = strPassword; }
    public void setStrPreferences(String strPreferences)  { this.strPreferences = strPreferences; }

    /**
     * 验证用户登录密码
     * @param strInputPassword 输入密码
     * @return 是否匹配
     */
    public boolean authenticate(String strInputPassword) 
    {
        return strPassword.equals(strInputPassword);
    }

    /**
     * 验证安全问题答案（忽略大小写）
     * @param strInputAnswer 输入答案
     * @return 是否匹配
     */
    public boolean validateSecurityAnswer(String strInputAnswer) 
    {
        return strSecurityA.equalsIgnoreCase(strInputAnswer);
    }

    @Override
    public String toString() 
    {
        return String.format(
            "User[%s], Prefs: %s",
            strUsername,
            (strPreferences.isEmpty() ? "None" : strPreferences)
        );
    }
}
