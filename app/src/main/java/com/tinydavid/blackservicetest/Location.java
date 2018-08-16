package com.tinydavid.blackservicetest;

import java.io.Serializable;

/**
 * Created by fred on 31/05/2017.
 */

public class Location implements Comparable<Location>, Serializable {

    String countryName, divisionName, subdivisionName;
    int weight;

    Location(String country, String division, String subdivision, int w){
        countryName = country;
        divisionName = division;
        subdivisionName = subdivision;
        weight = w;
    }

    @Override
    public int compareTo(Location loc) {
        return this.weight - loc.weight;
    }

}
