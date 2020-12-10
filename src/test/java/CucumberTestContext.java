
public class CucumberTestContext {
    private Server s;
    private Client c1;
    private Client c2;
    private Client c3;
    boolean isClosed = false;

    public Server getServer(){
        return s;
    }

    public void setServer(Server s){
        this.s = s;
    }

    public Client getClient(int pos){
        switch(pos){
            case 1:
                return c1;
            case 2:
                return c2;
            case 3:
                return c3;
            default:
                return null;
        }
    }

    public void setClient(int pos, Client c){
        switch (pos){
            case 1:
                c1 = c;
                break;
            case 2:
                c2 = c;
                break;
            case 3:
                c3 = c;
                break;
            default:
                break;
        }
    }

    public synchronized void close(){
        if(!isClosed){
            s.close();
        }
    }
}
