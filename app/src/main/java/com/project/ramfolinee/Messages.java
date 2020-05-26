package com.project.ramfolinee;


public class Messages {

    private String message, type;
    private long  time;
    private boolean seen;
    private String sender;
    private String reciever;

    private String from;

public Messages(){}

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Messages(String message, String type, long time, boolean seen, String sender, String reciever,String from) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
        this.sender = sender;
        this.reciever = reciever;
        this.from= from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }



}
