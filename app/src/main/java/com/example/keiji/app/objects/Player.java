package com.example.keiji.app.objects;

/**
 * Created by keiji on 4/22/2018.
 */

public class Player {
    private String name;
    private String role;
    private HashMap<String, Player> playerMap;

    public Player(String name)
    {
        this.name = name;
    }

    public void setPlayerMap(HashMap<String, Player> playerMap)
    {
        this.playerMap = playerMap;
    }

    public HashMap<String, Player> getPlayerMap()
    {
        return playerMap;
    }

    public int getId() {
        return id;
    }

    public String getRole()
    {
        return role;
    }

    public String getName() {
        return name;
    }
}
