package com.example.keiji.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class CreateGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

    }

    protected void playerList(View v) {
        Intent gl_intent = new Intent(CreateGameActivity.this, PlayerListActivity.class);
        /* String player_text = ((EditText)findViewById(R.id.cg_player_name_text)).getText().toString();
        String game_text = ((EditText)findViewById(R.id.cg_game_name_text)).getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("player_name", player_text);
        bundle.putString("game_name", game_text);
        gl_intent.putExtras(bundle); */
        startActivity(gl_intent);
    }
}
