package com.example.twitterclone;

public class Tweet {

    private String username;
    private String tweet;
    private String createdAt;
    private String objectId;

    public Tweet(String username, String tweet) {
        this.username = username;
        this.tweet = tweet;
    }

    public Tweet(String username, String tweet, String createdAt, String objectId) {
        this.username = username;
        this.tweet = tweet;
        this.createdAt = createdAt;
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getObjectId() {
        return objectId;
    }
}