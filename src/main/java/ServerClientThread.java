import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerClientThread extends Thread {
    Server server;
    Socket serverClient;
    BufferedReader in;
    PrintWriter out;
    int id;

    public ServerClientThread(Socket s, Server server) {
        this.server = server;
        try {
            this.out = new PrintWriter(s.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serverClient = s;
    }

    public synchronized void send(String msg) {
        out.write(msg + "\n");
        out.flush();
    }

    public void start(){
        while(true){
            try {
                String msg = in.readLine();
                server.handleMessage(id, msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        try {
            out.close();
            in.close();
            serverClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPlayerId(){
        return id;
    }
}
