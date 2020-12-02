//A Java program for a Server 

import java.net.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Server {
    //initialize socket and input stream
    private List<Connection> list = new ArrayList<>();
    private ServerSocket ss;
    private Thread waitForConnection;
    private Thread gameEngine;
    boolean up;
    boolean ready;
    ScoreCard card;
    private File log;
    private FileWriter logWriter;

    public Server(int port) {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        card = new ScoreCard();
        log = new File("./log.txt");
        //Create log file
        //And delete old one if exists
        try {
            if (log.getAbsoluteFile().exists()) {
                System.out.println("Old logs detected, deleting now");
                log.getAbsoluteFile().delete();
            }
            log.createNewFile();
            logWriter = new FileWriter(log.getAbsoluteFile(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        //Thread for getting connection requests
        //For now, only three players is allowed to join this game
        waitForConnection = new Thread(() -> {
            try {
                Socket s;
                logging("==Server start==");
                logging("Wait for client connection");
                while (list.size() < 3 && !ready) {
                    try {
                        s = ss.accept();
                        logging("A client is trying to connect in the server");
                        Connection connection = new Connection(s, list.size() + 1);
                        list.add(connection);

                        //Start thread listening to this client
                        listenToClient(connection);

                        //Send ID to client
                        send(connection.id, "Your ID is: " + connection.id);
                        //Give client a prompt to be ready for playing
                        send(connection.id, "Check Ready -- Enter 'ready' when you are ready to play");
                    } catch (SocketException e) {
                        logging("Server Socket closed, shut down connection");
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gameEngine = new Thread(new Runnable() {
            public synchronized void run() {
                logging("==Game start==");
                sendAll("Game Start");
                while (!card.isAllScored()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        logging("Game engine thread interrupted, closing now");
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
            while (!checkReady()) {
                Thread.sleep(100);
            }
            ready = true;
            gameEngine.start();

            //Wait until game ends
            gameEngine.join();
            logging("==Game ends==");
            //After game is complete, close all clients
            //Then wait for 3 seconds and close ServerSocket
            Thread.sleep(3000);
            close();
        } catch (InterruptedException e) {
            logging("Main server thread interrupted, closing now");
            close();
        }
    }

    public void close() {
        try {
            waitForConnection.interrupt();
            gameEngine.interrupt();
            ss.close();

            //Close fileWriter after threads terminated
            if (waitForConnection.isAlive())
                waitForConnection.join();
            if (gameEngine.isAlive())
                gameEngine.join();
            for(Connection c : list)
                c.close();
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            //Still need to close logWriter if interrupted signal is caught
            try {
                logWriter.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    //Getter for ready
    public boolean isReady() {
        return ready;
    }

    //Getter for connection
    public Connection getConnection(int id) {
        return list.get(id - 1);
    }

    //Handle messages coming from player
    private synchronized void handleMessage(int playerID, String msg) {
        if (msg == null)
            return;
        logging("From Client " + playerID + ": " + msg);
        if (msg.startsWith("Category: ")) {
            if (!isReady()) {
                System.out.println("Game is not ready yet, ignore this message");
                return;
            }
            //Remove the prefix "Category: "
            msg = msg.substring("Category: ".length());
            int category = Character.getNumericValue(msg.charAt(0));
            msg = msg.substring(3).substring("Dices: ".length());
            String[] dicesString = msg.substring(1, msg.length() - 1).split(",");
            int[] dices = new int[5];
            for (int i = 0; i < dicesString.length; i++) {
                dices[i] = Integer.parseInt(dicesString[i].trim());
            }
            if(card.getScoreByCategory(category-1)==-1) {
                logging("Scoring now in category " + (category - 1));
                card.score(dices, category - 1);
                updateAfterScore(playerID, category);
            }
            else
                logging("Client " + playerID + " is trying to score an non-empty category, ignore this one");
        } else if (msg.equals("ready")) {
            list.get(playerID - 1).setReady(true);
        } else {
            logging("Anonymous input: " + msg + ". ignore");
        }
    }

    private void logging(String str) {
        try {
            str = "[" + LocalTime.now() + "]Server: " + str + "\n";
            logWriter.write(str);
            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                } catch (SocketException e){
                    System.out.println("One of the client disconnected, close the server now");
                    System.exit(0);
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


    //Update to all players after a successful scoring
    //And reset re-roll counter and scorable list
    private void updateAfterScore(int playerID, int category) {
        String msg = "Update -- " + category + ", Client: " + playerID;
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