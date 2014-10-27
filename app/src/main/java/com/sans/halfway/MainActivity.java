package com.sans.halfway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends Activity {


    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView txmyloc;
    private EditText txfloc;
    private String friends;
    private TextView txtwindow;
    private GeoPoint myloc = new GeoPoint(0, 0);
    private FbReferencer fibi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        txmyloc = (TextView) findViewById(R.id.gps_my);
        txfloc = (EditText) findViewById(R.id.gpsf);
        txtwindow = (TextView) findViewById(R.id.textfullview1);
        fibi=new FbReferencer();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

            public void onLocationChanged(Location location) {
                if (location != null) {
                    myloc = new GeoPoint(location.getLatitude(), location.getLongitude());
                    txmyloc.setText(myloc.toString());
                }
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600, 50, locationListener);

        final Button button1 = (Button) findViewById(R.id.bu_route);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String friendloc = txfloc.getText().toString();
                checkFriendInput(friendloc);

            }
        });
    }



    public String makeURL(double startLatitude, double startLongitude, String floc) {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("https://maps.googleapis.com/maps/api/directions/json");
        localStringBuilder.append("?origin=");
        localStringBuilder.append(Double.toString(startLatitude));
        localStringBuilder.append(",");
        localStringBuilder.append(Double.toString(startLongitude));
        localStringBuilder.append("&destination=");
        localStringBuilder.append(floc);
        localStringBuilder.append("&sensor=false&mode=walking");
        return localStringBuilder.toString();
    }

    public String makeGeoURL(String addr) {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("https://maps.googleapis.com/maps/api/geocode/json");
        localStringBuilder.append("?address=");
        localStringBuilder.append(addr);
        localStringBuilder.append("&sensor=false");
        return localStringBuilder.toString();
    }


    public void checkFriendInput( String s) {
        String locals = s.trim();
        friends = "";
        if (locals.length() < 3) {
            txtwindow.setText("False input");
            txfloc.setText("");
            return;
        }
        if (locals.equals("unknown") || ((!locals.matches("^-?[0-9]{1,3}(.[0-9]{0,16})?,-?[0-9]{1,3}(.[0-9]{0,16})?$")) && (!locals.matches("^[a-zA-Z]{1,50}\\s?([0-9]{0,4})?\\s?([0-9]{4})?\\s?([a-zA-Z]{1,30})?$")))) {
            txtwindow.setText("False input");
            txfloc.setText("Address or GPS");
            return;
        }
        friends = locals;
        JSonParser jpar = null;
        try {
            jpar = new JSonParser();
        } catch (JSONException e) {
            Log.e("Json parser initialization: ", e.toString());
        }

        if (locals.matches("^[a-zA-Z]{1,50}\\s?([0-9]{0,4})?\\s?([0-9]{4})?\\s?([a-zA-Z]{1,30})?$")) {
            locals = locals.replaceAll("\\s", "+");

            if (jpar != null) {
              //  Log.i("REQUEST","Starting Request Location");
                jpar.getJSONFromUrl(makeGeoURL(locals), new ResponseListener() {
                    public void onResponseComplete(String response) {
                        //Log.d("onResponse ", response);
                        Log.i("REQUEST",response);
                        try {
                            final JSONObject json = new JSONObject(response);
                            if (json != null) {

                                txtwindow.setText("Friend location wird berechnet...");
                                final JSONObject res = json.getJSONArray("results").getJSONObject(0);
                                final JSONObject geo = res.getJSONObject("geometry");
                                final JSONObject loc = geo.getJSONObject("location");
                                final double lat = loc.getDouble("lat");
                                final double lgt = loc.getDouble("lng");
                                final String place = res.getString("formatted_address");

                                txfloc.setText(String.valueOf(lat) + "," + String.valueOf(lgt));
                                friends = String.valueOf(lat) + "," + String.valueOf(lgt);
                                txtwindow.setText(place + "\nGPS:" + lat + "," + lgt);
                                checkFriendInput(friends);
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", e.toString());
                        }
                    }
                });
            }

        } else {

        Log.i("friendvar", friends);
            assert jpar != null;
            jpar.getJSONFromUrl(makeURL(myloc.getLat(), myloc.getLgt(), friends), new ResponseListener() {
                public void onResponseComplete(String response) {
                    //Log.d("onResponse ", response);
                    Calct2 c = new Calct2();
                    int wayt = 0;
                    try {
                        final JSONObject json = new JSONObject(response);
                        txtwindow.setText("Mittelpunkt wird berechnet...");
                        final JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
                        final JSONObject leg = jsonRoute.getJSONArray("legs").getJSONObject(0);
                        final JSONArray r = leg.getJSONArray("steps");
                        int i;
                        for (i = 0; i < r.length(); i++) {
                            final JSONObject step = r.getJSONObject(i);
                            final int length = step.getJSONObject("distance").getInt("value");
                            wayt += length;
                            final double startLocLat = step.getJSONObject("start_location").getDouble("lat");
                            final double startLocLng = step.getJSONObject("start_location").getDouble("lng");
                            final double endLocLat = step.getJSONObject("start_location").getDouble("lat");
                            final double endLocLng = step.getJSONObject("start_location").getDouble("lng");
                            c.add(new JStep(length, startLocLat, startLocLng, endLocLat, endLocLng));
                        }
                        c.setTotaldistance(wayt);
                        JStep halfroute = c.getHalf();
                        txtwindow.setText(halfroute.getEnd().toString());
                        try {
                            Intent intent = new Intent(findViewById(android.R.id.content).getContext(), WebWayView.class);
                            WebWayView.gp = halfroute.getEnd();
                            startActivity(intent);

                        } catch (Exception e) {
                            Log.e("Intent webview: ", e.toString());
                        }
                    } catch (JSONException e) {
                        Log.e("JSONException ", e.toString());
                    }
                }
            });
        }


    }


}