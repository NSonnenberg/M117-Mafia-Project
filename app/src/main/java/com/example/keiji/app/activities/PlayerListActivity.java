package com.example.keiji.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerListActivity extends AppCompatActivity {

    ArrayList<String> player_list;
    String pname = "";
    String gname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        player_list = new ArrayList<>();
        player_list.add(getIntent().getStringExtra("player_name"));
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_list_element, player_list);

        ListView listView = (ListView) findViewById(R.id.pl_player_list);
        TextView textView = (TextView)findViewById(R.id.pl_player_name_view);
        TextView textView2 = (TextView)findViewById(R.id.pl_game_room_name_view);
        listView.setAdapter(adapter);
        textView.setText(getIntent().getStringExtra("player_name"));
        textView2.setText(getIntent().getStringExtra("game_name"));
    }
}