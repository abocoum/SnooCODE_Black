package com.tinydavid.snoocodeblack;

import java.util.HashMap;

public class Error {

    public String code, description;

    public Error(){

    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {

        HashMap<String, String> errorString = new HashMap<String, String>(){{
            put("code", code);
            put("description", description);
        }};

        return errorString.toString();
    }
}
