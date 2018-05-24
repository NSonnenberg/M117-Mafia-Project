package com.example.keiji.app.objects;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by keiji on 4/22/2018.
 */

public class Player implements Serializable {
    private String name;
    private String role;
    private int id;
    private HashMap<Integer, Player> playerMap;
    private int connectid;

    public Player(String name, int id)
    {
        this.name = name;
        this.id = id;
    }

    public void setPlayerMap(HashMap<Integer, Player> playerMap)
    {
        this.playerMap = playerMap;
    }

    public HashMap<Integer, Player> getPlayerMap()
    {
        return playerMap;
    }

    public String getRole()
    {
        return role;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getConnectId() {return connectid; }
}
