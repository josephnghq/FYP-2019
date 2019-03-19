package com.example.joseph.fyp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HandGestureActivity extends AppCompatActivity {

    CustomJavaCameraView mOpenCvCameraView;
    SharedPreferences sharedPref;
    SharedPreferences sharedPref2;
    SharedPreferences defaultPrefs;

    ArrayList<String> synthDataTitles = new ArrayList<String>();
    ArrayList<String> noteDataTitles = new ArrayList<String>();
    private BaseLoaderCallback mLoaderCallback;
    private boolean mIsColorSelected = false;
    private Scalar mBlobColorRgba;
    private Scalar mBlobColorHsv;
    private ColorBlobDetector mDetector;
    private ColorBlobDetector mDetector2;
    private Mat mSpectrum;
    private Size SPECTRUM_SIZE;
    private Scalar CONTOUR_COLOR;
    private Scalar CONTOUR_COLOR2;
    private GestureDetector mGestureDetector;
    private int mTouchChoice = 0;
    private Synth mSynth;
    private Synth mSynth2;

    private boolean SYNTH_PLAYING = true;
    private boolean SYNTH_PLAYING2 = true;
    private boolean SECOND_OBJ_OCTAVE = false;
    private boolean SECOND_OBJ_NOTE = true;
    private boolean SECOND_OBJ_VOLUME = false;
    private boolean SELECT_THUMB = true;
    private boolean SELECT_FINGERS = false;
    // used to prevent constant reading of octave up on reading
    private boolean OCTAVE_UP_LATCHER = false;
    private boolean OCTAVE_DWN_LATCHER = false;
    private boolean SETUP_MASTER_AREA = true;
    private boolean SECOND_OBJ_DETECT_ONE_CONTOUR = false;
    private boolean GLISSANDO_MODE = true;
    private boolean VIRTUAL_CURSOR_LOCK = false;
    private boolean PERFORMANCE_MODE = false;

    private int width = 864;
    private int height = 486;
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson;
    private ArrayList<SynthData> listOfSynthData = new ArrayList<SynthData>();
    private ArrayList<NotesArrayList> listOfNoteData = new ArrayList<NotesArrayList>();
    private ArrayList<Notes> notesArrayList = new ArrayList<>();
    private Button btn_set_exposure;
    private Button btn_second_obj_func_btn;
    private Button btn_select_sound_btn;
    private Button btn_calibrate_first_obj_btn;
    private Button btn_hand_gesture_setRadius;
    private Button btn_hand_gesture_setThumbRadius;
    private Button btn_perf_mode;

    private ToggleButton toggle_select_thumbs;
    private ToggleButton toggle_select_fingers;
    private double first_obj_area = 0;
    private int startPointXObj1 = -1;
    private int currentXObj1 = -1; //used to track current obj 1 's x coordinate

    //Use this for finger pointing gesture
    private int startPointYObj1 = -1;
    private int currentYObj1 = -1; //used to track current obj 1 's y coordinate

    private boolean SHIFTED_UP  = false;
    private boolean SHIFTED_DOWN  = false;

    private int SHIFT_MODE = -1;


    private int detectSize = 8;

    private Mat mIntermediateMat;
    private Mat mRgba;
    private Handler secondObjHandler;
    private Handler vibratoEnablerHandler;


    private boolean fingerTimerLock = false;
    private boolean thumbTimerLock = false;
    private Timer thumbTimerTask;
    private Timer fingerTimerTask;
    private Handler virtualCursorTimer;
    private int thumbx;
    private int thumby;
    private int fingerx;
    private int fingery;
    private int rightX;
    private int rightY;

    private Handler handler;


    private boolean ENABLE_DELETE_CLOSE_OBJECTS = false;
    private boolean ENABLE_DELETE_SMALL_OBJECTS = true;
    private boolean SAME_THUMB_COLOR_MODE = true;

    private BackgroundSubtractorMOG2 backgroundSubtractorMOG2;



    private ArrayList<FingerMomentsXYData> thumbLeftSideVelocity = new ArrayList<FingerMomentsXYData>();
    private ArrayList<FingerMomentsXYData> thumbRightSideVelocity = new ArrayList<FingerMomentsXYData>();

    private List<RoomSynthData> ListOfRoomSynthData;

    private ArrayList<Integer> leftHandFingerInBetweenDistance = new ArrayList<Integer>();

    private int fingerCount = 0;
    private Timer fingerCounter;
    private boolean VIBRATO_ENABLE = false;
    private boolean fingerCounterLock = false;

    private double leftThumbSize = 0;
    private double rightThumbSize = 0;

    private Scalar fingerScalarRadius;
    private Scalar thumbScalarRadius;


    private double onCameraFrameStartTime;

    private double onCameraFrameEndTime;

    private boolean HSVMode = false;

    private Camera mCamera;

    private int boxFirstLeftX = width-150;
    private int boxFirstRightX = width;

    private int boxSecondLeftX = width - 310;
    private int boxSecondRightX = width - 160;

    private double boxHeight = (height*0.2);












    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_gesture);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mOpenCvCameraView = (CustomJavaCameraView) findViewById(R.id.HelloOpenCvView);

        mOpenCvCameraView.setMaxFrameSize(width, height);


        mCamera = Camera.open(1);

        Camera.Parameters params = mCamera.getParameters();

        Log.i("FYP" , "Exposure is " + params.getAutoExposureLock());
        Log.i("FYP" , "Exposure value is " + params.getExposureCompensation());
        Log.i("FYP" , "Exposure min value is " + params.getMaxExposureCompensation());
        Log.i("FYP" , "Exposure max value is " + params.getMinExposureCompensation());





        Camera camera = Camera.open(1);

        List<android.hardware.Camera.Size> sizeList = params.getSupportedVideoSizes();


        for(int i = 0 ; i <sizeList.size(); i ++ ){

            android.hardware.Camera.Size s = sizeList.get(i);
            Log.i("FYP" + String.valueOf(i) , String.valueOf(s.height));
            Log.i("FYP" + String.valueOf(i) , String.valueOf(s.width));


        }




        mGestureDetector = new GestureDetector(this, new MyGestureListener());



        sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        sharedPref2 = this.getSharedPreferences(
                getString(R.string.notes_array_list), Context.MODE_PRIVATE);

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);


        HSVMode = defaultPrefs.getBoolean("hand_gesture_hsv_view" , false);
        SAME_THUMB_COLOR_MODE = defaultPrefs.getBoolean("hand_gesture_same_color_thumb_mode" , false);



        gson = builder.create();

        String gsonString = sharedPref.getString(getString(R.string.preference_file_key), "");
        String gsonString2 = sharedPref2.getString(getString(R.string.notes_array_list), "");


        if (gsonString.length() > 0) {
            listOfSynthData = gson.fromJson(gsonString, new TypeToken<ArrayList<SynthData>>() {
            }.getType());
        }


        if (gsonString2.length() > 0) {
            listOfNoteData = gson.fromJson(gsonString2, new TypeToken<ArrayList<NotesArrayList>>() {
            }.getType());
        }


        handler = new Handler();
        thumbTimerTask = new Timer();
        fingerTimerTask = new Timer();
        virtualCursorTimer = new Handler();


        new Thread(new Runnable() {
            @Override
            public void run() {

                ListOfRoomSynthData = RoomSingleton.db.roomDAO().getAll();
                // ListOfRoomNotesArrayLists = RoomSingleton.db.roomDAO().getAllNotes();



                synthDataTitles = new ArrayList<String>();
                noteDataTitles = new ArrayList<String>();

                String gsonString2 = sharedPref2.getString(getString(R.string.notes_array_list) , "");
                listOfNoteData = gson.fromJson(gsonString2,new TypeToken<ArrayList<NotesArrayList>>(){}.getType());


                Log.i("FYP" , "Size of db roomsynthdata is " + ListOfRoomSynthData.size());

                for(int i = 0 ; i < ListOfRoomSynthData.size(); i++){

                    // synthDataTitles.add(listOfSynthData.get(i).title);

                    synthDataTitles.add(ListOfRoomSynthData.get(i).title);

                    Log.i("FYP" , ListOfRoomSynthData.get(i).title);


                }


                for(int i = 0 ; i < listOfNoteData.size(); i++){

                    noteDataTitles.add(listOfNoteData.get(i).name);


                }



                Log.i("FYP" , "size of listofnotedata is " + listOfNoteData.size());


                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(HandGestureActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.list_for_xy, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Select Sound");
                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(HandGestureActivity.this,android.R.layout.simple_list_item_1,synthDataTitles);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Log.i("FYP" , "CLICKED CLICKED CLICKED");
                        mSynth.destory();
                        mSynth = new Synth(ListOfRoomSynthData.get(position));

                        mSynth2.destory();
                        mSynth2 = new Synth(ListOfRoomSynthData.get(position));


                    }
                });




                final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(HandGestureActivity.this);
                LayoutInflater inflater2 = getLayoutInflater();
                View convertView2 = (View) inflater2.inflate(R.layout.list_for_xy, null);
                alertDialog2.setView(convertView2);
                alertDialog2.setTitle("Select Scale");
                ListView lv2 = (ListView) convertView2.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(HandGestureActivity.this,android.R.layout.simple_list_item_1,noteDataTitles);
                lv2.setAdapter(adapter2);
                lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Log.i("FYP" , "CLICKED CLICKED CLICKED");
                        notesArrayList = listOfNoteData.get(position).notesArrayList;
                      //  setupNoteLayout();

                    }
                });



                handler.post(new Runnable() {
                    @Override
                    public void run() {


                        alertDialog.show();
                        alertDialog2.show();
                    }
                });


            }
        }).start();


        btn_set_exposure = (Button) findViewById(R.id.hand_gesture_exposure);
        btn_hand_gesture_setRadius = (Button) findViewById(R.id.hand_gesture_setRadius);
        btn_hand_gesture_setThumbRadius = (Button) findViewById(R.id.hand_gesture_setThumbRadius);

        btn_second_obj_func_btn = (Button) findViewById(R.id.hand_gesture_second_obj_func_btn);
        btn_select_sound_btn = (Button) findViewById(R.id.hand_gesture_select_sound_btn);
        btn_calibrate_first_obj_btn = (Button) findViewById(R.id.hand_gesture_calibrate_first_obj);
        toggle_select_thumbs = (ToggleButton) findViewById(R.id.hand_gesture_select_thumbs);
        toggle_select_fingers = (ToggleButton) findViewById(R.id.hand_gesture_select_fingers);
        btn_perf_mode = (Button) findViewById(R.id.hand_gesture_perf_btn);

        toggle_select_thumbs.setChecked(true);

        toggle_select_thumbs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SELECT_THUMB = true;
                    SELECT_FINGERS = false;
                    toggle_select_fingers.setChecked(false);

                } else {
                    SELECT_THUMB = false;
                    toggle_select_thumbs.setChecked(false);


                }


            }
        });

        btn_set_exposure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExposure();
            }
        });


        btn_hand_gesture_setRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFingersRange();
            }
        });

        btn_hand_gesture_setThumbRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectThumbRange();
            }
        });

        toggle_select_fingers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SELECT_THUMB = false;
                    SELECT_FINGERS = true;
                    toggle_select_thumbs.setChecked(false);

                } else {
                    SELECT_FINGERS = false;
                    toggle_select_fingers.setChecked(false);


                }
            }
        });


        btn_second_obj_func_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // selectSecondObject();

               /* if(!HSVMode)
                HSVMode = true;
                else{
                    HSVMode = false;
                }*/

               fingerScalarRadius = null;
               thumbScalarRadius = null;
                Intent i = new Intent(getApplicationContext(),HandGestureSettings.class);
                startActivity(i);






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
                //mSynth.setMasterArea(first_obj_area);
                if(GLISSANDO_MODE)
                    GLISSANDO_MODE = false;
                else{
                    GLISSANDO_MODE = true;
                }

            }
        });

        btn_perf_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(PERFORMANCE_MODE) {
                    PERFORMANCE_MODE = false;
                    showButtonGroup();
                }

                else {
                    PERFORMANCE_MODE = true;
                    hideButtonGroup();
                }




            }
        });


        secondObjHandler = new Handler();
        vibratoEnablerHandler = new Handler();
        fingerCounter = new Timer();

        for (int i = 0; i < listOfSynthData.size(); i++) {

            synthDataTitles.add(listOfSynthData.get(i).title);


        }


        for (int i = 0; i < listOfNoteData.size(); i++) {

            noteDataTitles.add(listOfNoteData.get(i).name);


        }


        /*selectSound();
        selectNotes();*/


//        Button btn = (Button)findViewById(R.id.button_mute_audio_open_CV);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSynth.mute();
//            }
//        });
        mSynth = new Synth();
        mSynth.disableFilterEnv();
        mSynth.selectHighPass();
        mSynth.setfreqQ(6);

        mSynth2 = new Synth();
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
                                                          CONTOUR_COLOR = new Scalar(255, 0, 0, 255);
                                                          CONTOUR_COLOR2 = new Scalar(200, 200, 200, 200);
                                                          backgroundSubtractorMOG2 = Video.createBackgroundSubtractorMOG2();
                                                          mOpenCvCameraView.setExposureCompensation(Integer.parseInt(defaultPrefs.getString("hand_gesture_exposure" , "0")));


                                                      }

                                                      @Override
                                                      public void onCameraViewStopped() {

                                                      }


                                                      @Override
                                                      public Mat onCameraFrame(Mat inputFrame) {




                                                          onCameraFrameStartTime = System.currentTimeMillis();

                                                          mRgba = inputFrame;
                                                          Core.flip(mRgba, mRgba, 1);




                                                              // mIntermediateMat = new Mat();

                                                              // used to keep track of object 1's x value

                                                             // mRgba = inputFrame;
                                                              // backgroundSubtractorMOG2.apply(inputFrame , mRgba);


                                                              //Imgproc.blur(mRgba,mRgba,new Size(9.0, 9.0));

                                                             // Core.flip(mRgba, mRgba, 1);

                                                              List<MatOfPoint> contours = mDetector.getContours(); // anaylse contours
                                                              List<MatOfPoint> contours2 = mDetector2.getContours(); // anaylse contours


                                                              // for all the contours, delete away contours that are near to each other, to avoid situations whereby two points are detected
                                                              // for the same object


                                                              Size sizeRgba = mRgba.size();


                                                              height = (int) sizeRgba.height;
                                                              width = (int) sizeRgba.width;
                                                              int firstHalf = width / 2;


                                                              if (mIsColorSelected) {

                                                                  if(!SAME_THUMB_COLOR_MODE)
                                                                  mDetector.process(mRgba);


                                                                  mDetector2.process(mRgba);



                                                              /*if (contours.size() == 0 && SYNTH_PLAYING) {


                                                                  mSynth.releaseOsc();
                                                                  SYNTH_PLAYING = false;
                                                              }*/


                                                                  if (contours2.size() == 0 && SYNTH_PLAYING2) {

                                                                      mSynth2.releaseOsc();
                                                                      SYNTH_PLAYING2 = false;
                                                                  }

                                                                  if(!SAME_THUMB_COLOR_MODE)
                                                                  Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);


                                                                  Imgproc.drawContours(mRgba, contours2, -1, CONTOUR_COLOR2);


                                                              }





                                                              //FIRST OBJECT
                                                              //FIRST OBJECT
                                                              //FIRST OBJECT
                                                              //FIRST OBJECT

                                                              //THIS IS THUMB


                                                              ArrayList<FingerMomentsXYData> thumbLeftSide = new ArrayList<FingerMomentsXYData>();
                                                              ArrayList<FingerMomentsXYData> thumbRightSide = new ArrayList<FingerMomentsXYData>();


                                                              List<Moments> mu = new ArrayList<Moments>(contours.size());



                                                          if(!SAME_THUMB_COLOR_MODE) {
                                                              for (int i = 0; i < contours.size(); i++) {
                                                                  mu.add(i, Imgproc.moments(contours.get(i), false));
                                                                 /* Moments p = mu.get(i);




                                                                  final int x = (int) (p.get_m10() / p.get_m00());
                                                                  final int y = (int) (p.get_m01() / p.get_m00());



                                                                  double area = Imgproc.contourArea(contours.get(i));*/

                                                              }

                                                              if (contours.size() == 0) {
                                                                  startObjectLatchResetHandler();
                                                              }

                                                              if (ENABLE_DELETE_SMALL_OBJECTS)
                                                                  for (int i = 0; i < mu.size(); i++) {

                                                                      Moments mom = mu.get(i);
                                                                      if (mom.get_m00() < 500) {
                                                                          mu.remove(i);
                                                                      }


                                                                  }


                                                              //remove close points
                                                              if (ENABLE_DELETE_CLOSE_OBJECTS)
                                                                  for (int i = 0; i < mu.size(); i++) {

                                                                      for (int p = i; p < mu.size(); p++) {

                                                                          Moments mom = mu.get(i);
                                                                          Moments momToCompare = mu.get(p);


                                                                          double dist = distanceOfXYPoints(mom.get_m10() / mom.get_m00(), momToCompare.get_m10() / momToCompare.get_m00(),
                                                                                  mom.get_m01() / momToCompare.get_m00(), momToCompare.get_m01() / momToCompare.get_m00());


                                                                          if (dist < 20 && p != i) {

                                                                              Logger.Log("Removing close points");
                                                                              mu.remove(p);
                                                                              break;

                                                                          }


                                                                      }


                                                                  }


                                                              //  Log.i("FYP","number of thumbs " + mu.size());

                                                              if (mu.size() == 0) {

                                                                  thumbLeftSideVelocity.clear();
                                                                  thumbRightSideVelocity.clear();
                                                                  thumbTimerTask.cancel();
                                                                  thumbTimerLock = false;
                                                                  thumbTimerTask = new Timer();
                                                              }


                                                              for (int i = 0; i < mu.size()/*i < contours.size()*/; i++) {

                                                             /* mu.add(i, Imgproc.moments(contours.get(i), false));*/
                                                                  Moments p = mu.get(i);
                                                                  int x = (int) (p.get_m10() / p.get_m00());
                                                                  int y = (int) (p.get_m01() / p.get_m00());
                                                                  currentXObj1 = x;

                                                                  thumbx = x;
                                                                  thumby = y;


                                                                  if (!thumbTimerLock) {

                                                                      thumbTimerTask.schedule(new TimerTask() {
                                                                          @Override
                                                                          public void run() {

                                                                              //selectColorOnPoint(0);
                                                                              Logger.Log("SELECTING POINT " + thumbx + " " + thumby);


                                                                          }
                                                                      }, 3000, 3000);

                                                                      thumbTimerLock = true;

                                                                  }


                                                                  if (x < firstHalf) {
                                                                      //  Log.i("FYP" , "Moments number " + i + " (thumb) is in first half");

                                                                      thumbLeftSide.add(new FingerMomentsXYData(i, x, y, true));

                                                                      if (thumbLeftSideVelocity.size() == 30) {

                                                                          thumbLeftSideVelocity.remove(thumbLeftSideVelocity.size() - 1);
                                                                          thumbLeftSideVelocity.add(0, new FingerMomentsXYData(i, x, y, true, System.currentTimeMillis()));

                                                                      } else {
                                                                          thumbLeftSideVelocity.add(new FingerMomentsXYData(i, x, y, true, System.currentTimeMillis()));
                                                                      }


                                                                  } else if (x > firstHalf) {

                                                                      //  Log.i("FYP" , "Moments number " + i + " (thumb) is in second half");

                                                                      thumbRightSide.add(new FingerMomentsXYData(i, x, y, false));

                                                                      if (thumbRightSideVelocity.size() == 30) {

                                                                          thumbRightSideVelocity.remove(thumbRightSideVelocity.size() - 1);
                                                                          thumbRightSideVelocity.add(0, new FingerMomentsXYData(i, x, y, true, System.currentTimeMillis()));

                                                                      } else {
                                                                          thumbRightSideVelocity.add(new FingerMomentsXYData(i, x, y, true, System.currentTimeMillis()));
                                                                      }

                                                                  }


                                                              /*if(thumbLeftSideVelocity.size() == 30) {
                                                                  double leftSideThumbInitialVelocityX = thumbLeftSideVelocity.get(thumbLeftSideVelocity.size() - 1).x;
                                                                  double leftSideThumbInitialVelocityY = thumbLeftSideVelocity.get(thumbLeftSideVelocity.size() - 1).y;
                                                                  double leftSideThumbLastVelocityX = thumbLeftSideVelocity.get(0).x;
                                                                  double leftSideThumbLastVelocityY = thumbLeftSideVelocity.get(0).y;

                                                                  double distLeft = distanceOfXYPoints(leftSideThumbInitialVelocityX, leftSideThumbLastVelocityX, leftSideThumbInitialVelocityY, leftSideThumbLastVelocityY);

                                                                  double leftVelocity = distLeft / (thumbLeftSideVelocity.get(0).timeStamp - thumbLeftSideVelocity.get(thumbLeftSideVelocity.size() - 1).timeStamp);

                                                                  Log.i("FYP", "Thumb left velocity is " + leftVelocity);
                                                              }


                                                              if(thumbRightSideVelocity.size() == 30) {
                                                                  double rightSideThumbInitialVelocityX = thumbRightSideVelocity.get(thumbRightSideVelocity.size() - 1).x;
                                                                  double rightSideThumbInitialVelocityY = thumbRightSideVelocity.get(thumbRightSideVelocity.size() - 1).y;
                                                                  double rightSideThumbLastVelocityX = thumbRightSideVelocity.get(0).x;
                                                                  double rightSideThumbLastVelocityY = thumbRightSideVelocity.get(0).y;

                                                                  double distRight = distanceOfXYPoints(rightSideThumbInitialVelocityX, rightSideThumbLastVelocityX, rightSideThumbInitialVelocityY, rightSideThumbLastVelocityY);

                                                                  double rightVelocity = distRight / (thumbRightSideVelocity.get(0).timeStamp - thumbRightSideVelocity.get(thumbRightSideVelocity.size() - 1).timeStamp);

                                                                  Log.i("FYP", "Thumb right velocity is " + rightVelocity);
                                                              }*/


                                                                  first_obj_area = p.get_m00();

                                                              /*if (!SYNTH_PLAYING) {
                                                                  mSynth.playOsc();
                                                                  SYNTH_PLAYING = true;
                                                              }*/

                                                                  if (SETUP_MASTER_AREA) {

                                                                      mSynth.setMasterArea(first_obj_area);
                                                                      SETUP_MASTER_AREA = false;


                                                                  }


//                    Log.i("FYP" , "For contour number " + i + " area is  " + area);

                                                              /*boolean noteChanged = Scales.chord(0, x, width, notesArrayList, mSynth);
                                                              if (noteChanged) {

                                                                  VIBRATO_ENABLE = false;
                                                                  startVibratoHandler();


                                                              }

                                                              if (VIBRATO_ENABLE) {


                                                                  Scales.chord((x - startPointXObj1) / 3, x, width, notesArrayList, mSynth);
                                                              }
*/

                                                             /* mSynth.setFilterAmp2(first_obj_area);

                                                              if (!mSynth.isFilterEnvEnabled())
                                                                  mSynth.setFilterValue(y * 10);

*/


                                                                  //       Log.i("FYP" , "Value of OCTAVE UP LATCHER " + OCTAVE_UP_LATCHER + " Value of OCTAVE DOWN LATCHER " + OCTAVE_DWN_LATCHER);


                                                                  Imgproc.circle(mRgba, new Point(x, y), 4, new Scalar(255, 49, 0, 255));
                                                                  Imgproc.putText(mRgba, String.valueOf(i), new Point(x, y), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 49, 0, 255), 4);


                                                              }
                                                          }

                                                              //SECOND OBJECT
                                                              //SECOND OBJECT
                                                              //SECOND OBJECT
                                                              //SECOND OBJECT
                                                              //THIS IS FINGERS


                                                              List<Moments> mu2 = new ArrayList<Moments>(contours2.size());
                                                              ArrayList<FingerMomentsXYData> fingerMomentsXYDataArrayListLeftSide = new ArrayList<FingerMomentsXYData>();
                                                              ArrayList<FingerMomentsXYData> fingerMomentsXYDataArrayListRightSide = new ArrayList<FingerMomentsXYData>();


                                                              for (int i = 0; i < contours2.size(); i++) {
                                                                  mu2.add(i, Imgproc.moments(contours2.get(i), false));

                                                              }


                                                              //remove small objects
                                                              if (ENABLE_DELETE_SMALL_OBJECTS)
                                                                  for (int i = 0; i < mu2.size(); i++) {

                                                                      Moments mom = mu2.get(i);
                                                                      if (mom.get_m00() < 500) {
                                                                          mu2.remove(i);
                                                                      }


                                                                  }


                                                              //remove close objects
                                                          if(ENABLE_DELETE_CLOSE_OBJECTS)
                                                          for(int i = 0 ; i < mu2.size(); i ++){

                                                              for(int p = i; p < mu2.size(); p++){

                                                                  Moments mom = mu2.get(i);
                                                                  Moments momToCompare = mu2.get(p);


                                                                  double dist = distanceOfXYPoints(mom.get_m10()/mom.get_m00() , momToCompare.get_m10()/momToCompare.get_m00() ,
                                                                          mom.get_m01()/momToCompare.get_m00() , momToCompare.get_m01()/momToCompare.get_m00());


                                                                  if ( dist < 20 && p != i ){

                                                                      Logger.Log("Removing close points");

                                                                     // mu2.remove(p);
                                                                      break;

                                                                  }

                                                              }


                                                          }


                                                              final int a = mu2.size();


                                                              // this counts finger

                                                         /* if(!fingerCounterLock) {
                                                              Log.i("FYP", "sss");

                                                              fingerCounterLock = true;
                                                              fingerCounter.schedule(new TimerTask() {
                                                                  @Override
                                                                  public void run() {

                                                                      fingerCount = a;
                                                                      Log.i("FYP", "triggering fingerCount");
                                                                      fingerCounterLock = false;

                                                                  }
                                                              }, 300);

                                                          }*/

                                                              //detected nothing from 2nd object
                                                              if (contours2.size() == 0) {

                                                                  startObjectLatchResetHandler();
                                                                  fingerTimerTask.cancel();
                                                                  fingerTimerTask = new Timer();
                                                                  fingerTimerLock = false;


                                                              }

                                                              //Log.i("FYP", "Number of fingers detected = " + fingerCount);


                                                              // processing all the moments
                                                              for (int i = 0; i < mu2.size(); i++) {
                                                                  resetObjectLatchResetHandler();

                                                              /*mu2.add(i, Imgproc.moments(contours2.get(i), false));*/
                                                                  Moments p = mu2.get(i);
                                                                  int x = (int) (p.get_m10() / p.get_m00());
                                                                  int y = (int) (p.get_m01() / p.get_m00());


                                                                  fingerx = x;
                                                                  fingery = y;


                                                                  if(!fingerTimerLock){

                                                                      /*fingerTimerTask.schedule(new TimerTask() {
                                                                          @Override
                                                                          public void run() {

                                                                              //selectColorOnPoint(1);
                                                                              Log.i("FYP" , "SELECTING POINT " + fingerx + " " + fingery);


                                                                          }
                                                                      },3000,3000);*/

                                                                      fingerTimerLock = true;

                                                                  }

                                                                  Log.i("FYP", "mu2 " + i + " " + p.get_m00());


                                                                  //split the data into both sides
                                                                  if (x < firstHalf) {
                                                                      //Log.i("FYP" , "Moments number " + i + " (finger) is in first half");
                                                                      fingerMomentsXYDataArrayListLeftSide.add(new FingerMomentsXYData(i, x, y, true));


                                                                  } else if (x > firstHalf) {

                                                                      //  Log.i("FYP" , "Moments number " + i + " (finger) is in second half");
                                                                      fingerMomentsXYDataArrayListRightSide.add(new FingerMomentsXYData(i, x, y, false));

                                                                  }


                                                                  double area = p.get_m00();


                                                                  //draw circle of center point of the contours, and label them with numbers
                                                                  Imgproc.circle(mRgba, new Point(x, y), 4, new Scalar(122, 122, 122, 255));
                                                                  // Log.i("FYP",String.valueOf(i));
                                                                  Imgproc.putText(mRgba, String.valueOf(i), new Point(x, y), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(122, 122, 122, 255), 4);


                                                                  //note processing

                                                              /*if (SECOND_OBJ_OCTAVE) {


                                                                  if(fingerCount == 2 && !OCTAVE_UP_LATCHER){

                                                                      shiftNotes(12, 1);
                                                                      OCTAVE_UP_LATCHER = true;
                                                                      OCTAVE_DWN_LATCHER = false;


                                                                  }

                                                                  else if (fingerCount == 1 && !OCTAVE_DWN_LATCHER){

                                                                      shiftNotes(12, 0);
                                                                      OCTAVE_DWN_LATCHER = true;
                                                                      OCTAVE_UP_LATCHER = false;

                                                                  }
                                                                  else{

                                                                      resetObjectLatchResetHandler();

                                                                  }


                                                              }*/
                                                              /*
                                                              if (SECOND_OBJ_NOTE) {

                                                                  if (!SYNTH_PLAYING2) {
                                                                      mSynth2.playOsc();
                                                                      SYNTH_PLAYING2 = true;
                                                                  }

                                                                  Scales.chord(0, x, width, notesArrayList, mSynth2);
                                                                  mSynth2.setFilterAmp2(area);

                                                                  if (!mSynth2.isFilterEnvEnabled())
                                                                      mSynth2.setFilterValue(y * 10);
                                                              }


                                                              if (SECOND_OBJ_DETECT_ONE_CONTOUR) {
                                                                  break;
                                                              }*/


                                                              }


                                                              //find the right finger that would be used for 'cursor'


                                                              //we determine cursor by selecting the point that has the largestY ( it is at the bottom most of the screen)

                                                              int largestY = 0;
                                                              //int smallestX = 0;
                                                              FingerMomentsXYData selectedCursor = null;

                                                              //same color thumb mode, this detects thumb
                                                          if(fingerMomentsXYDataArrayListRightSide.size() > 1 && SAME_THUMB_COLOR_MODE){

                                                              int lastItemCount = fingerMomentsXYDataArrayListRightSide.size() - 1;
                                                              int lastItemX = fingerMomentsXYDataArrayListRightSide.get(lastItemCount).x;
                                                              int firstItemX = fingerMomentsXYDataArrayListRightSide.get(0).x;

                                                              if ((lastItemX-firstItemX )> 100 ){
                                                                  thumbRightSide.add(fingerMomentsXYDataArrayListRightSide.get(lastItemCount));
                                                                  fingerMomentsXYDataArrayListRightSide.remove(lastItemCount);
                                                                  Logger.Log("Adding thumb , y position is " + fingerMomentsXYDataArrayListRightSide.get(fingerMomentsXYDataArrayListRightSide.size() - 1).y + " size of " +
                                                                          "thumbRightSize is " + thumbRightSide.size());

                                                              }






                                                          }

                                                              for (int i = 0; i < fingerMomentsXYDataArrayListRightSide.size(); i++) {


                                                                  if (fingerMomentsXYDataArrayListRightSide.get(i).FIRST_HALF == false && fingerMomentsXYDataArrayListRightSide.get(i).y > largestY) {

                                                                      largestY = fingerMomentsXYDataArrayListRightSide.get(i).y;
                                                                      selectedCursor = fingerMomentsXYDataArrayListRightSide.get(i);

                                                                  }

                                                              }


                                                              // processing of cursor to finger
                                                              //observerations so far is, 0 is pinky on left hand, 3 is second finger on left hand

                                                              if (thumbLeftSide.size() == 1 && !OCTAVE_UP_LATCHER && fingerMomentsXYDataArrayListLeftSide.size() > 3 /*&& thumbLeftSide.get(0).x < fingerMomentsXYDataArrayListLeftSide.get(3).x && thumbLeftSide.get(0).y < fingerMomentsXYDataArrayListLeftSide.get(2).y*/ && thumbLeftSide.get(0).y < fingerMomentsXYDataArrayListLeftSide.get(3).y) {

/*

                                                              shiftNotes(12, 1);
                                                              OCTAVE_UP_LATCHER = true;
                                                              OCTAVE_DWN_LATCHER = false;
                                                              Log.i("FYP" , "GOING UP LATCHER");
*/


                                                              }


                                                              // shift notes octave up if thumb is facing up

                                                              else if (thumbRightSide.size() == 1 /*&& !OCTAVE_DWN_LATCHER*/ && fingerMomentsXYDataArrayListRightSide.size() > 0 && thumbRightSide.get(0).y < fingerMomentsXYDataArrayListRightSide.get(fingerMomentsXYDataArrayListRightSide.size() - 1).y) {


                                                                  SHIFT_MODE = 1;


                                                         /*     if(!SHIFTED_UP) {
                                                                  shiftNotes(12, 1);
                                                              }
                                                              OCTAVE_DWN_LATCHER = true;
                                                              OCTAVE_UP_LATCHER = false;
                                                          //    Log.i("FYP" , "GOING Down LATCHER ");
                                                              SHIFTED_UP = true;
*/


                                                              } else if (thumbRightSide.size() == 1 && !OCTAVE_DWN_LATCHER && fingerMomentsXYDataArrayListRightSide.size() > 0 && thumbRightSide.get(0).y > fingerMomentsXYDataArrayListRightSide.get(0).y) {


                                                                 // SHIFT_MODE = 0;


/*

                                                              if(!SHIFTED_DOWN) {
                                                                  shiftNotes(12, 0);
                                                              }
                                                              OCTAVE_DWN_LATCHER = true;
                                                              OCTAVE_UP_LATCHER = false;
                                                              //    Log.i("FYP" , "GOING Down LATCHER ");
                                                              SHIFTED_DOWN = true;
*/


                                                              } else {

                                                              /*if(SHIFTED_UP){
                                                                  shiftNotes(12,0);
                                                                  SHIFTED_UP = false;
                                                              }*/

                                                                  SHIFT_MODE = -1;


                                                         /*     if(SHIFTED_DOWN){
                                                                  shiftNotes(12,1);
                                                                  SHIFTED_DOWN = false;
                                                              }*/

                                                                  startObjectLatchResetHandler();

                                                              }


                                                              leftHandFingerInBetweenDistance.clear();


                                                              // this one calculates the DISTANCE between each INDIVIUAL fingers
                                                              /*for(int i = 0 ; i < fingerMomentsXYDataArrayListLeftSide.size();i++){

                                                                  if(i+1 != fingerMomentsXYDataArrayListLeftSide.size()){

                                                                      int x1 = fingerMomentsXYDataArrayListLeftSide.get(i).x;
                                                                      int x2 = fingerMomentsXYDataArrayListLeftSide.get(i+1).x;
                                                                      int y1 = fingerMomentsXYDataArrayListLeftSide.get(i).y;
                                                                      int y2 = fingerMomentsXYDataArrayListLeftSide.get(i+1).y;

                                                                      leftHandFingerInBetweenDistance.add((int)distanceOfXYPoints(x1,x2,y1,y2));

                                                                      Log.i("FYP","distance between " + i + " and " + (i+1) + " is " + (int)distanceOfXYPoints(x1,x2,y1,y2) );


                                                                  }

                                                              }*/




                                                              //virtual cursor, thumb

                                                          if(thumbRightSide.size() == 1 && PERFORMANCE_MODE){




                                                                rightX = thumbRightSide.get(0).x;
                                                                rightY = thumbRightSide.get(0).y;




                                                                if(rightX > boxFirstLeftX && rightX < boxFirstRightX && rightY < boxHeight ){


                                                                    Logger.Log("box First");


                                                                    if(!VIRTUAL_CURSOR_LOCK) {
                                                                        VIRTUAL_CURSOR_LOCK = true;
                                                                        virtualCursorTimer.postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {


                                                                                if (rightX > boxFirstLeftX && rightX < boxFirstRightX && rightY < boxHeight) {
                                                                                    shiftNotes(12, 1);
                                                                                    runOnUiThread(new Runnable() {
                                                                                        @Override
                                                                                        public void run() {

                                                                                            Toast.makeText(getApplicationContext(), "Shifted Up 1 Oct", Toast.LENGTH_SHORT).show();

                                                                                        }
                                                                                    });

                                                                                }
                                                                                VIRTUAL_CURSOR_LOCK = false;

                                                                            }
                                                                        }, 1500);
                                                                    }

                                                                }

                                                              else if(rightX > boxSecondLeftX && rightX < boxSecondRightX && rightY < boxHeight ){


                                                                    Logger.Log("box Second");
                                                                     if(!VIRTUAL_CURSOR_LOCK) {
                                                                        VIRTUAL_CURSOR_LOCK = true;
                                                                        virtualCursorTimer.postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {


                                                                                if (rightX > boxSecondLeftX && rightX < boxSecondRightX && rightY < boxHeight) {
                                                                                    shiftNotes(12, 0);
                                                                                    runOnUiThread(new Runnable() {
                                                                                        @Override
                                                                                        public void run() {

                                                                                            Toast.makeText(getApplicationContext(), "Shifted Dwn 1 Oct", Toast.LENGTH_SHORT).show();

                                                                                        }
                                                                                    });

                                                                                }
                                                                                VIRTUAL_CURSOR_LOCK = false;

                                                                            }
                                                                        }, 1500);
                                                                    }

                                                              }
                                                              else{


                                                                  virtualCursorTimer.removeCallbacksAndMessages(null);
                                                                  VIRTUAL_CURSOR_LOCK = false;
                                                              }










                                                          }




                                                              //the following lines calculate only the distance between first finger and last finger, assumption is that last element of the list is a finger
                                                          if(fingerMomentsXYDataArrayListLeftSide.size()>1) {
                                                              int x1 = fingerMomentsXYDataArrayListLeftSide.get(0).x;
                                                              int x2 = fingerMomentsXYDataArrayListLeftSide.get(fingerMomentsXYDataArrayListLeftSide.size() - 1).x;
                                                              int y1 = fingerMomentsXYDataArrayListLeftSide.get(0).y;
                                                              int y2 = fingerMomentsXYDataArrayListLeftSide.get(fingerMomentsXYDataArrayListLeftSide.size() - 1).y;
                                                              leftHandFingerInBetweenDistance.add(y1-y2);
                                                          }




                                                          //PLAY NOTES HERE
                                                              if (selectedCursor != null) // there is a cursor
                                                                    //assign notes based on right cursor to left fingers position
                                                                  for (int i = 0; i < fingerMomentsXYDataArrayListLeftSide.size(); i++) {

                                                                      currentYObj1 = selectedCursor.y;

                                                                      if (!SYNTH_PLAYING2) {
                                                                          mSynth2.playOsc();
                                                                          SYNTH_PLAYING2 = true;
                                                                      }

                                                                      //only works for single note slide here
                                                                      if(GLISSANDO_MODE){

                                                                          if(fingerMomentsXYDataArrayListRightSide.size() == 1 && leftHandFingerInBetweenDistance.size()>0){


                                                                                        //within sliding range
                                                                                    if(selectedCursor.y <= fingerMomentsXYDataArrayListLeftSide.get(0).y && selectedCursor.y >= fingerMomentsXYDataArrayListLeftSide.get(fingerMomentsXYDataArrayListLeftSide.size()-1).y){

                                                                                        leftHandFingerInBetweenDistance.get(0);

                                                                                        double glissAmount =fingerMomentsXYDataArrayListLeftSide.get(fingerMomentsXYDataArrayListLeftSide.size()-1).y - selectedCursor.y;
                                                                                        double frequencyTop = notesArrayList.get(3).noteFreqs.get(0);
                                                                                        double frequencyBot = notesArrayList.get(0).noteFreqs.get(0);
                                                                                        double freqDiff = frequencyTop - frequencyBot;
                                                                                        glissAmount = Math.abs(glissAmount);
                                                                                        Logger.Log("Freq diff is " + freqDiff + " , glissAmount is " + glissAmount + " leftHandFingerInBetweenDistance.get(0) " + leftHandFingerInBetweenDistance.get(0));



                                                                                        if(glissAmount!=0)
                                                                                        glissAmount = (glissAmount / leftHandFingerInBetweenDistance.get(0) ) * freqDiff;

                                                                                        if(frequencyTop - glissAmount < frequencyBot)
                                                                                            glissAmount = frequencyTop - frequencyBot;


                                                                                        Logger.Log("Final glissamount is " + glissAmount);


                                                                                        Scales.chordPoint(-glissAmount, 3, shiftNotes(12, SHIFT_MODE, notesArrayList), mSynth2, fingerMomentsXYDataArrayListRightSide.size());


                                                                                    }
                                                                                    else if (selectedCursor.y > fingerMomentsXYDataArrayListLeftSide.get(0).y){

                                                                                        Scales.chordPoint(0, 0, shiftNotes(12, SHIFT_MODE, notesArrayList), mSynth2, fingerMomentsXYDataArrayListRightSide.size());



                                                                                    }




                                                                          }

                                                                          else if(fingerMomentsXYDataArrayListRightSide.size() == 2)
                                                                          {


                                                                              //within sliding range
                                                                              if(selectedCursor.y <= fingerMomentsXYDataArrayListLeftSide.get(0).y && selectedCursor.y >= fingerMomentsXYDataArrayListLeftSide.get(fingerMomentsXYDataArrayListLeftSide.size()-1).y){

                                                                                  leftHandFingerInBetweenDistance.get(0);

                                                                                  double glissAmount =fingerMomentsXYDataArrayListLeftSide.get(fingerMomentsXYDataArrayListLeftSide.size()-1).y - selectedCursor.y;
                                                                                  double frequencyTop = notesArrayList.get(7).noteFreqs.get(0);
                                                                                  double frequencyBot = notesArrayList.get(4).noteFreqs.get(0);
                                                                                  double freqDiff = frequencyTop - frequencyBot;
                                                                                  glissAmount = Math.abs(glissAmount);
                                                                                  Log.i("FYP" , "Freq2 diff is " + freqDiff + " , glissAmount is " + glissAmount + " leftHandFingerInBetweenDistance.get(0) " + leftHandFingerInBetweenDistance.get(0));


                                                                                  if(glissAmount!=0)
                                                                                      glissAmount = (glissAmount / leftHandFingerInBetweenDistance.get(0) ) * freqDiff;

                                                                                  if(frequencyTop - glissAmount < frequencyBot)
                                                                                      glissAmount = frequencyTop - frequencyBot;

                                                                                  Log.i("FYP" , "Final2 glissamount is " + glissAmount);

                                                                                  Scales.chordPoint(-glissAmount, 7, shiftNotes(12, SHIFT_MODE, notesArrayList), mSynth2, fingerMomentsXYDataArrayListRightSide.size());


                                                                              }
                                                                              else if (selectedCursor.y > fingerMomentsXYDataArrayListLeftSide.get(0).y){

                                                                                  Scales.chordPoint(0, 0, shiftNotes(12, SHIFT_MODE, notesArrayList), mSynth2, fingerMomentsXYDataArrayListRightSide.size());


                                                                              }




                                                                          }





                                                                      }


                                                                      if (!GLISSANDO_MODE && selectedCursor.y < fingerMomentsXYDataArrayListLeftSide.get(i).y + 15 && selectedCursor.y > fingerMomentsXYDataArrayListLeftSide.get(i).y - 15)

                                                                      {



                                                                          Boolean noteChanged = Scales.chordPoint(0, i, shiftNotes(12, SHIFT_MODE, notesArrayList), mSynth2, fingerMomentsXYDataArrayListRightSide.size());

                                                                          if (noteChanged) {

                                                                              VIBRATO_ENABLE = false;
                                                                              startVibratoHandler();


                                                                          }

                                                                          if (VIBRATO_ENABLE) {
                                                                              Scales.chordPoint((selectedCursor.y - startPointYObj1) / 2, i, shiftNotes(12, SHIFT_MODE, notesArrayList), mSynth2, fingerMomentsXYDataArrayListRightSide.size());
                                                                          }

                                                                          int distanceDifference = selectedCursor.x - fingerMomentsXYDataArrayListLeftSide.get(i).x;

                                                                          if (!mSynth2.isFilterEnvEnabled())
                                                                              mSynth2.setFilterValue(distanceDifference * 10);


                                                                      }

                                                                  }

                                                              else {


                                                                  mSynth2.releaseOsc();
                                                                  SYNTH_PLAYING2 = false;


                                                              }


                                                              //this is hull convex part, W.I.P
/*

                                                          List<MatOfInt4> ConvexityDefectsMatOfInt4  = new ArrayList<MatOfInt4>();

                                                          List<MatOfInt> hull = new ArrayList<MatOfInt>();
                                                          //List<MatOfInt> hull2 = new ArrayList<MatOfInt>();

                                                          // Find the convex hull
                                                          for(int i=0; i < contours.size(); i++){
                                                              hull.add(new MatOfInt());
                                                          }

                                                          // Convert MatOfInt to MatOfPoint for drawing convex hull
                                                          for(int i=0; i < contours.size(); i++){
                                                              Imgproc.convexHull(contours.get(i), hull.get(i));
                                                           }



                                                          // Loop over all contours
                                                           List<Point[]> hullpoints = new ArrayList<Point[]>();

                                                          for(int i=0; i < hull.size(); i++){
                                                              Point[] points = new Point[hull.get(i).rows()];

                                                              //convex
                                                              ConvexityDefectsMatOfInt4.add(new MatOfInt4());
                                                              Imgproc.convexityDefects(contours.get(i),hull.get(i),ConvexityDefectsMatOfInt4.get(i));


                                                              // Loop over all points that need to be hulled in current contour
                                                              for(int j=0; j < hull.get(i).rows(); j++){
                                                                  int index = (int)hull.get(i).get(j, 0)[0];
                                                                  points[j] = new Point(contours.get(i).get(index, 0)[0], contours.get(i).get(index, 0)[1]);
                                                              }

                                                              hullpoints.add(points);
                                                          }

                                                          Log.i("FYP" , "Size of convex is " + ConvexityDefectsMatOfInt4.size());



                                                          // Convert Point arrays into MatOfPoint
                                                          List<MatOfPoint> hullmop = new ArrayList<MatOfPoint>();
                                                          for(int i=0; i < hullpoints.size(); i++){

                                                               MatOfPoint mop = new MatOfPoint();
                                                              mop.fromArray(hullpoints.get(i));
                                                              hullmop.add(mop);


                                                          }


                                                          for(int i = 0 ; i < hullmop.size(); i ++){



                                                              MatOfPoint points = hullmop.get(i);
                                                              Point[] point = points.toArray();

                                                              Log.i("FYP" , "For iter " + i + " Size of point is " + point.length );


                                                              for(int p = 0 ; p < point.length; p ++)
                                                              Imgproc.putText(mRgba ,String.valueOf(p) , new Point(point[p].x,point[p].y) , Core.FONT_HERSHEY_SIMPLEX,1,new Scalar(192, 222, 162, 255),4);


                                                          }


                                                          // Draw contours + hull results
                                                          //Mat overlay = new Mat(mRgba.size(), CvType.CV_8UC3);
                                                          Scalar color = new Scalar(0, 255, 0);   // Green

                                                              Imgproc.drawContours(mRgba, contours, -1, color);
                                                              Imgproc.drawContours(mRgba, hullmop, -1, color);

*/


                                                              //  Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_RGB2HSV_FULL);



                                                          // this section is responsible for drawing all the lines and rectangles

                                                          if(PERFORMANCE_MODE) {
                                                              Imgproc.rectangle(mRgba, new Point(boxFirstLeftX, 0), new Point(boxFirstRightX, boxHeight), new Scalar(0, 255, 0));
                                                              Imgproc.rectangle(mRgba, new Point(boxSecondLeftX, 0), new Point(boxSecondRightX, boxHeight), new Scalar(0, 255, 0));
                                                          }


                                                          Mat colorLabel = mRgba.submat(4, 68, 4, 68);
                                                          colorLabel.setTo(mBlobColorRgba);

                                                          Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
                                                          mSpectrum.copyTo(spectrumLabel);


                                                              Imgproc.line(mRgba, new Point(width / 2, height - 1), new Point(width / 2, 0), new Scalar(255, 0, 0, 255)
                                                                      , 3);


                                                              onCameraFrameEndTime = System.currentTimeMillis();





                                                              if(HSVMode)
                                                                  Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_RGB2HSV_FULL);

                                                          Log.i("FYP", "Camera frame took " + (onCameraFrameEndTime - onCameraFrameStartTime));



                                                          return mRgba;

                                                      }


                                                  }


        );


        mLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {
                        Log.i("FYP", "OpenCV loaded successfully");
                        mOpenCvCameraView.enableView();


                    }
                    break;
                    default: {
                        super.onManagerConnected(status);
                    }
                    break;
                }
            }
        };


    }


    private double distanceOfXYPoints(double x1, double x2, double y1, double y2){

        double dx = x1 - x2;
        double dy = y1 - y2;


        return Math.sqrt(Math.pow(dx,2) + Math.pow(dy ,2));



    }




    private void resetObjectLatchResetHandler() {

        secondObjHandler.removeCallbacksAndMessages(null);


    }





    private void startObjectLatchResetHandler() {

        secondObjHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetSecondObjLatch();
            }
        }, 300);


    }

    private void startVibratoHandler() {

        vibratoEnablerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                VIBRATO_ENABLE = true;
                startPointXObj1 = currentXObj1;
                startPointYObj1 = currentYObj1;
            }
        }, 700);


    }


    private void resetSecondObjLatch() {

        OCTAVE_UP_LATCHER = false;
        OCTAVE_DWN_LATCHER = false;
    }

    private void selectSecondObject() {


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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, secondObj);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("FYP", "CLICKED CLICKED CLICKED");
                if (position == 0) {


                    SECOND_OBJ_OCTAVE = true;
                    SECOND_OBJ_NOTE = false;
                    SECOND_OBJ_VOLUME = false;


                }

                if (position == 1) {


                    SECOND_OBJ_NOTE = true;
                    SECOND_OBJ_OCTAVE = false;
                    SECOND_OBJ_VOLUME = false;


                }

                if (position == 2) {


                    SECOND_OBJ_NOTE = false;
                    SECOND_OBJ_OCTAVE = false;
                    SECOND_OBJ_VOLUME = true;

                }


            }
        });
        alertDialog.show();


    }


    private void selectSound() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list_for_xy, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("List");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, synthDataTitles);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("FYP", "CLICKED CLICKED CLICKED");
                mSynth.destory();
                mSynth = new Synth(ListOfRoomSynthData.get(position));

                // mSynth.playOsc();
                mSynth2.destory();
                mSynth2 = new Synth(ListOfRoomSynthData.get(position));
                // mSynth2.playOsc();


            }
        });
        alertDialog.show();
    }


    private void selectNotes() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
        LayoutInflater inflater2 = getLayoutInflater();
        View convertView2 = (View) inflater2.inflate(R.layout.list_for_xy, null);
        alertDialog2.setView(convertView2);
        alertDialog2.setTitle("Select Scale");
        ListView lv2 = (ListView) convertView2.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noteDataTitles);
        lv2.setAdapter(adapter2);
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("FYP", "CLICKED CLICKED CLICKED");
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



    private ArrayList<Notes> shiftNotes(int semitones, int mode, ArrayList<Notes> nal) {


        ArrayList<Notes> tempnotes = new ArrayList<Notes>();

        for(int p = 0 ; p < nal.size(); p ++){

            tempnotes.add(new Notes());
            tempnotes.get(p).copyNotesFrom(nal.get(p));


        }


        for (int i = 0; i < tempnotes.size(); i++) {


            tempnotes.get(i).shiftSemitone(semitones, mode);


        }

        return tempnotes;

    }

    private void shiftNotes(int semitones, int mode) {


        for (int i = 0; i < notesArrayList.size(); i++) {


            notesArrayList.get(i).shiftSemitone(semitones, mode);


        }


    }

    private void setExposure(){



        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        final EditText value = new EditText(this);
        value.setInputType(InputType.TYPE_CLASS_NUMBER);
        value.setHint("Value between -12 to 12");
        ll.addView(value);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exposure");
        builder.setMessage("Enter value (Default is 0)");
        builder.setView(ll);
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                int ExpoValue = Integer.valueOf(value.getText().toString());
                mOpenCvCameraView.setExposureCompensation(ExpoValue);

            }
        });

        builder.show();


    }



    private void selectFingersRangeNoAlert(){



        int H = Integer.parseInt(defaultPrefs.getString("fingers_hue" , "12"));
        int S = Integer.parseInt(defaultPrefs.getString("fingers_sat" , "50"));
        int V = Integer.parseInt(defaultPrefs.getString("fingers_value" , "50"));

        Logger.Log("Hue is now  " + H);


        fingerScalarRadius = new Scalar(H,S,V,0);

        mDetector2.setColorRadius(fingerScalarRadius);
        mDetector2.setHsvColor(mBlobColorHsv);
        Imgproc.resize(mDetector2.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);

    }

    private void selectThumbRangeNoAlert() {

        int H = Integer.parseInt(defaultPrefs.getString("thumb_hue" , "12"));
        int S = Integer.parseInt(defaultPrefs.getString("thumb_sat" , "50"));
        int V = Integer.parseInt(defaultPrefs.getString("thumb_value" , "50"));




        thumbScalarRadius = new Scalar(H, S, V, 0);


       mDetector.setColorRadius(thumbScalarRadius);
       mDetector.setHsvColor(mBlobColorHsv);
       Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);

   }

    private void selectFingersRange(){


        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        final EditText etH = new EditText(this);
        etH.setInputType(InputType.TYPE_CLASS_NUMBER);
        etH.setHint("H");

        final EditText etS = new EditText(this);
        etS.setInputType(InputType.TYPE_CLASS_NUMBER);

        etS.setHint("S");

        final EditText etV = new EditText(this);
        etV.setInputType(InputType.TYPE_CLASS_NUMBER);

        etV.setHint("V");

        ll.addView(etH);
        ll.addView(etS);
        ll.addView(etV);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fingers");
        builder.setMessage("Enter HSV Ranges (Default is 12,100,50)");
        builder.setView(ll);
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                int H = Integer.valueOf(etH.getText().toString());
                int S = Integer.valueOf(etS.getText().toString());
                int V = Integer.valueOf(etV.getText().toString());

                fingerScalarRadius = new Scalar(H,S,V,0);

                mDetector2.setColorRadius(fingerScalarRadius);
                mDetector2.setHsvColor(mBlobColorHsv);
                Imgproc.resize(mDetector2.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);

            }
        });


        builder.show();










    }

    private void selectThumbRange(){
        {


            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);

            final EditText etH = new EditText(this);
            etH.setHint("H");

            final EditText etS = new EditText(this);
            etS.setHint("S");

            final EditText etV = new EditText(this);
            etV.setHint("V");

            ll.addView(etH);
            ll.addView(etS);
            ll.addView(etV);



            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Fingers");
            builder.setMessage("Enter HSV Ranges (Default is 12,100,50)");
            builder.setView(ll);
            builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    int H = Integer.valueOf(etH.getText().toString());
                    int S = Integer.valueOf(etS.getText().toString());
                    int V = Integer.valueOf(etV.getText().toString());

                    thumbScalarRadius = new Scalar(H,S,V,0);


                    mDetector.setColorRadius(thumbScalarRadius);
                    mDetector.setHsvColor(mBlobColorHsv);
                    Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);

                }
            });


            builder.show();










        }
    }


    private void setupNoteLayout() {


        int size = notesArrayList.size();

        LinearLayout ll = (LinearLayout) findViewById(R.id.hand_gesture__linear_layout_notes);

        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearParams.weight = 1;


        for (int i = 0; i < size; i++) {

            TextView tv = new TextView(this);

            tv.setText(String.valueOf(i));
            tv.setLayoutParams(linearParams);


            ll.addView(tv);

        }


    }

    private void hideButtonGroup(){

        LinearLayout ll = (LinearLayout)findViewById(R.id.hand_gesture_button_group);
        ll.setVisibility(View.GONE);


    }

    private void showButtonGroup(){

        LinearLayout ll = (LinearLayout)findViewById(R.id.hand_gesture_button_group);
        ll.setVisibility(View.VISIBLE);


    }




    private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }

    private Mat overtrayImage(Mat background, Mat foreground) {
        // The background and the foreground are assumed to be of the same size.
        Mat destination = new Mat(background.size(), background.type());

        for (int y = 0; y < (int) (background.rows()); ++y) {
            for (int x = 0; x < (int) (background.cols()); ++x) {
                double b[] = background.get(y, x);
                double f[] = foreground.get(y, x);

                double alpha = f[3] / 255.0;

                double d[] = new double[3];
                for (int k = 0; k < 3; ++k) {
                    d[k] = f[k] * alpha + b[k] * (1.0 - alpha);
                }

                destination.put(y, x, d);
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






    public void selectColorOnPoint( int mode){

        {
            {

                int x = 0;
                int y = 0;
                if(mode == 0){

                    x = thumbx;
                    y = thumby;

                }
                if (mode == 1){

                    x = fingerx;
                    y = fingery;

                }

                int cols = mRgba.cols();
                int rows = mRgba.rows();

                double xOffset = (mOpenCvCameraView.getWidth() - cols) / 2.35;
                double yOffset = (mOpenCvCameraView.getHeight() - rows) / 2.35;




                double xDiv = (double) mOpenCvCameraView.getWidth() / (double) cols;
                double yDiv = (double) mOpenCvCameraView.getHeight() / (double) rows;

                Log.i("FYP", "Touch imagediv coordinates: (" + mOpenCvCameraView.getWidth() + ", " + cols + ")");


                Log.i("FYP", "Touch imagediv coordinates: (" + xDiv + ", " + yDiv + ")");






                Log.i("FYP", "Touch image coordinates: (" + x + ", " + y + ")");

                if ((x < 0) || (y < 0) || (x > cols) || (y > rows))  return;


                Rect touchedRect = new Rect();

                touchedRect.x = (x > detectSize) ? x - detectSize : 0;
                touchedRect.y = (y > detectSize) ? y - detectSize : 0;


                touchedRect.width = (x + detectSize < cols) ? x + detectSize - touchedRect.x : cols - touchedRect.x;
                touchedRect.height = (y + detectSize < rows) ? y + detectSize - touchedRect.y : rows - touchedRect.y;

                touchedRect.width = touchedRect.width - detectSize;
                touchedRect.height = touchedRect.height - detectSize;

                Log.i("FYP", "Touch touchedRect coordinates: (" + touchedRect.width + ", " + touchedRect.height + ")");



                Mat touchedRegionRgba = mRgba.submat(touchedRect);

                Mat touchedRegionHsv = new Mat();
                Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

                // Calculate average color of touched region
                mBlobColorHsv = Core.sumElems(touchedRegionHsv);
                int pointCount = touchedRect.width * touchedRect.height;
                for (int i = 0; i < mBlobColorHsv.val.length; i++)
                    mBlobColorHsv.val[i] /= pointCount;

                mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);

                Log.i("FYP", "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                        ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");


                //touch the square to raise/lower octave





                    //new Scalar(25,50,50,0);
                if(mode == 0) {

                    if(thumbScalarRadius == null)
                    mDetector.setColorRadius(new Scalar(12, 50, 25, 0));

                    mDetector.setHsvColor(mBlobColorHsv);
                    //mTouchChoice = 1;
                    Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);
                }

                if(mode == 1) {

                    if(fingerScalarRadius == null)
                    mDetector2.setColorRadius(new Scalar(12, 100, 50, 0));

                    mDetector2.setHsvColor(mBlobColorHsv);

                    // mTouchChoice = 0;
                    Imgproc.resize(mDetector2.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);

                }

//                Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);
//                Imgproc.resize(mDetector2.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);


              //  mIsColorSelected = true;

                touchedRegionRgba.release();
                touchedRegionHsv.release();




            }
        }
    }


    private android.graphics.Rect calculateTapArea(float x, float y, float coefficient) {
        int areaSize = Float.valueOf(100 * coefficient).intValue();

        Matrix matrix = new Matrix();

        int left = clamp((int) x - areaSize / 2, 0, width - areaSize);
        int top = clamp((int) y - areaSize / 2, 0, height - areaSize);

        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
        matrix.mapRect(rectF);

        return new android.graphics.Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }



    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
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
                double yDiv = (double) mOpenCvCameraView.getHeight() / (double) rows;

                Log.i("FYP", "Touch imagediv coordinates: (" + mOpenCvCameraView.getWidth() + ", " + cols + ")");


                Log.i("FYP", "Touch imagediv coordinates: (" + xDiv + ", " + yDiv + ")");


                double xx = (double) e.getX() / xDiv;
                double yy = (double) e.getY() / yDiv;

                x = (int) xx;
                y = (int) yy;


                Log.i("FYP", "Touch image coordinates: (" + x + ", " + y + ")");

                if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;


                Rect touchedRect = new Rect();
                android.graphics.Rect focusRect = new android.graphics.Rect(calculateTapArea(x,y,1.0f));


                touchedRect.x = (x > detectSize) ? x - detectSize : 0;
                touchedRect.y = (y > detectSize) ? y - detectSize : 0;


                if(!SELECT_FINGERS && !SELECT_THUMB)
                mOpenCvCameraView.setFocus(focusRect);


                touchedRect.width = (x + detectSize < cols) ? x + detectSize - touchedRect.x : cols - touchedRect.x;
                touchedRect.height = (y + detectSize < rows) ? y + detectSize - touchedRect.y : rows - touchedRect.y;

                touchedRect.width = touchedRect.width - detectSize;
                touchedRect.height = touchedRect.height - detectSize;

                Log.i("FYP", "Touch touchedRect coordinates: (" + touchedRect.width + ", " + touchedRect.height + ")");


                Mat touchedRegionRgba = mRgba.submat(touchedRect);

                Mat touchedRegionHsv = new Mat();
                Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

                // Calculate average color of touched region
                mBlobColorHsv = Core.sumElems(touchedRegionHsv);
                int pointCount = touchedRect.width * touchedRect.height;
                for (int i = 0; i < mBlobColorHsv.val.length; i++)
                    mBlobColorHsv.val[i] /= pointCount;

                mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);

                Log.i("FYP", "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                        ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");


                //touch the square to raise/lower octave

                if (!SELECT_FINGERS && !SELECT_THUMB) {


                    double upperBound = height * 0.7;
                    double lowerBound = height * 0.4;


                    if (y < lowerBound && x < 150 && !OCTAVE_UP_LATCHER) {
                        shiftNotes(12, 1);
                        OCTAVE_UP_LATCHER = true;
                        OCTAVE_DWN_LATCHER = false;


                    } else if (y > upperBound && x < 150 && !OCTAVE_DWN_LATCHER) {
                        shiftNotes(12, 0);
                        OCTAVE_DWN_LATCHER = true;
                        OCTAVE_UP_LATCHER = false;


                    }


                }


                if (SELECT_THUMB) {


                    //new Scalar(25,50,50,0);
                    if(thumbScalarRadius == null)
                        selectThumbRangeNoAlert();
                    //mDetector.setColorRadius(new Scalar(12,50,25,0));

                    mDetector.setHsvColor(mBlobColorHsv);
                    mTouchChoice = 1;
                    Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);
                    mSynth.resetFilterAmp();

                } else if (SELECT_FINGERS) {

                    if(fingerScalarRadius == null) {
                        selectFingersRangeNoAlert();
                        Logger.Log("Fingers are null!!!!!!!!!!");

                    }


                    Logger.Log("Fingers are not null");

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
