package Semag1;

import java.io.Serializable;
import java.util.ArrayList;

public class Text<E> implements Serializable {

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

    //StringBuilder can KIV first, can consider changing to better optimize the memory
    public String getString() {
        String result = "";
//        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i) + "\n";
//            sb.append(list.get(i) + "\n");
        }
//        return sb.toString();
        return result;
    }
}
