package com.example.keiji.app.objects;

import java.io.Serializable;

public class NominateMessage implements Serializable {
    private String nominatedPlayer;

    public NominateMessage(String nominatedPlayer) {
        this.nominatedPlayer = nominatedPlayer;
    }

    public String getNominatedPlayer() {
        return this.nominatedPlayer;
    }
}
