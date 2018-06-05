package com.example.keiji.app.objects;

import java.io.Serializable;
import java.util.List;

public class InitMessage implements Serializable{
    private List<String> player_list;
    private long roundLength;

    public InitMessage(List<String> player_list, long roundLength) {
        this.player_list = player_list;
        this.roundLength = roundLength;
    }

    public List<String> getPlayer_list() {
        return player_list;
    }

    public long getRoundLength() {
        return roundLength;
    }
}
