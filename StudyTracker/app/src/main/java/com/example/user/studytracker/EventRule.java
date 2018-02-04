package com.example.user.studytracker;

import java.io.Serializable;
import java.util.Calendar;


public class EventRule implements Serializable {
    boolean repeating;
    Calendar startDate;
    Calendar endDate;
    int type;
    int increment;
    int dayOfWeek;
    int oddOrEven;
    Calendar startTime;
    Calendar endTime;

    public EventRule(boolean rep, Calendar startD, Calendar endD, int type, int inc, int dayOfWeek,
                     int oddOreven, Calendar startTime, Calendar endTime){
        this.repeating = rep;
        this.startDate = startD;
        this.endDate = endD;
        this.type = type;
        this.increment = inc;
        this.dayOfWeek = dayOfWeek;
        this.oddOrEven = oddOreven;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //for testing purposes only
    public String toString(){
        return "type"+type+" startDate "+startDate;
    }
}
