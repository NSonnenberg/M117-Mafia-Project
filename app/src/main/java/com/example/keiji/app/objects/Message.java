package com.example.keiji.app.objects;

import java.io.Serializable;

public class Message implements Serializable {
    private String message;
    private String type;

    public Message(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
