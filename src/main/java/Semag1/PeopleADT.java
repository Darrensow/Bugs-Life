package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;

public class PeopleADT implements Serializable {

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

    public boolean contain(String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
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
        PeopleADT temp = dm.readPeopleADTData();
        this.list = temp.getList();
    }

    // -- Getter & Setter --
    public ArrayList<People> getList() {
        return list;
    }

    public void setList(ArrayList<People> list) {
        this.list = list;
    }
}
