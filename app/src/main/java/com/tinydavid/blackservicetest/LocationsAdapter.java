package com.tinydavid.blackservicetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fred on 31/05/2017.
 */

public class LocationsAdapter extends ArrayAdapter<String[]> implements Filterable {

    private LayoutInflater inflater;
    private List<String[]> locations;

    LocationsAdapter(Context context, List<String[]> locs) {
        super(context, R.layout.locations_object);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        locations = locs;
    }



    public int getCount(){
        return locations.size();
    }

    public String[] getItem(int position){
        return locations.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder holder;
        convertView = inflater.inflate(R.layout.locations_object, null);
        holder = new ViewHolder();
        holder.subdivision = convertView.findViewById(R.id.subdivision);
        holder.division = convertView.findViewById(R.id.division);
        holder.country = convertView.findViewById(R.id.country);

        String cName = locations.get(position)[0];
        String dName = locations.get(position)[1];
        String sName = locations.get(position)[2];

        holder.subdivision.setText(sName);
        holder.division.setText(dName);
        holder.country.setText(cName);

        holder.refreshUI();

        return convertView;
    }


    static class ViewHolder {

        TextView subdivision, division, country;

        void refreshUI(){

            ArrayList<String> hiddenDivs = new ArrayList<String>();

            hiddenDivs.add("Mali Region");
            hiddenDivs.add("Central African Republic (CAR) Prefecture");
            hiddenDivs.add("Chad Region");
            hiddenDivs.add("Ethiopia Region");
            hiddenDivs.add("Abidjan District");
            hiddenDivs.add("Yamoussoukro District");
            hiddenDivs.add("Hamburg");
            hiddenDivs.add("Berlin");

            String sName = subdivision.getText().toString();
            String dName = division.getText().toString();

            if(sName.equals("--null--")){
                subdivision.setVisibility(View.GONE);
            }else{
                subdivision.setVisibility(View.VISIBLE);
            }

            if(dName.equals("--null--") || hiddenDivs.indexOf(dName) != -1){
                division.setVisibility(View.GONE);
            }else{
                division.setVisibility(View.VISIBLE);
            }

        }

    }

}
