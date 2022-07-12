package com.example.ideaservices.Models;

public class NewChatsModel {
    private String userName,senderId,receiverId,message,dateTime;

    public NewChatsModel(String userName, String senderId, String receiverId, String message, String dateTime) {
        this.userName = userName;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.dateTime = dateTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }



    public String getMessage() {
        return message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public NewChatsModel(){

    }


}
