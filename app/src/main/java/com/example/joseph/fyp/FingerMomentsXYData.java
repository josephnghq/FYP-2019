package com.example.joseph.fyp;

/**
 * Created by Joseph on 1/12/2019.
 */


//class used to hold the data of moments detected by finger detection contour, holds their id , x and y points
public class FingerMomentsXYData {



    public int x;
    public int y;
    public int id;
    public boolean FIRST_HALF;

    public FingerMomentsXYData(int id , int x , int y , boolean FIRST_HALF){

        this.id = id;
        this.x = x;
        this.y = y;
        this.FIRST_HALF = FIRST_HALF;

    }


}
