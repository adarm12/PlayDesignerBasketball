package com.ashcollege.entities;

public class Phase {
    private int id;
    private int order;
    private Play play;
    private PlayerPhase player1;
    private PlayerPhase player2;
    private PlayerPhase player3;
    private PlayerPhase player4;
    private PlayerPhase player5;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getOrder() {
        return order;
    }

    public void setOrder(int phaseOrder) {
        this.order = phaseOrder;
    }


    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public PlayerPhase getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerPhase player1) {
        this.player1 = player1;
    }

    public PlayerPhase getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerPhase player2) {
        this.player2 = player2;
    }

    public PlayerPhase getPlayer3() {
        return player3;
    }

    public void setPlayer3(PlayerPhase player3) {
        this.player3 = player3;
    }

    public PlayerPhase getPlayer4() {
        return player4;
    }

    public void setPlayer4(PlayerPhase player4) {
        this.player4 = player4;
    }

    public PlayerPhase getPlayer5() {
        return player5;
    }

    public void setPlayer5(PlayerPhase player5) {
        this.player5 = player5;
    }
}
