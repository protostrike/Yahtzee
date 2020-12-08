//A Java program for a Server 
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;

public class Server {
    //initialize socket and input stream
    public List<ServerClientThread> list = new ArrayList<>();
    ServerSocket ss;
    boolean ready = false;
    int[] dices = new int[5];
    int currentRound = 1;
    int rerollCounter = 0;
    Integer highest = null;

    public Server(int port) {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Getter function to retrieve player thread by given name
    public ServerClientThread getPlayer(String name) {
        for(ServerClientThread sct : list) {
            if(sct.name.equals(name))
                return sct;
        }
        System.out.println(name + " Cannot be found");
        return null;
    }

    public void rollDice() {
        for(int i = 0; i < dices.length; i++) {
            dices[i] = (int) Math.ceil(Math.random()*6);
        }
        rerollCounter++;
    }

    public void rollDice(String hold) {
        String[] str = hold.split(" ");
        int[] holdingList = new int[str.length];

        //Convert string to int
        for(int i = 0; i < str.length; i++) {
            holdingList[i] = Integer.parseInt(str[i]) - 1;
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
        rerollCounter++;
    }

    public void checkReady() {
        for(int i = 0; i < list.size(); i++) {
            if(i == 0) {
                send(list.get(i).name, "Currently " + list.size() + " player(s) in lobby, do you want to start game now?\n"
                        + "Enter 'yes' or 'no'");
            }
            else
                send(list.get(i).name, "Waiting for Player one's response");
        }
        //Waiting for ready signal from player 1
        try {
            String msg = list.get(0).in.readLine();
            if(msg.equals("yes")) {
                ready = true;
            }
            else {
                for(int i = 0; i < list.size(); i++) {
                    if(i == 0) {
                        send(list.get(i).name, "OK, waiting for more players");
                    }
                    else
                        send(list.get(i).name, "Player one wants more players");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scorePlayer(String currentPlayer) {
        try {
            send(currentPlayer, "Which category do you want to score? Please enter the number of a category");

            String msg = getPlayer(currentPlayer).in.readLine();
            int category = Integer.parseInt(msg);

            //Send warning if category number is invalid
            while(category < 1 || category > 13) {
                send(currentPlayer, "The category you chose is invalid, please enter a number from 1 to 13");
                msg = getPlayer(currentPlayer).in.readLine();
                category = Integer.parseInt(msg);
            }

            //Send warning if chosen category is already scored
            int isScored = getPlayer(currentPlayer).card.getScoreByCategory(category-1);
            while(isScored!=-1) {
                send(currentPlayer, "The category you entered is already scored, please choose an unscored category");
                msg = getPlayer(currentPlayer).in.readLine();
                category = Integer.parseInt(msg);
                isScored = getPlayer(currentPlayer).card.getScoreByCategory(category-1);
            }
            getPlayer(currentPlayer).card.score(dices, category-1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendWinner() {
        StringBuilder winner = new StringBuilder();
        sendScoreToAll(list.get(0).name);
        for(ServerClientThread sct : list) {
            if(highest == null) {
                highest = sct.card.calculateTotalScore();
                winner = new StringBuilder(sct.name);
                continue;
            }
            if(sct.card.calculateTotalScore() > highest) {
                winner = new StringBuilder(sct.name);
                highest = sct.card.calculateTotalScore();
            }
            else if(sct.card.calculateTotalScore() == highest)
                winner.append(" and ").append(sct.name);
        }

        sendAll("Winner is " + winner + "\n" + "Thank you for playing Yahtzee game!!!");
        sendAll("close socket");
    }

    /*
     * Send score cards to all players
     * currentPlayer should be on top of other players
     */
    public void sendScoreToAll(String currentPlayer) {
        StringBuilder msg = new StringBuilder(getPlayer(currentPlayer).card.toString());
        for(ServerClientThread sct : list) {
            if(!sct.getClientName().equals(currentPlayer))
                msg.append(sct.card.toString());
        }
        sendAll(msg.toString());
    }

    public void send(String name, String msg) {
        for(ServerClientThread sct : list) {
            if(sct.getClientName().equals(name))
                sct.send(msg);
        }
    }

    public void sendAll(String msg) {
        for(ServerClientThread sct : list) {
            sct.send(msg);
        }
    }

    public void start() {

        //Thread for getting connection requests
        //For now, only three players is allowed to join this game
        Thread waitForConnection = new Thread(() -> {
            try {
                Socket s;
                System.out.println("Server start!");
                System.out.println("Wait for client connection");
                while(list.size() < 3 && !ready) {
                    s = ss.accept();
                    System.out.println("A client is trying to connect in server");

                    System.out.println("Initializing this player...");
                    ServerClientThread sct = new ServerClientThread(s);
                    list.add(sct);
                    //Start thread to this client
                    sct.start();
                    checkReady();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread gameEngine = new Thread(new Runnable() {
            public synchronized void run() {
                System.out.println("Game start");

                //Max round is 13
                while(currentRound <= 13) {
                    System.out.println("Round " + currentRound + " start");

                    //loop on each player
                    for (ServerClientThread serverClientThread : list) {
                        String currentPlayer = serverClientThread.name;
                        sendScoreToAll(currentPlayer);
                        send(currentPlayer, "Press <<Enter>> to roll your dices");
                        try {
                            String msg = serverClientThread.in.readLine();
                            if (msg != null) {
                                //Roll first time
                                rollDice();
                                while (rerollCounter < 3) {
                                    //Conditional statement to proceed player's choice
                                    //And give player a warning if input is invalid
                                    label:
                                    while (true) {
                                        send(currentPlayer, "                 --- --- --- --- ---" + "\n"
                                                + String.format("Your dices are: | %1d | %1d | %1d | %1d | %1d |", dices[0], dices[1], dices[2], dices[3], dices[4]) + "\n"
                                                + "                 --- --- --- --- ---");
                                        send(currentPlayer, "Do you want to reroll some dices, reroll all dices, or score current dices?");
                                        send(currentPlayer, "(1) Reroll some dices\n(2) Reroll all\n(3) Score");
                                        send(currentPlayer, "Please enter your choice as 1, 2 or 3");
                                        send(currentPlayer, "Reroll(s) left: " + (3 - rerollCounter));

                                        //Waiting for current player's input
                                        msg = serverClientThread.in.readLine();
                                        switch (msg) {
                                            case "1":
                                                //re-roll some dices
                                                send(currentPlayer, "Please enter in the dice position that you want to hold. Please seperate each number with <<Space>>");
                                                msg = serverClientThread.in.readLine();
                                                rollDice(msg);

                                                //re-roll counter reaches limit
                                                if (rerollCounter == 3) {
                                                    send(currentPlayer, "                 --- --- --- --- ---" + "\n"
                                                            + String.format("Your dices are: | %1d | %1d | %1d | %1d | %1d |", dices[0], dices[1], dices[2], dices[3], dices[4]) + "\n"
                                                            + "                 --- --- --- --- ---");
                                                    send(currentPlayer, "No rerolls left, you have to score your dices");
                                                }
                                                break label;
                                            case "2":
                                                //re-roll all dices
                                                rollDice();

                                                //re-roll counter reaches limit
                                                if (rerollCounter == 3) {
                                                    send(currentPlayer, "                 --- --- --- --- ---" + "\n"
                                                            + String.format("Your dices are: | %1d | %1d | %1d | %1d | %1d |", dices[0], dices[1], dices[2], dices[3], dices[4]) + "\n"
                                                            + "                 --- --- --- --- ---");
                                                    send(currentPlayer, "No rerolls left, you have to score your dices");
                                                }
                                                break label;
                                            case "3":
                                                rerollCounter = 3; //Make re-rolls impossible

                                                break label;

                                            //Invalid input
                                            //Prompt player to re-enter the message
                                            default:
                                                send(currentPlayer, "Your input is invalid, Please enter 1, 2 ,or 3");
                                                break;
                                        }
                                    }
                                }
                                //re-roll counter reached limit, must score now
                                scorePlayer(currentPlayer);
                                System.out.println(currentPlayer + " has completed this turn");

                                //reset re-roll counter
                                rerollCounter = 0;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Round " + currentRound + " completed!" + "\n");
                    currentRound++;
                }
                //Game ends here
                sendWinner();
            }
        });

        //Start waiting for connection
        waitForConnection.start();

        //Wait until 3 players has joined game
        try {
            waitForConnection.join();
            //Wait for ready signal
            gameEngine.start();

            gameEngine.join();
            //After game is complete, close all clients
            //Then wait for 3 seconds and close ServerSocket
            Thread.sleep(3000);
            close();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void close() {
        for(ServerClientThread sct : list) {
            sct.close();
            sct.interrupt();
        }
        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Main function for testing purpose for now
     */
    public static void main(String[] args) {
        Server s = new Server(Integer.parseInt(args[0]));
        s.start();
    }
}

class ServerClientThread extends Thread {
    Socket serverClient;
    BufferedReader in;
    PrintWriter out;
    String name;
    ScoreCard card;

    public ServerClientThread(Socket s) {
        try {
            this.out = new PrintWriter(s.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serverClient = s;
        this.name = askName();
        this.card = new ScoreCard(name);
    }

    public String askName() {
        send("Please enter your name:");
        String msg = null;
        try {
            msg = in.readLine();
            System.out.println(msg + " Joined game");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }


    public String getClientName() {
        return name;
    }

    public synchronized void send(String msg) {
        out.write(msg + "\n");
        out.flush();
    }

    public void run(){
    }

    public void close() {
        try {
            out.close();
            in.close();
            serverClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}