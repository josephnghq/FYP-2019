package com.example.joseph.fyp;

import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.SquareOscillator;

import java.util.ArrayList;

/**
 * Created by Joseph on 9/25/2018.
 */

public class SqrOscillator {

    private int noOfVoices = 3;
    private ArrayList<SquareOscillator> mOscArray;

    public SqrOscillator(int noOfVoices){

        this.noOfVoices = noOfVoices;
        mOscArray = new ArrayList<SquareOscillator>(noOfVoices);


        for(int i = 0 ; i < noOfVoices; i ++){
            mOscArray.add(new SquareOscillator());
        }


    }

    public ArrayList<SquareOscillator> getmOscArray() {
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
