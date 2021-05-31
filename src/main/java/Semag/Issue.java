package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@JsonIgnoreProperties(value = {"changelog", "sc", "current_people", "timeComparator", "priorityComparator", "idComparator", "titleComparator", "statusComparator", "tagComparator", "TitleComparator", "IDComparator"})
public class Issue implements Serializable {

    private ArrayList<String> changelog;
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

    @JsonIgnore
    private Project project_belongsTo;      //for the delete function

    /**
     * Tags available
     */
    @JsonIgnore
    public  static ArrayList<String> tagsOption = new ArrayList<String>(){{
        add("frontend");
        add("backend");
        add("cmty:bug-report");
    }};

    public ArrayList<String> tagsOption_replica = new ArrayList<String>();



    /**
     * Tags available
     */
    private static final String[] statusOption = {"open", "in progress", "solved", "closed"};

    public Issue() {
    }

    public People getAssignee() {
        return assignee;
    }

    public Issue(Integer ID, String title, String text, People creator, People assignee, String[] tag, int priop, Project project_belongsTo) {
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
        this.timestamp = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(new java.util.Date(Instant.now().getEpochSecond() * 1000));
    }

    //add comment
    public void issuewindow_owner() {
        boolean quit = false;
        while (!quit) {
            try {
                print();
                System.out.print("1)Add a Comment 2)React on a Comment 3)Delete This Issue 4)Change Properties 5)Quit \nInput: ");
                int input1 = sc.nextInt();
                switch (input1) {
                    case 1:
                        addComment();
//                    changelog();
                        break;
                    case 2:
                        react();
//                    changelog();
                        break;
                    case 3:
                        deleteThisIssue();
//                    changelog();
                        break;
                    case 4:
                        changeProperties();
                        break;
                    case 5:
                        quit = true;
                        break;
                    default:
                        System.out.println("Invalid input. Please enter a value between 1-5");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a value between 1-5");
                sc.nextLine();
            }
        }
    }

    public void issuewindow() {
        boolean quit = false;
        while (!quit) {
            try {
                print();
                System.out.print("1)Add comment 2)React on comment 3)Quit \nInput: ");
                int input1 = sc.nextInt();
                switch (input1) {
                    case 1:
                        addComment();
//                    changelog();
                        break;
                    case 2:
                        react();
//                    changelog();
                        break;
                    case 3:
                        quit = true;
                        break;
                    default:                        //catch int other than 1-3
                        System.out.println("Invalid Input. Please input a value between 1-3");
                        break;
                }
            } catch (InputMismatchException e) {    //catch type error (String/double/char/etc)
                System.out.println("Invalid Input. Please input a value between 1-3");
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
        System.out.println("Enter 'h' or happy , 'a' for angry.");
        char reaction = sc.next().charAt(0);
        if (reaction == 'h') {
//            comments.get(index).happy();
        } else {
//            comments.get(index).angry();
        }
    }

    /**
     * Method to modify properties
     * Only modifiable properties are status, tag, priority and Description text
     */
    private void changeProperties() {
        boolean exit = false;
        while (!exit) {
            try {
                System.out.print("Changes: 1)Status 2)Tag 3)Priority 4)Description Text 5)Quit \nInput: ");
                switch (sc.nextInt()) {
                    case 1:
                        String statusUpdate = updateStatus();
                        if (!statusUpdate.equals("")) { //will not add if value of String is ""
                            if (addChangeLog(statusUpdate)) {
                                System.out.println("Status changed successfully!");
                                ;
                            }
                        }
                        break;
                    case 2:
                        if (addChangeLog(updateTag())) {
                            System.out.println("Tags changed successfully!");
                        }
                        break;
                    case 3:
                        String priorityUpdate = updatePriority();
                        if (!priorityUpdate.equals("")) {
                            if (addChangeLog(priorityUpdate)) {
                                System.out.println("Priority changed successfully!");
                            }
                        }
                        break;
                    case 4:
                        String descUpdate = updateDescriptionText();
                        if (!descUpdate.equals("")) {
                            if (addChangeLog(descUpdate)) {
                                System.out.println("Description changed successfully!");
                                ;
                            }
                        }
                        break;
                    case 5:
                        exit = true;
                        break;
                    default:                            //Used to catch values other than 1-5
                        System.out.println("Invalid input, please try again.");
                        break;
                }
            } catch (InputMismatchException e) {        //Used to catch other inputs (String/char/double)
                System.out.println("Invalid input. Please enter a value from 1 to 5");
            }
        }
    }

    /**
     * Method to update tags
     *
     * @return A message that will be added to the change log regarding the change of tags
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
     * Method to update priority
     *
     * @return A message that will be added to the change log regarding the change of priority
     */
    private String updatePriority() {
        StringBuilder sb = new StringBuilder();
        int originalPriority = this.priority;

        int input = -2;
        while (true) {
            try {
                System.out.println("Please input the new priority for the Issue. (Value is 0-9). Enter '-1' to quit if you don't want to change priority");
                input = sc.nextInt();
                if (input > 0 && input <= 10) {
                    this.priority = input;
                    break;
                } else if (input == -1) {
                    return "";          //empty String signalling no change to be made
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a value from 0 to 9 : ");
                sc.nextLine();
            }
        }

        sb.append(current_people.getName()).append(" updated the priority to ").append(this.priority).append(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(new java.util.Date(Instant.now().getEpochSecond() * 1000))).append("\n");
        return sb.toString();
    }


    /**
     * This method check current status and allow the associated operation
     *
     * @return A String representation of the updates made to the tag. The String will be added to the changeLog.
     */
    private String updateStatus() {
        StringBuilder sb = new StringBuilder();
        String originalStatus = this.getStatus();
        boolean exit = false;

        if (status.equalsIgnoreCase("open")) {
            while (!exit) {
                try {
                    System.out.print("Set a status -> (1)In Progress 2)Resolved 3)Closed 4)Quit : ");
                    switch (sc.nextInt()) {
                        case 1 -> {
                            status = "In Progress";
                            exit = true;
                        }
                        case 2 -> {
                            status = "Resolved";
                            exit = true;
                        }
                        case 3 -> {
                            status = "Closed";
                            exit = true;
                            current_people.addResolved();
                        }
                        case 4 -> {
                            return "";      //return empty String, if .equals(""), then won't add to changeLog
                        }
                        default -> System.out.println("Invalid input. Please enter a value from 1-4");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a value from 1-4");
                    sc.nextLine();
                }
            }
        } else if (status.equalsIgnoreCase("in progress")) {
            while (!exit) {
                try {
                    System.out.print("Set a status -> (1)Closed 2)Resolved) 3)Quit : ");
                    switch (sc.nextInt()) {
                        case 1 -> {
                            status = "Closed";
                            exit = true;
                            current_people.addResolved();
                        }
                        case 2 -> {
                            status = "Resolved";
                            exit = true;
                        }
                        case 3 -> {
                            return "";
                        }
                        default -> System.out.println("Invalid input. Please enter either 1 or 3");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter either 1 or 3");
                    sc.nextLine();
                }
            }
        } else if (status.equalsIgnoreCase("resolved")) {
            while (!exit) {
                try {
                    System.out.print("Set a status -> (1)Closed 2)Reopen 3)Quit : ");
                    switch (sc.nextInt()) {
                        case 1 -> {
                            status = "Closed";
                            exit = true;
                            current_people.addResolved();
                        }
                        case 2 -> {
                            status = "Open";
                            exit = true;
                        }
                        case 3 -> {
                            return "";
                        }
                        default -> System.out.println("Invalid input. Please enter either 1 or 3.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter either 1 or 3");
                    sc.nextLine();
                }
            }
        } else if (status.equalsIgnoreCase("closed")) {
            while (!exit) {
                try {
                    System.out.print("Set a status -> (1)Reopen (2)Quit : ");
                    switch (sc.nextInt()) {
                        case 1 -> {
                            status = "Open";
                            exit = true;
                            current_people.reduceResolved();
                        }
                        case 2 -> {
                            return "";
                        }
                        default -> {
                            System.out.println("Invalid input. Please enter either 1 or 2.");
                        }
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter either 1 or 2.");
                    sc.nextLine();
                }
            }
        }

        sb.append(current_people.getName()).append(" updated the status from '").append(originalStatus).append("' to '").append(this.getStatus());
        sb.append("' at ").append(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(new java.util.Date(Instant.now().getEpochSecond() * 1000))).append("\n");

        return sb.toString();
    }

    private String updateDescriptionText() {
        System.out.println("Enter new Issue description. ('Enter' to register a new line, '#undo' to undo a line, '#redo' to redo a line and '#quit' to finish.");
        String text = sc.nextLine();
        Text<String> text_obj = new Text<>();       //main stack to store all lines enterred by user
        Text<String> temp_text = new Text<>();      //temp stack for redo and und function
        while (!text.equals("#quit")) {
            if (text.equals("#undo")) {
                if (text_obj.getSize() < 0) {
                    return "";
                }
                temp_text.push(text_obj.pop());
            } else if (text.equals("#redo")) {
                if (temp_text.getSize() < 0) {
                    return "";
                }
                text_obj.push(temp_text.pop());
            } else {
                text_obj.push(text);
                temp_text.clear();
            }
            text = sc.nextLine();
        }
        text = text_obj.getString();
        this.descriptionText = text;

        return this.current_people.getName() + "changed to description at " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(new java.util.Date(Instant.now().getEpochSecond() * 1000)) + "\n";
    }

    /**
     * Add the change description to the changeLog ArrayList
     *
     * @param changedDescription The description detailing the changes made. If the String is "", then it will not be added.
     * @return True if successfully added to the changeLog, false otherwise.
     */
    private boolean addChangeLog(String changedDescription) {
        if (!changedDescription.equals("")) {
            this.changelog.add(changedDescription);
            return true;
        }
        return false;
    }

    /**
     * A method that returns the String representation of the change log
     *
     * @return String representation of the change log
     */
    public String returnChangeLog() {
        StringBuilder sb = new StringBuilder();
        for (String s : this.changelog) {
            sb.append(s).append("\n");
        }
        return sb.toString();
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
     * Show full Issue page details, this is an overloading method
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

    //Test today

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
        this.project_belongsTo = temp.project_belongsTo;
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