package olioli.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import olioli.dao.INetwork;
import olioli.dao.NetworkConnect;
import olioli.tag.GPSTracker;
import olioli.tag.IGPSTracker;
import olioli.tag.R;

/**
 * Created by Ohardwick on 9/16/14.
 *
 * Establishing auto login &
 * Register
 *
 */
public class Login extends Activity {

    // Global class variables
    public double lat;
    public double lng;

    // Connection to interfaces
    INetwork nc;
    IGPSTracker mGPS;

    // Intents
    Intent locatorService = null;

    // UI variables, access to buttons
    EditText uName;
    EditText pass;
    Button sign_In;
    Button register;
    AlertDialog alertDialog = null;

    /**
     * Create initial login
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        StrictMode.enableDefaults();

        // Collect the user info for login
        uName = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);

        // Establish connnection before starting game
        nc = new NetworkConnect();
        mGPS = new GPSTracker(this);

        // Check that GPS is working
        if(mGPS.canGetLocation()) {}
        // Prompt user to enable GPS
        else { mGPS.showSettingsAlert(); }

        // Ensure Service doesn't fail
        if (!startService()) {
            CreateAlert("Error!", "Service Cannot be started");
        }

        // Establish buttons
        sign_In = (Button) findViewById(R.id.btn_auto_sign);
        register = (Button) findViewById(R.id.btn_register);

        // Direct Login to current game
        sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int ID = nc.Login("ollie","ollie", lat, lng);
                    String tempName = "ollie"; // change to edittext value later
                    Toast.makeText(Login.this, lat +" "+ lng, Toast.LENGTH_LONG).show();
                    Intent next = new Intent(Login.this, CurrentGame.class);
                    next.putExtra("N", tempName);

                    startActivity(next);

                }


        });

        // Register User, NOT IN USE
        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                    Intent reg = new Intent(Login.this, GameSettings.class);
                    startActivity(reg);

            }
        });
    }

    // NOT IN USE
    public boolean stopService() {
        if (this.locatorService != null) {
            this.locatorService = null;
        }
        return true;
    }

    // Execute Asynctask to find user
    public boolean startService() {
        try {
            UserAccess userAccess = new UserAccess();
            userAccess.execute();
            return true;
        } catch (Exception error) {
            return false;
        }

    }

    // Create dialog in case service fails
    public AlertDialog CreateAlert(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(this).create();

        alert.setTitle(title);

        alert.setMessage(message);

        return alert;

    }

    /**
     * AsyncTask to find user
     * Prevents the application from defaulting the user to (0, 0)
     *
     * Shows load dialog so user so they aware of what the app is doing
     */
    public class UserAccess extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog = null;

        public double lati = 0.0;
        public double longi = 0.0;

        public LocationManager locMan;
        public Location local;
        public FirstLocationListener locationListener;

        @Override
        protected void onPreExecute() {
            locationListener = new FirstLocationListener();
            locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // request location
            locMan.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0,
                    locationListener);

            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                // allow cancel
                @Override
                public void onCancel(DialogInterface dialog) {
                    UserAccess.this.cancel(true);
                }
            });
            progressDialog.setMessage("Finding Location...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();


        }

        @Override
        protected void onCancelled(){
            System.out.println("Cancelled by user!");
            progressDialog.dismiss();
            locMan.removeUpdates(locationListener);
        }

        /**
         * Toast User location and assign variables for application upon completion
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();

            Toast.makeText(Login.this,
                    "LATITUDE :" + lati + " LONGITUDE :" + longi,
                    Toast.LENGTH_LONG).show();

            lat = lati;
            lng = longi;
        }

        /**
         * Ensure that the application does not return the default location for user.
         * @param params
         * @return null
         */
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            while (this.lati == 0.0) {

            }
            return null;
        }


        /**
         * Simple locationlistener class that finds current user
         *
         */
    public class FirstLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            int lat = (int) location.getLatitude(); // * 1E6);
            int log = (int) location.getLongitude(); // * 1E6);
            int acc = (int) (location.getAccuracy());

            String info = location.getProvider();
            try {

                lati = location.getLatitude();
                longi = location.getLongitude();

            } catch (Exception e) {

            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("OnProviderDisabled", "OnProviderDisabled");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("onProviderEnabled", "onProviderEnabled");
        }

        @Override
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
            Log.i("onStatusChanged", "onStatusChanged");

        }
    }
    }


}
