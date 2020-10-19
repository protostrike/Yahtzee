import org.junit.Assert;
import org.junit.Test;
import org.junit.Assert.*;

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
    enum scoreType {Ones, Twos, Threes, Fours, Fives, Sixes, ThreeOfAKind, FourOfAKind, FullHouse, SmallStraight, LargeStraight};
    public void testOnes(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Ones);
            sc.scoreOnes(dices);
            try{
                Assert.assertEquals(expectedScore, sc.Ones);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Ones\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.Ones);
            };
            sc.Ones = 0;
        }
        return;
    }
    public void testTwos(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Twos);
            sc.scoreTwos(dices);
            try{
                Assert.assertEquals(expectedScore, sc.Twos);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Twos\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.Twos);
            };
            sc.Twos = 0;
        }
        return;
    }
    public void testThrees(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Threes);
            sc.scoreThrees(dices);
            try{
                Assert.assertEquals(expectedScore, sc.Threes);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Threes\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.Threes);
            };
            sc.Ones = 0;
        }
        return;
    }
    public void testFours(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Fours);
            sc.scoreFours(dices);
            try{
                Assert.assertEquals(expectedScore, sc.Fours);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Fours\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.Fours);
            };
            sc.Ones = 0;
        }
        return;
    }
    public void testFives(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Fives);
            sc.scoreFives(dices);
            try{
                Assert.assertEquals(expectedScore, sc.Fives);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Fives\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.Fives);
            };
            sc.Ones = 0;
        }
        return;
    }
    public void testSixes(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Sixes);
            sc.scoreSixes(dices);
            try{
                Assert.assertEquals(expectedScore, sc.Sixes);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Sixes\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.Sixes);
            };
            sc.Sixes = 0;
        }
        return;
    }
    public void testThreeOfAKind(){

        return;
    }
    public void testFourOfAKind(){

        return;
    }
    public void testFullHouse(){

        return;
    }
    public void testSmallStraight(){

        return;
    }
    public void testLargeStraight(){

        return;
    }
    public void testYahtzee(){

        return;
    }
    public void testChance(){

        return;
    }
}
