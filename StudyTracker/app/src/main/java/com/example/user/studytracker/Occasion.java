package com.example.user.studytracker;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Occasion implements Serializable{

    String name;
    Date start;
    Date end;
    boolean attended;
    boolean inThePast;

        public Occasion(String name, Date start, Date end, boolean attended, boolean inThePast){
            this.name = name;
            this.start = start;
            this.end = end;
            this.attended = attended;
            this.inThePast = inThePast; // deprecated, not currently in use
        }

        //for debugging/testing only
        public String toString(){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

            return "name: "+name +"\n"+"start: "+dateFormat.format(start)+", end: "+dateFormat.format(end);


        }
    }
