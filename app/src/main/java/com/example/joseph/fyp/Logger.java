package com.example.joseph.fyp;

import android.util.Log;

/**
 * Created by Joseph on 3/12/2019.
 */



//general purpose log class
public class Logger {

    static boolean DO_LOG = true;



    public static void Log(String message){

        if(DO_LOG)
        Log.i("FYP" , message);


    }




}
