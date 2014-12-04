package olioli.ui;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import olioli.dao.INetwork;
import olioli.dao.NetworkConnect;
import olioli.dto.Users;
import olioli.tag.GPSTracker;
import olioli.tag.IGPSTracker;
import olioli.tag.R;

public class CurrentGame extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    // Connection to interfaces
    INetwork nc;
    IGPSTracker mGPS;

    // Variables with-in Current game
    List<Users> userList;
    CircleOptions cO;
    String tempName;

    // Location tracking
    Location location;
    Criteria criteria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_screen);

        // retrieve current user name
        tempName = getIntent().getStringExtra("N");

        // instantiate interfaces
        mGPS = new GPSTracker(this);
        nc = new NetworkConnect();

        // Build map
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        location = service.getLastKnownLocation(provider);

        // instantiate array of users, to be filled later
        userList = new ArrayList<Users>();

        // Begin setting up Map for game
        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }

    /**
     * Sets up map if it has not been done yet
     *
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        LatLng ll;
        if (location != null) {
            ll = new LatLng(mGPS.getLatitude(),mGPS.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));


        }

        setUsers();

    }

    /**
     * Method for setting users retrieved from database
     *
     */
     public void setUsers() {

        // create circles
        cO = new CircleOptions();

        // fill local arraylist for counting via PHP NetworkConnect class
        userList.addAll(nc.getUsers());

         // iterate through users and plot their location
         for (final Users users : userList) {

             // ignore local user
             if(users.getName() != tempName) {

                 mMap.addMarker(new MarkerOptions().position(new LatLng(users.getLat(), users.getLng())).title(users.getName()));
                 mMap.addCircle(cO.center(new LatLng(users.getLat(), users.getLng())).radius(20).fillColor(0xff0000ff));

             } else {
                 // nothing
                 tagSystem();
             }

         }

    }

    /**
     * Calculate distance for user from other users
     * Checks for tagging.
     * NOT WORKING
     */
    public void tagSystem() {

        for (final Users users : userList) {

            float[] distance = new float[2];

            Location.distanceBetween(mGPS.getLatitude(), mGPS.getLongitude(), users.getLat(), users.getLng(), distance);

            if (distance[0] > cO.getRadius()) {
                //NEED CIRCLE
                Toast.makeText(this, "YOU TAGGED", Toast.LENGTH_LONG).show();
            }else if (distance[0] < cO.getRadius()) {
                //Do what you need
                Toast.makeText(this, "NOPE", Toast.LENGTH_LONG).show();
            }

        }
    }


}

