package com.example.keiji.app.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.keiji.app.objects.Game;
import com.example.keiji.app.objects.Message;
import com.example.keiji.app.objects.NominateMessage;
import com.example.keiji.app.objects.Player;
import com.example.keiji.app.utilities.SerializationHandler;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainGameActivity extends AppCompatActivity {

    private static final int PLAYER_LIST = 0;
    private static final int SEARCH = 1;
    private static final int DAY = 2;
    private static final int NIGHT = 3;

    ArrayList<String> player_list = new ArrayList<>();
    HashMap<String, Integer> player_map = new HashMap<String, Integer>();
    Player player;
    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    String pname = "";
    String gname = "";
    boolean host = false;
    Game game;
    int REQUEST_LOCATION = 1;
    android.app.Activity curr_activity;

    int mode;

    ArrayAdapter p_list_adapter;

    static String TAG = "MainGameActivity";

    String serviceId = "com.example.keiji";

    ConnectionsClient connectionsClient;

    private final PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            Object received = null;
            try {
                received = SerializationHandler.deserialize(payload.asBytes());
            } catch (IOException | ClassNotFoundException e) {
                Log.d(TAG, e.getMessage());
            }

            if (received == null) {
                Log.d(TAG, "Messaged was not received");
            }

            else {
                if (received.getClass() == Player.class) {
                    Log.d(TAG, "Received player object from " + s + ". Their role was: " + ((Player) received).getRole());
                }

                else if (received.getClass() == NominateMessage.class) {
                    if (host) {

                    }
                    else {

                    }
                }
            }
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

        }
    };

    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String id, @NonNull ConnectionInfo connectionInfo) {
            connectionsClient.stopAdvertising();
            Log.d("MainGame", "Connection initiated accepting connection");
            connectionsClient.acceptConnection(id, payloadCallback);
            if (host) {
                player_list.add(connectionInfo.getEndpointName());
                player_map.put(connectionInfo.getEndpointName(), game.addPlayer(connectionInfo.getEndpointName(), id));
                p_list_adapter.notifyDataSetChanged();
                Log.d(TAG, "Accepted connection player_list is now " + player_list.get(1));
            }
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
            Log.d(TAG, "Connection established");
        }

        @Override
        public void onDisconnected(@NonNull String s) {

        }
    };

    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String id, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
            connectionsClient.stopDiscovery();
            Log.d(TAG, id);
            Log.d(TAG, "Endpoint found with serviceId: " + discoveredEndpointInfo.getServiceId());
            Log.d("SearchGameActivity", "Endpoint found, connecting to device");

            final String endpointId = id;
            android.app.AlertDialog.Builder builder;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                builder = new android.app.AlertDialog.Builder(curr_activity, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new android.app.AlertDialog.Builder(curr_activity);
            }
            builder.setTitle("Make Connection")
                    .setMessage("Do you want to connect to this player?")
                    .setPositiveButton(android.R.string.yes, new android.content.DialogInterface.OnClickListener() {
                        public void onClick(android.content.DialogInterface dialog, int which) {
                            // continue with connection
                            connectionsClient.requestConnection(pname, endpointId, connectionLifecycleCallback).addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Successfully requested connection");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Log.d(TAG, "Failed to request connection", e);
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, new android.content.DialogInterface.OnClickListener() {
                        public void onClick(android.content.DialogInterface dialog, int which) {
                            // continue with discovery
                            startDiscovery();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

        @Override
        public void onEndpointLost(@NonNull String s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        curr_activity = this;

        //Hide button and listview on startup
        Button button = (Button)findViewById(R.id.mg_start_game_button);
        ListView list = (ListView)findViewById(R.id.mg_player_list);
        button.setVisibility(View.GONE);
        list.setVisibility(View.GONE);

        connectionsClient = Nearby.getConnectionsClient(this);

        pname = getIntent().getStringExtra("player_name");
        gname = getIntent().getStringExtra("game_name"); //TO-DO: Joining players should have gname synced to the host
        host = getIntent().getBooleanExtra("host", false);

        //Create game object and player object only for host
        if (host) {
            game = new Game(gname, pname);
            player = new Player(pname, 0, "");
            player_list.add(pname);
            player_map.put(pname, 0);
            button.setVisibility(View.VISIBLE); //enables start game button
            list.setVisibility(View.VISIBLE); //enables list view
        }
        //TODO: Else create player object
        else {
            player = new Player(pname, 1, "");
            player_list.add(pname);
        }

        //Display list of players
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_list_element, player_list);

        p_list_adapter = adapter;

        //Display current game name and player name
        ListView listView = (ListView) findViewById(R.id.mg_player_list);
        TextView textView = (TextView) findViewById(R.id.mg_player_name_view);
        TextView textView2 = (TextView) findViewById(R.id.mg_game_room_name_view);
        listView.setAdapter(adapter);
        textView.setText(pname);
        textView2.setText(gname);

        gname = getIntent().getStringExtra("game_name");

        if (host) {
            mode = PLAYER_LIST;
            startAdvertising();
        } else {
            mode = SEARCH;
            startDiscovery();
        }
    }

    // Change MainGameActivity configuration for running the game
    protected void startGame(View v) {
        connectionsClient.stopAdvertising();
        int numPlayers = player_map.keySet().size();
        Log.d(TAG, "numPlayers = " + numPlayers);
        Random rand = new Random();
        int mafiaNum = rand.nextInt(numPlayers);
        int doctorNum = -1;
        if  (numPlayers >  2) {
            while (mafiaNum == doctorNum) {
                doctorNum = rand.nextInt(numPlayers);
            }
        }

        int i = 0;
        for (String player : player_map.keySet()) {
            Player currPlayer = game.getPlayer(player_map.get(player));
            if (i == mafiaNum) {
                currPlayer.setRole("Mafia");
            }
            else if (i == doctorNum) {
                currPlayer.setRole("Doctor");
            }
            else {
                currPlayer.setRole("Villager");
            }

            if (!player.equals(pname)) {
                try {
                    connectionsClient.sendPayload(currPlayer.getConnectId(), Payload.fromBytes(SerializationHandler.serialize(currPlayer))).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, e.getMessage());
                        }
                    });
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            i++;
        }

        mode = DAY;
        /*
        Intent pl_intent = new Intent(MainGameActivity.this, MainGameDay.class);
        boolean host = getIntent().getBooleanExtra("host", false);
        if (host) {
            pl_intent.putExtra("Game", (Serializable)game);
        }
        pl_intent.putExtra("host", host);
        startActivity(pl_intent);
        */
    }

    private void startAdvertising() {
        connectionsClient.startAdvertising(gname, serviceId, connectionLifecycleCallback, new AdvertisingOptions(STRATEGY)).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MainGame", "Successfully started advertising");
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MainGame", "Failed to start advertising", e);
                    }
                }
        );
        Log.d("startAdvertising", "Started Advertising");
    }

    private void startDiscovery() {
        connectionsClient.startDiscovery(serviceId, endpointDiscoveryCallback, new DiscoveryOptions(STRATEGY)).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("SearchGame","Successfully started discovery");
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("SearchGame", "Failed to start discovery", e);
                    }
                }
        );
        Log.d("SearchGameActivity)", "started discovery");
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDiscovery();
            }
        } else {
            Log.d("SearchGame", "Guess we're not running the game");
        }
    }
}