package com.onlyapps.sample.unigue;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by hyungsoklee on 2015. 8. 13..
 */
public class UnigueUtils {

    private static final String TAG = UnigueUtils.class.getSimpleName();

    /**
     * ANDROID_ID 또한 단점이 있는데, Proyo 2.2 이전 Version 에는 100% 디바이스 고유 번호를 획득 한다고는 보장 할 수 없으며,
     * 몇몇 Vendor 에서 출하된 디바이스에 동일한 고유 번호가 획득 된다는 버그가 report 됐다는 것이다.
     * @return
     */
    public static String getAndroidId(Context context) {
        String myAndroidDeviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "Settings.Secure.ANDROID_ID : " + myAndroidDeviceId);
        return myAndroidDeviceId;
    }

    /**
     * 안드로이드의 API Level 9 (진저브레드 2.3) 이후 부터 제공 하는 고유 번호 로서
     * TelephonyManager 클래스를 통한 획득 보다는 안전 하다고 할 수 있지만 2.3 미만의 Version 에서는 문제가 발생 할 수 가 있다.
     * API Level 9 부터 제공 하기 때문에 @SuppressLint("NewApi") 를 추가 해야 되기 때문에 아래와 같이 Java reflection을 통해 획득 하는 것이 좋다.
     * @return
     */
    public static String getDeviceSerialNumber() {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                Class<?> c = null;
                try {
                    c = Class.forName("android.os.SystemProperties");
                    Method get = c.getMethod("get", String.class);
                    return (String) get.invoke(c, "ro.serialno");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return (String) Build.class.getField("SERIAL").get(null);
            }
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String myAndroidDeviceId = mTelephony.getDeviceId();
            Log.d(TAG, "getDeviceId() : " + myAndroidDeviceId);
            return myAndroidDeviceId;
        } catch (SecurityException e) {
            return null;
        }
    }

    /**
     * 한 사용자에게 여러 기기를 허용하기 위해 기기별 id가 필요하다.
     * ANDROID_ID가 기기마다 다른 값을 준다고 보장할 수 없어, 보완된 로직이 포함되어 있다.
     * https://github.com/rangken/kakao-sdk/blob/master/src/main/java/com/kakao/PushToken.java
     */
    public static String getDeviceUUID(final Context context) {
        UUID uuid = null;
        // 먼저 가장 정확하다고 알려진 ANDROID_ID 를 가져옵니다.
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            // 예전 어떤 기기에서 특정 번호로만 나오던 버그
            if (!"9774d56d682e549c".equals(androidId)) {
                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
            } else {
                final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                // 둘 다 실패하면, 랜덤하게 UUID 를 발생시킵니다.
                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // 랜덤한 값이지만 preference 에 저장해두고 사용하기 때문에 삭제하고 재설치하지 않는 이상 랜덤한 넘버를 두번 만들지는 않습니다.

        return uuid.toString();
    }

    /**
     *
     * @return
     */
    public static String getMakeUnigueIdByBuild() {
        return  "35" +
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10 ; //13 digits
    }

    /**
     * Return pseudo unique ID
     *
     * http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id
     *
     * @return ID
     */
    public static String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" +
                (Build.BOARD.length() % 10) +
                (Build.BRAND.length() % 10) +
                (Build.CPU_ABI.length() % 10) +
                (Build.DEVICE.length() % 10) +
                (Build.MANUFACTURER.length() % 10) +
                (Build.MODEL.length() % 10) +
                (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     *
     * @param context
     * @return
     */
    public static String getAdvertisingId(Context context) {
        AdvertisingIdClient.Info adInfo = null;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
        if (adInfo != null) {
            return adInfo.getId();
        }
        return null;
    }

    /**
     *
     * @param context
     * @return
     */
    public static String getAccountName(Context context) {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        for (Account account : accounts) {
            if (account != null && !TextUtils.isEmpty(account.name)) {
                return account.name;
            }
        }
        return null;
    }

    public static String getNameUUIDFromBytes(Context context) {
        String accountName = getAccountName(context);
        try {
            return UUID.nameUUIDFromBytes(accountName.getBytes("utf8")).toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
        return null;
    }


}
