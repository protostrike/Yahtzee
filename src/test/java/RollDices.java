
import io.cucumber.java.en.*;
import org.junit.Assert;

public class RollDices {
    Server s;
    int[] dices = new int[5];

    @Given("I am a Server at port {int}")
    public void initializeServer(Integer int1) {
        s = new Server(int1);
    }

    @When("I roll dices")
    public void rollDices(){
        s.rollDice();
        dices = s.dices;
    }

    @When("I want to hold {int} dices and re-roll")
    public void rerollDices(Integer int1){
        System.out.println("Holding " + int1 + " dice(s)");
        String msg = "";
        if(int1 == 0) {
            s.rollDice();
            return;
        }
        for(int i = 1; i <= int1; i++) {
            msg += i;
            msg += " ";
        }
        s.rollDice(msg);
    }

    @Then("first {int} dices should be unchanged")
    public void checkDices(Integer int1) {
        for(int i = 0; i < dices.length; i++) {
            assert(dices[i] == s.dices[i]);
        }

        //After loop, everything should be passing at this point
        System.out.println("Dice(s) unchanged in this check\n");
        dices = s.dices;
    }

    @Then("I reached re-roll limit")
    public void checkRerollCounter() {
        assert(s.rerollCounter >= 3);
        System.out.println("Re-roll limit reached, test pass\n");
    }

    @Then("clear Server")
    public void clearServer() {
        s.close();
    }
}