package com.example.keiji.app.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class SearchGameActivity extends AppCompatActivity {

    private static final Strategy STRATEGY = Strategy.P2P_STAR;

    String pname = "";
    int REQUEST_LOCATION = 1;
    static String TAG = "SearchGame";
    android.app.Activity curr_activity;


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

    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String id, @NonNull ConnectionInfo connectionInfo) {
            connectionsClient.acceptConnection(id, payloadCallback);
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
            Log.d("tag","connection successful");
            connectionsClient.stopDiscovery();
            Intent sg_intent = new Intent(SearchGameActivity.this, MainGameActivity.class);
            sg_intent.putExtra("player_name", pname);
            sg_intent.putExtra("host", false);
            startActivity(sg_intent);
        }

        @Override
        public void onDisconnected(@NonNull String s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_game);

        curr_activity = this;
        connectionsClient = Nearby.getConnectionsClient(this);

        pname = getIntent().getStringExtra("player_name");
        TextView textView = (TextView)findViewById(R.id.gl_player_name_view);
        textView.setText(pname);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        } else {
            startDiscovery();
        }
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
