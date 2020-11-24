import org.junit.Assert;
import org.junit.Test;

public class SimulationTest {
    Server s;
    Client c1;
    Client c2;
    Client c3;

    @Test
    public void test() {
        testOnePlayerReady();
        sleep(1000);
        testServerReady();
    }


    //Test case: test player's ready status after one player connected to server
    public void testOnePlayerReady() {
        System.out.println("\nTest Case: Test Player Ready\n");
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(s.getConnection(1).isReady());
        System.out.println("Test passed");
        server.interrupt();
        client1.interrupt();
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

        Assert.assertTrue(s.isReady());
        System.out.println("Test passed");

        //Shut down thread after test
        server.interrupt();
        client1.interrupt();
        client2.interrupt();
        client3.interrupt();
    }

    public void testScoringFromOnePlayer(){
        s = new Server(5002);
        c1 = new Client(5002);
        c2 = new Client(5002);
        c3 = new Client(5002);

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

        //Send a message from client 1 to score in Ones
        sendMessage("Category: 1, Dices: [1,1,1,1,1]", c1);
        sleep(2000);

        Assert.assertEquals(s.card.getScoreByCategory(1), 5);
        Assert.assertFalse(c1.scorableCategory[0]);
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Still waiting...");
        }
    }

    private void waitForOneClientUp(Client c){
        System.out.println("Wait until the client is up and running properly");
        while(!c.up){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Still waiting...");
        }
    }

    private void waitForAllClientsUp(){
        System.out.println("Wait until all clients are up and running properly");
        while(!c1.up||!c2.up||!c3.up){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Still waiting...");
        }
    }

    private void waitForAllClientsReady(){
        System.out.println("Wait until all clients are ready to play");
        while(!s.getConnection(1).isReady()||!s.getConnection(2).isReady()||!s.getConnection(3).isReady()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Still waiting...");
        }
    }
}
