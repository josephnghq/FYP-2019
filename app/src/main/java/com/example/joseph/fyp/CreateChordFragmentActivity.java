package com.example.joseph.fyp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateChordFragmentActivity extends AppCompatActivity  {

    private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private static ChordFragmentPage2 CFP2;

    static SharedPreferences sharedPref;
    static SharedPreferences.Editor editor;

    private static GsonBuilder builder = new GsonBuilder();
    private static Gson gson;

    private static ArrayList<NotesArrayList> listOfNotesArrayList = new ArrayList<NotesArrayList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chord_fragment);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        sharedPref=  this.getSharedPreferences(
                getString(R.string.notes_array_list), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        gson = builder.create();
        String gsonString = sharedPref.getString(getString(R.string.notes_array_list) , "");

        if(gsonString.length()>0){
            listOfNotesArrayList = gson.fromJson(gsonString,new TypeToken<ArrayList<NotesArrayList>>(){}.getType());
            Log.i("FYP","Size is " + listOfNotesArrayList.size());

        }



    }







    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new ChordFragment();


                case 1:
                    return CFP2 = new ChordFragmentPage2();


                default:
                    return new ChordFragment();




            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }



    public static class ChordFragment extends Fragment {


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_chord_page_1, container, false);

            return rootView;

        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            final EditText et = (EditText) getView().findViewById(R.id.page_1_edit_text);


            Button enter = getView().findViewById(R.id.page_1_ok_button);
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CFP2.addLines(Integer.parseInt(et.getText().toString()));
                }
            });
        }
    }



    public static class ChordFragmentPage2 extends Fragment {

        public void buildAlertDialog(final NotesArrayList nal){


            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Enter synth name");
            final EditText et = new EditText(getContext());

            builder.setView(et);
            builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String name = et.getText().toString();
                    nal.setName(name);
                    listOfNotesArrayList.add(nal);
                    editor.putString(getString(R.string.notes_array_list), gson.toJson(listOfNotesArrayList));
                    editor.commit();

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();


        }





         HashMap<Integer,ArrayList<Spinner>> spinnerHashMap = new HashMap<Integer, ArrayList<Spinner>>();

         public void initHashMap(int num){

             for(int i = 0 ; i < num ; i++){

                 spinnerHashMap.put(i , new ArrayList<Spinner>());


             }





         }

         public void nextPage(){


             ArrayList<Notes> notesArrayList = new ArrayList<>();

             for(int i = 0 ; i < spinnerHashMap.size(); i ++){

                 notesArrayList.add(new Notes());

                  for(int p = 0 ; p < spinnerHashMap.get(i).size() ; p++){

                   int pos =  spinnerHashMap.get(i).get(p).getSelectedItemPosition();
                   pos = pos + 24; // remember to remove this once we complete all the notes in Scales, because right now our lowest note is C4

                      notesArrayList.get(i).addNotes(Constants.getNote(pos));


                 }




             }

             NotesArrayList nal = new NotesArrayList(notesArrayList , "");
             buildAlertDialog(nal);





         }


        int id = 0;


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_chord_page_2, container, false);


            return rootView;

        }


        public void addLines(int num){

            initHashMap(num);


            for(int i = 0 ; i < num ; i++){

                final int id = i;

                LinearLayout ll = (LinearLayout) getView().findViewById(R.id.page_2_linear_layout);

                LayoutInflater inflater = (LayoutInflater)getView().getContext().getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);

                View v = inflater.inflate(R.layout.page_2_line , null, false);

                final LinearLayout page2LineLL = (LinearLayout) v.findViewById(R.id.page2_lines);


                Button add = v.findViewById(R.id.add_spinner_button);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Spinner spin = new Spinner(getView().getContext());

                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getView().getContext(),
                                R.array.notes_name, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spin.setAdapter(adapter);

                        spinnerHashMap.get(id).add(spin);

                        Log.i("FYP" , "Adding a spinner to id number " + id);

                        page2LineLL.addView(spin);


                    }
                });


                ll.addView(v);




            }






        }


        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            Button btn = getView().findViewById(R.id.page_2_button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextPage();
                }
            });


        }
    }








}
