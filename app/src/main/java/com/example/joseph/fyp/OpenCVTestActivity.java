package com.example.joseph.fyp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

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
    private ColorBlobDetector    mDetector2;

    private Mat                  mSpectrum;
    private Size                 SPECTRUM_SIZE;
    private Scalar               CONTOUR_COLOR;
    private Scalar               CONTOUR_COLOR2;
    private GestureDetector      mGestureDetector;

    private int                  mTouchChoice = 0;

    private double hue_start = 119;
    private double hue_stop = 149;
    private double sat_start = 174;
    private double sat_stop = 196;
    private double val_start = 146;
    private double val_stop = 181;
    private Synth mSynth;
    private Synth mSynth2;


    private boolean SYNTH_PLAYING = true;
    private boolean SYNTH_PLAYING2 = true;

    private boolean SECOND_OBJ_OCTAVE = false;
    private boolean SECOND_OBJ_NOTE = true;
    private boolean SECOND_OBJ_VOLUME = false;

    private boolean SELECT_FIRST_OBJ = true;
    private boolean SELECT_SECOND_OBJ = false;


    // used to prevent constant reading of octave up on reading
    private boolean OCTAVE_UP_LATCHER = false;
    private boolean OCTAVE_DWN_LATCHER = false;

    private boolean SETUP_MASTER_AREA = true;


    private boolean SECOND_OBJ_DETECT_ONE_CONTOUR = true;



    private int width = 864;
    private int height = 486;


    SharedPreferences sharedPref;
    SharedPreferences sharedPref2;

    private GsonBuilder builder = new GsonBuilder();
    private Gson gson;

    private ArrayList<SynthData> listOfSynthData = new ArrayList<SynthData>();
    private ArrayList<NotesArrayList> listOfNoteData = new ArrayList<NotesArrayList>();
    private ArrayList<Notes> notesArrayList = new ArrayList<>();

    private Button btn_second_obj_func_btn;
    private Button btn_select_sound_btn;
    private Button btn_calibrate_first_obj_btn;
    private ToggleButton toggle_select_first_obj;
    private ToggleButton toggle_select_second_obj;

    private double first_obj_area = 0;

    private int startPointXObj1 = -1;
    private int currentXObj1 = -1; //used to track current obj 1 's x coordinate

    private Mat mIntermediateMat;

    private Mat mRgba;

    private Handler secondObjHandler;
    private Handler vibratoEnablerHandler;

    private boolean VIBRATO_ENABLE = false;

    ArrayList<String> synthDataTitles = new ArrayList<String>();
    ArrayList<String> noteDataTitles = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cvtest);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);

        mOpenCvCameraView.setMaxFrameSize(width,height);



/*
        Camera camera = Camera.open(1);
        android.hardware.Camera.Parameters params = camera.getParameters();


        List<android.hardware.Camera.Size> sizeList = params.getSupportedVideoSizes();


        for(int i = 0 ; i <sizeList.size(); i ++ ){

            android.hardware.Camera.Size s = sizeList.get(i);
            Log.i("FYP" + String.valueOf(i) , String.valueOf(s.height));
            Log.i("FYP" + String.valueOf(i) , String.valueOf(s.width));


        }
*/



        mGestureDetector = new GestureDetector(this , new MyGestureListener());


        sharedPref=  this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        sharedPref2=  this.getSharedPreferences(
                getString(R.string.notes_array_list), Context.MODE_PRIVATE);

        gson = builder.create();

        String gsonString = sharedPref.getString(getString(R.string.preference_file_key) , "");
        String gsonString2 = sharedPref2.getString(getString(R.string.notes_array_list) , "");


        if(gsonString.length()>0){
            listOfSynthData = gson.fromJson(gsonString,new TypeToken<ArrayList<SynthData>>(){}.getType());
        }


        if(gsonString2.length()>0){
            listOfNoteData = gson.fromJson(gsonString2,new TypeToken<ArrayList<NotesArrayList>>(){}.getType());
        }






        btn_second_obj_func_btn = (Button) findViewById(R.id.second_obj_func_btn);
        btn_select_sound_btn = (Button) findViewById(R.id.select_sound_btn);
        btn_calibrate_first_obj_btn = (Button) findViewById(R.id.calibrate_first_obj);
        toggle_select_first_obj = (ToggleButton)findViewById(R.id.select_1st_obj);
        toggle_select_second_obj = (ToggleButton)findViewById(R.id.select_2nd_obj);

        toggle_select_first_obj.setChecked(true);

        toggle_select_first_obj.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    SELECT_FIRST_OBJ = true;
                    SELECT_SECOND_OBJ = false;
                    toggle_select_second_obj.setChecked(false);

                }
                else{
                    SELECT_FIRST_OBJ = false;
                    toggle_select_first_obj.setChecked(false);


                }


            }
        });

        toggle_select_second_obj.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    SELECT_FIRST_OBJ = false;
                    SELECT_SECOND_OBJ = true;
                    toggle_select_first_obj.setChecked(false);

                }
                else{
                    SELECT_SECOND_OBJ = false;
                    toggle_select_second_obj.setChecked(false);


                }
            }
        });





        btn_second_obj_func_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectSecondObject();


            }
        });



        btn_select_sound_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectSound();

            }
        });


        btn_calibrate_first_obj_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSynth.setMasterArea(first_obj_area);

            }
        });





        secondObjHandler = new Handler();
        vibratoEnablerHandler = new Handler();


        for(int i = 0 ; i < listOfSynthData.size(); i++){

            synthDataTitles.add(listOfSynthData.get(i).title);


        }



        for(int i = 0 ; i < listOfNoteData.size(); i++){

            noteDataTitles.add(listOfNoteData.get(i).name);


        }



        selectSound();
        selectNotes();







//        Button btn = (Button)findViewById(R.id.button_mute_audio_open_CV);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSynth.mute();
//            }
//        });
        mSynth= new Synth();
        mSynth.disableFilterEnv();
        mSynth.selectHighPass();
        mSynth.setfreqQ(6);

        mSynth2= new Synth();
        mSynth2.disableFilterEnv();
        mSynth2.selectHighPass();
        mSynth2.setfreqQ(6);



        // mSynth.playOsc();

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener() {
            @Override
            public void onCameraViewStarted(int width, int height) {


                mDetector = new ColorBlobDetector();
                mDetector2 = new ColorBlobDetector();

                mSpectrum = new Mat();
                mBlobColorRgba = new Scalar(255);
                mBlobColorHsv = new Scalar(255);
                SPECTRUM_SIZE = new Size(200, 64);
                CONTOUR_COLOR = new Scalar(255,0,0,255);
                CONTOUR_COLOR2 = new Scalar(200,200,200,200);


            }

            @Override
            public void onCameraViewStopped() {

            }



            @Override
            public Mat onCameraFrame(Mat inputFrame) {

               // mIntermediateMat = new Mat();

                  // used to keep track of object 1's x value

                mRgba = inputFrame;


                Core.flip(mRgba,mRgba,1);

                List<MatOfPoint> contours = mDetector.getContours(); // anaylse contours
                List<MatOfPoint> contours2 = mDetector2.getContours(); // anaylse contours

                Size sizeRgba = mRgba.size();


                height = (int)sizeRgba.height;
                width = (int)sizeRgba.width;


                Imgproc.rectangle(mRgba ,new Point(0,0) , new Point(150, height * 0.3) , new Scalar(0,255,0));
                Imgproc.rectangle(mRgba ,new Point(0,height * 0.7) , new Point(150, height ) , new Scalar(0,255,0));



                if (mIsColorSelected) {


                    mDetector.process(mRgba);
                    mDetector2.process(mRgba);

                    // Log.e("FYP", "Contours count: " + contours.size());


                    if(contours.size() == 0 && SYNTH_PLAYING){
//                        Log.i("FYP" , "RELEASEING OSC ");
                        mSynth.releaseOsc();
                        SYNTH_PLAYING = false;
                    }



                    if(contours2.size() == 0 && SYNTH_PLAYING2){
//                        Log.i("FYP" , "RELEASEING OSC ");
                        mSynth2.releaseOsc();
                        SYNTH_PLAYING2 = false;
                    }




                    Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);
                    Imgproc.drawContours(mRgba, contours2, -1, CONTOUR_COLOR2);





                       /* Mat colorLabel = mRgba.submat(4, 68, 4, 68); // this is just the color label at top left
                        colorLabel.setTo(mBlobColorRgba);

                        Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols()); // this is just color label at top left too
                        mSpectrum.copyTo(spectrumLabel);*/







                }


                //FIRST OBJECT
                //FIRST OBJECT
                //FIRST OBJECT
                //FIRST OBJECT


                List<Moments> mu = new ArrayList<Moments>(contours.size());

                for (int i = 0; i < contours.size(); i++) {
                    mu.add(i, Imgproc.moments(contours.get(i), false));
                    Moments p = mu.get(i);
                    int x = (int) (p.get_m10() / p.get_m00());
                    int y = (int) (p.get_m01() / p.get_m00());
                    currentXObj1 = x;

                    first_obj_area = p.get_m00();

                    if(!SYNTH_PLAYING) {
                        mSynth.playOsc();
                        SYNTH_PLAYING = true;
                    }

                    if(SETUP_MASTER_AREA){

                        mSynth.setMasterArea(first_obj_area);
                        SETUP_MASTER_AREA = false;


                    }


//                    Log.i("FYP" , "For contour number " + i + " area is  " + area);


                    Log.i("FYP" , "For contour number " + i + " x is at " + x);
                    Log.i("FYP" , "For contour number " + i + " y is at " + y);

                    boolean noteChanged = Scales.chord( 0 ,x , width , notesArrayList, mSynth);
                    if(noteChanged) {

                        VIBRATO_ENABLE = false;
                        startVibratoHandler();



                    }

                    if(VIBRATO_ENABLE){

                        Log.i("FYP" , "For vibrato enable, difference is  " + (x - startPointXObj1));

                        Scales.chord((x - startPointXObj1)/3 , x , width , notesArrayList, mSynth);
                    }




                    mSynth.setFilterAmp2(first_obj_area);

                    if(!mSynth.isFilterEnvEnabled())
                        mSynth.setFilterValue(y*10);

                    Imgproc.circle(mRgba, new Point(x, y), 4, new Scalar(255,49,0,255));


                }



                //SECOND OBJECT
                //SECOND OBJECT
                //SECOND OBJECT
                //SECOND OBJECT



                mu = new ArrayList<Moments>(contours2.size());

                    //detected nothing from 2nd object
                    if(contours2.size() == 0){

                        startObjectLatchResetHandler();

                    }

                    for (int i = 0; i < contours2.size(); i++) {

                        resetObjectLatchResetHandler();

                        mu.add(i, Imgproc.moments(contours2.get(i), false));
                        Moments p = mu.get(i);
                        int x = (int) (p.get_m10() / p.get_m00());
                        int y = (int) (p.get_m01() / p.get_m00());

                        double area = p.get_m00();
//                    Log.i("FYP" , "For contour2 number " + i + " area is  " + area);


                         Log.i("FYP" , "For 2nd obj countour number " + i + " x is at " + x);
                         Log.i("FYP" , "For 2nd obj countour number " + i + " y is at " + y);

                        if(SECOND_OBJ_OCTAVE){


                            double upperBound =  height * 0.7;
                            double lowerBound =  height * 0.4;


                            if(y < lowerBound && x < 150 && !OCTAVE_UP_LATCHER ){
                                shiftNotes(12 , 1);
                                OCTAVE_UP_LATCHER = true;
                                OCTAVE_DWN_LATCHER = false;


                                Log.i("FYP" , "SHIFT OCT UP " + i + " y is at " + y);

                            }
                            else if(y > upperBound && x < 150 && !OCTAVE_DWN_LATCHER){
                                shiftNotes(12 , 0);
                                OCTAVE_DWN_LATCHER = true;
                                OCTAVE_UP_LATCHER = false;


                                Log.i("FYP" , "SHIFT OCT DWN " + i + " y is at " + y);


                            }

                            else if (y > lowerBound && y < upperBound){

                                resetObjectLatchResetHandler();



                            }





                        }



                        if(SECOND_OBJ_NOTE) {

                            if(!SYNTH_PLAYING2) {
                                mSynth2.playOsc();
                                SYNTH_PLAYING2 = true;
                            }

                            Scales.chord( 0 , x, width, notesArrayList, mSynth2);
                            mSynth2.setFilterAmp2(area);

                            if (!mSynth2.isFilterEnvEnabled())
                                mSynth2.setFilterValue(y * 10);
                        }

                        Imgproc.circle(mRgba, new Point(x, y), 4, new Scalar(122, 122, 122, 255));

                        if(SECOND_OBJ_DETECT_ONE_CONTOUR){
                            break;
                        }


                    }








                return mRgba;

            }







        }




        );


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


    private void resetObjectLatchResetHandler(){

        secondObjHandler.removeCallbacksAndMessages(null);


    }



    private void startObjectLatchResetHandler(){

        secondObjHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetSecondObjLatch();
            }
        },300);


    }

    private void startVibratoHandler( ){

        vibratoEnablerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                VIBRATO_ENABLE = true;
                startPointXObj1 = currentXObj1;
            }
        },700);



    }


    private void resetSecondObjLatch(){

        OCTAVE_UP_LATCHER = false;
        OCTAVE_DWN_LATCHER = false;
    }

    private void selectSecondObject(){


        ArrayList<String> secondObj = new ArrayList<String>();

        secondObj.add("Octave");
        secondObj.add("Note");
        secondObj.add("Volume");



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list_for_xy, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("List");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,secondObj);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("FYP" , "CLICKED CLICKED CLICKED");
                if(position == 0){


                    SECOND_OBJ_OCTAVE = true;
                    SECOND_OBJ_NOTE = false;
                    SECOND_OBJ_VOLUME = false;


                }

                if(position == 1){


                    SECOND_OBJ_NOTE = true;
                    SECOND_OBJ_OCTAVE = false;
                    SECOND_OBJ_VOLUME = false;


                }

                if(position == 2){



                    SECOND_OBJ_NOTE = false;
                    SECOND_OBJ_OCTAVE = false;
                    SECOND_OBJ_VOLUME = true;

                }


            }
        });
        alertDialog.show();



    }


    private void selectSound(){

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
                mSynth.destory();
                mSynth = new Synth(listOfSynthData.get(position));

               // mSynth.playOsc();
                mSynth2.destory();
                mSynth2 = new Synth(listOfSynthData.get(position));
               // mSynth2.playOsc();


            }
        });
        alertDialog.show();
    }


    private void selectNotes(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
        LayoutInflater inflater2 = getLayoutInflater();
        View convertView2 = (View) inflater2.inflate(R.layout.list_for_xy, null);
        alertDialog2.setView(convertView2);
        alertDialog2.setTitle("Select Scale");
        ListView lv2 = (ListView) convertView2.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,noteDataTitles);
        lv2.setAdapter(adapter2);
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("FYP" , "CLICKED CLICKED CLICKED");
                notesArrayList = listOfNoteData.get(position).notesArrayList;
                setupNoteLayout();
            }
        });

        alertDialog2.show();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return mGestureDetector.onTouchEvent(event);





    }

    private void shiftNotes(int semitones , int mode){




            for(int i = 0 ; i < notesArrayList.size(); i++){


                    notesArrayList.get(i).shiftSemitone(semitones,mode);


            }












    }



    private void setupNoteLayout(){


        int size = notesArrayList.size();

        LinearLayout ll = (LinearLayout) findViewById(R.id.opencv_linear_layout_notes);

        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT);
        linearParams.weight = 1;


        for(int i = 0 ; i < size ; i++){

            TextView tv = new TextView(this);

            tv.setText(String.valueOf(i));
            tv.setLayoutParams(linearParams);


            ll.addView(tv);

        }





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
        mSynth2.destory();


    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
    }



    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            {



                int cols = mRgba.cols();
                int rows = mRgba.rows();

                double xOffset = (mOpenCvCameraView.getWidth() - cols) / 2.35;
                double yOffset = (mOpenCvCameraView.getHeight() - rows) / 2.35;



                int x /*= (int) e.getX() -  (int) xOffset*/;
                int y /* = (int) e.getY() - (int) yOffset*/;


                double xDiv = (double) mOpenCvCameraView.getWidth() / (double) cols;
                double yDiv =(double) mOpenCvCameraView.getHeight() /(double) rows;

                Log.i("FYP", "Touch imagediv coordinates: (" + mOpenCvCameraView.getWidth()  + ", " + cols + ")");



                Log.i("FYP", "Touch imagediv coordinates: (" + xDiv + ", " + yDiv + ")");


                double xx = (double) e.getX() /  xDiv;
                double yy = (double) e.getY() /  yDiv;

                x = (int) xx;
                y = (int) yy;





                Log.i("FYP", "Touch image coordinates: (" + x + ", " + y + ")");

                if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;


                Rect touchedRect = new Rect();

                touchedRect.x = (x>8) ? x-8 : 0;
                touchedRect.y = (y>8) ? y-8 : 0;





                touchedRect.width = (x+8 < cols) ? x + 8 - touchedRect.x : cols - touchedRect.x;
                touchedRect.height = (y+8 < rows) ? y + 8 - touchedRect.y : rows - touchedRect.y;

                touchedRect.width = touchedRect.width - 8;
                touchedRect.height = touchedRect.height - 8;

                Log.i("FYP", "Touch touchedRect coordinates: (" + touchedRect.width + ", " + touchedRect.height + ")");



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


                //touch the square to raise/lower octave

                if(!SELECT_SECOND_OBJ && !SELECT_FIRST_OBJ){


                    double upperBound =  height * 0.7;
                    double lowerBound =  height * 0.4;


                    if(y < lowerBound && x < 150 && !OCTAVE_UP_LATCHER ){
                        shiftNotes(12 , 1);
                        OCTAVE_UP_LATCHER = true;
                        OCTAVE_DWN_LATCHER = false;



                    }
                    else if(y > upperBound && x < 150 && !OCTAVE_DWN_LATCHER){
                        shiftNotes(12 , 0);
                        OCTAVE_DWN_LATCHER = true;
                        OCTAVE_UP_LATCHER = false;




                    }






                }










                if(SELECT_FIRST_OBJ) {

                    mDetector.setHsvColor(mBlobColorHsv);
                    mTouchChoice = 1;
                    Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);
                    mSynth.resetFilterAmp();

                }

                else if (SELECT_SECOND_OBJ){

                    mDetector2.setHsvColor(mBlobColorHsv);
                    mTouchChoice = 0;
                    Imgproc.resize(mDetector2.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);
                    mSynth2.resetFilterAmp();

                }

//                Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);
//                Imgproc.resize(mDetector2.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);


                mIsColorSelected = true;

                touchedRegionRgba.release();
                touchedRegionHsv.release();












                return false;






            }
        }
    }





}
