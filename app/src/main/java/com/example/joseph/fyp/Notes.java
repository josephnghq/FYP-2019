package com.example.joseph.fyp;

import android.util.Log;

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

    public Notes(){
        noteFreqs = new ArrayList<>();
    }

    public void printNotesIntoLog(){

        for(int i = 0 ; i < noteFreqs.size(); i ++){

            Log.i("FYP" ,"Freq is " + noteFreqs.get(i));


        }


    }



    //for this noteArray,shift every note in that chord/note
    public void shiftSemitone(int semitones , int mode) {

        // for every single note
        // for mode, 1 for up semitone, 0 for down


        if (mode == 1){

            for (int i = 0; i < noteFreqs.size(); i++) {

                for (int p = 0; p < semitones; p++) {

                    double tempNoteFreq = noteFreqs.get(i);

                    tempNoteFreq = tempNoteFreq * 1.0595;

                    noteFreqs.set(i, tempNoteFreq);


                }


            }
    }

   if (mode == 0){

        for (int i = 0; i < noteFreqs.size(); i++) {

            for (int p = 0; p < semitones; p++) {

                double tempNoteFreq = noteFreqs.get(i);

                tempNoteFreq = tempNoteFreq / 1.0595;

                noteFreqs.set(i, tempNoteFreq);


            }


        }
    }





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
