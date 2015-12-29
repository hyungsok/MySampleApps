/*  * ______________________________________________________________________Copyright 2015 wemakeprice
  * Description : 디바이스 고유 아이디 생성 클래스
  * Date : 2015. 8. 20
  * Author : hyungsok lee
  * History :
  * _________________________________________________________________________________________________  */
package com.onlyapps.sample.unigue;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.onlyapps.sample.MainApplication;

import java.lang.reflect.Method;
import java.util.Random;


public class DeviceID {
    // [final/static_property]====================[START]===================[final/static_property]
    /** LOG TAG */
    private final static String TAG = DeviceID.class.getSimpleName();

    private final static String PREFS_PRIVATE = "PREFS_PRIVATE_DEVICEID";
    private final static String DEVICE_ID = "DeviceID";
    /**
     * Bugs: We have seen a few instances of production phones for which the implementation is buggy and returns garbage, for example zeros or asterisks.
     */
    private static final String BUGGY_ANDROID_ID = "9774d56d682e549c";
    // [final/static_property]=====================[END]====================[final/static_property]
    // [private/protected/public_property]========[START]=======[private/protected/public_property]
    private static String ID = null;
    // [private/protected/public_property]=========[END]========[private/protected/public_property]
    // [interface/enum/inner_class]===============[START]==============[interface/enum/inner_class]
    // [interface/enum/inner_class]================[END]===============[interface/enum/inner_class]
    // [inherited/listener_method]================[START]===============[inherited/listener_method]
    // [inherited/listener_method]=================[END]================[inherited/listener_method]
    // [private_method]===========================[START]==========================[private_method]
    /**
     * get generate ID
     *
     * 1. device Id
     * 2. android Id
     * 3. serial Number
     * 4. random Id
     *
     * @return
     */
    private static String generateID() {
        final Context context = MainApplication.getContext();

        String deviceId = getDeviceId(context);
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getAndroidId(context);
        }
        // in case known problems are occured
        if (BUGGY_ANDROID_ID.equals(deviceId) || deviceId == null) {

            if (TextUtils.isEmpty(deviceId)) {
                deviceId = getSerialNumber();
            }
            // if nothing else works, generate a random number
            if (TextUtils.isEmpty(deviceId)) {
                Random tmpRand = new Random();
                deviceId = String.valueOf(tmpRand.nextLong());
            }
        }

        return deviceId;
    }

    /**
     * get a unique deviceID like IMEI for GSM or ESN for CDMA phones
     *      <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     *
     * @param context
     * @return
     */
    private static String getDeviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            Log.d(TAG, "getDeviceId() : " + deviceId);
            return deviceId;
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceId() : " + e);
            return null;
        }
    }

    /**
     * get Android Id
     *
     * @param context
     * @return
     */
    private static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "getAndroidId() : " + androidId);
        return androidId;
    }

    /**
     * Android SERIAL Number
     *
     * @return
     */
    private static String getSerialNumber() {
        try {
            String serialNumber;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                Class<?> c = null;
                try {
                    c = Class.forName("android.os.SystemProperties");
                    Method get = c.getMethod("get", String.class);
                    serialNumber = (String) get.invoke(c, "ro.serialno");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                serialNumber = (String) Build.class.getField("SERIAL").get(null);
            }
            Log.d(TAG, "getSerialNumber() : " + serialNumber);
            return serialNumber;
        } catch (Exception e) {
            Log.e(TAG, "getSerialNumber() : " + e);
            return null;
        }
    }

    /**
     * set ID
     *
     * @param value
     */
    private static void setId(String value) {
        MainApplication.getContext().getSharedPreferences(PREFS_PRIVATE, 0).edit().putString(DEVICE_ID, value).commit();
    }

    /**
     * get ID
     *
     * @return
     */
    private static String getId() {
        return MainApplication.getContext().getSharedPreferences(PREFS_PRIVATE, 0).getString(DEVICE_ID, null);
    }

    // [private_method]============================[END]===========================[private_method]
    // [life_cycle_method]========================[START]=======================[life_cycle_method]
    // [life_cycle_method]=========================[END]========================[life_cycle_method]
    // [public_method]============================[START]===========================[public_method]

    /**
     * get ID
     *
     * @return
     */
    public static String getID() {
        if (ID == null) {
            ID = getId();
        }

        if (ID == null) {
            ID = generateID();

            if (ID != null) {
                setId(ID);
            }
        }
        Log.d(TAG, "getID() : " + ID);

        return ID;
    }
    // [public_method]=============================[END]============================[public_method]
    // [get/set]==================================[START]=================================[get/set]
    // [get/set]===================================[END]==================================[get/set]
}
