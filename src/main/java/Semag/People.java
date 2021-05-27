package Semag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class People implements Serializable {

    public ArrayList<AssignedIssue> newAssignedNotification = new ArrayList<>();
    private String password;
    private String name;
    private int assigned = 0;
    private int ID;
    private int number_solved = 0;

    private Scanner sc = new Scanner(System.in);

    public People() {
    }

    public People(String password, String name, int ID) {
        this.password = password;
        this.name = name;
        this.ID = ID;
    }

    public void addResolved() {
        number_solved++;
    }

    public void declineResolved() {
        number_solved--;
    }

    public void addAssigned(int projectID, String projectName, int IssueID, String IssueTitle, String creator) {
        newAssignedNotification.add(new AssignedIssue(projectID, projectName, IssueID, IssueTitle, creator));
        assigned();
    }

    public void displayNewAssigned() {
        if (!newAssignedNotification.isEmpty()) {
            for (int i = newAssignedNotification.size(); i < 0; i--) {
                if (newAssignedNotification.get(i).isAccepted() == false) {
                    System.out.println(newAssignedNotification.get(i).getIssueInfo());
                }
            }
            System.out.println("Choose Issue to be accepted(starting from 1): ");
            acceptAssign(sc.nextInt());
        }
    }

    /**
     * Set the selected Issue to accepted, according to index
     * @param select
     */
    public void acceptAssign(int select) {
        newAssignedNotification.get(newAssignedNotification.size() - select + 1).setAccepted(true);
    }

    public void assigned() {
        assigned++;
    }

    public void reduceassigned() {
        assigned--;
    }

    public int getNumber_solved() {
        return number_solved;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public int getAssigned() {
        return assigned;
    }

    public int getID() {
        return ID;
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

}

class AssignedIssue {
    private String IssueInfo = "";
    private boolean accepted;

    public AssignedIssue() {
    }

    public AssignedIssue(int projectID, String projectName, int IssueID, String IssueTitle, String creator) {
        IssueInfo += String.format("Project ID: %5d", projectID);
        IssueInfo += String.format("Project Name: %20s\n", projectName);
        IssueInfo += String.format("Issue ID: %5d", IssueID);
        IssueInfo += String.format("Issue Name: %20s\n", IssueTitle);
        IssueInfo += String.format("Assigned by: %20s\n", creator);
        accepted = false;
    }

    //Get the assigned Issue Info
    public String getIssueInfo() {
        return IssueInfo;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setIssueInfo(String issueInfo) {
        IssueInfo = issueInfo;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
