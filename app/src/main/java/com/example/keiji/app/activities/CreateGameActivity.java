package com.example.keiji.app.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class CreateGameActivity extends AppCompatActivity {

    private ConstraintLayout cl;
    private Spinner lengthlist;
    private int timer = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        lengthlist = (Spinner)findViewById(R.id.cg_spinner);
        lengthlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                switch(pos) {
                    case 0:
                        timer = 15000;
                        break;
                    case 1:
                        timer = 30000;
                        break;
                    case 2:
                        timer = 60000;
                        break;
                    case 3:
                        timer = 120000;
                        break;
                    case 4:
                        timer = 180000;
                        break;
                    case 5:
                        timer = 240000;
                        break;
                    case 6:
                        timer = 300000;
                        break;
                }
            }
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
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
        cg_intent.putExtra("timer", timer);
        startActivity(cg_intent);
    }


}
