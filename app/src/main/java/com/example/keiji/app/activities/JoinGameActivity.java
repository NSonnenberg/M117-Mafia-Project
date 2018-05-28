package com.example.keiji.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class JoinGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
    }

    //Move to GameListActivity on submit
    protected void gameList(View v) {
        Intent jg_intent = new Intent(JoinGameActivity.this, SearchGameActivity.class);
        String pname = ((EditText)findViewById(R.id.jg_player_name_text)).getText().toString();
        //Send data to next activity
        jg_intent.putExtra("player_name", pname);
        startActivity(jg_intent);
    }
}
