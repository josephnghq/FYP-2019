package com.example.joseph.fyp;

import java.util.ArrayList;

/**
 * Created by Joseph on 9/18/2018.
 */


//Call the function and they will return the scale based on where x OR x and y is



    //call this function to play notes
    //this class handles note playings, and vibrato

public class Scales {


   static int currentPos = -1;
   static int oldNote = -1;
   static int currentNote = -1;


    //use amount to indicate how much to deviate from original frequency, useful in vibrato
    //this function is used in the sliding feature





    //for shift mode, 1 goes up one octave, 0 goes down one octave

    public static boolean chordPoint ( double amount , int x , ArrayList<Notes> notesArrayList , Synth mSynth , int noOfFingers){





       if(noOfFingers == 2){

           x = x + 4;


       }

       if(noOfFingers == 3){

           x = x + 8;


       }





       if(x >= notesArrayList.size()){


           mSynth.setNotes(notesArrayList.get(notesArrayList.size()-1) , -1 , amount);


           currentNote = notesArrayList.size()-1;

       }

       else {
           mSynth.setNotes(notesArrayList.get(x), -1, amount);
           currentNote = x;
       }

       if(currentNote != oldNote){

           oldNote = currentNote;
           return true;


       }

       if(currentNote == oldNote){

           oldNote = currentNote;
           return false;


       }

    return true;


   }


    //use amount to indicate how much to deviate from original frequency, useful in vibrato
    //this function is used in the sliding feature

    public static boolean chord (int amount , int x , int width, ArrayList<Notes> notesArrayList , Synth mSynth){

        int divider = width / notesArrayList.size();
        int i = 1;



        boolean changedNote = false; //false means the notes didn't change, true means the notes changed

        // set first note
        if(x <= divider){
            mSynth.setNotes(notesArrayList.get(0) , -1 , amount);
             if(currentPos != 0){
                changedNote = true;
            }
            currentPos = 0;
        }


        //rest of the notes
        while( i < notesArrayList.size()){

            if( x > divider * (i) && x < divider * (i + 1)){

                mSynth.setNotes(notesArrayList.get(i) , -1 , amount);
                 if(currentPos != i){
                    changedNote = true;
                }
                currentPos = i;

            }

            i++;

        }

        return changedNote;

    }







}
