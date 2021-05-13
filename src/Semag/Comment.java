package Semag;

import java.util.ArrayList;
import java.util.Date;

public class Comment {

    private ArrayList<Reply> replies = new ArrayList<>();
    private static int number = 0;
    private int commentID;
    private Date createdOn;
    private People createdBy;
    private String text;
    private int happy = 0;
    private int angry = 0;
    private int likes = 0;
    private int dislikes = 0;

    //empty constructor
    public Comment() { }

    /**
     * Constructor with three parameters
     * @param createdBy The person who commented
     * @param text His comment
     * @param ID Comment number
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
     * @return String representation of the comment
     */
    @Override
    public String toString() {
        String str = "#" + this.commentID;
        str += "\nCreated by " + this.createdBy + " on " + this.createdOn;
        str += "\n" + this.text;
        str += "\nReactions: ANGRY(" + this.angry + ") | HAPPY(" + this.happy + ")";
        str += "\nLikes: " + this.likes;
        str += "\nDislikes: " + this.dislikes;
        return str + "\n";
    }

    public void addReply() {

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
