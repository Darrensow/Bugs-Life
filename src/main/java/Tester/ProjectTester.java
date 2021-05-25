package Tester;

import Semag.Comment;
import Semag.Issue;

/**
 * This package is a tester to our program
 * @author ZHUOLI
 */
public class ProjectTester {
    public static void main(String[] args) {
        Comment cmt = new Comment();
        cmt.loadData();
        System.out.println(cmt.toString());

        Issue isu = new Issue();
        isu.loadData();
        isu.print();
    }
}
