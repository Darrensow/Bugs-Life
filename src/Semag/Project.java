package Semag;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Project implements Comparable<Project> {

    private Window window_control = new Window(); //call or use method from window class
    private static ArrayList<Issue> issue_Array = new ArrayList<>();  // store issue
    PeopleADT people_Array = new PeopleADT();   // store people
//    ArrayList<people> people_Array = new ArrayList<>();
    private Issue issue_control = new Issue(); // call or use method from issue class 
    Scanner sc = new Scanner(System.in);
    private static int numissue = 0;  // issue id
    private Integer ID;  //project id
    private String name; //project name
    private People current_people;  //current log in people
    private String signal;  // use for sort / change compareto method
    private People owner;  // project owner

    public Project() {
    }
/**
 * 
 * @param signal 
 * set signal
 */
    public void setSignal(String signal) {
        this.signal = signal;
    }
/**
 * 
 * @param issue want to remove
 * remove issue
 */
    public void removeissue(Issue issue_obj) {
        issue_Array.remove(issue_obj);
    }
/**
 * 
 * @param name
 * @param ID
 * @param owner 
 * create project
 */
    public Project(String name, int ID, People owner) {
        this.ID = ID;
        this.name = name;
        this.owner = owner;
    }
/**
 * owner of project window
 */
    public void projectwindow_owner() {
        sortBased(1);
        boolean quit = false;
        while (quit == false) {
            print();
            System.out.println("action? \n1)sort \n2)include \n3)exclude \n4)add issue \n5)search issue \n6)delete project \n7)quit");
            int input1 = sc.nextInt();
            switch (input1) {
                case 1:
                    System.out.println("sort based on \n1)priority \n2)time");
                    int in2 = sc.nextInt();
                    sortBased(in2);
                    print();
                    break;
                case 2:
                    System.out.println("enter tags in format (#tags1#tags2)");
                    String tags_in = sc.nextLine();
                    System.out.println("enter state in format (#state1#state2)");
                    String states_in = sc.nextLine();
                    filterin(tags_in, states_in);
                    break;
                case 3:
                    System.out.println("enter tags in format (#tags1#tags2)");
                    String tags_out = sc.nextLine();
                    System.out.println("enter state in format (#state1#state2)");
                    String states_out = sc.nextLine();
                    filterout(tags_out, states_out);
                    break;
                case 4:
                    addissue();
                    break;
                case 5:
                    System.out.println("enter search project keyword or #ID");
                    String in4 = sc.next();
                    search(in4);
                    break;
                case 6:
                    clear();
                    return;
                case 7:
                    quit = true;
                    return;
                default:
                    System.out.println("wrong input");
                    break;
            }
        }
    }
/**
 * normal user window
 */
    public void projectwindow() {
        sortBased(1);
        boolean quit = false;
        while (quit == false) {
            print();
            System.out.println("action? \n1)sort \n2)include \n3)exclude \n4)add issue \n5)search issue \n6)quit");
            int input1 = sc.nextInt();
            switch (input1) {
                case 1:
                    System.out.println("sort based on \n1)priority \n2)time");
                    int in2 = sc.nextInt();
                    sortBased(in2);
                    print();
                    break;
                case 2:
                    System.out.println("enter tags in format (#tags1#tags2)");
                    String tags_in = sc.nextLine();
                    System.out.println("enter state in format (#state1#state2)");
                    String states_in = sc.nextLine();
                    filterin(tags_in, states_in);
                    break;
                case 3:
                    System.out.println("enter tags in format (#tags1#tags2)");
                    String tags_out = sc.nextLine();
                    System.out.println("enter state in format (#state1#state2)");
                    String states_out = sc.nextLine();
                    filterout(tags_out, states_out);
                    break;
                case 4:
                    addissue();
                    break;
                case 5:
                    System.out.println("enter search project keyword or #ID");
                    String in4 = sc.next();
                    search(in4);
                    break;
                case 6:
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
 * @param current_people 
 * determine whether is owner or not
 */
    public void projectwindow(People current_people) {
        if (current_people == owner) {
            projectwindow_owner();
        } else {
            projectwindow();
        }
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
/**
 * remove project
 */
    public void clear() {
        for (int i = 0; i < issue_Array.size(); i++) {
            issue_Array.get(i).getAssignee().reduceassigned();
        }
        window_control.removeproject(this);
    }

/**
 * add issue
 */
    public void addissue() {
        System.out.println("Enter issue name");
        String issue_name = sc.next();
        System.out.println("enter tags");
        String issue_tags = sc.nextLine();
        System.out.println("Enter priority");
        int priority = sc.nextInt();
        People assignee_obj = new People();
        while (true) {
            System.out.println("enter assignee:");
            String assignee = sc.nextLine();
            assignee_obj = searchpeople(assignee);
            if (assignee_obj != null) {
                break;
            }
            System.out.print("invalid name");
        }
        System.out.println("enter issue des");
        String text = sc.nextLine();
        Text<String> text_obj = new Text<>();
        Text<String> temp_text = new Text<>();
        while (!text.equals("#quit")) {
            if (text.equals("#undo")) {
                if (text_obj.getSize() < 0) {
                    break;
                }
                temp_text.push(text_obj.pop());
            } else if (text.equals("#redo")) {
                if (temp_text.getSize() < 0) {
                    break;
                }
                text_obj.push(temp_text.pop());
            } else {
                text_obj.push(text);
                temp_text.clear();
            }
            text = sc.nextLine();
        }
        text = text_obj.getString();
        issue_Array.add(new Issue(numissue, issue_name, text, current_people, assignee_obj, issue_tags, priority));
        numissue++;
    }
/**
 * 
 * @param keyword search
 * search issue
 */
    public void search(String input) { //  only input number will directly assume as ID
        if (isnumberic(input)) {
            entertheissue(Integer.parseInt(input.substring(1)));
        } else {
            if (printsearchResult(input)) {
                System.out.println("enter ID of project");
                int id = sc.nextInt();
                entertheissue(id);
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
 * print issue
 * @return true if have isseu
 */
    public boolean printsearchResult(String seachkeyword) {
        ArrayList<Issue> temp = new ArrayList<>();
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        String[] token = seachkeyword.split(" ");
        for (int i = 0; i < issue_Array.size(); i++) {
            for (int j = 0; j < token.length; j++) {
                if (issue_Array.get(i).getTitle().contains(token[j] + " ")) {
                    temp.add(issue_Array.get(i));
                    break;
                } else if (issue_Array.get(i).getTitle().contains(" " + token[j] + " ")) {
                    temp.add(issue_Array.get(i));
                    break;
                } else if (issue_Array.get(i).getTitle().contains(" " + token[j])) {
                    temp.add(issue_Array.get(i));
                    break;
                } else if (issue_Array.get(i).getText().contains(" " + token[j])) {
                    temp.add(issue_Array.get(i));
                } else if (issue_Array.get(i).getText().contains(token[j] + " ")) {
                    temp.add(issue_Array.get(i));
                } else if (issue_Array.get(i).getText().contains(" " + token[j] + " ")) {
                    temp.add(issue_Array.get(i));
                } else if (checkcomment(issue_Array.get(i).getComment(), token[j])) {
                    temp.add(issue_Array.get(i));
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
 * @param comment arraylist
 * @param keyword
 * check a wword in the comment 
 * @return 
 */
    public boolean checkcomment(ArrayList<Comment> arr, String token) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getText().contains(token + " ")) {
                return true;
            } else if (arr.get(i).getText().contains(" " + token)) {
                return true;
            } else if (arr.get(i).getText().contains(" " + token + " ")) {
                return true;
            }
        }
        return false;
    }

    //check whether is id or not
    public boolean isnumberic(String sen) {
        try {
            if (sen.charAt(0) != '#') {
                return false;
            }
            double d = Double.parseDouble(sen.substring(1));
            if (d > issue_Array.size()) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
/**
 * 
 * @param action
 * change signal
 */
    public void sortBased(int num) {
        if (num == 1) {
            issue_control.setSignal("PRIORITY");
        } else {
            issue_control.setSignal("TIME");
        }
    }
/*
    print array
    */
    public void print() {
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        for (int i = 0; i < issue_Array.size(); i++) {
            pq.add(issue_Array.get(i));
        }
        for (int i = 0; i < issue_Array.size(); i++) {
            System.out.println(pq.poll());
        }
    }
/*
  include  filter kind
    */
    public void filterin(String tag, String state) {
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        String[] tags = tag.split("#");
        String[] states = state.split("#");
        for (int i = 0; i < issue_Array.size(); i++) {
            for (int j = 0; j < Math.max(state.length(), tags.length); j++) {
                if (states.length < j && issue_Array.get(i).getStatus().equals(states[j])) {
                    pq.add(issue_Array.get(i));
                    break;
                }
                if (tags.length < j && issue_Array.get(i).getTags().equals(tags[j])) {
                    pq.add(issue_Array.get(i));
                    break;
                }

            }
        }
        for (int i = 0; i < issue_Array.size(); i++) {
            System.out.println(pq.poll());
        }
    }
/*
    exclude filter 
    */
    public void filterout(String tag, String state) {
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        String[] tags = tag.split("#");
        String[] states = state.split("#");
        for (int i = 0; i < issue_Array.size(); i++) {
            for (int j = 0; j < Math.max(state.length(), tags.length); j++) {
                if (states.length < j && issue_Array.get(i).getStatus().equals(states[j])) {
                    break;
                }
                if (tags.length < j && issue_Array.get(i).getTags().equals(tags[j])) {
                    break;
                }
                pq.add(issue_Array.get(i));
            }
        }
        for (int i = 0; i < issue_Array.size(); i++) {
            System.out.println(pq.poll());
        }
    }

    public People searchpeople(String name) {
        for (int i = 0; i < people_Array.size(); i++) {
            if (name.equals(people_Array.get(i).getName())) {
                return people_Array.get(i);
            }
        }
        return null;
    }

    /**
     * 
     * @param issue index
     * enter issue window
     */
    public void entertheissue(int index) {
        issue_Array.get(index).issuewindow(current_people);
    }

    @Override
    public int compareTo(Project o) {
        if (signal == "NAME") {
            return this.name.compareTo(o.name);
        } else {
            return this.ID.compareTo(o.ID);
        }

    }
}
