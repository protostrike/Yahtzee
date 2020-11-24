//A Java program for a Server 

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Server {
    //initialize socket and input stream
    public List<Connection> list = new ArrayList<>();
    ServerSocket ss;
    Thread waitForConnection;
    Thread gameEngine;
    boolean up = false;
    boolean ready = false;
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
        waitForConnection = new Thread(() -> {
            try {
                Socket s;
                System.out.println("Server start!");
                System.out.println("Wait for client connection");
                while (list.size() < 3 && !ready) {
                    try {
                        s = ss.accept();
                        System.out.println("A client is trying to connect in server");

                        System.out.println("Initializing this player...");
                        Connection connection = new Connection(s, list.size() + 1);
                        list.add(connection);

                        //Start thread listening to this client
                        listenToClient(connection);

                        //Give client a prompt to be ready for playing
                        send(connection.id, "Check Ready -- Enter 'ready' when you are ready to play");
                    } catch (SocketException e) {
                        System.out.println("Server Socket closed");
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gameEngine = new Thread(new Runnable() {
            public synchronized void run() {
                System.out.println("Game start");
                while (!card.isAllScored()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted, closing now");
                        close();
                        return;
                    }
                }
            }
        });

        //Start waiting for connection
        waitForConnection.start();

        //Indicating the server is up
        up = true;

        //Wait until 3 players has joined game
        try {
            waitForConnection.join();
            //Wait until all clients are ready
            System.out.println("Waiting for ready signal from all clients......");
            while (!checkReady()) ;
            ready = true;
            gameEngine.start();

            //Wait until game ends
            gameEngine.join();
            //After game is complete, close all clients
            //Then wait for 3 seconds and close ServerSocket
            Thread.sleep(3000);
            close();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted, closing now");
            close();
        }
    }

    public void close() {
        try {
            waitForConnection.interrupt();
            gameEngine.interrupt();
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isReady() {
        return ready;
    }


    private void send(int id, String msg) {
        for (Connection sct : list) {
            if (sct.getPlayerId() == id)
                sct.send(msg);
        }
    }

    private void sendAll(String msg) {
        for (Connection sct : list) {
            sct.send(msg);
        }
    }

    private void listenToClient(Connection connection) {
        new Thread(() -> {
            while (true) {
                try {
                    String msg = connection.in.readLine();
                    handleMessage(connection.id, msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //Check all client's status on ready-to-play
    private boolean checkReady() {
        for (Connection c : list) {
            if (!c.isReady())
                return false;
        }
        return true;
    }

    //Handle messages coming from player
    public synchronized void handleMessage(int playerID, String msg) {
        if(msg==null)
            return;
        System.out.println("From Client " + playerID + ": " + msg);
        if (msg.startsWith("Category: ")) {
            //Remove the prefix "Category: "
            msg = msg.substring("Category: ".length());
            int category = msg.charAt(0);
            msg = msg.substring(3).substring("Dices: ".length());
            String[] dicesString = msg.substring(1, msg.length() - 1).split(",");
            int[] dices = new int[5];
            for (int i = 0; i < dicesString.length; i++) {
                dices[i] = Integer.parseInt(dicesString[i]);
            }
            card.score(dices, category);
            updateAfterScore(category);
        } else if (msg.equals("ready")) {
            list.get(playerID - 1).setReady(true);
        } else {
            System.out.println("Anonymous input, ignore");
        }
    }

    //Getter for connection
    public Connection getConnection(int id) {
        return list.get(id - 1);
    }

    //Update to all players after a successful scoring
    //And reset re-roll counter and scorable list
    private void updateAfterScore(int category) {
        String msg = "Update -- " + category;
        sendAll(msg);
    }

    /*
     * Main function for testing purpose for now
     */
    public static void main(String[] args) {
        Server s = new Server(Integer.parseInt(args[0]));
        s.start();
    }
}