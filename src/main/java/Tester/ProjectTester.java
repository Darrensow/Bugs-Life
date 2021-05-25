package Tester;

import Semag.Comment;
import Semag.Issue;

/**
 * This package is a tester to our program
 * @author ZHUOLI
 */
public class ProjectTester {
    public static void main(String[] args) {
        /*
            Please take note that these printing are just for testing, the main json file is data.json.
            For now, troubleshooting for Comment.java and Issue.java are using both Comment.json and Issue.json
            Once the troubleshooting for Project and Issue is done, the data.json file will be used as the main storage location
         */
        //Try to print from the Comment.json
        Comment cmt = new Comment();
        cmt.loadData();
        System.out.println(cmt.toString());

        //Try to prin from the Issue.json
        Issue isu = new Issue();
        isu.loadData();
        isu.print();
    }
}
