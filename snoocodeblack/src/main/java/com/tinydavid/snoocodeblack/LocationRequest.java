package com.tinydavid.snoocodeblack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.HashMap;
import java.util.List;

public class LocationRequest {

    final int LOCATION_REQUEST = 6;
    double REQUIRED_ACCURACY = 17;

    private Context lContext;
    private LocationRequestListener locationRequestListener;

    public LocationRequest(Context context){
        this.locationRequestListener = null;
        this.lContext = context;
    }

    public LocationRequest(Context context, LocationRequestListener listener){
        this.locationRequestListener = listener;
        this.lContext = context;
    }

    public LocationRequest makeRequest(String filter_string){

        Intent intent = new Intent("com.tinydavid.snoocode_black_service");
        intent.setPackage("com.tinydavid.snoocode");
        intent.putExtra("REQUEST", LOCATION_REQUEST);
        intent.putExtra("REQUIRED_ACCURACY", REQUIRED_ACCURACY);
        intent.putExtra("FILTER_STRING", filter_string);
        lContext.startService(intent);

        lContext.registerReceiver(locationBroadcastReceiver, new IntentFilter("com.snoocode.snoocodeblack.locations_broadcast"));

        return this;
    }


    public interface LocationRequestListener{
        public void onLocationsReady(Boolean success, List<String[]> locations, Error error);
    }


    public void setLocationRequestListener(LocationRequestListener lister){
        this.locationRequestListener = lister;
    }

    public void removeBroadCastReceiver() {

        try {
            lContext.unregisterReceiver(locationBroadcastReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private BroadcastReceiver locationBroadcastReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            HashMap<String, Object> result = (HashMap<String, Object>) intent.getSerializableExtra("result");

            HashMap<String, String> error_value = (HashMap<String, String>) result.get("error");
            Error error = new Error();
            error.code = error_value.get("code");
            error.description = error_value.get("description");

            boolean success = false;
            List<String[]> locations = null;

            if(error.getCode().equalsIgnoreCase("001")){

                success = true;
                HashMap<String, Object> locations_value = (HashMap<String, Object>) result.get("value");
                locations = (List<String[]>) locations_value.get("locations");

            }

            locationRequestListener.onLocationsReady(success, locations, error);

            removeBroadCastReceiver();

        }

    };

}
