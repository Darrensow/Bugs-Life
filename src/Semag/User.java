package Semag;

import java.util.ArrayList;
import java.util.Scanner;

public class User {

    private ArrayList<People> list = new ArrayList<>();
    Scanner sc = new Scanner(System.in);
    private static int ID = 0;

    public People register() {
        System.out.println("enter name");
        String name = sc.nextLine();
        int a = new Password().getPass();
        System.out.println("your password......" + a);
        People people_obj = new People(a, name, ID);
        ID++;
        list.add(people_obj);
        return people_obj;
    }

    public People login() { // only three time try , return to previous window if failed.
        int b = 0;
        String text = "";
        while (b != -1) {
            System.out.println(text + "enter username");
            String name = sc.nextLine();
            b = getindex(name);
            text = "wrong username. please re-";
        }
        String text2 = "3";
        for (int i = 0; i < 3; i++) {
            System.out.println(text2 + "enter password. " + (3 - i) + " time try left.");
            int a = sc.nextInt();
            if (a == list.get(b).getPassword()) {
                System.out.println("Welcome. " + list.get(b).getName());
                return list.get(b);
            }
            text2 = "wrong password. please re-";
        }
        return null;
    }

    public int getindex(String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

}
