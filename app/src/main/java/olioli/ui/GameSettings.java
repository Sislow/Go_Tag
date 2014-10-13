package olioli.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;

import olioli.tag.R;

/**
 * Created by Ohardwick on 9/16/14.
 * not sure if we are using this yet but it might be used for different user settings
 */
public class GameSettings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.setting_screen);
        addPreferencesFromResource(R.xml.preference);
        //CR Comment: better nameing for the button. Perhaps btnProfile
        Button button = (Button) findViewById(R.id.profile);
 //       button.setOnClickListener(new View.OnClickListener() {
  //          @Override
  //          public void onClick(View view) {
   //             Intent game = new Intent(GameSettings.this, CurrentGame.class);
   //             startActivity(game);
        //    }
   //     });
    }
}
