import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PlayerTest {
    static Player player = new Player();
    String[] testHoldingString = {"1", "1 2 3", "1 3 5", "1 3 5 2", "1 2 3 4 5"};
    @Test
    public void test(){
        testHoldingDices();
    }

    private void testHoldingDices(){
        player.rollDice();
        for(String test : testHoldingString) {
            int[] dicesBeforeReroll = player.dices;
            String[] str = test.split(" ");
            int[] holdingList = new int[str.length];
            for(int i = 0; i < str.length; i++) {
                holdingList[i] = Integer.parseInt(str[i]) - 1;
            }
            Arrays.sort(holdingList);

            player.rollDice(test);
            for(int holdingPosition: holdingList){
                Assert.assertEquals(player.dices[holdingPosition], dicesBeforeReroll[holdingPosition]);
            }
        }
    }

}
