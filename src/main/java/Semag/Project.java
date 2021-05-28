package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.*;

public class Project implements Serializable {

    private ArrayList<Issue> issue = new ArrayList<>();  // store issue
    PeopleADT people_Array;   // store people
    transient Scanner sc = new Scanner(System.in);
    private int numissue = 0;  // issue id

    @JsonIgnore
    private People current_people;  //current log in people


    /**
     * Project ID
     */
    private Integer ID;  //project id

    /**
     * Project Name
     */
    private String name; //project name

    /**
     * Project Owner, which is a People
     */
    private People owner;  // project owner

    public Project() {
    }

    /**
     * @param name
     * @param ID
     * @param owner create project
     */
    public Project(String name, int ID, People owner) {
        this.ID = ID;
        this.name = name;
        this.owner = owner;
    }

    /**
     * owner of project window
     */
    public void projectwindow_owner() {
        this.sortIssueBased(1);
        boolean quit = false;
        while (quit == false) {
            System.out.println("action? \n1)sort \n2)include \n3)exclude \n4)add issue \n5)search issue \n6)delete project \n7)quit");
            int input1 = sc.nextInt();
            switch (input1) {
                case 1:
                    System.out.println("sort based on \n1)priority \n2)time");
                    int in2 = sc.nextInt();
                    this.sortIssueBased(2);
                    break;
                case 2:
                    System.out.println("enter tags in format (#tags1#tags2)");
                    String tags_in = sc.nextLine();
                    System.out.println("enter state in format (#state1#state2)");
                    String states_in = sc.nextLine();
                    filterin(tags_in, states_in);
                    break;
                case 3:
                    System.out.println("enter tags in format (#tags1#tags2)");
                    String tags_out = sc.nextLine();
                    System.out.println("enter state in format (#state1#state2)");
                    String states_out = sc.nextLine();
                    filterout(tags_out, states_out);
                    break;
                case 4:
                    addissue();
                    break;
                case 5:
                    System.out.println("enter search project keyword or #ID");
                    String in4 = sc.next();
                    search(in4);
                    break;
                case 6:
                    deleteThisProject();
                case 7:
                    quit = true;
                    return;
                default:
                    System.out.println("wrong input");
                    break;
            }
        }
    }

    /**
     * normal user window
     */
    public void projectwindow() {
        this.sortIssueBased(1);
        boolean quit = false;
        while (quit == false) {
            System.out.println("action? \n1)sort \n2)include \n3)exclude \n4)add issue \n5)search issue \n6)quit");
            int input1 = sc.nextInt();
            switch (input1) {
                case 1:
                    System.out.println("sort based on \n1)priority \n2)time");
                    int in2 = sc.nextInt();
                    this.sortIssueBased(in2);
                    break;
                case 2:
                    System.out.println("enter tags in format (#tags1#tags2)");
                    String tags_in = sc.nextLine();
                    System.out.println("enter state in format (#state1#state2)");
                    String states_in = sc.nextLine();
                    filterin(tags_in, states_in);
                    break;
                case 3:
                    System.out.println("enter tags in format (#tags1#tags2)");
                    String tags_out = sc.nextLine();
                    System.out.println("enter state in format (#state1#state2)");
                    String states_out = sc.nextLine();
                    filterout(tags_out, states_out);
                    break;
                case 4:
                    addissue();
                    break;
                case 5:
                    System.out.println("enter search project keyword or #ID");
                    String in4 = sc.next();
                    search(in4);
                    break;
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
     * @param current_people determine whether is owner or not
     */
    public void projectwindow(People current_people) {
        if (current_people == owner) {
            this.current_people = current_people;
            projectwindow_owner();
        } else {
            this.current_people = current_people;
            projectwindow();
        }
    }


    /**
     * add issue
     */
    public void addissue() {
        System.out.println("Enter issue name");
        String issue_name = sc.next();
        System.out.println("enter tags with spacing in between");
        String issue_tags = sc.nextLine();
        String[] issue_tags_array = issue_tags.split(" ");
        System.out.println("Enter priority");
        int priority = sc.nextInt();
        People assignee_obj;
        while (true) {
            System.out.println("enter assignee:");
            String assignee = sc.nextLine();
            assignee_obj = searchpeople(assignee);
            if (assignee_obj != null) {
                break;
            }
            System.out.print("invalid name");
        }
        System.out.println("enter issue des");
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

        //To push notification to assignee_obj

        Issue iss = new Issue(numissue, issue_name, text, current_people, assignee_obj, issue_tags_array, priority,this);
        issue.add(iss);
        assignee_obj.addAssigned(this.ID,this.name,numissue,issue_name,current_people.getName());
        numissue++;
    }

    /**
     * @param input keyword search search issue
     */
    public void search(String input) { //  only input number will directly assume as ID
        if (isnumberic(input)) {
            entertheissue(Integer.parseInt(input.substring(1)));
        } else {
            if (printsearchResult(input)) {
                System.out.println("enter ID of project");
                int id = sc.nextInt();
                entertheissue(id);
            } else {
                System.out.println("not result found. re-enter keyword");
                String str = sc.nextLine();
                search(str);
            }
        }
    }

    /**
     * @param seachkeyword print issue
     * @return true if have isseu
     */
    public boolean printsearchResult(String seachkeyword) {
        ArrayList<Issue> temp = new ArrayList<>();
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        String[] token = seachkeyword.split(" ");
        for (int i = 0; i < issue.size(); i++) {
            for (int j = 0; j < token.length; j++) {
                if (issue.get(i).getTitle().contains(token[j] + " ")) {
                    temp.add(issue.get(i));
                    break;
                } else if (issue.get(i).getTitle().contains(" " + token[j] + " ")) {
                    temp.add(issue.get(i));
                    break;
                } else if (issue.get(i).getTitle().contains(" " + token[j])) {
                    temp.add(issue.get(i));
                    break;
                } else if (issue.get(i).getDescriptionText().contains(" " + token[j])) {
                    temp.add(issue.get(i));
                } else if (issue.get(i).getDescriptionText().contains(token[j] + " ")) {
                    temp.add(issue.get(i));
                } else if (issue.get(i).getDescriptionText().contains(" " + token[j] + " ")) {
                    temp.add(issue.get(i));
                } else if (checkcomment(issue.get(i).getComments(), token[j])) {
                    temp.add(issue.get(i));
                    break;
                }
            }
        }

        for (int i = 0; i < temp.size(); i++) {
            pq.add(temp.get(i));
        }
        for (int i = 0; i < temp.size(); i++) {
            System.out.println(pq.poll());
        }
        if (temp.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param arr comment arraylist
     * @param token keyword check a wword in the comment
     * @return
     */
    private boolean checkcomment(ArrayList<Comment> arr, String token) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getText().contains(token + " ")) {
                return true;
            } else if (arr.get(i).getText().contains(" " + token)) {
                return true;
            } else if (arr.get(i).getText().contains(" " + token + " ")) {
                return true;
            }
        }
        return false;
    }

    //check whether is id or not
    private boolean isnumberic(String sen) {
        try {
            if (sen.charAt(0) != '#') {
                return false;
            }
            double d = Double.parseDouble(sen.substring(1));
            if (d > issue.size()) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * This method will sort the Issue with the column that the user wish, and str8 print it out
     *
     * @param choose is the attribute of the Issue, eg ID, Title, returned as int
     */
    public void sortIssueBased(int choose) {
        ArrayList<Issue> sortedIssueList = new ArrayList<>(issue);
        switch (choose) {
            case 0: //0 is the first option, ID
                Collections.sort(sortedIssueList, Issue.IDComparator);
                break;
            case 1: //1 is the sec option, Priority
                Collections.sort(sortedIssueList, Issue.priorityComparator);
                break;
            case 2: //2 is the third option, IssueCount
                Collections.sort(sortedIssueList, Issue.timeComparator);
                break;
            default:
                break;
        }
        print(sortedIssueList);
    }


    /**
         * Delete current project
         * Update, this remove project we move it to Window
     */
    public void deleteThisProject() {
        for (int i = 0; i < issue.size(); i++) {
            removeIssue(issue.get(i));
        }
//        Window.removeProject(this);
    }

    /**
     * This is a method to just remove one issue from issue dashboard, called by Issue class
     * @param issue_obj issue to be removed
     */
    public void removeIssue(Issue issue_obj) {
        issue_obj.getAssignee().reduceAssigned();
        issue.remove(issue_obj);
        numissue--;
    }

    /**
     * print Issue list, this method is an overloading method
     */
    public void print() {
        this.print(issue);
    }

    /**
     * print selected list
     * Tag formatting may be weird if there are a lot of tags( String[] )
     */
    public void print(ArrayList<Issue> toPrint) {
        System.out.println(String.format("%3s %-30s %-15s %-15s %10s %-30s %-20s %-20s", "ID", "Title", "Status",
                "Tag", "Priority", "Time", "Assignee", "Creator"));
        for (int i = 0; i < toPrint.size(); i++) {
            System.out.println(printOneIssue(toPrint.get(i)));
        }
    }

    /**
     * This method return string representation of one Issue in the Issue Dashboard,
     * this method is called by {@code print(ArrayList<Issue> toPrint)}
     */
    public String printOneIssue(Issue o){
        StringBuilder str = new StringBuilder();
        str.append(String.format(" %3d",o.getID()));
        str.append(String.format(" %-30s",o.getTitle()));
        str.append(String.format(" %-15s",o.getStatus()));
        str.append(String.format(" %-15s",o.getTag()));
        str.append(String.format(" %10d",o.getPriority()));
        str.append(String.format(" %-30s", o.getTimestamp()));
        str.append(String.format(" %-20s",o.getAssignee().getName()));
        str.append(String.format(" %-20s" ,o.getCreator().getName()));
        return str.toString();
    }


    /*
  include  filter kind
     */
    private void filterin(String tag, String state) {
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        String[] tags = tag.split("#");
        String[] states = state.split("#");
        for (int i = 0; i < issue.size(); i++) {
            label :
            {
                /*
                    note to Sam, my IDE says that the two if statements are always false.
                    I think it's the states.length > j and tags.length > j problem, could you check it out further? Thanks
                 */
                for (int j = 0; j < Math.max(state.length(), tags.length); j++) {
                    if (states.length < j && issue.get(i).getStatus().equals(states[j])) {
                        pq.add(issue.get(i));
                        break label;
                    }
                    String[] tagsArray = issue.get(i).getTag();
                    for (int k = 0; k < tagsArray.length; k++) { //compares tags[i] with tagsArray[k]
                        if (tags.length < j && tagsArray[k].equals(tags[j])) {
                            pq.add(issue.get(i));
                            break label;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < issue.size(); i++) {
            System.out.println(pq.poll());
        }
    }

    /*
    exclude filter 
     */
    private void filterout(String tag, String state) {
        PriorityQueue<Issue> pq = new PriorityQueue<>();
        String[] tags = tag.split("#");
        String[] states = state.split("#");
        for (int i = 0; i < issue.size(); i++) {
            label :
            {
                /*
                    note to Sam, my IDE says that the two if statements are always false.
                    I think its the states.length > j and tags.length > j problem, could you check it out further?
                 */
                for (int j = 0; j < Math.max(state.length(), tags.length); j++) {
                    if (states.length < j && issue.get(i).getStatus().equals(states[j])) {
                        break label;
                    }
                    String[] tagsArray = issue.get(i).getTag();
                    for (int k = 0; k < tagsArray.length; k++) { //compares tags[i] with tagsArray[k]
                        if (tags.length < j && tagsArray[k].equals(tags[j])) {
                            break label;
                        }
                    }
                    pq.add(issue.get(i));
                }
            }
        }
        for (int i = 0; i < issue.size(); i++) {
            System.out.println(pq.poll());
        }
    }

    private People searchpeople(String name) {
        for (int i = 0; i < people_Array.size(); i++) {
            if (name.equals(people_Array.get(i).getName())) {
                return people_Array.get(i);
            }
        }
        return null;
    }


    /**
     * @param index issue index enter issue window
     */
    public void entertheissue(int index){
        issue.get(index).issuewindow(current_people);
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



    // Save and read data -- Jackson -- JSON --
    @JsonIgnore
    private static DataManagement dm = new DataManagement();

    /**
     * Method to save data, calls the writeData method in DataManagement Class
     */
    public void saveData(){
        dm.writeData(this);
    }

    public void loadData() {
        Project temp = dm.readProjectData();
        this.people_Array = temp.people_Array;
        this.ID = temp.ID;
        this.name = temp.name;
        this.owner = temp.owner;
        this.issue = temp.issue;
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

    public PeopleADT getPeople_Array() {
        return people_Array;
    }

    public People getOwner() {
        return owner;
    }


    //Setter method

    public void setIssue(ArrayList<Issue> issue) {
        this.issue = issue;
    }

    public void setPeople_Array(PeopleADT people_Array) {
        this.people_Array = people_Array;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setOwner(People owner) {
        this.owner = owner;
    }

    public static void setDm(DataManagement dm) {
        Project.dm = dm;
    }
}
