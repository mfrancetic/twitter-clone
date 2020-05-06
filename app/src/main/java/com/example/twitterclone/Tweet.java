package com.example.twitterclone;

public class Tweet {

    private String username;
    private String tweet;

    public Tweet(String username, String tweet) {
        this.username = username;
        this.tweet = tweet;
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
}