package Semag;
import org.w3c.dom.html.HTMLIsIndexElement;

import java.util.ArrayList;

public class Report {

    private ArrayList<people_int> ADT = new ArrayList<>();
    private ArrayList<labelCounter> label = new ArrayList<>();
    private Integer num_solve;
    private Integer num_nosolved;
    private Integer inprogress;

    private ArrayList<people_int> cum_ADT = new ArrayList<>();
    private ArrayList<labelCounter> cum_label = new ArrayList<>();
    private Integer cum_num_solve;
    private Integer cum_num_nosolved;
    private Integer cum_inprogress;

    private String top_perform = "";  // no need store
    private Integer max = -1;             //no
    private String tags = "";         // no
    private Integer max_tags = -1;        //no

   private String cum_topperform = "";
    private int cum_toperform_max = -1;
    private String cum_toptags = "";
    private int cum_maxtags = -1;

    public Report() {
    }

    public Report(ArrayList<people_int> ADT, int num_solve, int num_nosolved, int inprogress, ArrayList<labelCounter> label) {
        this.ADT = ADT;
        this.cum_ADT=ADT;
        this.num_solve = num_solve;
        this.cum_num_solve =num_solve;
        this.num_nosolved = num_nosolved;
        this.cum_num_nosolved= num_nosolved;
        this.inprogress = inprogress;
        this.cum_inprogress =inprogress;
        this.label = label;
        this.cum_label =label;
        FindCum();
    }

    public String getCum_topperform() {
        return cum_topperform;
    }

    public int getCum_toperform_max() {
        return cum_toperform_max;
    }

    public String getCum_toptags() {
        return cum_toptags;
    }

    public int getCum_maxtags() {
        return cum_maxtags;
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
            if (o.cum_ADT.size() > i) {
                temp.add(new people_int(ADT.get(i).getName(), ADT.get(i).getNum_resolved() - o.getCum_ADT().get(i).getNum_resolved()));
            } else {
                temp.add(new people_int(ADT.get(i).getName(), ADT.get(i).getNum_resolved()));
            }
        }
        ADT = temp;
        findtopperform();
        ArrayList<labelCounter> temp_label = new ArrayList<>();
        for (int i = 0; i < label.size(); i++) {
            boolean have = false;
            for (int j = 0; j < o.cum_label.size(); j++) {
                if (o.cum_label.get(j).getName().equals(label.get(i).getName())) {
                    temp_label.add(new labelCounter(label.get(i).getName(), label.get(i).getTotal() - o.cum_label.get(j).getTotal()));
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

    public void FindCum(){
        for (int i = 0; i < cum_ADT.size(); i++) {
            if (cum_ADT.get(i).getNum_resolved() > cum_toperform_max) {
                cum_toperform_max = cum_ADT.get(i).getNum_resolved();
                cum_topperform = cum_ADT.get(i).getName();
            } else if (cum_ADT.get(i).getNum_resolved() == cum_toperform_max && cum_toperform_max > 0) {
                cum_topperform += cum_ADT.get(i).getName() + "; ";
            }
        }

        for (int i = 0; i < cum_label.size(); i++) {
            if (cum_label.get(i).getTotal() > cum_maxtags) {
                cum_maxtags = cum_label.get(i).getTotal();
                cum_toptags = cum_label.get(i).getName();
            } else if (cum_label.get(i).getTotal() == cum_maxtags && cum_maxtags > 0) {
                cum_toptags += cum_label.get(i).getName() + "; ";
            }
        }
    }

    public ArrayList<people_int> getCum_ADT() {
        return cum_ADT;
    }

    public void setCum_ADT(ArrayList<people_int> cum_ADT) {
        this.cum_ADT = cum_ADT;
    }

    public ArrayList<labelCounter> getCum_label() {
        return cum_label;
    }

    public void setCum_label(ArrayList<labelCounter> cum_label) {
        this.cum_label = cum_label;
    }

    public Integer getCum_num_solve() {
        return cum_num_solve;
    }

    public void setCum_num_solve(Integer cum_num_solve) {
        this.cum_num_solve = cum_num_solve;
    }

    public Integer getCum_num_nosolved() {
        return cum_num_nosolved;
    }

    public void setCum_num_nosolved(Integer cum_num_nosolved) {
        this.cum_num_nosolved = cum_num_nosolved;
    }

    public Integer getCum_inprogress() {
        return cum_inprogress;
    }

    public void setCum_inprogress(Integer cum_inprogress) {
        this.cum_inprogress = cum_inprogress;
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

    public void setLabel(ArrayList<labelCounter> label) {
        this.label = label;
    }

    public void setNum_solve(Integer num_solve) {
        this.num_solve = num_solve;
    }

    public void setNum_nosolved(Integer num_nosolved) {
        this.num_nosolved = num_nosolved;
    }

    public void setInprogress(Integer inprogress) {
        this.inprogress = inprogress;
    }
}

class people_int {
    private String name;
    private Integer num_resolved;

    public people_int() {
    }

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

    public Integer getNum_resolved() {
        return num_resolved;
    }

    public void setNum_resolved(int num_resolved) {
        this.num_resolved = num_resolved;
    }

    public void setNum_resolved(Integer num_resolved) {
        this.num_resolved = num_resolved;
    }
}
