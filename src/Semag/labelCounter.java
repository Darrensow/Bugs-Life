/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semag;

/**
 *
 * @author xianp
 */
public class labelCounter implements Comparable<labelCounter> {
    
    private String name;
    private Integer total = 0;
    
    public labelCounter(String name) {
        this.name = name;
        add();
    }
    
    public void add() {
        total++;
    }
    
    public String getName() {
        return name;
    }
    
    public int getTotal() {
        return total;
    }
    
    @Override
    public int compareTo(labelCounter o) {
        return this.total.compareTo(o.total);
    }
    
}
