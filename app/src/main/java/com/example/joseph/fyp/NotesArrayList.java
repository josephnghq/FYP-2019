package com.example.joseph.fyp;

import java.util.ArrayList;

/**
 * Created by Joseph on 9/27/2018.
 */


//contains arraylist of notes/chords to be played, but this one has a name
public class NotesArrayList {

    ArrayList<Notes> notesArrayList = new ArrayList<>();
    String name = "untitled";

    public NotesArrayList(ArrayList<Notes> notesArrayList , String name){
        this.notesArrayList = notesArrayList;
        this.name = name;



    }

    public void setName(String name) {
        this.name = name;
    }
}
