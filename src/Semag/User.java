package Semag;

import java.util.ArrayList;
import java.util.Scanner;

public class User {

    private PeopleADT people_array = new PeopleADT();
    Scanner sc = new Scanner(System.in);
    private static int ID = 0;

    public People register() {
        System.out.println("Enter gmail");
        String gmail = sc.nextLine();
        String title = "Vertified code by Doge";
        Password password_obj = new Password();
        String verified = password_obj.getPass();
        String content = "Hi this is the vertified code : " + verified;
        gmail_sender obj = new gmail_sender(gmail, title, content);
        obj.send();
        System.out.println("enter name");
        String name = sc.nextLine();
        while (people_array.contain(name) == true) {
            System.out.println("enter name");
            name = sc.nextLine();
        }
        System.out.println("enter password:");
        String password = sc.nextLine();
        while (!password_obj.issecure(password)) {
            System.out.println("enter password:");
            password = sc.nextLine();
        }
        People people_obj = new People(password, name, ID);
        ID++;
        people_array.add(people_obj);
        return people_obj;
    }

    public People login() { // only three time try , return to previous window if failed.
        int b = -1;
        String text = "";
        while (b == -1) {
            System.out.println(text + "enter username");
            String name = sc.nextLine();
            b = getindex(name);
            text = "wrong username. please re-";
        }
        String text2 = "";
        for (int i = 0; i < 3; i++) {
            System.out.println("enter password. " + (3 - i) + " time try left.");
            int a = sc.nextInt();
            if (people_array.get(b).getPassword().equals(a)) {
                System.out.println("Welcome. " + people_array.get(b).getName());
                return people_array.get(b);
            }
            text2 = "wrong password. please re-";
        }
        return null;
    }

    public int getindex(String name) {
        for (int i = 0; i < people_array.size(); i++) {
            if (people_array.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

}
