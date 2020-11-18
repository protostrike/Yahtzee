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
    int rerollCounter = 0;

    ScoreCard card;

    public Server(int port) {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        card = new ScoreCard();
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
                    ServerClientThread sct = new ServerClientThread(s, this);
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
                while(card.isAllScored()) {
                    continue;
                }

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

    //Getter function to retrieve player thread by given name
    private ServerClientThread getPlayer(int id) {
        for(ServerClientThread sct : list) {
            if(sct.id==id)
                return sct;
        }
        System.out.println("Client " + id + " Cannot be found");
        return null;
    }

    private void checkReady() {
        for(int i = 0; i < list.size(); i++) {
            if(i == 0) {
                send(list.get(i).id, "Currently " + list.size() + " player(s) in lobby, do you want to start game now?\n"
                        + "Enter 'yes' or 'no'");
            }
            else
                send(list.get(i).id, "Waiting for Player one's response");
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
                        send(list.get(i).id, "OK, waiting for more players");
                    }
                    else
                        send(list.get(i).id, "Player one wants more players");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(int id, String msg) {
        for(ServerClientThread sct : list) {
            if(sct.getPlayerId()==id)
                sct.send(msg);
        }
    }

    private void sendAll(String msg) {
        for(ServerClientThread sct : list) {
            sct.send(msg);
        }
    }
    //Handle messages coming from player
    public synchronized void handleMessage(int playerID, String msg){
        if(msg.startsWith("Category: ")){
            //Remove the prefix "Category: "
            msg = msg.substring("Category: ".length());
            int category = msg.charAt(0);
            msg = msg.substring(3).substring("Dices: ".length());
            String[] dicesString = msg.substring(1, msg.length()-1).split(",");
            int[] dices = new int[5];
            for(int i = 0; i < dicesString.length; i++){
                dices[i] = Integer.parseInt(dicesString[i]);
            }
            card.score(dices, category);
            updateAfterScore();
        }
        else{
            System.out.println("Anonymous input, ignore");
        }
    }
    //Update to all players after a successful scoring
    //And reset re-roll counter and scorable list
    private void updateAfterScore(){
        String msg = "";
        sendAll(msg);
    }
}