import org.junit.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class SimulationTest {
    Server s;
    Client c1;
    Client c2;
    Client c3;
    int port = 5000;

    @Test
    public void test(){
        testOnePlayer();
    }

    public void testOnePlayer(){
        s = new Server(5000);
        try {
            c1 = new Client(InetAddress.getLocalHost().getHostAddress(), 5000);
            //Start server
            Thread serverThread = new Thread(() -> {
                s.start();
            });
            serverThread.start();
            //Start Client
            Thread clientThread = new Thread(() -> {
                c1.start();
            });
            clientThread.start();

            //Tell server that the game is ready to go
            sendMessage("yes", c1);

            //
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //Send given message from given client
    private void sendMessage(String msg, Client c){
        c.out.write(msg);
        c.out.flush();
    }
}
