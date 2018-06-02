package com.example.keiji.app.objects;

import java.io.Serializable;

public class MafiaMessage implements Serializable{
    private String player;

    public MafiaMessage(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return this.player;
    }
}
