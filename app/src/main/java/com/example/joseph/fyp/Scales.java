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


    public static boolean chordPoint ( int amount , int x , ArrayList<Notes> notesArrayList , Synth mSynth , int noOfFingers){



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
           currentNote = notesArrayList.size()-1;
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



    public static void C251 (int x , int width, Synth mSynth){



        int divider = width/3;


        if(x <= divider){
            mSynth.traid(Constants.NoteC4 , Constants.NoteE5 , Constants.NoteG4) ;
        }

        else if (x > divider && x < divider*2){

            mSynth.traid(Constants.NoteD4 , Constants.NoteF4 , Constants.NoteA4);

        }
        else if (x > divider*2 && x < divider*3){

            mSynth.traid(Constants.NoteG4 , Constants.NoteB4 , Constants.NoteD5);

        }






    }









    public static void CPentatonic(int x , int width, Synth mSynth){
        {

            int divider = width/5;


            if(x <= divider){
                mSynth.setFrequencyWithPorta(Constants.NoteC4);
            }
            else if (x > divider && x < divider*2){

                mSynth.setFrequencyWithPorta(Constants.NoteD4);

            }
            else if (x > divider*2 && x < divider*3){

                mSynth.setFrequencyWithPorta(Constants.NoteE4);

            }
            else if (x > divider*3 && x < divider*4){

                mSynth.setFrequencyWithPorta(Constants.NoteG4);

            }
            else if (x > divider*4 && x < divider*5){
                mSynth.setFrequencyWithPorta(Constants.NoteA4);

            }




        }
    }


    public static void CPentatonic2Oct(int x , int y, int height, int width, Synth mSynth){
        {

            int divider = width/5;
            int height_divider = height/2;


            if(y >= height_divider) {

                if (x <= divider) {
                    mSynth.setFrequencyWithPorta(Constants.NoteC4);
                } else if (x > divider && x < divider * 2) {

                    mSynth.setFrequencyWithPorta(Constants.NoteD4);

                } else if (x > divider * 2 && x < divider * 3) {

                    mSynth.setFrequencyWithPorta(Constants.NoteE4);

                } else if (x > divider * 3 && x < divider * 4) {

                    mSynth.setFrequencyWithPorta(Constants.NoteG4);

                } else if (x > divider * 4 && x < divider * 5) {
                    mSynth.setFrequencyWithPorta(Constants.NoteA4);

                }
            }


            else {
                if (x <= divider && y < height_divider) {
                    mSynth.setFrequencyWithPorta(Constants.NoteC5);
                } else if (x > divider && x < divider * 2 && y < height_divider) {

                    mSynth.setFrequencyWithPorta(Constants.NoteD5);

                } else if (x > divider * 2 && x < divider * 3 && y < height_divider) {

                    mSynth.setFrequencyWithPorta(Constants.NoteE5);

                } else if (x > divider * 3 && x < divider * 4 && y < height_divider) {

                    mSynth.setFrequencyWithPorta(Constants.NoteG5);

                } else if (x > divider * 4 && x < divider * 5 && y < height_divider) {
                    mSynth.setFrequencyWithPorta(Constants.NoteA5);

                }
            }



        }
    }




    public static void CMajorScale(int x , int width, Synth mSynth){

        int divider = width/13;


        if(x <= divider){
            mSynth.setFrequencyWithPorta(Constants.NoteC4);
        }
        else if (x > divider && x < divider*2){

            mSynth.setFrequencyWithPorta(Constants.NoteD4b);

        }
        else if (x > divider*2 && x < divider*3){

            mSynth.setFrequencyWithPorta(Constants.NoteD4);

        }
        else if (x > divider*3 && x < divider*4){

            mSynth.setFrequencyWithPorta(Constants.NoteE4b);

        }
        else if (x > divider*4 && x < divider*5){

            mSynth.setFrequencyWithPorta(Constants.NoteE4);

        }
        else if (x > divider*5 && x < divider*6){

            mSynth.setFrequencyWithPorta(Constants.NoteF4);

        }

        else if (x > divider*6 && x < divider*7){

            mSynth.setFrequencyWithPorta(Constants.NoteG4b);

        }
        else if (x > divider*7 && x < divider*8){

            mSynth.setFrequencyWithPorta(Constants.NoteG4);

        }
        else if (x > divider*8 && x < divider*9){

            mSynth.setFrequencyWithPorta(Constants.NoteA4b);

        }
        else if (x > divider*9 && x < divider*10){

            mSynth.setFrequencyWithPorta(Constants.NoteA4);

        }
        else if (x > divider*10 && x < divider*11){

            mSynth.setFrequencyWithPorta(Constants.NoteB4b);

        }
        else if (x > divider*11 && x < divider*12){

            mSynth.setFrequencyWithPorta(Constants.NoteB4);

        }
        else if (x > divider*12 && x < divider*13){

            mSynth.setFrequencyWithPorta(Constants.NoteC5);

        }





    }














}
