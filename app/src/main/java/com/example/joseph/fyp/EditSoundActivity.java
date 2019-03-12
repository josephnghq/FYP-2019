package com.example.joseph.fyp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EditSoundActivity extends AppCompatActivity {



    private CheckBox oscSawRadioButton;
    private CheckBox oscSineRadioButton;
    private CheckBox oscTriRadioButton;
    private CheckBox oscSqrRadioButton;
    private CheckBox delayCheckbox;
    private CheckBox filterADSRCheckbox;


    private CheckBox oscSawRadioButton2;
    private CheckBox oscSineRadioButton2;
    private CheckBox oscTriRadioButton2;
    private CheckBox oscSqrRadioButton2;

    private CheckBox enablePM;
    private CheckBox enableLFO;
    private CheckBox enablePhaseShifter;



    private Synth mSynth;
    private Button btnPlayTestPitch;
    private Button btnPlay;
    private Button btnStop;
    private Button btnSave;


    private RadioButton radioHighPass;
    private RadioButton radioLowPass;
    private SeekBar detune;
    private SeekBar filterQ;
    private SeekBar filterValue;
    private SeekBar seekBarAttack;
    private SeekBar seekBarDecay;
    private SeekBar seekBarSustain;
    private SeekBar seekBarRelease;

    private SeekBar PMfreq;
    private SeekBar PMamp;

    private SeekBar LFOfreq;
    private SeekBar LFOamp;

    private SeekBar phaseShifterFreq;
    private SeekBar phaseShifterAmp;
    private SeekBar phaseShifterFeedback;
    private SeekBar phaseShifterDepth;


    private SeekBar seekBarAttackLevel;
    private SeekBar seekBarDecayLevel;
    private SeekBar seekBarSustainLevel;
    private SeekBar seekBarReleaseLevel;




    private SeekBar seekBarAttackFilter;
    private SeekBar seekBarDecayFilter;
    private SeekBar seekBarSustainFilter;
    private SeekBar seekBarReleaseFilter;


    private SeekBar seekBarAttackFilterLevel;
    private SeekBar seekBarDecayFilterLevel;
    private SeekBar seekBarSustainFilterLevel;
    private SeekBar seekBarReleaseFilterLevel;



    private SeekBar detune2;


    private double durationAttack = 0.5;
    private double durationDecay = 0.5;
    private double durationSustain = 0.5;
    private double durationRelease = 0.5;


    private double durationAttackFilter = 0.5;
    private double durationDecayFilter = 0.5;
    private double durationSustainFilter = 0.5;
    private double durationReleaseFilter = 0.5;


    private GsonBuilder builder = new GsonBuilder();
    private Gson gson;

    private ArrayList<SynthData> listOfSynthData = new ArrayList<SynthData>();

    private String title = "";


    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sound);
        sharedPref=  this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        gson = builder.create();
        String gsonString = sharedPref.getString(getString(R.string.preference_file_key) , "");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if(gsonString.length()>0){
            listOfSynthData = gson.fromJson(gsonString,new TypeToken<ArrayList<SynthData>>(){}.getType());
        }


        Log.i("FYP" , "Length of listOfSynthData is " + String.valueOf(listOfSynthData.size()));

        Log.i("FYP" , gsonString);


        mSynth = new Synth();
        btnPlayTestPitch = (Button)findViewById(R.id.play_test_pitch_button);
        btnPlayTestPitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSynth.Dmin();
            }
        });
        btnPlay = (Button)findViewById(R.id.play_osc_button);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSynth.playOsc();
            }
        });
        btnStop = (Button)findViewById(R.id.end_osc_button);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSynth.releaseOsc();
            }
        });

        btnSave = (Button)findViewById(R.id.save_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SynthData sd;
                buildAlertDialog();
             /*   sd = mSynth.saveData();
                sd.title = title;
                listOfSynthData.add(sd);
                editor.putString(getString(R.string.preference_file_key), gson.toJson(listOfSynthData));
                editor.commit();*/

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

        oscSawRadioButton2 = (CheckBox)findViewById(R.id.osc_sawtooth2);
        oscSawRadioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = oscSawRadioButton2.isChecked();

                if(checked){


                    mSynth.enableSaw2();

                }
                else{
                    mSynth.disableSaw2();
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

        oscSineRadioButton2 = (CheckBox)findViewById(R.id.osc_sine2);
        oscSineRadioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = oscSineRadioButton2.isChecked();

                if(checked){


                    mSynth.enableSine2();

                }
                else{
                    mSynth.disableSine2();
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

        oscSqrRadioButton2 = (CheckBox)findViewById(R.id.osc_square2);
        oscSqrRadioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean checked = oscSqrRadioButton2.isChecked();

                if(checked){


                    mSynth.enableSqr2();

                }
                else{
                    mSynth.disableSqr2();
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

        oscTriRadioButton2 = (CheckBox)findViewById(R.id.osc_triangle);
        oscTriRadioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = oscTriRadioButton2.isChecked();

                if(checked){


                    mSynth.enableTri2();

                }
                else{
                    mSynth.disableTri2();
                }


            }
        });

        delayCheckbox = (CheckBox)findViewById(R.id.delay_enable);
        delayCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean checked = delayCheckbox.isChecked();

                if(checked){


                    mSynth.enableDelay();

                }
                else{
                    mSynth.disableDelay();
                }


            }
        });

        enablePM = (CheckBox)findViewById(R.id.osc2_enablePM);
        enablePM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean checked = enablePM.isChecked();

                if(checked){


                    mSynth.enablePM();

                }
                else{
                    mSynth.disablePM();
                }


            }
        });


        enableLFO = (CheckBox)findViewById(R.id.LFO_1);
        enableLFO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean checked = enableLFO.isChecked();

                if(checked){


                    mSynth.enableLFO();

                }
                else{
                    mSynth.disableLFO();
                }


            }
        });

        enablePhaseShifter = (CheckBox)findViewById(R.id.phaser);
        enablePhaseShifter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = enablePhaseShifter.isChecked();

                if(checked){


                    mSynth.enablePhaseShifter();

                }
                else{
                    mSynth.disablePhaseShifter();
                }

            }
        });


        filterADSRCheckbox = (CheckBox)findViewById(R.id.enable_filter_adsr);
        filterADSRCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                boolean checked = filterADSRCheckbox.isChecked();
                if(checked){


                    mSynth.enableFilterEnv();



                }
                else{

                    mSynth.disableFilterEnv();

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


        detune = (SeekBar)findViewById(R.id.detune);
        detune.setMax(15);
        detune.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress/10;
                mSynth.setDetuneValue(value);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        filterQ = (SeekBar)findViewById(R.id.filter_q_value);
        filterQ.setMax(85);
        filterQ.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {




                double value = (double)progress/10;
                mSynth.setfreqQ(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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

        LFOfreq = (SeekBar)findViewById(R.id.LFO_1_freq);
        LFOfreq.setMax(15);

        LFOfreq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                mSynth.setLFOfreq(progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        PMfreq = (SeekBar)findViewById(R.id.PM_freq);
        PMfreq.setMax(8000);

        PMfreq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                mSynth.setPMfreq(progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        PMamp = (SeekBar) findViewById(R.id.PM_amp);
        PMamp.setMax(100);
        PMamp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress/100;
                mSynth.setPMamp(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        LFOamp = (SeekBar) findViewById(R.id.LFO_1_amp);
        LFOamp.setMax(200);
        LFOamp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress/100;
                mSynth.setLFOamp(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        phaseShifterFreq = (SeekBar) findViewById(R.id.phaser_freq);
        phaseShifterFreq.setMax(100);
        phaseShifterFreq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress/100;
                mSynth.setPhaserShifterfreq(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        phaseShifterAmp = (SeekBar) findViewById(R.id.phaser_amp);
        phaseShifterAmp.setMax(200);
        phaseShifterAmp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress/100;
                mSynth.setPhaseShifteramp(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        phaseShifterFeedback = (SeekBar) findViewById(R.id.phaser_feedback);
        phaseShifterFeedback.setMax(100);
        phaseShifterFeedback.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress/100;
                mSynth.setPhaseShifterfeedback(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        phaseShifterDepth = (SeekBar) findViewById(R.id.phaser_depth);
        phaseShifterDepth.setMax(100);
        phaseShifterDepth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress/100;
                mSynth.setPhaserShifterdepth(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });








        seekBarAttack = (SeekBar)findViewById(R.id.env_attack);
        seekBarAttack.setMax(400);
        seekBarAttack.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress / 100;
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
        seekBarDecay.setMax(400);
        seekBarDecay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress / 100;
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
        seekBarSustain.setMax(400);
        seekBarSustain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value =(double) progress / 100;
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
        seekBarRelease.setMax(400);
        seekBarRelease.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress / 100;
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


        seekBarAttackLevel = (SeekBar) findViewById(R.id.env_attack_lvl);
        seekBarAttackLevel.setMax(100);
        seekBarAttackLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress/100;
                mSynth.setEnv_attack_value(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        seekBarDecayLevel = (SeekBar) findViewById(R.id.env_decay_lvl);
        seekBarDecayLevel.setMax(100);
        seekBarDecayLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress/100;
                mSynth.setEnv_decay_value(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBarSustainLevel = (SeekBar) findViewById(R.id.env_sustain_lvl);
        seekBarSustainLevel.setMax(100);
        seekBarSustainLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress/100;
                mSynth.setEnv_sustain_value(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarReleaseLevel = (SeekBar) findViewById(R.id.env_release_lvl);
        seekBarReleaseLevel.setMax(100);
        seekBarReleaseLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress/100;
                mSynth.setEnv_release_value(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBarAttackFilter = (SeekBar) findViewById(R.id.env_attack_filter);
        seekBarAttackFilter.setMax(400);
        seekBarAttackFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress / 100;
                durationAttackFilter = value;
                mSynth.setADSRFilter(value,durationDecayFilter,durationSustainFilter,durationReleaseFilter);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        seekBarDecayFilter = (SeekBar) findViewById(R.id.env_decay_filter);
        seekBarDecayFilter.setMax(400);
        seekBarDecayFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress / 100;
                durationDecayFilter = value;
                mSynth.setADSRFilter(durationAttackFilter,value,durationSustainFilter,durationReleaseFilter);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarSustainFilter = (SeekBar) findViewById(R.id.env_sustain_filter);
        seekBarSustainFilter.setMax(400);
        seekBarSustainFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress / 100;
                durationSustainFilter = value;
                mSynth.setADSRFilter(durationAttackFilter,durationDecayFilter,value,durationReleaseFilter);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBarReleaseFilter = (SeekBar) findViewById(R.id.env_release_filter);
        seekBarReleaseFilter.setMax(400);
        seekBarReleaseFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress / 100;
                durationReleaseFilter = value;
                mSynth.setADSRFilter(durationAttackFilter,durationDecayFilter,durationSustainFilter,value);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBarAttackFilterLevel = (SeekBar) findViewById(R.id.env_attack_lvl_filter);
        seekBarAttackFilterLevel.setMax(20000);
        seekBarAttackFilterLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                mSynth.setEnv_attack_value_filter(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        seekBarDecayFilterLevel = (SeekBar) findViewById(R.id.env_decay_lvl_filter);
        seekBarDecayFilterLevel.setMax(20000);
        seekBarDecayFilterLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                mSynth.setEnv_decay_value_filter(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarSustainFilterLevel = (SeekBar) findViewById(R.id.env_sustain_lvl_filter);
        seekBarSustainFilterLevel.setMax(20000);
        seekBarSustainFilterLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                mSynth.setEnv_sustain_value_filter(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBarReleaseFilterLevel = (SeekBar) findViewById(R.id.env_release_lvl_filter);
        seekBarReleaseFilterLevel.setMax(20000);
        seekBarReleaseFilterLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                mSynth.setEnv_release_value_filter(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });








    }

    @Override
    protected void onDestroy() {

        mSynth.destory();
        super.onDestroy();
    }

    public void buildAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter synth name");
         final EditText et = new EditText(this);

        builder.setView(et);
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                title = et.getText().toString();
                 mSynth.title = title;
                mSynth.saveData();
/*                d = mSynth.saveData();
                sd.title = title;
                listOfSynthData.add(sd);
                editor.putString(getString(R.string.preference_file_key), gson.toJson(listOfSynthData));
                editor.commit();*/

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }









}
