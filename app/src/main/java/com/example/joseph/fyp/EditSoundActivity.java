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



    private CheckBox oscSawRadioButton;
    private CheckBox oscSineRadioButton;
    private CheckBox oscTriRadioButton;
    private CheckBox oscSqrRadioButton;
    private Synth mSynth;
    private Button btnPlayTestPitch;
    private RadioButton radioHighPass;
    private RadioButton radioLowPass;
    private SeekBar filterValue;
    private SeekBar seekBarAttack;
    private SeekBar seekBarDecay;
    private SeekBar seekBarSustain;
    private SeekBar seekBarRelease;

    private double durationAttack = 0.5;
    private double durationDecay = 0.5;
    private double durationSustain = 0.5;
    private double durationRelease = 0.5;






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

        seekBarAttack = (SeekBar)findViewById(R.id.env_attack);
        seekBarAttack.setMax(500);
        seekBarAttack.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = progress / 100;
                durationAttack = value;
                mSynth.setADSR(value,durationDecay,durationSustain,durationRelease);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        seekBarDecay = (SeekBar)findViewById(R.id.env_decay);
        seekBarDecay.setMax(500);
        seekBarDecay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = progress / 100;
                durationDecay = value;
                mSynth.setADSR(durationAttack,value,durationSustain,durationRelease);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBarSustain = (SeekBar)findViewById(R.id.env_sustain);
        seekBarSustain.setMax(500);
        seekBarSustain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = progress / 100;
                durationSustain = value;
                mSynth.setADSR(durationAttack,durationDecay,value,durationRelease);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarRelease = (SeekBar)findViewById(R.id.env_release);
        seekBarRelease.setMax(500);
        seekBarRelease.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = progress / 100;
                durationRelease = value;
                mSynth.setADSR(durationAttack,durationDecay,durationSustain,value);



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
