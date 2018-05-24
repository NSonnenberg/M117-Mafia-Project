package com.example.keiji.app.objects;

public class PhaseChangeMessage extends Message {
    private String[] playerList;
    private String newPhase;

    public PhaseChangeMessage(String type, String newPhase, String[] playerList) {
        super(type);
        this.newPhase = newPhase;
        this.playerList = playerList;
    }

    public String getNewPhase() {
        return newPhase;
    }

    public String[] getPlayerList() {
        return playerList;
    }
}
