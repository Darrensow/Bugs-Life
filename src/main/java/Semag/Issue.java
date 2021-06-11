package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@JsonIgnoreProperties(value = {"current_people", "timeComparator", "priorityComparator", "idComparator", "titleComparator", "statusComparator", "tagComparator", "TitleComparator", "IDComparator", "project_belongsTo"})
public class Issue implements ActionListener, MouseListener, Comparator<Issue> {

    private ArrayList<String> changelog = new ArrayList<>();
    private Integer ID;
    private String title;
    private String descriptionText;
    private String creator;
    private String assignee;
    private ArrayList<Comment> comments = new ArrayList<>();
    private int numberOfComments;
    private String[] tag;
    private Integer priority;
    private String status;
    private String timestamp;
    transient Scanner sc = new Scanner(System.in);
    private People current_people;
    private File image_file;

    public static ArrayList<String> tagsOption = new ArrayList<>();

    public static final String[] statusOption = {"Open", "In Progress", "Solved", "Closed"};

    private Project project_belongsTo;      //for the delete function

    //gui
    JFrame frame = new JFrame();
    ImageIcon backgroud_image = new ImageIcon("register backgroud.jpg");
    JLabel label = new JLabel(backgroud_image);
    JButton add_comment = new JButton();
    ImageIcon add_image = new ImageIcon("add.jpg");
    JPanel add_comment_panel = new JPanel();
    JLabel add_comment_title = new JLabel();
    JButton add_comment_submit = new JButton();
    JTextPane add_comment_text = new JTextPane();
    JScrollPane add_comment_sp = new JScrollPane(add_comment_text);
    JButton undo = new JButton();
    JButton redo = new JButton();
    ImageIcon undo_icon = new ImageIcon("undo.png");
    ImageIcon redo_icon = new ImageIcon("redo.png");
    ImageIcon delete_issue_image = new ImageIcon("trash_icon.png");
    JButton delete_issue = new JButton();
    JButton open_changelog = new JButton();
    JPanel mid_panel = new JPanel();
    JPanel issue_des_panel = new JPanel();
    JTextArea issue_descrip = new JTextArea();
    JScrollPane issue_descrip_sp = new JScrollPane(issue_descrip);
    ArrayList<JPanel> comment_panel = new ArrayList<>();
    ArrayList<JButton> like = new ArrayList<>();
    ArrayList<JButton> dislike = new ArrayList<>();
    ArrayList<JButton> happy = new ArrayList<>();
    ArrayList<JButton> angry = new ArrayList<>();
    ArrayList<JLabel> happycount = new ArrayList<>();
    ArrayList<JLabel> angrycount = new ArrayList<>();
    ArrayList<JLabel> likecount = new ArrayList<>();
    ArrayList<JLabel> dislikecount = new ArrayList<>();

    ImageIcon happy_icon = new ImageIcon("happy_icon.png");
    ImageIcon angry_icon = new ImageIcon("angry_icon.png");
    ImageIcon like_image = new ImageIcon("like_icon.png");
    ImageIcon dislike_image = new ImageIcon("dislike_icon.png");

    ImageIcon happy_icon_blue = new ImageIcon("happy_icon_blue.jpg");
    ImageIcon angry_icon_blue = new ImageIcon("angry_icon_blue.jpg");
    ImageIcon like_image_blue = new ImageIcon("like_icon_blue.png");
    ImageIcon dislike_image_blue = new ImageIcon("dislike_icon_blue.jpg");


    ArrayList<JTextPane> comment = new ArrayList<>();
    JScrollPane issue_scroll = new JScrollPane(mid_panel);
    ArrayList<JScrollPane> comment_sr = new ArrayList<>();
    JPanel edit_panel = new JPanel();
    JLabel edit_priop_title = new JLabel();
    JComboBox edit_priop = new JComboBox();
    JComboBox edit_status = new JComboBox();
    JTextField edit_tags = new JTextField();
    JTextArea edit_descrip = new JTextArea();
    JScrollPane edit_descrip_scroll = new JScrollPane(edit_descrip);

    JLabel edit_change_image = new JLabel();
    ImageIcon edit_insert_image;
    JButton edit_image_button = new JButton("Change image");

    JButton done_edit = new JButton();
    JButton insert_image_button = new JButton("Insert image");
    ImageIcon insert_image;
    ImageIcon edit = new ImageIcon("edit_icon.png");
    JButton edit_issue = new JButton();
    JLabel image = new JLabel();
    boolean undo_pressed = false;
    boolean redo_pressed = false;
    boolean exit = false;
    Text<String> text_undo = new Text<>();
    Text<String> text_redo = new Text<>();

    JTextArea changelog_textarea = new JTextArea();
    JScrollPane changelog_textsp = new JScrollPane(changelog_textarea);
    JButton quitchangelog = new JButton("Quit changelog");

    JFrame project_frame;
    boolean isowner = false;

    public Issue() {
    }

    public String getAssignee() {
        return assignee;
    }

    public Issue(Integer ID, String title, String text, String creator, String assignee, String[] tag, int priop, Project project_belongsTo) {
        changelog = new ArrayList<>();  //only create the changelog Arraylist when a Issue is created
        this.ID = ID;
        this.title = title;
        this.creator = creator;
        this.assignee = assignee;
        this.descriptionText = text;
        this.status = "Open";
        this.priority = priop;
        this.tag = tag;
        this.project_belongsTo = project_belongsTo;
        this.timestamp = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(new java.util.Date(Instant.now().getEpochSecond() * 1000));
    }
//add comment  // change properties //react done //quit // variable bolean,arraylist<integer>


    public void issuewindow(People current_people, JFrame frame) {  //return one index ,int 0,1  arraylist<integer>
        this.current_people = current_people;
        project_frame = frame;

        setupwindow();
        set_comment(comments);
        print();//while(boolean)
        cheacowner();
        keeprenew();
    }

    public void cheacowner() {
        if (this.creator.equals(current_people.getName()) || this.assignee.equals(current_people.getName())) {
            isowner = true;
            edit_issue.setVisible(true);
        } else {
            isowner = false;
            edit_issue.setVisible(false);
        }
    }

    public void keeprenew() {
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    print();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }


    /**
     * Reacting to a comment. Reacts by first checking that whether the user has reacted before
     * @param commentID Comment that was reacted on
     * @param reaction Reaction that was pressed
     * @param current_people The user who reacted
     */
    public void reactComment(int commentID, String reaction, People current_people) {
        this.comments.get(commentID).addReaction(reaction, current_people.getName());
    }

    public void first_run_setcommentcolor(ArrayList<Comment> com) {
        for (int i = 0; i < happy.size(); i++) {
            if (com.get(i).reactedBefore("happy", current_people.getName())) {
                happy.get(i).setIcon(happy_icon_blue);
            } else {
                happy.get(i).setIcon(happy_icon);
            }
        }
        for (int i = 0; i < angry.size(); i++) {
            if (com.get(i).reactedBefore("angry", current_people.getName())) {
                angry.get(i).setIcon(angry_icon_blue);
            } else {
                angry.get(i).setIcon(angry_icon);
            }
        }
        for (int i = 0; i < like.size(); i++) {
            if (com.get(i).reactedBefore("like", current_people.getName())) {
                like.get(i).setIcon(like_image_blue);
            } else {
                happy.get(i).setIcon(like_image);
            }
        }
        for (int i = 0; i < dislike.size(); i++) {
            if (com.get(i).reactedBefore("dislike", current_people.getName())) {
                happy.get(i).setIcon(dislike_image_blue);
            } else {
                happy.get(i).setIcon(dislike_image);
            }
        }
    }

    public void cheack_reaction_color(int index, ArrayList<Comment> com) {
        if (com.get(index).reactedBefore("happy", current_people.getName())) {
            happy.get(index).setIcon(happy_icon_blue);
        } else {
            happy.get(index).setIcon(happy_icon);
        }
        if (com.get(index).reactedBefore("angry", current_people.getName())) {
            angry.get(index).setIcon(angry_icon_blue);
        } else {
            angry.get(index).setIcon(angry_icon);
        }
        if (com.get(index).reactedBefore("likes", current_people.getName())) {
            like.get(index).setIcon(like_image_blue);
        } else {
            like.get(index).setIcon(like_image);
        }
        if (com.get(index).reactedBefore("dislikes", current_people.getName())) {
            dislike.get(index).setIcon(dislike_image_blue);
        } else {
            dislike.get(index).setIcon(dislike_image);
        }
    }


    /**
     * Method to update tags
     *
     * @return A message that will be added to the change log regarding the
     * change of tags
     */
    private String updateTag() {
        StringBuilder sb = new StringBuilder();
        sb.append(current_people.getName()).append(" updated the tags at ").append(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(new java.util.Date(Instant.now().getEpochSecond() * 1000))).append("\n");
        sb.append("Original: ").append(showAllTags(this.tag)).append("\n");  //append original tags

        System.out.println("Please input the tags you want to update. (Format: 'Tag1 Tag2 Tag3 ... TagN'");
        String input = sc.nextLine();
        String[] tagsArray = input.split(" ");      //might have logic error if user give "abcabcabc" or "", might not be a problem
        this.tag = tagsArray;

        sb.append("Edited: ").append(showAllTags(tagsArray)).append("\n");   //append new tags

        return sb.toString();
    }

    //helper method to return String representation of all the tags
    private String showAllTags(String[] tagsArray) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < tagsArray.length; i++) {
            if (i < tagsArray.length - 1) {
                sb.append(tagsArray[i]).append(", ");
            } else {
                sb.append(tagsArray[i]).append("]");
            }
        }
        return sb.toString();
    }

    /**
     * Method to return a String of the tags in '#Tag1#Tag2#Tag3' format for GUI
     * implementation
     *
     * @return String of the tags array
     */
    private String returnTagsGUI() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.tag.length; i++) {
            sb.append("#").append(this.tag[i]);
        }
        return sb.toString();
    }


    /*
        The method below is to update the variables returned by the GUI.
        The String format of one of the change log is as of below.

        -----
        "========= Time: dd/MM/yyyy HH:mm:ss z =========="
        "<Change description>\n"
        "\n"
        -----

     */


    //Method to add the changes made to the ChangeLog
    private void updateChangeLog(int priorityFromGUI, String tagsFromGUI, String descriptionFromGUI, String statusFromGUI) {
        StringBuilder sb = new StringBuilder();

        String priorityStr = updatePriorityGUI(priorityFromGUI);
        String tagsStr = updateTagsGUI(tagsFromGUI);
        String textStr = updateTextGUI(descriptionFromGUI);
        String statusStr = updateStatusGUI(statusFromGUI);

        if (!priorityStr.equals("") || !tagsStr.equals("") || !textStr.equals("") || !statusStr.equals("")) {
            sb.append("========= Time : ").append(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(new java.util.Date(Instant.now().getEpochSecond() * 1000))).append(" ==========\n");
        }

        //If "", then points out that no changes were made.
        if (!priorityStr.equals("")) {
            sb.append("   - ").append(priorityStr);
        }
        if (!tagsStr.equals("")) {
            sb.append("   - ").append(tagsStr);
        }
        if (!textStr.equals("")) {
            sb.append("   - ").append(textStr);
        }
        if (!statusStr.equals("")) {
            sb.append("   - ").append(statusStr);
        }
        sb.append("\n");
        this.changelog.add(sb.toString());
    }

    /**
     * Helper method for updateChangeLog().
     *
     * @param fromGUI String input from GUI
     * @return String to be updated, if "", then means no changes were made.
     */
    private String updateTagsGUI(String fromGUI) {
        String[] updatedTags = fromGUI.substring(1).split("#");                // tags returned from GUI
        StringBuilder sb = new StringBuilder();

        if (this.tag.length == updatedTags.length) {
            if (sameTags(this.tag, updatedTags)) {
                System.out.println("Same tags, don't need to anything");
                return "";                                  // don't update and add to changelog
            } else {
                System.out.println("Same array length but different tags inside");
                sb.append(pushTagsUpdate(updatedTags));     // can delete sb.append after debugging
            }
        } else {
            System.out.println("Different array length");
            sb.append(pushTagsUpdate(updatedTags));         // can delete sb.append after debugging
        }
        System.out.println("Inside updateTagsGUI");
        System.out.println(sb.toString());
        return sb.toString();
    }

    //Helper method for updateTagsGUI() to check if the tags are the same when both array size are equals
    private boolean sameTags(String[] array1, String[] array2) {
        if (array1.length != array2.length) {
            return false;
        }

        // Hash map
        Map<String, Integer> map = new HashMap<>();
        int count = 0;
        // Store arr1[] elements and their counts in
        for (int i = 0; i < array1.length; i++) {
            if (map.get(array1[i]) == null)             // no key exists in the map
                map.put(array1[i], 1);
            else {
                count = map.get(array1[i]);
                count++;
                map.put(array1[i], count);
            }
        }

        // Traverse array2 to check if all the elements in it has the same count as array1
        for (int i = 0; i < array1.length; i++) {
            if (!map.containsKey(array2[i]))            // There exists no key of array2[i] inside array1
                return false;

            if (map.get(array2[i]) == 0)                // If an element of array2 appears more times than it appears in array1
                return false;

            count = map.get(array2[i]);                 // Get the count of array2[i] from the Map
            --count;                                    // Subtract by 1
            map.put(array2[i], count);                  // Update the value into the Map
        }
        return true;
    }

    //Helper method for updateTagsGUI() to push updates onto changelog
    private String pushTagsUpdate(String[] updatedTags) {
        StringBuilder sb = new StringBuilder();
        sb.append(current_people.getName()).append(" updated the tags.").append("\n");
        sb.append("      Original : ").append(showAllTags(this.tag)).append("\n");         // append original tags
        sb.append("      Edited   : ").append(showAllTags(updatedTags)).append("\n");      // append new tags
        this.tag = updatedTags;                                                     // update the tags array with the new tags
        return sb.toString();
    }

    /**
     * Helper method for updateChangeLog().
     *
     * @param fromGUI String input from GUI
     * @return String to be updated, if "", then means no changes were made.
     */
    private String updatePriorityGUI(int fromGUI) {
        StringBuilder sb = new StringBuilder();
        if (this.priority != fromGUI) {
            sb.append(current_people.getName()).append(" updated the priority from ").append(this.priority).append(" to ").append(fromGUI).append(".\n");
            this.priority = fromGUI;                                             // update priority with new priority from GUI
        }
        System.out.println("Inside Priority GUI");
        System.out.println(sb.toString());
        return sb.toString();                                                   // default value is "" if no changes were made
    }

    /**
     * Helper method for updateChangeLog().
     *
     * @param fromGUI String input from GUI
     * @return String to be updated, if "", then means no changes were made.
     */
    private String updateStatusGUI(String fromGUI) {
        StringBuilder sb = new StringBuilder();
        // Add or deduct resolved
        if (fromGUI.equals("Resolved")) {                                       // increase the number of issues solved for current_people
            Window.resolvedAnIssue(current_people);
        } else if (fromGUI.equals("Reopen")) {
            Window.reopenedAnIssue(current_people);                             // decrease the number of issues solved for current_people
        }

        if (!this.status.equals(fromGUI)) {
            if (fromGUI.equals("Reopen")) {                                     // special case when status is 'Reopened'
                fromGUI = "Open";                                               // "Reopen -> Open", so that won't have issues with the algo
                sb.append(current_people.getName()).append(" reopened the issue.").append(".\n");
            } else {
                sb.append(current_people.getName()).append(" updated the status from '").append(this.status).append("' to '").append(fromGUI).append("'").append(".\n");
            }
            this.status = fromGUI;                                               // update status with new status from GUI
//            this.changelog.add(sb.toString());                                   // add change description changelog
        }
        System.out.println("Inside updateStatusGUI");
        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * Helper method for updateChangeLog().
     *
     * @param fromGUI String input from GUI
     * @return String to be updated, if "", then means no changes were made.
     */
    private String updateTextGUI(String fromGUI) {
        StringBuilder sb = new StringBuilder();
        if (!this.descriptionText.equals(fromGUI)) {
            sb.append(current_people.getName()).append(" updated the description text.").append("\n");
            this.descriptionText = fromGUI;                                      // update the Description text with the new input from GUI
//            this.changelog.add(sb.toString());                                   // add change description changelog
        }
        System.out.println("Inside updateTextGUI");
        System.out.println(sb.toString());
        return sb.toString();
    }


    //Display the options that the user is able to choose to update the status of the issue
    public void return_option() {
        String originalStatus = this.getStatus();
        ArrayList<String> option_state = new ArrayList<>();
        option_state.add(originalStatus);
        if (originalStatus.equalsIgnoreCase("Open")) {
            option_state.add("In Progress");
            option_state.add("Resolved");
            option_state.add("Closed");
        } else if (originalStatus.equalsIgnoreCase("In Progress")) {
            option_state.add("Closed");
            option_state.add("Resolved");
        } else if (originalStatus.equalsIgnoreCase("Resolved")) {
            option_state.add("Closed");
            option_state.add("Reopen");
        } else if (originalStatus.equalsIgnoreCase("Closed")) {
            option_state.add("Reopen");
        }
        set_status(option_state);
    }





    public void print() {
        print(this);
    }

    /**
     * Show full Issue page details
     */
    public void print(Issue o) {
        StringBuilder sb = new StringBuilder();

        String issueInfo = String.format("Issue ID: %-20sStatus: %-20s\n"
                        + "Tag: %-50s\nPriority: %-20sCreated On: %-20s\n"
                        + "Title: %-40s\n"
                        + "Assigned to: %-20sCreated by: %-20s\n\n",
                o.getID(), o.getStatus(),
                o.returnAllTags(o), o.getPriority(), o.getTimestamp(),
                o.getTitle(),
                o.getAssignee(), o.getCreator());
        sb.append(issueInfo);
        sb.append("Issue Description\n-----------");
        sb.append("\n" + o.getDescriptionText());
        issue_descrip.setText(sb.toString());
    }

    //Helper method to print(Issue o), returns all the tags in String format
    private String returnAllTags(Issue o) {
        StringBuilder sb = new StringBuilder();
        String[] tagsArray = o.getTag();
        for (int i = 0; i < tagsArray.length; i++) {
            if (i < tagsArray.length - 1) {
                sb.append(tagsArray[i]).append(", ");
            } else {
                sb.append(tagsArray[i]);

            }
        }
        return sb.toString();
    }


    /*
        -- Comparator Object --
     */
    /**
     * Comparator for sorting the list by Priority
     */
    public static Comparator<Issue> priorityComparator = new Comparator<Issue>() {
        @Override
        public int compare(Issue o1, Issue o2) {
            //for descending order
            return o2.getPriority() - o1.getPriority();
        }
    };

    /**
     * Comparator for sorting the list by Time
     */
    public static Comparator<Issue> timeComparator = new Comparator<Issue>() {
        @Override
        public int compare(Issue o1, Issue o2) {
            return o1.getID().compareTo(o2.getID());
        }
    };

    /**
     * Comparator for sorting the list by Project ID
     */
    public static Comparator<Issue> IDComparator = new Comparator<Issue>() {
        @Override
        public int compare(Issue o1, Issue o2) {
            //for ascending order
            return o1.getID() - o2.getID();
        }
    };

    /**
     * Comparator for sorting the list by Title
     */
    public Comparator<Issue> TitleComparator = new Comparator<Issue>() {
        @Override
        public int compare(Issue o1, Issue o2) {
            //for ascending order
            return o1.getTitle().compareTo(o2.getTitle());
        }
    };

    /**
     * Comparator for sorting the list by Status Alphabetical
     */
    public Comparator<Issue> statusComparator = new Comparator<Issue>() {
        @Override
        public int compare(Issue o1, Issue o2) {
            return o1.getStatus().compareTo(o2.getStatus());
        }
    };

    /**
     * Comparator for sorting the list by Tag
     */
    public Comparator<Issue> tagComparator = new Comparator<Issue>() {
        @Override
        public int compare(Issue o1, Issue o2) {
            return o1.getStatus().compareTo(o2.getStatus());
        }
    };

    //gui method
    public void setupwindow() {

        //build window
        ImageIcon konoha = new ImageIcon("doge_image.jpg");
        frame.setLayout(null);
        frame.setTitle("ISSUE PAGE");
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                project_frame.setVisible(true);
            }
        });
        frame.setIconImage(konoha.getImage());
        frame.setResizable(false);
        frame.setSize(1350, 730);
        frame.setLayout(null);
        frame.setVisible(true);
        //set backgroud picture
        label.setBounds(0, 0, 1350, 690);
        label.setVisible(true);

        //add comment button
        add_comment.setIcon(add_image);
        add_comment.setBounds(1180, 0, 50, 50);
        add_comment.setVisible(true);
        add_comment.setFocusable(true);
        add_comment.addActionListener(this);
        //build add comment panel
        add_comment_panel.setBackground(Color.blue);
        add_comment_panel.setOpaque(true);
        add_comment_panel.setVisible(false);
        add_comment_panel.setBounds(100, 100, 1000, 600);
        add_comment_panel.setLayout(null);

        //build add comment title
        add_comment_title.setText("Add Comment");
        add_comment_title.setFont(new Font("MV Boli", Font.PLAIN, 24));
        add_comment_title.setBounds(400, 0, 200, 50);
        add_comment_title.setVisible(true);
        add_comment_title.setBackground(Color.blue);
        add_comment_title.setOpaque(true);
        //build add comment text
        add_comment_text.setVisible(true);
        add_comment_text.setPreferredSize(new Dimension(900, 100000));
        add_comment_text.setEditable(true);
        add_comment_text.setText("Enter comment there");
        add_comment_text.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { //after mouse click
                if (add_comment_text.getText().equals("Enter comment there")) {
                    add_comment_text.setText("");
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
        //build submit button
        add_comment_submit.setBounds(900, 550, 100, 50);
        add_comment_submit.setText("Submit");
        add_comment_submit.setVisible(true);
        add_comment_submit.setFocusable(true);
        add_comment_submit.addActionListener(this);
        //add undo buttomn
        undo.setBounds(900, 0, 50, 50);
        undo.setIcon(undo_icon);
        undo.setVisible(true);
        undo.setFocusable(true);
        undo.addActionListener(this);
        //build redo button
        redo.setBounds(950, 0, 50, 50);
        redo.setVisible(true);
        redo.setIcon(redo_icon);
        redo.setFocusable(true);
        redo.addActionListener(this);
        //build insert image button
        insert_image_button.setVisible(true);
        insert_image_button.setBounds(650, 0, 100, 50);
        insert_image_button.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        insert_image_button.addActionListener(this);
        insert_image_button.setFocusable(true);
        //build label image
        image.setVisible(true);
        image.setBounds(750, 0, 100, 50);
        image.setFocusable(true);
        image.addMouseListener(this);
        image.setBackground(Color.red);
        image.setOpaque(true);

        //build add comment scroll
        add_comment_sp.setBounds(50, 50, 900, 500);
        add_comment_sp.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        add_comment_sp.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        add_comment_sp.setVisible(true);

        //add componet to add comment panel
        add_comment_panel.add(image);
        add_comment_panel.add(insert_image_button);
        add_comment_panel.add(add_comment_title);
        add_comment_panel.add(add_comment_sp);
        add_comment_panel.add(undo);
        add_comment_panel.add(redo);
        add_comment_panel.add(add_comment_submit);

        //build delete issue button
        delete_issue.setIcon(delete_issue_image);
        delete_issue.setBounds(1230, 0, 50, 50);
        delete_issue.setVisible(false);
        delete_issue.setFocusable(true);
        delete_issue.addActionListener(this);
        //build delete issue button
        edit_issue.setIcon(edit);
        edit_issue.setBounds(1280, 0, 50, 50);
        edit_issue.setFocusable(true);
        edit_issue.addActionListener(this);
        //build setting button / quit
        open_changelog.setVisible(true);
        open_changelog.setBounds(1000, 35, 150, 35);
        open_changelog.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        open_changelog.setText("Open Changelog");
        open_changelog.addActionListener(this);
        open_changelog.setBackground(Color.CYAN);
        open_changelog.setOpaque(true);
        //build issue show text
        mid_panel.setVisible(true);
        mid_panel.setPreferredSize(new Dimension(1000, 100000));
        mid_panel.setBackground(Color.CYAN);
        mid_panel.setOpaque(true);
        mid_panel.setLayout(new FlowLayout());
        //builf issue scroll
        issue_scroll.setBounds(0, 200, 1100, 450);
        issue_scroll.setVisible(true);
        issue_scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        issue_scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));

        //build issue  descrip
        issue_descrip.setPreferredSize(new Dimension(900, 200000));
        issue_descrip.setVisible(true);
        issue_descrip.setEditable(false);

        //build issue scroll
        issue_descrip_sp.setBounds(50, 50, 900, 150);
        issue_descrip_sp.setVisible(true);
        issue_descrip_sp.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        issue_descrip_sp.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));

        //build issue descrp panel
        issue_des_panel.setVisible(true);
        issue_des_panel.setBounds(0, 0, 1000, 200);
        issue_des_panel.setLayout(null);
        issue_des_panel.setBackground(Color.red);
        issue_des_panel.setOpaque(true);
        // add scroll to panel
        issue_des_panel.add(issue_descrip_sp);
        //add component to issue panel
        frame.add(issue_des_panel);
        //build edit panel
        edit_panel.setBounds(0, 80, 1000, 600);
        edit_panel.setBackground(Color.yellow);
        edit_panel.setOpaque(true);
        edit_panel.setLayout(null);
        edit_panel.setVisible(false);
        //build edit tags
        edit_tags.setBounds(0, 0, 300, 50);
        edit_tags.setVisible(true);
        edit_tags.setEditable(true);
        edit_tags.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        //build prior button
        edit_priop.removeAllItems();
        for (int i = 1; i < 11; i++) {
            edit_priop.addItem(i + "");
        }
        edit_priop.setBounds(900, 0, 100, 50);
        edit_priop.setVisible(true);
        edit_priop.addActionListener(this);
        edit_priop.setFocusable(true);
        edit_priop.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        //build prior title
        edit_priop_title.setText("Priority");
        edit_priop_title.setVisible(true);
        edit_priop_title.setBounds(800, 0, 100, 50);
        edit_priop_title.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        edit_priop_title.setBackground(Color.yellow);
        edit_priop_title.setOpaque(true);
        //build status
        edit_status.setBounds(900, 100, 100, 50);
        edit_status.setVisible(true);
        edit_status.addActionListener(this);
        edit_status.setFocusable(true);
        edit_status.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        //build description text
        edit_descrip.setVisible(true);
        edit_descrip.setEditable(true);
        edit_descrip.setPreferredSize(new Dimension(900, 10000));
        //build descriptionn text srcoll
        edit_descrip_scroll.setBounds(0, 100, 900, 500);
        edit_descrip_scroll.setVisible(true);
        edit_descrip_scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        edit_descrip_scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        //build submit button
        done_edit.setVisible(true);
        done_edit.setText("Submit");
        done_edit.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        done_edit.setFocusable(true);
        done_edit.addActionListener(this);
        done_edit.setBounds(900, 550, 100, 50);

        //add component to edit panle
        edit_panel.add(edit_tags);
        edit_panel.add(edit_priop);
        edit_panel.add(edit_priop_title);
        edit_panel.add(edit_status);
        edit_panel.add(edit_descrip_scroll);
        edit_panel.add(done_edit);

//build quit changelog button
        quitchangelog.setBounds(1150, 0, 150, 50);
        quitchangelog.setFocusable(true);
        quitchangelog.addActionListener(this);
        quitchangelog.setVisible(false);

        //build changelog text
        changelog_textarea.setPreferredSize(new Dimension(900, 200000));
        changelog_textarea.setVisible(true);
        changelog_textarea.setEditable(false);

        //build chnagelog text sp
        changelog_textsp.setBounds(0, 0, 1150, 730);
        changelog_textsp.setVisible(false);
        changelog_textsp.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        changelog_textsp.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));

        first_run_setcommentcolor(comments);

        //add component to frame
        frame.add(quitchangelog);
        frame.add(changelog_textsp);
        frame.add(edit_issue);
        frame.add(edit_panel);
        frame.add(add_comment);
        frame.add(add_comment_panel);
        frame.add(delete_issue);
        frame.add(open_changelog);
        frame.add(issue_scroll);
        frame.add(label);
        frame.repaint();

    }

    public void set_status(ArrayList<String> status) {
        edit_status.removeAllItems();
        for (int i = 0; i < status.size(); i++) {
            edit_status.addItem(status.get(i));
        }
    }

    public void setchangelogtext(ArrayList<String> changelogetext) {
        changelog_textarea.setText("");
        for (int i = 0; i < changelogetext.size(); i++) {
            changelog_textarea.append(changelogetext.get(i));
        }
    }

    public void set_tags(String tags) {
        edit_tags.setText(tags);
    }

    public void set_comment(ArrayList<Comment> com) {
        for (int i = 0; i < com.size(); i++) {
            if (i < comment_panel.size()) {
                continue;
            }
            //build small panel
            comment_panel.add(new JPanel());
            comment_panel.get(i).setLayout(null);
            comment_panel.get(i).setPreferredSize(new Dimension(900, 300));
            comment_panel.get(i).setBackground(Color.YELLOW);
            comment_panel.get(i).setOpaque(true);
            comment_panel.get(i).setVisible(true);
            //build comment
            comment.add(new JTextPane());
            if (com.get(i).getImage_file() != null) {
                System.out.println("have enter");
                comment.get(i).insertIcon(new ImageIcon(com.get(i).getImage_file().toString()));
                append("\n", comment.get(i));
            }
            append(com.get(i).toString(), comment.get(i));
            comment.get(i).setVisible(true);
            comment.get(i).setPreferredSize(new Dimension(900, 20000));
            comment.get(i).setEditable(false);
            comment_sr.add(new JScrollPane(comment.get(i)));
            comment_sr.get(i).setBounds(0, 0, 900, 200);
            comment_sr.get(i).setVisible(true);
            comment_sr.get(i).getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
            comment_sr.get(i).getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
            // build like button
            like.add(new JButton());
            like.get(i).setIcon(like_image);
            like.get(i).addActionListener(this);
            like.get(i).addMouseListener(this);
            like.get(i).setFocusable(true);
            like.get(i).setBounds(800, 200, 50, 50);
            like.get(i).setVisible(true);
            //build dislike button
            dislike.add(new JButton());
            dislike.get(i).setIcon(dislike_image);
            dislike.get(i).addActionListener(this);
            dislike.get(i).addMouseListener(this);
            dislike.get(i).setFocusable(true);
            dislike.get(i).setBounds(850, 200, 50, 50);
            dislike.get(i).setVisible(true);
            //build happy button
            happy.add(new JButton());
            happy.get(i).setIcon(happy_icon);
            happy.get(i).addActionListener(this);
            happy.get(i).addMouseListener(this);
            happy.get(i).setFocusable(true);
            happy.get(i).setBounds(750, 200, 50, 50);
            happy.get(i).setVisible(true);
            //build angry button
            angry.add(new JButton());
            angry.get(i).setIcon(angry_icon);
            angry.get(i).addActionListener(this);
            angry.get(i).addMouseListener(this);
            angry.get(i).setFocusable(true);
            angry.get(i).setBounds(700, 200, 50, 50);
            angry.get(i).setVisible(true);
            //build label
            happycount.add(new JLabel());
            happycount.get(i).setBounds(750, 250, 50, 50);
            happycount.get(i).setVisible(false);
            happycount.get(i).setBackground(Color.pink);
            happycount.get(i).setOpaque(true);
            //
            angrycount.add(new JLabel());
            angrycount.get(i).setBounds(700, 250, 50, 50);
            angrycount.get(i).setVisible(false);
            angrycount.get(i).setBackground(Color.pink);
            angrycount.get(i).setOpaque(true);
            //
            likecount.add(new JLabel());
            likecount.get(i).setBounds(800, 250, 50, 50);
            likecount.get(i).setVisible(false);
            likecount.get(i).setBackground(Color.pink);
            likecount.get(i).setOpaque(true);
            //
            dislikecount.add(new JLabel());
            dislikecount.get(i).setBounds(850, 250, 50, 50);
            dislikecount.get(i).setVisible(false);
            dislikecount.get(i).setBackground(Color.pink);
            dislikecount.get(i).setOpaque(true);


            //add component to small panel
            comment_panel.get(i).add(happycount.get(i));
            comment_panel.get(i).add(angrycount.get(i));
            comment_panel.get(i).add(likecount.get(i));
            comment_panel.get(i).add(dislikecount.get(i));
            comment_panel.get(i).add(happy.get(i));
            comment_panel.get(i).add(angry.get(i));
            comment_panel.get(i).add(like.get(i));
            comment_panel.get(i).add(dislike.get(i));
            comment_panel.get(i).add(comment_sr.get(i));
            //add component to mid_panel
            mid_panel.add(comment_panel.get(i));
        }
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add_comment) {
            System.out.println("adding comment");
            add_comment_panel.setVisible(true);
            edit_issue.setVisible(false);
            issue_scroll.setVisible(false);
            issue_des_panel.setVisible(false);
            open_changelog.setVisible(false);
            add_comment.setVisible(false);
            handle_thread();
        }
        if (e.getSource() == add_comment_submit) {
            exit = true;
            text_undo.clear();
            text_redo.clear();
            add_comment_panel.setVisible(false);

            if (isowner) {
                edit_issue.setVisible(true);
            }
            issue_des_panel.setVisible(true);
            issue_scroll.setVisible(true);
            open_changelog.setVisible(true);
            add_comment.setVisible(true);
            numberOfComments++;
            comments.add(new Comment(current_people.getName(), add_comment_text.getText(), numberOfComments, image_file));
            add_comment_text.setText("Enter comment there");
            insert_image = new ImageIcon();
            insert_image_button.setEnabled(true);
            insert_image_topanel();
            set_comment(comments);
        }
        if (e.getSource() == delete_issue) {
            frame.setVisible(false);
//            deleteThisIssue();
            project_frame.setVisible(true);

        }
        if (like != null) {
            for (int i = 0; i < like.size(); i++) {
                if (e.getSource() == like.get(i)) {
                    reactComment(i, "likes",current_people);
                    cheack_reaction_color(i, comments);
                }
            }
        }
        if (dislike != null) {
            for (int i = 0; i < dislike.size(); i++) {
                if (e.getSource() == dislike.get(i)) {
                    reactComment(i, "dislikes",current_people);
                    cheack_reaction_color(i, comments);
                }
            }
        }
        if (happy != null) {
            for (int i = 0; i < happy.size(); i++) {
                if (e.getSource() == happy.get(i)) {
                    reactComment(i, "happy",current_people);
                    cheack_reaction_color(i, comments);
                }
            }
        }
        if (angry != null) {
            for (int i = 0; i < angry.size(); i++) {
                if (e.getSource() == angry.get(i)) {
                    reactComment(i, "angry",current_people);
                    cheack_reaction_color(i, comments);
                }
            }
        }
        if (e.getSource() == open_changelog) {
            System.out.println("changlog");
            setchangelogtext(getChangelog());
            quitchangelog.setVisible(true);
            changelog_textsp.setVisible(true);
            edit_issue.setVisible(false);
            add_comment.setVisible(false);
            issue_des_panel.setVisible(false);
            mid_panel.setVisible(false);
            open_changelog.setVisible(false);
        }
        if (e.getSource() == done_edit) {
            System.out.println("submit");
            edit_panel.setVisible(false);
            if (isowner) {
                edit_issue.setVisible(true);
            }

            issue_des_panel.setVisible(true);
            issue_scroll.setVisible(true);
            open_changelog.setVisible(true);
            add_comment.setVisible(true);

            int updatedPriority = edit_priop.getSelectedIndex() + 1;
            String updatedTags = edit_tags.getText();
            String updatedDescription = edit_descrip.getText();
            String updatedStatus = edit_status.getSelectedItem() + "";
            updateChangeLog(updatedPriority, updatedTags, updatedDescription, updatedStatus);
        }
        if (e.getSource() == undo) {
            if (!text_undo.isEmpty()) {
                exit = true;
                undo_pressed = true;
                exit = true;
                text_redo.push(text_undo.peek());
                add_comment_text.setText(text_undo.pop());
                System.out.println("undo");
            }
        }
        if (e.getSource() == redo) {
            if (!text_redo.isEmpty()) {
                exit = true;
                redo_pressed = true;
                exit = true;
                text_undo.push(text_redo.peek());
                add_comment_text.setText(text_redo.pop());
                System.out.println("reundo");
            }
        }
        if (e.getSource() == insert_image_button) {
            insert_image_button.setEnabled(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif", "png", "jpeg");
            JFileChooser choose = new JFileChooser();
            choose.setFileFilter(filter);
            choose.setCurrentDirectory(new File("."));  // select where the file window start
            int res = choose.showOpenDialog(null);  // select file to open
            if (res == JFileChooser.APPROVE_OPTION) {
                image_file = new File(choose.getSelectedFile().getAbsolutePath());
                insert_image = new ImageIcon(image_file.toString());
                insert_image_topanel();
            }
        }
        if (e.getSource() == edit_issue) {
            edit_priop.setSelectedIndex(this.priority - 1);
            edit_panel.setVisible(true);

            edit_issue.setVisible(false);
            issue_des_panel.setVisible(false);
            issue_scroll.setVisible(false);
            open_changelog.setVisible(false);
            add_comment.setVisible(false);
            return_option(); //set the option
            edit_descrip.setText(this.getDescriptionText()); //set descrip text
            set_tags(this.returnTagsGUI()); //set tags
        }
        if (e.getSource() == quitchangelog) {
            changelog_textsp.setVisible(false);
            quitchangelog.setVisible(false);
            if (isowner) {
                edit_issue.setVisible(true);
            }
            add_comment.setVisible(true);
            issue_des_panel.setVisible(true);
            mid_panel.setVisible(true);
            open_changelog.setVisible(true);
        }

    }

    public void append(String s, JTextPane textPane) {
        try {
            Document doc = textPane.getDocument();
            doc.insertString(doc.getLength(), s, null);
        } catch (BadLocationException exc) {
            exc.printStackTrace();
        }
    }

    public void insert_image_topanel() {
        image.setIcon(insert_image);
//        add_comment_text.insertIcon(insert_image);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == image_file) {
            image.setBounds(0, 0, 1000, 600);
        }
        if (dislike != null) {
            for (int i = 0; i < dislike.size(); i++) {
                if (e.getSource() == dislike.get(i)) {
                    dislikecount.get(i).setText(comments.get(i).dislikecount() + "");
                    dislikecount.get(i).setVisible(true);
                }
            }
        }
        if (happy != null) {
            for (int i = 0; i < happy.size(); i++) {
                if (e.getSource() == happy.get(i)) {
                    happycount.get(i).setText(comments.get(i).happycount() + "");
                    happycount.get(i).setVisible(true);
                }
            }
        }
        if (angry != null) {
            for (int i = 0; i < angry.size(); i++) {
                if (e.getSource() == angry.get(i)) {
                    angrycount.get(i).setText(comments.get(i).angrycount() + "");
                    angrycount.get(i).setVisible(true);
                }
            }
        }
        if (like != null) {
            for (int i = 0; i < like.size(); i++) {
                if (e.getSource() == like.get(i)) {
                    likecount.get(i).setText(comments.get(i).likecount() + "");
                    likecount.get(i).setVisible(true);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == image_file) {
            image.setBounds(0, 0, 1000, 600);
        }
        if (dislike != null) {
            for (int i = 0; i < dislike.size(); i++) {
                if (e.getSource() == dislike.get(i)) {
                    dislikecount.get(i).setText(comments.get(i).dislikecount() + "");
                    dislikecount.get(i).setVisible(true);
                }
            }
        }
        if (happy != null) {
            for (int i = 0; i < happy.size(); i++) {
                if (e.getSource() == happy.get(i)) {
                    happycount.get(i).setText(comments.get(i).happycount() + "");
                    happycount.get(i).setVisible(true);
                }
            }
        }
        if (angry != null) {
            for (int i = 0; i < angry.size(); i++) {
                if (e.getSource() == angry.get(i)) {
                    angrycount.get(i).setText(comments.get(i).angrycount() + "");
                    angrycount.get(i).setVisible(true);
                }
            }
        }
        if (like != null) {
            for (int i = 0; i < like.size(); i++) {
                if (e.getSource() == like.get(i)) {
                    likecount.get(i).setText(comments.get(i).likecount() + "");
                    likecount.get(i).setVisible(true);
                }
            }
        }


    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == image_file) {
            image.setBounds(750, 0, 100, 50);
        }
        if (dislike != null) {
            for (int i = 0; i < dislike.size(); i++) {
                if (e.getSource() == dislike.get(i)) {
                    dislikecount.get(i).setVisible(false);
                }
            }
        }
        if (happy != null) {
            for (int i = 0; i < happy.size(); i++) {
                if (e.getSource() == happy.get(i)) {
                    happycount.get(i).setVisible(false);
                }
            }
        }
        if (angry != null) {
            for (int i = 0; i < angrycount.size(); i++) {
                if (e.getSource() == angry.get(i)) {
                    angrycount.get(i).setVisible(false);
                }
            }
        }
        if (like != null) {
            for (int i = 0; i < likecount.size(); i++) {
                if (e.getSource() == like.get(i)) {
                    likecount.get(i).setVisible(false);
                }
            }
        }

    }

    public void handle_thread() {
        Thread thread = new Thread() {
            public void run() {
                while (exit == false) {
                    text_undo.push(add_comment_text.getText());
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

    /*
        -- Getter and setter methods --
     */
    public ArrayList<String> getChangelog() {
        return changelog;
    }

    public void setChangelog(ArrayList<String> changelog) {
        this.changelog = changelog;
    }

    public Integer getID() {
        return this.ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public int getNumbercomment() {
        return this.numberOfComments;
    }

    public void setNumbercomment(int numbercomment) {
        this.numberOfComments = numbercomment;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public People getCurrent_people() {
        return current_people;
    }

    public void setCurrent_people(People current_people) {
        this.current_people = current_people;
    }

    public Project getProject_belongsTo() {
        return project_belongsTo;
    }

    public void setProject_belongsTo(Project project_belongsTo) {
        this.project_belongsTo = project_belongsTo;
    }

    public static Comparator<Issue> getPriorityComparator() {
        return priorityComparator;
    }

    public static void setPriorityComparator(Comparator<Issue> priorityComparator) {
        Issue.priorityComparator = priorityComparator;
    }

    public static Comparator<Issue> getTimeComparator() {
        return timeComparator;
    }

    public static void setTimeComparator(Comparator<Issue> timeComparator) {
        Issue.timeComparator = timeComparator;
    }

    public static Comparator<Issue> getIDComparator() {
        return IDComparator;
    }

    public static void setIDComparator(Comparator<Issue> IDComparator) {
        Issue.IDComparator = IDComparator;
    }

    public Comparator<Issue> getTitleComparator() {
        return TitleComparator;
    }

    public void setTitleComparator(Comparator<Issue> titleComparator) {
        TitleComparator = titleComparator;
    }

    public Comparator<Issue> getStatusComparator() {
        return statusComparator;
    }

    public void setStatusComparator(Comparator<Issue> statusComparator) {
        this.statusComparator = statusComparator;
    }

    public Comparator<Issue> getTagComparator() {
        return tagComparator;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public File getImage_file() {
        return image_file;
    }

    public void setImage_file(File image_file) {
        this.image_file = image_file;
    }

    public void setTagComparator(Comparator<Issue> tagComparator) {
        this.tagComparator = tagComparator;
    }

    @Override
    public int compare(Issue o1, Issue o2) {
        return 0;
    }
}
