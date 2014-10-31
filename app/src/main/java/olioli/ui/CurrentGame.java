package olioli.ui;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import olioli.dao.INetwork;
import olioli.dao.NetworkConnect;
import olioli.dto.Users;
import olioli.tag.GPSTracker;
import olioli.tag.R;

public class CurrentGame extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Location location;
    Criteria criteria;
    GPSTracker mGPS;

    List<Users> userList;

    INetwork nc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_screen);
        mGPS = new GPSTracker(this);

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);


        userList = new ArrayList<Users>();

        nc = new NetworkConnect();

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

        LatLng ll;
        if (location != null) {
            ll = new LatLng(mGPS.getLatitude(),mGPS.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));


        }

        setUsers();

    }

     public void setUsers() {

         userList.addAll(nc.collectUsers());

         for (final Users users : userList) {

            mMap.addMarker(new MarkerOptions().position(new LatLng(users.getLat(), users.getLng())).title(users.getName()));
            mMap.addCircle(new CircleOptions().center(new LatLng(users.getLat(), users.getLng())).radius(20).fillColor(0xff0000ff));

         }

    }

}
