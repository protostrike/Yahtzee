import org.junit.Assert;
import org.junit.Test;

public class PlayerSimulateTest {
    static Launcher lcOne = new Launcher(1);
    static Launcher lcTwo = new Launcher(2);

    @Test
    public void test(){
        Assert.fail("Not implemented yet");
    }

    public void testOnePlayer(){
        Player player = lcOne.players[0];
        int roundCount = 1;
        while(roundCount < 13){
            lcOne.playRound(player);
            roundCount++;
        }
    }

    public void testTwoPlayers(){
        Player P1 = lcTwo.players[0];
        Player P2 = lcTwo.players[1];
        int roundCount = 1;
        while(roundCount < 13){
            lcTwo.playRound(P1);
            lcTwo.playRound(P2);
            roundCount++;
        }
    }
}
