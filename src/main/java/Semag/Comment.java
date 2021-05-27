package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

@JsonIgnoreProperties(value = {"number", "reaction", "happy", "angry", "likes", "dislikes", "sc"})
public class Comment implements Serializable {

    private static int number = 0;
    private int ID;
    private Date createdOn;
    private transient DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private People createdBy;
    private String text;
    private long timestamp;
    private String user;

    private final String[] reaction = {"happy", "angry", "likes", "dislikes"};
    private static transient Scanner sc = new Scanner(System.in);

    /**
     * This is a Hashmap that links the reaction and its count
     */
    private HashMap<String, Integer> counter = new HashMap<String, Integer>() {{
        for (int i = 0; i < reaction.length; i++) {
            put(reaction[i], 0);
        }
    }};

    //empty constructor
    public Comment() {
    }

    /**
     * Constructor with three parameters
     *
     * @param createdBy The person who commented
     * @param text      His comment
     * @param ID        Comment number
     */
    public Comment(People createdBy, String text, int ID) {
        if (createdBy == null) {
            throw new NullPointerException("Person cannot be anonymous");
        }
        this.ID = ID;
        this.createdOn = new Date();
        this.createdBy = createdBy;
        this.text = text;
        this.timestamp = Instant.now().getEpochSecond();
    }

    /**
     * This method will add reaction count through Hashmap
     * @param reaction is the reaction in String, option is like, dislike, happy and angry
     */
    public void addReaction(String reaction) {
        counter.replace(reaction, counter.get(reaction) + 1);
    }


    public String getText() {
        return this.text;
    }

    /**
     * @return String representation of the comment + its own replies section
     */
    @Override
    public String toString() {
        String str = "#" + this.ID;
        str += "\t" + timestamp + " By : " + this.user;
        str += "\n" + this.text;
        str += reactionsToString();
        return str + "\n";
    }

    //helper method for toString(), returns a String representation of all the reactions
    private String reactionsToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n$$ Reactions: ");
        sb.append("happy: " + counter.get("happy"));
        sb.append(" | angry: " + counter.get("angry"));
        sb.append(" | likes: " + counter.get("likes"));
        sb.append(" | dislikes: " + counter.get("dislikes"));
        return sb.toString();
    }


    //methods that may be used for GUI implementation - KIV first
//    public String displayLikes()
//    public String displayDislikes()
//    public String displayReactions()

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
        Comment temp = dm.readCommentData();
        this.ID = temp.ID;
        this.text = temp.text;
        this.counter = temp.counter;
        this.timestamp = temp.timestamp;
        this.user = temp.user;
    }

    /*
        --- Mutator methods ---
     */

    public static void setNumber(int number) {
        Comment.number = number;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setCreatedBy(People createdBy) {
        this.createdBy = createdBy;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    /*
        --- Accessor methods ---
     */

    public static int getNumber() {
        return number;
    }

    public int getID() {
        return ID;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public People getCreatedBy() {
        return createdBy;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public HashMap<String, Integer> getCounter() {
        return counter;
    }

    public void setCounter(HashMap<String, Integer> counter) {
        this.counter = counter;
    }
}
