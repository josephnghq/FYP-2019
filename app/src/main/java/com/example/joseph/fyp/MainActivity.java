package com.example.joseph.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {


    boolean onOff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent editSoundIntent = new Intent(this, EditSoundActivity.class);
        final Intent XYPadActivity = new Intent(this, XYPadActivity.class);

        final Intent OpenCVTestActivity = new Intent(this, OpenCVTestActivity.class);


        final Button btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(editSoundIntent);

            }
        });

        final Button btn2 = (Button)findViewById(R.id.button2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(XYPadActivity);

            }
        });


        final Button btn3 = (Button)findViewById(R.id.button3);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(OpenCVTestActivity);

            }
        });









    }
}
