import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ServerClientStepDefinition {

    Thread serverThread;
    File log = new File("./log.txt").getAbsoluteFile();
    final CucumberTestContext testContext;


    public ServerClientStepDefinition(CucumberTestContext testContext){
        this.testContext = testContext;
    }

    @Given("A server starts at port {int}")
    public void InitializeServer(Integer int1){
        Server s = new Server(int1);
        testContext.setServer(s);
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
        testContext.setClient(int1, c);
        c.start();
        System.out.println("Client initialized");
    }

    @Then("Client {int} is ready to play")
    public void sendReady(Integer int1){
        while(!testContext.getClient(int1).up)
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
    }

    @Then("Wait and check score {int} in category {int}")
    public void checkAfterScore(Integer int1, Integer int2){
        sleep(1000);
        Assert.assertEquals(testContext.getServer().card.getScoreByCategory(int2-1), int1.intValue());
        Assert.assertTrue(containLog("Category index " + (int2-1) + " is scored"));
    }

    @Then("Wait and check update on category {int} from client {int}")
    public void checkUpdate(Integer int1, Integer int2){
        sleep(1000);
        Assert.assertTrue(containLog("Update -- " + (int1-1) + ", Client: " + testContext.getClient(int2).id));
    }

    @Then("Wait and check reset triggered by client {int}")
    public void checkReset(Integer int1){
        sleep(1000);
        Assert.assertTrue(containLog("Reset, a score is made by Client " + testContext.getClient(int1).id));
    }

    //Send message from client with given position in array
    private void sendMessage(Integer pos, String msg){
        testContext.getClient(pos).send(msg);
    }

    //Check if log file contains certain message
    private boolean containLog(String msg){
        //Wait until log file is created
        while(!log.exists()){
            System.out.println("Waiting for log file");
            sleep(100);
        }
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

    private void sleep(long msec){
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
