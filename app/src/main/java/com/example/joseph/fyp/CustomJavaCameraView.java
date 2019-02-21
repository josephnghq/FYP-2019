package com.example.joseph.fyp;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;

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


}
