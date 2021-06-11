package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class PeopleADT  {

    private ArrayList<People> list = new ArrayList<>();

    public PeopleADT() {
    }

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

    public boolean contains(People obj) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(obj)) {
                return true;
            }
        }
        return false;
    }

    public boolean contain(String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // -- Getter & Setter --
    public ArrayList<People> getList() {
        return list;
    }

    public void setList(ArrayList<People> list) {
        this.list = list;
    }
}
