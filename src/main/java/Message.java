import java.io.Serializable;

public class Message implements Serializable {
    enum Type{CheckReady, Start, ID, Card, Update, Score, ClientReady, End}

    Type type;
    String message;

    public Message(Type t, String s){
        type = t;
        message = s;
    }
}
