package com.example.keiji.app.objects;

import java.util.HashMap;

/**
 * Created by keiji on 4/22/2018.
 */

public class Game {
    private static int DAY = 0;
    private static int NIGHT = 1;

    private String gameName;
    private int numPlayers;
    private HashMap<String, Player> playerMap = new HashMap<String, Player>();

    private int phase = DAY;

    public Game(String gameName, String playerName) {
        this.gameName = gameName;

        Player hostPlayer = new Player(playerName);


    }
}
