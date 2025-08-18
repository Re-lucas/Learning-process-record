package model;


public class User 
{
    private String strUsername;    
    private String strPassword;     
    private String strSecurityQ;    
    private String strSecurityA;    
    private String strPreferences;  

    public User(String strUsername, String strPassword, 
                String strSecurityQ, String strSecurityA) 
    {
        this.strUsername     = strUsername;
        this.strPassword     = strPassword;
        this.strSecurityQ    = strSecurityQ;
        this.strSecurityA    = strSecurityA;
        this.strPreferences  = "";         
    }

    public String getStrUsername()      
    { 
        return strUsername; 
    }

    public String getStrPassword()      
    { 
        return strPassword; 
    }

    public String getStrSecurityQ()     
    { 
        return strSecurityQ; 
    }

    public String getStrSecurityA()     
    { 
        return strSecurityA; 
    }

    public String getStrPreferences()   
    { 
        return strPreferences; 
    }

    public void setStrPassword(String strPassword)        
    { 
        this.strPassword = strPassword; 
    }

    public void setStrPreferences(String strPreferences)  
    { 
        this.strPreferences = strPreferences; 
    }


    public boolean authenticate(String strInputPassword) 
    {
        return strPassword.equals(strInputPassword);
    }

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
