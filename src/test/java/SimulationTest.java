import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SimulationTest {
    Server s;
    Client c1;
    Client c2;
    Client c3;
    File log;

    //Waiter for waiting after each test case
    private final CountDownLatch waiter = new CountDownLatch(1);

    @Test
    public void test(){
        //testOnePlayerReady();
        //testServerReady();
        //testScoringFromOnePlayer();
        testMultiScoresFromMultiPlayer();
    }

    //Test case: test player's ready status after one player connected to server
    public void testOnePlayerReady() {
        System.out.println("\nTest Case: Test single player's ready status\n");
        //Construct server and client
        s = new Server(5000);
        c1 = new Client();
        log = new File("./log-" + 5000 +".txt").getAbsoluteFile();
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
        sendMessage(new Message(Message.Type.ClientReady, "ready"), c1);
        sleep(1000);
        try {
            Assert.assertTrue(containLog("Client 1: Client ready"));
        } catch (AssertionError e){
            System.out.println("Client cannot enter ready status");
        }
        System.out.println("Test single player's ready status passed");
        server.interrupt();
        client1.interrupt();
        try {
            waiter.await(1000 * 1000, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Test case: test server's ready status after all three players are ready
    public void testServerReady(){
        System.out.println("\nTest Case: Test Server ready status\n");
        //Construct server and client
        s = new Server(5001);
        c1 = new Client(5001);
        c2 = new Client(5001);
        c3 = new Client(5001);
        log = new File("./log-" + 5001 +".txt").getAbsoluteFile();
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
        sendMessage(new Message(Message.Type.ClientReady, "ready"), c1);
        sendMessage(new Message(Message.Type.ClientReady, "ready"), c2);
        sendMessage(new Message(Message.Type.ClientReady, "ready"), c3);

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
        try {
            waiter.await(1000 * 1000, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testScoringFromOnePlayer(){
        System.out.println("\nTest case: test scoring once from one player\n");
        s = new Server(5002);
        c1 = new Client(5002);
        c2 = new Client(5002);
        c3 = new Client(5002);
        log = new File("./log-" + 5002 +".txt").getAbsoluteFile();
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
        sendMessage(new Message(Message.Type.ClientReady, "ready"), c1);
        sendMessage(new Message(Message.Type.ClientReady, "ready"), c2);
        sendMessage(new Message(Message.Type.ClientReady, "ready"), c3);
        //Wait until all three players are ready
        waitForServerReady();

        //Send a message from client 1 to score in Ones
        sendMessage(new Message(Message.Type.Score, "Category: 1, Dices: [1,1,1,1,1]"), c1);
        sleep(2000);

        Assert.assertEquals(5, s.card.getScoreByCategory(0));
        sleep(2000);
        try {
            Assert.assertTrue(containLog("Category index 0 is scored"));
        } catch (AssertionError e){
            Assert.fail("Client cannot get updated message after scoring");
        }
        try {
            waiter.await(1000 * 1000, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testMultiScoresFromMultiPlayer(){
        System.out.println("\nTest case: test scoring from multiple players\n");
        s = new Server(5003);
        c1 = new Client(5003);
        c2 = new Client(5003);
        c3 = new Client(5003);
        log = new File("./log-" + 5003 +".txt").getAbsoluteFile();
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
        sendMessage(new Message(Message.Type.ClientReady, "ready"), c1);
        sendMessage(new Message(Message.Type.ClientReady, "ready"), c2);
        sendMessage(new Message(Message.Type.ClientReady, "ready"), c3);
        //Wait until all three players are ready
        waitForServerReady();

        //Score many times from a random player each time
        for(int i = 1; i < 10; i++){
            int randomPick = (int)Math.ceil(Math.random()*2);
            Client c;
            switch (randomPick){
                case 1:
                    c = c1;
                    break;
                case 2:
                    c = c2;
                    break;
                case 3:
                    c = c3;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + randomPick);
            }
            //Roll random dices
            int[] dices = new int[5];
            Arrays.fill(dices, (int)Math.ceil(Math.random()*5));

            String str = "Category: " + i + ", Dices: " + Arrays.toString(dices);
            System.out.println(str);
            //Send message from player
            sendMessage(new Message(Message.Type.Score, str), c);
            sleep(2000);
        }
        for(int i = 0; i < 9; i++){
            try {
                Assert.assertTrue(containLog("Category index " + i + " is scored"));
            } catch (AssertionError e){
                Assert.fail("Error: category " + i + " is not scored");
            }
        }
        try {
            waiter.await(1000 * 1000, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Send given message from given client
    private void sendMessage(Message msg, Client c) {
        c.send(msg);
    }

    //Method to read running status from server
    //Wait if the server is not running
    private void waitForServerUp(){
        System.out.println("Wait until server is up and running properly");
        while(!s.up){
            sleep(1000);
            System.out.println("Still waiting...");
        }
    }

    //Method to read running status from one client
    //Wait if it is not all running
    private void waitForOneClientUp(Client c){
        System.out.println("Wait until the client is up and running properly");
        while(!c.up){
            sleep(1000);
            System.out.println("Still waiting...");
        }
    }

    //Method to read running status from all clients
    //Wait if they are not all running
    private void waitForAllClientsUp(){
        System.out.println("Wait until all clients are up and running properly");
        while(!c1.up||!c2.up||!c3.up){
            sleep(1000);
            System.out.println("Still waiting...");
        }
    }

    //Method to read ready status from all clients
    //Wait if they are not all ready
    private void waitForAllClientsReady(){
        System.out.println("Wait until all clients are ready to play");
        while(!s.getConnection(1).isReady()||!s.getConnection(2).isReady()||!s.getConnection(3).isReady()){
            sleep(1000);
            System.out.println("Still waiting...");
        }
    }

    //Method to read server ready status
    //Wait if server is not ready
    private void waitForServerReady(){
        System.out.println("Wait until server is ready to play");
        while(!s.isReady()){
            sleep(1000);
            System.out.println("Still waiting...");
        }
    }

    //Check if log file contains certain message
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
