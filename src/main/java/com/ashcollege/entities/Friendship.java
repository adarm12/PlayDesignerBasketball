package com.ashcollege.entities;


import javax.persistence.*;

@Entity
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User requester;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User accepter;
    @Column(name = "status")
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
