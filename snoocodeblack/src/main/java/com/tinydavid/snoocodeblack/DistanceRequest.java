package com.tinydavid.snoocodeblack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.HashMap;

public class DistanceRequest {

    final int DISTANCE_REQUEST = 5;
    double REQUIRED_ACCURACY = 17;

    private Context sContext;
    private DistanceRequestListener distanceRequestListener;

    public DistanceRequest(Context context){
        this.distanceRequestListener = null;
        this.sContext = context;
    }

    public DistanceRequest(Context context, DistanceRequestListener listener){
        this.distanceRequestListener = listener;
        this.sContext = context;
    }

    public DistanceRequest requestDistance(String [] target){

        removeBroadcastReceiver();
        setBroadcastReceiver();

        REQUIRED_ACCURACY = 17;

        Intent intent = new Intent("com.tinydavid.snoocode_black_service");
        intent.setPackage("com.tinydavid.snoocode");
        intent.putExtra("REQUEST", DISTANCE_REQUEST);
        intent.putExtra("TYPE", 1);
        intent.putExtra("REQUIRED_ACCURACY", REQUIRED_ACCURACY);
        intent.putExtra("LOCATION_1", target);
        sContext.startService(intent);

        return this;
    }

    public DistanceRequest requestDistance(String[] target, double required_accuracy){

        removeBroadcastReceiver();
        setBroadcastReceiver();

        REQUIRED_ACCURACY = required_accuracy;

        Intent intent = new Intent("com.tinydavid.snoocode_black_service");
        intent.setPackage("com.tinydavid.snoocode");
        intent.putExtra("REQUEST", DISTANCE_REQUEST);
        intent.putExtra("TYPE", 2);
        intent.putExtra("REQUIRED_ACCURACY", REQUIRED_ACCURACY);
        intent.putExtra("LOCATION_1", target);
        sContext.startService(intent);

        return this;
    }

    public DistanceRequest requestDistance(String[] target, String[] origin){

        removeBroadcastReceiver();
        setBroadcastReceiver();

        REQUIRED_ACCURACY = 17;

        Intent intent = new Intent("com.tinydavid.snoocode_black_service");
        intent.setPackage("com.tinydavid.snoocode");
        intent.putExtra("REQUEST", DISTANCE_REQUEST);
        intent.putExtra("TYPE", 3);
        intent.putExtra("REQUIRED_ACCURACY", REQUIRED_ACCURACY);
        intent.putExtra("LOCATION_1", target);
        intent.putExtra("LOCATION_2", origin);
        sContext.startService(intent);

        return this;
    }

    public DistanceRequest requestDistance(double[] target, String[] origin){

        removeBroadcastReceiver();
        setBroadcastReceiver();

        REQUIRED_ACCURACY = 17;

        Intent intent = new Intent("com.tinydavid.snoocode_black_service");
        intent.setPackage("com.tinydavid.snoocode");
        intent.putExtra("REQUEST", DISTANCE_REQUEST);
        intent.putExtra("TYPE", 4);
        intent.putExtra("REQUIRED_ACCURACY", REQUIRED_ACCURACY);
        intent.putExtra("LOCATION_1", new String[]{Double.toString(target[0]), Double.toString(target[1])});
        intent.putExtra("LOCATION_2", origin);
        sContext.startService(intent);

        return this;
    }

    public DistanceRequest requestDistance(String[] target, double[] origin){

        removeBroadcastReceiver();
        setBroadcastReceiver();

        REQUIRED_ACCURACY = 17;

        Intent intent = new Intent("com.tinydavid.snoocode_black_service");
        intent.setPackage("com.tinydavid.snoocode");
        intent.putExtra("REQUEST", DISTANCE_REQUEST);
        intent.putExtra("TYPE", 5);
        intent.putExtra("REQUIRED_ACCURACY", REQUIRED_ACCURACY);
        intent.putExtra("LOCATION_1", target);
        intent.putExtra("LOCATION_2", new String[]{Double.toString(origin[0]), Double.toString(origin[1])});
        sContext.startService(intent);

        return this;
    }

    public interface DistanceRequestListener{
        public void onCurrentLocationUpdated(Address target, Address origin, double distance , Error error, double accuracy);
        public void onDistanceReady(Address target, Address origin, double distance , Error error);
    }

    public void setDistanceRequestListener(DistanceRequestListener listener){
        this.distanceRequestListener = listener;
    }

    public void removeBroadcastReceiver() {

        try {
            sContext.unregisterReceiver(distanceBroadcastReceiver);
        }catch (Exception e){

        }

    }

    public void setBroadcastReceiver(){
        sContext.registerReceiver(distanceBroadcastReceiver, new IntentFilter("com.snoocode.snoocodeblack.distance_broadcast"));
    }

    private BroadcastReceiver distanceBroadcastReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            HashMap<String, Object> result = (HashMap<String, Object>) intent.getSerializableExtra("result");

            double accuracy = (double) result.get("accuracy");
            if(accuracy <= REQUIRED_ACCURACY){
                removeBroadcastReceiver();
            }

            HashMap<String, String> error_value = (HashMap<String, String>) result.get("error");
            Error error = new Error();
            error.code = error_value.get("code");
            error.description = error_value.get("description");

            HashMap<String, Object> value = (HashMap<String, Object>) result.get("value");

            Address target = new Address();
            HashMap<String, Object> target_location = (HashMap<String, Object>) value.get("target_location");
            target.country = (String) target_location.get("country");
            target.division = (String) target_location.get("division");
            target.subdivision = (String) target_location.get("subdivision");
            target.code = (String) target_location.get("code");
            target.latitude = (String) target_location.get("latitude");
            target.longitude = (String) target_location.get("longitude");
            target.latitudeNumber = (String) target_location.get("latitudeNum");
            target.longitudeNumber = (String) target_location.get("longitudeNum");

            Address origin = new Address();
            HashMap<String, Object> origin_location = (HashMap<String, Object>) value.get("origin_location");
            origin.country = (String) origin_location.get("country");
            origin.division = (String) origin_location.get("division");
            origin.subdivision = (String) origin_location.get("subdivision");
            origin.code = (String) origin_location.get("code");
            origin.latitude = (String) origin_location.get("latitude");
            origin.longitude = (String) origin_location.get("longitude");
            origin.latitudeNumber = (String) origin_location.get("latitudeNum");
            origin.longitudeNumber = (String) origin_location.get("longitudeNum");

            String distanceString = (String) value.get("distance");
            double distance = 0.0;

            if(!distanceString.equalsIgnoreCase("NA")){
                distance = Double.parseDouble(distanceString);
            }

            if(accuracy < 0){
                distanceRequestListener.onDistanceReady(target, origin, distance, error);
            }else{
                distanceRequestListener.onCurrentLocationUpdated(target, origin, distance, error, accuracy);
            }

        }

    };

}
