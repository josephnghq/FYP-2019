package com.example.joseph.fyp;

/**
 * Created by Joseph on 7/12/2018.
 */

public class Constants {


    //middle C is C4, 24th note from left

    static double NoteC4 = 261.63;
    static double NoteD4b =  277.18;
    static double NoteD4 = 293.66;
    static double NoteE4b = 311.13;
    static double NoteE4 = 329.63;
    static double NoteF4 = 349.23;
    static double NoteG4b =  369.99;
    static double NoteG4 = 392.00;
    static double NoteA4b = 415.30;
    static double NoteA4 = 440;
    static double NoteB4b =  466.16;
    static double NoteB4 = 493.88;
    static double NoteC5 = 523.25;
    static double NoteD5b = 554.37;
    static double NoteD5 = 587.33;
    static double NoteE5b = 622.25;
    static double NoteE5 = 659.25;
    static double NoteF5 = 698.46;
    static double NoteG5b = 739.99;
    static double NoteG5 = 783.99;
    static double NoteA5b = 830.61;
    static double NoteA5 = 880;
    static double NoteB5b = 932.33;
    static double NoteB5 = 987.77;
    static double NoteC6 = 1046.50;


    static double[] NoteC4Pos = {24 , 261.63};
    static double[] NoteD4bPos =  {25 , 277.18};
    static double[] NoteD4Pos = {26 , 293.66};
    static double[] NoteE4bPos = {27 , 311.13};
    static double[] NoteE4Pos = {28 , 329.63};
    static double[] NoteF4Pos = {29 , 349.23};
    static double[] NoteG4bPos =  {30 , 369.99};
    static double[] NoteG4Pos = {31 , 392.00};
    static double[] NoteA4bPos = {32 ,415.30};
    static double[] NoteA4Pos = {33 , 440};
    static double[] NoteB4bPos =  {34, 466.16};
    static double[] NoteB4Pos = {35 , 493.88};
    static double[] NoteC5Pos = {36, 523.25};
    static double[] NoteD5bPos = {37, 554.37};
    static double[] NoteD5Pos = {38 , 587.33};
    static double[] NoteE5bPos = {39 ,622.25};
    static double[] NoteE5Pos = {40 , 659.25};
    static double[] NoteF5Pos = {41 , 698.46};
    static double[] NoteG5bPos = {42 , 739.99};
    static double[] NoteG5Pos = {43 , 783.99};
    static double[] NoteA5bPos = {44 , 830.61};
    static double[] NoteA5Pos = {45 , 880};
    static double[] NoteB5bPos = {46 , 932.33};
    static double[] NoteB5Pos = {47 ,987.77};
    static double[] NoteC6Pos = {48 , 1046.50};


    //position is in relative to piano key, 0 is for lowest note on piano
    public static double getNote(int position){

        switch(position){

            case 24:
                return NoteC4;
            case 25:
                return NoteD4b;
            case 26:
                return NoteD4;
            case 27:
                return NoteE4b;
            case 28:
                return NoteE4;
            case 29:
                return NoteF4;
            case 30:
                return NoteG4b;
            case 31:
                return NoteG4;
            case 32:
                return NoteA4b;
            case 33:
                return NoteA4;
            case 34:
                return NoteB4b;
            case 35:
                return NoteB4;
            case 36:
                return NoteC5;
            case 37:
                return NoteD5b;
            case 38:
                return NoteD5;
            case 39:
                return NoteE5b;
            case 40:
                return NoteE5;
            case 41:
                return NoteF5;
            case 42:
                return NoteG5b;
            case 43:
                return NoteG5;
            case 44:
                return NoteA5b;
            case 45:
                return NoteA5;
            case 46:
                return NoteB5b;
            case 47:
                return NoteB5;
            case 48:
                return NoteC6;
             default:
                 return NoteC4;




        }










    }









}

