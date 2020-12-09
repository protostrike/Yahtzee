
public class CucumberTestContext {
    private Server s = null;
    private Client[] clients = new Client[3];

    public Server getServer(){
        return s;
    }

    public void setServer(Server s){
        this.s = s;
    }

    public Client getClient(int pos){
        return clients[pos-1];
    }

    public void setClient(int pos, Client c){
        clients[pos-1] = c;
    }
}
