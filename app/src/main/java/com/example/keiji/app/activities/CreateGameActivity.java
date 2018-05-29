package com.example.keiji.app.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class CreateGameActivity extends AppCompatActivity {

    private ConstraintLayout cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
    }

    //move to PlayerListActivity on submit
    protected void playerList(View v) {
        Intent cg_intent = new Intent(CreateGameActivity.this, MainGameActivity.class);
        String pname = ((EditText)findViewById(R.id.cg_player_name_text)).getText().toString();
        String gname = ((EditText)findViewById(R.id.cg_game_name_text)).getText().toString();
        //Send data to next activity
        cg_intent.putExtra("player_name", pname);
        cg_intent.putExtra("game_name", gname);
        cg_intent.putExtra("host", true);
        startActivity(cg_intent);
    }
}
