package com.tinydavid.snoocodeblack;

import java.util.HashMap;

public class Stamp {

    public String day, date, time, stamp_string, key;

    public Stamp(){

    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getKey() {
        return key;
    }

    public String getStamp_string() {
        return stamp_string;
    }

    @Override
    public String toString() {

        HashMap<String, String> stampString = new HashMap<String, String>(){{
            put("day", day);
            put("date", date);
            put("time", time);
            put("key", key);
            put("stamp_string", stamp_string);
        }};

        return stampString.toString();
    }
}
