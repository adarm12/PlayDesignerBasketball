package com.ashcollege.entities;



public class Friendship {



    private int id;
    private User requester;
    private User accepter;
    private int status; // Add this column for friendship status (active, not active, waiting)

    public Friendship() {
    }

    public Friendship(User requester, User accepter, int status) {
        this.requester = requester;
        this.accepter = accepter;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getAccepter() {
        return accepter;
    }

    public void setAccepter(User accepter) {
        this.accepter = accepter;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
