/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semag;

import java.time.LocalDate;

/**
 *
 * @author xianp
 */
public class Comment {

    private static int number = 0;
    private int ID;
    private LocalDate Date;
    private People name;
    private String text;
    private int happy = 0;
    private int angry = 0;  // can add on

    public Comment(People createdby, String text, int ID) {
        this.ID = ID;
        Date = LocalDate.now();
        this.name = createdby;
        this.text = text;
    }

    public void happy() {
        happy++;
    }

    public void angry() {
        angry++;
    }

    public String getText() {
        return text;
    }
    

}
