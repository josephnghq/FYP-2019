package com.example.joseph.fyp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class XYPadActivity extends AppCompatActivity {

    private int xDelta;
    private int yDelta;
    private RelativeLayout XYLayout;
    private ImageView XYImg;
    private Synth mSynth;
    private int width = 0;
    private int height = 0;
    SharedPreferences sharedPref;
    SharedPreferences sharedPref2;

    private GsonBuilder builder = new GsonBuilder();
    private Gson gson;

    private ArrayList<SynthData> listOfSynthData = new ArrayList<SynthData>();
    private ArrayList<NotesArrayList> listOfNoteData = new ArrayList<NotesArrayList>();
    private ArrayList<Notes> notesArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_xypad);

        sharedPref=  this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        sharedPref2=  this.getSharedPreferences(
                getString(R.string.notes_array_list), Context.MODE_PRIVATE);


        gson = builder.create();

        String gsonString = sharedPref.getString(getString(R.string.preference_file_key) , "");
        String gsonString2 = sharedPref2.getString(getString(R.string.notes_array_list) , "");


        Log.i("FYP" , gsonString2);


        if(gsonString.length()>0){
            listOfSynthData = gson.fromJson(gsonString,new TypeToken<ArrayList<SynthData>>(){}.getType());
        }


        if(gsonString2.length()>0){
            listOfNoteData = gson.fromJson(gsonString2,new TypeToken<ArrayList<NotesArrayList>>(){}.getType());
        }


        ArrayList<String> synthDataTitles = new ArrayList<String>();
        ArrayList<String> noteDataTitles = new ArrayList<String>();

        for(int i = 0 ; i < listOfSynthData.size(); i++){

            synthDataTitles.add(listOfSynthData.get(i).title);


        }

        for(int i = 0 ; i < listOfNoteData.size(); i++){

            noteDataTitles.add(listOfNoteData.get(i).name);


        }

        Log.i("FYP" , "size of listofnotedata is " + listOfNoteData.size());










        mSynth = new Synth();
        mSynth.disableFilterEnv();
        mSynth.selectHighPass();
        mSynth.setfreqQ(6);



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list_for_xy, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select Sound");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,synthDataTitles);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("FYP" , "CLICKED CLICKED CLICKED");
                mSynth = new Synth(listOfSynthData.get(position));


            }
        });

        alertDialog.show();



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

            }
        });

        alertDialog2.show();






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
                        Scales.chord(x, width,notesArrayList,mSynth);

                        if(!mSynth.isFilterEnvEnabled())
                        mSynth.setFilterValue(y*10);







                        break;



                }

                XYLayout.invalidate();
                return true;






            }
        };











    }


}
