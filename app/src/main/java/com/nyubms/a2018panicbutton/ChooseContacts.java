package com.nyubms.a2018panicbutton;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChooseContacts extends AppCompatActivity {
    ListView listView_Choose_Contacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contacts);

        listView_Choose_Contacts=(ListView)findViewById(R.id.listview_choose_contacts);

        if (getIntent().hasExtra("panic_button.chooseContact")){
            fp_get_Android_Contacts();

        }

    }

    public class Android_Contact {
        public String android_contact_Name = "";
        public String android_contact_TelefonNr = "";
        public int android_contact_ID=0;
    }

    public void fp_get_Android_Contacts() {
        final ArrayList<Android_Contact> preloaded_List = new ArrayList<Android_Contact>();
        Cursor cursor_Android_Contacts = null;
        ContentResolver contentResolver = getContentResolver();
        try {
            cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Error on contact", ex.getMessage());
        }

        if (cursor_Android_Contacts.getCount() > 0) {
            while (cursor_Android_Contacts.moveToNext()) {

                Android_Contact android_contact = new Android_Contact();
                String contact_id = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contact_display_name = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                android_contact.android_contact_Name = contact_display_name;
                //android_contact.android_contact_ID = Integer.parseInt(contact_id);
                int hasPhoneNumber = Integer.parseInt(cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contact_id}
                            , null);

                    while (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        android_contact.android_contact_TelefonNr = phoneNumber;

                    }
                    phoneCursor.close();
                }
                preloaded_List.add(android_contact);
            }
            Adapter_for_Android_Contacts adapter = new Adapter_for_Android_Contacts(this, preloaded_List);
            listView_Choose_Contacts.setAdapter(adapter);

        }
        listView_Choose_Contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Object obj1= listView_Choose_Contacts.getItemAtPosition(position);
                Toast.makeText(ChooseContacts.this, "Attempting to add " + preloaded_List.get(position).android_contact_Name+" to the list", Toast.LENGTH_LONG ).show();
                String name= preloaded_List.get(position).android_contact_Name;
                String phone_no= preloaded_List.get(position).android_contact_TelefonNr;
                Intent MainMenuIntent= new Intent(getApplicationContext(), MainActivity.class);

                MainMenuIntent.putExtra("Name", name);
                MainMenuIntent.putExtra("Number", phone_no);
                startActivity(MainMenuIntent);

                Log.d("tag","passing number "+ phone_no);
            }
        });

    }
}
