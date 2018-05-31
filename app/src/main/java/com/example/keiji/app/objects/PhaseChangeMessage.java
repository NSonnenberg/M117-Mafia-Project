package com.example.keiji.app.objects;

public class PhaseChangeMessage {
    private String newPhase;
    private String[] newPlayerList;

    public PhaseChangeMessage(String newPhase, String[] newPlayerList) {
        this.newPhase = newPhase;
        this.newPlayerList = newPlayerList;
    }

    public String getNewPhase() {
        return this.newPhase;
    }

    public String[] getNewPlayerList() {
        return this.newPlayerList;
    }
}
