package Semag;

import java.util.ArrayList;

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
