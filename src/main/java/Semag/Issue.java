package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Issue {

    private ArrayList<Issue> changelog;
    private int ID;
    private String title;
    private String text;
    private LocalDateTime time;
    private People creator;
    private People assignee;
    private ArrayList<Comment> comment = new ArrayList<>();
    private static int numbercomment = 1;
    private String tags;
    private Integer priority;
    private String status;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    Scanner sc = new Scanner(System.in);
    private People current_people;

    public Issue() {
    }


    public People getAssignee() {
        return assignee;
    }

    public Issue(int ID, String title, String text, People creator, People assignee, String tags, int priop) {
        changelog = new ArrayList<>();  //only create the changelog Arraylist when a Issue is created
        this.ID = ID;
        this.title = title;
        this.creator = creator;
        this.assignee = assignee;
        time = LocalDateTime.now();
        this.text = text;
        this.status = "open";
        this.priority = priop;
        this.tags = tags;
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
    public void setStatus(String status) {
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
                    break;
            }
        } else if (status.equalsIgnoreCase("In progress")) {
            System.out.println("Set a status(1)Closed 2)Resolved): ");
            switch (sc.nextInt()) {
                case 1:
                    status = "Closed";
                    break;
                case 2:
                    status = "Resolved";
                    break;
            }
        } else if (status.equalsIgnoreCase("Resolved")) {
            System.out.println("Set a status(1)Closed 2)Reopen): ");
            switch (sc.nextInt()) {
                case 1:
                    status = "Closed";
                    break;
                case 2:
                    status = "Reopened";
                    break;
            }
        } else if (status.equalsIgnoreCase("Closed")) {
            System.out.println("Set a status(1)Reopen): ");
            switch (sc.nextInt()) {
                case 1:
                    status = "Reopen";
                    break;
                default:
                    break;
            }
        } else if (status.equalsIgnoreCase("Reopened")) {
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
            comment.get(index).happy();
        } else {
            comment.get(index).angry();
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
        comment.add(new Comment(current_people, text, numbercomment));
        numbercomment++;
    }

    /**
     *This method is overloading.
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
        sb.append("Comments\n-----------");
        for (Comment value : comment) {
            sb.append(value);
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
                        "Tag: %-20sPriority: %-20sCreated On: %-20s\n" +
                        "Title: %-40s\n" +
                        "Assigned to: %-20sCreated by: %-20s\n"
                , o.getID(), o.getStatus(),
                o.getTags(), o.getPriority(), o.getTime(),
                o.getTitle(),
                o.getAssignee(), o.getCreator());
        sb.append(issueInfo);
        sb.append("Issue Description\n-----------");
        sb.append("\n" + o.getText());
        sb.append(displayCommentsSection(o));

        System.out.println(sb.toString());
    }

    /**
     * Delete this Issue object
     */
    public void deleteThisIssue() {
        Project.removeIssue(this);
    }

    //----accessor
    public String getTitle() {
        return title;
    }

    public int getID() {
        return ID;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public People getCreator() {
        return creator;
    }

    public static int getNumbercomment() {
        return numbercomment;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getText() {
        return text;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }

    public String getStatus() {
        return status;
    }

    public String getTags() {
        return tags;
    }


    //-- Comparator Object --


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
     * Comparator for sorting the list by Tag
     */
    public static Comparator<Issue> timeComparator = new Comparator<Issue>() {
        @Override
        public int compare(Issue o1, Issue o2) {
            return o1.getTime().compareTo(o2.getTime());
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
     * Comparator for sorting the list by Title Name
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
        Issue temp = new Issue();
    }

    // -- Getter and setter methods --

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setCreator(People creator) {
        this.creator = creator;
    }

    public void setAssignee(People assignee) {
        this.assignee = assignee;
    }

    public void setComment(ArrayList<Comment> comment) {
        this.comment = comment;
    }

    public static void setNumbercomment(int numbercomment) {
        Issue.numbercomment = numbercomment;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public DateTimeFormatter getDtf() {
        return dtf;
    }

    public void setDtf(DateTimeFormatter dtf) {
        this.dtf = dtf;
    }

    public People getCurrent_people() {
        return current_people;
    }

    public void setCurrent_people(People current_people) {
        this.current_people = current_people;
    }

    public Comparator<Issue> getIDComparator() {
        return IDComparator;
    }

    public void setIDComparator(Comparator<Issue> IDComparator) {
        this.IDComparator = IDComparator;
    }

    public Comparator<Issue> getTitleComparator() {
        return TitleComparator;
    }

    public void setTitleComparator(Comparator<Issue> titleComparator) {
        TitleComparator = titleComparator;
    }

    public Comparator<Issue> getPriorityComparator() {
        return priorityComparator;
    }

    public void setPriorityComparator(Comparator<Issue> priorityComparator) {
        this.priorityComparator = priorityComparator;
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

    public Comparator<Issue> getTimeComparator() {
        return timeComparator;
    }

    public void setTimeComparator(Comparator<Issue> timeComparator) {
        this.timeComparator = timeComparator;
    }
}