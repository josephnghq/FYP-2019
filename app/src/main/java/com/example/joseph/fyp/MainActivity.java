package com.example.joseph.fyp;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    boolean onOff = false;
    static RoomMyDatabase db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent editSoundIntent = new Intent(this, EditSoundActivity.class);
        final Intent XYPadActivity = new Intent(this, XYPadActivity.class);

        final Intent OpenCVTestActivity = new Intent(this, OpenCVTestActivity.class);
        final Intent CreateChordFragmentActivity = new Intent(this, CreateChordFragmentActivity.class);
        final Intent PlayOptionsActivity = new Intent(this , PlayOptionsActivity.class);
        final Intent CreateOptionsActivity = new Intent(this , CreateOptionsActivity.class);



        final Button btn2 = (Button)findViewById(R.id.button2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(PlayOptionsActivity);

            }
        });




        final Button btn4 = (Button)findViewById(R.id.button4);

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(CreateOptionsActivity);

            }
        });

        RoomSingleton.buildDb(this);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i("FYP" , "Size of db is " + RoomSingleton.db.roomDAO().getAll().size());

            }
        }).start();













    }
}
