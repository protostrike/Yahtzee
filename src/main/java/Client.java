
import java.net.*;
import java.time.LocalTime;
import java.util.*;
import java.io.*;

    public class Client
    {
        //Basic attribute to start socket
        private static int Port = 5000;
        public InetAddress ip;
        private Socket s;

        //User input reader
        private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        //Used to obtain socket's input and output streams
        private ObjectInputStream in;
        private ObjectOutputStream out;

        private Thread userInput;

        //Attributes for scoring and rolling dices
        private int[] dices = new int[5];
        private int rerollCounter = 0;
        int id = 0;
        private boolean[] scorableCategory = new boolean[13];

        //Flag variables to help on testing and determining different situations
        boolean up = false;
        boolean checkReady = false;
        boolean ready = false;
        boolean reset = false;

        //Variables for logging
        private File log;
        private FileWriter logWriter;

        //Array contains all categories' names
        //Used for printing information to user
        private final String[] categoryNames = {"Ones", "Twos", "Threes", "Fours", "Fives", "Sixes", "Three of a Kind",
        "Four of a Kind", "Small Straight", "Large Straight", "Full House", "Yahtzee", "Chance"};

        //Default constructor
        public Client(){
            //All categories should be able to score when initializing
            Arrays.fill(scorableCategory, true);
            try {
                ip = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        public Client(int port){
            this();
            Port = port;
        }

        public Client(String ip){
            Arrays.fill(scorableCategory, true);
            try {
                this.ip = InetAddress.getByName(ip);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        public Client(String ip, int port) {
            this(ip);
            Port = port;
        }

        //Main entry point of client
        public void start() {
            // obtaining input and out streams of socket
            try {
                log = new File("./log-" + Port +".txt");
                logWriter = new FileWriter(log.getAbsoluteFile(), true);
                s = new Socket(ip, Port);
                in = new ObjectInputStream(s.getInputStream());
                out = new ObjectOutputStream(s.getOutputStream());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            // readMessage thread
            // read the message send to this client
            //Declared this thread here to help on closing the client
            Thread readMessage = new Thread(new Runnable() {
                @Override
                public synchronized void run() {
                    try {
                        while (true) {
                            // read the message send to this client
                            Message msg = (Message) in.readObject();
                            if (msg.equals("close socket")) {
                                return;
                            } else
                                handleMessage(msg);
                        }
                    } catch (SocketException e) {
                        System.out.println("Server disconnected, close this client now");
                        System.exit(0);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            readMessage.start();
            listenToUserInput();
            synchronized (userInput){
                while(!isCheckReady()) {
                    try {
                        userInput.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            up = true;
            new Thread(() -> {
                try {
                    readMessage.join();
                    close();
                } catch (InterruptedException e) {
                    System.out.println("Client thread interrupted, closing now");
                    close();
                }
            }).start();
        }

        //Close all stream and socket
        public void close() {
            try {
                logWriter.close();
                in.close();
                out.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Method to initialize thread for input listener
        private void listenToUserInput(){
            userInput = new Thread(() -> {
                try {
                    while (true) {
                        if(!isCheckReady())
                            Thread.sleep(100);
                        else{
                            //User is now prompt to enter 'ready'
                            String str = input.readLine();
                            if(!ready) {
                                if (!str.equals("ready"))
                                    System.out.println("Invalid input, please enter 'ready' when you are ready to play");
                                else {
                                    send(new Message(Message.Type.ClientReady, str));
                                    System.out.println("Wait for game to start");
                                }
                            }
                            else{
                                //Game is ready to go, user should be prompted to input for scoring
                                formScoringMessage();
                                //Check and handle reset status here
                                if(reset){
                                    handleReset();
                                }
                            }
                        }
                    }
                }
                catch(IOException | InterruptedException e){
                    e.printStackTrace();
                }
            });
            userInput.start();
        }

        //Form messages for rolling dices and scoring
        //Also need to check reset status after each user input
        //Return if reset is needed or finishing up on information gathering
        private void formScoringMessage() throws IOException{
            String str = "";
            System.out.println("Press <<Enter>> to start rolling dices");
            if (input.readLine() != null) {
                if(reset)
                    return;
                rollDice();
            }
            while (rerollCounter < 3) {
                //Conditional statement to proceed player's choice
                //And give player a warning if input is invalid
                label:
                while (true) {
                    System.out.println("                 --- --- --- --- ---" + "\n"
                            + String.format("Your dices are: | %1d | %1d | %1d | %1d | %1d |", dices[0], dices[1], dices[2], dices[3], dices[4]) + "\n"
                            + "                 --- --- --- --- ---");
                    System.out.println("Do you want to reroll some dices, reroll all dices, or score current dices?");
                    System.out.println("(1) Reroll some dices\n(2) Reroll all\n(3) Score");
                    System.out.println("Please enter your choice as 1, 2 or 3");
                    System.out.println("Reroll(s) left: " + (3 - rerollCounter));

                    //Waiting for current player's input
                    String msg = input.readLine();
                    if(reset)
                        return;
                    switch (msg) {
                        case "1":
                            //re-roll some dices
                            System.out.println("Please enter in the dice position that you want to hold. Please split each number with <<Space>>");
                            msg = input.readLine();
                            if(reset)
                                return;
                            rollDice(msg);

                            //re-roll counter reaches limit
                            if (rerollCounter == 3) {
                                System.out.println("                 --- --- --- --- ---" + "\n"
                                        + String.format("Your dices are: | %1d | %1d | %1d | %1d | %1d |", dices[0], dices[1], dices[2], dices[3], dices[4]) + "\n"
                                        + "                 --- --- --- --- ---");
                                System.out.println("No re-rolls left, you have to score your dices");
                            }
                            break label;
                        case "2":
                            //re-roll all dices
                            rollDice();

                            //re-roll counter reaches limit
                            if (rerollCounter == 3) {
                                System.out.println("                 --- --- --- --- ---" + "\n"
                                        + String.format("Your dices are: | %1d | %1d | %1d | %1d | %1d |", dices[0], dices[1], dices[2], dices[3], dices[4]) + "\n"
                                        + "                 --- --- --- --- ---");
                                System.out.println("No rerolls left, you have to score your dices");
                            }
                            break label;
                        case "3":
                            rerollCounter = 3; //Make re-rolls impossible
                            break label;

                        //Invalid input
                        //Prompt player to re-enter the message
                        default:
                            System.out.println("Your input is invalid, Please enter 1, 2 ,or 3");
                            break;
                    }
                }
            }
            System.out.println("Please enter a category to score\n" +
                    "1: Ones. 2: Twos. 3: Threes. 4: Fours. 5: Fives. 6: Sixes.\n" +
                    "7: Three of A Kind. 8: Four of A Kind. 9: Small Straight.\n" +
                    "10: Large Straight. 11: Full House. 12: Yahtzee. 13: Chance");

            String msg = input.readLine();
            if(reset)
                return;
            int category;
            while (true) {
                try {
                    category = Integer.parseInt(msg);
                } catch (NumberFormatException e){
                    System.out.println("Your input is not a number, please enter again");
                    msg = input.readLine();
                    continue;
                }
                break;
            }

            //Send warning if category number is invalid
            while (category < 1 || category > 13) {
                System.out.println("The category you chose is invalid, please enter a number from 1 to 13");
                msg = input.readLine();
                if(reset)
                    return;
                category = Integer.parseInt(msg);
            }

            //Send warning if chosen category is already scored
            while (!checkAvailableCategory(category-1)) {
                System.out.println("The category you entered is already scored, please choose an unscored category");
                msg = input.readLine();
                if(reset)
                    return;
                category = Integer.parseInt(msg);
            }

            //Everything is ready, send it to server now
            str += "Category: " + category + ", " + "Dices: " + Arrays.toString(dices);
            send(new Message(Message.Type.Score, str));

            //reset re-roll counter after forming message
            rerollCounter = 0;
        }

        public void send(Message msg) {
            try {
                out.writeObject(msg);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Roll all dices
        private void rollDice() {
            for(int i = 0; i < dices.length; i++) {
                dices[i] = (int) Math.ceil(Math.random()*6);
            }
            rerollCounter++;
        }

        //Roll all other dices that are not appear in holding list
        private void rollDice(String hold) {
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

        private boolean isCheckReady(){
            return checkReady;
        }

        //Ask server whether the category is scored
        //Return true if the category is scored
        private boolean checkAvailableCategory(int category){
            return scorableCategory[category];
        }

        //Handle messages incoming from server
        private void handleMessage(Message msg){
            String message = msg.message;
            if(msg.type.equals(Message.Type.CheckReady)){
                logging("Check Client ready");
                System.out.println(message);
                synchronized (userInput){
                    checkReady = true;
                    userInput.notify();
                }
            }
            else if(msg.type.equals(Message.Type.Update)){
                logging(message);
                message = message.substring("Update -- ".length());
                handleUpdate(message);
            }
            else if(msg.type.equals(Message.Type.ID)){
                logging("Received ID -- " + message);
                id = Character.getNumericValue(message.charAt(0));
            }
            else if(msg.type.equals(Message.Type.Start)) {
                ready = true;
                System.out.println("Game start, press <<Enter>> to start the game");
            }
            else if(msg.type.equals(Message.Type.Card)){
                System.out.println("Current score card is: ");
                System.out.println(message);
            }
            else if(msg.type.equals(Message.Type.End)){
                handleGameEnd(message);
            }
        }

        //Update client information based on server's message
        //Message's prefix is already removed
        private synchronized void handleUpdate(String msg){
            //Message supposed to be the scored category
            String categoryStr = msg.split(", Client:")[0];
            int category = Integer.parseInt(categoryStr);
            scorableCategory[category-1] = false;
            int scorerID = Character.getNumericValue(msg.charAt(msg.length()-1));
            if(scorerID==id) {
                //if this client made the last scoring, reset is not needed for it
                System.out.println("Successfully scored in " + categoryNames[category-1]);
                logging("Category index " + (category - 1) + " is scored");
                System.out.println(categoryNames[category-1] + " has been scored!\n " +
                        "you can press <<Enter>> now to start a new round");
            }
            else {
                logging("Reset, a score is made by Client " + scorerID);
                System.out.println(categoryNames[category-1] + " has been scored!\n " +
                        "you can press <<Enter>> now to make client reset for a new rolling-dice process");

                //toggle reset status to true
                reset = true;
            }
        }

        //reset alert handler
        private void handleReset(){
            System.out.println("Your input will be reset in a second, please wait for a new round");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //reset flag variable
            reset = false;

            //reset re-roll counter
            rerollCounter = 0;
        }

        private void handleGameEnd(String msg){
            System.out.println("Game ends\n Final score information is: ");
            System.out.println(msg);
            System.out.println("Closing client now");
            System.exit(0);
        }

        //Logging to file
        private void logging(String str){
            try {
                if(id==0) {
                    str = "[" + LocalTime.now() + "]Client: "+ str + "\n";
                }
                else {
                    str = "[" + LocalTime.now() + "]Client " + id +": "+ str + "\n";
                }
                logWriter.write(str);
                logWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {
            Client c;
            if(args[0].equals("5000")) {
                c = new Client("127.0.0.1", 5000);
            }
            else {
                c = new Client(args[0]);
                Port = Integer.parseInt(args[1]);
            }
            c.start();
        }

    }

