package olioli.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import olioli.dao.NetworkConnect;
import olioli.tag.R;

/**
 * Created by Ohardwick on 9/16/14.
 *
 * Establishing auto login &
 * Register
 *
 */
public class Login extends Activity {

    NetworkConnect nc;
    Button sign_In;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
     // Code Review Comment: sign_In is not a proper naming convention. Should be signIn, or btnSignIn
        // Establish button
        sign_In = (Button) findViewById(R.id.btn_auto_sign);

        // Establish connnection before starting game
        nc = new NetworkConnect();
        nc.connect();



        sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent next = new Intent(Login.this, CurrentGame.class);
                    startActivity(next);

                }


        });
    }
}
