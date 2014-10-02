package olioli.ui;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import olioli.dto.Paths;
import olioli.tag.GPSTracker;
import olioli.tag.R;

/**
 * Created by Ohardwick on 9/16/14.
 * The main activity for the Current GAME
 * In works with changing the way it tracks users to pass to the PATHS in dto
 */


public class CurrentGame extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Location location;
    Criteria criteria;
    GPSTracker mGPS;

    ArrayList<Double> lng = new ArrayList<Double>();
    ArrayList<Double> lat = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_screen);
        mGPS = new GPSTracker(this);

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);

        if(mGPS.canGetLocation ){
            mGPS.getLocation();
            Log.v("location", "Lat" + mGPS.getLatitude() + "Lon" + mGPS.getLongitude());
            lat.add(0, mGPS.getLatitude());
            lng.add(0, mGPS.getLongitude());

        }else{
            Log.v("location","Unabletofind");
            System.out.println("Unable");
        }

        location = service.getLastKnownLocation(provider);
        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }

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

        //Paths.lat = location.getLatitude();
        //Paths.lon = location.getLongitude();
        //Paths.profile = "otis";

        LatLng ll = null;
        if (location != null) {
            ll = new LatLng(mGPS.getLatitude(),mGPS.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));


        }
        //setUsers();

        //for (Paths.users) {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(mGPS.getLatitude(), mGPS.getLongitude())).title("Mark"));
        //mMap.addCircle(new CircleOptions().center(new LatLng(0,0)).radius(20).fillColor(0xff0000ff));
        //}
    }

    /**
     *  This places fake points for users on the map
     *  
     * 
     */
    private void setUsers() {

        ArrayList<String> use = new ArrayList<String>();
        ArrayList<Double> lat = new ArrayList<Double>();
        ArrayList<Double> lon = new ArrayList<Double>();

        use.add(0, "Mark");
        use.add(1, "Tim");
        use.add(2, "Oliver");
        use.add(3, "Ashley");

        lat.add(0, 15.0);
        lat.add(1, 1.0);
        lat.add(2, 45.0);
        lat.add(3, 39.142359);

        lon.add(0, 0.0);
        lon.add(1, 10.0);
        lon.add(2, 20.0);
        lon.add(3, -84.519517);

        Paths.users = use;
        Paths.latList = lat;
        Paths.lonList = lon;
        int index = 0;

        for (String s : use) {

            if (lat != null || lon != null) {

                mMap.addMarker(new MarkerOptions().position(new LatLng(lat.get(index), lon.get(index))).title(s));
                mMap.addCircle(new CircleOptions().center(new LatLng(lat.get(index), lon.get(index))).radius(10)).setFillColor(getResources().getColor(R.color.LawnGreen));

                index++;

            }
        }
    }

}
