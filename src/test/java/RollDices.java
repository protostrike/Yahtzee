
import io.cucumber.java.en.*;

public class RollDices {
    Player player;
    int[] firstDices = new int[5];
    int[] secondDices = new int[5];

    @Given("I am a player")
    public void initializePlayer() {
        player = new Player();
    }

    @When("I roll dices")
    public void rollDices(){
        player.rollDice();
        firstDices = player.dices;
    }

    @When("I want to hold {int} dices and re-roll")
    public void rerollDices(Integer int1){
        System.out.println("Holding " + int1 + " dice(s)");
        String msg = "";
        if(int1 == 0) {
            player.rollDice();
            return;
        }
        for(int i = 1; i <= int1; i++) {
            msg += i;
            msg += " ";
        }
        player.rollDice(msg);
        secondDices = player.dices;
    }

    @Then("first {int} dices should be unchanged")
    public void checkDices(Integer int1) {
        for(int i = 0; i < 5; i++) {
            assert(firstDices[i] == secondDices[i]);
        }

        //After loop, everything should be passing at this point
        System.out.println("Dice(s) unchanged in this check\n");
    }

    @Then("I check after re-roll")
    public void checkRerollCounter() {
        System.out.println("First set of dices: " + firstDices);
        System.out.println("Dices after re-roll: " + secondDices);
    }

}