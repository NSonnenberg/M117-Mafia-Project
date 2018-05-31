package com.example.keiji.app.objects;

import java.io.Serializable;

/**
 * Created by keiji on 4/22/2018.
 */

public class Player implements Serializable {
    private String name;
    private String role;
    private int id;
    private String connectid;
    private boolean isMafia = false;
    private boolean isDoctor = false;

    public Player(String name, int id, String connectId)
    {
        this.name = name;
        this.id = id;
        this.connectid = connectId;
    }

    public void setRole(String role) {
        this.role = role;
        if (role.equals("Mafia")) {
            isMafia = true;
        }
        else if (role.equals("Doctor")) {
            isDoctor = true;
        }
    }

    public String getRole()
    {
        return role;
    }

    public boolean isMafia() {
        return isMafia;
    }

    public boolean isDoctor() {
        return isDoctor;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getConnectId() { return connectid; }
}
