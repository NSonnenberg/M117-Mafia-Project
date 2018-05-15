package com.example.keiji.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.keiji.app.objects.Game;

import java.util.ArrayList;

public class PlayerListActivity extends AppCompatActivity {

    ArrayList<String> player_list = new ArrayList<>();
    String pname = "";
    String gname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        //Hide button on startup
        Button button = (Button)findViewById(R.id.pl_start_game_button);
        button.setVisibility(View.GONE);

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
        ListView listView = (ListView)findViewById(R.id.pl_player_list);
        TextView textView = (TextView)findViewById(R.id.pl_player_name_view);
        TextView textView2 = (TextView)findViewById(R.id.pl_game_room_name_view);
        listView.setAdapter(adapter);
        textView.setText(pname);
        textView2.setText(gname);
    }

    //Move to MainGameDay Activity
    protected void startGame(View v) {
        startActivity(new Intent(PlayerListActivity.this, MainGameDay.class));
    }
}