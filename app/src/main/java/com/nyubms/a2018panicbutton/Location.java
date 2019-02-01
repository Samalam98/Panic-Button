package com.nyubms.a2018panicbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

public class Location extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Switch location_switch = (Switch)findViewById(R.id.switch2);



        if (location_switch.isChecked()){
            Toast.makeText(getApplicationContext(), "Location turned on", Toast.LENGTH_SHORT).show();

        }

        else{
            Toast.makeText(getApplicationContext(), "Location turned off", Toast.LENGTH_SHORT).show();
        }



    }
}
