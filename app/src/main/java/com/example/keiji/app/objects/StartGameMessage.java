package com.example.keiji.app.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StartGameMessage implements Serializable {
    private Player player;
    private List<String> player_list;

    public StartGameMessage(Player player, List<String> player_list) {
        this.player = player;
        this.player_list = player_list;
    }

    public Player getPlayer() {
        return player;
    }

    public List<String> getPlayer_list() {
        return player_list;
    }
}
