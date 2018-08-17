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
                        mSynth.setFrequency(x-xDelta);
                        mSynth.setFilterValue(y-yDelta);

                        Log.i("FYP" , "x - xDelta is " + (x - xDelta));
                        Log.i("FYP" , "y - yDelta is " + (y - yDelta));


                        break;



                }

                XYLayout.invalidate();
                return true;






            }
        };











    }


}
