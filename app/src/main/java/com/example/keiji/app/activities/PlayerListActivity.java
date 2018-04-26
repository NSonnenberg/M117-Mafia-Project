package com.example.keiji.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PlayerListActivity extends AppCompatActivity {

    String[] player_list = {"player 1", "player 2", "player 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_list_element, player_list);

        ListView listView = (ListView)findViewById(R.id.pl_player_list);
        listView.setAdapter(adapter);
    }
}
