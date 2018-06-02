package com.example.keiji.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class YouWinActivity extends AppCompatActivity {

    private int winner;
    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_win);
        winner = getIntent().getIntExtra("winner", 0);
        textview = (TextView)findViewById(R.id.yw_message);
        if(winner == 0)
            textview.setText("Mafia Wins!");
        else if(winner == 1)
            textview.setText("Village Wins!");
    }
}
