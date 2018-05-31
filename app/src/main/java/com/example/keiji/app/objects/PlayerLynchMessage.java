package com.example.keiji.app.objects;

public class PlayerLynchMessage {
    private String playerLynched;
    private String[] updatedPlayerList;

    public PlayerLynchMessage(String playerLynched, String[] updatedPlayerList) {
        this.playerLynched = playerLynched;
        this.updatedPlayerList = updatedPlayerList;
    }

    public String getPlayerLynched() {
        return this.playerLynched;
    }

    public String[] getUpdatedPlayerList() {
        return this.updatedPlayerList;
    }
}
