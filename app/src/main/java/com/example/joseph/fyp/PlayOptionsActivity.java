package com.example.joseph.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_options);


        final Intent editSoundIntent = new Intent(this, EditSoundActivity.class);
        final Intent XYPadActivity = new Intent(this, XYPadActivity.class);

        final Intent OpenCVTestActivity = new Intent(this, HandGestureActivity.class);
        final Intent CreateChordFragmentActivity = new Intent(this, CreateChordFragmentActivity.class);


        final Button btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(XYPadActivity);

            }
        });

        final Button btn2 = (Button)findViewById(R.id.button2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(OpenCVTestActivity);

            }
        });




    }
}
