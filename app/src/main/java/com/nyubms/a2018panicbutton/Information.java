package com.nyubms.a2018panicbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);


        TextView txtView = (TextView)findViewById(R.id.textView_info);

        txtView.setText("Welcome to the Panic Button App! \n On the starting menu: \n Click 'Quick Send!' to send messages to your saved contacts \n " +
                "Press the contact icon to select and add new contacts \n Press settings icon to customize your personal settings \n\n On the settins screen: " +
                "Press any icon to customize your settings" );
    }
}
