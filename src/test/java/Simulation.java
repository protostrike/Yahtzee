import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Simulation {
    static Launcher lc;

    @Given("start a launcher with {int} player(s)")
    public void startLauncher(int int1){
        lc = new Launcher(int1);
    }

    @Then("player(s) play the game")
    public void play(){
        int roundCount = 1;
        while(roundCount<=13) {
            for (Player p : lc.players) {
                String str = "skip\n" + roundCount + "\n";
                addInput(str, lc);
                lc.playRound(p);
            }
        }
    }

    @Then("game ends")
    public void gameEnds(){
        for(Player p : lc.players){
            System.out.println("\nSimulation test ends, getting player's score card now");
            System.out.println(p.card);
        }
    }

    private void addInput(String str, Launcher lc){
        InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        lc.r = new InputStreamReader(stream);
        lc.br = new BufferedReader(lc.r);
    }
}
