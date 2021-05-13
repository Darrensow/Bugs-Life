package Semag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Issue implements Comparable<Issue>, Cloneable {

    private Project project_control = new Project();
    private int ID;
    private String title;
    private String text;
    private LocalDateTime time;
    private People creator;
    private People assignee;
    private ArrayList<Comment> comment = new ArrayList<>();
    private static int numbercomment = 1;
    private String tags;
    private Integer priority;
    private String status;
    private static String signal;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    Scanner sc = new Scanner(System.in);
    private People current_people;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Issue() {

    }

    public People getAssignee() {
        return assignee;
    }

    public Issue(int ID, String title, String text, People creator, People assignee, String tags, int priop) {
        this.ID = ID;
        this.title = title;
        this.creator = creator;
        this.assignee = assignee;
        time = LocalDateTime.now();
        this.text = text;
        this.status = "open";
        this.priority = priop;
        this.tags = tags;
    }

//    public void addtags(String[] list) {
//        for (int i = 0; i < list.length; i++) {
//            tags.add(list[i]);
//        }
//    }
    public void setStatus(String status) {
        this.status = status;
    }

    //add comment
    public void issuewindow_owner() {
        boolean quit = false;
        while (quit == false) {
            print();
            System.out.println("action? \n1)add comment \n2)react on comment \n3)delete issue \n4)quit");
            int input1 = sc.nextInt();
            switch (input1) {
                case 1:
                    addComment();
                    break;
                case 2:
                    react();
                    break;
                case 3:
                    clear();
                    break;
                case 4:
                    quit = true;
                    break;
                default:
                    System.out.println("wrong input");
                    break;
            }
        }
    }

    public void issuewindow() {
        boolean quit = false;
        while (quit == false) {
            print();
            System.out.println("action? \n1)add comment \n2)react on comment \n3)quit");
            int input1 = sc.nextInt();
            switch (input1) {
                case 1:
                    addComment();
                    break;
                case 2:
                    react();
                    break;
                case 3:
                    quit = true;
                    break;
                default:
                    System.out.println("wrong input");
                    break;
            }
        }
    }

    public void issuewindow(People current_people) {
        if (current_people == creator) {
            this.current_people = current_people;
            issuewindow_owner();
        } else {
            this.current_people = current_people;
            issuewindow();
        }
    }

    public void react() {
        System.out.println("enter comment ID that you want to react");
        int index = sc.nextInt();
        System.out.println("Enter 'h' or happay , 'a' for angry.");
        char reaction = sc.next().charAt(0);
        if (reaction == 'h') {
            comment.get(index).happy();
        } else {
            comment.get(index).angry();
        }
    }

    public void changelog() {
        //upload any change into text or database
    }

    public void addComment() {

        System.out.println("enter comment");
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
        comment.add(new Comment(current_people, text, numbercomment));
        numbercomment++;
    }

    /**
     * Displays the whole comment section. Regarding the wrapping of the text,
     * have to modify the toString() in Comment class
     *
     * @return String representation of the whole comment section.
     */
    public String displayCommentsSection() {
        StringBuilder sb = new StringBuilder();
        sb.append("Comments\n-----------");
        for (Comment value : comment) {
            sb.append(value);
        }
        return sb.toString();
    }

    public void clear() {
        project_control.removeissue(this);
        comment.clear();
    }

    public static void setSignal(String signal) {
        Issue.signal = signal;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }

    public String getStatus() {
        return status;
    }

    public String getTags() {
        return tags;
    }

    public void print() {

    }

    @Override
    public int compareTo(Issue o) {
        if (signal == "PRIORITY") {
            return this.priority.compareTo(o.priority);
        } else {
            return this.time.compareTo(o.time);
        }

    }

}
