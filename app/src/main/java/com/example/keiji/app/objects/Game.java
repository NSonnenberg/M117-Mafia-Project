package com.example.keiji.app.objects;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by keiji on 4/22/2018.
 */

public class Game implements Serializable {
    private static int DAY = 0;
    private static int NIGHT = 1;
    private static Integer maxplayerid = 0;

    private String gameName;
    private int numPlayers;
    private HashMap<Integer, Player> playerMap = new HashMap<Integer, Player>();

    private int phase = DAY;

    public Game(String gameName, String playerName) {
        this.gameName = gameName;
        Player hostPlayer = new Player(playerName, maxplayerid, "");
        playerMap.put(maxplayerid, hostPlayer);

        maxplayerid++;
    }

    public int addPlayer(String name, String connectId){
        Player player = new Player(name, maxplayerid, connectId);
        playerMap.put(maxplayerid, player);
        int currId = maxplayerid;
        maxplayerid++;
        return currId;
    }

    public Player getPlayer(int id){
        return playerMap.get(id);
    }
}
