package com.ashcollege.entities;

import javax.persistence.OneToMany;
import java.util.List;

public class Play {
    private int id;
    private List<Phase> phases;
    private User owner;

    public Play() {
    }

    public Play(List<Phase> phases) {
        this.phases = phases;
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
