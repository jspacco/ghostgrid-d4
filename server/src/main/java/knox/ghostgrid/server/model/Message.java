package knox.ghostgrid.server.model;

import java.time.Instant;

public class Message {
    private String user;
    private String text;
    private String timestamp;

    public Message() {}

    public Message(String user, String text) {
        this.user = user;
        this.text = text;
        this.timestamp = Instant.now().toString();
    }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
