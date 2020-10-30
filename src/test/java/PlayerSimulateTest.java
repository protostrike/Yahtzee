import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PlayerSimulateTest {
    static Launcher lcOne = new Launcher(1);
    static Launcher lcTwo = new Launcher(2);

    @Test
    public void test(){
        testOnePlayer();
        testTwoPlayers();
    }

    public void testOnePlayer(){
        Player player = lcOne.players[0];

        int roundCount = 1;
        while(roundCount < 13){
            String str = "skip\n" + roundCount + "\n";
            System.out.println("Test String: \n" + str);
            addInput(str, lcOne);
            lcOne.playRound(player);
            roundCount++;
        }
        System.out.println("\nSimulation test ends, getting player's score card now");
        System.out.println(player.card);
    }

    public void testTwoPlayers(){
        Player P1 = lcTwo.players[0];
        Player P2 = lcTwo.players[1];

        int roundCount = 1;
        while(roundCount < 13){
            String strOne = "skip\n" + roundCount + "\n";
            String strTwo = "1 3 5\nskip\n" + roundCount + "\n";
            addInput(strOne,lcTwo);
            lcTwo.playRound(P1);
            addInput(strTwo, lcTwo);
            lcTwo.playRound(P2);
            roundCount++;
        }
        System.out.println("\nSimulation test ends, getting player's score card now");
        System.out.println("Player 1's card:");
        System.out.println(P1.card);
        System.out.println("Player 2's card:");
        System.out.println(P2.card);
    }

    private void addInput(String str, Launcher lc){
        InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        lc.r = new InputStreamReader(stream);
        lc.br = new BufferedReader(lc.r);
    }
}
