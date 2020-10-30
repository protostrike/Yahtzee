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
    int upperBonus = 0;
    int yahtzeeBonus = 0;
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
        if(checkYahtzeeBonus(dices)==true)
            yahtzeeBonus += 100;
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
        calculateUpperBonus();
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

    private void calculateUpperBonus(){
        int upperTotal = 0;
        for(int i = 0; i < upperSection.length; i++){
            upperTotal += getScoreByCategory(i);
        }
        if(upperTotal >= 63)
            upperBonus = 35;
    }

    private boolean checkYahtzeeBonus(int[] dices){
        for(int i = 0; i < dices.length-1; i++) {
            if(dices[i+1] != dices[i])
                return false;
        }
        return true;
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
            return lowerSection[category-1];
    }

    public String toString() {
        StringBuilder dashes = new StringBuilder();
        for(int i = 0; i< 120; i++)
            dashes.append("-");

        StringBuilder firstLine = new StringBuilder();
        for(int i = firstLine.length(); i < 30; i++) {
            firstLine.append(" ");
        }

        if(calculateTotalScore() == 0)
            firstLine.append("| Current Score: ", 0, "| Current Score: ".length());
        else
            firstLine.append("| Current Score: " + calculateTotalScore(), 0, ("| Current Score: " + calculateTotalScore()).length());
        for(int i = firstLine.length(); i < 70; i++) {
            firstLine.append(" ");
        }

        StringBuilder secondLine = new StringBuilder();
        if(upperSection[0]==-1)
            secondLine.append("| (1) Ones: ");
        else
            secondLine.append("| (1) Ones: ").append(upperSection[0]);
        for(int i = secondLine.length(); i < 17; i++)
            secondLine.append(" ");
        if(upperSection[1]==-1)
            secondLine.append("| (2) Twos: ");
        else
            secondLine.append("| (2) Twos: ").append(upperSection[1]);
        for(int i = secondLine.length(); i < 34; i++)
            secondLine.append(" ");
        if(upperSection[2]==-1)
            secondLine.append("| (3) Threes: ");
        else
            secondLine.append("| (3) Threes: ").append(upperSection[2]);
        for(int i = secondLine.length(); i < 51; i++)
            secondLine.append(" ");
        if(upperSection[3]==-1)
            secondLine.append("| (4) Fours: ");
        else
            secondLine.append("| (4) Fours: ").append(upperSection[3]);
        for(int i = secondLine.length(); i < 68; i++)
            secondLine.append(" ");
        if(upperSection[4]==-1)
            secondLine.append("| (5) Fives: ");
        else
            secondLine.append("| (5) Fives: ").append(upperSection[4]);
        for(int i = secondLine.length(); i < 85; i++)
            secondLine.append(" ");
        if(upperSection[5]==-1)
            secondLine.append("| (6) Sixes: ");
        else
            secondLine.append("| (6) Sixes: ").append(upperSection[5]);
        for(int i = secondLine.length(); i < 102; i++)
            secondLine.append(" ");
        secondLine.append("| Bonus: ").append(upperBonus);
        for(int i = secondLine.length(); i < 119; i++)
            secondLine.append(" ");
        secondLine.append("|");

        StringBuilder thirdLine = new StringBuilder();
        if(lowerSection[0]==-1)
            thirdLine.append("| (7) Three of a Kind: ");
        else
            thirdLine.append("| (7) Three of a Kind: ").append(lowerSection[0]);
        for(int i = thirdLine.length(); i < 119; i++)
            thirdLine.append(" ");
        thirdLine.append("|");

        StringBuilder fourthLine = new StringBuilder();
        if(lowerSection[1]==-1)
            fourthLine.append("| (8) Four of a Kind: ");
        else
            fourthLine.append("| (8) Four of a Kind: ").append(lowerSection[1]);
        for(int i = fourthLine.length(); i < 30; i++)
            fourthLine.append(" ");
        if(lowerSection[2]==-1)
            thirdLine.append("| (9) Small Straight: ");
        else
            thirdLine.append("| (9) Small Straight: ").append(lowerSection[2]);
        for(int i = thirdLine.length(); i < 30; i++)
            thirdLine.append(" ");
        if(lowerSection[3]==-1)
            thirdLine.append("| (10) Large Straight: ");
        else
            thirdLine.append("| (10) Large Straight: ").append(lowerSection[3]);
        for(int i = thirdLine.length(); i < 60; i++)
            thirdLine.append(" ");
        if(lowerSection[4]==-1)
            thirdLine.append("| (11) Full House: ");
        else
            thirdLine.append("| (11) Full House: ").append(lowerSection[4]);
        for(int i = thirdLine.length(); i < 90; i++)
            thirdLine.append(" ");
        if(lowerSection[5]==-1)
            fourthLine.append("| (13) Chance: ");
        else
            fourthLine.append("| (13) Chance: ").append(lowerSection[5]);
        for(int i = fourthLine.length(); i < 60; i++)
            fourthLine.append(" ");
        if(lowerSection[6]==-1)
            fourthLine.append("| (12) Yahtzee: ");
        else
            fourthLine.append("| (12) Yahtzee: ").append(lowerSection[6]);
        fourthLine.append("| Yahtzee Bonus: ").append(yahtzeeBonus);
        for(int i = fourthLine.length(); i < 90; i++)
            fourthLine.append(" ");
        for(int i = fourthLine.length(); i < 119; i++)
            fourthLine.append(" ");
        fourthLine.append("|");

        return dashes + "\n" + firstLine + "\n" + dashes + "\n" + secondLine + "\n" + dashes + "\n"
                + thirdLine + "\n" + dashes + "\n" + fourthLine + "\n" + dashes + "\n\n";
    }
}
