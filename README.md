# Welcome to Doge's BugsLife. 

This project is developed by Doge, Group 30.

Our group members include:
1) Pua Zhi Xian
2) Sam Zhuo Li
3) Darren Sow Zhu Jian
4) Tee Wei Lun


<br></br>
## How to Use Doge?
Run main program in SEMAG class to launch the program.
`
public class SEMAG{

  Window obj = new Window();

    public static void main(String[] args) throws InterruptedException {
        //load json
        SEMAG o = new SEMAG();
        o.obj.loadData();
        o.obj.ac();
        o.obj.saveData();
      .....
    }

}

`

##How to use the Chat Function?
Before using the chat function, you need to launch the server in multi-thread.
`
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(1234);
        Socket s;
        while (true) {
            s = ss.accept();
            .....
    }
}

`
After launching this server, you can run another SEMAG class instance,log in different account, and two accounts user can chat to each other.