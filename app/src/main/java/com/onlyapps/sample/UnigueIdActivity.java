package com.onlyapps.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.onlyapps.sample.unigue.DeviceIdentifier;
import com.onlyapps.sample.unigue.Installation;
import com.onlyapps.sample.unigue.UnigueUtils;

import java.util.UUID;

public class UnigueIdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter.add("Settings.Secure.ANDROID_ID : " + UnigueUtils.getAndroidId(this));
        adapter.add("TelephonyManager.getDeviceId() : " + UnigueUtils.getDeviceId(this));
        adapter.add("DeviceSerialNumber : " +UnigueUtils.getDeviceSerialNumber());
        adapter.add("UniquePsuedoID : " + UnigueUtils.getUniquePsuedoID());
        adapter.add("UnigueUtils.getMakeUnigueIdByBuild : " + UnigueUtils.getMakeUnigueIdByBuild());
        adapter.add("Kakao getDeviceUUID : " + UnigueUtils.getDeviceUUID(this));
        adapter.add("Installation.id() : " + Installation.id(this));
        adapter.add("UUID.randomUUID() : "+UUID.randomUUID().toString());
        adapter.add("DeviceIdentifier.getDeviceIdentifier() : " + DeviceIdentifier.getId(this));
        adapter.add("AdvertisingIdClient.getAdvertisingIdInfo() : " + UnigueUtils.getAdvertisingId(this));
        adapter.add("AccountManager Name : " + UnigueUtils.getAccountName(this));
        adapter.add("AccountNameUUIDFromBytes : " + UnigueUtils.getNameUUIDFromBytes(this));
        listView.setAdapter(adapter);
        setContentView(listView);
    }
}
