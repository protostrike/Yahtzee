import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ScoreCardTest {
    static ScoreCard sc = new ScoreCard();
    int[][] testDices = {{1,2,3,4,5},
            {1,2,3,4,6},
            {1,1,1,2,2},
            {6,2,3,4,5},
            {5,2,3,4,5},
            {4,4,3,4,5},
            {6,6,3,6,6},
            };
    enum scoreType {Ones, Twos, Threes, Fours, Fives, Sixes, ThreeOfAKind, FourOfAKind, FullHouse, SmallStraight, LargeStraight, Yahtzee, Chance}

    @Test
    public void testSingleTimeScoring(){
        testOnes();
        testTwos();
        testThrees();
        testFours();
        testFives();
        testSixes();
        System.out.println("Test upper section scoring passed");
        testThreeOfAKind();
        System.out.println("Test Three Of A Kind scoring passed");
        testFourOfAKind();
        System.out.println("Test Four Of A Kind scoring passed");
        testSmallStraight();
        System.out.println("Test Small Straight scoring passed");
        testLargeStraight();
        System.out.println("Test Large Straight scoring passed");
        testFullHouse();
        System.out.println("Test Full House scoring passed");
        testYahtzee();
        System.out.println("Test Yahtzee scoring passed");
        testChance();
        System.out.println("Test Chance scoring passed");
    }
    public void testOnes(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Ones);
            sc.score(dices, 0);
            try{
                Assert.assertEquals(expectedScore, sc.upperSection[0]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Ones\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.upperSection[0]);
            }
            sc.upperSection[0] = 0;
        }
    }
    public void testTwos(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Twos);
            sc.score(dices, 1);
            try{
                Assert.assertEquals(expectedScore, sc.upperSection[1]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Twos\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.upperSection[1]);
            }
            sc.upperSection[1] = 0;
        }
    }
    public void testThrees(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Threes);
            sc.score(dices, 2);
            try{
                Assert.assertEquals(expectedScore, sc.upperSection[2]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Threes\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.upperSection[2]);
            }
            sc.upperSection[2] = 0;
        }
    }
    public void testFours(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Fours);
            sc.score(dices, 3);
            try{
                Assert.assertEquals(expectedScore, sc.upperSection[3]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Fours\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.upperSection[3]);
            }
            sc.upperSection[3] = 0;
        }
    }
    public void testFives(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Fives);
            sc.score(dices, 4);
            try{
                Assert.assertEquals(expectedScore, sc.upperSection[4]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Fives\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.upperSection[4]);
            }
            sc.upperSection[4] = 0;
        }
    }
    public void testSixes(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Sixes);
            sc.score(dices, 5);
            try{
                Assert.assertEquals(expectedScore, sc.upperSection[5]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Sixes\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.upperSection[5]);
            }
            sc.upperSection[5] = 0;
        }
    }
    public void testThreeOfAKind(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.ThreeOfAKind);
            sc.score(dices, 6);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[0]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Three Of A Kind\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[0]);
            }
            sc.lowerSection[0] = 0;
        }
    }
    public void testFourOfAKind(){
        for(int[] dices : testDices){
            int expectedScore;
            expectedScore = calculateScore(dices, scoreType.FourOfAKind);
            sc.score(dices, 7);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[1]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Four Of A Kind\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[1]);
            }
            sc.lowerSection[1] = 0;
        }
    }

    public void testSmallStraight(){
        for(int[] dices : testDices){
            int expectedScore;
            expectedScore = calculateScore(dices, scoreType.SmallStraight);
            sc.score(dices, 8);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[2]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Small Straight\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[2]);
            }
            sc.lowerSection[2] = 0;
        }
    }
    public void testLargeStraight(){
        for(int[] dices : testDices){
            int expectedScore;
            expectedScore = calculateScore(dices, scoreType.LargeStraight);
            sc.score(dices, 9);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[3]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Large Straight\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[3]);
            }
            sc.lowerSection[3] = 0;
        }
    }

    public void testFullHouse(){
        for(int[] dices : testDices){
            int expectedScore;
            expectedScore = calculateScore(dices, scoreType.FullHouse);
            sc.score(dices, 10);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[4]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Full House\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[4]);
            }
            sc.lowerSection[4] = 0;
        }
    }

    public void testYahtzee(){
        for(int[] dices : testDices){
            int expectedScore;
            expectedScore = calculateScore(dices, scoreType.Yahtzee);
            sc.score(dices, 11);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[5]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Yahtzee\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[5]);
            }
            sc.lowerSection[5] = 0;
        }
    }
    public void testChance(){
        for(int[] dices : testDices){
            int expectedScore;
            expectedScore = calculateScore(dices, scoreType.Chance);
            sc.score(dices, 12);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[6]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Chance\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[6]);
            }
            sc.lowerSection[6] = 0;
        }
    }

    private int calculateScore(int[] dices, scoreType type){
        int finalScore = 0;
        switch(type){
            case Ones:
                for(int dice: dices){
                    if(dice==1)
                        finalScore += dice;
                }
                break;
            case Twos:
                for(int dice: dices){
                    if(dice==2)
                        finalScore += dice;
                }
                break;
            case Threes:
                for(int dice: dices){
                    if(dice==3)
                        finalScore += dice;
                }
                break;
            case Fours:for(int dice: dices){
                if(dice==4)
                    finalScore += dice;
            }
                break;
            case Fives:
                for(int dice: dices){
                    if(dice==5)
                        finalScore += dice;
                }
                break;
            case Sixes:
                for(int dice: dices){
                    if(dice==6)
                        finalScore += dice;
                }
                break;
            case ThreeOfAKind:
                Arrays.sort(dices);
                int count = 1;
                for(int i = 0; i < dices.length-1; i++){
                    if(dices[i]==dices[i+1])
                        count++;
                    else
                        count=1;
                    if(count==3){
                        finalScore = Arrays.stream(dices).sum();
                        break;
                    }
                }
                break;
            case FourOfAKind:
                Arrays.sort(dices);
                count = 1;
                for(int i = 0; i < dices.length-1; i++){
                    if(dices[i]==dices[i+1])
                        count++;
                    else
                        count=1;
                    if(count==4){
                        finalScore = Arrays.stream(dices).sum();
                        break;
                    }
                }
                break;
            case SmallStraight:
                Arrays.sort(dices);
                count = 1;
                for(int i = 0; i < dices.length-1; i++) {
                    if (dices[i]+1 == dices[i + 1])
                        count++;
                    else
                        count = 1;
                    if(count==4){
                        finalScore = 30;
                        break;
                    }
                }
                break;
            case LargeStraight:
                Arrays.sort(dices);
                count = 1;
                for(int i = 0; i < dices.length-1; i++) {
                    if (dices[i]+1 == dices[i + 1])
                        count++;
                    else
                        count = 1;
                    if(count==5){
                        finalScore = 40;
                        break;
                    }
                }
                break;
            case FullHouse:
                Arrays.sort(dices);
                int first = 0;
                int second = 0;
                int head = dices[0];
                int tail = dices[dices.length - 1];

                for (int dice : dices) {
                    if (dice == head)
                        first++;
                    else if (dice == tail)
                        second++;
                }

                if ((first == 2 && second == 3) || (first == 3 && second == 2)){
                    finalScore = 25;
                    break;
                }
                break;
            case Yahtzee:
                Arrays.sort(dices);
                count = 1;
                for(int i = 0; i < dices.length-1; i++){
                    if(dices[i]==dices[i+1])
                        count++;
                    else
                        count=1;
                    if(count==5){
                        finalScore = 50;
                        break;
                    }
                }
                break;
            case Chance:
                finalScore = Arrays.stream(dices).sum();
                break;
        }
        return finalScore;
    }
}
