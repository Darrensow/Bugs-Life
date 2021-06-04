package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;

import java.io.Serializable;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

@JsonIgnoreProperties(value = {"reaction"})
public class Comment implements Serializable {

    private int ID;
    private String createdBy;
    private String text;
    private String timestamp;
    private File image_file;

    private final String[] reaction = {"happy", "angry", "likes", "dislikes"};

    /**
     * This is a Hashmap that links the reaction and its count
     */
    private HashMap<String, Integer> counter = new HashMap<String, Integer>() {
        {
            for (int i = 0; i < reaction.length; i++) {
                put(reaction[i], 0);
            }
        }
    };

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
    public Comment(String createdBy, String text, int ID, File image_file) {
        if (createdBy == null) {
            throw new NullPointerException("Person cannot be anonymous");
        }
        this.image_file = image_file;
        this.ID = ID;
        this.createdBy = createdBy;
        this.text = text;
        this.timestamp = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(new java.util.Date(Instant.now().getEpochSecond() * 1000));
    }

    /**
     * This method will add reaction count through Hashmap
     *
     * @param reaction is the reaction in String, option is like, dislike, happy
     *                 and angry
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
        str += "\t" + timestamp + " By : " + this.createdBy;
        str += "\n" + this.text;
        str += reactionsToString();
        return str + "\n";
    }

    //helper method for toString(), returns a String representation of all the reactions
    private String reactionsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n$$ Reactions: ");
        sb.append("happy: " + counter.get("happy"));
        sb.append(" | angry: " + counter.get("angry"));
        sb.append(" | likes: " + counter.get("likes"));
        sb.append(" | dislikes: " + counter.get("dislikes"));
        return sb.toString();
    }

    public int gethappy() {
        return counter.get("happy");
    }

    public int getdislikes() {
        return counter.get("dislikes");
    }

    public int getangry() {
        return counter.get("angry");
    }

    public int getlike() {
        return counter.get("like");
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
        this.image_file = temp.image_file;
        this.ID = temp.ID;
        this.text = temp.text;
        this.counter = temp.counter;
        this.timestamp = temp.timestamp;
    }

    /*
        --- Mutator methods ---
     */

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public File getImage_file() {
        return image_file;
    }
    /*
        --- Accessor methods ---
     */


    public int getID() {
        return ID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public HashMap<String, Integer> getCounter() {
        return counter;
    }

    public void setCounter(HashMap<String, Integer> counter) {
        this.counter = counter;
    }

    public void setImage_file(File image_file) {
        this.image_file = image_file;
    }


}
