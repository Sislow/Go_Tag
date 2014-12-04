package olioli.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import olioli.dao.NetworkConnect;
import olioli.tag.R;

/**
 * Created by Ohardwick on 9/16/14.
 * NOT USED YET AND NOT SURE IF IT WILL BE
 */
public class GameSettings extends Activity {

    String userFile;
    String string;
    EditText username;
    boolean gps = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_screen);

        username = (EditText) findViewById(R.id.name);
        string = username.getText().toString();

        userFile = string;
        gpsConnected(gps);
        writeToUser();

    }

    void writeToUser() {

            FileOutputStream fos = null;
            try {
                fos = openFileOutput(userFile, Context.MODE_PRIVATE);
                fos.write(string.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    boolean gpsConnected(boolean enabled) {

        // Get location service
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);

        // set boolean to gps on/off
        enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check and force user to change setting
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            enabled = true;
        }
        return enabled;
    }



}
