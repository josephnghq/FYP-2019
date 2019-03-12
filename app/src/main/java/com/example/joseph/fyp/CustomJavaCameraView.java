package com.example.joseph.fyp;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;

import org.opencv.android.JavaCameraView;

import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 2/21/2019.
 */

public class CustomJavaCameraView extends JavaCameraView {


    public CustomJavaCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomJavaCameraView(Context context, int cameraId) {
        super(context, cameraId);
    }

    @Override
    protected boolean connectCamera(int width, int height) {




         return super.connectCamera(width, height);




    }

    public void setExposureCompensation(int value){


        Camera.Parameters params = mCamera.getParameters();
        params.setExposureCompensation(value);
        mCamera.setParameters(params);


    }

    public void setFocus(Rect rec ) {

         Camera.Parameters params = mCamera.getParameters();

        List<String> lists = params.getSupportedFocusModes();

        for(int i = 0 ; i < lists.size(); i++ ){

            Log.i("FYP" , "Supported camera focus mode " + lists.get(i));

        }


        Log.i("FYP","max is " + params.getMaxNumMeteringAreas());

        /* List<Camera.Area> are = params.getMeteringAreas();

        if(are!=null)
        for(int i = 0 ; i < are.size(); i++){


            Log.i("FYP","max  are is " + are.get(i).rect.centerX() + " " + are.get(i).rect.centerY());



        }
*/




        Camera.Area area = new Camera.Area(rec,1000);
        List<Camera.Area> areaList = new ArrayList<Camera.Area>();
        areaList.add(area);

        params.setMeteringAreas(areaList);
        //params.setFocusAreas(areaList);

        mCamera.setParameters(params);






    }


}
