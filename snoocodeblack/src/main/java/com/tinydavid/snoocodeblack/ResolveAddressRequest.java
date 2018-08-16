package com.tinydavid.snoocodeblack;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.HashMap;

public class ResolveAddressRequest{

    final int COORDINATES_REQUEST = 2;

    private Context cContext;
    private Activity cActivity;

    public SingleRequestListener singleRequestListener;
    public MultipleRequestListener multipleRequestListener;

    public ResolveAddressRequest(Context context){
        this.cContext = context;
        this.cActivity = (Activity) context;

    }

    public ResolveAddressRequest ResolveSingleAddress(String[] address){

        removeBroadcastReceiver();
        setBroadcastReceiver();

        Intent intent = new Intent("com.tinydavid.snoocode_black_service");
        intent.setPackage("com.tinydavid.snoocode");
        intent.putExtra("TYPE", 1);
        intent.putExtra("REQUEST", COORDINATES_REQUEST);
        intent.putExtra("ADDRESS", address);
        cContext.startService(intent);

        return this;
    }

    public interface SingleRequestListener{
        public void onAddressResolved(Boolean status, Address address, Error error);
    }

    public void setSingleRequestListener(SingleRequestListener listener){
        this.singleRequestListener = listener;
    }

    public ResolveAddressRequest ResolveMultipleAddresses(ArrayList<String[]> addresses){

        removeBroadcastReceiver();
        setBroadcastReceiver();

        Intent intent = new Intent("com.tinydavid.snoocode_black_service");
        intent.setPackage("com.tinydavid.snoocode");
        intent.putExtra("TYPE", 2);
        intent.putExtra("REQUEST", COORDINATES_REQUEST);
        intent.putExtra("ADDRESSES", addresses);
        cContext.startService(intent);

        return this;
    }

    public interface MultipleRequestListener{
        public void onAddressesResolved(ArrayList<Object[]> result, Error error);
    }

    public void setMultipleRequestListener(MultipleRequestListener listener){
        this.multipleRequestListener = listener;
    }

    public void removeBroadcastReceiver() {

        try {
            cContext.unregisterReceiver(coordinatesBroadcastReceiver);
        }catch (Exception e){

        }

    }

    public void setBroadcastReceiver(){
        cContext.registerReceiver(coordinatesBroadcastReceiver, new IntentFilter("com.snoocode.snoocodeblack.coordinates_broadcast"));
    }

    private BroadcastReceiver coordinatesBroadcastReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            removeBroadcastReceiver();

            HashMap<String, Object> result = (HashMap<String, Object>) intent.getSerializableExtra("result");
            HashMap<String, String> error_value = (HashMap<String, String>) result.get("error");

            Error error = new Error();
            error.code = error_value.get("code");
            error.description = error_value.get("description");

            Integer type = intent.getIntExtra("type", 1);

            if(type == 1){

                HashMap<String, Object> value = (HashMap<String, Object>) result.get("value");
                boolean status = (boolean) value.get("status");

                Address address = new Address();
                address.country = (String) value.get("country");
                address.division = (String) value.get("division");
                address.subdivision = (String) value.get("subdivision");
                address.code = (String) value.get("code");
                address.extension = (String) value.get("extension");
                address.latitude = (String) value.get("latitude");
                address.longitude = (String) value.get("longitude");
                address.latitudeNumber = (String) value.get("latitudeNum");
                address.longitudeNumber = (String) value.get("longitudeNum");

                if(singleRequestListener != null){
                    singleRequestListener.onAddressResolved(status, address, error);
                }

            }else if(type == 2){

                ArrayList<Object[]> requestResults = new ArrayList<>();

                ArrayList<HashMap<String, Object>> value = (ArrayList<HashMap<String, Object>>) result.get("value");
                for(HashMap<String, Object> val : value){

                    boolean status = (boolean) val.get("status");

                    Address address = new Address();
                    address.country = (String) val.get("country");
                    address.division = (String) val.get("division");
                    address.subdivision = (String) val.get("subdivision");
                    address.code = (String) val.get("code");
                    address.extension = (String) val.get("extension");
                    address.latitude = (String) val.get("latitude");
                    address.longitude = (String) val.get("longitude");
                    address.latitudeNumber = (String) val.get("latitudeNum");
                    address.longitudeNumber = (String) val.get("longitudeNum");

                    Error address_error = new Error();
                    address_error.code = (String) val.get("error_code");
                    address_error.description = (String) val.get("description");

                    requestResults.add(new Object[]{status, address, error});

                    if(multipleRequestListener != null){
                        multipleRequestListener.onAddressesResolved(requestResults, error);
                    }

                }
            }

        }

    };

}
