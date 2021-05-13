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
public class PeopleADT {

    private static ArrayList<People> list = new ArrayList<>();

    public void add(People people_obj) {
        list.add(people_obj);
    }

    public People get(int index) {
        return list.get(index);
    }

    public void remove(int index) {
        list.remove(index);
    }

    public int size() {
        return list.size();
    }
}
