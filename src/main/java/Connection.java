import java.io.*;
import java.net.Socket;
import java.time.LocalTime;

public class Connection {
    ObjectInputStream in;
    ObjectOutputStream out;
    int id;
    boolean ready = false;
    int port;
    private File log;
    private FileWriter logWriter;

    public Connection(Socket s, int id, int port) {
        try {
            this.port = port;
            this.out = new ObjectOutputStream(s.getOutputStream());
            this.in = new ObjectInputStream(s.getInputStream());
            log = new File("./log-" + port +".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.id = id;
        try {
            logWriter = new FileWriter(log.getAbsoluteFile(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
