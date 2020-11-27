import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SimulationTest {
    Server s;
    Client c1;
    Client c2;
    Client c3;
    File log = new File("./log.txt").getAbsoluteFile();

    @Test
    public void test() {
        testOnePlayerReady();
        sleep(1000);
        testServerReady();
        sleep(1000);
        testScoringFromOnePlayer();
    }

    //Test case: test player's ready status after one player connected to server
    public void testOnePlayerReady() {
        System.out.println("\nTest Case: Test one player ready status\n");
        //Construct server and client
        s = new Server(5000);
        c1 = new Client();

        //Start server
        Thread server = new Thread(() -> s.start());
        server.start();

        //Start Client
        Thread client1 = new Thread(() -> c1.start());

        waitForServerUp();
        client1.start();

        //Wait until client is up and running
        waitForOneClientUp(c1);

        //Send ready message from client 1 to server
        sendMessage("ready", c1);
        sleep(1000);
        try {
            Assert.assertTrue(containLog("Client 1: Client ready"));
        } catch (AssertionError e){
            System.out.println("Client cannot enter ready status");
        }
        System.out.println("Test passed");
        server.interrupt();
        client1.interrupt();
    }

    //Test case: test server's ready status after all three players are ready
    public void testServerReady(){
        System.out.println("\nTest Case: Test Server ready status\n");
        //Construct server and client
        s = new Server(5001);
        c1 = new Client(5001);
        c2 = new Client(5001);
        c3 = new Client(5001);

        //Start server
        Thread server = new Thread(() -> s.start());
        server.start();

        //Start client
        Thread client1 = new Thread(() -> c1.start());
        Thread client2 = new Thread(() -> c2.start());
        Thread client3 = new Thread(() -> c3.start());

        waitForServerUp();
        client1.start();
        client2.start();
        client3.start();

        waitForAllClientsUp();

        //Send ready messages from all three clients
        sendMessage("ready", c1);
        sendMessage("ready", c2);
        sendMessage("ready", c3);

        //Wait until all three players are ready
        waitForAllClientsReady();
        sleep(2000);
        try {
            Assert.assertTrue(containLog("Game start"));
        } catch (AssertionError e){
            System.out.println("Server cannot enter ready status after all clients are ready");
        }
        System.out.println("Test Server ready status passed");

        //Shut down thread after test
        server.interrupt();
        client1.interrupt();
        client2.interrupt();
        client3.interrupt();
    }

    public void testScoringFromOnePlayer(){
        System.out.println("\nTest case: test scoring once from one player\n");
        s = new Server(5002);
        c1 = new Client(5002, true);
        c2 = new Client(5002, true);
        c3 = new Client(5002, true);

        //Start server
        Thread server = new Thread(() -> s.start());
        server.start();

        //Start client
        Thread client1 = new Thread(() -> c1.start());
        Thread client2 = new Thread(() -> c2.start());
        Thread client3 = new Thread(() -> c3.start());

        waitForServerUp();
        client1.start();
        client2.start();
        client3.start();

        waitForAllClientsUp();

        //Send ready messages from all three clients
        sendMessage("ready", c1);
        sendMessage("ready", c2);
        sendMessage("ready", c3);
        //Wait until all three players are ready
        waitForServerReady();

        //Send a message from client 1 to score in Ones
        sendMessage("Category: 1, Dices: [1,1,1,1,1]", c1);
        sleep(2000);

        Assert.assertEquals(5, s.card.getScoreByCategory(0));
        sleep(2000);
        try {
            Assert.assertTrue(containLog("Category index 0 is scored"));
        } catch (AssertionError e){
            System.out.println("Client cannot get updated message after scoring");
        }
    }

    public void testMultiScoresFromMultiPlayer(){

    }

    private void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Send given message from given client
    private void sendMessage(String msg, Client c) {
        c.send(msg);
    }

    private void waitForServerUp(){
        System.out.println("Wait until server is up and running properly");
        while(!s.up){
            sleep(1000);
            System.out.println("Still waiting...");
        }
    }

    private void waitForOneClientUp(Client c){
        System.out.println("Wait until the client is up and running properly");
        while(!c.up){
            sleep(1000);
            System.out.println("Still waiting...");
        }
    }

    private void waitForAllClientsUp(){
        System.out.println("Wait until all clients are up and running properly");
        while(!c1.up||!c2.up||!c3.up){
            sleep(1000);
            System.out.println("Still waiting...");
        }
    }

    private void waitForAllClientsReady(){
        System.out.println("Wait until all clients are ready to play");
        while(!s.getConnection(1).isReady()||!s.getConnection(2).isReady()||!s.getConnection(3).isReady()){
            sleep(1000);
            System.out.println("Still waiting...");
        }
    }

    private void waitForServerReady(){
        System.out.println("Wait until server is ready to play");
        while(!s.isReady()){
            sleep(1000);
            System.out.println("Still waiting...");
        }
    }

    private boolean containLog(String msg){
        try {
            Scanner scr = new Scanner(log);
            while(scr.hasNextLine()){
                String line = scr.nextLine();
                if(line.contains(msg))
                    return true;
            }
            scr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
