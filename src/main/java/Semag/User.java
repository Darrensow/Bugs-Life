package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class User implements Serializable {

    private PeopleADT people_array = new PeopleADT();
    @JsonIgnore
    Scanner sc = new Scanner(System.in);

    private static int ID = 0;

    public User() {
    }

    public People register() {
        System.out.println("Enter gmail");
        String gmail = sc.nextLine();
        String title = "Vertified code by Doge";
        Password password_obj = new Password();
        String verified = password_obj.getPass();
        String content = "Hi this is the vertified code : " + verified;
        gmail_sender obj = new gmail_sender(gmail, title, content);
        obj.send();
        System.out.println("enter the verified code : ");
        String num = sc.nextLine();
        while (!num.equals(verified)) {
            System.out.println("enter the verified code : ");
        }
        System.out.println("enter name");
        String name = sc.nextLine();
        while (people_array.contain(name) == true) {
            System.out.println("enter name");
            name = sc.nextLine();
        }
        System.out.println("enter password:");
        String password = sc.nextLine();
        while (!password_obj.isValid(password)) {
            System.out.println("re-enter password, password not secure:");
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

    // Save and read data -- Jackson -- JSON --
    @JsonIgnore
    private static DataManagement dm = new DataManagement();

    /**
     * Method to save data, calls the writeData method in DataManagement Class
     */
    public void saveData() {
        dm.writeData(this);
    }

    public void loadData() {
        User temp = dm.readUserData();
        this.people_array = temp.people_array;
        this.ID = temp.ID;
    }

    // -- Getter and setter methods --
    public PeopleADT getPeople_array() {
        return people_array;
    }

    public void setPeople_array(PeopleADT people_array) {
        this.people_array = people_array;
    }

    public static int getID() {
        return ID;
    }

    public static void setID(int ID) {
        User.ID = ID;
    }
}
