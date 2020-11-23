import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
    Socket serverClient;
    BufferedReader in;
    PrintWriter out;
    int id;
    boolean ready = false;

    public Connection(Socket s, int id) {
        try {
            this.out = new PrintWriter(s.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serverClient = s;
        this.id = id;
    }

    public synchronized void send(String msg) {
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
    }
}
