package com.example.keiji.app.activities;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.ArrayList;

public class GameListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final Strategy STRATEGY = Strategy.P2P_STAR;

    ArrayList<String> game_list;
    String pname = "";

    String serviceId = "com.example.keiji";

    ConnectionsClient connectionsClient;

    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String s, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
            Log.d("GameListActivity", "Endpoint found, connecting to device");
            connectionsClient.requestConnection(pname, serviceId, connectionLifecycleCallback);
        }

        @Override
        public void onEndpointLost(@NonNull String s) {

        }
    };

    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {

        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
            connectionsClient.stopDiscovery();
            Log.d("tag","connection successful");
        }

        @Override
        public void onDisconnected(@NonNull String s) {

        }
    };

   //This activity is temporary.  Major changes need to be made so that only a single game is searched at a time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        connectionsClient = Nearby.getConnectionsClient(this);
        game_list = new ArrayList<>();
        game_list.add("test1");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_list_element, game_list);

        ListView listView = (ListView)findViewById(R.id.gl_game_list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        pname = getIntent().getStringExtra("player_name");
        TextView textView = (TextView)findViewById(R.id.gl_player_name_view);
        textView.setText(pname);

        startDiscovery();
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Intent gl_intent = new Intent(GameListActivity.this, PlayerListActivity.class);
        gl_intent.putExtra("player_name", pname);
        gl_intent.putExtra("game_name", game_list.get(position));
        gl_intent.putExtra("host", "no");
        startActivity(gl_intent);
    }

    private void startDiscovery() {
        connectionsClient.startDiscovery(serviceId, endpointDiscoveryCallback, new DiscoveryOptions(STRATEGY));
        Log.d("GameListActivity)", "started discovery");
    }
}
