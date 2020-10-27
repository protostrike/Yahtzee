import org.junit.Assert;
import org.junit.Test;

public class PlayerTest {
    static Player player = new Player();
    String[] testHoldingString = {"1", "1 2 3", "1 3 5", "1 2 3 4 5"};
    @Test
    public void test(){
        Assert.fail("Not implemented yet");
    }

    private void testHoldingDices(){
        player.rollDice();
        for(String test : testHoldingString) {
            player.rollDice(test);
            checkHoldingDices();
        }
    }

    private void checkHoldingDices(){

    }
}
