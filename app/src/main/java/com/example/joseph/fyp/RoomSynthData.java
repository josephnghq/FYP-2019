package com.example.joseph.fyp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Joseph on 1/17/2019.
 */



@Entity()
public class RoomSynthData {


    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "title")
    public String title;


    @ColumnInfo(name = "detuneValue")
    public double detuneValue;

    @ColumnInfo(name = "env_attack_duration")
    public double env_attack_duration;

    @ColumnInfo(name = "env_decay_duration")
    public double env_decay_duration;

    @ColumnInfo(name = "env_sustain_duration")
    public double env_sustain_duration;

    @ColumnInfo(name = "env_release_duration")
    public double env_release_duration;

    @ColumnInfo(name = "env_attack_value")
    public double env_attack_value;

    @ColumnInfo(name = "env_decay_value")
    public double env_decay_value;

    @ColumnInfo(name = "env_sustain_value")
    public double env_sustain_value;

    @ColumnInfo(name = "env_release_value")
    public double env_release_value;

    @ColumnInfo(name = "enable_filter_adsr")
    public boolean enable_filter_adsr;

    @ColumnInfo(name = "enable_porta")
    public boolean enable_porta = true;

    @ColumnInfo(name = "enable_delay")
    public boolean enable_delay;

    @ColumnInfo(name = "env_attack_duration_filter")
    public double env_attack_duration_filter;

    @ColumnInfo(name = "env_decay_duration_filter")
    public double env_decay_duration_filter;

    @ColumnInfo(name = "env_sustain_duration_filter")
    public double env_sustain_duration_filter;

    @ColumnInfo(name = "env_release_duration_filter")
    public double env_release_duration_filter;

    @ColumnInfo(name = "env_attack_value_filter")
    public double env_attack_value_filter;

    @ColumnInfo(name = "env_decay_value_filter")
    public double env_decay_value_filter;

    @ColumnInfo(name = "env_sustain_value_filter")
    public double env_sustain_value_filter;

    @ColumnInfo(name = "env_release_value_filter")
    public double env_release_value_filter;

    @ColumnInfo(name = "numOfOsc")
    public int numOfOsc = 1;

    @ColumnInfo(name = "DELAY_TIME")
    public double DELAY_TIME = 0.3;

    @ColumnInfo(name = "num_of_delay_voices")
    public int num_of_delay_voices = 7;

    @ColumnInfo(name = "DisableSaw")
    public boolean DisableSaw;

    @ColumnInfo(name = "DisableSine")
    public boolean DisableSine;

    @ColumnInfo(name = "DisableSqr")
    public boolean DisableSqr;

    @ColumnInfo(name = "DisableTri")
    public boolean DisableTri;

    @ColumnInfo(name = "enableLowPass")
    public boolean enableLowPass;

    @ColumnInfo(name = "enableHighPass")
    public boolean enableHighPass;

    @ColumnInfo(name = "ENABLE_LFO")
    public boolean ENABLE_LFO;

    @ColumnInfo(name = "LFO_freq")
    public double LFO_freq;

    @ColumnInfo(name = "LFO_amp")
    public double LFO_amp;

    @ColumnInfo(name = "ENABLE_PM")
    public boolean ENABLE_PM;

    @ColumnInfo(name = "PM_freq")
    public double PM_freq;

    @ColumnInfo(name = "PM_amp")
    public double PM_amp;



}
