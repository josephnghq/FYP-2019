package com.example.joseph.fyp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


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

    ArrayList<String> synthDataTitles;
    ArrayList<String> noteDataTitles;


    private Handler handler;

    private GsonBuilder builder = new GsonBuilder();
    private Gson gson;

    //private ArrayList<SynthData> listOfSynthData = new ArrayList<SynthData>();

    private List<RoomSynthData> ListOfRoomSynthData;
    private List<RoomNotesArrayList> ListOfRoomNotesArrayLists;



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




        if(gsonString2.length()>0){
            listOfNoteData = gson.fromJson(gsonString2,new TypeToken<ArrayList<NotesArrayList>>(){}.getType());
        }

        handler = new Handler();



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


                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(XYPadActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.list_for_xy, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Select Sound");
                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(XYPadActivity.this,android.R.layout.simple_list_item_1,synthDataTitles);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Log.i("FYP" , "CLICKED CLICKED CLICKED");
                        mSynth = new Synth(ListOfRoomSynthData.get(position));


                    }
                });




                final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(XYPadActivity.this);
                LayoutInflater inflater2 = getLayoutInflater();
                View convertView2 = (View) inflater2.inflate(R.layout.list_for_xy, null);
                alertDialog2.setView(convertView2);
                alertDialog2.setTitle("Select Scale");
                ListView lv2 = (ListView) convertView2.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(XYPadActivity.this,android.R.layout.simple_list_item_1,noteDataTitles);
                lv2.setAdapter(adapter2);
                lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Log.i("FYP" , "CLICKED CLICKED CLICKED");
                        notesArrayList = listOfNoteData.get(position).notesArrayList;
                        setupNoteLayout();

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















        mSynth = new Synth();
        mSynth.disableFilterEnv();
        mSynth.selectHighPass();
        mSynth.setfreqQ(6);










        XYLayout = (RelativeLayout) findViewById(R.id.XY_relative_layout);





        XYImg = (ImageView) findViewById(R.id.XY_image);
        XYImg.setOnTouchListener(onTouchListener());

    }





    private void setupNoteLayout(){


        int size = notesArrayList.size();

        LinearLayout ll = (LinearLayout) findViewById(R.id.XYPad_linear_layout_for_notes);

        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT);
        linearParams.weight = 1;


        for(int i = 0 ; i < size ; i++){

        TextView tv = new TextView(this);

        tv.setText(String.valueOf(i));
        tv.setLayoutParams(linearParams);


        ll.addView(tv);

        }





    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

         width = XYLayout.getWidth();
         height = XYLayout.getHeight();

        super.onWindowFocusChanged(hasFocus);
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
                        Scales.chord( 0 , x, width,notesArrayList,mSynth);

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
