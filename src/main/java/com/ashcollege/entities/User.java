package com.ashcollege.entities;

import java.util.*;

public class User {
    private int id;
    private String username;
    private String password;
    private int age;
    private Map<User,Status> friends = new HashMap<>();
    private String secret;
    private List<Play> plays;

    private enum Status {
        YES,
        NO,
        IN_PROCESS
    }


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Map<User, Status> getFriends() {
        return friends;
    }

    public void setFriends(Map<User, Status> friends) {
        this.friends = friends;
    }
    public void addFriends(User user,Status success) {
        friends.put(user,success);
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<Play> getPlays() {
        return plays;
    }

    public void setPlays(List<Play> plays) {
        this.plays = plays;
    }
}
