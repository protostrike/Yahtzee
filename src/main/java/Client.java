
import java.net.*;
import java.util.ArrayDeque;
import java.util.Queue;
import java.io.*;

    public class Client
    {
        static int Port = 5000;
        public BufferedReader br = new BufferedReader(new InputStreamReader((System.in)));
        public BufferedReader in;
        public PrintWriter out;
        public Socket s;
        public final Queue<String> logs = new ArrayDeque<>();
        public InetAddress ip;
        public String name = null;


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
                    // write on the output stream
                    String msg;
                    try {
                        msg = br.readLine();
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

        public static void main(String[] args) {
            Client c = new Client(args[0]);
            Port = Integer.parseInt(args[1]);
            c.start();
        }
    }

