package com.example.keiji.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GameListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

   ArrayList<String> game_list;
   String pname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

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
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Intent gl_intent = new Intent(GameListActivity.this, PlayerListActivity.class);
        gl_intent.putExtra("player_name", pname);
        gl_intent.putExtra("game_name", game_list.get(position));
        startActivity(gl_intent);
    }
}
