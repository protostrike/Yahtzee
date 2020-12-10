import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ClientStep {

    static Server s;
    static Client c1;
    static Client c2;
    static Client c3;

    Thread serverThread;
    File log = new File("./log.txt").getAbsoluteFile();
    final CucumberTestContext testContext;


    public ClientStep(CucumberTestContext testContext){
        this.testContext = testContext;
    }

    @Given("A server starts at port {int}")
    public void InitializeServer(Integer int1){
        if(log.getAbsoluteFile().exists())
            log.delete();
        Server s = new Server(int1);
        ClientStep.s = s;
        serverThread = new Thread(s::start);
        serverThread.start();
    }

    @Given("Client {int} wants to connect to port {int}")
    public void initClient(Integer int1, Integer int2){
        //Wait for server start
        System.out.println("client thread starts");
        while(!containLog("Server start")){
            System.out.println("Wait for server");
            sleep(100);
        }
        Client c = new Client(int2);
        setClient(int1, c);
        c.start();
        System.out.println("Client initialized");
    }

    @Then("Client {int} is ready to play")
    public void sendReady(Integer int1){
        while(!getClient(int1).up)
            sleep(100);
        System.out.println("Client send ready");
        sendMessage(int1, "ready");
    }

    @Then("Client waits until game starts")
    public void waitForGameStart(){
        while(!containLog("Game start")){
            sleep(100);
        }
    }

    @Then("Client {int} wants to score dices[{int}, {int}, {int}, {int}, {int}] to Category {int}")
    public void score(Integer int1, Integer int2, Integer int3, Integer int4, Integer int5, Integer int6, Integer int7){
        int[] dices = new int[5];
        dices[0] = int2;
        dices[1] = int3;
        dices[2] = int4;
        dices[3] = int5;
        dices[4] = int6;
        String msg = "Category: " + int7 + ", " + "Dices: " + Arrays.toString(dices);
        sendMessage(int1, msg);
        System.out.println("Client " + int1 + " scored dices[" + int2 + int3 + int4 + int5 + int6 + "] in category " + int7);
    }

    @Then("Check score {int} in category {int}")
    public void checkAfterScore(Integer int1, Integer int2){
        Assert.assertTrue(containLog("Category index " + (int2-1) + " is scored"));
        Assert.assertEquals(int1.intValue(), s.card.getScoreByCategory(int2-1));
    }

    @Then("Check update on category {int} from client {int}")
    public void checkUpdate(Integer int1, Integer int2){
        Assert.assertTrue(containLog("Update -- " + int1 + ", Client: " + getClient(int2).id));
    }

    @Then("Check reset triggered by client {int}")
    public void checkReset(Integer int1){
        Assert.assertTrue(containLog("Reset, a score is made by Client " + getClient(int1).id));
    }

    @Then("Wait for {int} second\\(s)")
    public void wait(Integer int1){
        sleep(int1*1000);
    }

    //@After
    public void cleanUp(){
        testContext.close();
    }

    //Send message from client with given position in array
    private void sendMessage(Integer pos, String msg){
        getClient(pos).send(msg);
    }

    //Check if log file contains certain message
    private boolean containLog(String msg){
        //Wait until log file is created
        while(!log.exists()){
            System.out.println("Waiting for log file");
            sleep(100);
        }
        int counter = 1;
        while(counter < 5) {
            System.out.println("Checking log");
            try {
                Scanner scr = new Scanner(log);
                while (scr.hasNextLine()) {
                    String line = scr.nextLine();
                    if (line.contains(msg))
                        return true;
                }
                scr.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Did not find log message, check again later");
            sleep(1000);
            counter++;
        }
        System.out.println("Log checking timed out, no log message '" + msg + "' found");
        return false;
    }

    private void sleep(long msec){
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Client getClient(int pos){
        switch(pos){
            case 1:
                return c1;
            case 2:
                return c2;
            case 3:
                return c3;
            default:
                return null;
        }
    }

    public void setClient(int pos, Client c){
        switch (pos){
            case 1:
                c1 = c;
                break;
            case 2:
                c2 = c;
                break;
            case 3:
                c3 = c;
                break;
            default:
                break;
        }
    }

}
