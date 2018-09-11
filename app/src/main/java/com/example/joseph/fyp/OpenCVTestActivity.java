package com.example.joseph.fyp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.SeekBar;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class OpenCVTestActivity extends AppCompatActivity {

    CameraBridgeViewBase mOpenCvCameraView;


    private BaseLoaderCallback mLoaderCallback;

    private double hue_start = 0;
    private double hue_stop = 255;
    private double sat_start = 0;
    private double sat_stop = 255;
    private double val_start = 0;
    private double val_stop = 255;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cvtest);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);

        setupBars();


        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener() {
            @Override
            public void onCameraViewStarted(int width, int height) {

            }

            @Override
            public void onCameraViewStopped() {

            }

            @Override
            public Mat onCameraFrame(Mat inputFrame) {


                System.gc();

               //Mat returner = new Mat();

                Mat blurredImage = new Mat();
               // Mat hsvImage = new Mat();
                Mat mask = new Mat();
                Mat morphOutput = new Mat();

// remove some noise
                Imgproc.blur(inputFrame, blurredImage, new Size(7, 7));

// convert the frame to HSV

                Imgproc.cvtColor(blurredImage, mask, Imgproc.COLOR_BGR2HSV);



                //hue , sat, value
                Scalar minValues = new Scalar(hue_start ,sat_start , val_start);
                Scalar maxValues = new Scalar(hue_stop,sat_stop,val_stop);


               // Scalar minValues = new Scalar(0,0,0);
             //   Scalar maxValues = new Scalar(150,150,150);
                Core.inRange(mask,minValues,maxValues,mask);







                Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2));
                Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2));



       /*         Imgproc.erode(mask, morphOutput, erodeElement);
                Imgproc.erode(morphOutput, morphOutput, erodeElement);

                Imgproc.dilate(morphOutput, morphOutput, dilateElement);
                Imgproc.dilate(morphOutput, morphOutput, dilateElement);*/


                 Mat hierarchy = new Mat();
/*

               Imgproc.cvtColor(morphOutput , morphOutput , Imgproc.COLOR_HSV2BGR);
                Imgproc.cvtColor(morphOutput , morphOutput , Imgproc.COLOR_BGR2GRAY);


                ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();


                Imgproc.findContours(morphOutput, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);



                // if any contour exist...
                if (hierarchy.size().height > 0 && hierarchy.size().width > 0)
                {
                    // for each contour, display it in blue
                    for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0])
                    {


                        Imgproc.drawContours(morphOutput, contours, idx, new Scalar(250, 0, 0));
                        //Log.i("FYP" , "THere was a contour");
                    }
                }

*/



                inputFrame = mask.clone();
                hierarchy.release();
                dilateElement.release();
                erodeElement.release();
                blurredImage.release();
                mask.release();
                morphOutput.release();




                return inputFrame;
            }
        });


         mLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS:
                    {
                        Log.i("FYP", "OpenCV loaded successfully");
                        mOpenCvCameraView.enableView();
                    } break;
                    default:
                    {
                        super.onManagerConnected(status);
                    } break;
                }
            }
        };




    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
    }



    private void setupBars(){


        SeekBar hueStartSb = (SeekBar) findViewById(R.id.seekBar_hue_start);
        hueStartSb.setMax(180);
        hueStartSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                hue_start = value;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar hueStopSB = (SeekBar) findViewById(R.id.seekBar_hue_stop);
        hueStopSB.setMax(180);
        hueStopSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                hue_stop = value;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SeekBar satStart = (SeekBar) findViewById(R.id.seekBar_sat_start);
        hueStopSB.setMax(255);
        hueStopSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                sat_start = value;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SeekBar satStop = (SeekBar) findViewById(R.id.seekBar_sat_stop);
        hueStopSB.setMax(255);
        hueStopSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                sat_stop = value;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SeekBar valStart = (SeekBar) findViewById(R.id.seekBar_val_start);
        hueStopSB.setMax(255);
        hueStopSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                val_start = value;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar valStop = (SeekBar) findViewById(R.id.seekBar_val_stop);
        hueStopSB.setMax(255);
        hueStopSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                val_stop = value;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }




}
