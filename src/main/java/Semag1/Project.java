package Semag1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.Serializable;
import static java.lang.Thread.sleep;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

public class Project implements Serializable, ActionListener {

    private ArrayList<Issue> issue = new ArrayList<>();  // store issue
    PeopleADT people_Array; // store people
    transient Scanner sc = new Scanner(System.in);
    private int numissue = 0;  // issue id

    @JsonIgnore
    private People current_people;  //current log in people

    /**
     * Project ID
     */
    private Integer ID;  //project id

    /**
     * Project Name
     */
    private String name; //project name

    /**
     * Project Owner, which is a People
     */
    private People owner;  // project owner

    //gui
    JFrame frame = new JFrame();
    ImageIcon backgroud_image = new ImageIcon("D:\\Download\\register backgroud.jpg");
    JLabel label = new JLabel(backgroud_image);
    JTextField search_issue = new JTextField();
    JButton add_issue = new JButton();
    ImageIcon add_image = new ImageIcon("D:\\Download\\add.jpg");
    ImageIcon delete_project_image = new ImageIcon("D:\\Download\\trash_icon.png");
    JButton delete_project = new JButton();
    JComboBox sort_issue = new JComboBox();
    String[] setting_option = {"Changlog", "Quit"};
    JComboBox setting_button = new JComboBox();
    ArrayList<JCheckBox> include = new ArrayList<>();
    ArrayList<JCheckBox> exclude = new ArrayList<>();
    JPanel black_panel_up = new JPanel();
    JScrollPane black_ps_up = new JScrollPane(black_panel_up);
    JPanel black_panel_down = new JPanel();
    JScrollPane black_ps_down = new JScrollPane(black_panel_down);
    JPanel include_title = new JPanel();
    JPanel exluded_title = new JPanel();
    JTextField include_text = new JTextField();
    JTextField exclude_text = new JTextField();
    DefaultTableModel tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JTable table = new JTable(tableModel);
    JScrollPane table_scroll = new JScrollPane(table);
    JPanel add_issue_panel = new JPanel();
    JTextField name_text = new JTextField("Enter issue name");
    JComboBox priority = new JComboBox();
    JLabel priop_text = new JLabel();
    JTextField assignee_text = new JTextField("Enter  assignee");
    JTextField tags_text = new JTextField("Enter tags");
    JTextArea descrip = new JTextArea();
    JScrollPane desc_sp = new JScrollPane(descrip);
    ImageIcon undo_icon = new ImageIcon("D:\\Download\\undo.png");
    ImageIcon redo_icon = new ImageIcon("D:\\Download\\redo.png");
    JButton undo = new JButton();
    JButton reundo = new JButton();
    String[] list_priority = {"1", "2", "3", "4", "5"};
    JButton submit = new JButton("SUBMIT");
    String[] sort_option = {"Sort based ID", "Sort based priority", "Sort based issuecount"};

    String[] column = {"ID", "Title", "Status", "Tag", "Priority", "Time", "Assignee", "CreatedBy"};
    boolean exit = false;
    Text<String> text_undo = new Text<>();
    Text<String> text_redo = new Text<>();
    boolean undo_pressed = false;
    boolean redo_pressed = false;

    JPanel green_panel_up = new JPanel();
    JScrollPane green_ps_up = new JScrollPane(green_panel_up);
    JPanel green_panel_down = new JPanel();
    JScrollPane green_ps_down = new JScrollPane(green_panel_down);
    JPanel include_state_title = new JPanel();
    JPanel exluded_state_title = new JPanel();
    JTextField include_state_text = new JTextField();
    JTextField exclude_state_text = new JTextField();
    JButton sreach = new JButton("Go");
    ArrayList<JCheckBox> include_state = new ArrayList<>();
    ArrayList<JCheckBox> exclude_state = new ArrayList<>();
    JFrame window_frame;

    public Project() {
    }

    /**
     * @param name
     * @param ID
     * @param owner create project
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
        this.sortIssueBased(1);
        boolean quit = false;
        while (quit == false) {
            System.out.println("action? \n1)sort \n2)include \n3)exclude \n4)add issue \n5)search issue \n6)delete project \n7)quit");
            int input1 = sc.nextInt();
            switch (input1) {
                case 1:
                    System.out.println("sort based on \n1)priority \n2)time");
                    int in2 = sc.nextInt();
                    this.sortIssueBased(2);
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
                    deleteThisProject();
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
        this.sortIssueBased(1);
        boolean quit = false;
        while (quit == false) {
            System.out.println("action? \n1)sort \n2)include \n3)exclude \n4)add issue \n5)search issue \n6)quit");
            int input1 = sc.nextInt();
            switch (input1) {
                case 1:
                    System.out.println("sort based on \n1)priority \n2)time");
                    int in2 = sc.nextInt();
                    this.sortIssueBased(in2);
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
     * @param current_people determine whether is owner or not
     */
    public void projectwindow(People current_people, JFrame frame, PeopleADT obj) {
        window_frame = frame;
        people_Array = obj;
        if (current_people == owner) {
            this.current_people = current_people;
            delete_project.setEnabled(true);

        } else {
            this.current_people = current_people;
            delete_project.setEnabled(false);
        }
        setupwindow();
        sortIssueBased(0);
        ArrayList<String> state = new ArrayList<>();
        state.add("no have state");
        ArrayList<String> tags = new ArrayList<>();
        tags.add("no have tags");
        include_and_exluded(tags);
        include_and_exluded_state(state);
    }

    /**
     * add issue
     */
    public void addissue() {
        System.out.println("Enter issue name");
        String issue_name = sc.next();
        System.out.println("enter tags with , in between");
        String issue_tags = sc.nextLine();
        String[] issue_tags_array = issue_tags.split(",");
        System.out.println("Enter priority");
        int priority = sc.nextInt();
        People assignee_obj;
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

        //To push notification to assignee_obj
        Issue iss = new Issue(numissue, issue_name, text, current_people, assignee_obj, issue_tags_array, priority, this);
        issue.add(iss);
        assignee_obj.addAssigned(this.ID, this.name, numissue, issue_name, current_people.getName());
        numissue++;
    }

    /**
     * @param input keyword search search issue
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
     * @param input keyword search search issue
     */
    public void search(String input, ArrayList<Issue> issue_arr) { //  only input number will directly assume as ID
        if (isnumberic(input)) {
            entertheissue(Integer.parseInt(input.substring(1)));
        } else {
            printsearchResult(input, issue_arr);
//            entertheissue(id);

        }
    }

    /**
     * @param seachkeyword print issue
     * @return true if have isseu
     */
    public void printsearchResult(String seachkeyword, ArrayList<Issue> issue_arr) {
        if (seachkeyword.equals("") || seachkeyword.equals("search")) {
            reset_table(issue_arr);
            return;
        }
        ArrayList<Issue> temp = new ArrayList<>();
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        String[] token = seachkeyword.split("#");
        for (int i = 0; i < issue_arr.size(); i++) {
            for (int j = 0; j < token.length; j++) {
                if (issue_arr.get(i).getTitle().contains(token[j] + " ")) {
                    temp.add(issue_arr.get(i));
                    break;
                } else if (issue_arr.get(i).getTitle().contains(" " + token[j] + " ")) {
                    temp.add(issue_arr.get(i));
                    break;
                } else if (issue_arr.get(i).getTitle().contains(" " + token[j])) {
                    temp.add(issue_arr.get(i));
                    break;
                } else if (issue_arr.get(i).getTitle().equals(token[j])) {
                    temp.add(issue_arr.get(i));
                    break;
                } else if (issue_arr.get(i).getDescriptionText().contains(" " + token[j])) {
                    temp.add(issue_arr.get(i));
                } else if (issue_arr.get(i).getDescriptionText().contains(token[j] + " ")) {
                    temp.add(issue_arr.get(i));
                } else if (issue_arr.get(i).getDescriptionText().equals(token[j])) {
                    temp.add(issue_arr.get(i));
                } else if (issue_arr.get(i).getDescriptionText().contains(" " + token[j] + " ")) {
                    temp.add(issue_arr.get(i));
                } else if (checkcomment(issue_arr.get(i).getComments(), token[j])) {
                    temp.add(issue_arr.get(i));
                    break;
                }
            }
        }

        for (int i = 0; i < temp.size(); i++) {
            pq.add(temp.get(i));
        }
        ArrayList<Issue> issue_table = new ArrayList<>();
        for (int i = 0; i < temp.size(); i++) {
            issue_table.add(pq.poll());
        }
        reset_table(issue_table);
    }

    /**
     * @param seachkeyword print issue
     * @return true if have isseu
     */
    public boolean printsearchResult(String seachkeyword) {
        ArrayList<Issue> temp = new ArrayList<>();
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        String[] token = seachkeyword.split(" ");
        for (int i = 0; i < issue.size(); i++) {
            for (int j = 0; j < token.length; j++) {
                if (issue.get(i).getTitle().contains(token[j] + " ")) {
                    temp.add(issue.get(i));
                    break;
                } else if (issue.get(i).getTitle().contains(" " + token[j] + " ")) {
                    temp.add(issue.get(i));
                    break;
                } else if (issue.get(i).getTitle().contains(" " + token[j])) {
                    temp.add(issue.get(i));
                    break;
                } else if (issue.get(i).getDescriptionText().contains(" " + token[j])) {
                    temp.add(issue.get(i));
                } else if (issue.get(i).getDescriptionText().contains(token[j] + " ")) {
                    temp.add(issue.get(i));
                } else if (issue.get(i).getDescriptionText().contains(" " + token[j] + " ")) {
                    temp.add(issue.get(i));
                } else if (checkcomment(issue.get(i).getComments(), token[j])) {
                    temp.add(issue.get(i));
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
     * @param arr comment arraylist
     * @param token keyword check a wword in the comment
     * @return
     */
    private boolean checkcomment(ArrayList<Comment> arr, String token) {
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
    private boolean isnumberic(String sen) {
        try {
            if (sen.charAt(0) != '#') {
                return false;
            }
            double d = Double.parseDouble(sen.substring(1));
            if (d > issue.size()) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * This method will sort the Issue with the column that the user wish, and
     * str8 print it out
     *
     * @param choose is the attribute of the Issue, eg ID, Title, returned as
     * int
     */
    public void sortIssueBased(int choose) {
        ArrayList<Issue> sortedIssueList = new ArrayList<>(issue);
        switch (choose) {
            case 0: //0 is the first option, ID
                Collections.sort(sortedIssueList, Issue.IDComparator);
                break;
            case 1: //1 is the sec option, Priority
                Collections.sort(sortedIssueList, Issue.priorityComparator);
                break;
            case 2: //2 is the third option, IssueCount
                Collections.sort(sortedIssueList, Issue.timeComparator);
                break;
            default:
                break;
        }
//        print(sortedIssueList);
        reset_table(sortedIssueList);
    }

    /**
     * Delete current project Update, this remove project we move it to Window
     */
    public void deleteThisProject() {
        for (int i = 0; i < issue.size(); i++) {
            removeIssue(issue.get(i));
        }
//        Window.removeProject(this);
    }

    /**
     * This is a method to just remove one issue from issue dashboard, called by
     * Issue class
     *
     * @param issue_obj issue to be removed
     */
    public void removeIssue(Issue issue_obj) {
        issue_obj.getAssignee().reduceAssigned();
        issue.remove(issue_obj);
        numissue--;
    }

    /**
     * print Issue list, this method is an overloading method
     */
    public void print() {
        this.print(issue);
    }

    /**
     * print selected list Tag formatting may be weird if there are a lot of
     * tags( String[] )
     */
    public void print(ArrayList<Issue> toPrint) {
        System.out.println(String.format("%3s %-30s %-15s %-15s %10s %-30s %-20s %-20s", "ID", "Title", "Status",
                "Tag", "Priority", "Time", "Assignee", "Creator"));
        for (int i = 0; i < toPrint.size(); i++) {
            System.out.println(printOneIssue(toPrint.get(i)));
        }
    }

    /**
     * This method return string representation of one Issue in the Issue
     * Dashboard, this method is called by
     * {@code print(ArrayList<Issue> toPrint)}
     */
    public String printOneIssue(Issue o) {
        StringBuilder str = new StringBuilder();
        str.append(String.format(" %3d", o.getID()));
        str.append(String.format(" %-30s", o.getTitle()));
        str.append(String.format(" %-15s", o.getStatus()));
        str.append(String.format(" %-15s", o.getTag()));
        str.append(String.format(" %10d", o.getPriority()));
        str.append(String.format(" %-30s", o.getTimestamp()));
        str.append(String.format(" %-20s", o.getAssignee().getName()));
        str.append(String.format(" %-20s", o.getCreator().getName()));
        return str.toString();
    }


    /*
  include  filter kind
     */
    private ArrayList<Issue> filterin(String tag, String state, ArrayList<Issue> issue_array) {
        if (tag.equals("") && state.equals("")) {
            return issue_array;
        }
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        String[] tags = tag.split("#");
        String[] states = state.split("#");
        for (int i = 0; i < issue_array.size(); i++) {
            label:
            {
                /*
                    note to Sam, my IDE says that the two if statements are always false.
                    I think it's the states.length > j and tags.length > j problem, could you check it out further? Thanks
                 */
                for (int j = 0; j < Math.max(state.length(), tags.length); j++) {
                    if (states.length < j && issue_array.get(i).getStatus().equals(states[j])) {
                        pq.add(issue_array.get(i));
                        break label;
                    }
                    String[] tagsArray = issue_array.get(i).getTag();
                    for (int k = 0; k < tagsArray.length; k++) { //compares tags[i] with tagsArray[k]
                        if (tags.length < j && tagsArray[k].equals(tags[j])) {
                            pq.add(issue_array.get(i));
                            break label;
                        }
                    }
                }
            }
        }
        ArrayList<Issue> return_value = new ArrayList<>();
        for (int i = 0; i < issue.size(); i++) {
            return_value.add(pq.poll());
        }
        return return_value;
    }

    /*
  include  filter kind
     */
    private void filterin(String tag, String state) {
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        String[] tags = tag.split("#");
        String[] states = state.split("#");
        for (int i = 0; i < issue.size(); i++) {
            label:
            {
                /*
                    note to Sam, my IDE says that the two if statements are always false.
                    I think it's the states.length > j and tags.length > j problem, could you check it out further? Thanks
                 */
                for (int j = 0; j < Math.max(state.length(), tags.length); j++) {
                    if (states.length < j && issue.get(i).getStatus().equals(states[j])) {
                        pq.add(issue.get(i));
                        break label;
                    }
                    String[] tagsArray = issue.get(i).getTag();
                    for (int k = 0; k < tagsArray.length; k++) { //compares tags[i] with tagsArray[k]
                        if (tags.length < j && tagsArray[k].equals(tags[j])) {
                            pq.add(issue.get(i));
                            break label;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < issue.size(); i++) {
            System.out.println(pq.poll());
        }
    }

    /*
    exclude filter 
     */
    private void filterout(String tag, String state) {
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        String[] tags = tag.split("#");
        String[] states = state.split("#");
        for (int i = 0; i < issue.size(); i++) {
            label:
            {
                /*
                    note to Sam, my IDE says that the two if statements are always false.
                    I think its the states.length > j and tags.length > j problem, could you check it out further?
                 */
                for (int j = 0; j < Math.max(state.length(), tags.length); j++) {
                    if (states.length < j && issue.get(i).getStatus().equals(states[j])) {
                        break label;
                    }
                    String[] tagsArray = issue.get(i).getTag();
                    for (int k = 0; k < tagsArray.length; k++) { //compares tags[i] with tagsArray[k]
                        if (tags.length < j && tagsArray[k].equals(tags[j])) {
                            break label;
                        }
                    }
                    pq.add(issue.get(i));
                }
            }
        }
        for (int i = 0; i < issue.size(); i++) {
            System.out.println(pq.poll());
        }
    }

    private ArrayList<Issue> filterout_withreturn(String tag, String state) {
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        String[] tags = tag.split("#");
        String[] states = state.split("#");
        for (int i = 0; i < issue.size(); i++) {
            label:
            {
                /*
                    note to Sam, my IDE says that the two if statements are always false.
                    I think its the states.length > j and tags.length > j problem, could you check it out further?
                 */
                for (int j = 0; j < Math.max(state.length(), tags.length); j++) {
                    if (states.length < j && issue.get(i).getStatus().equals(states[j])) {
                        break label;
                    }
                    String[] tagsArray = issue.get(i).getTag();
                    for (int k = 0; k < tagsArray.length; k++) { //compares tags[i] with tagsArray[k]
                        if (tags.length < j && tagsArray[k].equals(tags[j])) {
                            break label;
                        }
                    }
                    pq.add(issue.get(i));
                }
            }
        }
        ArrayList<Issue> return_value = new ArrayList<>();
        for (int i = 0; i < issue.size(); i++) {
            return_value.add(pq.poll());
        }
        return return_value;
    }

    private People searchpeople(String name) {
        for (int i = 0; i < people_Array.size(); i++) {
            if (name.equals(people_Array.get(i).getName())) {
                return people_Array.get(i);
            }
        }
        return null;
    }

    /**
     * @param index issue index enter issue window
     */
    public void entertheissue(int index) {
        issue.get(index).issuewindow(current_people);
    }

    public int issue_Arraysize() {
        return issue.size();
    }

    public Issue issuegetindex(int index) {
        return issue.get(index);
    }

    //--Comparator--
    /**
     * Comparator for sorting the list by Project ID
     */
    public static Comparator<Project> IDComparator = new Comparator<Project>() {
        @Override
        public int compare(Project o1, Project o2) {
            //for ascending order
            return o1.getID() - o2.getID();
        }
    };

    /**
     * Comparator for sorting the list by Project Name
     */
    public static Comparator<Project> NameComparator = new Comparator<Project>() {
        @Override
        public int compare(Project o1, Project o2) {
            //for ascending order
            return o1.getName().compareTo(o2.getName());
        }
    };

    /**
     * Comparator for sorting the list by Issues count
     */
    public static Comparator<Project> IssueCountComparator = new Comparator<Project>() {
        @Override
        public int compare(Project o1, Project o2) {
            //for ascending order
            return o1.issue_Arraysize() - o2.issue_Arraysize();
        }
    };

    //gui
    public void setupwindow() {

        //build window
        ImageIcon konoha = new ImageIcon("konoha_logo.jpg");
        frame.setLayout(null);
        frame.setTitle("firstPage");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(konoha.getImage());
        frame.setResizable(true);
        frame.setSize(1350, 730);
        frame.setLayout(null);
        frame.setVisible(true);
        //set backgroud picture
        label.setBounds(0, 0, 1350, 690);
        label.setVisible(true);

        // build search textfield
        search_issue.setBounds(0, 0, 200, 50);
        search_issue.setText("search");
        search_issue.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { //after mouse click 
                if (search_issue.getText().equals("search")) {
                    search_issue.setBounds(0, 0, 300, 50);
                    search_issue.setText("");
                    sreach.setBounds(300, 0, 100, 50);

                }
            }
        });
        Action action1 = new AbstractAction() { // after enter pressed
            @Override
            public void actionPerformed(ActionEvent e) {
                search_situation();
            }
        };
        search_issue.addActionListener(action1);
        search_issue.setVisible(true);
        search_issue.setEditable(true);

        //add issue button
        add_issue.setIcon(add_image);
        add_issue.setBounds(1180, 0, 50, 50);
        add_issue.setVisible(true);
        add_issue.setFocusable(true);
        add_issue.addActionListener(this);

        //add delete button
        delete_project.setIcon(delete_project_image);
        delete_project.setBounds(1230, 0, 50, 50);
        delete_project.setVisible(true);
        delete_project.setFocusable(true);
        delete_project.addActionListener(this);

        //add sort combobox
        sort_issue.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        for (int i = 0; i < sort_option.length; i++) {
            sort_issue.addItem(sort_option[i]);
        }
        sort_issue.setBounds(1000, 0, 150, 35);
        sort_issue.setVisible(true);
        sort_issue.addActionListener(this);

        //add the setting button or quit
        for (int i = 0; i < setting_option.length; i++) {
            setting_button.addItem(setting_option[i]);
        }
        setting_button.setVisible(true);
        setting_button.setBounds(1000, 35, 150, 35);
        setting_button.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        setting_button.addActionListener(this);
        setting_button.setBackground(Color.CYAN);
        setting_button.setOpaque(true);

        // build the included title and button
        //title
        include_text.setText("Include");
        include_text.setBackground(Color.black);
        include_text.setOpaque(true);
        include_text.setVisible(true);
        include_text.setForeground(Color.YELLOW);
        include_text.setFont(new Font("MV Boli", Font.PLAIN, 20));
        include_text.setBounds(0, 0, 100, 35);
        include_text.setBorder(BorderFactory.createEmptyBorder());

        //title panel
        include_title = new JPanel();
        include_title.setBackground(Color.black);
        include_title.setOpaque(true);
        include_title.setVisible(true);
        include_title.setLayout(null);
        include_title.setBounds(1000, 100, 330, 50);
        include_title.add(include_text);

        //build exclude title and button
        exclude_text.setText("Enclude");
        exclude_text.setBackground(Color.black);
        exclude_text.setOpaque(true);
        exclude_text.setVisible(true);
        exclude_text.setForeground(Color.YELLOW);
        exclude_text.setFont(new Font("MV Boli", Font.PLAIN, 20));
        exclude_text.setBounds(0, 0, 100, 35);
        exclude_text.setBorder(BorderFactory.createEmptyBorder());

        //title panel
        exluded_title = new JPanel();
        exluded_title.setBackground(Color.black);
        exluded_title.setOpaque(true);
        exluded_title.setVisible(true);
        exluded_title.setLayout(null);
        exluded_title.setBounds(1000, 400, 330, 50);
        exluded_title.add(exclude_text);
        //build the right_up black panel
        //build panel
        black_panel_up.setBackground(Color.black);
        black_panel_up.setPreferredSize(new Dimension(300, 2000));
        black_panel_up.setLayout(new FlowLayout());
        black_panel_up.setVisible(true);
        //build scroll
        black_ps_up.setBounds(1000, 150, 330, 250);
        black_ps_up.getVerticalScrollBar().setPreferredSize(new Dimension(1, 0));
        black_ps_up.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 1));
        black_ps_up.setVisible(true);

        //build the right_down black panel
        //build panel
        black_panel_down.setBackground(Color.black);
        black_panel_down.setPreferredSize(new Dimension(300, 2000));
        black_panel_down.setLayout(new FlowLayout());
        black_panel_down.setVisible(true);
        //build scroll
        black_ps_down.setBounds(1000, 450, 330, 250);
        black_ps_down.getVerticalScrollBar().setPreferredSize(new Dimension(1, 0));
        black_ps_down.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 1));
        black_ps_down.setVisible(true);
        include_state_text.setText("Include State");
        include_state_text.setBackground(Color.GREEN);
        include_state_text.setOpaque(true);
        include_state_text.setVisible(true);
        include_state_text.setForeground(Color.BLUE);
        include_state_text.setFont(new Font("MV Boli", Font.PLAIN, 20));
        include_state_text.setBounds(0, 0, 160, 35);
        include_state_text.setBorder(BorderFactory.createEmptyBorder());
        //title panel
        include_state_title = new JPanel();
        include_state_title.setBackground(Color.GREEN);
        include_state_title.setOpaque(true);
        include_state_title.setVisible(true);
        include_state_title.setLayout(null);
        include_state_title.setBounds(1000, 400, 160, 50);
        include_state_title.add(include_state_text);
//        include_title.add(include_go);

        //build exclude title and button
        exclude_state_text.setText("Enclude State");
        exclude_state_text.setBackground(Color.GREEN);
        exclude_state_text.setOpaque(true);
        exclude_state_text.setVisible(true);
        exclude_state_text.setForeground(Color.BLUE);
        exclude_state_text.setFont(new Font("MV Boli", Font.PLAIN, 20));
        exclude_state_text.setBounds(0, 0, 160, 35);
        exclude_state_text.setBorder(BorderFactory.createEmptyBorder());
        //title panel
        exluded_state_title = new JPanel();
        exluded_state_title.setBackground(Color.GREEN);
        exluded_state_title.setOpaque(true);
        exluded_state_title.setVisible(true);
        exluded_state_title.setLayout(null);
        exluded_state_title.setBounds(1160, 400, 160, 50);
        exluded_state_title.add(exclude_state_text);
//        exluded_title.add(exclude_go);
        //build the right_up black panel
        //build panel
        green_panel_up.setBackground(Color.GREEN);
        green_panel_up.setPreferredSize(new Dimension(160, 2000));
        green_panel_up.setLayout(new FlowLayout());
        green_panel_up.setVisible(true);
        //build scroll
        green_ps_up.setBounds(1000, 450, 160, 250);
        green_ps_up.getVerticalScrollBar().setPreferredSize(new Dimension(1, 0));
        green_ps_up.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 1));
        green_ps_up.setVisible(true);

        //build the right_down black panel
        //build panel
        green_panel_down.setBackground(Color.GREEN);
        green_panel_down.setPreferredSize(new Dimension(160, 2000));
        green_panel_down.setLayout(new FlowLayout());
        green_panel_down.setVisible(true);
        //build scroll
        green_ps_down.setBounds(1160, 450, 160, 250);
        green_ps_down.getVerticalScrollBar().setPreferredSize(new Dimension(1, 0));
        green_ps_down.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 1));
        green_ps_down.setVisible(true);
        // build search go button
        sreach.setVisible(true);
        sreach.setBounds(200, 0, 100, 50);
        sreach.addActionListener(this);
        sreach.setFocusable(true);

        //build table
        for (int i = 0; i < column.length; i++) {
            tableModel.addColumn(column[i]);
        }
        table.setFocusable(true);
        table.setRowSelectionAllowed(true);

        //set columm width
        table.getColumnModel().getColumn(0).setPreferredWidth(1);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(1);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());

                System.out.println("row : " + row + " col : " + col);
            }
        });
        //set table scroll
        table_scroll.setBounds(0, 100, 1000, 550);
        table_scroll.setVisible(true);

        //set adding issue panel
        add_issue_panel.setBackground(Color.red);
        add_issue_panel.setOpaque(true);
        add_issue_panel.setBounds(0, 100, 1000, 550);
        add_issue_panel.setLayout(null);
        add_issue_panel.setVisible(false);
        // set name text 
        name_text.setBounds(0, 0, 150, 50);
        name_text.setEditable(true);
        name_text.setVisible(true);
        name_text.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        name_text.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { //after mouse click 
                if (name_text.getText().equals("Enter issue name")) {
                    name_text.setBounds(0, 0, 300, 50);
                    name_text.setText("");
                }
            }
        });
        //set the priority 
        priority.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        for (int i = 0; i < list_priority.length; i++) {
            priority.addItem(list_priority[i]);
        }
        priority.setBounds(900, 0, 100, 35);
        priority.setVisible(true);
        priority.addActionListener(this);
        //build priority text
        priop_text.setBounds(850, 0, 50, 35);
        priop_text.setText("Priority");
        priop_text.setVisible(true);
        priop_text.setBackground(Color.red);
        priop_text.setOpaque(true);
        priop_text.setForeground(Color.yellow);
        //build assignee
        assignee_text.setBounds(0, 60, 150, 50);
        assignee_text.setEditable(true);
        assignee_text.setVisible(true);
        assignee_text.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        assignee_text.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { //after mouse click 
                if (assignee_text.getText().equals("Enter  assignee")) {
                    assignee_text.setBounds(0, 60, 300, 50);
                    assignee_text.setText("");
                }
            }
        });
        //build tags
        tags_text.setBounds(0, 120, 150, 50);
        tags_text.setEditable(true);
        tags_text.setVisible(true);
        tags_text.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        tags_text.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { //after mouse click 
                if (tags_text.getText().equals("Enter tags")) {
                    tags_text.setBounds(0, 120, 300, 50);
                    tags_text.setText("");
                }
            }
        });
        //build issus description
        descrip.setSize(new Dimension(1100, 5000));
        descrip.setText("Add description there");
        descrip.setEditable(true);
        descrip.setVisible(true);
        descrip.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { //after mouse click 
                if (descrip.getText().equals("Add description there")) {
                    descrip.setText("");
                }
                if (undo_pressed == true || redo_pressed == true) {
                    undo_pressed = false;
                    redo_pressed = false;
                    text_undo.clear();
                    text_redo.clear();
                    exit = false;
                    handle_thread();
                }

            }
        });
        Action action2 = new AbstractAction() { // after enter pressed
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("enter key pressed for search" + descrip.getText());
            }
        };
        //build descrip scroll
        desc_sp.setBounds(0, 180, 1100, 320);
        desc_sp.setVisible(true);
        desc_sp.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        desc_sp.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));

        //build undo button
        undo.setBounds(900, 120, 50, 50);
        undo.addActionListener(this);
        undo.setVisible(true);
        undo.setFocusable(true);
        undo.setIcon(undo_icon);
        //build reundo button
        reundo.setBounds(950, 120, 50, 50);
        reundo.setVisible(true);
        reundo.addActionListener(this);
        reundo.setFocusable(true);
        reundo.setIcon(redo_icon);
        //build submit button
        submit.setBounds(900, 500, 100, 50);
        submit.setVisible(true);
        submit.addActionListener(this);
        submit.setFocusable(true);
        //add componet to adding panel
        add_issue_panel.add(priop_text);
        add_issue_panel.add(submit);
        add_issue_panel.add(name_text);
        add_issue_panel.add(priority);
        add_issue_panel.add(tags_text);
        add_issue_panel.add(assignee_text);
        add_issue_panel.add(desc_sp);
        add_issue_panel.add(undo);
        add_issue_panel.add(reundo);

        //add componet to frame
        frame.add(sreach);
        frame.add(add_issue_panel);
        frame.add(search_issue);
        frame.add(add_issue);
        frame.add(delete_project);
        frame.add(sort_issue);
        frame.add(setting_button);
        frame.add(include_title);
        frame.add(exluded_title);
        frame.add(black_ps_up);
        frame.add(black_ps_down);
        frame.add(include_state_title);
        frame.add(exluded_state_title);
        frame.add(green_ps_up);
        frame.add(green_ps_down);
        frame.add(table_scroll);
        frame.add(label);
        //sample method calling 

    }

    public void include_and_exluded(ArrayList<String> arr) {
        include.clear();
        exclude.clear();
        for (int i = 0; i < arr.size(); i++) {

            //add tags to include button
            include.add(new JCheckBox());
            include.get(i).setText(arr.get(i));
            include.get(i).setFocusable(true);
            include.get(i).setLayout(null);
            include.get(i).setFont(new Font("Consolas", Font.PLAIN, 25));
            include.get(i).setSize(new Dimension(300, 100));
            include.get(i).setVisible(true);
            black_panel_up.add(include.get(i));

            //add tags to exclude button
            exclude.add(new JCheckBox());
            exclude.get(i).setText(arr.get(i));
            exclude.get(i).setFocusable(true);
            exclude.get(i).setFont(new Font("Consolas", Font.PLAIN, 25));
            exclude.get(i).setSize(new Dimension(300, 100));
            exclude.get(i).setVisible(true);
            black_panel_down.add(exclude.get(i));
        }
    }

    public void include_and_exluded_state(ArrayList<String> arr) {
        include_state.clear();
        exclude_state.clear();
        for (int i = 0; i < arr.size(); i++) {

            //add tags to include button
            include_state.add(new JCheckBox());
            include_state.get(i).setText(arr.get(i));
            include_state.get(i).setFocusable(true);
            include_state.get(i).setLayout(null);
            include_state.get(i).setFont(new Font("Consolas", Font.PLAIN, 10));
            include_state.get(i).setSize(new Dimension(300, 1000));
            include_state.get(i).setVisible(true);
            green_panel_up.add(include_state.get(i));

            //add tags to exclude button
            exclude_state.add(new JCheckBox());
            exclude_state.get(i).setText(arr.get(i));
            exclude_state.get(i).setFocusable(true);
            exclude_state.get(i).setFont(new Font("Consolas", Font.PLAIN, 10));
            exclude_state.get(i).setSize(new Dimension(300, 100));
            exclude_state.get(i).setVisible(true);
            green_panel_down.add(exclude_state.get(i));
        }
    }

    public void reset_table(String[][] data) {
        tableModel.setRowCount(0);
        for (int i = 0; i < data.length; i++) {
            tableModel.addRow(data[i]);
        }
        frame.repaint();
    }

    public void reset_table(ArrayList<Issue> data) {
        tableModel.setRowCount(0);
        for (int i = 0; i < data.size(); i++) {
            String[] arow = new String[8];
            arow[0] = data.get(i).getID() + "";
            arow[1] = data.get(i).getTitle() + "";
            arow[2] = data.get(i).getStatus() + "";
            arow[3] = Arrays.toString(data.get(i).getTag()) + "";
            arow[4] = data.get(i).getPriority() + "";
            arow[5] = data.get(i).getTimestamp() + "";
            arow[6] = data.get(i).getAssignee().getName() + "";
            arow[7] = data.get(i).getCreator().getName() + "";
            tableModel.addRow(arow);
        }
        frame.repaint();
    }

    public void handle_thread() {
        Thread thread = new Thread() {
            public void run() {
                while (exit == false) {
                    text_undo.push(descrip.getText());
                    try {
                        sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        thread.start();
    }
//    name, tags ,priority, assignee

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add_issue) {
            add_issue_panel.setVisible(true);
            table_scroll.setVisible(false);
            System.out.println("add issue button pressed");
            handle_thread();
        }
        if (e.getSource() == delete_project) {
            System.out.println("delete project button pressed");
            deleteThisProject();
            this.frame.setVisible(false);
            window_frame.setVisible(true);
        }
        if (e.getSource() == sort_issue) {  // this can short but left first 
            if (sort_issue.getSelectedIndex() == 0) {
                sortIssueBased(0);
                System.out.println("sort based on ID selected");
            }
            if (sort_issue.getSelectedIndex() == 1) {
                sortIssueBased(1);
                System.out.println("sort based on Priority sselected");
            }
            if (sort_issue.getSelectedIndex() == 2) {
                sortIssueBased(2);
                System.out.println("sort based on IssueCount sselected");
            }
        }
        if (e.getSource() == setting_button) {
            switch (setting_button.getSelectedIndex()) {
                case 0:
                    System.out.println("changlog displaying");
                    break;
                case 1:
                    this.frame.setVisible(false);
                    window_frame.setVisible(true);
                    System.out.println("quitting ");
                    break;
            }
        }
        if (e.getSource() == undo) {
            if (!text_undo.isEmpty()) {
                exit = true;
                undo_pressed = true;
                exit = true;
                text_redo.push(text_undo.peek());
                descrip.setText(text_undo.pop());
                System.out.println("undo");
            }
        }
        if (e.getSource() == reundo) {
            if (!text_redo.isEmpty()) {
                exit = true;
                redo_pressed = true;
                exit = true;
                text_undo.push(text_redo.peek());
                descrip.setText(text_redo.pop());
                System.out.println("reundo");
            }
        }
        if (e.getSource() == submit) {
            add_issue_panel.setVisible(false);
            table_scroll.setVisible(true);
            String issue_name = name_text.getText();
            String tags = tags_text.getText();
            String[] issue_tags_array = tags.split(",");
            String description = descrip.getText();
            int priop = priority.getSelectedIndex() + 1;
            String assignee = assignee_text.getText();
            People assignee_obj = searchpeople(assignee);
            if (assignee_obj == null) {
                popwindow("user issue", "invalid people");
            } else {
                Issue iss = new Issue(numissue, issue_name, description, current_people, assignee_obj, issue_tags_array, priop, this);
                issue.add(iss);
                assignee_obj.addAssigned(this.ID, this.name, numissue, issue_name, current_people.getName());
                numissue++;
                exit = true;
                System.out.println("issue added");
                reset_table(issue);
            }
        }
        if (e.getSource() == sreach) {
            System.out.println("sreach");
            search_situation();
        }
    }

    public void search_situation() {
        String include_tags = "";
        String exclude_tags = "";
        String includestate = "";
        String excludestate = "";
        String keyword = search_issue.getText();
        for (int i = 0; i < include.size(); i++) {
            if (include.get(i).isSelected()) {
                include_tags += "#" + include.get(i).getText();
            }
        }
        for (int i = 0; i < exclude.size(); i++) {
            if (exclude.get(i).isSelected()) {
                exclude_tags += "#" + exclude.get(i).getText();
            }
        }
        for (int i = 0; i < include_state.size(); i++) {
            if (include_state.get(i).isSelected()) {
                includestate += "#" + include_state.get(i).getText();
            }
        }
        for (int i = 0; i < exclude_state.size(); i++) {
            if (exclude_state.get(i).isSelected()) {
                excludestate += "#" + exclude_state.get(i).getText();
            }
        }
        ArrayList<Issue> issue_table = filterout_withreturn(exclude_tags, excludestate);
        issue_table = filterin(include_tags, includestate, issue_table);
        search(keyword, issue_table);

    }

    public void popwindow(String title, String content) {
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.WARNING_MESSAGE);
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
        Project temp = dm.readProjectData();
        this.people_Array = temp.people_Array;
        this.ID = temp.ID;
        this.name = temp.name;
        this.owner = temp.owner;
        this.issue = temp.issue;
    }

    // -- Getter methods --
    public ArrayList<Issue> getIssue() {
        return issue;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public PeopleADT getPeople_Array() {
        return people_Array;
    }

    public People getOwner() {
        return owner;
    }

    //Setter method
    public void setIssue(ArrayList<Issue> issue) {
        this.issue = issue;
    }

    public void setPeople_Array(PeopleADT people_Array) {
        this.people_Array = people_Array;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(People owner) {
        this.owner = owner;
    }

    public static void setDm(DataManagement dm) {
        Project.dm = dm;
    }
}
