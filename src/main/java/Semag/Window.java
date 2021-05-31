package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

@JsonIgnoreProperties(value = {"sc", "sc", "current_people", "catFont", "redFont", "subFont", "smallBold"})
public class Window implements Serializable {

    /**
     * Project List
     */
    private ArrayList<Project> project_Array = new ArrayList<>();  // store project

    /**
     * List of registered User
     */
    PeopleADT people_Array = new PeopleADT();                           // store people

    /**
     * Current comparator in use
     */
    @JsonIgnore
    private Comparator<Project> comparatorInUse;    //ID, Name, IssueCount

    private int numberproject = 1;    // to keep track of project id
    transient Scanner sc = new Scanner(System.in);

    private People current_people; // current user logged

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    public Window() {
    }

    /**
     * @param ac
     * @return true if action success , false if false.
     */
    public boolean ac(String ac) {
        sc = new Scanner(System.in);
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
     * open window project dashboard
     */
    public void userwindow() throws IOException { // display project original window
        current_people.displayNewAssigned();

        sortBased(1);
        boolean quit = false;
        while (quit == false) {
            System.out.println("action? \n1)sort \n2)add project 3)search project 4)generate report 5)Save 6)quit");
            int input1 = sc.nextInt();
            switch (input1) {
                case 1:
                    System.out.println("sort based on \n1)ID \n2)name");
                    int in2 = sc.nextInt();
                    sortBased(in2);
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
                    System.out.println("Enter text want to generate 1)txt 2)csv 3)pdf");
                    int num = sc.nextInt();
                    selectfile(num);
                    break;
                case 5:
                    saveData();
                    return;
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
     * @param input keyword or ID of the project
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
     * @param seachkeyword
     * @return true if have that project
     */
    public boolean printsearchResult(String seachkeyword) {
        ArrayList<Project> temp = new ArrayList<>();
//        PriorityQueue<Project> pq = new PriorityQueue<>();
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

//        for (int i = 0; i < temp.size(); i++) {
//            pq.add(temp.get(i));
//        }
//        for (int i = 0; i < temp.size(); i++) {
//            System.out.println(pq.poll());
//        }

        if (temp.size() > 0) {
            Collections.sort(temp,comparatorInUse);
            for (int i = 0; i < temp.size(); i++) {
                print(temp);
            }
            return true;
        } else {
            return false;
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
        project_Array.add(new Project(name, numberproject, current_people, this));
        numberproject++;
    }

    /**
     * This method will set column to sort, sort the project , and print it out
     * @param option is the attribute of the project, eg ID, Project Name,
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
        Collections.sort(sortedProjectList,comparatorInUse);
        print(sortedProjectList);
    }

    /**
     * @param index project index enter project window
     */
    public void entertheprojext(int index) {
        project_Array.get(index).projectwindow(current_people);
    }

    /**
     * print Project list
     */
    public void print() {
        this.print(project_Array);
    }

    /**
     * print selected list, this is an overloading method
     */
    public void print(ArrayList<Project> toPrint) {
        System.out.println(String.format("%-3s %-30s %-15s ", "ID", "Name", "Issue Count"));
        for (int i = 0; i < toPrint.size(); i++) {
            System.out.println(printOneProject(toPrint.get(i)));
        }
    }

    /**
     * This method return string representation of one Project in the Project
     * Dashboard, this method is called by {@code print(ArrayList<Project> toPrint)}
     */
    public String printOneProject(Project o) {
        StringBuilder str = new StringBuilder();
        str.append(String.format(" %3d", o.getID()));
        str.append(String.format(" %-30s", o.getName()));
        str.append(String.format(" %-15d", o.getIssue().size()));
        return str.toString();
    }

    //Called only in Window class
    public void removeProject(Project o) {
        project_Array.remove(o);
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
        //open the file
        if (!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not  
        {
            System.out.println("not supported");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if (file_name.exists()) //checks file exists or not  
        {
            desktop.open(file_name);              //opens the specified file  
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
        Window temp = dm.readWindowData();
        this.project_Array = temp.project_Array;
        this.people_Array = temp.people_Array;
        this.numberproject = temp.numberproject;
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

}
