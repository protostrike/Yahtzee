import java.util.Arrays;

public class ScoreCard {

    /*Upper section
        index 0: Ones
        index 1: Twos
        index 2: Threes
        index 3: Fours
        index 4: Fives
        index 5: Sixes
     */
    int[] upperSection;

    int[] lowerSection;

    public ScoreCard(){
        //Initial all score to -1 (-1 means no score recorded yet)
        upperSection = new int[]{-1, -1, -1, -1, -1, -1};
        lowerSection = new int[]{-1, -1, -1, -1, -1, -1, -1};
    }

    public void score(int[] dices, int category){
        if(category<=5)
            scoreUppers(dices, category);
        else if(category==6)
            scoreThreeOfAKind(dices);
        else if(category==7)
            scoreFourOfAKind(dices);
        else if(category==8)
            scoreSmallStraight(dices);
        else if(category==9)
            scoreLargeStraight(dices);
        else if(category==10)
            scoreFullHouse(dices);
        else if(category==11)
            scoreYahtzee(dices);
        else if(category==12)
            scoreChance(dices);
    }

    private void scoreUppers(int[] dices, int category){
        for(int dice : dices){
            if(dice==category+1){
                if(upperSection[category]==-1) {
                    //If no score is in yet, make the first dice as the initial score
                    upperSection[category] = dice;
                    continue;
                }
                upperSection[category] += dice;
            }
        }
        if(upperSection[category] == -1){
            //If no score is in, make it a 0
            upperSection[category] = 0;
        }
    }

    private void scoreThreeOfAKind(int[] dices){
        Arrays.sort(dices);
        int counter = 1;
        for(int i = 0; i < dices.length-1; i++) {
            if(dices[i+1] == dices[i])
                counter++;
            else
                counter = 1;
            if(counter == 3) {
                lowerSection[0] = Arrays.stream(dices).sum();
                return;
            }
        }
        lowerSection[0] = 0;
    }

    private void scoreFourOfAKind(int[] dices){
        Arrays.sort(dices);
        int counter = 1;
        for(int i = 0; i < dices.length-1; i++) {
            if(dices[i+1] == dices[i])
                counter++;
            else
                counter = 1;
            if(counter == 4) {
                lowerSection[1] = Arrays.stream(dices).sum();
                return;
            }
        }
        lowerSection[1] = 0;
    }

    private void scoreSmallStraight(int[] dices){
        Arrays.sort(dices);
        int counter = 1;

        for(int i = 0; i < dices.length-1; i++) {
            if(dices[i+1] == dices[i]+1)
                counter++;
            else if(dices[i+1] == dices[i])
                continue;
            else
                counter = 1;
            if(counter == 4) {
                lowerSection[2] = 30;
                return;
            }
        }
        lowerSection[2] = 0;
    }

    private void scoreLargeStraight(int[] dices){
        Arrays.sort(dices);
        int counter = 1;

        for(int i = 0; i < dices.length-1; i++) {
            if(dices[i+1] == dices[i]+1)
                counter++;
            else if(dices[i+1] == dices[i])
                continue;
            else
                counter = 1;
            if(counter == 5) {
                lowerSection[3] = 40;
                return;
            }
        }
        lowerSection[3] = 0;
    }

    private void scoreFullHouse(int[] dices){
        Arrays.sort(dices);
        int first = 0;
        int second = 0;
        int head = dices[0];
        int tail = dices[dices.length-1];

        for (int dice : dices) {
            if (dice == head)
                first++;
            else if (dice == tail)
                second++;
        }

        if((first == 2 && second == 3) || (first == 3 && second == 2)) {
            lowerSection[4] = 25;
            return;
        }
        lowerSection[4] = 0;
    }

    private void scoreYahtzee(int[] dices){
        Arrays.sort(dices);
        int counter = 1;
        for(int i = 0; i < dices.length-1; i++) {
            if(dices[i+1] == dices[i])
                counter++;
            else
                counter = 1;
            if(counter == 5) {
                lowerSection[5] = 50;
                return;
            }
        }
        lowerSection[5] = 0;
    }

    private void scoreChance(int[] dices){
        lowerSection[6] = Arrays.stream(dices).sum();
    }

    public int calculateTotalScore(){
        int total = 0;
        for(int score:lowerSection){
            if(score!=-1)
                total += score;
        }
        for(int score:upperSection){
            if(score!=-1)
                total += score;
        }

        return total;
    }

    public int getScoreByCategory(int category){
        if(category<=5)
            return upperSection[category];
        else
            return lowerSection[category-6];
    }
}
