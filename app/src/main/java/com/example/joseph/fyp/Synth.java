package com.example.joseph.fyp;

import android.arch.persistence.room.Room;
import android.util.Log;

import com.jsyn.JSyn;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.unitgen.FilterHighPass;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.InterpolatingDelay;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.PhaseShifter;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SineOscillatorPhaseModulated;
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

    private RoomDAO mRoomDAO;

    public String title;

    private double FREQUENCY_NOW = 440;
    private double freqDiff = 0;
    private double freqDiffSliderFactor;

    private double globalAmp = 0.1; // amp for oscs
    private double masterAmp = 0.5; // amp for master filter
    private double oldArea = -1; //used for zoom object in/out for manipulating filter volume setFilterAmp2
    private double masterArea = -1; // used for setFilterAmp

    private boolean ENABLE_PORTA = true;
    private boolean DELAY_ENABLED = true;
    private boolean ENABLE_LOW_PASS = true;
    private boolean ENABLE_HIGH_PASS = false;
    private boolean ENABLE_FILTER_ADSR = true;
    private boolean ENABLE_PHASER = false;

    private boolean ENABLE_PM;
    private boolean ENABLE_LFO;

    private int unison = 4;
    private int polyphony = 10;

    private int noOfOscVoice = unison * polyphony;


    private ArrayList<Oscs> mOscSawArray;
    private ArrayList<Oscs> mOscSineArray;
    private ArrayList<Oscs> mOscSqrArray;
    private ArrayList<Oscs> mOscTriArray;


    private SineOscillator SinePMFeeder = new SineOscillator();
    private SineOscillator SineLFO = new SineOscillator();
    private PhaseShifter phaseShifter = new PhaseShifter();
    private SineOscillator phaseShifterLFO = new SineOscillator();

    private ArrayList<Oscs> mOscSawArray2;
    private ArrayList<Oscs> mOscSineArray2;
    private ArrayList<Oscs> mOscSqrArray2;
    private ArrayList<Oscs> mOscTriArray2;



/*    private SawtoothOscillatorBL mOscSaw = new SawtoothOscillatorBL(); // useless
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
    private TriangleOscillator mOscTri3 = new TriangleOscillator();*/


    private boolean DisableSaw = false;
    private boolean DisableSine = true;
    private boolean DisableSqr = true;
    private boolean DisableTri = true;


    private boolean DisableSaw2 = true;
    private boolean DisableSine2 = true;
    private boolean DisableSqr2 = true;
    private boolean DisableTri2 = true;

    private LineOut mOut = new LineOut();
    private FilterLowPass mLowPassFilter = new FilterLowPass();
    private FilterHighPass mHighPassFilter = new FilterHighPass();
    private FilterLowPass mMasterFilter = new FilterLowPass();

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

        setupCircuitStuff();

    }


    public Synth(SynthData sd){

            loadData(sd);

        setUpOscsArray();
        setupCircuitStuff();




    }

    public Synth(RoomSynthData rsd){

        loadData(rsd);

        setUpOscsArray();
        setupCircuitStuff();




    }



    private void setupCircuitStuff(){




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
        mSynth.add(mMasterFilter);
        mSynth.add(phaseShifter);
        mSynth.add(phaseShifterLFO);
         // mSynth.add(mDelayUnit);



        for(int i = 0 ; i < mDelays.length ; i++){


            mDelaysFilter[i] = new FilterLowPass();
            mDelaysFilter[i].frequency.set(20000);
            mDelaysFilter[i].amplitude.set(   (masterAmp / (i + 1) )       );

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

        mOscSawArray2 = new ArrayList<Oscs>(polyphony);
        mOscSineArray2 = new ArrayList<Oscs>(polyphony);
        mOscTriArray2 = new ArrayList<Oscs>(polyphony);
        mOscSqrArray2 = new ArrayList<Oscs>(polyphony);


       for(int i = 0 ; i < polyphony; i++){

           mOscSawArray.add(i , new Oscs(unison, Oscs.SAW));
           mOscSineArray.add(i , new Oscs(unison , Oscs.SINEPM));
           mOscTriArray.add(i , new Oscs(unison , Oscs.TRI));
           mOscSqrArray.add(i , new Oscs(unison , Oscs.SQR));

           mOscSawArray2.add(i , new Oscs(unison, Oscs.SAW));
           mOscSineArray2.add(i , new Oscs(unison , Oscs.SINE));
           mOscTriArray2.add(i , new Oscs(unison , Oscs.TRI));
           mOscSqrArray2.add(i , new Oscs(unison , Oscs.SQR));



       }



        for(int o = 0; o < polyphony; o++){

           for(int i = 0 ; i < unison; i ++){

               mSynth.add(mOscSawArray.get(o).getmOscArray().get(i));
               mSynth.add(mOscSineArray.get(o).getmOscArray().get(i));
               mSynth.add(mOscTriArray.get(o).getmOscArray().get(i));
               mSynth.add(mOscSqrArray.get(o).getmOscArray().get(i));


               mSynth.add(mOscSawArray2.get(o).getmOscArray().get(i));
               mSynth.add(mOscSineArray2.get(o).getmOscArray().get(i));
               mSynth.add(mOscTriArray2.get(o).getmOscArray().get(i));
               mSynth.add(mOscSqrArray2.get(o).getmOscArray().get(i));


           }




        }






    }


/*
    public void setOscToSine(){

    disableSaw();
    disableTri();
    disableSqr();
    enableSine();



    }*/

    public void setFilterValue(int value){

        mLowPassFilter.frequency.set(value);
        mHighPassFilter.frequency.set(value);



    }

    public void setMasterArea(double masterArea) {
        this.masterArea = masterArea;
        mMasterFilter.amplitude.set(globalAmp);
    }

    //use this to reset the area volume back to normal
    public void resetFilterAmp(){

        mMasterFilter.amplitude.set(masterAmp);


    }


    public void setFilterAmp(double area){

        //max is 0.1

        double prevAmp = mMasterFilter.amplitude.get();

        prevAmp = prevAmp * area/masterArea;

        Log.i("FYP" , "masterArea is " + masterArea);


        Log.i("FYP" , "prevAmp is " + prevAmp);

        mMasterFilter.amplitude.set(prevAmp);



    }


    //This one works by comparing current area with previous area
    public void setFilterAmp2(double area){

        //max is 0.1

        if(oldArea == -1){
            oldArea = area;
            return;
        }


        double prevAmp = mMasterFilter.amplitude.get();

        prevAmp = prevAmp * area/oldArea;


        Log.i("FYP" , "difference between two area  is " + area/oldArea);

        Log.i("FYP" , "prevAmp is " + prevAmp);

        oldArea = area;

//        for(int i = 0 ; i < mDelays.length ; i++) {
//            mDelaysFilter[i].amplitude.set((prevAmp / (i + 1)));
//        }





        mMasterFilter.amplitude.set(prevAmp);



    }





    private void setupCircuit(){


        // osc -> filter -> output
        // env volume controls amp of oscs

        mSynth.add(SinePMFeeder);
        mSynth.add(SineLFO);

        phaseShifterLFO.frequency.set(0.5);
        phaseShifterLFO.output.connect(phaseShifter.offset);




        mLowPassFilter.frequency.set(20000);
        mLowPassFilter.Q.set(2.5);
        mLowPassFilter.output.connect( phaseShifter.input);

        mHighPassFilter.frequency.set(0);
        mHighPassFilter.Q.set(2.5);
        mHighPassFilter.output.connect( phaseShifter.input);


        phaseShifter.output.connect(mMasterFilter.input);

        mMasterFilter.frequency.set(20000);
        mMasterFilter.Q.set(1);
        mMasterFilter.output.connect(0,mOut.input ,0);
        mMasterFilter.output.connect(0,mOut.input ,1);
        mMasterFilter.amplitude.set(masterAmp);



        mLowPassFilter.amplitude.set(0.2);
        mHighPassFilter.amplitude.set(0.2);

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


//
//            mLowPassFilter.output.connect(mDelays[i]);
//            mHighPassFilter.output.connect(mDelays[i]);
//
//
//


            mMasterFilter.output.connect(mDelays[i]);

            mDelays[i].output.connect(mDelaysFilter[i].input);

            mDelaysFilter[i].output.connect(0,mOut.input ,0);
            mDelaysFilter[i].output.connect(0,mOut.input ,1);
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


                mOscSawArray2.get(i).setAmp(globalAmp);
                mOscSineArray2.get(i).setAmp(globalAmp);
                mOscSqrArray2.get(i).setAmp(globalAmp);
                mOscTriArray2.get(i).setAmp(globalAmp);


                mOscSawArray2.get(i).getmOscArray().get(p).output.connect(mHighPassFilter.input);
                mOscSawArray2.get(i).getmOscArray().get(p).output.connect(mLowPassFilter.input);

                mOscSineArray2.get(i).getmOscArray().get(p).output.connect(mHighPassFilter.input);
                mOscSineArray2.get(i).getmOscArray().get(p).output.connect(mLowPassFilter.input);

                mOscSqrArray2.get(i).getmOscArray().get(p).output.connect(mHighPassFilter.input);
                mOscSqrArray2.get(i).getmOscArray().get(p).output.connect(mLowPassFilter.input);

                mOscTriArray2.get(i).getmOscArray().get(p).output.connect(mHighPassFilter.input);
                mOscTriArray2.get(i).getmOscArray().get(p).output.connect(mLowPassFilter.input);


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

                mOscSawArray2.get(i).setDisable();
                mOscSineArray2.get(i).setDisable();
                mOscSqrArray2.get(i).setDisable();
                mOscTriArray2.get(i).setDisable();




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

        if(DisableSaw2){
            disableSaw2();
        }

        if(DisableSqr2){
            disableSqr2();
        }

        if(DisableSine2){
            disableSine2();
        }

        if(DisableTri2){
            disableTri2();
        }


        envPlayer.output.connect(mLowPassFilter.amplitude);
        envPlayer.output.connect(mHighPassFilter.amplitude);


        envPlayerFilter.output.connect(mLowPassFilter.frequency);
        envPlayerFilter.output.connect(mHighPassFilter.frequency);



        if(!ENABLE_FILTER_ADSR){
            disableFilterEnv();
        }

        if(ENABLE_PHASER){
            enablePhaseShifter();
        }
        else{
            disablePhaseShifter();
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

        if(ENABLE_LFO){
            enableLFO();
        }

        if(ENABLE_PM){
            enablePM();
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



    public void enableLFO(){

        Log.i("FYP" , "LFO enabled");

        SineLFO.output.connect(mOut.input);


        SineLFO.setEnabled(true);
        SineLFO.start();

        ENABLE_LFO = true;



    }

    public void disableLFO(){
        Log.i("FYP" , "LFO disableLFO");


        SineLFO.output.disconnect(mOut.input);

        SineLFO.setEnabled(false);
        SineLFO.stop();

        ENABLE_LFO = false;



    }


    public void enablePM() {


        for (int i = 0; i < polyphony; i++) {

            for (int p = 0; p < unison; p++) {




                    SinePMFeeder.output.connect(((SineOscillatorPhaseModulated) mOscSineArray.get(i).getmOscArray().get(p)).modulation);



            }
        }

        SinePMFeeder.setEnabled(true);
        SinePMFeeder.start();

        ENABLE_PM = true;


    }

    public void disablePM() {


        for (int i = 0; i < polyphony; i++) {

            for (int p = 0; p < unison; p++) {




                SinePMFeeder.output.disconnect(((SineOscillatorPhaseModulated) mOscSineArray.get(i).getmOscArray().get(p)).modulation);



            }
        }

        SinePMFeeder.setEnabled(false);
        SinePMFeeder.stop();

        ENABLE_PM = false;

    }

    public void setLFOfreq(double freq){


        SineLFO.frequency.set(freq);

    }

    public void setLFOamp(double amp){


        SineLFO.amplitude.set(amp);


    }

    public void enablePhaseShifter(){

        phaseShifter.output.connect(mMasterFilter.input);
        phaseShifter.setEnabled(true);
        phaseShifter.start();
        ENABLE_PHASER = true;

    }

    public void disablePhaseShifter(){

        phaseShifter.depth.set(0);

    }

    public void setPhaseShifterfeedback(double feedback){

        phaseShifter.feedback.set(feedback);

    }

    public void setPhaserShifterdepth(double depth){

        phaseShifter.depth.set(depth);

    }

    public void setPhaserShifterfreq(double freq){

        phaseShifterLFO.frequency.set(freq);

    }

    public void setPhaseShifteramp(double amp){

        phaseShifterLFO.amplitude.set(amp);

    }


    public void setPMfreq(double freq){


        SinePMFeeder.frequency.set(freq);


    }

    public void setPMamp(double amp){


        SinePMFeeder.amplitude.set(amp);
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


    public void disableSaw2(){

        DisableSaw2 = true;
/*    mOscSaw.setEnabled(false);
    mOscSaw2.setEnabled(false);
    mOscSaw3.setEnabled(false);*/

        for(int i =0; i < polyphony; i++){

            mOscSawArray2.get(i).setDisable();

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


    public void disableSqr2(){

        DisableSqr2 = true;
/*        mOscSqr.setEnabled(false);
        mOscSqr2.setEnabled(false);
        mOscSqr3.setEnabled(false);*/

        for(int i =0; i < polyphony; i++){

            mOscSqrArray2.get(i).setDisable();
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

    public void disableSine2(){

        DisableSine2 = true;
/*        mOscSine.setEnabled(false);
        mOscSine2.setEnabled(false);
        mOscSine3.setEnabled(false);*/

        for(int i =0; i < polyphony; i++){

            mOscSineArray2.get(i).setDisable();


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

    public void disableTri2(){

        DisableTri2 = true;
/*        mOscTri.setEnabled(false);
        mOscTri2.setEnabled(false);
        mOscTri3.setEnabled(false);*/

        for(int i =0; i < polyphony; i++){

            mOscTriArray2.get(i).setDisable();
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


    public void enableSaw2(){

        DisableSaw2 = false;
/*        mOscSaw.setEnabled(true);
        mOscSaw2.setEnabled(true);
        mOscSaw3.setEnabled(true);*/


        for(int i =0; i < polyphony; i++){

            mOscSawArray2.get(i).setEnable();


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

    public void enableSine2(){

        DisableSine2 = false;
/*        mOscSine.setEnabled(true);
        mOscSine2.setEnabled(true);
        mOscSine3.setEnabled(true);*/


        for(int i =0; i < polyphony; i++){


            mOscSineArray2.get(i).setEnable();

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

    public void enableSqr2(){

        DisableSqr2 = false;
/*        mOscSqr.setEnabled(true);
        mOscSqr2.setEnabled(true);
        mOscSqr3.setEnabled(true);*/

        for(int i =0; i < polyphony; i++){

            mOscSqrArray2.get(i).setEnable();

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


    public void enableTri2(){

        DisableTri2 = false;
/*        mOscTri.setEnabled(true);
        mOscTri2.setEnabled(true);
        mOscTri3.setEnabled(true);*/


        for(int i =0; i < polyphony; i++){

            mOscTriArray2.get(i).setEnable();

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


/*
        mOscSaw.frequency.set(Constants.NoteC4);
        mOscSaw2.frequency.set(Constants.NoteE4);
        mOscSaw3.frequency.set(Constants.NoteG4);

        mOscSaw.amplitude.set(0.8);
        mOscSaw2.amplitude.set(0.8);
        mOscSaw3.amplitude.set(0.8);*/



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
        setNotes(note , detuneValue , 0);



    }


    public void D(){
        {



/*


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
*/


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








    public void setNotes(Notes notes , double detune , double offset ){

        //if detune is -1, just set detuneValue






        if(detune == -1){
            detune = detuneValue;
        }

        //play notes

        for (int i = 0 ; i < notes.noteFreqs.size(); i++){



            if(!DisableSaw)
            mOscSawArray.get(i).setEnable();

            if(!DisableSine)
            mOscSineArray.get(i).setEnable();

            if(!DisableSqr)
            mOscSqrArray.get(i).setEnable();

            if(!DisableTri)
            mOscTriArray.get(i).setEnable();


            if(!DisableSaw2)
                mOscSawArray2.get(i).setEnable();

            if(!DisableSine2)
                mOscSineArray2.get(i).setEnable();

            if(!DisableSqr2)
                mOscSqrArray2.get(i).setEnable();

            if(!DisableTri2)
                mOscTriArray2.get(i).setEnable();


            Log.i("FYP" , "Setting note " + i + " freq " + notes.noteFreqs.get(i));


            mOscSawArray.get(i).setFrequency(notes.noteFreqs.get(i) + offset , detune);
             mOscSineArray.get(i).setFrequency(notes.noteFreqs.get(i) + offset, detune);
             mOscSqrArray.get(i).setFrequency(notes.noteFreqs.get(i)+ offset, detune);
             mOscTriArray.get(i).setFrequency(notes.noteFreqs.get(i)+ offset, detune);


            mOscSawArray2.get(i).setFrequency(notes.noteFreqs.get(i) + offset , detune);
            mOscSineArray2.get(i).setFrequency(notes.noteFreqs.get(i) + offset, detune);
            mOscSqrArray2.get(i).setFrequency(notes.noteFreqs.get(i)+ offset, detune);
            mOscTriArray2.get(i).setFrequency(notes.noteFreqs.get(i)+ offset, detune);



        }


        //disable uneeded osc
        for(int i = notes.noteFreqs.size() ; i < polyphony ; i ++){

            mOscSawArray.get(i).setDisable();
            mOscSineArray.get(i).setDisable();
            mOscSqrArray.get(i).setDisable();
            mOscTriArray.get(i).setDisable();

            mOscSawArray2.get(i).setDisable();
            mOscSineArray2.get(i).setDisable();
            mOscSqrArray2.get(i).setDisable();
            mOscTriArray2.get(i).setDisable();



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


            setNotes(Dmin , detuneValue , 0);

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


/*
                mOscSaw.frequency.set(Constants.NoteD4);
                mOscSaw2.frequency.set(Constants.NoteF4);
                mOscSaw3.frequency.set(Constants.NoteG4);

                mOscSaw.amplitude.set(0.8);
                mOscSaw2.amplitude.set(0.8);
                mOscSaw3.amplitude.set(0.8);*/


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
                        setNotes(note , detuneValue , 0);









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
                        setNotes(note , detuneValue , 0);
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



/*


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
            mOscTri3.frequency.set(frequency);*/



    }



    public void stopTestPitch(){


/*

        mOscSaw.amplitude.set(0.0);
*/




    }


    public void setDetuneValue(double detuneValue) {
        this.detuneValue = detuneValue;
    }


    public void loadData(RoomSynthData rsd){
        {
/*

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
        setADSRFilter(env_attack_duration_filter,env_decay_duration_filter,env_sustain_value_filter,env_release_duration_filter);*/

            final RoomSynthData RoomSynthData = rsd;

            detuneValue = RoomSynthData.detuneValue ;

            env_attack_duration = RoomSynthData.env_attack_duration;
            env_decay_duration = RoomSynthData.env_decay_duration;
            env_sustain_duration = RoomSynthData.env_sustain_duration;
            env_release_duration = RoomSynthData.env_release_duration;

            env_attack_value = RoomSynthData.env_attack_value;
            env_decay_value = RoomSynthData.env_decay_value;
            env_sustain_value = RoomSynthData.env_sustain_value;
            env_release_value = RoomSynthData.env_release_value;

            ENABLE_FILTER_ADSR = RoomSynthData.enable_filter_adsr;


            ENABLE_PORTA = RoomSynthData.enable_porta;
            DELAY_ENABLED = RoomSynthData.enable_delay;
            DELAY_TIME = RoomSynthData.DELAY_TIME;
            num_of_delay_voices = RoomSynthData.num_of_delay_voices;


            env_attack_duration_filter = RoomSynthData.env_attack_duration_filter;
            env_decay_duration_filter = RoomSynthData.env_decay_duration_filter;
            env_sustain_duration_filter = RoomSynthData.env_sustain_duration_filter;
            env_release_duration_filter = RoomSynthData.env_release_duration_filter;

            env_attack_value_filter = RoomSynthData.env_attack_value_filter ;
            env_decay_value_filter = RoomSynthData.env_decay_value_filter ;
            env_sustain_value_filter = RoomSynthData.env_sustain_value_filter;
            env_release_value_filter = RoomSynthData.env_release_value_filter;



            DisableSaw = RoomSynthData.DisableSaw;
            DisableSine = RoomSynthData.DisableSine;
            DisableSqr = RoomSynthData.DisableSqr;
            DisableTri = RoomSynthData.DisableTri;

            DisableSaw2 = RoomSynthData.DisableSaw2;
            DisableSine2 = RoomSynthData.DisableSine2;
            DisableSqr2 = RoomSynthData.DisableSqr2;
            DisableTri2 = RoomSynthData.DisableTri2;

            ENABLE_LOW_PASS = RoomSynthData.enableLowPass;
            ENABLE_HIGH_PASS = RoomSynthData.enableHighPass;



            ENABLE_LFO = RoomSynthData.ENABLE_LFO;
            SineLFO.frequency.set(RoomSynthData.LFO_freq);
            SineLFO.amplitude.set(RoomSynthData.LFO_amp);

            ENABLE_PM = RoomSynthData.ENABLE_PM;
            SinePMFeeder.frequency.set(RoomSynthData.PM_freq);
            SinePMFeeder.amplitude.set(RoomSynthData.PM_amp);


            ENABLE_PHASER = RoomSynthData.ENABLE_PHASER;
            phaseShifterLFO.amplitude.set(RoomSynthData.PHASER_amp);
            phaseShifter.depth.set(RoomSynthData.PHASER_depth);
            phaseShifterLFO.frequency.set(RoomSynthData.PHASER_freq);
            phaseShifter.feedback.set(RoomSynthData.PHASER_feedback);




            setADSR(env_attack_duration,env_decay_duration,env_sustain_duration,env_release_duration);
            setADSRFilter(env_attack_duration_filter,env_decay_duration_filter,env_sustain_value_filter,env_release_duration_filter);



        }
    }



    public void loadData(SynthData sd){
/*

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
        setADSRFilter(env_attack_duration_filter,env_decay_duration_filter,env_sustain_value_filter,env_release_duration_filter);*/

        final RoomSynthData RoomSynthData = new RoomSynthData();


        detuneValue = RoomSynthData.detuneValue ;

        env_attack_duration = RoomSynthData.env_attack_duration;
        env_decay_duration = RoomSynthData.env_decay_duration;
        env_sustain_duration = RoomSynthData.env_sustain_duration;
        env_release_duration = RoomSynthData.env_release_duration;

        env_attack_value = RoomSynthData.env_attack_value;
        env_decay_value = RoomSynthData.env_decay_value;
        env_sustain_value = RoomSynthData.env_sustain_value;
        env_release_value = RoomSynthData.env_release_value;

        ENABLE_FILTER_ADSR = RoomSynthData.enable_filter_adsr;


        ENABLE_PORTA = RoomSynthData.enable_porta;
        DELAY_ENABLED = RoomSynthData.enable_delay;
        DELAY_TIME = RoomSynthData.DELAY_TIME;
        num_of_delay_voices = RoomSynthData.num_of_delay_voices;


        env_attack_duration_filter = RoomSynthData.env_attack_duration_filter;
        env_decay_duration_filter = RoomSynthData.env_decay_duration_filter;
        env_sustain_duration_filter = RoomSynthData.env_sustain_duration_filter;
        env_release_duration_filter = RoomSynthData.env_release_duration_filter;

        env_attack_value_filter = RoomSynthData.env_attack_value_filter ;
        env_decay_value_filter = RoomSynthData.env_decay_value_filter ;
        env_sustain_value_filter = RoomSynthData.env_sustain_value_filter;
        env_release_value_filter = RoomSynthData.env_release_value_filter;



        DisableSaw = RoomSynthData.DisableSaw;
        DisableSine = RoomSynthData.DisableSine;
        DisableSqr = RoomSynthData.DisableSqr;
        DisableTri = RoomSynthData.DisableTri;

        DisableSaw2 = RoomSynthData.DisableSaw2;
        DisableSine2 = RoomSynthData.DisableSine2;
        DisableSqr2 = RoomSynthData.DisableSqr2;
        DisableTri2 = RoomSynthData.DisableTri2;




        ENABLE_LOW_PASS = RoomSynthData.enableLowPass;
        ENABLE_HIGH_PASS = RoomSynthData.enableHighPass;



        ENABLE_LFO = RoomSynthData.ENABLE_LFO;
        SineLFO.frequency.set(RoomSynthData.LFO_freq);
        SineLFO.amplitude.set(RoomSynthData.LFO_amp);

        ENABLE_PM = RoomSynthData.ENABLE_PM;
        SinePMFeeder.frequency.set(RoomSynthData.PM_freq);
        SinePMFeeder.amplitude.set(RoomSynthData.PM_amp);

        ENABLE_PHASER = RoomSynthData.ENABLE_PHASER;
        phaseShifterLFO.amplitude.set(RoomSynthData.PHASER_amp);
        phaseShifter.depth.set(RoomSynthData.PHASER_depth);
        phaseShifterLFO.frequency.set(RoomSynthData.PHASER_freq);
        phaseShifter.feedback.set(RoomSynthData.PHASER_feedback);



        setADSR(env_attack_duration,env_decay_duration,env_sustain_duration,env_release_duration);
        setADSRFilter(env_attack_duration_filter,env_decay_duration_filter,env_sustain_value_filter,env_release_duration_filter);



    }



    public void saveData(){


/*
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
*/


        final RoomSynthData RoomSynthData = new RoomSynthData();


        RoomSynthData.title = title;

        RoomSynthData.detuneValue = detuneValue;

        RoomSynthData.env_attack_duration = env_attack_duration;
        RoomSynthData.env_decay_duration = env_decay_duration;
        RoomSynthData.env_sustain_duration = env_sustain_duration;
        RoomSynthData.env_release_duration = env_release_duration;

        RoomSynthData.env_attack_value = env_attack_value;
        RoomSynthData.env_decay_value = env_decay_value;
        RoomSynthData.env_sustain_value = env_sustain_value;
        RoomSynthData.env_release_value = env_release_value;

        RoomSynthData.enable_filter_adsr = envPlayerFilter.isEnabled();
        RoomSynthData.enable_porta = ENABLE_PORTA;
        RoomSynthData.enable_delay = DELAY_ENABLED;

        RoomSynthData.env_attack_duration_filter = env_attack_duration_filter;
        RoomSynthData.env_decay_duration_filter = env_decay_duration_filter;
        RoomSynthData.env_sustain_duration_filter = env_sustain_duration_filter;
        RoomSynthData.env_release_duration_filter = env_release_duration_filter;

        RoomSynthData.env_attack_value_filter = env_attack_value_filter;
        RoomSynthData.env_decay_value_filter = env_decay_value_filter;
        RoomSynthData.env_sustain_value_filter = env_sustain_value_filter;
        RoomSynthData.env_release_value_filter = env_release_value_filter;

        RoomSynthData.DELAY_TIME = DELAY_TIME;
        RoomSynthData.num_of_delay_voices = num_of_delay_voices;
        RoomSynthData.DisableSaw = DisableSaw;
        RoomSynthData.DisableSine = DisableSine;
        RoomSynthData.DisableSqr = DisableSqr;
        RoomSynthData.DisableTri = DisableTri;

        RoomSynthData.DisableSaw2 = DisableSaw2;
        RoomSynthData.DisableSine2 = DisableSine2;
        RoomSynthData.DisableSqr2 = DisableSqr2;
        RoomSynthData.DisableTri2 = DisableTri2;

        RoomSynthData.enableLowPass = mLowPassFilter.isEnabled();
        RoomSynthData.enableHighPass = mHighPassFilter.isEnabled();

        RoomSynthData.ENABLE_LFO = ENABLE_LFO;
        RoomSynthData.LFO_freq = SineLFO.frequency.get();
        RoomSynthData.LFO_amp = SineLFO.amplitude.get();

        RoomSynthData.ENABLE_PM = ENABLE_PM;
        RoomSynthData.PM_freq = SinePMFeeder.frequency.get();
        RoomSynthData.PM_amp = SinePMFeeder.amplitude.get();

        RoomSynthData.ENABLE_PHASER = ENABLE_PHASER;
        RoomSynthData.PHASER_amp = phaseShifterLFO.amplitude.get();
        RoomSynthData.PHASER_depth = phaseShifter.depth.get();
        RoomSynthData.PHASER_freq = phaseShifterLFO.frequency.get();
        RoomSynthData.PHASER_feedback = phaseShifter.feedback.get();


        new Thread(new Runnable() {
            @Override
            public void run() {

                RoomSingleton.db.roomDAO().insertAll(RoomSynthData);

            }
        }).start();








    }





































}

