package com.example.joseph.fyp;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class XYPadActivity extends AppCompatActivity {

    private int xDelta;
    private int yDelta;
    private RelativeLayout XYLayout;
    private ImageView XYImg;
    private Synth mSynth;
    private int width = 0;
    private int height = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_xypad);

        mSynth = new Synth();
        mSynth.disableFilterEnv();
        mSynth.selectHighPass();
        mSynth.setfreqQ(6);
        XYLayout = (RelativeLayout) findViewById(R.id.XY_relative_layout);




        XYImg = (ImageView) findViewById(R.id.XY_image);
        XYImg.setOnTouchListener(onTouchListener());

    }


    private void startPlayOsc(){




    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

         width = XYLayout.getWidth();
         height = XYLayout.getHeight();

        super.onWindowFocusChanged(hasFocus);
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

    private View.OnTouchListener onTouchListener(){

        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK){

                    case MotionEvent.ACTION_DOWN:

                        mSynth.playOsc();
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)v.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_UP:
                        mSynth.releaseOsc();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        v.setLayoutParams(layoutParams);
                        CMajorScale(x);
                        mSynth.setFilterValue(y);







                        break;



                }

                XYLayout.invalidate();
                return true;






            }
        };











    }


}
