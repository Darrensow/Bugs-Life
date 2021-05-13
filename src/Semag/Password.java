package Semag;

import java.util.Random;

public class Password {

    private int pass;
    Random r = new Random();

    public Password() {
        pass = r.nextInt(10000);
    }

    public int getPass() {
        return pass;
    }

}
