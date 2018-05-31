package com.example.keiji.app.objects;

public class NominateMessage {
    private String nominatedPlayer;

    public NominateMessage(String nominatedPlayer) {
        this.nominatedPlayer = nominatedPlayer;
    }

    public String getNominatedPlayer() {
        return this.nominatedPlayer;
    }
}
