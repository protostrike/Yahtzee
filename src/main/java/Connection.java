import java.io.*;
import java.net.Socket;
import java.time.LocalTime;

public class Connection {
    Socket serverClient;
    BufferedReader in;
    PrintWriter out;
    int id;
    boolean ready = false;

    File log = new File("./log.txt");
    FileWriter logWriter;

    public Connection(Socket s, int id) {
        try {
            this.out = new PrintWriter(s.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serverClient = s;
        this.id = id;
        try {
            logWriter = new FileWriter(log.getAbsoluteFile(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        System.out.println("Send to Client:" + msg);
        out.write(msg + "\n");
        out.flush();
    }

    public int getPlayerId(){
        return id;
    }

    public boolean isReady(){
        return ready;
    }

    public void setReady(boolean ready){
        this.ready = ready;
        logging("Client ready");
    }

    public void close(){
        try {
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logging(String str){
        try {
            str = "[" + LocalTime.now() + "]Client " + id +": "+ str + "\n";
            logWriter.write(str);
            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
