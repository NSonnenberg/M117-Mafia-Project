package com.example.keiji.app.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    private TextView countdownText;
    private Button countdownButton;
    private Button countdownButtonReset;

    private CountDownTimer countdownTimer;
    private long timeLeftInMilliseconds = 300000;//5 minutes
    private boolean timerRunning;

    private TextView gmnameview;
    private TextView gmnametext;
    private TextView pnnameview;
    private TextView pnnametext;
    private TextView listtext;
    private Button startgamebutton;
    private ListView list;

    ArrayList<String> player_list = new ArrayList<>();
    HashMap<String, Player> player_map = new HashMap<String, Player>();
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
                    player = (Player) received;
                    playerListToDay();
                }

                else if (received.getClass() == NominateMessage.class) {
                    android.app.AlertDialog.Builder builder;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        builder = new android.app.AlertDialog.Builder(curr_activity, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new android.app.AlertDialog.Builder(curr_activity);
                    }
                    if (host) {
                        builder.setTitle("Nomination")
                                .setMessage("Player nominated " + ((NominateMessage) received).getNominatedPlayer() + ". Do you want to second?")
                                .setPositiveButton(android.R.string.yes, new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(android.content.DialogInterface dialog, int which) {
                                        // if yes

                                    }
                                })
                                .setNegativeButton(android.R.string.no, new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(android.content.DialogInterface dialog, int which) {
                                        // if no
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    else {
                        builder.setTitle("Nomination")
                                .setMessage("Player nominated " + ((NominateMessage) received).getNominatedPlayer() + ". Do you want to second?")
                                .setPositiveButton(android.R.string.yes, new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(android.content.DialogInterface dialog, int which) {
                                        // if yes

                                    }
                                })
                                .setNegativeButton(android.R.string.no, new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(android.content.DialogInterface dialog, int which) {
                                        // if no
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
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
            Log.d("MainGame", "Connection initiated accepting connection");
            connectionsClient.acceptConnection(id, payloadCallback);
            if (host) {
                player_list.add(connectionInfo.getEndpointName());
                player_map.put(connectionInfo.getEndpointName(), new Player(connectionInfo.getEndpointName(), id));
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
        startgamebutton = (Button)findViewById(R.id.mg_start_game_button);
        ListView list = (ListView)findViewById(R.id.mg_player_list);
        startgamebutton.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
        countdownText = findViewById(R.id.countdown_text);
        countdownButton = findViewById(R.id.countdown_button);
        countdownButtonReset = findViewById(R.id.countdown_button_reset);
        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();

            }

        });
        countdownButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }

        });
        countdownText.setVisibility(View.GONE);
        countdownButton.setVisibility(View.GONE);
        countdownButtonReset.setVisibility(View.GONE);

        gmnameview = (TextView)findViewById(R.id.mg_game_room_name_view);
        gmnametext = (TextView)findViewById(R.id.mg_game_room_name);
        pnnameview = (TextView)findViewById(R.id.mg_player_name_view);
        pnnametext = (TextView)findViewById(R.id.mg_player_name);
        listtext = (TextView)findViewById(R.id.mg_list_text);

        connectionsClient = Nearby.getConnectionsClient(this);

        pname = getIntent().getStringExtra("player_name");
        gname = getIntent().getStringExtra("game_name"); //TO-DO: Joining players should have gname synced to the host
        host = getIntent().getBooleanExtra("host", false);

        pnnameview.setText(pname);
        gmnameview.setText(gname);

        //Create game object and player object only for host
        if (host) {
            game = new Game(gname, pname);
            player = new Player(pname, "");
            player_list.add(pname);
            startgamebutton.setVisibility(View.VISIBLE); //enables start game button
            player_map.put(pname, player);
            list.setVisibility(View.VISIBLE); //enables list view
        }

        //Display list of players
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_list_element, player_list);

        p_list_adapter = adapter;

        //Perform function on clicking item in list
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d("ListListener", "Clicked item " + position + " " + id);



            }
        });

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
            Player playerObj = player_map.get(player);
            if (i == mafiaNum) {
                playerObj.setRole("Mafia");
            }
            else if (i == doctorNum) {
                playerObj.setRole("Doctor");
            }
            else {
                playerObj.setRole("Villager");
            }

            if (!player.equals(pname)) {
                try {
                    connectionsClient.sendPayload(playerObj.getConnectId(), Payload.fromBytes(SerializationHandler.serialize(playerObj))).addOnFailureListener(new OnFailureListener() {
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
        playerListToDay();
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

    //sitch from PlayerList to DayPhase
    private void playerListToDay() {
        countdownText.setVisibility(View.VISIBLE);
        countdownButton.setVisibility(View.VISIBLE);
        countdownButtonReset.setVisibility(View.VISIBLE);
        pnnametext.setVisibility(View.GONE);
        pnnameview.setVisibility(View.GONE);
        gmnametext.setText("Your role is: ");
        String role = player.getRole();
        gmnameview.setText(role);
        listtext.setText("Nominate");
        startgamebutton.setVisibility(View.GONE);
    }


    private void broadcastGame() {
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

    // Timer functions
    public void startStop() {
        if (timerRunning) {
            stopTimer();

        } else {
            startTimer();
        }
    }
    public void startTimer () {
        countdownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        countdownButton.setText("Pause");
        timerRunning = true;
    }
    public void stopTimer () {
        countdownTimer.cancel();
        timerRunning = false;
        countdownButton.setText("Start");
    }
    public void updateTimer ()
    {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;
        String timeLeftText;
        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;
        countdownText.setText(timeLeftText);
    }
    public void resetTimer()
    {
        timeLeftInMilliseconds = 300000;
        if(timerRunning)
        {
            stopTimer();
        }
        updateTimer();
        startTimer();
    }
}