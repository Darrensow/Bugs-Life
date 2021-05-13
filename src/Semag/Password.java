/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semag;

import java.util.Random;

/**
 *
 * @author xianp
 */
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
