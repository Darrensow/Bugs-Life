package Semag1;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 *
 * @author xianp
 */
public class labelCounter implements Comparable<labelCounter> , Serializable {
    
    private String name;
    private Integer total = 0;

    public labelCounter() {
    }

    public labelCounter(String name) {
        this.name = name;
        add();
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

    /**
     * Method to save data, calls the writeData method in DataManagement Class
     */
    public void saveData(){
        dm.writeData(this);
    }

    public void loadData() {
        labelCounter temp = dm.readLabelCounterData();
        this.name = temp.name;
        this.total = temp.total;
    }
}
