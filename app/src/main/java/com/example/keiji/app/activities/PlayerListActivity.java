package com.example.keiji.app.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;

public class PlayerListActivity extends AppCompatActivity {

    private static final Strategy STRATEGY = Strategy.P2P_STAR;

    ArrayList<String> player_list;
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

        player_list = new ArrayList<>();
        player_list.add(getIntent().getStringExtra("player_name"));
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_list_element, player_list);

        connectionsClient = Nearby.getConnectionsClient(this);

        ListView listView = (ListView) findViewById(R.id.pl_player_list);
        TextView textView = (TextView)findViewById(R.id.pl_player_name_view);
        TextView textView2 = (TextView)findViewById(R.id.pl_game_room_name_view);
        listView.setAdapter(adapter);
        textView.setText(getIntent().getStringExtra("player_name"));
        textView2.setText(getIntent().getStringExtra("game_name"));

        gname = getIntent().getStringExtra("game_name");
        startAdvertising();
    }

    private void broadcastGame() {

    }

    private void startAdvertising() {
        connectionsClient.startAdvertising(gname, serviceId, connectionLifecycleCallback, new AdvertisingOptions(STRATEGY));
        Log.d("startAdvertising", "Started Advertising");
    }
}