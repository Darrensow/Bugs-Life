package Semag;

public class People {

    private int password;
    private String name;
    private int assigned = 0;
    private int ID;

    public People() {
    }

    public People(int password, String name, int ID) {
        this.password = password;
        this.name = name;
        this.ID=ID;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void assigned() {
        assigned++;
    }
    public  void reduceassigned(){
        assigned--;
    }

    public String getName() {
        return name;
    }
    

}
