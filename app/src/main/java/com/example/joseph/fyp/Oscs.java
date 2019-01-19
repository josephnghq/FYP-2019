package com.example.joseph.fyp;

import android.util.Log;

import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.UnitOscillator;

import java.util.ArrayList;

/**
 * Created by Joseph on 9/26/2018.
 */

public class Oscs {



    private int noOfVoices = 3;
    private ArrayList<UnitOscillator> mOscArray;
    public final static int  SAW = 0;
    public final static int SINE = 1;
    public final static int SQR = 2;
    public final static int TRI = 3;
    private double currentFreq = 0;


    public Oscs(int noOfVoices, int choice){

        this.noOfVoices = noOfVoices;
        mOscArray = new ArrayList<UnitOscillator>(noOfVoices);

        for(int i = 0 ; i < noOfVoices; i ++){


            switch(choice) {

                case SAW:
                    mOscArray.add(new SawtoothOscillatorBL());
                    break;

                case SINE:
                    mOscArray.add(new SineOscillator());
                    break;

                case SQR:
                    mOscArray.add(new SquareOscillator());
                    break;

                case TRI:
                    mOscArray.add(new TriangleOscillator());
                    break;




            }

        }


    }

    public ArrayList<UnitOscillator> getmOscArray() {
        return mOscArray;
    }

    public void setFrequency(double frequency){


        for(int i = 0 ; i < noOfVoices; i ++){
            mOscArray.get(i).frequency.set(frequency);

        }


    }

    public double getCurrentFreq() {

        return currentFreq;

    }

    public void setAmp(double amp){


        for(int i = 0 ; i < noOfVoices; i ++){
            mOscArray.get(i).amplitude.set(amp);

        }

    }


    //detuned handled by this function

    public void setFrequency(double frequency, double detune){

        currentFreq = frequency;


        for(int i = 0 ; i < noOfVoices; i ++){
            if(i == 0)
            mOscArray.get(i).frequency.set(frequency);

            else if(i%2 ==0){
                mOscArray.get(i).frequency.set(frequency + detune);

            }else{
                mOscArray.get(i).frequency.set(frequency - detune);

            }

        }


    }

    public void setEnable(){

        for(int i = 0 ; i < noOfVoices; i ++){
            mOscArray.get(i).setEnabled(true);
        }

    }

    public void setDisable(){

        for(int i = 0 ; i < noOfVoices; i ++){
            mOscArray.get(i).setEnabled(false);
        }

    }








}


