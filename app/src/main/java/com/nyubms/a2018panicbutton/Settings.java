package com.nyubms.a2018panicbutton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView imageView_messgaes = (ImageView)findViewById(R.id.imageView_message);
        ImageView imageView_location = (ImageView)findViewById(R.id.imageView_location);
        ImageView imageView_info = (ImageView)findViewById(R.id.imageView_info);
        ImageView imageView_number = (ImageView)findViewById(R.id.imageView_number);


        imageView_messgaes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent msgIntent= new Intent(getApplicationContext(), Preset_messages.class);
                msgIntent.putExtra("messages","hi");
                startActivity(msgIntent);
            }
        });


        imageView_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent= new Intent(getApplicationContext(), Information.class);
                startActivity(infoIntent);
            }
        });

        imageView_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent numberIntent = new Intent(getApplicationContext(), Emergency_number.class);
                startActivity(numberIntent);
            }
        });

        imageView_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationIntent = new Intent(getApplicationContext(), Location.class);
                startActivity(locationIntent);
            }
        });


        if (getIntent().hasExtra("panic_button.Settings")){


        }
    }
}
