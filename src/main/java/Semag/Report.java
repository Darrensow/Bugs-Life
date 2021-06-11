package Semag;
import java.util.ArrayList;

public class Report {
    // only these five need to save
    private ArrayList<PerformanceCounter> cumuADT = new ArrayList<>(); //cumulative PerformanceCounter
    private ArrayList<TagCounter> cumuLabel = new ArrayList<>();      //cumulative label
    private Integer cumuSolved;                                      //cumulative solved number
    private Integer cumuUnsolved;                                   //cumulative unsolved number
    private Integer cumuInProgress;                                //cumulative in progress number

    private String TopPerformCumu = "";         //cumulative top performer
    private int maxSolvedCumu = -1;             //his/her performance
    private String topTagsCumu = "";            //cumulative top tags
    private int maxTagsCumu = -1;               //tags' usage


    private Integer solvedThisWeek;             //oneWeek Performance
    private Integer unsolvedThisWeek;           //oneWeek Performance
    private Integer inProgressThisWeek;

    private String topPerformThisWeek = "";     //oneWeek top performer
    private Integer maxSolvedThisWeek = -1;     //oneWeek top performer's performance
    private String topTagsThisWeek = "";        //oneWeek top tags
    private Integer maxTagsThisWeek = -1;       //oneWeek top tags's usage


    public Report() {
    }

    public Report(ArrayList<PerformanceCounter> cumuADT, int cumuSolved, int cumuUnsolved, int cumuInProgress, ArrayList<TagCounter> cumuLabel) {
        this.cumuADT =cumuADT;
        this.cumuSolved = cumuSolved;
        this.cumuUnsolved = cumuUnsolved;
        this.cumuInProgress = cumuInProgress;
        this.cumuLabel =cumuLabel;

        FindCumuTopPerformerAndTags();
    }


    public void finddifferent(Report o) {
        solvedThisWeek = this.solvedThisWeek - o.getCumuSolved();
        unsolvedThisWeek = this.cumuSolved - o.getCumuUnsolved();
        inProgressThisWeek = this.inProgressThisWeek - o.getCumuInProgress();

        for (int i = 0; i < o.cumuADT.size(); i++) {     //previous worker evaluation
            int difference = this.cumuADT.get(i).getNum_resolved() - o.cumuSolved;
            if (difference > maxSolvedThisWeek) {
                maxSolvedThisWeek = difference;
                topPerformThisWeek = this.cumuADT.get(i).getName();

            } else if (difference == maxSolvedThisWeek && maxSolvedThisWeek > 0) {
                topPerformThisWeek += this.cumuADT.get(i).getName() + "; ";
            }
        }

        if(this.cumuADT.size() - o.cumuADT.size() > 0){    //evaluate if there is new guy in the office
            int newgGuyIndex = o.cumuADT.size() + 1;
            int newGuyCount = this.cumuADT.size() - o.cumuADT.size();

            for (int i = newgGuyIndex; i <= newGuyCount; i++) {
                int difference = cumuADT.get(i).getNum_resolved();

                if (difference > maxSolvedThisWeek) {
                    maxSolvedThisWeek = difference;
                    topPerformThisWeek = this.cumuADT.get(i).getName();
                } else if (difference == maxSolvedThisWeek && maxSolvedThisWeek > 0) {
                    topPerformThisWeek += this.cumuADT.get(i).getName() + "; ";
                }
            }
        }


        for (int i = 0; i < o.cumuLabel.size(); i++) {

            int difference = this.cumuLabel.get(i).getTotal() - o.cumuLabel.get(i).getTotal();
            if (difference > maxTagsThisWeek) {
                maxTagsThisWeek = difference;
                topTagsThisWeek = this.cumuLabel.get(i).getName();

            } else if (difference == maxTagsThisWeek && maxTagsThisWeek > 0) {
                topTagsThisWeek += this.cumuLabel.get(i).getName() + "; ";
            }

        }

        if(this.cumuLabel.size() - o.cumuLabel.size() > 0){    //evaluate if there is new tags
            int newgTagIndex = o.cumuLabel.size() + 1;
            int newTagCount = this.cumuLabel.size() - o.cumuLabel.size();

            for (int i = newgTagIndex; i <= newTagCount; i++) {

                int difference = cumuLabel.get(i).getTotal();

                if (difference > maxTagsThisWeek) {
                    maxTagsThisWeek = difference;
                    topTagsThisWeek = this.cumuLabel.get(i).getName();
                } else if (difference == maxTagsThisWeek && maxTagsThisWeek > 0) {
                    topTagsThisWeek += this.cumuLabel.get(i).getName() + "; ";
                }
            }
        }
    }

    public void FindCumuTopPerformerAndTags(){
        for (int i = 0; i < cumuADT.size(); i++) {
            if (cumuADT.get(i).getNum_resolved() > maxSolvedCumu) {
                maxSolvedCumu = cumuADT.get(i).getNum_resolved();
                TopPerformCumu = cumuADT.get(i).getName();
            } else if (cumuADT.get(i).getNum_resolved() == maxSolvedCumu && maxSolvedCumu > 0) {
                TopPerformCumu += cumuADT.get(i).getName() + "; ";
            }
        }

        for (int i = 0; i < cumuLabel.size(); i++) {
            if (cumuLabel.get(i).getTotal() > maxTagsCumu) {
                maxTagsCumu = cumuLabel.get(i).getTotal();
                topTagsCumu = cumuLabel.get(i).getName();
            } else if (cumuLabel.get(i).getTotal() == maxTagsCumu && maxTagsCumu > 0) {
                topTagsCumu += cumuLabel.get(i).getName() + "; ";
            }
        }
    }


    //-- Information to generate report, & do not need to save in json. --
    //-- Another 3 info at below. --
    public String getTopPerformCumu() {
        return TopPerformCumu;
    }

    public int getMaxSolvedCumu() {
        return maxSolvedCumu;
    }

    public String getTopTagsCumu() {
        return topTagsCumu;
    }

    public int getMaxTagsCumu() {
        return maxTagsCumu;
    }


    public int getSolvedThisWeek() {
        return solvedThisWeek;
    }

    public int getUnsolvedThisWeek() {
        return unsolvedThisWeek;
    }


    public int getInProgressThisWeek() {
        return inProgressThisWeek;
    }

    public String getTopPerformThisWeek() {
        return topPerformThisWeek;
    }

    public int getMaxSolvedThisWeek() {
        return maxSolvedThisWeek;
    }


    public String getTopTagsThisWeek() {
        return topTagsThisWeek;
    }

    public int getMaxTagsThisWeek() {
        return maxTagsThisWeek;
    }






    // -- Getter & Setter --
    public ArrayList<PerformanceCounter> getCumuADT() {
        return cumuADT;
    }

    public void setCumuADT(ArrayList<PerformanceCounter> cumuADT) {
        this.cumuADT = cumuADT;
    }

    public ArrayList<TagCounter> getCumuLabel() {
        return cumuLabel;
    }

    public void setCumuLabel(ArrayList<TagCounter> cumuLabel) {
        this.cumuLabel = cumuLabel;
    }

    public Integer getCumuSolved() {
        return cumuSolved;
    }

    public void setCumuSolved(Integer cumuSolved) {
        this.cumuSolved = cumuSolved;
    }

    public Integer getCumuUnsolved() {
        return cumuUnsolved;
    }

    public void setCumuUnsolved(Integer cumuUnsolved) {
        this.cumuUnsolved = cumuUnsolved;
    }

    public Integer getCumuInProgress() {
        return cumuInProgress;
    }

    public void setCumuInProgress(Integer cumuInProgress) {
        this.cumuInProgress = cumuInProgress;
    }

}

