package com.example.keiji.app.objects;

import java.io.Serializable;

public class DoctorMessage implements Serializable {
    private String player;

    public DoctorMessage(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return this.player;
    }
}
