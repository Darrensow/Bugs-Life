package Semag;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class Comment implements Serializable {

    private ArrayList<Reply> replies = new ArrayList<>();
    private static int number = 0;
    private int commentID;
    private Date createdOn;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private People createdBy;
    private String text;
    private final String[] reaction = {"happy", "angry", "likes", "dislikes"};
    private int happy = 0;
    private int angry = 0;
    private int likes = 0;
    private int dislikes = 0;
    private static Scanner sc = new Scanner(System.in);

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
        this.commentID = ID;
        this.createdOn = new Date();
        this.createdBy = createdBy;
        this.text = text;
    }

    /**
     * This method will add reaction count through Hashmap
     * @param reaction is the reaction in String, option is like, dislike, happy and angry
     */
    public void addReaction(String reaction) {
        counter.replace(reaction, counter.get(reaction) + 1);
    }

    //Like and Dislike ---------------------
    public void like() {
        this.likes++;
    }

    public void dislike() {
        this.dislikes++;
    }

    //Reaction method ---------------------
    public void happy() {
        this.happy++;
    }

    public void angry() {
        this.angry++;
    }

    public String getText() {
        return this.text;
    }

    /**
     * @return String representation of the comment + its own replies section
     */
    @Override
    public String toString() {
        String str = "#" + this.commentID;
        str += "\n" + this.createdBy + " | " + this.createdOn;
        str += "\n" + this.text;
        str += "\nReactions: ANGRY(" + this.angry + ") | HAPPY(" + this.happy + ")";
        str += "\nLikes: " + this.likes;
        str += "\nDislikes: " + this.dislikes;
        str += displayRepliesSection();
        return str + "\n";
    }

    /**
     * Displays the whole replies section.
     * Regarding the wrapping of the text, have to modify the toString() in Reply class
     *
     * @return String representation of the whole repleis section.
     */
    public String displayRepliesSection() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\tReplies\n-----------");
        for (Reply value : replies) {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * Add replies under the comment
     */
    public void addReply() {
        //can be modified afterwards
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

        //call the constructor with 3 parameters in the Reply class.
        //discuss again how to link the People who are commenting and also Replying - KIV
        /**
         * My idea is below (create a new People on the spot and add their reply to the comments)
         * Anyone can reply (Anonymous)
         * Users can reply (non-Anonymous)
         */
        /*
            People replier = new People(...);
            replies.add(new Reply(replier, text));
         */
    }


    //methods that may be used for GUI implementation - KIV first
//    public String displayLikes()
//    public String displayDislikes()
//    public String displayReactions()

    //Mutator methods --------------
    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }

    public static void setNumber(int number) {
        Comment.number = number;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
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

    public void setHappy(int happy) {
        this.happy = happy;
    }

    public void setAngry(int angry) {
        this.angry = angry;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }


    //Accessor methods -----------------
    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public static int getNumber() {
        return number;
    }

    public int getCommentID() {
        return commentID;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public People getCreatedBy() {
        return createdBy;
    }

    public int getHappy() {
        return happy;
    }

    public int getAngry() {
        return angry;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }


}
