package com.example.keiji.app.activities;

import android.content.Intent;
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
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class PlayerListActivity extends AppCompatActivity {

    ArrayList<String> player_list = new ArrayList<>();
    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    String pname = "";
    String gname = "";

    String serviceId = "com.example.keiji";

    ConnectionsClient connectionsClient;

    private final PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {

        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

        }
    };

    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {
            connectionsClient.acceptConnection(s, payloadCallback);
            player_list.add(connectionInfo.getEndpointName());
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
            connectionsClient.stopAdvertising();
        }

        @Override
        public void onDisconnected(@NonNull String s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        //Hide button on startup
        Button button = (Button) findViewById(R.id.pl_start_game_button);
        button.setVisibility(View.GONE);

        connectionsClient = Nearby.getConnectionsClient(this);
        //Create game object only for host
        pname = getIntent().getStringExtra("player_name");
        gname = getIntent().getStringExtra("game_name"); //TO-DO: Joining players should have gname synced to the host
        if (getIntent().getStringExtra("host").equals("yes")) {
            Game game = new Game(gname, pname);
            player_list.add(pname);
            button.setVisibility(View.VISIBLE); //enables start game button
        }

        //Display list of players
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_list_element, player_list);

        //Display current game name and player name
        ListView listView = (ListView) findViewById(R.id.pl_player_list);
        TextView textView = (TextView) findViewById(R.id.pl_player_name_view);
        TextView textView2 = (TextView) findViewById(R.id.pl_game_room_name_view);
        listView.setAdapter(adapter);
        textView.setText(pname);
        textView2.setText(gname);

        gname = getIntent().getStringExtra("game_name");
        startAdvertising();
    }

    //Move to MainGameDay Activity
    protected void startGame(View v) {
        startActivity(new Intent(PlayerListActivity.this, MainGameDay.class));
    }

    private void broadcastGame() {

    }

    private void startAdvertising() {
        connectionsClient.startAdvertising(gname, serviceId, connectionLifecycleCallback, new AdvertisingOptions(STRATEGY)).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("PlayerList", "Successfully started advertising");
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("PlayerList", "Failed to start advertising", e);
                    }
                }
        );
        Log.d("startAdvertising", "Started Advertising");
    }
}