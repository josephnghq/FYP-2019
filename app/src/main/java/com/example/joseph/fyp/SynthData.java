package com.example.joseph.fyp;

/**
 * Created by Joseph on 28/8/2018.
 */

public class SynthData {

    public String title = "Untitled Synth";

    public double detuneValue;

    public double env_attack_duration;
    public double env_decay_duration;
    public double env_sustain_duration;
    public double env_release_duration;

    public double env_attack_value;
    public double env_decay_value;
    public double env_sustain_value;
    public double env_release_value;


    public boolean enable_filter_adsr;
    public boolean enable_porta = true;
    public boolean enable_delay;


    public double env_attack_duration_filter;
    public double env_decay_duration_filter;
    public double env_sustain_duration_filter;
    public double env_release_duration_filter;

    public double env_attack_value_filter;
    public double env_decay_value_filter;
    public double env_sustain_value_filter;
    public double env_release_value_filter;


    public int numOfOsc = 1;
    public double DELAY_TIME = 0.3;
    public int num_of_delay_voices = 7;

    public boolean DisableSaw;
    public boolean DisableSine;
    public boolean DisableSqr;
    public boolean DisableTri;


    public boolean enableLowPass;
    public boolean enableHighPass;












}
