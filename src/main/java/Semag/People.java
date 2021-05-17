package Semag;

public class People {

    private String password;
    private String name;
    private int assigned = 0;
    private int ID;
    private int number_solved = 0;

    public void insolve() {
        number_solved++;
    }

    public void resolve() {
        number_solved --;
    }

    public int getNumber_solved() {
        return number_solved;
    }
    
    public People() {
    }

    public People(String password, String name, int ID) {
        this.password = password;
        this.name = name;
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void assigned() {
        assigned++;
    }

    public void reduceassigned() {
        assigned--;
    }

    public String getName() {
        return name;
    }

}
