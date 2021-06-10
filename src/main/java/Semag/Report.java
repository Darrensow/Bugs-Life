package Semag;

import java.util.ArrayList;

public class Report {

    private ArrayList<people_int> ADT = new ArrayList<>();
    private ArrayList<labelCounter> label = new ArrayList<>();
    private int num_solve;
    private int num_nosolved;
    private int inprogress;
    private String top_perform = "";  // no need store
    private int max = -1;             //no
    private String tags = "";         // no
    private int max_tags = -1;        //no

    public Report(ArrayList<people_int> ADT, int num_solve, int num_nosolved, int inprogress, ArrayList<labelCounter> label) {
        this.ADT = ADT;
        this.num_solve = num_solve;
        this.num_nosolved = num_nosolved;
        this.inprogress = inprogress;
        this.label = label;
    }
    public ArrayList<labelCounter> getLabel() {
        return label;
    }

    public String getTags() {
        return tags;
    }

    public int getMax_tags() {
        return max_tags;
    }
    public void finddifferent(Report o) {
        num_solve -= o.num_solve;
        num_nosolved -= o.num_nosolved;
        inprogress -= o.inprogress;
        ArrayList<people_int> temp = new ArrayList<>();
        for (int i = 0; i < ADT.size(); i++) {
            if (o.ADT.size() > i) {
                temp.add(new people_int(ADT.get(i).getName(), ADT.get(i).getNum_resolved() - o.ADT.get(i).getNum_resolved()));
            } else {
                temp.add(new people_int(ADT.get(i).getName(), ADT.get(i).getNum_resolved()));
            }
        }
        ADT = temp;
        findtopperform();
        ArrayList<labelCounter> temp_label = new ArrayList<>();
        for (int i = 0; i < label.size(); i++) {
            boolean have = false;
            for (int j = 0; j < o.label.size(); j++) {
                if (o.label.get(j).getName().equals(label.get(i).getName())) {
                    temp_label.add(new labelCounter(label.get(i).getName(), label.get(i).getTotal() - o.label.get(j).getTotal()));
                    have = true;
                    break;
                }
            }
            if(have==false){
                temp_label.add(new labelCounter(label.get(i).getName(), label.get(i).getTotal()));
            }
        }
        label =temp_label;
        findtags();

    }

    public void findtopperform() {
        for (int i = 0; i < ADT.size(); i++) {
            if (ADT.get(i).getNum_resolved() > max) {
                max = ADT.get(i).getNum_resolved();
                top_perform = ADT.get(i).getName();
            } else if (ADT.get(i).getNum_resolved() == max && max > 0) {
                top_perform += ADT.get(i).getName() + "; ";
            }
        }
    }

    public void findtags() {

        for (int i = 0; i < label.size(); i++) {
            if (label.get(i).getTotal() > max_tags) {
                max_tags = label.get(i).getTotal();
                tags = label.get(i).getName();
            } else if (label.get(i).getTotal() == max_tags && max_tags > 0) {
                tags += label.get(i).getName() + "; ";
            }
        }
    }


    public int getMax() {
        return max;
    }

    public String getTop_perform() {
        return top_perform;
    }

    public int getNum_solve() {
        return num_solve;
    }


    public void setNum_solve(int num_solve) {
        this.num_solve = num_solve;
    }

    public int getNum_nosolved() {
        return num_nosolved;
    }

    public void setNum_nosolved(int num_nosolved) {
        this.num_nosolved = num_nosolved;
    }

    public int getInprogress() {
        return inprogress;
    }

    public void setInprogress(int inprogress) {
        this.inprogress = inprogress;
    }

    public ArrayList<people_int> getADT() {
        return ADT;
    }

    public void setADT(ArrayList<people_int> ADT) {
        this.ADT = ADT;
    }

    public Report(ArrayList<people_int> ADT) {
        this.ADT = ADT;
    }
}

class people_int {
    private String name;
    private int num_resolved;

    public people_int(String name, int num_resolved) {
        this.name = name;
        this.num_resolved = num_resolved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum_resolved() {
        return num_resolved;
    }

    public void setNum_resolved(int num_resolved) {
        this.num_resolved = num_resolved;
    }
}
