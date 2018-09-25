package com.example.joseph.fyp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.ArrayList;
import java.util.List;

public class OpenCVTestActivity extends AppCompatActivity {

    CameraBridgeViewBase mOpenCvCameraView;


    private BaseLoaderCallback mLoaderCallback;

    private boolean              mIsColorSelected = false;
    private Scalar               mBlobColorRgba;
    private Scalar               mBlobColorHsv;
    private ColorBlobDetector    mDetector;
    private Mat                  mSpectrum;
    private Size                 SPECTRUM_SIZE;
    private Scalar               CONTOUR_COLOR;

    private double hue_start = 119;
    private double hue_stop = 149;
    private double sat_start = 174;
    private double sat_stop = 196;
    private double val_start = 146;
    private double val_stop = 181;
    private Synth mSynth;

    private boolean SYNTH_PLAYING = true;

    private int width = 0;
    private int height = 0;


    SharedPreferences sharedPref;
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson;

    private ArrayList<SynthData> listOfSynthData = new ArrayList<SynthData>();



    private Mat mIntermediateMat;

    private Mat mRgba;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cvtest);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);



        sharedPref=  this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        gson = builder.create();

        String gsonString = sharedPref.getString(getString(R.string.preference_file_key) , "");


        if(gsonString.length()>0){
            listOfSynthData = gson.fromJson(gsonString,new TypeToken<ArrayList<SynthData>>(){}.getType());
        }


        ArrayList<String> synthDataTitles = new ArrayList<String>();
        for(int i = 0 ; i < listOfSynthData.size(); i++){

            synthDataTitles.add(listOfSynthData.get(i).title);


        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list_for_xy, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("List");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,synthDataTitles);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("FYP" , "CLICKED CLICKED CLICKED");
                mSynth = new Synth(listOfSynthData.get(position));
                mSynth.playOsc();

            }
        });
        alertDialog.show();





        setupBars();

        Button btn = (Button)findViewById(R.id.button_mute_audio_open_CV);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSynth.mute();
            }
        });
        mSynth= new Synth();
        mSynth.disableFilterEnv();
        mSynth.selectHighPass();
        mSynth.setfreqQ(6);


       // mSynth.playOsc();

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener() {
            @Override
            public void onCameraViewStarted(int width, int height) {


                mDetector = new ColorBlobDetector();
                mSpectrum = new Mat();
                mBlobColorRgba = new Scalar(255);
                mBlobColorHsv = new Scalar(255);
                SPECTRUM_SIZE = new Size(200, 64);
                CONTOUR_COLOR = new Scalar(255,0,0,255);

            }

            @Override
            public void onCameraViewStopped() {

            }



            @Override
            public Mat onCameraFrame(Mat inputFrame) {

               // mIntermediateMat = new Mat();

                mRgba = inputFrame;

                Core.flip(mRgba,mRgba,1);

                List<MatOfPoint> contours = mDetector.getContours();
                Size sizeRgba = mRgba.size();


                height = (int)sizeRgba.height;
                width = (int)sizeRgba.width;

                if (mIsColorSelected) {
                    mDetector.process(mRgba);
                    Log.e("FYP", "Contours count: " + contours.size());

                    if(contours.size() == 0 && SYNTH_PLAYING){
                        Log.i("FYP" , "RELEASEING OSC ");
                        mSynth.releaseOsc();
                        SYNTH_PLAYING = false;
                    }
                    if(!SYNTH_PLAYING && contours.size() > 0){
                        mSynth.playOsc();
                        SYNTH_PLAYING = true;
                    }



                    Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);

                    Mat colorLabel = mRgba.submat(4, 68, 4, 68);
                    colorLabel.setTo(mBlobColorRgba);

                    Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
                    mSpectrum.copyTo(spectrumLabel);
                }

                List<Moments> mu = new ArrayList<Moments>(contours.size());
                for (int i = 0; i < contours.size(); i++) {
                    mu.add(i, Imgproc.moments(contours.get(i), false));
                    Moments p = mu.get(i);
                    int x = (int) (p.get_m10() / p.get_m00());
                    int y = (int) (p.get_m01() / p.get_m00());

                    Log.i("FYP" , "For contour number " + i + " x is at " + x);
                    Log.i("FYP" , "For contour number " + i + " y is at " + y);

                    Scales.C251(x ,   width , mSynth);

                    if(!mSynth.isFilterEnvEnabled())
                        mSynth.setFilterValue(y*5);

                    Imgproc.circle(mRgba, new Point(x, y), 4, new Scalar(255,49,0,255));
                }


                return mRgba;

                /*System.gc();

                Core.flip(mRgba,mRgba,1);

                Size sizeRgba = mRgba.size();

                height = (int)sizeRgba.height;
                width = (int)sizeRgba.width;

                int rows = (int) sizeRgba.height;
                int cols = (int) sizeRgba.width;

                int left = cols / 8;
                int top = rows / 8;

                int width = cols * 3 / 4;
                int height = rows * 3 / 4;


               //Mat returner = new Mat();
                 Mat blurredImage = new Mat();
               // Mat hsvImage = new Mat();
                Mat mask = new Mat();
                Mat morphOutput = new Mat();


// remove some noise
                Imgproc.blur(mRgba, blurredImage, new Size(7, 7));

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



                Imgproc.erode(mask, morphOutput, erodeElement);
                Imgproc.erode(morphOutput, morphOutput, erodeElement);

                Imgproc.dilate(morphOutput, morphOutput, dilateElement);
                Imgproc.dilate(morphOutput, morphOutput, dilateElement);


                 Mat hierarchy = new Mat();




            //    Imgproc.cvtColor(morphOutput , morphOutput , Imgproc.COLOR_HSV2BGR);
             //   Imgproc.cvtColor(morphOutput , morphOutput , Imgproc.COLOR_BGR2GRAY);

                ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();

                 Imgproc.findContours(morphOutput, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
                // if any contour exist...
                if (hierarchy.size().height > 0 && hierarchy.size().width > 0)
                {
                    // for each contour, display it in blue
                    for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0])
                    {


                        Imgproc.drawContours(mRgba, contours, idx, new Scalar(0, 255, 0));
                        //Log.i("FYP" , "THere was a contour");
                    }
                }

                List<Moments> mu = new ArrayList<Moments>(contours.size());
                for (int i = 0; i < contours.size(); i++) {
                    mu.add(i, Imgproc.moments(contours.get(i), false));
                    Moments p = mu.get(i);
                    int x = (int) (p.get_m10() / p.get_m00());
                    int y = (int) (p.get_m01() / p.get_m00());

                    Log.i("FYP" , "For contour number " + i + " x is at " + x);
                    Log.i("FYP" , "For contour number " + i + " y is at " + y);

                    CMajorScale(x);

                    if(!mSynth.isFilterEnvEnabled())
                        mSynth.setFilterValue(y*5);

                    Imgproc.circle(mRgba, new Point(x, y), 4, new Scalar(255,49,0,255));
                }

              *//*  Mat rgbaInnerWindow = new Mat(inputFrame , new Rect(0, 0 ,width,height));
                Imgproc.resize(morphOutput,morphOutput,rgbaInnerWindow.size());
                morphOutput.copyTo(rgbaInnerWindow);*//*




*//*
                Mat rgbaInnerWindow;


                rgbaInnerWindow = inputFrame.submat(top, top + height, left, left + width);
                Imgproc.resize(morphOutput,morphOutput,rgbaInnerWindow.size());
                morphOutput.copyTo(rgbaInnerWindow);



               Imgproc.Canny(rgbaInnerWindow, mIntermediateMat, 80, 90);
                Imgproc.cvtColor(mIntermediateMat, rgbaInnerWindow, Imgproc.COLOR_GRAY2BGRA, 4);

                mIntermediateMat.release();
                rgbaInnerWindow.release();
*//*

            *//*   Mat corner = inputFrame.submat(top, top + height, left, left + width);
               Imgproc.resize(mask,mask,corner.size());


                Mat zoomCorner = inputFrame.submat(0, rows / 2 - rows / 10, 0, cols / 2 - cols / 10);
                Mat mZoomWindow = inputFrame.submat(rows / 2 - 9 * rows / 100, rows / 2 + 9 * rows / 100, cols / 2 - 9 * cols / 100, cols / 2 + 9 * cols / 100);
                Imgproc.resize(mask,mask,mZoomWindow.size());

                mask.copyTo(mZoomWindow);
                Imgproc.resize(mZoomWindow, zoomCorner, zoomCorner.size(), 0, 0, Imgproc.INTER_LINEAR_EXACT);*//*
              //  Size wsize = mZoomWindow.size();
               // Imgproc.rectangle(mZoomWindow, new Point(1, 1), new Point(wsize.width - 2, wsize.height - 2), new Scalar(255, 0, 0, 255), 2);


           //     Imgproc.rectangle(corner, new Point(1, 1), new Point(10, 10), new Scalar(255, 0, 0, 255), 2);



               // mask.copyTo(inputFrame);



                //  Imgproc.resize(mask, inputFrame, inputFrame.size(), 0, 0, Imgproc.INTER_LINEAR_EXACT);

               // Imgproc.rectangle(mask, new Point(1, 1), new Point(mask.size().width - 2, mask.size().height - 2), new Scalar(255, 0, 0, 255), 2);

*//*                hierarchy.release();
                dilateElement.release();
                erodeElement.release();*//*

                hierarchy.release();
                blurredImage.release();
                mask.release();
                morphOutput.release();




                return mRgba;*/
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
    public boolean onTouchEvent(MotionEvent event) {

        int cols = mRgba.cols();
        int rows = mRgba.rows();

        int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
        int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;

        int x = (int) event.getX() - xOffset;
        int y = (int) event.getY() - yOffset;

        Log.i("FYP", "Touch image coordinates: (" + x + ", " + y + ")");

        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;


        Rect touchedRect = new Rect();

        touchedRect.x = (x>4) ? x-4 : 0;
        touchedRect.y = (y>4) ? y-4 : 0;

        touchedRect.width = (x+4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
        touchedRect.height = (y+4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;

        Mat touchedRegionRgba = mRgba.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width*touchedRect.height;
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;

        mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);

        Log.i("FYP", "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");

        mDetector.setHsvColor(mBlobColorHsv);

        Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);

        mIsColorSelected = true;

        touchedRegionRgba.release();
        touchedRegionHsv.release();












        return false;






    }

    private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }

    private Mat overtrayImage(Mat background, Mat foreground ) {
        // The background and the foreground are assumed to be of the same size.
        Mat destination = new Mat( background.size(), background.type() );

        for ( int y = 0; y < ( int )( background.rows() ); ++y ) {
            for ( int x = 0; x < ( int )( background.cols() ); ++x ) {
                double b[] = background.get( y, x );
                double f[] = foreground.get( y, x );

                double alpha = f[3] / 255.0;

                double d[] = new double[3];
                for ( int k = 0; k < 3; ++k ) {
                    d[k] = f[k] * alpha + b[k] * ( 1.0 - alpha );
                }

                destination.put( y, x, d );
            }
        }

        return destination;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSynth.destory();


    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
    }


    private void CPentatonic(int x){
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


    private void CMajorScale(int x){

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



    private void setupBars(){


        SeekBar hueStartSb = (SeekBar) findViewById(R.id.seekBar_hue_start);
        hueStartSb.setMax(180);
        hueStartSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                hue_start = value;
                Log.i("FYP" , "value of hue_start is " + hue_start);

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
                Log.i("FYP" , "value of hue_stop is " + hue_stop);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SeekBar satStart = (SeekBar) findViewById(R.id.seekBar_sat_start);
        satStart.setMax(255);
        satStart.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                sat_start = value;
                Log.i("FYP" , "value of sat_start is " + sat_start);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SeekBar satStop = (SeekBar) findViewById(R.id.seekBar_sat_stop);
        satStop.setMax(255);
        satStop.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                sat_stop = value;
                Log.i("FYP" , "value of sat_stop is " + sat_stop);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SeekBar valStart = (SeekBar) findViewById(R.id.seekBar_val_start);
        valStart.setMax(255);
        valStart.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                val_start = value;
                Log.i("FYP" , "value of val_start is " + val_start);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar valStop = (SeekBar) findViewById(R.id.seekBar_val_stop);
        valStop.setMax(255);
        valStop.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double value = (double)progress;
                val_stop = value;
                Log.i("FYP" , "value of val_stop is " + val_stop);


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
