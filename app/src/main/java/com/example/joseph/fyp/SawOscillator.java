package com.example.joseph.fyp;

import com.jsyn.unitgen.SawtoothOscillatorBL;

import java.util.ArrayList;

/**
 * Created by Joseph on 9/25/2018.
 */

public class SawOscillator {


    private int noOfVoices = 3;
    private ArrayList<SawtoothOscillatorBL> mOscArray;

    public SawOscillator(int noOfVoices){

        this.noOfVoices = noOfVoices;
        mOscArray = new ArrayList<SawtoothOscillatorBL>(noOfVoices);

        for(int i = 0 ; i < noOfVoices; i ++){
            mOscArray.add(new SawtoothOscillatorBL());
        }


    }

    public ArrayList<SawtoothOscillatorBL> getmOscArray() {
        return mOscArray;
    }

    public void setFrequency(double frequency){


        for(int i = 0 ; i < noOfVoices; i ++){
            mOscArray.get(i).frequency.set(frequency);
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
