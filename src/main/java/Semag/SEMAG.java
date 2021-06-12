package Semag;

import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.SpellChecker;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SEMAG{

  Window obj = new Window();

    public static void main(String[] args) throws InterruptedException {
        //load json
        SEMAG o = new SEMAG();
        o.obj.loadData();
        o.obj.ac();
        o.obj.saveData();
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    o.obj.saveData();
                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        thread.start();
    }

}
