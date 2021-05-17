package Semag;

import java.util.Date;

/**
 * The replies don't have IDs, they are just separeted with a blank line
 */

public class Reply {
    private Date createdOn;
    private People createdBy;
    private String text;
    private int likes = 0;
    private int dislikes = 0;

    //empty constructor
    public Reply() {}

    /**
     * Constructor with three parameters
     * @param createdBy Who replied
     * @param text Reply content
     */
    public Reply(People createdBy, String text) {
        this.createdOn = new Date();
        this.createdBy = createdBy;
        this.text = text;
    }

    /**
     * *Might have to wrap the text afterwards, KIV
     * @return String representation fo the reply
     */
    @Override
    public String toString() {
        String str = "\t" + this.createdBy + " | " + this.createdOn;
        str += "\n\t" + this.text;
        return str;
    }

    //Like and Dislike ---------------------
    public void like() {
        this.likes++;
    }

    public void dislike() {
        this.dislikes++;
    }

    //methods that may be used for GUI implementation - KIV first
//    public String displayLikes()
//    public String displayDislikes()

    //Mutator methods --------------

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setCreatedBy(People createdBy) {
        this.createdBy = createdBy;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    //Accessor methods -----------------

    public Date getCreatedOn() {
        return createdOn;
    }

    public People getCreatedBy() {
        return createdBy;
    }

    public String getText() {
        return text;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }
}
