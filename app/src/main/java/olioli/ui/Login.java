package olioli.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import olioli.dao.INetwork;
import olioli.dao.NetworkConnect;
import olioli.dto.UserAccess;
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
    public static double lat;
    public static double lng;

    // Connection to interfaces
    INetwork nc;
    IGPSTracker mGPS;
    UserAccess userAccess;

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

        //Establish connnection before starting game
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
            userAccess = new UserAccess(this);
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

}
