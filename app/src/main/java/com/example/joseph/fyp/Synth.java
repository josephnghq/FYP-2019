package com.example.joseph.fyp;

import android.util.Log;

import com.jsyn.JSyn;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.unitgen.FilterHighPass;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.InterpolatingDelay;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.VariableRateMonoReader;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joseph on 7/12/2018.
 */



public class Synth

{
    private com.jsyn.Synthesizer mSynth = JSyn.createSynthesizer(new AndroidAudioForJSyn());

   /* private UnitOscillator mSawOsc = new SawtoothOscillatorBL();
    private UnitOscillator mSawOsc2 = new SawtoothOscillatorBL();
    private UnitOscillator mSawOsc3 = new SawtoothOscillatorBL();

*/
    // give 3 voices first



    public double DELAY_TIME = 0.3;
    private int num_of_delay_voices = 7;

    //give 4 delay voices, delays are connected from the output of filters, and output of delay goes straight into line out
   /* private InterpolatingDelay mDelayVoice1 = new InterpolatingDelay();
    private InterpolatingDelay mDelayVoice2 = new InterpolatingDelay();
    private InterpolatingDelay mDelayVoice3 = new InterpolatingDelay();
    private InterpolatingDelay mDelayVoice4 = new InterpolatingDelay();*/


    private InterpolatingDelay[] mDelays = new InterpolatingDelay[num_of_delay_voices];
    private FilterLowPass[] mDelaysFilter = new FilterLowPass[num_of_delay_voices];


    private double FREQUENCY_NOW = 440;
    private double freqDiff = 0;
    private double freqDiffSliderFactor;

    private double globalAmp = 0.1;

    private boolean ENABLE_PORTA = true;
    private boolean DELAY_ENABLED = true;
    private boolean ENABLE_LOW_PASS = true;
    private boolean ENABLE_HIGH_PASS = false;
    private boolean ENABLE_FILTER_ADSR = true;

    private int unison = 4;
    private int polyphony = 10;

    private int noOfOscVoice = unison * polyphony;


    private ArrayList<Oscs> mOscSawArray;
    private ArrayList<Oscs> mOscSineArray;
    private ArrayList<Oscs> mOscSqrArray;
    private ArrayList<Oscs> mOscTriArray;


    private SawtoothOscillatorBL mOscSaw = new SawtoothOscillatorBL();
    private SawtoothOscillatorBL mOscSaw2 = new SawtoothOscillatorBL();
    private SawtoothOscillatorBL mOscSaw3 = new SawtoothOscillatorBL();


    private SineOscillator mOscSine = new SineOscillator();
    private SineOscillator mOscSine2 = new SineOscillator();
    private SineOscillator mOscSine3 = new SineOscillator();

    private SquareOscillator mOscSqr = new SquareOscillator();
    private SquareOscillator mOscSqr2 = new SquareOscillator();
    private SquareOscillator mOscSqr3 = new SquareOscillator();

    private TriangleOscillator mOscTri = new TriangleOscillator();
    private TriangleOscillator mOscTri2 = new TriangleOscillator();
    private TriangleOscillator mOscTri3 = new TriangleOscillator();


    private boolean DisableSaw = false;
    private boolean DisableSine = true;
    private boolean DisableSqr = true;
    private boolean DisableTri = true;


    private LineOut mOut = new LineOut();
    private FilterLowPass mLowPassFilter = new FilterLowPass();
    private FilterHighPass mHighPassFilter = new FilterHighPass();

    private double env_attack_duration = 0.6;
    private double env_decay_duration = 0.3;
    private double env_sustain_duration = 1;
    private double env_release_duration = 1.3;

    private double env_attack_value = 0.4;
    private double env_decay_value = 0.25;
    private double env_sustain_value = 0.2;
    private double env_release_value = 0;



    private double env_attack_duration_filter = 0.3;
    private double env_decay_duration_filter = 0.5;
    private double env_sustain_duration_filter = 0.3;
    private double env_release_duration_filter = 0.1;

    private double env_attack_value_filter = 16000;
    private double env_decay_value_filter = 8000;
    private double env_sustain_value_filter = 3000;
    private double env_release_value_filter = 2500;


    private double detuneValue = 0.2;

/*
    private InterpolatingDelay mDelayUnit = new InterpolatingDelay();
*/





    private  double[] envVol =
            {       env_attack_duration , env_attack_value,
                    env_decay_duration , env_decay_value,
                    env_sustain_duration , env_sustain_value,
                    env_release_duration , env_release_value
            };



    private  double[] envFilter =
            {       env_attack_duration_filter , env_attack_value_filter,
                    env_decay_duration_filter , env_decay_value_filter,
                    env_sustain_duration_filter , env_sustain_value_filter,
                    env_release_duration_filter , env_release_value_filter
            };





    private SegmentedEnvelope envForVol = new SegmentedEnvelope(envVol);
    private VariableRateMonoReader envPlayer = new VariableRateMonoReader();
    private SegmentedEnvelope envForFilter = new SegmentedEnvelope(envFilter);
    private VariableRateMonoReader envPlayerFilter = new VariableRateMonoReader();










    public Synth(){





        setUpOscsArray();

/*
        mSynth.add(mOscSine);
        mSynth.add(mOscSine2);
        mSynth.add(mOscSine3);

        mSynth.add(mOscSqr);
        mSynth.add(mOscSqr2);
        mSynth.add(mOscSqr3);

        mSynth.add(mOscTri);
        mSynth.add(mOscTri2);
        mSynth.add(mOscTri3);

        mSynth.add(mOscSaw);
        mSynth.add(mOscSaw2);
        mSynth.add(mOscSaw3);

        */










        mSynth.add(mOut);
        mSynth.add(mLowPassFilter);
        mSynth.add(mHighPassFilter);
        mSynth.add(envPlayer);
        mSynth.add(envPlayerFilter);
       // mSynth.add(mDelayUnit);



         for(int i = 0 ; i < mDelays.length ; i++){


            mDelaysFilter[i] = new FilterLowPass();
            mDelaysFilter[i].frequency.set(20000);
            mDelaysFilter[i].amplitude.set(   (0.6 / (i + 1) ) - 0      );

           // Log.i("FYP" , "delay amp is " + String.valueOf((0.6 / (i + 1) ) - 0 ) + " " + i);

            mDelays[i] = new InterpolatingDelay();
            mDelays[i].allocate(132300);
            mSynth.add(mDelays[i]);
            mSynth.add(mDelaysFilter[i]);

        }

        setupCircuit();

    }


    public Synth(SynthData sd){

            loadData(sd);

        setUpOscsArray();


/*        mSynth.add(mOscSine);
        mSynth.add(mOscSine2);
        mSynth.add(mOscSine3);

        mSynth.add(mOscSqr);
        mSynth.add(mOscSqr2);
        mSynth.add(mOscSqr3);

        mSynth.add(mOscTri);
        mSynth.add(mOscTri2);
        mSynth.add(mOscTri3);

        mSynth.add(mOscSaw);
        mSynth.add(mOscSaw2);
        mSynth.add(mOscSaw3);*/



        mSynth.add(mOut);
        mSynth.add(mLowPassFilter);
        mSynth.add(mHighPassFilter);
        mSynth.add(envPlayer);
        mSynth.add(envPlayerFilter);
        // mSynth.add(mDelayUnit);



        for(int i = 0 ; i < mDelays.length ; i++){


            mDelaysFilter[i] = new FilterLowPass();
            mDelaysFilter[i].frequency.set(20000);
            mDelaysFilter[i].amplitude.set(   (0.6 / (i + 1) ) - 0      );

            // Log.i("FYP" , "delay amp is " + String.valueOf((0.6 / (i + 1) ) - 0 ) + " " + i);

            mDelays[i] = new InterpolatingDelay();
            mDelays[i].allocate(132300);
            mSynth.add(mDelays[i]);
            mSynth.add(mDelaysFilter[i]);

        }

        setupCircuit();
     //   disableFilterEnv();


    }


    private void setUpOscsArray(){

       mOscSawArray = new ArrayList<Oscs>(polyphony);
       mOscSineArray = new ArrayList<Oscs>(polyphony);
       mOscTriArray = new ArrayList<Oscs>(polyphony);
       mOscSqrArray = new ArrayList<Oscs>(polyphony);


       for(int i = 0 ; i < polyphony; i++){

           mOscSawArray.add(i , new Oscs(unison, Oscs.SAW));
           mOscSineArray.add(i , new Oscs(unison , Oscs.SINE));
           mOscTriArray.add(i , new Oscs(unison , Oscs.TRI));
           mOscSqrArray.add(i , new Oscs(unison , Oscs.SQR));



       }



        for(int o = 0; o < polyphony; o++){

           for(int i = 0 ; i < unison; i ++){

               mSynth.add(mOscSawArray.get(o).getmOscArray().get(i));
               mSynth.add(mOscSineArray.get(o).getmOscArray().get(i));
               mSynth.add(mOscTriArray.get(o).getmOscArray().get(i));
               mSynth.add(mOscSqrArray.get(o).getmOscArray().get(i));


           }




        }






    }



    public void setOscToSine(){

    disableSaw();
    disableTri();
    disableSqr();
    enableSine();



    }

    public void setFilterValue(int value){

        mLowPassFilter.frequency.set(value);
        mHighPassFilter.frequency.set(value);


        Log.i("FYP" , "low pass is " + mLowPassFilter.frequency.get());
        Log.i("FYP" , "high pass is " + mHighPassFilter.frequency.get());


    }



    private void setupCircuit(){


        // osc -> filter -> output
        // env volume controls amp of oscs




        mLowPassFilter.frequency.set(20000);
        mLowPassFilter.Q.set(2.5);
        mLowPassFilter.output.connect( 0, mOut.input, 0 ); /* Left side */
        mLowPassFilter.output.connect( 0, mOut.input, 1 );

        mHighPassFilter.frequency.set(0);
        mHighPassFilter.Q.set(2.5);
        mHighPassFilter.output.connect(0,mOut.input , 0);
        mHighPassFilter.output.connect(0,mOut.input , 1);

        mLowPassFilter.amplitude.set(0.1);
        mHighPassFilter.amplitude.set(0.1);

        if(!ENABLE_LOW_PASS){

            selectHighPass();

        }
        if(!ENABLE_HIGH_PASS){
            selectLowPass();
        }


      /*  mOscSaw.amplitude.set(0);
        mOscSaw2.amplitude.set(0);
        mOscSaw3.amplitude.set(0);
*/
    /*    mOscSine.amplitude.set(0);
        mOscSine2.amplitude.set(0);
        mOscSine3.amplitude.set(0);*/




       // mDelayUnit.output.connect(0,mOut.input,0);
       // mDelayUnit.output.connect(0,mOut.input,1);

       // mDelayUnit.delay.set(1.0);


       // mLowPassFilter.output.connect(mDelayUnit.input);
       // mHighPassFilter.output.connect(mDelayUnit.input);


        for(int i = 0 ; i<mDelays.length ; i++){



            mLowPassFilter.output.connect(mDelays[i]);
            mHighPassFilter.output.connect(mDelays[i]);


            mDelays[i].output.connect(mDelaysFilter[i].input);

            mDelaysFilter[i].output.connect(0,mOut.input,0);
            mDelaysFilter[i].output.connect(0,mOut.input,1);

            mDelays[i].delay.set(DELAY_TIME * (i+1));




        }


        // connect filters and amp

        for(int i = 0 ; i < polyphony; i++){

            for(int p = 0 ; p < unison; p++){


                mOscSawArray.get(i).setAmp(globalAmp);
                mOscSineArray.get(i).setAmp(globalAmp);
                mOscSqrArray.get(i).setAmp(globalAmp);
                mOscTriArray.get(i).setAmp(globalAmp);


                mOscSawArray.get(i).getmOscArray().get(p).output.connect(mHighPassFilter.input);
                mOscSawArray.get(i).getmOscArray().get(p).output.connect(mLowPassFilter.input);

                mOscSineArray.get(i).getmOscArray().get(p).output.connect(mHighPassFilter.input);
                mOscSineArray.get(i).getmOscArray().get(p).output.connect(mLowPassFilter.input);

                mOscSqrArray.get(i).getmOscArray().get(p).output.connect(mHighPassFilter.input);
                mOscSqrArray.get(i).getmOscArray().get(p).output.connect(mLowPassFilter.input);

                mOscTriArray.get(i).getmOscArray().get(p).output.connect(mHighPassFilter.input);
                mOscTriArray.get(i).getmOscArray().get(p).output.connect(mLowPassFilter.input);



               /* envPlayer.output.connect(mOscSawArray.get(i).getmOscArray().get(p).amplitude);
                envPlayer.output.connect(mOscSineArray.get(i).getmOscArray().get(p).amplitude);
                envPlayer.output.connect(mOscSqrArray.get(i).getmOscArray().get(p).amplitude);
                envPlayer.output.connect(mOscTriArray.get(i).getmOscArray().get(p).amplitude);*/




            }




        }




        //disable all osc first, enable later when we need to use them
        for(int i = 0 ; i < polyphony; i ++){

            mOscSawArray.get(i).setDisable();
            mOscSineArray.get(i).setDisable();
            mOscSqrArray.get(i).setDisable();
            mOscTriArray.get(i).setDisable();




        }





        /*


        mOscSaw.output.connect(mHighPassFilter.input);
        mOscSaw2.output.connect(mHighPassFilter.input);
        mOscSaw3.output.connect(mHighPassFilter.input);

        mOscSaw.output.connect(mLowPassFilter.input);
        mOscSaw2.output.connect(mLowPassFilter.input);
        mOscSaw3.output.connect(mLowPassFilter.input);


        mOscSine.output.connect(mHighPassFilter.input);
        mOscSine2.output.connect(mHighPassFilter.input);
        mOscSine3.output.connect(mHighPassFilter.input);

        mOscSine.output.connect(mLowPassFilter.input);
        mOscSine2.output.connect(mLowPassFilter.input);
        mOscSine3.output.connect(mLowPassFilter.input);


        mOscTri.output.connect(mHighPassFilter.input);
        mOscTri2.output.connect(mHighPassFilter.input);
        mOscTri3.output.connect(mHighPassFilter.input);

        mOscTri.output.connect(mLowPassFilter.input);
        mOscTri2.output.connect(mLowPassFilter.input);
        mOscTri3.output.connect(mLowPassFilter.input);


        mOscSqr.output.connect(mHighPassFilter.input);
        mOscSqr2.output.connect(mHighPassFilter.input);
        mOscSqr3.output.connect(mHighPassFilter.input);


        mOscSqr.output.connect(mLowPassFilter.input);
        mOscSqr2.output.connect(mLowPassFilter.input);
        mOscSqr3.output.connect(mLowPassFilter.input);




        envPlayer.output.connect(mOscSaw.amplitude);
        envPlayer.output.connect(mOscSaw2.amplitude);
        envPlayer.output.connect(mOscSaw3.amplitude);


        envPlayer.output.connect(mOscSine.amplitude);
        envPlayer.output.connect(mOscSine2.amplitude);
        envPlayer.output.connect(mOscSine3.amplitude);


        envPlayer.output.connect(mOscSqr.amplitude);
        envPlayer.output.connect(mOscSqr2.amplitude);
        envPlayer.output.connect(mOscSqr3.amplitude);

        envPlayer.output.connect(mOscTri.amplitude);
        envPlayer.output.connect(mOscTri2.amplitude);
        envPlayer.output.connect(mOscTri3.amplitude);

*/

        if(DisableSaw){
            disableSaw();

        }

        if(DisableSqr){

            disableSqr();

        }

        if(DisableSine){

        disableSine();
        }

        if(DisableTri){

        disableTri();
        }


      envPlayer.output.connect(mLowPassFilter.amplitude);
      envPlayer.output.connect(mHighPassFilter.amplitude);


      envPlayerFilter.output.connect(mLowPassFilter.frequency);
      envPlayerFilter.output.connect(mHighPassFilter.frequency);



        if(!ENABLE_FILTER_ADSR){
            disableFilterEnv();
        }


       /* envPlayer.output.connect(mOscSine.amplitude);
        envPlayer.output.connect(mOscSine2.amplitude);
        envPlayer.output.connect(mOscSine3.amplitude);


        envPlayer.output.connect(mOscSqr.amplitude);
        envPlayer.output.connect(mOscSqr2.amplitude);
        envPlayer.output.connect(mOscSqr3.amplitude);


        envPlayer.output.connect(mOscTri.amplitude);
        envPlayer.output.connect(mOscTri2.amplitude);
        envPlayer.output.connect(mOscTri3.amplitude);
*/



        mSynth.start();
        mOut.start();
        //mDelayUnit.start();
        for(int i = 0 ; i < mDelays.length; i++){

            mDelays[i].start();


        }


        if(!DELAY_ENABLED){
            disableDelay();
        }



    }


    public void selectLowPass(){

        Log.i("FYP" , "selecting Low Pass");
        ENABLE_LOW_PASS = true;
        ENABLE_HIGH_PASS = false;
        mLowPassFilter.setEnabled(true);
        mHighPassFilter.setEnabled(false);

    }

    public void selectHighPass(){

        Log.i("FYP" , "selecting High Pass");
        ENABLE_LOW_PASS = false;
        ENABLE_HIGH_PASS = true;
        mLowPassFilter.setEnabled(false);
        mHighPassFilter.setEnabled(true);

    }

    public void setfreqQ(double value){

        mLowPassFilter.Q.set(value);
        mHighPassFilter.Q.set(value);


    }


    public void disableSaw(){

        DisableSaw = true;
/*    mOscSaw.setEnabled(false);
    mOscSaw2.setEnabled(false);
    mOscSaw3.setEnabled(false);*/

        for(int i =0; i < polyphony; i++){

            mOscSawArray.get(i).setDisable();

        }





    }

    public void disableSqr(){

        DisableSqr = true;
/*        mOscSqr.setEnabled(false);
        mOscSqr2.setEnabled(false);
        mOscSqr3.setEnabled(false);*/

        for(int i =0; i < polyphony; i++){

                mOscSqrArray.get(i).setDisable();
        }

    }

    public void disableSine(){

        DisableSine = true;
/*        mOscSine.setEnabled(false);
        mOscSine2.setEnabled(false);
        mOscSine3.setEnabled(false);*/

        for(int i =0; i < polyphony; i++){

                mOscSineArray.get(i).setDisable();


        }



    }

    public void disableTri(){

        DisableTri = true;
/*        mOscTri.setEnabled(false);
        mOscTri2.setEnabled(false);
        mOscTri3.setEnabled(false);*/

        for(int i =0; i < polyphony; i++){

            mOscTriArray.get(i).setDisable();
        }


    }

    public void enableSaw(){

        DisableSaw = false;
/*        mOscSaw.setEnabled(true);
        mOscSaw2.setEnabled(true);
        mOscSaw3.setEnabled(true);*/


        for(int i =0; i < polyphony; i++){

                mOscSawArray.get(i).setEnable();


        }

    }

    public void enableSine(){

        DisableSine = false;
/*        mOscSine.setEnabled(true);
        mOscSine2.setEnabled(true);
        mOscSine3.setEnabled(true);*/


        for(int i =0; i < polyphony; i++){


                mOscSineArray.get(i).setEnable();

        }


    }


    public void enableSqr(){

        DisableSqr = false;
/*        mOscSqr.setEnabled(true);
        mOscSqr2.setEnabled(true);
        mOscSqr3.setEnabled(true);*/

        for(int i =0; i < polyphony; i++){

                mOscSqrArray.get(i).setEnable();

        }


    }

    public void enableTri(){

        DisableTri = false;
/*        mOscTri.setEnabled(true);
        mOscTri2.setEnabled(true);
        mOscTri3.setEnabled(true);*/


        for(int i =0; i < polyphony; i++){

                mOscTriArray.get(i).setEnable();

        }

    }




    public void setADSR(double attack, double decay, double sustain, double release){


        env_attack_duration = attack;
        env_decay_duration = decay;
        env_sustain_duration = sustain;
        env_release_duration = release;





        double[] envVol =
                {       attack , env_attack_value,
                        decay , env_decay_value,
                        sustain , env_sustain_value,
                        release , env_release_value
                };


        envForVol = new SegmentedEnvelope(envVol);




    }

    public void setADSRFilter(double attack, double decay, double sustain, double release){



      /*  Log.i("fyp" , "attack is " + attack);
        Log.i("fyp" , "decay is " + decay);
        Log.i("fyp" , "sustain is " + sustain);
        Log.i("fyp" , "release is " + release);



        Log.i("fyp" , "attack value is " + env_attack_value_filter);
        Log.i("fyp" , "decay is " + env_decay_value_filter);
        Log.i("fyp" , "sustain is " + env_sustain_value_filter);
        Log.i("fyp" , "release is " + env_release_value_filter);*/


        env_attack_duration_filter = attack;
        env_decay_duration_filter = decay;
        env_sustain_duration_filter = sustain;
        env_release_duration_filter = release;





        double[] envFilter =
                {       attack , env_attack_value_filter,
                        decay , env_decay_value_filter,
                        sustain , env_sustain_value_filter,
                        release , env_release_value_filter
                };


        envForFilter = new SegmentedEnvelope(envFilter);




    }


    public void setEnv_attack_value_filter(double env_attack_value_filter) {
        this.env_attack_value_filter = env_attack_value_filter;
    }

    public void setEnv_decay_value_filter(double env_decay_value_filter) {
        this.env_decay_value_filter = env_decay_value_filter;
    }

    public void setEnv_sustain_value_filter(double env_sustain_value_filter) {
        this.env_sustain_value_filter = env_sustain_value_filter;
    }

    public void setEnv_release_value_filter(double env_release_value_filter) {
        this.env_release_value_filter = env_release_value_filter;
    }

    public void setEnv_attack_value(double env_attack_value) {
        this.env_attack_value = env_attack_value;
    }

    public void setEnv_decay_value(double env_decay_value) {
        this.env_decay_value = env_decay_value;
    }

    public void setEnv_sustain_value(double env_sustain_value) {
        this.env_sustain_value = env_sustain_value;
    }

    public void setEnv_release_value(double env_release_value) {
        this.env_release_value = env_release_value;
    }



    public void playTestPitch(){



        mOscSaw.frequency.set(Constants.NoteC4);
        mOscSaw2.frequency.set(Constants.NoteE4);
        mOscSaw3.frequency.set(Constants.NoteG4);

        mOscSaw.amplitude.set(0.8);
        mOscSaw2.amplitude.set(0.8);
        mOscSaw3.amplitude.set(0.8);



        envPlayer.dataQueue.clear();
        envPlayer.dataQueue.queue(envForVol, 0, 4 );
         envPlayer.start();


        Log.i("FYP" ,"Playing");



    }

    public void playOsc(){






        if(envPlayerFilter.isEnabled()) {
            mLowPassFilter.frequency.set(0);
            mHighPassFilter.frequency.set(0);

        }
        envPlayerFilter.dataQueue.clear();

        envPlayerFilter.dataQueue.queue(envForFilter,0,envForFilter.getNumFrames());



     //   mLowPassFilter.frequency.set(20000);
      //  mHighPassFilter.frequency.set(20000);

        envPlayer.dataQueue.clear();

       // envPlayer.dataQueue.queue(envForVol, 0, 4 );

        envPlayer.dataQueue.queue(envForVol , 0 , 3);
        envPlayer.dataQueue.queueLoop(envForVol , 1 , 2);



        //  envPlayer.start();



    }

    public void releaseOsc(){


        envPlayer.dataQueue.clear();
        envPlayer.dataQueue.queue(envForVol , 3 , 1);


    }




    public void enableFilterEnv(){



        envPlayerFilter.output.connect(mLowPassFilter.frequency);
        envPlayerFilter.output.connect(mHighPassFilter.frequency);

        envPlayerFilter.setEnabled(true);


    }

    public Boolean isFilterEnvEnabled(){
        return envPlayerFilter.isEnabled();
    }

    public void disableFilterEnv(){

        envPlayerFilter.output.disconnect(mLowPassFilter.frequency);
        envPlayerFilter.output.disconnect(mHighPassFilter.frequency);

        envPlayerFilter.setEnabled(false);




    }




    public void traid(double first, double second, double third){


        Notes note = new Notes(3);
        note.addNotes(first);
        note.addNotes(second);
        note.addNotes(third);
        setNotes(note , detuneValue);



    }


    public void D(){
        {





            mOscSaw.frequency.set(Constants.NoteD4);
            mOscSaw2.frequency.set(Constants.NoteD4 + detuneValue);
            mOscSaw3.frequency.set(Constants.NoteD4 - detuneValue);

            mOscSine.frequency.set(Constants.NoteD4);
            mOscSine2.frequency.set(Constants.NoteD4 + detuneValue);
            mOscSine3.frequency.set(Constants.NoteD4 - detuneValue);

            mOscSqr.frequency.set(Constants.NoteD4);
            mOscSqr2.frequency.set(Constants.NoteD4 + detuneValue);
            mOscSqr3.frequency.set(Constants.NoteD4 - detuneValue);

            mOscTri.frequency.set(Constants.NoteD4);
            mOscTri2.frequency.set(Constants.NoteD4 + detuneValue);
            mOscTri3.frequency.set(Constants.NoteD4 - detuneValue);


            if(envPlayerFilter.isEnabled()) {
                mLowPassFilter.frequency.set(0);
                mHighPassFilter.frequency.set(0);

            }
            envPlayerFilter.dataQueue.clear();

            envPlayerFilter.dataQueue.queue(envForFilter,0,envForFilter.getNumFrames());
            //  envPlayerFilter.start();



            envPlayer.dataQueue.clear();
            envPlayer.dataQueue.queue(envForVol, 0, 4 );

            //  envPlayer.start();





        }
    }



    //this method receives a collection of notes, and set all the osc's frequency to match those of the notes

    public void setNotes(Notes notes , double detune){

        //if detune is -1, just set detuneValue

        if(detune == -1){
            detune = detuneValue;
        }

        for (int i = 0 ; i < notes.noteFreqs.size(); i++){


            if(!DisableSaw)
            mOscSawArray.get(i).setEnable();

            if(!DisableSine)
            mOscSineArray.get(i).setEnable();

            if(!DisableSqr)
            mOscSqrArray.get(i).setEnable();

            if(!DisableTri)
            mOscTriArray.get(i).setEnable();



            mOscSawArray.get(i).setFrequency(notes.noteFreqs.get(i) , detune);
             mOscSineArray.get(i).setFrequency(notes.noteFreqs.get(i) , detune);
             mOscSqrArray.get(i).setFrequency(notes.noteFreqs.get(i), detune);
             mOscTriArray.get(i).setFrequency(notes.noteFreqs.get(i), detune);


        }


        //disable uneeded osc
        for(int i = notes.noteFreqs.size() ; i < polyphony ; i ++){

            mOscSawArray.get(i).setDisable();
            mOscSineArray.get(i).setDisable();
            mOscSqrArray.get(i).setDisable();
            mOscTriArray.get(i).setDisable();


        }



    }







    public void Dmin(){
        {


            Notes Dmin = new Notes(5);
            Dmin.addNotes(Constants.NoteE4);
            Dmin.addNotes(Constants.NoteG4);
            Dmin.addNotes(Constants.NoteB4);
            Dmin.addNotes(Constants.NoteD5);
            Dmin.addNotes(Constants.NoteA5);


            setNotes(Dmin , detuneValue);

/*

            mOscSaw.frequency.set(Constants.NoteD4);
            mOscSaw2.frequency.set(Constants.NoteF4);
            mOscSaw3.frequency.set(Constants.NoteA4);

            mOscSine.frequency.set(Constants.NoteD4);
            mOscSine2.frequency.set(Constants.NoteF4);
            mOscSine3.frequency.set(Constants.NoteA4);

            mOscSqr.frequency.set(Constants.NoteD4);
            mOscSqr2.frequency.set(Constants.NoteF4);
            mOscSqr3.frequency.set(Constants.NoteA4);

            mOscTri.frequency.set(Constants.NoteD4);
            mOscTri2.frequency.set(Constants.NoteF4);
            mOscTri3.frequency.set(Constants.NoteA4);*/


            if(envPlayerFilter.isEnabled()) {
                mLowPassFilter.frequency.set(0);
                mHighPassFilter.frequency.set(0);
            }
            envPlayerFilter.dataQueue.clear();

           envPlayerFilter.dataQueue.queue(envForFilter,0,envForFilter.getNumFrames());
         //  envPlayerFilter.start();



             envPlayer.dataQueue.clear();
            envPlayer.dataQueue.queue(envForVol, 0, 4 );
          //  envPlayer.start();





        }
    }

    public void G7(){


        {
            {



                mOscSaw.frequency.set(Constants.NoteD4);
                mOscSaw2.frequency.set(Constants.NoteF4);
                mOscSaw3.frequency.set(Constants.NoteG4);

                mOscSaw.amplitude.set(0.8);
                mOscSaw2.amplitude.set(0.8);
                mOscSaw3.amplitude.set(0.8);


                envPlayer.dataQueue.clear();
                envPlayer.dataQueue.queue(envForVol, 0, 4 );
                envPlayer.start();





            }
        }




    }


    public void mute(){

        if(mOut.isEnabled())
        mOut.setEnabled(false);

        else{
            mOut.setEnabled(true);
        }


    }






    public void setFrequencyWithPorta(final double frequency){




        freqDiff = 0;

        if(ENABLE_PORTA) {

            final Timer tim = new Timer();



            if (frequency <= FREQUENCY_NOW) {

                freqDiff = FREQUENCY_NOW - frequency;

                freqDiffSliderFactor = 1;

                TimerTask porta = new TimerTask() {
                    @Override
                    public void run() {


                        Notes note = new Notes(1);
                        note.addNotes(FREQUENCY_NOW);
                        setNotes(note , detuneValue);









/*                        mOscSaw.frequency.set(FREQUENCY_NOW);
                        mOscSaw2.frequency.set(FREQUENCY_NOW + detuneValue);
                        mOscSaw3.frequency.set(FREQUENCY_NOW - detuneValue);

                        mOscSine.frequency.set(FREQUENCY_NOW);
                        mOscSine2.frequency.set(FREQUENCY_NOW + detuneValue);
                        mOscSine3.frequency.set(FREQUENCY_NOW - detuneValue);

                        mOscSqr.frequency.set(FREQUENCY_NOW);
                        mOscSqr2.frequency.set(FREQUENCY_NOW + detuneValue);
                        mOscSqr3.frequency.set(FREQUENCY_NOW - detuneValue);

                        mOscTri.frequency.set(FREQUENCY_NOW);
                        mOscTri2.frequency.set(FREQUENCY_NOW + detuneValue);
                        mOscTri3.frequency.set(FREQUENCY_NOW - detuneValue);*/

                        FREQUENCY_NOW--;
                        freqDiff--;
                        if(freqDiff <= 0.0){

                             tim.cancel();


                        }

                    }
                };

                tim.schedule(porta,0,1);






            }

            if (frequency > FREQUENCY_NOW) {




                freqDiff = frequency - FREQUENCY_NOW;
                freqDiffSliderFactor = 1;


                TimerTask porta = new TimerTask() {
                    @Override
                    public void run() {

                        Notes note = new Notes(1);
                        note.addNotes(FREQUENCY_NOW);
                        setNotes(note , detuneValue);
/*

                        mOscSaw.frequency.set(FREQUENCY_NOW);
                        mOscSaw2.frequency.set(FREQUENCY_NOW + detuneValue);
                        mOscSaw3.frequency.set(FREQUENCY_NOW - detuneValue);

                        mOscSine.frequency.set(FREQUENCY_NOW);
                        mOscSine2.frequency.set(FREQUENCY_NOW + detuneValue);
                        mOscSine3.frequency.set(FREQUENCY_NOW - detuneValue);

                        mOscSqr.frequency.set(FREQUENCY_NOW);
                        mOscSqr2.frequency.set(FREQUENCY_NOW + detuneValue);
                        mOscSqr3.frequency.set(FREQUENCY_NOW - detuneValue);

                        mOscTri.frequency.set(FREQUENCY_NOW);
                        mOscTri2.frequency.set(FREQUENCY_NOW + detuneValue);
                        mOscTri3.frequency.set(FREQUENCY_NOW - detuneValue);
*/

                        FREQUENCY_NOW++;
                        freqDiff--;

                    //    Log.i("FYP" ,String.valueOf(freqDiff));

                        if(freqDiff <= 0.0){

                     //       Log.i("FYP" ,"Cancelling slide up");

                            tim.cancel();


                        }

                    }
                };


                tim.schedule(porta,0,1);

               /* for (int i = 0; i < (int) freqDiff; i++) {


                    mOscSaw.frequency.set(FREQUENCY_NOW);
                    mOscSaw2.frequency.set(FREQUENCY_NOW + 10);
                    mOscSaw3.frequency.set(FREQUENCY_NOW - 10);

                    mOscSine.frequency.set(FREQUENCY_NOW);
                    mOscSine2.frequency.set(FREQUENCY_NOW);
                    mOscSine3.frequency.set(FREQUENCY_NOW);

                    mOscSqr.frequency.set(FREQUENCY_NOW);
                    mOscSqr2.frequency.set(FREQUENCY_NOW);
                    mOscSqr3.frequency.set(FREQUENCY_NOW);

                    mOscTri.frequency.set(FREQUENCY_NOW);
                    mOscTri2.frequency.set(FREQUENCY_NOW);
                    mOscTri3.frequency.set(FREQUENCY_NOW);

                    FREQUENCY_NOW++;

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }*/


            }


        }




    }


    public void destory(){

        mSynth.stop();



    }


    public void enableDelay(){



        DELAY_ENABLED = true;


        for(int i = 0 ; i < mDelays.length; i++){
            mDelays[i].setEnabled(true);
        }


    }

    public void disableDelay(){


        DELAY_ENABLED = false;

        for(int i = 0 ; i < mDelays.length; i++){
            mDelays[i].setEnabled(false);
        }


    }




    public void setFrequency(double frequency){






            mOscSaw.frequency.set(frequency);
            mOscSaw2.frequency.set(frequency + detuneValue);
            mOscSaw3.frequency.set(frequency - detuneValue);

            mOscSine.frequency.set(frequency);
            mOscSine2.frequency.set(frequency);
            mOscSine3.frequency.set(frequency);

            mOscSqr.frequency.set(frequency);
            mOscSqr2.frequency.set(frequency);
            mOscSqr3.frequency.set(frequency);

            mOscTri.frequency.set(frequency);
            mOscTri2.frequency.set(frequency);
            mOscTri3.frequency.set(frequency);



    }



    public void stopTestPitch(){



        mOscSaw.amplitude.set(0.0);




    }


    public void setDetuneValue(double detuneValue) {
        this.detuneValue = detuneValue;
    }

    public void loadData(SynthData sd){


        detuneValue = sd.detuneValue;

        env_attack_duration = sd.env_attack_duration;
        env_decay_duration = sd.env_decay_duration;
        env_sustain_duration = sd.env_sustain_duration;
        env_release_duration = sd.env_release_duration;

        env_attack_value = sd.env_attack_value;
        env_decay_value = sd.env_decay_value;
        env_sustain_value = sd.env_sustain_value;
        env_release_value = sd.env_release_value;


        ENABLE_FILTER_ADSR = sd.enable_filter_adsr;

        if(ENABLE_FILTER_ADSR)
        Log.i("FYP " , "ENABLE_FILTER_ADSR true" );


        ENABLE_PORTA = sd.enable_porta;




        env_attack_duration_filter = sd.env_attack_duration_filter;
        env_decay_duration_filter = sd.env_decay_duration_filter;
        env_sustain_duration_filter = sd.env_sustain_duration_filter;
        env_release_duration_filter = sd.env_release_duration_filter;

        env_attack_value_filter = sd.env_attack_value_filter;
        env_decay_value_filter = sd.env_decay_value_filter;
        env_sustain_value_filter = sd.env_sustain_value_filter;
        env_release_value_filter = sd.env_release_value_filter;


        Log.i("FYP" , " Value of filter attack, decay, sustain and release are "

                +env_attack_value_filter + " "
                +env_decay_value_filter + " "
                +env_sustain_value_filter + " "
                +env_release_value_filter + " "

        );

        DELAY_TIME = sd.DELAY_TIME;
        num_of_delay_voices = sd.num_of_delay_voices;
        DELAY_ENABLED = sd.enable_delay;
        DisableSaw = sd.DisableSaw;
        DisableSine = sd.DisableSine;
        DisableSqr = sd.DisableSqr;
        DisableTri = sd.DisableTri;
        ENABLE_LOW_PASS = sd.enableLowPass;
        ENABLE_HIGH_PASS = sd.enableHighPass;

        setADSR(env_attack_duration,env_decay_duration,env_sustain_duration,env_release_duration);
        setADSRFilter(env_attack_duration_filter,env_decay_duration_filter,env_sustain_value_filter,env_release_duration_filter);









    }



    public SynthData saveData(){


    SynthData sd = new SynthData();


    sd.detuneValue = detuneValue;

    sd.env_attack_duration = env_attack_duration;
    sd.env_decay_duration = env_decay_duration;
    sd.env_sustain_duration = env_sustain_duration;
    sd.env_release_duration = env_release_duration;

    sd.env_attack_value = env_attack_value;
    sd.env_decay_value = env_decay_value;
    sd.env_sustain_value = env_sustain_value;
    sd.env_release_value = env_release_value;

    sd.enable_filter_adsr = envPlayerFilter.isEnabled();
    sd.enable_porta = ENABLE_PORTA;
    sd.enable_delay = DELAY_ENABLED;

    sd.env_attack_duration_filter = env_attack_duration_filter;
    sd.env_decay_duration_filter = env_decay_duration_filter;
    sd.env_sustain_duration_filter = env_sustain_duration_filter;
    sd.env_release_duration_filter = env_release_duration_filter;

    sd.env_attack_value_filter = env_attack_value_filter;
    sd.env_decay_value_filter = env_decay_value_filter;
    sd.env_sustain_value_filter = env_sustain_value_filter;
    sd.env_release_value_filter = env_release_value_filter;

    sd.DELAY_TIME = DELAY_TIME;
    sd.num_of_delay_voices = num_of_delay_voices;
    sd.DisableSaw = DisableSaw;
    sd.DisableSine = DisableSine;
    sd.DisableSqr = DisableSqr;
    sd.DisableTri = DisableTri;

    sd.enableLowPass = mLowPassFilter.isEnabled();
    sd.enableHighPass = mHighPassFilter.isEnabled();

    return sd;






    }





































}

