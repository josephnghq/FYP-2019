package com.example.joseph.fyp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jsyn.unitgen.Delay;


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
        setContentView(R.layout.activity_xypad);

        mSynth = new Synth();
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

        int divider = width/8;


        if(x <= divider){
            mSynth.setFrequencyWithPorta(Constants.NoteCFreq);
        }
        else if (x > divider && x < divider*2){

            mSynth.setFrequencyWithPorta(Constants.NoteDFreq);

        }
        else if (x > divider*2 && x < divider*3){

            mSynth.setFrequencyWithPorta(Constants.NoteEfreq);

        }
        else if (x > divider*3 && x < divider*4){

            mSynth.setFrequencyWithPorta(Constants.NoteFfreq);

        }
        else if (x > divider*4 && x < divider*5){

            mSynth.setFrequencyWithPorta(Constants.NoteGfreq);

        }
        else if (x > divider*5 && x < divider*6){

            mSynth.setFrequencyWithPorta(Constants.NoteAfreq);

        }

        else if (x > divider*6 && x < divider*7){

            mSynth.setFrequencyWithPorta(Constants.NoteBfreq);

        }
        else if (x > divider*7 && x < divider*8){

            mSynth.setFrequencyWithPorta(Constants.NoteC2freq);

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
