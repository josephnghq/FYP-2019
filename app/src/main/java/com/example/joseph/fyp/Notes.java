package com.example.joseph.fyp;

import java.util.ArrayList;

/**
 * Created by Joseph on 9/25/2018.
 */

//this class contains notes information that is feed into an oscillator for it to play

public class Notes {

    ArrayList<Double> noteFreqs = new ArrayList<>();


    public Notes(int size){
        noteFreqs = new ArrayList<>(size);
    }

    public void copyNotesFrom(Notes source){

        noteFreqs = source.noteFreqs;


    }

    public void addNotes(double frequency, int at){

        noteFreqs.add(at,frequency);


    }


    public void addNotes(double frequency){

        noteFreqs.add(frequency);


    }


}
