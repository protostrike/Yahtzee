
import java.net.*;
import java.time.LocalTime;
import java.util.*;
import java.io.*;

    public class Client
    {
        static int Port = 5000;
        BufferedReader in;
        PrintWriter out;
        Socket s;
        Thread readMessage;
        boolean up = false;
        public InetAddress ip;
        public String name = null;
        int[] dices = new int[5];
        int rerollCounter = 1;
        int id = 0;
        Scanner scr = new Scanner(System.in);
        boolean[] scorableCategory = new boolean[13];
        boolean isTest = false;

        File log = new File("./log.txt");
        FileWriter logWriter;


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

        public Client(int port, boolean test){
            this();
            Port = port;
            isTest = test;
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

        public void send(String msg) {
            out.write(msg + "\n");
            out.flush();
        }

        public void start() {
            // obtaining input and out streams of socket
            try {
                logWriter = new FileWriter(log.getAbsoluteFile(), true);
                s = new Socket(ip, Port);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            // readMessage thread
            readMessage = new Thread(new Runnable()
            {
                @Override
                public synchronized void run() {
                    try {
                        while (true) {
                            // read the message send to this client
                            String msg = in.readLine();
                            System.out.println("Received from server: " + msg);
                            if(msg.equals("close socket")) {
                                return;
                            }
                            else
                                handleMessage(msg);
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            readMessage.start();
            up = true;
            try {
                readMessage.join();
                System.out.println("ends here");
                close();
            } catch (InterruptedException e) {
                System.out.println("Client thread interrupted, closing now");
                close();
            }
        }

        public void close() {
            try {
                logWriter.close();
                in.close();
                scr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.close();
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Form messages for rolling dices and scoring
        private String formScoringMessage() throws IOException{
            String str = "";
            System.out.println("Enter anything to start rolling dices");
            if (scr.nextLine() != null)
                rollDice();
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
                    String msg = scr.nextLine();
                    switch (msg) {
                        case "1":
                            //re-roll some dices
                            System.out.println("Please enter in the dice position that you want to hold. Please seperate each number with <<Space>>");
                            msg = scr.nextLine();
                            rollDice(msg);

                            //re-roll counter reaches limit
                            if (rerollCounter == 3) {
                                System.out.println("                 --- --- --- --- ---" + "\n"
                                        + String.format("Your dices are: | %1d | %1d | %1d | %1d | %1d |", dices[0], dices[1], dices[2], dices[3], dices[4]) + "\n"
                                        + "                 --- --- --- --- ---");
                                System.out.println("No rerolls left, you have to score your dices");
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
            System.out.println("Which category do you want to score? Please enter the number of a category");

            String msg = scr.nextLine();
            int category = Integer.parseInt(msg);

            //Send warning if category number is invalid
            while (category < 1 || category > 13) {
                System.out.println("The category you chose is invalid, please enter a number from 1 to 13");
                msg = scr.nextLine();
                category = Integer.parseInt(msg);
            }

            //Send warning if chosen category is already scored
            while (checkIfScored(category)) {
                System.out.println("The category you entered is already scored, please choose an unscored category");
                msg = scr.nextLine();
                category = Integer.parseInt(msg);
            }
            str += "Category: " + category + ", " + "Dices: " + Arrays.toString(dices);
            return str;
        }

        private void rollDice() {
            for(int i = 0; i < dices.length; i++) {
                dices[i] = (int) Math.ceil(Math.random()*6);
            }
            rerollCounter++;
        }

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

        //Ask server whether the category is scored
        //Return true if the category is scored
        private boolean checkIfScored(int category){
            return scorableCategory[category];
        }

        //Handle messages incoming from server
        private void handleMessage(String msg){
            if(msg.startsWith("Check Ready")){
                logging(msg);
                System.out.println(msg.substring("Check Ready -- ".length()));
                if(!isTest)
                    handleReady();
            }
            else if(msg.startsWith("Update")){
                logging(msg);
                msg = msg.substring("Update -- ".length());
                handleUpdate(msg);
            }
            else if(msg.startsWith("Your ID is")){
                msg = msg.substring("Your ID is: ".length());
                logging("Received ID -- " + msg);
                id = Character.getNumericValue(msg.charAt(0));
            }
        }

        //Read user input for game ready status
        private void handleReady(){
            String msg = scr.nextLine();
            while(!msg.equals("ready")){
                System.out.println("Invalid input, please enter 'ready' when you are ready to play");
                msg = scr.nextLine();
            }
            send(msg);
            scr.close();
        }

        //Update client information based on server's message
        //Message's prefix is already removed
        private synchronized void handleUpdate(String msg){
            //Message supposed to be the scored category
            int category = Integer.parseInt(msg);
            scorableCategory[category-1] = false;
            System.out.println("Category index " + (category-1) + " is scored");
            logging("Category index " + (category-1) + " is scored");
        }

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

