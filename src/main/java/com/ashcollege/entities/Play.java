package com.ashcollege.entities;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "plays")
public class Play {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "play", cascade = CascadeType.ALL)
    private List<Phase> phases;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;


    public Play() {
    }

    public Play(User owner, String playName) {
        this.owner = owner;
        this.name = playName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @OneToMany(mappedBy = "play")
    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
