package Semag;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class Changelog {
    private ArrayList<Issue> list = new ArrayList<>();

    public Changelog() { }

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
        Changelog temp = dm.readChangeLogData();
        this.list = temp.list;
    }


    // -- Getter and setter methods
    public ArrayList<Issue> getList() {
        return list;
    }

    public void setList(ArrayList<Issue> list) {
        this.list = list;
    }
}
