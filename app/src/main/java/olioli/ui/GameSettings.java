package olioli.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;

import olioli.tag.R;

/**
 * Created by Ohardwick on 9/16/14.
 */
public class GameSettings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.setting_screen);
        addPreferencesFromResource(R.xml.preference);
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
