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

    @Test
    public void testUpperScoring(){
        testOnes();
        testTwos();
        testThrees();
        testFours();
        testFives();
        testSixes();
    }
    public void testOnes(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Ones);
            sc.score(dices, 0);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[0]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Ones\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[0]);
            };
            sc.lowerSection[0] = 0;
        }
        return;
    }
    public void testTwos(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Twos);
            sc.score(dices, 1);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[1]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Twos\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[1]);
            };
            sc.lowerSection[1] = 0;
        }
        return;
    }
    public void testThrees(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Threes);
            sc.score(dices, 2);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[2]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Threes\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[2]);
            };
            sc.lowerSection[2] = 0;
        }
        return;
    }
    public void testFours(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Fours);
            sc.score(dices, 3);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[3]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Fours\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[3]);
            };
            sc.lowerSection[3] = 0;
        }
        return;
    }
    public void testFives(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Fives);
            sc.score(dices, 4);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[4]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Fives\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[4]);
            };
            sc.lowerSection[4] = 0;
        }
        return;
    }
    public void testSixes(){
        int expectedScore;
        for(int[] dices : testDices){
            expectedScore = calculateScore(dices, scoreType.Sixes);
            sc.score(dices, 5);
            try{
                Assert.assertEquals(expectedScore, sc.lowerSection[5]);
            }
            catch (AssertionError e){
                System.out.println("Error when testing scoring in Sixes\n"+
                        "Expected: " + expectedScore + "\n"+
                        "Actual: " + sc.lowerSection[5]);
            };
            sc.lowerSection[5] = 0;
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
        }
        return finalScore;
    }
}
