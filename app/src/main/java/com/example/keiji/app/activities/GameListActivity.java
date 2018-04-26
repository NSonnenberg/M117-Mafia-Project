package com.example.keiji.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GameListActivity extends AppCompatActivity {

    String[] game_list = {"test1", "test2", "test3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_list_element, game_list);

        ListView listView = (ListView)findViewById(R.id.gl_game_list);
        listView.setAdapter(adapter);
    }
}
