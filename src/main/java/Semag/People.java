package Semag;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Scanner;

public class People  {

    /**
     * {@code list} is the user's newly assigned Issue
     */
    public ArrayList<AssignedIssue> newAssignedNotification = new ArrayList<>();

    /**
     * {@code password} is the user's account password
     */
    private String password;

    /**
     * {@code name} is the user's name
     */
    private String name;

    /**
     * {@code ID} is the unique id of the user
     */
    private int ID;

    /**
     * {@code assign} is the Issue that has been assigned to the user
     */
    private int assigned = 0;

    /**
     * {@code number_solved} is the unique id of the user
     */
    private int number_solved = 0;
    private String gmail;

    @JsonIgnoreProperties
    private transient Scanner sc = new Scanner(System.in);

    public People() {
    }

    public People(String password, String name, int ID, String gmail) {
        this.password = password;
        this.name = name;
        this.ID = ID;
        this.gmail = gmail;
    }

    public void addAssigned(int projectID, String projectName, int IssueID, String IssueTitle, String creator) {
        newAssignedNotification.add(new AssignedIssue(projectID, projectName, IssueID, IssueTitle, creator));
        addAssigned();
    }

    /**
     * Set the selected Issue to accepted, according to index
     *
     * @param select
     */
    public void dismissAndRemove(int select) {
        newAssignedNotification.remove(select);
    }

    public void addResolved() {
        number_solved++;
    }

    public void reduceResolved() {
        number_solved--;
    }

    public void addAssigned() {
        assigned++;
    }

    public void reduceAssigned() {
        assigned--;
    }

    //--Getter--
    public ArrayList<AssignedIssue> getNewAssignedNotification() {
        return newAssignedNotification;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public int getAssigned() {
        return assigned;
    }

    public int getNumber_solved() {
        return number_solved;
    }

    //--Setter--
    public void setNewAssignedNotification(ArrayList<AssignedIssue> newAssignedNotification) {
        this.newAssignedNotification = newAssignedNotification;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setAssigned(int assigned) {
        this.assigned = assigned;
    }

    public void setNumber_solved(int number_solved) {
        this.number_solved = number_solved;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }
    
}

class AssignedIssue {

    private String IssueInfo = "";

    public AssignedIssue() {
    }

    public AssignedIssue(int projectID, String projectName, int IssueID, String IssueTitle, String creator) {
        IssueInfo += String.format("Project ID: %5d", projectID);
        IssueInfo += String.format("Project Name: %20s\n", projectName);
        IssueInfo += String.format("Issue ID: %5d", IssueID);
        IssueInfo += String.format("Issue Name: %20s\n", IssueTitle);
        IssueInfo += String.format("Assigned by: %20s\n", creator);
    }
    //Get the assigned Issue Info
    public String getIssueInfo() {
        return IssueInfo;
    }

    public void setIssueInfo(String issueInfo) {
        IssueInfo = issueInfo;
    }

}
