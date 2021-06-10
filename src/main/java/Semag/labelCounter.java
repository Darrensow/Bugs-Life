package Semag;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author xianp
 */
public class labelCounter implements Comparable<labelCounter>  {

    private String name;
    private Integer total = 0;

    public labelCounter() {
    }

    public labelCounter(String name) {
        this.name = name;
        add();
    }

    public labelCounter(String name,int num) {
        this.name = name;
        this.total = num;
    }

    public void add() {
        total++;
    }

    // -- Getter and setter methods --
    public String getName() {
        return name;
    }

    public int getTotal() {
        return total;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public int compareTo(labelCounter o) {
        return this.total.compareTo(o.total);
    }

    // Save and read data -- Jackson -- JSON --
    @JsonIgnore
    private static DataManagement dm = new DataManagement();

}
