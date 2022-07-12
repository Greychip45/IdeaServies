package com.example.ideaservices.Models;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class messageModel {
    private String message,dateTime,sentImage,senderId,receiverId;

    public messageModel(String message, String dateTime, String sentImage, String senderId, String receiverId) {
        this.message = message;
        this.dateTime = dateTime;
        this.sentImage = sentImage;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public messageModel(){

    }

    public String getMessage() {
        return message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getSentImage() {
        return sentImage;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }


}
