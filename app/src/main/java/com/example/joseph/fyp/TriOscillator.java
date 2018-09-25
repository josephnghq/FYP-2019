package com.example.joseph.fyp;

import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.TriangleOscillator;

import java.util.ArrayList;

/**
 * Created by Joseph on 9/25/2018.
 */

public class TriOscillator {



    private int noOfVoices = 3;
    private ArrayList<TriangleOscillator> mOscArray;

    public TriOscillator(int noOfVoices){

        this.noOfVoices = noOfVoices;
        mOscArray = new ArrayList<TriangleOscillator>(noOfVoices);


        for(int i = 0 ; i < noOfVoices; i ++){
            mOscArray.add(new TriangleOscillator());
        }

    }

    public ArrayList<TriangleOscillator> getmOscArray() {
        return mOscArray;
    }
}
