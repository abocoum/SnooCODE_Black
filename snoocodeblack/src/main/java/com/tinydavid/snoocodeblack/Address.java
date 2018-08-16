package com.tinydavid.snoocodeblack;

import java.util.HashMap;

public class Address {

    public String country, division, subdivision, code, extension;
    public String latitudeNumber, longitudeNumber;
    public String latitude, longitude;

    public Address(){

    }

    public String getCountry() {
        return country;
    }

    public String getDivision() {
        return division;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public String getCode() {
        return code;
    }

    public String getExtension() {
        return extension;
    }

    public String getLatitudeNumber() {
        return latitudeNumber;
    }

    public String getLongitudeNumber() {
        return longitudeNumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }


    @Override
    public String toString() {

        HashMap<String, Object> addressString = new HashMap<String, Object>(){{
           put("country", country);
           put("division", division);
           put("subdivision", subdivision);
           put("code", code);
           put("extension", extension);
           put("latitude_number", latitudeNumber);
           put("longitude_number", longitudeNumber);
           put("latitude", latitude);
           put("longitude", longitude);

        }};

        return addressString.toString();
    }
}
