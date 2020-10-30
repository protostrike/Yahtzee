import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Launcher {
    Player[] players;
    InputStreamReader r = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(r);

    public Launcher(int numOfPlayers){
        players = new Player[numOfPlayers];
        for(int i = 0; i < players.length; i++){
            players[i] = new Player();
        }
    }

    public void start()  {
        System.out.println("Game start with " + players.length + " player(s)");
        int roundCount = 1;
        while(roundCount < 13){
            System.out.println("Round " + roundCount + "\n");
            for(int i = 0; i < players.length; i++){
                System.out.println("Player " + (i+1) + "'s round\n");
                System.out.println("Player " + (i+1) + "'s score card:");
                System.out.println(players[i].card);
                playRound(players[i]);
            }
            roundCount ++;
        }
        int[] finalScore = new int[players.length];
        for(int i = 0; i < finalScore.length; i++){
            finalScore[i] = players[i].card.calculateTotalScore();
        }
        System.out.println("Game ends now, calculating final scores......");
        try {
            wait(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < finalScore.length; i++){
            System.out.println("Player " + i + " final score: " + finalScore[i]);
        }
    }

    public void playRound(Player player){
        int rerollCount = 1;
        System.out.println("Rolling dices for the first time......");
        player.rollDice();
        try {
            while(rerollCount < 3){
                System.out.print("Current Dices: ");
                player.printDices();
                System.out.println("\nPlease select which dice(s) you want to hold. \n" +
                        "Enter the position of dices and split them by space, i.e. 1 3 4\n" +
                        "Or enter skip to skip the re-roll\n");
                System.out.println("Re-roll left: " + (3-rerollCount));
                String hold = br.readLine();
                if(hold.equals("skip")){
                    score(player);
                    return;
                }
                else if(hold.equals("")){
                    player.rollDice();
                    rerollCount++;
                }
                else {
                    player.rollDice(hold);
                    rerollCount++;
                }
            }
            System.out.println("All re-rolls are used.");
            System.out.print("Current Dices: ");
            player.printDices();
            score(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void score(Player player){
        System.out.println("Please enter a category to score\n" +
                "1: Ones. 2: Twos. 3: Threes. 4: Fours. 5: Fives. 6: Sixes.\n" +
                "7: Three of A Kind. 8: Four of A Kind. 9: Small Straight.\n" +
                "10: Large Straight. 11: Full House. 12: Yahtzee. 13: Chance");
        try {
            while(true){
                String cat = br.readLine();
                int category = Integer.parseInt(cat)-1;
                if(player.card.getScoreByCategory(category)!=-1){
                    System.out.println("The category you entered has already scored\n" +
                            "Please choose another category to score");
                }
                else {
                    player.card.score(player.dices, category);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
