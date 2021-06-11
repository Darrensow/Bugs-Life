package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

@JsonIgnoreProperties(value = {"reaction"})
public class Comment {

    private int ID;
    private String createdBy;
    private String text;
    private String timestamp;
    private File image_file;

    private final String[] reaction = {"happy", "angry", "likes", "dislikes"};

    /**
     * This is a Hashmap that links the reaction and the users that reacted
     */
    private HashMap<String, ArrayList<String>> counter = new HashMap<>() {
        //Initialisation block
        {
            for (int i = 0; i < reaction.length; i++) {
                put(reaction[i], new ArrayList<>());
            }
        }
    };

    ///// Empty constructor /////
    public Comment() {}

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

    public String getText() {
        return this.text;
    }

    /**
     * @return String representation of the comment
     */
    @Override
    public String toString() {
        String str = this.text + "\n\n";
        str += String.format("#%-10d Created on: %-34s By: %-12s\n", this.ID, this.timestamp, this.createdBy);
        return str;
    }

    /*
        * These four methods are used to display out the count of the reaction
     */
    public int happycount() {
        return counter.get("happy").size();
    }

    public int dislikecount() {
        return counter.get("dislikes").size();
    }

    public int angrycount() {
        return counter.get("angry").size();
    }

    public int likecount() {
        return counter.get("likes").size();
    }

    /**
     * Method to check whether the user has reacted before
     * @param reaction The reaction
     * @param username Username to be checked
     * @return True if the user reacted on the reaction.
     */
    public boolean reactedBefore(String reaction, String username) {
        if (reaction == null) {
            throw new NullPointerException("Reaction cannot be null");
        }
        if (username == null) {
            throw new NullPointerException("Username cannot be null");
        }
        return counter.get(reaction).contains(username);
    }

    /**
     * Method to check if the user can react.
     * @param username Username
     * @return True if can react, false otherwise
     */
    public boolean canReact(String username) {
        return !reactedBefore("happy", username) && !reactedBefore("angry", username)
                && !reactedBefore("likes", username) && !reactedBefore("dislikes", username);
    }

    //Helper method to return the reaction that the user reacted on. Used to check for unreacting reactino feature
    private String reactedReaction(String username) {
        if (reactedBefore("happy", username)) {
            return "happy";
        } else if (reactedBefore("angry", username)) {
            return "angry";
        } else if (reactedBefore("likes", username)) {
            return "likes";
        } else if (reactedBefore("dislikes", username)) {
            return "dislikes";
        }
        return "";
    }

    /**
     * This method will remove the reaction through hashmap
     * @param reaction The reaction the user reacted on
     * @param username Username of the person who reacted, used to add and check
     */
    public void addReaction(String reaction, String username) {
        if (canReact(username)) {                                           // User has no reaction to a particular comment
            counter.get(reaction).add(username);                            // Add the reaction
        } else {
            if (reactedReaction(username).equals(reaction)) {               // User clicks on the same button
                counter.get(reactedReaction(username)).remove(username);    // Removes the reaction
            } else {                                                        // User clicks on other buttons
                counter.get(reactedReaction(username)).remove(username);    // Removes
                counter.get(reaction).add(username);                        // Add new reaction
            }
        }
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

    public HashMap<String, ArrayList<String>> getCounter() {
        return counter;
    }

    public void setCounter(HashMap<String, ArrayList<String>> counter) {
        this.counter = counter;
    }

    public void setImage_file(File image_file) {
        this.image_file = image_file;
    }


}
