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
public class TagCounter {
    private String name;
    private Integer total = 0;

    public TagCounter() {
    }

    public TagCounter(String name) {
        this.name = name;
    }

    public void add() {
        total++;
    }

    // -- Getter and setter methods --
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    // Save and read data -- Jackson -- JSON --
    @JsonIgnore
    private static DataManagement dm = new DataManagement();
}
