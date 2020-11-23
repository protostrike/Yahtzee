import org.junit.Assert;
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
    public void test() {
        testOnePlayerReady();
        testServerReady();
    }


    //Test case: test player's ready status after one player connected to server
    public void testOnePlayerReady() {
        System.out.println("\nTest Case: Test Player Ready\n");
        //Construct server and client
        s = new Server(5000);
        c1 = new Client();

        //Start server
        new Thread(() -> s.start()).start();

        //Start Client
        new Thread(() -> c1.start()).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Send ready message from client 1 to server
        sendMessage("ready", c1);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(s.getConnection(1).isReady());
        System.out.println("Test passed");
        s.close();
        c1.close();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Test case: test server's ready status after all three players are ready
    public void testServerReady(){
        System.out.println("\nTest Case: Test Server Ready\n");
        //Construct server and client
        s = new Server(5001);
        c1 = new Client(5001);
        c2 = new Client(5001);
        c3 = new Client(5001);

        //Start server
        new Thread(() -> s.start()).start();
        //Start client
        new Thread(() -> c1.start()).start();
        new Thread(() -> c2.start()).start();
        new Thread(() -> c3.start()).start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Send ready messages from all three clients
        sendMessage("ready", c1);
        sendMessage("ready", c2);
        sendMessage("ready", c3);

        Assert.assertTrue(s.ready);
        System.out.println("Test passed");
        s.close();
        c1.close();
        c2.close();
        c3.close();
    }

    //Send given message from given client
    private void sendMessage(String msg, Client c) {
        c.send(msg);
    }
}
