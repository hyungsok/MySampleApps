package com.onlyapps.sample;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.onlyapps.sample.unigue.DeviceID;
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
        adapter.add("ANDROID_ID : " + UnigueUtils.getAndroidId(this));
        adapter.add("IMEI : " + UnigueUtils.getDeviceId(this));
        adapter.add("Build.SERIAL : " + UnigueUtils.getDeviceSerialNumber());
        adapter.add("Build.DEVICE : " + Build.DEVICE);
        adapter.add("Build.MODEL : " + Build.MODEL);
        adapter.add("Build.ID : " + Build.ID);
        adapter.add("UniquePsuedoID : " + UnigueUtils.getUniquePsuedoID());
        adapter.add("getMakeUnigueIdByBuild : " + UnigueUtils.getMakeUnigueIdByBuild());
        adapter.add("getDeviceUUID : " + UnigueUtils.getDeviceUUID(this));
        adapter.add("Installation.id() : " + Installation.id(this));
        adapter.add("UUID.randomUUID() : " + UUID.randomUUID().toString());
        adapter.add("DeviceIdentifier.getDeviceIdentifier() : " + DeviceIdentifier.getId(this));
        adapter.add("AccountManager Name : " + UnigueUtils.getAccountName(this));
        adapter.add("AccountNameUUIDFromBytes : " + UnigueUtils.getNameUUIDFromBytes(this));
        adapter.add("getUnigueId(Context) : " + UnigueUtils.getUnigueId(this));
        adapter.add("getUnigueId : " + UnigueUtils.getUnigueId());
        adapter.add("DeviceID.getID : " + DeviceID.getID());
        listView.setAdapter(adapter);
        setContentView(listView);
    }


}
