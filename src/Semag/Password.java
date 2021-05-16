package Semag;

import java.util.Random;

public class Password {

    private String pass;
    Random r = new Random();

    public Password() {
        pass = "password";
    }

    public String getPass() {
        return pass;
    }
    
    
    public boolean issecure(String password){
        if(password.equals("is secure")){
            return true;
        }else{
            return false;
        }
    }

}
