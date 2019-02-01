package com.nyubms.a2018panicbutton;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Adapter_for_Android_Contacts extends BaseAdapter {
    //----------------< Adapter_for_Android_Contacts() >----------------
//< Variables >
    Context mContext;
    List<ChooseContacts.Android_Contact> mList_Android_Contacts;
    //</ Variables >
    //< constructor with ListArray >
    public Adapter_for_Android_Contacts(Context mContext ,List<ChooseContacts.Android_Contact> mContact) {
        this.mContext = mContext;
        this.mList_Android_Contacts = mContact;
    }
    //</ constructor with ListArray >
    @Override
    public int getCount() {
        return mList_Android_Contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mList_Android_Contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //----< show items >----
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(mContext,R.layout.contactlist_items, null);
//< get controls >
        TextView textview_contact_Name= (TextView) view.findViewById(R.id.textview_android_contact_name);
        TextView textview_contact_TelefonNr= (TextView) view.findViewById(R.id.textview_android_contact_phoneNr);
//</ get controls >

//< show values >
        textview_contact_Name.setText(mList_Android_Contacts.get(position).android_contact_Name);
        textview_contact_TelefonNr.setText(mList_Android_Contacts.get(position).android_contact_TelefonNr);
//</ show values >


        view.setTag(mList_Android_Contacts.get(position).android_contact_Name);
        return view;
    }

}
