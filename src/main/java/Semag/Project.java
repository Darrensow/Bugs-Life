package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@JsonIgnoreProperties(value = {"current_people", "comparatorInUse"})
public class Project implements ActionListener, Comparator<Project>, Comparable<Project> {

    private ArrayList<Issue> issue = new ArrayList<>();     // store issue
    private int numissue = 0;                               // issue id

    @JsonIgnore
    private People current_people;                          // current log in people
    private Integer ID;                                     // project id
    private String name;                                    // project name
    private String owner;                                   // project owner
    private Comparator<Issue> comparatorInUse;              // ID, Title, Priority, Timestamp

    /**
     * issue that will be shown on the table
     */
    private ArrayList<Issue> current_issue;

    //gui
    JFrame frame = new JFrame();
    ImageIcon backgroud_image = new ImageIcon("register backgroud.jpg");
    JLabel label = new JLabel(backgroud_image);
    JTextField search_issue = new JTextField();
    JButton add_issue = new JButton();
    ImageIcon add_image = new ImageIcon("add.jpg");
    ImageIcon delete_project_image = new ImageIcon("trash_icon.png");
    JButton delete_project = new JButton();
    JComboBox sort_issue = new JComboBox();
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
    JTextField assignee_text = new JTextField("Enter assignee");
    JTextField tags_text = new JTextField("Enter tags");
    JTextArea descrip = new JTextArea();
    JScrollPane desc_sp = new JScrollPane(descrip);
    ImageIcon undo_icon = new ImageIcon("undo.png");
    ImageIcon redo_icon = new ImageIcon("redo.png");
    JButton undo = new JButton();
    JButton reundo = new JButton();
    String[] list_priority = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    JButton submit = new JButton("SUBMIT");
    String[] sort_option = {"Sort based ID", "Sort based priority", "Sort based timestamp"};

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
    boolean in_delete_mode = false;

    public Project() {
    }

    /**
     * @param name
     * @param ID
     * @param owner create project
     */
    public Project(String name, int ID, String owner) {
        this.ID = ID;
        this.name = name;
        this.owner = owner;
    }

    /**
     * @param current_people determine whether is owner or not
     */
    public void projectwindow(People current_people, JFrame frame) {
        this.current_people = current_people;
        this.current_issue = issue;
        window_frame = frame;
        setupwindow();
        sortIssueBased(0);
        keeprenew();
        ArrayList<String> status = new ArrayList<>(Arrays.asList(Issue.statusOption));
        include_and_exluded(Issue.tagsOption);
        include_and_exluded_state(status);
    }

    public void keeprenew() {
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    if (frame.isVisible() == false) {
                        current_issue = issue;
                        reset_table(current_issue);
                    }
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
     * check if the tags is registered, if not, add it to the tag option and
     * return This method is only called in @{addIssue}
     */
    public String[] addTag(String[] tag) {
        String[] toAdd = new String[tag.length];

        for (int i = 0; i < tag.length; i++) {
            boolean exist = false;
            for (int j = 0; j < Issue.tagsOption.size(); j++) {
                //check if the tag is available, if yes, copy and break
                if (Issue.tagsOption.get(j).equalsIgnoreCase(tag[i])) {
                    toAdd[i] = Issue.tagsOption.get(j);
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                //if no, add the tag option and copy
                Issue.tagsOption.add(tag[i]);
                toAdd[i] = tag[i];
            }
        }
        return toAdd;
    }


    /**
     * @param arr   comment arraylist
     * @param token keyword check a wword in the comment
     * @return
     */
    private boolean checkcomment(ArrayList<Comment> arr, String token) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getText().toLowerCase().contains(token)) {
                return true;
            }
        }
        return false;
    }

    //check whether is id or not
    private boolean isID(String sen) {
        if (sen.charAt(0) != '#') {
            return false;
        }
        try {
            Integer d = Integer.parseInt(sen.substring(1));
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
     *               int
     */
    public void sortIssueBased(int choose) {
        switch (choose) {
            case 0: //0 is the first option, ID
                comparatorInUse = Issue.IDComparator;
                break;
            case 1: //1 is the sec option, Priority
                comparatorInUse = Issue.priorityComparator;
                break;
            case 2: //2 is the third option, Timestamp
                comparatorInUse = Issue.timeComparator;
                break;
        }
        Collections.sort(current_issue, comparatorInUse);
        reset_table(current_issue);
        System.out.println(current_issue.size());
    }


    /**
     * d
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
        str.append(String.format(" %-20s", o.getAssignee()));
        str.append(String.format(" %-20s", o.getCreator()));
        return str.toString();
    }


    private People searchpeople(String name) {
        for (int i = 0; i < Window.people_Array.size(); i++) {
            if (name.equals(Window.people_Array.get(i).getName())) {
                return Window.people_Array.get(i);
            }
        }
        return null;
    }

    /**
     * @param ID issue index enter issue window
     */
    public void entertheissue(int ID) {
        this.frame.setVisible(false);
        getIssueOfID(ID).issuewindow(current_people, frame);
    }

    private Issue getIssueOfID(int ID) {
        for (int i = 0; i < issue.size(); i++) {
            if (issue.get(i).getID() == ID) {
                return issue.get(i);
            }
        }
        return null;
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
        ImageIcon konoha = new ImageIcon("doge_image.jpg");
        frame.setLayout(null);
        frame.setTitle("Doge");
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                window_frame.setVisible(true);
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

        // build the included title and button
        //title
        include_text.setText("Include Tags");
        include_text.setBackground(Color.black);
        include_text.setOpaque(true);
        include_text.setVisible(true);
        include_text.setForeground(Color.YELLOW);
        include_text.setFont(new Font("MV Boli", Font.PLAIN, 20));
        include_text.setBounds(0, 0, 160, 35);
        include_text.setBorder(BorderFactory.createEmptyBorder());
        //title panel
        include_title = new JPanel();
        include_title.setBackground(Color.black);
        include_title.setOpaque(true);
        include_title.setVisible(true);
        include_title.setLayout(null);
        include_title.setBounds(1000, 100, 160, 50);
        include_title.add(include_text);

        //build exclude title and button
        exclude_text.setText("Enclude Tags");
        exclude_text.setBackground(Color.black);
        exclude_text.setOpaque(true);
        exclude_text.setVisible(true);
        exclude_text.setForeground(Color.YELLOW);
        exclude_text.setFont(new Font("MV Boli", Font.PLAIN, 20));
        exclude_text.setBounds(0, 0, 160, 35);
        exclude_text.setBorder(BorderFactory.createEmptyBorder());
        //title panel
        exluded_title = new JPanel();
        exluded_title.setBackground(Color.black);
        exluded_title.setOpaque(true);
        exluded_title.setVisible(true);
        exluded_title.setLayout(null);
        exluded_title.setBounds(1160, 100, 160, 50);
        exluded_title.add(exclude_text);
        //build the right_up black panel
        //build panel
        black_panel_up.setBackground(Color.black);
        black_panel_up.setPreferredSize(new Dimension(160, 2000));
        black_panel_up.setLayout(new FlowLayout());
        black_panel_up.setVisible(true);
        //build scroll
        black_ps_up.setBounds(1000, 150, 160, 250);
        black_ps_up.getVerticalScrollBar().setPreferredSize(new Dimension(1, 0));
        black_ps_up.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 1));
        black_ps_up.setVisible(true);

        //build the right_down black panel
        //build panel
        black_panel_down.setBackground(Color.black);
        black_panel_down.setPreferredSize(new Dimension(160, 2000));
        black_panel_down.setLayout(new FlowLayout());
        black_panel_down.setVisible(true);
        //build scroll
        black_ps_down.setBounds(1160, 150, 160, 250);
        black_ps_down.getVerticalScrollBar().setPreferredSize(new Dimension(1, 0));
        black_ps_down.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 1));
        black_ps_down.setVisible(true);

        ///
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
        tableModel.setColumnCount(0);
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
                int value = Integer.parseInt(table.getValueAt(row, 0).toString());
                if (in_delete_mode == false) {
                    entertheissue(value);
                } else {
                    deleteIssue(value);
                    current_issue = issue;
                    reset_table(current_issue);
                    JOptionPane.showMessageDialog(null, "you are out from deleting issue mode", "delete issue", JOptionPane.WARNING_MESSAGE);
                }

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
                if (assignee_text.getText().equals("Enter assignee")) {
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
        for (int i = 0; i < include.size(); i++) {
            black_panel_up.remove(include.get(i));
        }
        for (int i = 0; i < exclude.size(); i++) {
            black_panel_down.remove(exclude.get(i));
        }
        include.clear();
        exclude.clear();
        for (int i = 0; i < arr.size(); i++) {

            //add tags to include button
            include.add(new JCheckBox());
            include.get(i).setText(arr.get(i));
            include.get(i).setFocusable(true);
            include.get(i).setLayout(null);
            include.get(i).setFont(new Font("Consolas", Font.PLAIN, 10));
            include.get(i).setSize(new Dimension(300, 100));
            include.get(i).setVisible(true);
            black_panel_up.add(include.get(i));

            //add tags to exclude button
            exclude.add(new JCheckBox());
            exclude.get(i).setText(arr.get(i));
            exclude.get(i).setFocusable(true);
            exclude.get(i).setFont(new Font("Consolas", Font.PLAIN, 10));
            exclude.get(i).setSize(new Dimension(300, 100));
            exclude.get(i).setVisible(true);
            black_panel_down.add(exclude.get(i));
        }
    }

    public void include_and_exluded_state(ArrayList<String> arr) {
        for (int i = 0; i < include_state.size(); i++) {
            green_panel_up.remove(include_state.get(i));
            green_panel_down.remove(exclude_state.get(i));
        }
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
            arow[6] = data.get(i).getAssignee() + "";
            arow[7] = data.get(i).getCreator() + "";
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

    /**
     * This is a method to just remove one issue from issue dashboard, called by
     * Issue class
     *
     * @param ID issue to be removed
     */
    public void deleteIssue(int ID) {

        for (int i = 0; i < issue.size(); i++) {
            if (issue.get(i).getID() == ID) {
                Window.getPeopleByUsername(issue.get(i).getAssignee()).reduceAssigned();
                issue.remove(i);
                in_delete_mode = false;
                break;
            }
        }
    }

    //    name, tags ,priority, assignee
    public ArrayList<Issue> returnCanDeleteIssue() {
        ArrayList<Issue> temp = new ArrayList<>();
        for (int i = 0; i < issue.size(); i++) {
            if (current_people.getName().equals(issue.get(i).getCreator())) {
                temp.add(issue.get(i));
            }
        }
        return temp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add_issue) {
            add_issue_panel.setVisible(true);
            table_scroll.setVisible(false);
            System.out.println("add issue button pressed");
            handle_thread();
        }
        if (e.getSource() == delete_project) {
            if (in_delete_mode == true) {
                in_delete_mode = false;
                JOptionPane.showMessageDialog(null, "you are out from deleting issue mode", "delete issue", JOptionPane.WARNING_MESSAGE);
                reset_table(current_issue);
            } else {
                in_delete_mode = true;
                JOptionPane.showMessageDialog(null, "you are in deleting issue mode", "delete issue", JOptionPane.WARNING_MESSAGE);
                reset_table(returnCanDeleteIssue());
            }

            System.out.println("delete project button pressed");
        }
        if (e.getSource() == sort_issue) {  // this can short but left first
            if (sort_issue.getSelectedIndex() == 0) {
                sortIssueBased(0);
                System.out.println("sort based on ID selected");
            }
            if (sort_issue.getSelectedIndex() == 1) {
                sortIssueBased(1);
                System.out.println("sort based on Priority selected");
            }
            if (sort_issue.getSelectedIndex() == 2) {
                sortIssueBased(2);
                System.out.println("sort based on Timestemp selected");
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
            String issue_name = name_text.getText();
            String tags = tags_text.getText();
            String[] issue_tags_array = tags.substring(1).split("#");
            String description = descrip.getText();
            int priop = priority.getSelectedIndex() + 1;
            String assignee = assignee_text.getText();
            name_text.setBounds(0, 0, 150, 50);
            tags_text.setBounds(0, 120, 150, 50);
            assignee_text.setBounds(0, 60, 150, 50);
            name_text.setText("Enter issue name");
            tags_text.setText("Enter tags");
            descrip.setText("Add description there");
            assignee_text.setText("Enter assignee");
            People assignee_obj = searchpeople(assignee);
            if (assignee_obj == null) {
                popwindow("user issue", "invalid people");
            } else if (issue_name.equals("Enter issue name") || issue_name.equals("")) {
                popwindow("Worring", "Please ENTER valid issue name");
            } else if (tags.equals("Enter tags") || tags.equals("")) {
                popwindow("Worring", "Please ENTER valid tags");
            } else {
                issue_tags_array = addTag(issue_tags_array);
                Issue iss = new Issue(numissue, issue_name, description, current_people.getName(), assignee_obj.getName(), issue_tags_array, priop, this);
                issue.add(iss);
                assignee_obj.addAssigned(this.ID, this.name, numissue, issue_name, current_people.getName());
                numissue++;
                exit = true;
                include_and_exluded(Issue.tagsOption);
                System.out.println("issue added");
                current_issue = issue;
                reset_table(current_issue);
                add_issue_panel.setVisible(false);
                table_scroll.setVisible(true);
            }
        }
        if (e.getSource() == sreach) {
            System.out.println("searchafasd");
            search_situation();
        }
    }

    public void search_situation() {
        String include_tags = "";
        String exclude_tags = "";
        String includestate = "";
        String excludestate = "";
        String keyword = search_issue.getText();
        boolean can_search = true;

        for (int i = 0; i < include.size(); i++) {
            if (include.get(i).isSelected()) {
                include_tags += "#" + include.get(i).getText();
                if (exclude.get(i).isSelected()) {
                    popwindow("WARNING!", "Including and excluding the same tags are not allowed.");
                    can_search = false;
                    break;
                }
            }
        }
        if (can_search) {
            for (int i = 0; i < exclude.size(); i++) {
                if (exclude.get(i).isSelected()) {
                    exclude_tags += "#" + exclude.get(i).getText();
                }
            }
            for (int i = 0; i < include_state.size(); i++) {
                if (include_state.get(i).isSelected()) {
                    includestate += "#" + include_state.get(i).getText();
                    if (exclude_state.get(i).isSelected()) {
                        popwindow("WARNING", "Including and excluding the same tags are not allowed.");
                        can_search = false;
                        break;
                    }
                }
            }
            if (can_search) {
                for (int i = 0; i < exclude_state.size(); i++) {
                    if (exclude_state.get(i).isSelected()) {
                        excludestate += "#" + exclude_state.get(i).getText();
                    }
                }
            }
        }
        if (can_search) {
            current_issue = filterout(exclude_tags, excludestate);//        System.out.println("issue size after filterout:" + current_issue.size());
            current_issue = filterin(include_tags, includestate);//        System.out.println("issue size after filterin:" + current_issue.size());
            search(keyword);
        }

    }

    private ArrayList<Issue> filterout(String tag, String state) {
        if (tag.equals("") && state.equals("")) {
            return issue;
        }
        String[] tags;
        String[] states;

        if (tag.length() == 0) {
            tags = new String[0];
        } else
            tags = tag.substring(1).split("#");

        if (state.length() == 0) {
            states = new String[0];
        } else
            states = state.substring(1).split("#");

//        System.out.println(Arrays.toString(tags));
//        System.out.println(Arrays.toString(states));

        boolean[] kenaExclude = new boolean[issue.size()];   //acts like an index pointer
        for (int i = 0; i < issue.size(); i++) {

            if (tags.length != 0) {     //only compare tag if there's tag
                for (int j = 0; j < issue.get(i).getTag().length; j++) {
                    String currentIssueTag = issue.get(i).getTag()[j];

                    for (int k = 0; k < tags.length; k++) {
                        if (tags[k].equalsIgnoreCase(currentIssueTag)) {
                            kenaExclude[i] = true;              //if kena mark true, means it will be excluded
                            j = issue.get(i).getTag().length;   //:)) sorry I don't know how to use label
                            break;
                        }
                    }

                }

            }

            if (states.length != 0) {     //only compare status if there's status
                String currentIssueStatus = issue.get(i).getStatus();
                for (int k = 0; k < states.length; k++) {
                    if (states[k].equalsIgnoreCase(currentIssueStatus)) {
//                            System.out.println("I kena exclude: " + issue.get(i).getTitle());
                        kenaExclude[i] = true;
                        break;
                    }
                }
            }
        }
        ArrayList<Issue> temp = new ArrayList<>();
        System.out.println(Arrays.toString(kenaExclude));
        for (int i = 0; i < kenaExclude.length; i++) {
            if (!kenaExclude[i]) {
//                System.out.println("I dinnot kena exclude: " + issue.get(i).getTitle());
                temp.add(issue.get(i));
            }
        }
        return temp;
    }


    /*
    include  filter kind
    */
    private ArrayList<Issue> filterin(String tag, String state) {
        if (current_issue.isEmpty() || (tag.equals("") && state.equals(""))) {
            return current_issue;
        }
        String[] tags;
        String[] states;

        if (tag.length() == 0) {
            tags = new String[0];
        } else
            tags = tag.substring(1).split("#");

        if (state.length() == 0) {
            states = new String[0];
        } else
            states = state.substring(1).split("#");

//        System.out.println(Arrays.toString(tags));
//        System.out.println(Arrays.toString(states));

        boolean[] willInclude = new boolean[current_issue.size()];   //acts like an index pointer
        for (int i = 0; i < current_issue.size(); i++) {
            if (tags.length != 0) {     //only compare tag if there's tag
                for (int j = 0; j < current_issue.get(i).getTag().length; j++) {
                    String currentIssueTag = current_issue.get(i).getTag()[j];

                    for (int k = 0; k < tags.length; k++) {
                        if (tags[k].equalsIgnoreCase(currentIssueTag)) {
                            willInclude[i] = true;              //if kena mark true, means it will be excluded
                            j = issue.get(i).getTag().length;   //:)) sorry I don't know how to use label
                            break;
                        }
                    }

                }

            }
            if (states.length != 0) {     //only compare status if there's status
                String currentIssueStatus = current_issue.get(i).getStatus();
                for (int k = 0; k < states.length; k++) {
                    if (states[k].equalsIgnoreCase(currentIssueStatus)) {
                        System.out.println("I kena include: " + current_issue.get(i).getTitle());
                        willInclude[i] = true;
                        break;
                    }
                }
            }
        }
        ArrayList<Issue> temp = new ArrayList<>();
        System.out.println(Arrays.toString(willInclude));
        for (int i = 0; i < willInclude.length; i++) {
            if (willInclude[i]) {
                System.out.println("I kena include: " + current_issue.get(i).getTitle());
                temp.add(current_issue.get(i));
            }
        }
        return temp;
    }


    /**
     * @param input keyword search search issue
     */
    public void search(String input) {//  only input number will directly assume as ID
        if (input.equals("") || input.equals("search")) {
            try {
                Collections.sort(current_issue, comparatorInUse);
                reset_table(current_issue);
            } catch (Exception e) {
                System.out.println("catching error in @search method");
            }
            return;         //str8 away end, no need check

        } else {
            Issue toAppend = null;
            boolean contains = false;   //if there is this Issue with this ID, then true


            //this step is to check if the user wants to find by #ID
            if (isID(input)) {
                toAppend = getIssueOfID(Integer.parseInt(input.substring(1)));
                if (toAppend != null) {
                    contains = true;
                }
            }
            current_issue = fuzzySearch(input);
            if (contains) {
                current_issue.add(0, toAppend);
            }
            reset_table(current_issue);
        }
    }

    public ArrayList<Issue> fuzzySearch(String seachkeyword) {

        int cutOffRatio = 60;

        PriorityQueue<issueWithScore> temporary = new PriorityQueue<>(RatioComparator);
        ArrayList<Issue> temp = new ArrayList<>();

        Issue current;
        int score;
        for (int i = 0; i < issue.size(); i++) {
            current = issue.get(i);


            score = Window.tokenSetPartialRatio(seachkeyword, current.getTitle());
            if (score > cutOffRatio) {
                temporary.add(new issueWithScore(current, score));
                continue;
            }

            score = Window.tokenSetPartialRatio(seachkeyword, current.getDescriptionText());
            if (score > cutOffRatio) {
                temporary.add(new issueWithScore(current, score));
                continue;
            }

            score = checkComment(seachkeyword, current.getComments(), cutOffRatio);
            if (score >= 0) {
                temporary.add(new issueWithScore(current, score));
                continue;
            }

        }

        while (!temporary.isEmpty()) {
            temp.add(temporary.poll().getElement());
        }

        return temp;
    }

    private Comparator<issueWithScore> RatioComparator = new Comparator<issueWithScore>() {
        @Override
        public int compare(issueWithScore o1, issueWithScore o2) {
            return o2.getScore() - o1.getScore();
        }
    };

    private class issueWithScore {
        private Issue element;
        private int score;

        public issueWithScore(Issue element, int score) {
            this.element = element;
            this.score = score;
        }

        public Issue getElement() {
            return element;
        }

        public int getScore() {
            return score;
        }
    }

    private int checkComment(String searchkeyword, ArrayList<Comment> comments, int cutOffRatio) {
        Comment current;
        int score;
        for (int i = 0; i < comments.size(); i++) {
            current = comments.get(i);
            score = Window.tokenSetPartialRatio(searchkeyword, current.getText());
            if (score > cutOffRatio) {
                return score;
            }
        }
        return 0;
    }


    public void popwindow(String title, String content) {
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.WARNING_MESSAGE);
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


    public String getOwner() {
        return owner;
    }

    //Setter method
    public void setIssue(ArrayList<Issue> issue) {
        this.issue = issue;
    }


    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getNumissue() {
        return numissue;
    }

    public void setNumissue(int numissue) {
        this.numissue = numissue;
    }

    public static void main(String[] args) {

    }

    @Override
    public int compare(Project o1, Project o2) {
        return 0;
    }

    @Override
    public int compareTo(Project o) {
        return 0;
    }
}
