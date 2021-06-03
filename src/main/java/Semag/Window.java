package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Window implements Serializable, ActionListener {

    /**
     * Project List
     */
    private ArrayList<Project> project_Array = new ArrayList<>();  // store project

    /**
     * List of registered User
     */
    /**
     * List of registered User
     */
    @JsonIgnore
    public static PeopleADT people_Array = new PeopleADT();

    /**
     * Replica non-static people_Array
     */
    private PeopleADT people_Array_replica = new PeopleADT();                   // store people

    private ArrayList<String> tagsOption_replica = new ArrayList<>();

    /**
     * Replica non-static Tags Option
     */
    public ArrayList<String> tagsOption = new ArrayList<String>();

    /**
     * Current comparator in use
     */
    @JsonIgnore
    private Comparator<Project> comparatorInUse;    //ID, Name, IssueCount

    private static int numberproject = 0;    // to keep track of project id
    transient Scanner sc = new Scanner(System.in);

    @JsonIgnore //ignore as don't want to override user logged in
    private People current_people; // current user logged

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    User user_obj = new User();
    //gui
    String column[] = {"ID", "Project Name", "Issue"};
    ArrayList<String> notification = new ArrayList<>();
    JTextField text1 = new JTextField();
    JComboBox option_button1 = new JComboBox();
    JComboBox delete_project = new JComboBox();
    JButton button1 = new JButton();
    JButton button2 = new JButton();
    JButton delete_project_button = new JButton("D Pro");
    JButton button3 = new JButton();
    JPanel panel1 = new JPanel();
    JTextField text2 = new JTextField();
    ImageIcon have_noti = new ImageIcon("notification with red.jpg");
    ImageIcon no_noti = new ImageIcon("notification without red.jpg");

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
    String[] setting_option = {"PDF report", "text report", "CSV report", "JSON file", "Quit"};

    public Window() {
    }

    /**
     * @param
     * @return true if action success , false if false.
     */
    public void ac() throws InterruptedException {
        user_obj.setPeople_array(this.people_Array);
        current_people = user_obj.register();
        System.out.println("succes run");
        if (!people_Array.contains(current_people)) {
            people_Array.add(current_people);
        }
        System.out.println(current_people.getName() + people_Array.get(0).getName());
        setupwindow();
        reset_table(project_Array);
//        setNotification(ArrayList of notification);
    }

    public void search(String input) {
        if (isnumberic(input)) {
            entertheprojext(Integer.parseInt(input.substring(1)));
        } else {
            printsearchResult(input);

        }
    }

    /**
     * @param seachkeyword
     * @return true if have that project
     */
    public void printsearchResult(String seachkeyword) {
        ArrayList<Project> temp = new ArrayList<>();
        String[] token = seachkeyword.split(" ");
        for (int i = 0; i < project_Array.size(); i++) {
            for (int j = 0; j < token.length; j++) {
                if (project_Array.get(i).getName().contains(token[j])) {
                    temp.add(project_Array.get(i));
                    break;
                } else if (project_Array.get(i).getName().contains(token[j] + " ")) {
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
        if (temp.size() > 0) {
            Collections.sort(temp, comparatorInUse);
            for (int i = 0; i < temp.size(); i++) {
                print(temp);
            }
        }
    }

    /**
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
     * @param name project name add project
     */
    public void addproject(String name) {
        project_Array.add(new Project(name, numberproject, current_people));
        numberproject++;
    }

    /**
     * This method will sort the project with the column that the user wish, and
     * str8 print it out
     *
     * @param option is the attribute of the project, eg ID, Project Name,
     *               returned as int
     */
    public void sortBased(int option) {
        ArrayList<Project> sortedProjectList = new ArrayList<>(project_Array);
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
        Collections.sort(sortedProjectList, comparatorInUse);
        reset_table(sortedProjectList);
    }

    /**
     * @param projectName want to remove
     */
    public void deleteProject(String projectName) {
        for (int i = 0; i < project_Array.size(); i++) {
            if(project_Array.get(i).getName().equalsIgnoreCase(projectName)){
                project_Array.remove(project_Array.get(i));
            }
        }
        numberproject--;
        reset_table(project_Array);
    }

    /**
     * @param index project index enter project window
     */
    public void entertheprojext(int index) {
        frame.setVisible(false);
        project_Array.get(index).projectwindow(current_people, frame, people_Array);
    }

    /**
     * print Project list, this method is an overloading method
     */
    public void print() {
        this.print(project_Array);
    }

    /**
     * print selected list
     */
    public void print(ArrayList<Project> toPrint) {
        System.out.println(String.format("%-3s %-30s %-15s ", "ID", "Name", "Issue Count"));
        String[][] atable = new String[toPrint.size()][3];
        for (int i = 0; i < toPrint.size(); i++) {
            atable[i][0] = toPrint.get(i).getID() + "";
            atable[i][1] = toPrint.get(i).getName();
            atable[i][2] = toPrint.get(i).getIssue().size() + "";
        }
        reset_table(atable);
    }

    /**
     * This method return string representation of one Project in the Project
     * Dashboard, this method is called by
     * {@code print(ArrayList<Project> toPrint)}
     */
    public String[] printOneProject(Project o) {
        String[] arow = new String[3];
        arow[0] = o.getID() + "";
        arow[1] = o.getName();
        arow[2] = o.getIssue().size() + "";
//        StringBuilder str = new StringBuilder();
//        str.append(String.format(" %3d", o.getID()));
//        str.append(String.format(" %-30s", o.getName()));
//        str.append(String.format(" %-15d", o.getIssue().size()));
        return arow;
    }

    // 1 = txt , 2 = csv
    public void selectfile(int num) throws IOException {  //select file location and set file name
//        System.out.println("the number is "+num);
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
            createtextfile(file, num);
        }
    }

    public void createtextfile(File file_name, int num) throws IOException {
        int resolved = 0;
        int unresolved = 0;
        int in_progress = 0;
        ArrayList<labelCounter> label = new ArrayList<>();
        int num_label = 0;
        labelCounter top_label;
        for (int i = 0; i < project_Array.size(); i++) {
            for (int j = 0; j < project_Array.get(i).issue_Arraysize(); j++) {
                Issue temp = project_Array.get(i).issuegetindex(j);
                switch (temp.getStatus()) {
                    case "resolved":
                        resolved++;
                        break;
                    case "unresolved":
                        unresolved++;
                        break;
                    case "in progress":
                        in_progress++;
                        break;
                }
                int have = -1;
                for (int k = 0; k < label.size(); k++) {
                    String[] tagsArray = temp.getTag();
                    for (int l = 0; l < tagsArray.length; l++) {
                        if (label.get(k).getName().equals(tagsArray[l])) {
                            label.get(k).add();
                            have = 1;
                            break;
                        }
                    }
                }
                if (have == -1) {
                    String[] tagsArray = temp.getTag();
                    for (int k = 0; k < tagsArray.length; k++) {
                        label.add(new labelCounter(tagsArray[k]));
                    }
                }
            }
        }
        PriorityQueue<labelCounter> pq = new PriorityQueue<>();
        for (int i = 0; i < label.size(); i++) {
            pq.add(label.get(i));
        }
        top_label = pq.peek();
        int max = -1;
        String top_perform = "";
        for (int i = 0; i < people_Array.size(); i++) {
            if (people_Array.get(i).getNumber_solved() > max) {
                max = people_Array.get(i).getNumber_solved();
                top_perform = people_Array.get(i).getName();
            }
        }
        if (num == 1) {
            try {
                PrintWriter out = new PrintWriter(new FileOutputStream(file_name));
                out.println("Number of resolved issue : " + resolved);
                out.println("Number of unresolved issue : " + unresolved);
                out.println("Number of in progress issue : " + in_progress);
                out.println("Most frequent label : " + top_label.getName() + " (total: " + top_label.getTotal() + " )");
                out.println("Top performer in team: " + top_perform + "(total: " + max + " )");
                out.close();
            } catch (IOException e) {
                System.out.println("Problem with file output");
            }
        } else if (num == 2) {
            try {
                PrintWriter out = new PrintWriter(new FileOutputStream(file_name));
                out.println("Number of resolved issue, Number of unresolved issue, Number of in progress issue, Most frequent label, Top performer in team");
                out.println(resolved + ", " + unresolved + ", " + in_progress + ", " + top_label.getName() + " (" + top_label.getTotal() + "), " + top_perform + " (" + max + ") ");
                out.close();
            } catch (IOException e) {
                System.out.println("Problem with file output");
            }
        } else if (num == 3) {
            ArrayList<String> text = new ArrayList<>();
            text.add("Number of resolved issue : " + resolved);
            text.add("Number of unresolved issue : " + unresolved);
            text.add("Number of in progress issue : " + in_progress);
            text.add("Most frequent label : " + top_label.getName() + " (total: " + top_label.getTotal() + " )");
            text.add("Top performer in team: " + top_perform + "(total: " + max + " )");
            createpdf(file_name, text);
        }
        gmail_sender gmail_obj = new gmail_sender(current_people.getGmail(), "Report by Doge", "Hi" + current_people.getName() + " this is the report file that required by you at " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(new java.util.Date(Instant.now().getEpochSecond() * 1000)));
        File[] file_send = {file_name};
        gmail_obj.sendattachment(file_send);
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

    }

    public void createpdf(File filename, ArrayList<String> text) {
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
        preface.add(new Paragraph("Report of Doge Buys life", catFont));

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
    //setup window

    public void setupwindow() {
        // make it not visible first
        panel_notification.setVisible(false);
        ImageIcon konoha = new ImageIcon("doge_image.jpg");
        frame.setLayout(null);
        frame.setTitle("Doge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(konoha.getImage());
        frame.setResizable(true);
        frame.setSize(1350, 730);
        frame.setLayout(null);
        frame.setVisible(true);
        ImageIcon image = new ImageIcon("register backgroud.jpg");
        JLabel label = new JLabel(image);
        label.setBounds(0, 0, 1350, 690);
        label.setVisible(true);
        ImageIcon image2 = new ImageIcon("add.jpg");
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
                frame.setVisible(false);
                entertheprojext(value);
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
                System.out.println("enter key pressed for search" + text1.getText());
                search(text1.getText());
            }
        };
        text1.addActionListener(action1);
        text1.setVisible(true);
        text1.setEditable(true);

        //add delete
        delete_project.setFont(new java.awt.Font("TimesRoman", java.awt.Font.PLAIN, 12));
        delete_project.setBounds(1000, 0, 150, 35);
        delete_project.setVisible(false);
        delete_project.addActionListener(this);

        delete_project_button.setBounds(1180, 50, 150, 50);
        delete_project_button.setVisible(true);
        delete_project_button.setFocusable(true);
        delete_project_button.addActionListener(this);

        option_button1.setFont(new java.awt.Font("TimesRoman", java.awt.Font.PLAIN, 12));
        option_button1.addItem("Sort based on name");
        option_button1.addItem("sort based on ID");
        option_button1.setBounds(850, 0, 150, 35);
        option_button1.setVisible(true);
        option_button1.addActionListener(this);

        panel1.setBackground(Color.BLUE);
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
                System.out.println("enter key pressed" + text2.getText());
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
        call_image = new ImageIcon("whatapps icon.jpg");
        call.setIcon(call_image);
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
        setting_option_button.setBackground(Color.CYAN);
        setting_option_button.setOpaque(true);

        //build notification button
        button3.setBounds(1230, 0, 50, 50);
        button3.setVisible(true);
        button3.setFocusable(true);
        button3.addActionListener(this);
        check_icon(notification.size());

        panel1.add(text2);
        panel1.add(button2);
        frame.add(delete_project);
        frame.add(delete_project_button);
        frame.add(setting_option_button);
        frame.add(call);
        frame.add(sp_notification);
        frame.add(panel1);
        frame.add(option_button1);
        frame.add(text1);
        frame.add(table_scroll);
        frame.add(button3);
        frame.add(button1);
        frame.add(label);
        frame.repaint();

    }

    public void add_delete_project(ArrayList<String> arr) {
        delete_project.removeAllItems();
        for (int i = 0; i < arr.size(); i++) {
            delete_project.addItem(arr.get(i));
        }
    }

    public ArrayList<String> showProjectThatCanDelete() {
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < project_Array.size(); i++) {
            if (project_Array.get(i).getOwner().equals(current_people)) {
                temp.add(project_Array.get(i).getName());
            }
        }
        return temp;
    }

    public void check_icon(int array_size) {
        if (array_size > 0) {
            button3.setIcon(have_noti);
        } else {
            button3.setIcon(no_noti);
        }
    }

    public void checknotification(ArrayList<String> arr) {

        // the notication panel
        panel_notification.setPreferredSize(new Dimension(300, 2000));
        panel_notification.setBackground(Color.green);
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
            accept_a.get(i).setText("Accept");
            accept_a.get(i).setText("Accept");
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
            notification_textarea.get(i).setText(arr.get(i));
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

    public void setNotification(ArrayList<String> notification) {
        this.notification = notification;
        check_icon(this.notification.size());
    }

    public void reset_table(String[][] data) {
        tableModel.setRowCount(0);
        for (int i = 0; i < data.length; i++) {
            tableModel.addRow(data[i]);
        }
        frame.repaint();
    }

    public static void reset_table(ArrayList<Project> data) {
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == option_button1) {
            if (option_button1.getSelectedIndex() == 0) {
                sortBased(1);
                System.out.println("sort based on name selected");
            }
            if (option_button1.getSelectedIndex() == 1) {
                sortBased(2);
                System.out.println("sort based on id sselected");
            }
            if (option_button1.getSelectedIndex() == 3) {
                sortBased(3);
                System.out.println("sort based on Issue Count selected");
            }
        }
        if (e.getSource() == button1) {
            System.out.println("add button pressed");
            panel1.setVisible(true);
        }
        if (e.getSource() == button2) {
            // add project to  array list
            String project_name = text2.getText();
            text2.setText("Enter Project Name");
//            System.out.println(text2.getText());
            addproject(project_name);
            panel1.setVisible(false);
            reset_table(project_Array);
        }
        if (e.getSource() == button3) {
            if (panel_notification.isShowing() == true) {
                table_scroll.setVisible(true);
                sp_notification.setVisible(false);
            } else {
                checknotification(notification);
                table_scroll.setVisible(false);
                System.out.println("button3 pressed");
            }

        }
        if (!accept_a.isEmpty()) {
            for (int i = 0; i < accept_a.size(); i++) {
                if (e.getSource() == accept_a.get(i)) {
                    each_notification_panel_a.get(i).setVisible(false);
                    each_notification_panel_a.remove(i);
                    System.out.println(notification.get(i));
                    notification.remove(i);
                    accept_a.remove(i);
                    ignore_a.remove(i);
                    frame.repaint();
                    //need to add index
                    System.out.println("accept " + i);
                }
            }
        }
        if (!ignore_a.isEmpty()) {
            for (int i = 0; i < ignore_a.size(); i++) {
                if (e.getSource() == ignore_a.get(i)) {
                    each_notification_panel_a.get(i).setVisible(false);
                    each_notification_panel_a.remove(i);
                    System.out.println(notification.get(i));
                    notification.remove(i);
                    accept_a.remove(i);
                    ignore_a.remove(i);
                    frame.repaint();
                    //need to add index
                    System.out.println("ignore " + i);
                }
            }
        }
        if (e.getSource() == call) {
            try {
                new client(current_people);
            } catch (IOException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("calling");
        }
        if (e.getSource() == delete_project_button) {
            add_delete_project(showProjectThatCanDelete());
            delete_project.setVisible(true);
        }
        if (e.getSource() == delete_project) {
            String name = (String) delete_project.getSelectedItem();
            deleteProject(name);
        }
        if (e.getSource() == setting_option_button) {
            switch (setting_option_button.getSelectedIndex()) {
                case 0: {
                    try {
                        selectfile(3);
                    } catch (IOException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("PDF report printing");
                break;

                case 1: {
                    try {
                        selectfile(1);
                    } catch (IOException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("text report printing");
                break;
                case 2: {
                    try {
                        selectfile(2);
                    } catch (IOException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("csv report printing");
                break;
                case 3: {
                    try {
                        sendfile(new File("Window.json"));
                    } catch (IOException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("json file printing");
                break;

                case 4:
                    System.out.println("user quiting ");
                    break;
            }
        }
    }

    public void sendfile(File file) throws IOException {
        gmail_sender gmail_obj = new gmail_sender(current_people.getGmail(), "Report by Doge", "Hi" + current_people.getName() + " this is the json file that required by you at " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(new java.util.Date(Instant.now().getEpochSecond() * 1000)));
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

    // Save and read data -- Jackson -- JSON --
    @JsonIgnore
    private static DataManagement dm = new DataManagement();

    /**
     * Method to save data, calls the writeData method in DataManagement Class
     */
    public void saveData() {
        StaticToNonStatic();
        dm.writeData(this);
    }

    public void loadData() {
        Window temp = dm.readWindowData();
        this.project_Array = temp.project_Array;
        this.people_Array = temp.people_Array;
        this.numberproject = temp.numberproject;
        this.people_Array_replica = people_Array_replica;
        this.tagsOption_replica = tagsOption_replica;
        NonStaticToStatic();
    }

    //Convert static to non-static to save into database
    private void StaticToNonStatic() {
        people_Array_replica = Window.people_Array;
        tagsOption_replica = Issue.tagsOption;
    }

    //Convert from non-static from database to load into static
    private void NonStaticToStatic() {

        //read一次
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

    public static int getNumberproject() {
        return numberproject;
    }

    public static void setNumberproject(int numberproject) {
        Window.numberproject = numberproject;
    }

    public People getCurrent_people() {
        return current_people;
    }

    public void setCurrent_people(People current_people) {
        this.current_people = current_people;
    }

    public static void main(String[] args) {
        Window obj = new Window();
        obj.setupwindow();
    }
}
