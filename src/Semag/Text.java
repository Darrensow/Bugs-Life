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
public class Text<E> {

    private ArrayList<E> list = new ArrayList<>();

    public void push(E o) {
        list.add(o);
    }

    public E pop() {
        E o = list.get(getSize() - 1);
        list.remove(getSize() - 1);
        return o;
    }

    public E peek() {
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

    public boolean search(E o) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == o) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        list.clear();
    }

    public String getString() {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i) + "\n";
        }
        return result;
    }
}
