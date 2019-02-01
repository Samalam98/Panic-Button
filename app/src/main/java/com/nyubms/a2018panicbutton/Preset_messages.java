package com.nyubms.a2018panicbutton;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Preset_messages extends AppCompatActivity {
    private static final String DEFAULT_MSG = "I am requesting help";
    private static final String TAG = "ChangeMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset_messages);

        final Switch message_on_off = (Switch)findViewById(R.id.switch1);
        Button btnSave= (Button)findViewById(R.id.btn_save);
        message_on_off.setChecked(false);

        SharedPreferences sharedPreferences = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        Log.d(TAG, "onCreate: assigning views");
        TextView oldMessage = (TextView) findViewById(R.id.textView_old);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message_on_off.isChecked()){
                    changeMessage();


                    Toast.makeText(getApplicationContext(), "Your message has been saved!", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(Preset_messages.this, "Please turn on custom message setting first", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    protected void onPause(){
        super.onPause();

        TextView oldMessage = (TextView) findViewById(R.id.textView_old);


        Log.d(TAG, "onPause: making reference to SharedPreferences");
        SharedPreferences sharedPreferences = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        Log.d(TAG, "onPause: making editor");
        SharedPreferences.Editor editor = sharedPreferences.edit();


        Log.d(TAG, "onPause: storing message into SharedPreferences");
        editor.putString("message", oldMessage.getText().toString());
        editor.commit();


    }
    //function for changing message
    public void changeMessage(){


        Log.d(TAG, "changeMessage: starting changeMessage");

        Log.d(TAG, "changeMessage: assigning views");
        EditText newMessage = (EditText) findViewById(R.id.textView_new);
        TextView oldMessage = (TextView) findViewById(R.id.textView_old);

        Log.d(TAG, "changeMessage: setting message to the user-input message");

        String message = newMessage.getText().toString();

        if (message.trim().isEmpty()){
            Log.d(TAG, "changeMessage: message was empty, setting back to default");
            Toast.makeText(this, "Message cannot be empty",Toast.LENGTH_SHORT).show();
        } else {
            oldMessage.setText(message);
        }

    }
}
