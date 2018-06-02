package com.example.keiji.app.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerLynchMessage implements Serializable {
    private String playerLynched;
    private List<String> updatedPlayerList;

    public PlayerLynchMessage(String playerLynched, List<String> updatedPlayerList) {
        this.playerLynched = playerLynched;
        this.updatedPlayerList = updatedPlayerList;
    }

    public String getPlayerLynched() {
        return this.playerLynched;
    }

    public List<String> getUpdatedPlayerList() {
        return this.updatedPlayerList;
    }
}
