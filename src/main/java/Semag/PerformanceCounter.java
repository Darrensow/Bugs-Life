package Semag;

public class PerformanceCounter {
    private String name;
    private Integer num_resolved;

    public PerformanceCounter() {
    }

    public PerformanceCounter(String name, int num_resolved) {
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
