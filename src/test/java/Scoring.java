import java.util.ArrayList;
import java.util.List;
import io.cucumber.java.en.*;

public class Scoring {

    static ScoreCard sc;

    @Given("I am a ScoreCard")
    public void InitializeScoreCard() {
        sc = new ScoreCard("Test");
    }


    @When("I score [{int}, {int}, {int}, {int}, {int}] in category {int}")
    public void Scoring(Integer int1, Integer int2, Integer int3, Integer int4, Integer int5, Integer int6) {
        List<Integer> dicesList = new ArrayList<>();
        dicesList.add(int1);
        dicesList.add(int2);
        dicesList.add(int3);
        dicesList.add(int4);
        dicesList.add(int5);
        int[] dices = new int[5];
        for(int i = 0; i < 5; i++) {
            dices[i] = dicesList.get(i);
        }
        System.out.println("Scoring [" + dices[0] + dices[1] + dices[2] + dices[3] + dices[4] +
                "] in category " + getCategoryName(int6));
        sc.score(dices, int6);
    }

    @Then("total score should be {int}")
    public void checkTotalScore(Integer int1) {
        System.out.println("Expected total score is: " + int1);
        System.out.println("Actual total score is: " + sc.calculateTotalScore());

        if(int1.equals(sc.calculateTotalScore()))
            System.out.println("Test pass\n");
        else {
            System.out.println("Test fail\n");
            assert(false);
        }
    }


    /*
     * Helper function to get category's name by given an index
     */
    public String getCategoryName(int i) {
        switch(i) {
            case 1:
                return "Ones";
            case 2:
                return "Twos";
            case 3:
                return "Threes";
            case 4:
                return "Fours";
            case 5:
                return "Fives";
            case 6:
                return "Sixes";
            case 7:
                return "Small Straight";
            case 8:
                return "Large Straight";
            case 9:
                return "Full House";
            case 10:
                return "Three of A Kind";
            case 11:
                return "Four of A Kind";
            case 12:
                return "Chance";
            case 13:
                return "Yahtzee";
            default:
                return null;
        }
    }

}
