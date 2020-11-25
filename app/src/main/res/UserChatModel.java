package com.example.stufun.Model;

public class UserChatModel {
    String message,sender,receiver,image,date,type,clear;
    private boolean isseen;

    public UserChatModel() {
    }

    public UserChatModel(String message, String sender, String receiver, String image, String date, String type, String clear, boolean isseen) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.image = image;
        this.date = date;
        this.type = type;
        this.clear = clear;
        this.isseen = isseen;
    }

    public String getClear() {
        return clear;
    }

    public void setClear(String clear) {
        this.clear = clear;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
