package com.example.keiji.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void createGame(View v) {
        startActivity(new Intent(MainActivity.this, CreateGameActivity.class));
    }

    protected void joinGame(View v) {
        startActivity(new Intent(MainActivity.this, JoinGameActivity.class));
    }
}
