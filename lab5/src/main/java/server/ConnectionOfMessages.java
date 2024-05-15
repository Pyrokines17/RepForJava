package server;

public class ConnectionOfMessages {
    private final String message;
    private final String sender;

    public ConnectionOfMessages(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
