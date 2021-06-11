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

    public String getText() {
        return this.text;
    }

    /**
     * @return String representation of the comment
     */
    @Override
    public String toString() {
        String str = "#" + this.ID;
        str += "\tCreated on: " + timestamp + " \tBy: " + this.createdBy;
        str += "\n" + this.text;
        return str + "\n";
    }

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


    /*
        -- Save and read data -- Jackson -- JSON --
     */
    @JsonIgnore
    private static DataManagement dm = new DataManagement();

    public boolean reactedBefore(String reaction, String username) {
        if (reaction == null) {
            throw new NullPointerException("Reaction cannot be null");
        }
        if (username == null) {
            throw new NullPointerException("Username cannot be null");
        }
        return counter.get(reaction).contains(username);
    }

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
     *
     * @param reaction
     * @param username
     */
    public void addReaction(String reaction, String username) {
//        System.out.println("Username " + username);
        if (canReact(username)) {                                           // User has no reaction to a particular comment
//            System.out.println("Username react " + canReact(username) + " " + reaction);
            counter.get(reaction).add(username);                            // Add the reaction
        } else {
            if (reactedReaction(username).equals(reaction)) {               // User clicks on the same button
                counter.get(reactedReaction(username)).remove(username);    // Removes the reaction
            } else {                                                        // User clicks on other buttons
//                System.out.println("Username removed " + canReact(username) + " " + reaction);
                counter.get(reactedReaction(username)).remove(username);    // Removes
                counter.get(reaction).add(username);                        // Add new reaction
            }
        }
    }


    public boolean a(String now, String name) {
        String[] a = {"happy", "angry", "dislike", "like"};
        for (int i = 0; i < a.length; i++) {
            if (!now.equals(a[i])) {
                if (reactedBefore(a[i], name)) {
                    return false;
                }
            }
        }
        return true;
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
