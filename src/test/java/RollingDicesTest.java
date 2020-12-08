import org.junit.Test;

import static org.junit.Assert.*;

public class RollingDicesTest {

    //Testing rolling dices' functionality
    //Including rolling valid number
    //And holding specific dices


    Server s = new Server(5000);

    @Test
    public void RandomHoldingDicesTest() {
        System.out.println("The goal of this test is verifying functionality of holding dices\n"
                + "by generating two random positions to hold after rolling dices.\n"
                + "This test will repeat 10 times to sufficiently test randomness");

        int i = 1;
        while(i <= 10) {
            testHoldingDices(i);
            i++;
        }
    }


    public void testHoldingDices(int index) {
        System.out.println("Holding Dices Test #" + index);
        System.out.println("-----------------------------------");

        //Initial dices
        s.rollDice();

        int[] dices = s.dices;
        String msg = "Dices are:                 ";
        for(int i : dices) {
            msg += i + " ";
        }
        System.out.println(msg);

        //Pick two random number to reroll
        int one = (int)(Math.ceil(Math.random()*5));
        int two = (int)(Math.ceil(Math.random()*5));
        while(two == one) {
            two = (int)(Math.ceil(Math.random()*5));
        }
        System.out.println("The two positions that will be hold are: " + one + " " + two);

        s.rollDice(one + " " + two);
        int[] dicesAfterReroll = s.dices;
        msg = "Dices after rerolling are: ";
        for(int i : dices) {
            msg += i + " ";
        }
        System.out.println(msg);

        //Verify those dices are hold correctly
        assertEquals(dicesAfterReroll[one-1], dices[one-1]);
        assertEquals(dicesAfterReroll[two-1], dices[two-1]);

    }

}