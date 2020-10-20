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
        lowerSection = new int[]{-1, -1, -1, -1, -1, -1};
    }

    public void score(int[] dices, int category){
        if(category<=5)
            scoreUppers(dices, category);
    }

    private void scoreUppers(int[] dices, int category){
        for(int dice : dices){
            if(dice==category+1){
                if(upperSection[category]==-1)
                    //If no score is in yet, make the first dice as the initial score
                    upperSection[category] = dice;
                upperSection[category] += dice;
            }
        }
        if(upperSection[category] == -1){
            //If no score is in, make it a 0
            upperSection[category] = 0;
        }
    }
}
