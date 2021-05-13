package Semag;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Window {

    private Project project_control = new Project();   //use for call or use method from project class
    private static ArrayList<Project> project_Array = new ArrayList<>();  // store project
    PeopleADT people_Array = new PeopleADT();                           // store people
//    private ArrayList<people> people_Array = new ArrayList<>();
    Scanner sc = new Scanner(System.in);
    private static int numberproject = 0;    // project id
    private People current_people; // current user logged

    /**
     *
     * @param project_obj want to remove
     */
    public void removeproject(Project project_obj) {
        project_Array.remove(project_obj);
    }

    /**
     *
     * @param ac
     * @return true if action success , false if false.
     */
    public boolean ac(String ac) {
        if ("register".equals(ac)) {
            current_people = new User().register();
            people_Array.add(current_people);
            return true;
        } else {
            current_people = new User().login();
            if (current_people == null) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * open window
     */
    public void userwindow() { // display project original window
        sortBased(1);
        boolean quit = false;
        while (quit == false) {
            print();
            System.out.println("action? \n1)sort \n2)add project 3)search project 4)quit");
            int input1 = sc.nextInt();
            switch (input1) {
                case 1:
                    System.out.println("sort based on \n1)ID \n2)name");
                    int in2 = sc.nextInt();
                    sortBased(in2);
                    print();
                    break;
                case 2:
                    System.out.println("Enter project name");
                    String in3 = sc.next();
                    addproject(in3);
                    break;
                case 3:
                    System.out.println("enter search project keyword or #ID");
                    String in4 = sc.next();
                    search(in4);
                    break;
                case 4:
                    quit = true;
                    return;
                default:
                    System.out.println("wrong input");
                    break;
            }
        }
    }

    /**
     *
     * @param input keyword or ID of the project
     *
     */
    public void search(String input) {
        if (isnumberic(input)) {
            entertheprojext(Integer.parseInt(input.substring(1)));
        } else {
            if (printsearchResult(input)) {
                System.out.println("enter ID of project");
                int id = sc.nextInt();
                entertheprojext(id);
            } else {
                System.out.println("not result found. re-enter keyword");
                String str = sc.nextLine();
                search(str);
            }
        }
    }

    /**
     *
     * @param seachkeyword
     * @return true if have that project
     */
    public boolean printsearchResult(String seachkeyword) {
        ArrayList<Project> temp = new ArrayList<>();
        PriorityQueue<Project> pq = new PriorityQueue<>();
        String[] token = seachkeyword.split(" ");
        for (int i = 0; i < project_Array.size(); i++) {
            for (int j = 0; j < token.length; j++) {
                if (project_Array.get(i).getName().contains(token[j] + " ")) {
                    temp.add(project_Array.get(i));
                    break;
                } else if (project_Array.get(i).getName().contains(" " + token[j] + " ")) {
                    temp.add(project_Array.get(i));
                    break;
                } else if (project_Array.get(i).getName().contains(" " + token[j])) {
                    temp.add(project_Array.get(i));
                    break;
                }
            }
        }
        for (int i = 0; i < temp.size(); i++) {
            pq.add(temp.get(i));
        }
        for (int i = 0; i < temp.size(); i++) {
            System.out.println(pq.poll());
        }
        if (temp.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param sen keyword
     * @return true if it is an id
     */
    public boolean isnumberic(String sen) {
        try {
            if (sen.charAt(0) != '#') {
                return false;
            }
            double d = Double.parseDouble(sen.substring(1));
            if (d > project_Array.size()) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param name project name add project
     */
    public void addproject(String name) {
        project_Array.add(new Project(name, numberproject, current_people));
        numberproject++;
    }

    /**
     *
     * @param num action change sort
     *
     */
    public void sortBased(int num) {
        if (num == 1) {
            project_control.setSignal("ID");
        } else {
            project_control.setSignal("NAME");
        }
    }

    /**
     *
     * @param index project index enter project window
     */
    public void entertheprojext(int index) {
        project_Array.get(index).projectwindow(current_people);
    }

    /**
     * print in sorted array
     */
    public void print() {
        PriorityQueue<Project> pq = new PriorityQueue<>();
        for (int i = 0; i < project_Array.size(); i++) {
            pq.add(project_Array.get(i));
        }
        for (int i = 0; i < project_Array.size(); i++) {
            System.out.println(pq.poll());
        }
    }
}
