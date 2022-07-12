package com.example.ideaservices.Models;

public class requestModel {
    private String userLocation,serviceStatus,dateTime,serviceDescription,senderId,providerId;

    public requestModel(String userLocation, String serviceStatus, String dateTime, String serviceDescription, String senderId, String providerId) {
        this.userLocation = userLocation;
        this.serviceStatus = serviceStatus;
        this.dateTime = dateTime;
        this.serviceDescription = serviceDescription;
        this.senderId = senderId;
        this.providerId = providerId;
    }

    public requestModel(){

    }

    public String getUserLocation() {
        return userLocation;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public String getDateSent() {
        return dateTime;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getProviderId() {
        return providerId;
    }
}
