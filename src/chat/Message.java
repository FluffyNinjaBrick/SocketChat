package chat;

import java.io.Serializable;

// simple POJO representing a chat message
public class Message implements Serializable  {

    private String text;
    private String sender;


    public Message(String text, String sender) {
        this.text = text;
        this.sender = sender;
    }


    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getSender() { return sender; }
    public void setSenderId(String senderId) { this.sender = senderId; }

    @Override
    public String toString() {
        return this.sender + ": " + this.text;
    }

}
