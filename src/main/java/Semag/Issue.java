package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

@JsonIgnoreProperties(value = {"changelog", "dtf", "sc", "current+people", "timeComparator", "priorityComparator", "idComparator", "titleComparator", "statusComparator", "tagComparator", "TitleComparator", "IDComparator"})
public class Issue implements Serializable {

    private ArrayList<Issue> changelog;
    private Integer ID;
    private String title;
    private String descriptionText;
    private People creator;
    private People assignee;
    private ArrayList<Comment> comments = new ArrayList<>();
    private int numberOfComments;
    private String[] tag;
    private Integer priority;
    private String status;
    private String timestamp;
    transient Scanner sc = new Scanner(System.in);
    private People current_people;

    private Project project_belongsTo;      //for the delete function

    public Issue() {
    }

    public People getAssignee() {
        return assignee;
    }

    public Issue(Integer ID, String title, String text, People creator, People assignee, String[] tag, int priop,Project project_belongsTo) {
        changelog = new ArrayList<>();  //only create the changelog Arraylist when a Issue is created
        this.ID = ID;
        this.title = title;
        this.creator = creator;
        this.assignee = assignee;
        this.descriptionText = text;
        this.status = "open";
        this.priority = priop;
        this.tag = tag;
        this.project_belongsTo = project_belongsTo;
        this.timestamp = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss z").format(new java.util.Date (Instant.now().getEpochSecond()*1000));
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
                    changelog();
                    break;
                case 2:
                    react();
                    changelog();
                    break;
                case 3:
                    deleteThisIssue();
                    changelog();
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
                    changelog();
                    break;
                case 2:
                    react();
                    changelog();
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

    /**
     * This method check current status and allow the associated operation
     *
     * @param status the current status
     */
    public void updateStatus(String status) {
        if (status.equalsIgnoreCase("open")) {
            System.out.println("Set a status(1)In Progress 2)Resolved 3)Closed): ");
            switch (sc.nextInt()) {
                case 1:
                    status = "In Progress";
                    break;
                case 2:
                    status = "Resolved";
                    break;
                case 3:
                    status = "Closed";
                    current_people.addResolved();
                    break;
            }
        } else if (status.equalsIgnoreCase("in progress")) {
            System.out.println("Set a status(1)Closed 2)Resolved): ");
            switch (sc.nextInt()) {
                case 1:
                    status = "Closed";
                    current_people.addResolved();
                    break;
                case 2:
                    status = "Resolved";
                    break;
            }
        } else if (status.equalsIgnoreCase("resolved")) {
            System.out.println("Set a status(1)Closed 2)Reopen): ");
            switch (sc.nextInt()) {
                case 1:
                    status = "Closed";
                    current_people.addResolved();
                    break;
                case 2:
                    status = "Open";
                    break;
            }
        } else if (status.equalsIgnoreCase("closed")) {
            System.out.println("Set a status(1)Reopen): ");
            switch (sc.nextInt()) {
                case 1:
                    status = "Open";
                    current_people.reduceResolved();
                    break;
                default:
                    break;
            }
        }
    }

    public void react() {
        System.out.println("enter comment ID that you want to react");
        int index = sc.nextInt();
        System.out.println("Enter 'h' or happy , 'a' for angry.");
        char reaction = sc.next().charAt(0);
        if (reaction == 'h') {
//            comments.get(index).happy();
        } else {
//            comments.get(index).angry();
        }
    }

    public void changelog() {
        changelog.add(this);
    }

    /**
     * A window to show the changelog
     */
    public void viewChangelog() {
        print();
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
        numberOfComments++;
        comments.add(new Comment(current_people, text, numberOfComments));
    }

    /**
     * This method is overloading.
     *
     * @return String representation of the whole comment section.
     */
    public String displayCommentsSection() {
        return displayCommentsSection(this);
    }

    /**
     * Displays the whole comment section. Regarding the wrapping of the text,
     * have to modify the toString() in Comment class
     *
     * @return String representation of the whole comment section.
     */
    public String displayCommentsSection(Issue o) {
        StringBuilder sb = new StringBuilder();
        sb.append("Comments\n-----------\n");
        for (Comment value : comments) {
            sb.append(value).append("\n");
        }
        return sb.toString();
    }

    public void print() {
        print(this);
    }

    /**
     * Show full Issue page details
     */
    public void print(Issue o) {
        StringBuilder sb = new StringBuilder();

        String issueInfo = String.format("Issue ID: %-20sStatus: %-20s\n" +
                        "Tag: %-50s\nPriority: %-20sCreated On: %-20s\n" +
                        "Title: %-40s\n" +
                        "Assigned to: %-20sCreated by: %-20s\n\n"
                , o.getID(), o.getStatus(),
                o.returnAllTags(o), o.getPriority(), o.getTimestamp(),
                o.getTitle(),
                o.getAssignee().getName(), o.getCreator().getName());
        sb.append(issueInfo);
        sb.append("Issue Description\n-----------");
        sb.append("\n" + o.getDescriptionText());
        sb.append("\n\n").append(displayCommentsSection(o));

        System.out.println(sb.toString());
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

    /**
     * Delete this Issue object
     */
    public void deleteThisIssue() {
        project_belongsTo.removeIssue(this);
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
            return o1.getTimestamp().compareTo(o2.getTimestamp());
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


    /*
        -- Save and read data -- Jackson -- JSON --
     */
    @JsonIgnore
    private static DataManagement dm = new DataManagement();

    /**
     * Method to save data, calls the writeData method in DataManagement Class
     */
    public void saveData() {
        dm.writeData(this);
    }

    public void loadData() {
        Issue temp = dm.readIssueData();
        this.ID = temp.ID;
        this.title = temp.title;
        this.descriptionText = temp.descriptionText;
        this.creator = temp.creator;
        this.assignee = temp.assignee;
        this.comments = temp.comments;
        this.timestamp = temp.timestamp;
        this.tag = temp.tag;
        this.priority = temp.priority;
        this.status = temp.status;
    }

    /*
        -- Getter and setter methods --
     */

    public ArrayList<Issue> getChangelog() {
        return changelog;
    }

    public void setChangelog(ArrayList<Issue> changelog) {
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

    public People getCreator() {
        return creator;
    }

    public void setCreator(People creator) {
        this.creator = creator;
    }

    public void setAssignee(People assignee) {
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

    public void setTagComparator(Comparator<Issue> tagComparator) {
        this.tagComparator = tagComparator;
    }
}