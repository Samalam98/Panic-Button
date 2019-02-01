 package com.nyubms.a2018panicbutton;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

 public class MainActivity extends AppCompatActivity {

     TextView TextViewNum;
     ListView ListView_saved_contacts;
     final ArrayList<ChooseContacts.Android_Contact> saved_List = new ArrayList<ChooseContacts.Android_Contact>();
     public static ArrayList<String> contactList = new ArrayList<>();
     public static ArrayList<String> nameList = new ArrayList<>();
     public static final String DEFAULT_STRING = "I'm requesting help";
     private static final String permission_not_granted ="No permission!";
     private static final String  failure_permission= "failure_permission";
     private static final String TAG = "MainActivity";
     private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
     private static String msg;
     private static String emergencyNumber = "911";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView_saved_contacts =(ListView)findViewById(R.id.listview_saved_contacts);
        ImageButton ImgBtnChooseContact= (ImageButton)findViewById(R.id.imgbtn_contacts);
        ImageView ImgViewSettings =(ImageView)findViewById(R.id.ImgViewSettings);
        Button btnSendSMS= (Button)findViewById(R.id.btnSendSMS);

        ImgBtnChooseContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), ChooseContacts.class);
                startIntent.putExtra("panic_button.chooseContact", "Hello World!");
                //going to choose contact
                startActivity(startIntent);
            }
        });

        ImgViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), Settings.class );
                startIntent.putExtra("panic_button.Settings","Hello World");
                startActivity(startIntent);
            }
        });


        SharedPreferences sharedPreferences= getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        Log.d("tag", "MainActivity: Loading sharedPreferences data into sets");
        int size = sharedPreferences.getInt("size", 0);
        nameList = new ArrayList<>(size);
        contactList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            nameList.add(i, sharedPreferences.getString("name_" + i, null));
        }
        for (int i = 0; i < size; i++) {
            contactList.add(i, sharedPreferences.getString("number_" + i, null));
        }

        //Load stored message into the msg variable
        msg = sharedPreferences.getString("message", DEFAULT_STRING);

        //Load emergency number into the variable
        emergencyNumber = sharedPreferences.getString("emergency_phone", "911");


        if (getIntent().hasExtra("Name")){
            String name=getIntent().getStringExtra("Name");
            String number=getIntent().getStringExtra("Number");
            ChooseContacts contact_obj= new ChooseContacts();
            ChooseContacts.Android_Contact main_contact= contact_obj.new Android_Contact();

            if (!contactList.contains(number)){
                nameList.add(name);
                contactList.add(number);
                main_contact.android_contact_Name = name;
                main_contact.android_contact_TelefonNr = number;
                Log.d("tag","Number: "+number+" Name: "+name);
                saved_List.add(main_contact);

            }





            Adapter_for_Android_Contacts main_adapter = new Adapter_for_Android_Contacts(getApplicationContext(), saved_List);
            ListView_saved_contacts.setAdapter(main_adapter);

        }

        for (int i = 0; i < size; i++) {
            ChooseContacts temp_obj= new ChooseContacts();
            ChooseContacts.Android_Contact temp_contact= temp_obj.new Android_Contact();
            temp_contact.android_contact_Name=nameList.get(i);
            temp_contact.android_contact_TelefonNr=contactList.get(i);

            saved_List.add(temp_contact);
        }

        final Adapter_for_Android_Contacts main_adapter = new Adapter_for_Android_Contacts(getApplicationContext(), saved_List);
        ListView_saved_contacts.setAdapter(main_adapter);


        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0; i<contactList.size(); i++){
                    sendSMS(contactList.get(i));

                }
                Toast.makeText(getApplicationContext(), "Your emergency message has been sent", Toast.LENGTH_SHORT).show();
            }
        });

        checkForSmsPermission();
        ListView_saved_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {
                final String temp_name= saved_List.get(position).android_contact_Name;
                final String temp_phone_no= saved_List.get(position).android_contact_TelefonNr;

                showMessageOKCancel("Delete this contact?",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int l) {
                                //list.removeIf(s -> s.equals("acbd"))
                                //remove items associated with the contact from the lists
                                contactList.remove(temp_phone_no);
                                nameList.remove(temp_name);
                                saved_List.remove(position);

                                //remove the view from the layout the view is in
                                ListView_saved_contacts.removeViewInLayout(view);
                                //Notify the adapter about the change in the list
                                final Adapter_for_Android_Contacts main_adapter = new Adapter_for_Android_Contacts(getApplicationContext(), saved_List);
                                ListView_saved_contacts.setAdapter(main_adapter);


                                Toast.makeText(getApplicationContext(), "Contact deleted", Toast.LENGTH_SHORT).show();


                            }

                        });




            }
        });



    }

     private void checkForSmsPermission() {
         if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
             Log.d("log", permission_not_granted);
             // Permission not yet granted. Use requestPermissions().
             // MY_PERMISSIONS_REQUEST_SEND_SMS is an
             // app-defined int constant. The callback method gets the
             // result of the request.
             ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
         } else {
             // Permission already granted. Enable the message button.
             enableSmsButton();
         }
     }

     public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
         switch (requestCode) {
             case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                 if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS) && grantResults[0] ==
                         PackageManager.PERMISSION_GRANTED) {
                     // Permission was granted.
                 } else {
                     // Permission denied.
                     Log.d("log",failure_permission);
                     Toast.makeText(MainActivity.this, failure_permission,Toast.LENGTH_SHORT).show();
                     // Disable the message button.
                     disableSmsButton();
                 }
             }
         }
     }

     public void disableSmsButton(){
         Button btn = (Button) findViewById(R.id.btnSendSMS);
         btn.setEnabled(false);
     }

     public void enableSmsButton(){
         Button btn = (Button)findViewById(R.id.btnSendSMS);
         btn.setEnabled(true);
     }


    public void sendSMS(String temp_number){
        String scAddress = null;
        // Set pending intents to broadcast
        // when message sent and when delivered, or set to null.
        PendingIntent sentIntent = null, deliveryIntent = null;
        // Check for permission first.

        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(temp_number, scAddress, msg, sentIntent, deliveryIntent);

    }


     private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
         new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                 .setMessage(message)
                 .setPositiveButton("OK", okListener)
                 .setNegativeButton("Cancel", null)
                 .create()
                 .show();
     }

    protected void onResume(){


         super.onResume();
         //final TextView currentPhone = (TextView) findViewById(R.id.currentNumberDisplay);
         TextViewNum=(TextView)findViewById( R.id.txtViewTesting );
         SharedPreferences sharedPreferences= getSharedPreferences("Contacts", Context.MODE_PRIVATE);
         msg = sharedPreferences.getString("message", DEFAULT_STRING);
         emergencyNumber = sharedPreferences.getString("emergency_phone", "911");

         Adapter_for_Android_Contacts main_adapter = new Adapter_for_Android_Contacts(getApplicationContext(), saved_List);
         ListView_saved_contacts.setAdapter(main_adapter);







         //Toast.makeText(MainActivity.this, "Received", Toast.LENGTH_LONG ).show();
         //Shared preferences reference
         //SharedPreferences sharedPreferences = getSharedPreferences("Contacts", MODE_PRIVATE);
         //Get phone number from preferences, set the textView to it
         //phone = sharedPreferences.getString("emergency_phone", "911");
         //currentPhone.setText(phone);


     }

     protected void onPause(){
         super.onPause();
         //Make reference to a sharedPreferences file
         SharedPreferences sharedpreferences = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
         //Set up an editor
         SharedPreferences.Editor editor = sharedpreferences.edit();

         //Clear out preferences first before adding in new ones
         Log.d("tag", "onPause: Clearing old preferences");
         editor.clear();
         editor.commit();

         //save the data to the preferences
         Log.d("tag", "onPause: saving data to sharedPreferences");
         editor.putInt("size", nameList.size());


         for (int i = 0; i < nameList.size(); i++) {
             editor.putString("name_" + i, nameList.get(i));
             editor.putString("number_" + i, contactList.get(i));
         }

         editor.putString("message", msg);
         editor.commit();


     }






















 }

