package com.example.joseph.fyp;

import android.util.Log;

import com.jsyn.JSyn;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.unitgen.FilterHighPass;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.VariableRateMonoReader;

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

    private  double[] envVol =
            { 0.6 , 0.4,
                    0.3 , 0.25,
                    1 , 0.2,
                    1.3 , 0
            };




    private SegmentedEnvelope envForVol = new SegmentedEnvelope(envVol);
    private VariableRateMonoReader envPlayer = new VariableRateMonoReader();




    public Synth(){

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



        mSynth.add(mOut);
        mSynth.add(mLowPassFilter);
        mSynth.add(mHighPassFilter);
        mSynth.add(envPlayer);

        setupCircuit();

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


    }



    private void setupCircuit(){


        // osc -> filter -> output
        // env volume controls amp of oscs




        mLowPassFilter.frequency.set(20000);
        mLowPassFilter.Q.set(0.2);
        mLowPassFilter.output.connect( 0, mOut.input, 0 ); /* Left side */
        mLowPassFilter.output.connect( 0, mOut.input, 1 );

        mHighPassFilter.frequency.set(0);
        mHighPassFilter.Q.set(0.2);
        mHighPassFilter.output.connect(0,mOut.input , 0);
        mHighPassFilter.output.connect(0,mOut.input , 1);



      /*  mOscSaw.amplitude.set(0);
        mOscSaw2.amplitude.set(0);
        mOscSaw3.amplitude.set(0);
*/
    /*    mOscSine.amplitude.set(0);
        mOscSine2.amplitude.set(0);
        mOscSine3.amplitude.set(0);*/






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



    }


    public void selectLowPass(){

        mLowPassFilter.setEnabled(true);
        mHighPassFilter.setEnabled(false);

    }

    public void selectHighPass(){

        mLowPassFilter.setEnabled(false);
        mHighPassFilter.setEnabled(true);

    }




    public void disableSaw(){

    mOscSaw.setEnabled(false);
    mOscSaw2.setEnabled(false);
    mOscSaw3.setEnabled(false);



    }

    public void disableSqr(){

        mOscSqr.setEnabled(false);
        mOscSqr2.setEnabled(false);
        mOscSqr3.setEnabled(false);


    }

    public void disableSine(){

        mOscSine.setEnabled(false);
        mOscSine2.setEnabled(false);
        mOscSine3.setEnabled(false);



    }

    public void disableTri(){

        mOscTri.setEnabled(false);
        mOscTri2.setEnabled(false);
        mOscTri3.setEnabled(false);


    }

    public void enableSaw(){

        mOscSaw.setEnabled(true);
        mOscSaw2.setEnabled(true);
        mOscSaw3.setEnabled(true);

    }

    public void enableSine(){

        mOscSine.setEnabled(true);
        mOscSine2.setEnabled(true);
        mOscSine3.setEnabled(true);


    }


    public void enableSqr(){

        mOscSqr.setEnabled(true);
        mOscSqr2.setEnabled(true);
        mOscSqr3.setEnabled(true);

    }

    public void enableTri(){


        mOscTri.setEnabled(true);
        mOscTri2.setEnabled(true);
        mOscTri3.setEnabled(true);

    }




    public void playTestPitch(){



        mOscSaw.frequency.set(Constants.NoteCFreq);
        mOscSaw2.frequency.set(Constants.NoteEfreq);
        mOscSaw3.frequency.set(Constants.NoteGfreq);

        mOscSaw.amplitude.set(0.8);
        mOscSaw2.amplitude.set(0.8);
        mOscSaw3.amplitude.set(0.8);


        envPlayer.dataQueue.clear();
        envPlayer.dataQueue.queue(envForVol, 0, 4 );
         envPlayer.start();


        Log.i("FYP" ,"Playing");



    }


    public void Dmin(){
        {





            mOscSaw.frequency.set(Constants.NoteDFreq);
            mOscSaw2.frequency.set(Constants.NoteFfreq);
            mOscSaw3.frequency.set(Constants.NoteAfreq);



            mOscSine.frequency.set(Constants.NoteDFreq);
            mOscSine2.frequency.set(Constants.NoteFfreq);
            mOscSine3.frequency.set(Constants.NoteAfreq);

            mOscSqr.frequency.set(Constants.NoteDFreq);
            mOscSqr2.frequency.set(Constants.NoteFfreq);
            mOscSqr3.frequency.set(Constants.NoteAfreq);

            mOscTri.frequency.set(Constants.NoteDFreq);
            mOscTri2.frequency.set(Constants.NoteFfreq);
            mOscTri3.frequency.set(Constants.NoteAfreq);



            envPlayer.dataQueue.clear();
            envPlayer.dataQueue.queue(envForVol, 0, 4 );
            envPlayer.start();





        }
    }

    public void G7(){


        {
            {



                mOscSaw.frequency.set(Constants.NoteDFreq);
                mOscSaw2.frequency.set(Constants.NoteFfreq);
                mOscSaw3.frequency.set(Constants.NoteGfreq);

                mOscSaw.amplitude.set(0.8);
                mOscSaw2.amplitude.set(0.8);
                mOscSaw3.amplitude.set(0.8);


                envPlayer.dataQueue.clear();
                envPlayer.dataQueue.queue(envForVol, 0, 4 );
                envPlayer.start();





            }
        }




    }






    public void stopTestPitch(){



        mOscSaw.amplitude.set(0.0);




    }




}

