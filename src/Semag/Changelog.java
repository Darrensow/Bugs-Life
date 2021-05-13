/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semag;

import java.util.ArrayList;

/**
 *
 * @author xianp
 */
public class Changelog {
    private ArrayList<Issue> list = new ArrayList<>();

    public void push(Issue o) throws CloneNotSupportedException {
        list.add((Issue) o.clone());
    }

    public Issue pop() {
        Issue o = list.get(getSize() - 1);
        list.remove(getSize() - 1);
        return o;
    }

    public Issue peek() {
        return list.get(getSize() - 1);
    }

    public int getSize() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public String toString() {
        return "stack: " + list.toString();
    }    
}
