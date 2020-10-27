import java.util.Arrays;
import java.util.Scanner;

public class Player {
    ScoreCard card;
    int[] dices = new int[5];
    Scanner scanner = new Scanner(System.in);

    public Player(){
        card = new ScoreCard();
    }

    public void rollDice(){
        for (int i = 0; i < dices.length; i++){
            dices[i] = (int)Math.ceil(Math.random()*6);
        }
    }

    public void rollDice(String str){
        String[] hold = str.split(" ");
        int[] holdingList = new int[hold.length];

        //Convert string to int
        for(int i = 0; i < hold.length; i++) {
            holdingList[i] = Integer.parseInt(hold[i]) - 1;
        }
        Arrays.sort(holdingList);
        //re-roll dices that not selected for holding
        int index = 0; // index for traversing holdingList
        for(int i = 0; i < dices.length; i++) {
            if(index >= holdingList.length) {
                dices[i] = (int) Math.ceil(Math.random()*6);
                continue;
            }
            if(i == holdingList[index]) {
                index++;
            }
            else
                dices[i] = (int) Math.ceil(Math.random()*6);
        }
    }

    private int getScore(){
        return card.calculateTotalScore();
    }
}
