package com.example.ideaservices.Models;

public class reviewModel {
    private String review,reviewDate,senderId;

    public reviewModel(String reviewText, String reviewDate, String senderId) {
        this.review = reviewText;
        this.reviewDate = reviewDate;
        this.senderId = senderId;
    }

    public reviewModel(String reviewText) {
        this.review = reviewText;
    }

    public reviewModel(){}

    public String getReviewText() {
        return review;
    }

    public void setReviewText(String reviewText) {
        this.review = reviewText;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewTimestamp) {
        this.reviewDate = reviewTimestamp;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String userId) {
        this.senderId = userId;
    }
}
