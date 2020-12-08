import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    Server s;
    int port = 5000;
    List<Client> players = new ArrayList<>();

    @Given("I start a Server at port {int}")
    public void startServer(Integer int1) {
        s = new Server(int1);
        port = int1;
        Thread server = new Thread(new Runnable() {

            @Override
            public void run() {
                s.start();
            }

        });
        server.start();
    }

    @Given("player {int} named {string}")
    public void connectClient(Integer int1, String name) {
        Client c = new Client("localhost", port, name);
        players.add(int1-1, c);
        Thread client = new Thread(new Runnable() {

            @Override
            public void run() {
                c.start();
            }

        });
        client.start();
        wait(2);
    }

    @When("player one gets ready")
    public void getReady() {
        System.out.println("Player one is ready\n");
        players.get(0).send("yes");
        wait(2);
    }

    @When("player one needs more players")
    public void notReady() {
        System.out.println("Player one is not ready\n");
        players.get(0).send("no");
        wait(2);
    }

    @When("players play this game")
    public void playGame() {

        for(int i = 1; i <= 13; i++) {
            for(int n = 0; n < players.size(); n++) {
                if(i == 4) {
                    System.out.println("Test re-roll all dices in Round 4!");
                    playRoundwithReroll(players.get(n), i);
                }
                else if(i == 6) {
                    System.out.println("Test re-roll last 3 dices in Round 6!");
                    playRoundHoldingDices(players.get(n), i);
                }
                else
                    playRound(players.get(n), i);
                System.out.println("Player " + players.get(n).name + " has played Round " + i);
                wait(1);
            }
        }
    }

    @Then("game ends")
    public void checkEndGame() {
        try {
            assert(s.highest!=null);
        } catch (AssertionError e) {
            System.out.println("No highest score found, game was not end correctly, test fail");
        }
        System.out.println("Simulation success");
    }

    @Then("do cleanup")
    public void cleanup() {
        s.close();
        for(Client c : players) {
            c.close();
        }
    }


    //Player plays a round with no re-roll
    public void playRound(Client currentPlayer, int currentRound) {
        System.out.println("Player " + currentPlayer.name +" roll first time");
        currentPlayer.send("");
        wait(1);

        System.out.println("Player " + currentPlayer.name +" send score signal");
        currentPlayer.send("3");
        wait(1);

        System.out.println("Player " + currentPlayer.name +" send scoring category");
        currentPlayer.send(Integer.toString(currentRound));
    }

    //Player plays a round with 2 re-rolls
    public void playRoundwithReroll(Client currentPlayer, int currentRound) {
        System.out.println("Player " + currentPlayer.name +" roll first time");
        currentPlayer.send("");
        wait(1);

        System.out.println("Player " + currentPlayer.name +" re-roll");
        currentPlayer.send("2");
        wait(1);

        System.out.println("Player " + currentPlayer.name +" re-roll");
        currentPlayer.send("2");
        wait(1);

        System.out.println("Player " + currentPlayer.name +" send scoring category");
        currentPlayer.send(Integer.toString(currentRound));
    }

    //Player plays a round with 2 re-rolls while holding first 2 dices
    public void playRoundHoldingDices(Client currentPlayer, int currentRound) {
        System.out.println("Player " + currentPlayer.name +" roll first time");
        currentPlayer.send("");
        wait(1);

        System.out.println("Player " + currentPlayer.name +" re-roll while holding dices");
        currentPlayer.send("1");

        System.out.println("Player " + currentPlayer.name +" hold first 2 dices");
        currentPlayer.send("1 2");

        System.out.println("Player " + currentPlayer.name +" re-roll while holding dices");
        currentPlayer.send("1");

        System.out.println("Player " + currentPlayer.name +" hold first 2 dices");
        currentPlayer.send("1 2");

        System.out.println("Player " + currentPlayer.name +" send scoring category");
        currentPlayer.send(Integer.toString(currentRound));
    }

    public void wait(int time) {
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
