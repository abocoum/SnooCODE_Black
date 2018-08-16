package com.tinydavid.blackservicetest;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tinydavid.snoocodeblack.Address;
import com.tinydavid.snoocodeblack.AddressRequest;
import com.tinydavid.snoocodeblack.ResolveAddressRequest;
import com.tinydavid.snoocodeblack.DistanceRequest;
import com.tinydavid.snoocodeblack.Error;
import com.tinydavid.snoocodeblack.LocationRequest;
import com.tinydavid.snoocodeblack.Stamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button get_address, get_coordinates, get_stamp, get_distance, get_locations, verify_stamp, optimize_route;
    Context context;

    LocationsAdapter locationsAdapter;
    List<String[]> locationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        final AddressRequest addressRequest = new AddressRequest(context);
        final LocationRequest locationRequest = new LocationRequest(context);
        final DistanceRequest distanceRequest = new DistanceRequest(context);
        final ResolveAddressRequest resolveAddressRequest = new ResolveAddressRequest(context);

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.locations_dialog, null);
        final ListView placesList = mView.findViewById(R.id.placeslist);
        locationsAdapter = new LocationsAdapter(context, locationList);
        placesList.setAdapter(locationsAdapter);
        final EditText searchBox = mView.findViewById(R.id.searchBox);

        mBuilder.setView(mView);
        final AlertDialog ucAlert = mBuilder.create();

        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                locationRequest.makeRequest(charSequence.toString()).setLocationRequestListener(new LocationRequest.LocationRequestListener() {

                    @Override
                    public void onLocationsReady(Boolean success, List<String[]> locations, Error error) {

                        if(locations != null){

                            System.out.println(Integer.toString(locations.size()));

                            locationsAdapter.clear();
                            locationList.clear();
                            locationList.addAll(locations);
                            locationsAdapter.notifyDataSetChanged();

                        }

                    }

                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        get_locations = findViewById(R.id.get_locations);
        get_locations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ucAlert.show();
            }
        });

        get_address = findViewById(R.id.get_address);
        get_address.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                addressRequest.requestAddress().setAddressRequestListener(new AddressRequest.AddressRequestListener() {

                    @Override
                    public void onAddressUpdated(Address address, Error error, double accuracy) {

                        Toast.makeText(context, Double.toString(accuracy), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, address.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

                    }

                });
            }
        });


        get_coordinates = findViewById(R.id.get_coordinates);
        get_coordinates.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String[] address_1 = new String[]{"Ghana", "Greater Accra Region", "Accra", "4BN-7MM"};
                String[] address_2 = new String[]{"Ghana", "Greater Accra Region", "Accra", "4BN-8OC"};
                String[] address_3 = new String[]{"Ghana", "Greater Accra Region", "Accra", "5OD-WIM"};

                ArrayList<String[]> addresses = new ArrayList<>();
                addresses.add(address_1);
                addresses.add(address_2);
                addresses.add(address_3);

                resolveAddressRequest.ResolveSingleAddress(address_1).setSingleRequestListener(new ResolveAddressRequest.SingleRequestListener() {
                    @Override
                    public void onAddressResolved(Boolean status, Address address, Error error) {

                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, address.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


        get_distance = findViewById(R.id.get_distance);
        get_distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] address_1 = new String[]{"Ghana", "Greater Accra Region", "Accra", "4BN-7MM"};
                String[] address_2 = new String[]{"Ghana", "Greater Accra Region", "Accra", "5OD-WIM"};

                double[] c_address = new double[]{5.689642, -0.302371};

                distanceRequest.requestDistance(c_address, address_2).setDistanceRequestListener(new DistanceRequest.DistanceRequestListener() {

                    @Override
                    public void onCurrentLocationUpdated(Address target, Address origin, double distance, Error error, double accuracy) {

                        Toast.makeText(context, Double.toString(accuracy), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, Double.toString(distance), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, target.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, origin.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onDistanceReady(Address target, Address origin, double distance, Error error) {

                        Toast.makeText(context, Double.toString(distance), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, target.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, origin.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

        });

    }

}
