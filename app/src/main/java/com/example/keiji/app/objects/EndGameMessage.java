package com.example.keiji.app.objects;

import java.io.Serializable;

public class EndGameMessage implements Serializable {
    private int winner;

    public EndGameMessage(int winner){
        this.winner = winner;
    }

    public int getWinner(){
        return winner;
    }
}
