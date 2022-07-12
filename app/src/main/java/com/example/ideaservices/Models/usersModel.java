package com.example.ideaservices.Models;

public class usersModel {
    private String firstName,lastName,userProfileImage,userPhone,userEmail,userId,lastMessage,qualification,userType;

    public usersModel(String firstName, String userProfileImage, String userPhone, String userEmail, String userId, String lastMessage, String qualification, String userType) {
        this.firstName = firstName;
        this.userProfileImage = userProfileImage;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.qualification = qualification;
        this.userType = userType;
    }

    public usersModel(String userName, String userPhone, String qualification) {
        this.firstName = userName;
        this.userPhone = userPhone;
        this.qualification = qualification;
    }

    public usersModel(){

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserId() {
        return userId;
    }

    public String getQualification() {
        return qualification;
    }


}
