package com.example.joseph.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_options);

        final Intent editSoundIntent = new Intent(this, EditSoundActivity.class);
        final Intent XYPadActivity = new Intent(this, XYPadActivity.class);

        final Intent OpenCVTestActivity = new Intent(this, OpenCVTestActivity.class);
        final Intent CreateChordFragmentActivity = new Intent(this, CreateChordFragmentActivity.class);
        final Intent PlayOptionsActivity = new Intent(this , PlayOptionsActivity.class);



        final Button btn2 = (Button)findViewById(R.id.button2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(CreateChordFragmentActivity);

            }
        });




        final Button btn4 = (Button)findViewById(R.id.button4);

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(editSoundIntent);

            }
        });

    }
}
