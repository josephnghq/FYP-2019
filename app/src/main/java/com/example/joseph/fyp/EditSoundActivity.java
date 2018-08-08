package com.example.joseph.fyp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;

public class EditSoundActivity extends AppCompatActivity {



    CheckBox oscSawRadioButton;
    CheckBox oscSineRadioButton;
    CheckBox oscTriRadioButton;
    CheckBox oscSqrRadioButton;
    Synth mSynth;
    Button btnPlayTestPitch;
    RadioButton radioHighPass;
    RadioButton radioLowPass;
    SeekBar filterValue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sound);
        mSynth = new Synth();

        btnPlayTestPitch = (Button)findViewById(R.id.play_test_pitch_button);
        btnPlayTestPitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSynth.Dmin();
            }
        });

        oscSawRadioButton = (CheckBox)findViewById(R.id.osc_sawtooth);
        oscSawRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = oscSawRadioButton.isChecked();

                if(checked){


                    mSynth.enableSaw();

                }
                else{
                    mSynth.disableSaw();
                }




            }
        });


        oscSineRadioButton = (CheckBox)findViewById(R.id.osc_sine);
        oscSineRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = oscSineRadioButton.isChecked();

                if(checked){


                    mSynth.enableSine();

                }
                else{
                    mSynth.disableSine();
                }


            }
        });

        oscSqrRadioButton = (CheckBox)findViewById(R.id.osc_square);
        oscSqrRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean checked = oscSqrRadioButton.isChecked();

                if(checked){


                    mSynth.enableSqr();

                }
                else{
                    mSynth.disableSqr();
                }


            }
        });

        oscTriRadioButton = (CheckBox)findViewById(R.id.osc_triangle);
        oscTriRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = oscTriRadioButton.isChecked();

                if(checked){


                    mSynth.enableTri();

                }
                else{
                    mSynth.disableTri();
                }


            }
        });

        radioLowPass = (RadioButton)findViewById(R.id.radio_lowpass);
        radioLowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean checked = radioLowPass.isChecked();

                if(checked){


                     mSynth.selectLowPass();

                }
                else{

                }



            }
        });


        radioHighPass = (RadioButton)findViewById(R.id.radio_hipass);
        radioHighPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean checked = radioHighPass.isChecked();

                if(checked){


                    mSynth.selectHighPass();

                }
                else{

                }



            }
        });


        filterValue = (SeekBar)findViewById(R.id.filter_value);
        filterValue.setMax(20000);

        filterValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mSynth.setFilterValue(progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });






    }
}
