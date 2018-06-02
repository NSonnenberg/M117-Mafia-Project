package com.example.keiji.app.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhaseChangeMessage implements Serializable {
    private int newPhase;
    private List<String> newPlayerList;

    public PhaseChangeMessage(int newPhase, List<String> newPlayerList) {
        this.newPhase = newPhase;
        this.newPlayerList = newPlayerList;
    }

    public int getNewPhase() {
        return this.newPhase;
    }

    public List<String> getNewPlayerList() {
        return this.newPlayerList;
    }
}
