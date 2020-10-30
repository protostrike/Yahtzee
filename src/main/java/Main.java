import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        System.out.println("Enter the number of players");
        Scanner scr = new Scanner(System.in);
        int numOfPlayers = Integer.parseInt(scr.nextLine());
        Launcher lc = new Launcher(numOfPlayers);
        lc.start();
    }
}
