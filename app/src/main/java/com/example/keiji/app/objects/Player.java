package com.example.keiji.app.objects;

import java.util.HashMap;

/**
 * Created by keiji on 4/22/2018.
 */

public class Player {
    private String name;
    private String role;
    private int id;
    private HashMap<Integer, Player> playerMap;

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
}
