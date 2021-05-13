package Semag;

import java.util.Scanner;

public class SEMAG {

// USER INTERFACE
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // login
        Window obj = new Window();
        while(true){
            System.out.println("login / register");
            String input1 = sc.nextLine();
            if(obj.ac(input1)){
                continue;
            }
            obj.userwindow();
        }
    }
    
}
