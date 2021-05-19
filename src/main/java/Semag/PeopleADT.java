package Semag;

import java.io.Serializable;
import java.util.ArrayList;

public class PeopleADT implements Serializable {

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

    public boolean contain(String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
