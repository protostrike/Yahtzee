
import java.net.*;
import java.util.*;
import java.io.*;

    public class Client
    {
        static int Port = 5000;
        int id;
        public BufferedReader br = new BufferedReader(new InputStreamReader((System.in)));
        public BufferedReader in;
        public PrintWriter out;
        public Socket s;
        public final Queue<String> logs = new ArrayDeque<>();
        public InetAddress ip;
        public String name = null;
        int[] dices = new int[5];
        int rerollCounter = 1;
        List<Integer> scorableCategory = new ArrayList<>();

        public Client(String ip){
            try {
                this.ip = InetAddress.getByName(ip);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        //This constructor is only used for testing purpose
        public Client(String ip, String name) {
            this(ip);
            this.name = name;
        }

        public Client(String ip, int port, String name) {
            this(ip, name);
            Port = port;
        }

        public void send(String msg) {
            out.write(msg + "\n");
            out.flush();
        }

        public boolean receive(String msg) {
            synchronized(logs) {
                for(String line : logs) {
                    if(line.equals(msg)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void start() {
            // obtaining input and out streams
            // sendMessage thread
            try {
                s = new Socket(ip, Port);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            Thread sendMessage = new Thread(() -> {
                while (true) {
                    try {
                        //Ask user to score until game ends
                        String msg = formMessage();
                        out.write(msg + "\n");
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            // readMessage thread
            Thread readMessage = new Thread(new Runnable()
            {
                @Override
                public synchronized void run() {
                    try {
                        while (true) {
                            // read the message sent to this client
                            String msg = in.readLine();
                            if(msg.equals("close socket")) {
                                sendMessage.interrupt();
                                return;
                            }
                            else
                                System.out.println(msg);
                            //This statement should ONLY work with the constructor
                            //Client(ip, name)
                            //And should not effect other messages
                            if(msg.equals("Please enter your name:") &&
                                    name != null) {
                                send(name);
                            }
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendMessage.start();
            readMessage.start();

            try {
                readMessage.join();
                close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        public void close() {
            try {
                in.close();
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

        private String formMessage() throws IOException{
            String str = "";
            System.out.println("Enter anything to start rolling dices");
            if (br.readLine() != null)
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
                    String msg = br.readLine();
                    switch (msg) {
                        case "1":
                            //re-roll some dices
                            System.out.println("Please enter in the dice position that you want to hold. Please seperate each number with <<Space>>");
                            msg = br.readLine();
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

            String msg = br.readLine();
            int category = Integer.parseInt(msg);

            //Send warning if category number is invalid
            while (category < 1 || category > 13) {
                System.out.println("The category you chose is invalid, please enter a number from 1 to 13");
                msg = br.readLine();
                category = Integer.parseInt(msg);
            }

            //Send warning if chosen category is already scored
            boolean isScored = checkIfScored(category);
            while (!isScored) {
                System.out.println("The category you entered is already scored, please choose an unscored category");
                msg = br.readLine();
                category = Integer.parseInt(msg);
            }
            str += "Category: " + category + ", " + "Dices: " + dices;
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
            for(Integer i : scorableCategory){
                if(i==category)
                    return false;
            }
            return true;
        }

        public static void main(String[] args) {
            Client c = new Client(args[0]);
            Port = Integer.parseInt(args[1]);
            c.start();
        }
    }

