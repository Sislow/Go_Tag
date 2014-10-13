package olioli.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;

import olioli.tag.GPSTracker;
import olioli.tag.R;

/**
 * Created by Ohardwick on 9/16/14.
 * FAKE LOGIN SCREEN
 */
public class Login extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //CR Comment: Poor choice of button id. try something such as btnSign.
        Button sign = (Button) findViewById(R.id.sign);

        GPSTracker mGPS = new GPSTracker(this);
        //CR Comment: Poor choice of TextView id. try something like txtText,
        //or something that relates to what the textview is used for. Be more specific
        TextView text = (TextView) findViewById(R.id.texts);
        if(mGPS.canGetLocation ){
            mGPS.getLocation();
            //CR Comment: without the space after "Lat" the gps data will be shown directly
            //after Lat. Try "Lat ". Same for Lon
            text.setText("Lat"+mGPS.getLatitude()+"Lon"+mGPS.getLongitude());
        }else{
        	//CR Comment: A better message may help the User understand the problem. "Unable to find your location"
            text.setText("Unabletofind");
            System.out.println("Unable");
        }
        getSharedPreferences("", Context.MODE_PRIVATE);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(Login.this, CurrentGame.class);
                startActivity(next);
            }
        });
    }
}
