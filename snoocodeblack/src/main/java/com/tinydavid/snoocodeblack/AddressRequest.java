package com.tinydavid.snoocodeblack;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.HashMap;

public class AddressRequest {

    final int ADDRESS_REQUEST = 1;
    double REQUIRED_ACCURACY = 17;

    private Context aContext;
    private AddressRequestListener addressRequestListener;

    public AddressRequest(Context context){
        this.addressRequestListener = null;
        this.aContext = context;
    }

    public AddressRequest requestAddress(){

        removeBroadcastReceivers();
        setBroadcastReceiver();

        REQUIRED_ACCURACY = 17;

        Intent intent = new Intent("com.tinydavid.snoocode_black_service");
        intent.setPackage("com.tinydavid.snoocode");
        intent.putExtra("REQUEST", ADDRESS_REQUEST);
        intent.putExtra("REQUIRED_ACCURACY", REQUIRED_ACCURACY);
        aContext.startService(intent);

        return this;
    }

    public AddressRequest requestAddress(double required_accuracy){

        removeBroadcastReceivers();
        setBroadcastReceiver();

        REQUIRED_ACCURACY = required_accuracy;

        Intent intent = new Intent("com.tinydavid.snoocode_black_service");
        intent.setPackage("com.tinydavid.snoocode");
        intent.putExtra("REQUEST", ADDRESS_REQUEST);
        intent.putExtra("REQUIRED_ACCURACY", REQUIRED_ACCURACY);
        aContext.startService(intent);

        return this;
    }

    public interface AddressRequestListener{
        public void onAddressUpdated(Address address, Error error, double accuracy);
    }

    public void setAddressRequestListener(AddressRequestListener lister){
        this.addressRequestListener = lister;
    }

    public void removeBroadcastReceivers() {

        try {
            aContext.unregisterReceiver(addressBroadcastReceiver);
        }catch (Exception e){

        }

    }

    public void setBroadcastReceiver(){
        aContext.registerReceiver(addressBroadcastReceiver, new IntentFilter("com.snoocode.snoocodeblack.address_broadcast"));
    }

    private BroadcastReceiver addressBroadcastReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            HashMap<String, Object> result = (HashMap<String, Object>) intent.getSerializableExtra("result");

            double accuracy = (double) result.get("accuracy");
            if(accuracy <= REQUIRED_ACCURACY){
                removeBroadcastReceivers();
            }

            HashMap<String, String> error_value = (HashMap<String, String>) result.get("error");
            Error error = new Error();
            error.code = error_value.get("code");
            error.description = error_value.get("description");

            HashMap<String, String> address_value = (HashMap<String, String>) result.get("value");
            Address address = new Address();
            address.country = address_value.get("country");
            address.division = address_value.get("division");
            address.subdivision = address_value.get("subdivision");
            address.code = address_value.get("code");
            address.latitude = address_value.get("latitude");
            address.longitude = address_value.get("longitude");
            address.latitudeNumber = address_value.get("latitudeNum");
            address.longitudeNumber = address_value.get("longitudeNum");
            address.extension = address_value.get("extension");

            addressRequestListener.onAddressUpdated(address, error, accuracy);

        }

    };

}
