package Semag;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import me.xdrop.fuzzywuzzy.algorithms.TokenSet;
import me.xdrop.fuzzywuzzy.ratios.PartialRatio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.time.Instant;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@JsonIgnoreProperties(value = {"people_Array", "comparatorInUse", "tagsOption", "current_people", "catFont", "redFont", "subFont", "smallBold", "user_obj"})
public class Window implements ActionListener {


    private ArrayList<Project> project_Array = new ArrayList<>();       // store project
    //static variables for usage in classes
    public static PeopleADT people_Array = new PeopleADT();
    private int numberproject;                                          // to keep track of project id
    private PeopleADT people_Array_replica = new PeopleADT();           // store people
    private ArrayList<String> tagsOption_replica = new ArrayList<>();   // store tags
    private int numberOfUsers;

    private People current_people;                                      // current user logged

    /**
     * project_Array that will be shown on the table
     */
    private ArrayList<Project> current_project_Array;

    private Report report;

    private Comparator<Project> comparatorInUse;                        // ID, Name, IssueCount
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    User user_obj = new User();

    //gui
    String column[] = {"ID", "Project Name", "Issue"};
    ArrayList<AssignedIssue> notification = new ArrayList<>();
    JTextField text1 = new JTextField();
    JComboBox option_button1 = new JComboBox();
    JButton button1 = new JButton();
    JButton button2 = new JButton();
    ImageIcon delete_project_image = new ImageIcon("trash_icon.png");
    JButton delete_project_button = new JButton();
    JButton button3 = new JButton();
    JPanel panel1 = new JPanel();
    JTextField text2 = new JTextField();
    ImageIcon have_noti = new ImageIcon("notification with red.png");
    ImageIcon no_noti = new ImageIcon("notification without red.png");

    ImageIcon call_image;
    static DefaultTableModel tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JTable table = new JTable(tableModel);
    JScrollPane table_scroll = new JScrollPane(table);
    static JFrame frame = new JFrame();
    JPanel panel_notification = new JPanel();
    JScrollPane sp_notification = new JScrollPane(panel_notification);
    ArrayList<JTextArea> notification_textarea = new ArrayList<>();
    ArrayList<JButton> accept_a = new ArrayList<>();
    ArrayList<JButton> ignore_a = new ArrayList<>();
    ArrayList<JPanel> each_notification_panel_a = new ArrayList<>();
    JButton call;
    JComboBox setting_option_button = new JComboBox();
    String[] setting_option = {"PDF report", "Text report", "CSV report", "JSON file"};
    boolean in_delete_mode = false;




    public Window() {
    }

    /**
     * @param
     * @return true if action success , false if false.
     */
    public void ac() throws InterruptedException {
        user_obj.setPeople_array(people_Array);    // set People array to check
        user_obj.setID(this.numberOfUsers);             // set number of Users to check if new register
        current_people = user_obj.register();
        System.out.println("success run");
        if (numberOfUsers < people_Array.size()) {
            numberOfUsers++;
        }
        setupwindow();
        keeprenew();
        reset_Table(project_Array);
        checkpeople();//cheack icon
        sp_notification.setVisible(false);
    }



    public void setupwindow() {
        // make it not visible first
        panel_notification.setVisible(true);
        ImageIcon konoha = new ImageIcon("doge.png");
        frame.setLayout(null);
        frame.setTitle("Project Dashboard");
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                frame.setVisible(false);
                System.exit(0);
            }
        });
        frame.setIconImage(konoha.getImage());
        frame.setResizable(false);
        frame.setSize(1350, 730);
        frame.setLayout(null);
        frame.setVisible(true);
        ImageIcon image = new ImageIcon("background.png");
        JLabel label = new JLabel(image);
        label.setBounds(0, 0, 1350, 690);
        label.setVisible(true);
        ImageIcon image2 = new ImageIcon("add.png");
        button1.setIcon(image2);
        button1.setBounds(1180, 0, 50, 50);
        button1.setVisible(true);
        button1.setFocusable(true);
        button1.addActionListener(this);

        //build table
        for (int i = 0; i < column.length; i++) {
            tableModel.addColumn(column[i]);
        }
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {

                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                int value = Integer.parseInt(table.getValueAt(row, 0).toString());
                if (in_delete_mode != true) {
                    try {
                        enterTheProjext(value);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {
                    deleteProject(value);
                    current_project_Array = project_Array;
                    reset_Table(current_project_Array);
                    JOptionPane.showMessageDialog(null, "you are out from deleting project mode", "delete project", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
//set collum width
        table.getColumnModel().getColumn(0).setPreferredWidth(1);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(1);

        table.setFocusable(true);
        table.setRowSelectionAllowed(true);

        //set table scroll
        table_scroll.setBounds(100, 100, 1100, 550);
        table_scroll.setVisible(true);

        text1.setBounds(0, 0, 200, 50);
        text1.setText("search");
        text1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (text1.getText().equals("search")) {
                    text1.setBounds(0, 0, 300, 50);
                    text1.setText("");
                }
            }
        });
        Action action1 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String temp = text1.getText();
                text1.setBounds(0, 0, 200, 50);
                text1.setText("search");
                search(temp);
            }
        };
        text1.addActionListener(action1);
        text1.setVisible(true);
        text1.setEditable(true);

        delete_project_button.setBounds(1130, 0, 50, 50);
        delete_project_button.setIcon(delete_project_image);
        delete_project_button.setVisible(true);
        delete_project_button.setFocusable(true);
        delete_project_button.addActionListener(this);

        option_button1.setFont(new java.awt.Font("TimesRoman", java.awt.Font.PLAIN, 12));
        option_button1.addItem("Sort based on ID");
        option_button1.addItem("sort based on Name");
        option_button1.addItem("sort based on Issue count");
        option_button1.setBounds(850, 0, 150, 35);
        option_button1.setBackground(Color.getHSBColor(47, 47, 100));
        option_button1.setVisible(true);
        option_button1.addActionListener(this);

        panel1.setBackground(Color.getHSBColor(36, 81, 100));
        panel1.setOpaque(true);
        panel1.setLayout(null);
        panel1.setBounds(300, 100, 700, 500);
        panel1.setVisible(false);
        text2.setText("Enter Project Name");
        text2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (text2.getText().equals("Enter Project Name")) {
                    text2.setText("");
                }
            }
        });
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };

        text2.addActionListener(action);
        text2.setVisible(true);
        text2.setEditable(true);
        text2.setBounds(100, 100, 300, 50);

        button2.setBounds(400, 400, 100, 50);
        button2.setText("Add");
        button2.setVisible(true);
        button2.setFocusable(true);
        button2.addActionListener(this);

        //build call button
        call = new JButton();
        call_image = new ImageIcon("whatapps icon.png");
        call.setIcon(call_image);
        call.setBounds(1280, 0, 50, 50);
        call.setBounds(1280, 0, 50, 50);
        call.setVisible(true);
        call.addActionListener(this);
        call.setFocusable(true);

        //build the setting option button
        for (int i = 0; i < setting_option.length; i++) {
            setting_option_button.addItem(setting_option[i]);
        }
        setting_option_button.setVisible(true);
        setting_option_button.setBounds(700, 0, 150, 35);
        setting_option_button.setFont(new java.awt.Font("TimesRoman", java.awt.Font.PLAIN, 12));
        setting_option_button.addActionListener(this);
        setting_option_button.setBackground(Color.getHSBColor(47, 47, 100));
        setting_option_button.setOpaque(true);

        //build notification button
        button3.setBounds(1230, 0, 50, 50);
        button3.setVisible(true);
        button3.setFocusable(true);
        button3.addActionListener(this);
        check_icon(current_people.getNewAssignedNotification().size());

        panel1.add(text2);
        panel1.add(button2);
        frame.add(sp_notification);
        frame.add(delete_project_button);
        frame.add(setting_option_button);
        frame.add(call);
        frame.add(panel1);
        frame.add(option_button1);
        frame.add(text1);
        frame.add(table_scroll);
        frame.add(button3);
        frame.add(button1);
        frame.add(label);
        frame.repaint();

    }

    public void reset_Table(ArrayList<Project> data) {
        tableModel.setRowCount(0);
        for (int i = 0; i < data.size(); i++) {
            String[] arow = new String[3];
            arow[0] = data.get(i).getID() + "";
            arow[1] = data.get(i).getName() + "";
            arow[2] = data.get(i).getIssue().size() + "";
            tableModel.addRow(arow);
        }
        frame.repaint();
    }

    public void keeprenew() {
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    if (frame.isVisible() == false) {
                        current_project_Array = project_Array;
                        reset_Table(current_project_Array);
                        check_icon(current_people.getNewAssignedNotification().size());
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == option_button1) {
            if (option_button1.getSelectedIndex() == 0) {
                sortBased(0);

            }
            if (option_button1.getSelectedIndex() == 1) {
                sortBased(1);
            }
            if (option_button1.getSelectedIndex() == 2) {
                sortBased(2);
            }
        }
        if (e.getSource() == button1) {
            table_scroll.setVisible(false);
            panel1.setVisible(true);
        }
        if (e.getSource() == button2) {
            // add project to  array list
            String project_name = text2.getText();
            if (project_name.equals("Enter Project Name") || project_name.equals("")) {
                text2.setText("Enter Project Name");
                JOptionPane.showMessageDialog(null, "Please ENTER project name", "warring", JOptionPane.WARNING_MESSAGE);
            } else {
                text2.setText("Enter Project Name");
                addProject(project_name);
                panel1.setVisible(false);
                table_scroll.setVisible(true);
                current_project_Array = project_Array;
                reset_Table(current_project_Array);
            }
        }
        if (e.getSource() == button3) {
            if (panel_notification.isShowing() == true) {
                table_scroll.setVisible(true);
                sp_notification.setVisible(false);
                check_icon(current_people.getNewAssignedNotification().size());
            } else {
                checkpeople();
                table_scroll.setVisible(false);
                sp_notification.setVisible(true);
                check_icon(current_people.getNewAssignedNotification().size());
            }

        }
        if (!accept_a.isEmpty()) {
            for (int i = 0; i < accept_a.size(); i++) {
                if (e.getSource() == accept_a.get(i)) {
                    each_notification_panel_a.get(i).setVisible(false);
                    each_notification_panel_a.remove(i);
                    accept_a.remove(i);
                    ignore_a.remove(i);
                    current_people.dismissAndRemove(i);
                    frame.repaint();
                }
            }
        }
        if (!ignore_a.isEmpty()) {
            for (int i = 0; i < ignore_a.size(); i++) {
                if (e.getSource() == ignore_a.get(i)) {
                    each_notification_panel_a.get(i).setVisible(false);
                    each_notification_panel_a.remove(i);
                    accept_a.remove(i);
                    ignore_a.remove(i);
                    frame.repaint();
                }
            }
        }
        if (e.getSource() == call) {
            try {
                new Client(current_people);
            } catch (IOException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("calling");
        }
        if (e.getSource() == delete_project_button) {
            if (in_delete_mode == true) {
                in_delete_mode = false;
                JOptionPane.showMessageDialog(null, "you are out from deleting project mode", "delete project", JOptionPane.WARNING_MESSAGE);
                reset_Table(current_project_Array);
            } else {
                in_delete_mode = true;
                JOptionPane.showMessageDialog(null, "you are in deleting project mode", "delete project", JOptionPane.WARNING_MESSAGE);
                reset_Table(returnCanDelete());
            }
        }

        if (e.getSource() == setting_option_button) {
            switch (setting_option_button.getSelectedIndex()) {
                case 0: {
                    try {
                        selectFile(3);
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

                case 1: {
                    try {
                        selectFile(1);
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case 2: {
                    try {
                        selectFile(2);
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case 3: {
                    try {
                        sendFile(new File("Window.json"));
                    } catch (IOException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;

            }
        }
    }

    public void check_icon(int array_size) {
        if (array_size > 0) {
            button3.setIcon(have_noti);
        } else {
            button3.setIcon(no_noti);
        }
    }

    public void checkpeople() {
        for (int i = 0; i < people_Array.size(); i++) {
            if (people_Array.get(i).getName().equals(current_people.getName())) {
                setNotification(people_Array.get(i).newAssignedNotification);
                checknotification(people_Array.get(i).newAssignedNotification);
                break;
            }
        }
    }

    public void checknotification(ArrayList<AssignedIssue> arr) {

        // the notication panel
        panel_notification.setPreferredSize(new Dimension(300, 2000));
        panel_notification.setBackground(Color.getHSBColor(87, 69, 100));
        panel_notification.setLayout(new FlowLayout());
        panel_notification.setVisible(true);

        //build scroll
        sp_notification.setBounds(1000, 50, 300, 600);
        sp_notification.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        sp_notification.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        sp_notification.setVisible(true);

        for (int i = 0; i < arr.size(); i++) {
            //set acccept button
            accept_a.add(new JButton());
            accept_a.get(i).setText("Dismiss");
            accept_a.get(i).setFocusable(true);
            accept_a.get(i).setBounds(100, 60, 90, 30);
            accept_a.get(i).setVisible(true);
            accept_a.get(i).addActionListener(this);
            // build ignore button
            ignore_a.add(new JButton());
            ignore_a.get(i).setText("Ignore");
            ignore_a.get(i).setFocusable(true);
            ignore_a.get(i).setBounds(200, 60, 90, 30);
            ignore_a.get(i).setVisible(true);
            ignore_a.get(i).addActionListener(this);
            //build small panel
            each_notification_panel_a.add(new JPanel());
            each_notification_panel_a.get(i).setBackground(Color.red);
            each_notification_panel_a.get(i).setOpaque(true);
            each_notification_panel_a.get(i).setVisible(true);
            each_notification_panel_a.get(i).setLayout(null);
            each_notification_panel_a.get(i).setPreferredSize(new Dimension(300, 100));
            //build textfield
            notification_textarea.add(new JTextArea());
            notification_textarea.get(i).setText(arr.get(i).getIssueInfo());
            notification_textarea.get(i).setVisible(true);
            notification_textarea.get(i).setBounds(0, 0, 300, 50);
            notification_textarea.get(i).setEditable(false);
            //add commponent to panel
            each_notification_panel_a.get(i).add(accept_a.get(i));
            each_notification_panel_a.get(i).add(ignore_a.get(i));
            each_notification_panel_a.get(i).add(notification_textarea.get(i));
            panel_notification.add(each_notification_panel_a.get(i));
        }

    }

    public void setNotification(ArrayList<AssignedIssue> notification) {
//        this.notification = notification;
        check_icon(current_people.getNewAssignedNotification().size());
    }

    /**
     * @param name project name add project
     */
    public void addProject(String name) {
        project_Array.add(new Project(name, numberproject, current_people.getName()));
        numberproject++;
    }

    public ArrayList<Project> returnCanDelete() {
        ArrayList<Project> return_value = new ArrayList<>();
        for (int i = 0; i < project_Array.size(); i++) {
            if (current_people.getName().equals(project_Array.get(i).getOwner())) {
                return_value.add(project_Array.get(i));
            }
        }
        return return_value;
    }

    /**
     * @param ID want to remove
     *///may delete other project which same name
    public void deleteProject(int ID) {
        for (int i = 0; i < project_Array.size(); i++) {
            if (project_Array.get(i).getID() == ID) {
                project_Array.remove(i);
                in_delete_mode = false;
                break;
            }
        }
    }


    /**
     * @param option is the attribute of the project, eg ID, Project Name,
     */
    public void sortBased(int option) {
        switch (option) {
            case 0: //0 is the first option, ID
                comparatorInUse = Project.IDComparator;
                break;
            case 1: //1 is the sec option, Name
                comparatorInUse = Project.NameComparator;
                break;
            case 2: //2 is the third option, IssueCount
                comparatorInUse = Project.IssueCountComparator;
                break;
        }
        Collections.sort(current_project_Array, comparatorInUse);
        reset_Table(current_project_Array);
    }

    public void search(String input) {
        if (input.equals("") || input.equals("search")) {
            try {
                current_project_Array = project_Array;
                Collections.sort(current_project_Array, comparatorInUse);
                reset_Table(current_project_Array);
            } catch (Exception e) {
                System.out.println("catching error in @search method");
            }
            return;                 //str8 away end, no need check

        }else {
            Project toAppend = null;
            boolean contains = false;

            //this step is to check if the user wants to find by #ID
            if (isID(input)) {
                toAppend = obtainProjectOfID(Integer.parseInt(input.substring(1)));
                if (toAppend != null) {
                    contains = true;
                }
            }

            current_project_Array = fuzzySearch(input);
            if (contains) {
                current_project_Array.add(0, toAppend);
            }
            reset_Table(current_project_Array);
        }
    }


    public ArrayList<Project> fuzzySearch(String seachkeyword) {
        int cutOffRatio = 60;

        PriorityQueue<projectWithScore> temporary = new PriorityQueue<>(RatioComparator);
        ArrayList<Project> temp = new ArrayList<>();

        for (int i = 0; i < project_Array.size(); i++) {
            Project current = project_Array.get(i);
            int score = tokenSetPartialRatio(seachkeyword, current.getName());
            if (score > cutOffRatio) {
                temporary.add(new projectWithScore(current,score));
                continue;
            }

        }
        while (!temporary.isEmpty()) {
            temp.add(temporary.poll().getElement());
        }
        return temp;
    }

    public static int tokenSetPartialRatio(String s1, String s2) {
        return new TokenSet().apply(s1, s2, new PartialRatio());
    }

    private class projectWithScore {
        private Project element;
        private int score;

        public projectWithScore(Project element, int score) {
            this.element = element;
            this.score = score;
        }

        public Project getElement() {
            return element;
        }
        public int getScore() {
            return score;
        }
    }

    private Comparator<projectWithScore> RatioComparator = new Comparator<projectWithScore>() {
        @Override
        public int compare(projectWithScore o1, projectWithScore o2) {
            return o2.getScore() - o1.getScore();
        }
    };

    /**
     * @param sen keyword
     * @return true if it is an id
     */
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
     * @param ID project index enter project window
     */
    public void enterTheProjext(int ID) throws MalformedURLException {
        frame.setVisible(false);
        obtainProjectOfID(ID).projectWindow(current_people, frame);
    }


    // 1 = txt , 2 = csv
    public void selectFile(int num) throws IOException, InterruptedException {  //select file location and set file name
        JFileChooser choose = new JFileChooser();
        choose.setCurrentDirectory(new File("."));  // select where the file window start
        if (num == 1) {
            choose.setSelectedFile(new File("report.txt"));
        } else if (num == 2) {
            choose.setSelectedFile(new File("report.csv"));
        } else if (num == 3) {
            choose.setSelectedFile(new File("report.pdf"));
        }

        int res = choose.showSaveDialog(choose);     // select file to save
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = new File(choose.getSelectedFile().getAbsolutePath());
            createReport(file, num);
        }
    }

    public void createReport(File file_name, int num) throws IOException, InterruptedException {
        int cumuResolved = 0;   //cum resolved
        int cumuUnresolved = 0;   //cum unresolved
        int cumuInProgress = 0;   //in_progress

        ArrayList<TagCounter> cumuLabel = new ArrayList<>();

        for (int i = 0; i < Issue.tagsOption.size(); i++) {
            cumuLabel.add(new TagCounter(Issue.tagsOption.get(i)));
        }

        for (int i = 0; i < project_Array.size(); i++) {
            for (int j = 0; j < project_Array.get(i).obtainIssueArraySize(); j++) {
                Issue temp = project_Array.get(i).getIssue().get(j);
                switch (temp.getStatus()) {
                    case "Closed":
                    case "Resolved":
                        cumuResolved++;
                        break;
                    case "In Progress":
                        cumuInProgress++;
                        break;
                    default:
                        cumuUnresolved++;
                }


                String[] tagsArray = temp.getTag();
                for (int l = 0; l < tagsArray.length; l++) {
                    for (int k = 0; k < cumuLabel.size(); k++) {
                        if(tagsArray[l].equals(cumuLabel.get(k).getName())){
                            cumuLabel.get(k).add();
                            break;
                        }
                    }
                }
            }
        }


        ArrayList<PerformanceCounter> cumuADT = new ArrayList<>();
        for (int i = 0; i < people_Array.size(); i++) {
            cumuADT.add(new PerformanceCounter(people_Array.get(i).getName(), people_Array.get(i).getNumber_solved()));
        }

        Report current = new Report(cumuADT, cumuResolved, cumuUnresolved, cumuInProgress, cumuLabel);
        current.finddifferent(report);

        //cum
        String cum_topperform = current.getTopPerformCumu();
        int cum_toperform_max = current.getMaxSolvedCumu();
        String cum_toptags = current.getTopTagsCumu();
        int cum_maxtags = current.getMaxTagsCumu();

        //weekly
        String top_perform = current.getTopPerformThisWeek();
        int max = current.getMaxSolvedThisWeek();
        String str = current.getTopTagsThisWeek();
        int max_tags = current.getMaxTagsThisWeek();

        if (num == 1) {
            try {
                PrintWriter out = new PrintWriter(new FileOutputStream(file_name));
                out.println("Weekly Report of Doge \n" +
                        "\n" +
                        "To whom it may concern, \n" +
                        "\n" +
                        "Below are the reports and statistics of the team's performance in the past and their weekly output. \n" +
                        "\n" +
                        "===== Cummulative data analysis =====");
                out.println("- Number of issues resolved: " + cumuResolved);
                out.println("- Number of issues unresolved: " + cumuUnresolved);
                out.println("- Number of issues in progress: " + cumuInProgress);
                if (cum_topperform.equals("")) {
                    out.println("Top performer in team: null");
                } else {
                    out.println("Top performer in team: [ " + cum_topperform + " ] (total: " + cum_toperform_max + " )");
                }
                if (cum_maxtags <= 0) {
                    out.println("Most frequent label : null");
                } else {
                    out.println("Most frequent label : " + cum_toptags + " (total: " + cum_maxtags + " )");
                }
                out.println();
                out.println("===== Weekly performance analysis =====");
                out.println();
                out.println("- Number of issues resolved: " + current.getSolvedThisWeek());
                out.println("- Number of issues unresolved: " + current.getUnsolvedThisWeek());
                out.println("- Number of issues in progress: " + current.getInProgressThisWeek());

                if (top_perform.equals("")) {
                    out.println("Top performer in team: null");
                } else {
                    out.println("Top performer in team: [ " + top_perform + " ] (total: " + max + " )");
                }
                if (max_tags <= 0) {
                    out.println("Most frequent label : null");
                } else {
                    out.println("Most frequent label : " + str + " (total: " + max_tags + " )");
                }
                out.println("We hope that this report is able to aid you in determining future goals plan for the betterment of the team. \n" +
                        "\n" +
                        "Thank you. \n" +
                        "\n" +
                        "Yours sincerely, \n" +
                        "\n" +
                        "Admin \n" +
                        "Doge");
                out.close();
            } catch (IOException e) {
                System.out.println("Problem with file output");
            }
        } else if (num == 2) {
            try {
                PrintWriter out = new PrintWriter(new FileOutputStream(file_name));
                out.println("Number of issues resolved, Number of issues unresolved, Number of issues in progress, Top performer in the team, Most frequently used tags, Number of issues resolved(weekly), Number of issues unresolved(weekly), Number of issues in progress(weekly), Top performer in the team(weekly), Most frequently used tags(weekly)");
                String temp_string = "";
                temp_string += cumuResolved + ", " + cumuUnresolved + ", " + cumuInProgress + ", ";
                if (cum_topperform.equals("")) {
                    temp_string += "null, ";
                } else {
                    temp_string += cum_topperform + "( " + cum_toperform_max + " ), ";
                }
                if (cum_maxtags <= 0) {
                    temp_string += "null, ";
                } else {
                    temp_string += cum_toptags + "(" + cum_maxtags + "), ";
                }
                temp_string += current.getSolvedThisWeek() + ", " + current.getUnsolvedThisWeek() + ", " + current.getInProgressThisWeek() + ", ";
                if (top_perform.equals("")) {
                    temp_string += "null, ";
                } else {
                    temp_string += top_perform + " (" + max + ") , ";
                }
                if (max_tags < 0) {
                    temp_string += "null, ";
                } else {
                    temp_string += str + " ( " + max_tags + " ) , ";
                }
                out.println(temp_string);
                out.close();
            } catch (IOException e) {
                System.out.println("Problem with file output");
            }
        } else if (num == 3) {
            ArrayList<String> text = new ArrayList<>();
            text.add("To whom it may concern, ");
            text.add("Below are the reports and statistics of the team's performance in the past and their weekly output.");
            text.add(" ");
            text.add("===== Cummulative data analysis =====");
            text.add(" ");
            text.add("- Number of issues resolved: " + cumuResolved);
            text.add("- Number of issues unresolved: " + cumuUnresolved);
            text.add("- Number of issues in progress: " + cumuInProgress);
            if (cum_topperform.equals("")) {
                text.add("Top performer in team: null");
            } else {
                text.add("Top performer in team: [ " + cum_topperform + " ] (total: " + cum_toperform_max + " )");
            }
            if (cum_maxtags < 0) {
                text.add("Most frequent label : null");
            } else {
                text.add("Most frequent label : " + cum_toptags + " (total: " + cum_maxtags + " )");
            }
            text.add(" ");
            text.add("===== Weekly performance analysis =====");
            text.add(" ");
            text.add("- Number of issues resolved: " + current.getSolvedThisWeek());
            text.add("- Number of issues unresolved: " + current.getUnsolvedThisWeek());
            text.add("- Number of issues in progress: " + current.getInProgressThisWeek());
            if (top_perform.equals("")) {
                text.add("Top performer in team: null");
            } else {
                text.add("Top performer in team: [ " + top_perform + " ] (total: " + max + " )");
            }
            if (max_tags < 0) {
                text.add("Most frequent label : null");
            } else {
                text.add("Most frequent label : " + str + " (total: " + max_tags + " )");
            }
            text.add(" ");
            text.add(" ");
            text.add("We hope that this report is able to aid you in determining future goals plan for the betterment of the team. ");
            text.add(" ");
            text.add("Thank you. ");
            text.add(" ");
            text.add("Yours sincerely, ");
            text.add(" ");
            text.add("Admin ");
            text.add("Doge");
            createPDF(file_name, text);
        }

//        report = current;
        GmailSender gmail_obj = new GmailSender(current_people.getGmail(), "Report by Doge", "Hi " + current_people.getName() + " this is the report file that required by you at " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(new java.util.Date(Instant.now().getEpochSecond() * 1000)));
        File[] file_send = {file_name};

        //open the file
        if (!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not
        {
            System.out.println("not supported");
        } else {
            Desktop desktop = Desktop.getDesktop();
            if (file_name.exists()) //checks file exists or not
            {
                desktop.open(file_name);              //opens the specified file

            }
        }
        Thread.sleep(5000);
        gmail_obj.sendattachment(file_send);

    }

    public void createPDF(File filename, ArrayList<String> text) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            addTitlePage(document);
            addContent(document, text);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTitlePage(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Weekly Report of Doge", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph("Report generated by: " + System.getProperty("user.name") + ", " + new Date(), smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph("This document describes provide by team Doge.", smallBold));

        addEmptyLine(preface, 8);

        preface.add(new Paragraph("This document is for education purpose only.", redFont));

        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static void addContent(Document document, ArrayList<String> text) throws DocumentException {
        Anchor anchor = new Anchor("Content", catFont); // change text beside the text
        anchor.setName("Content");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

//        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        for (int i = 0; i < text.size(); i++) {
            catPart.add(new Paragraph(text.get(i)));
        }
        document.add(catPart);

        // Next section
        anchor = new Anchor("END", catFont);
        anchor.setName("End");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 2);
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 20);
        preface.add(new Paragraph("                                                                         End of the report", redFont));
        catPart.add(preface);
        document.add(catPart);
    }

    public void sendFile(File file) throws IOException {
        GmailSender gmail_obj = new GmailSender(current_people.getGmail(), "Report by Doge", "Hi" + current_people.getName() + " this is the json file that required by you at " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(new java.util.Date(Instant.now().getEpochSecond() * 1000)));
        File[] file_send = {file};
        gmail_obj.sendattachment(file_send);
        //open the file
        if (!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not
        {
            System.out.println("not supported");
        } else {
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) //checks file exists or not
            {
                desktop.open(file);              //opens the specified file

            }
        }
    }










    public static People obtainPeopleByUsername(String username) {
        for (int i = 0; i < Window.people_Array.size(); i++) {
            if (username.equals(Window.people_Array.get(i).getName())) {
                return Window.people_Array.get(i);
            }
        }
        return null;
    }

    private Project obtainProjectOfID(int ID) {
        for (int i = 0; i < project_Array.size(); i++) {
            if (project_Array.get(i).getID() == ID) {
                return project_Array.get(i);
            }
        }
        return null;
    }
    /**
     * Method to increase the number of issues resolved of a particular person.
     * Occurs when an issue is set to 'Resolved' status.
     *
     * @param people The person assigned to the issue
     */
    public static void resolvedAnIssue(People people) {
        for (int i = 0; i < Window.people_Array.size(); i++) {
            if (people.getName().equals(Window.people_Array.get(i).getName())) {
                Window.people_Array.get(i).addResolved();
            }
        }
    }

    /**
     * Method to decrease the number of issues resolved of a particular person.
     * Occurs when a resolved issue is set to 'Reopen' state.
     *
     * @param people The person assigned to the issue.
     */
    public static void reopenedAnIssue(People people) {
        for (int i = 0; i < Window.people_Array.size(); i++) {
            if (people.getName().equals(Window.people_Array.get(i).getName())) {
                Window.people_Array.get(i).reduceResolved();
            }
        }
    }




    // Save and read data -- Jackson -- JSON --
    /**
     * Method to save data, calls the writeData method in DataManagement Class
     */

    public void saveData() {
        StaticToNonStatic();
        DataManagement.writeData(this);
    }

    public void loadData() {
        Window temp = DataManagement.readWindowData();
        this.report = temp.report;
        this.project_Array = temp.project_Array;
        this.numberproject = temp.numberproject;
        this.people_Array_replica = temp.people_Array_replica;
        this.tagsOption_replica = temp.tagsOption_replica;
        this.numberOfUsers = temp.numberOfUsers;
        this.current_project_Array = project_Array;
        NonStaticToStatic();
    }

    //Convert static to non-static to save into database
    private void StaticToNonStatic() {
        people_Array_replica = Window.people_Array;
        tagsOption_replica = Issue.tagsOption;
    }

    //Convert from non-static from database to load into static
    private void NonStaticToStatic() {

        //read once
        for (int i = 0; i < this.people_Array_replica.size(); i++) {
            Window.people_Array.add(people_Array_replica.get(i));
        }
        for (int i = 0; i < tagsOption_replica.size(); i++) {
            Issue.tagsOption.add(tagsOption_replica.get(i));
        }
    }

    // -- Getter and setter methods --
    public ArrayList<Project> getProject_Array() {
        return this.project_Array;
    }

    public void setProject_Array(ArrayList<Project> project_Array) {
        this.project_Array = project_Array;
    }

    public PeopleADT getPeople_Array() {
        return people_Array;
    }

    public void setPeople_Array(PeopleADT people_Array) {
        this.people_Array = people_Array;
    }

    public int getNumberproject() {
        return this.numberproject;
    }

    public void setNumberproject(int numberproject) {
        this.numberproject = numberproject;
    }

    public People getCurrent_people() {
        return current_people;
    }

    public void setCurrent_people(People current_people) {
        this.current_people = current_people;
    }

    public PeopleADT getPeople_Array_replica() {
        return people_Array_replica;
    }

    public void setPeople_Array_replica(PeopleADT people_Array_replica) {
        this.people_Array_replica = people_Array_replica;
    }

    public ArrayList<String> getTagsOption_replica() {
        return tagsOption_replica;
    }

    public void setTagsOption_replica(ArrayList<String> tagsOption_replica) {
        this.tagsOption_replica = tagsOption_replica;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
