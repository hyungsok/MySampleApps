package com.onlyapps.sample.unigue;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
                return Build.SERIAL;
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
                Build.BOARD.length()%10 +
                Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 +
                Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 +
                Build.HOST.length()%10 +
                Build.ID.length()%10 +
                Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 +
                Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 +
                Build.TYPE.length()%10 +
                Build.USER.length()%10 ;
                //13 digits
    }

    /**
     * Return pseudo unique ID
     *
     * http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id
     *
     * @return ID
     */
    public static String getUniquePsuedoID() {
        String devceIDShort = "35" +
                (Build.BOARD.length() % 10) +
                (Build.DEVICE.length() % 10) +
                (Build.MANUFACTURER.length() % 10) +
                (Build.MODEL.length() % 10) +
                (Build.PRODUCT.length() % 10);

        String serial = getDeviceSerialNumber();
        if (serial != null) {
            return new UUID(devceIDShort.hashCode(), serial.hashCode()).toString();
        } else {

            try {
                return UUID.nameUUIDFromBytes(devceIDShort.getBytes("utf8")).toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String getUnigueId() {
        StringBuilder sb = new StringBuilder();
        sb.append(Build.MODEL);
        sb.append(Build.DEVICE);
        sb.append(Build.MANUFACTURER);
        sb.append(getDeviceSerialNumber());
        String id = getHash(sb.toString());
        Log.d(TAG, "getUnigueId() : " + id.getBytes().length);
        return id;
    }


    /**
     *
     * @param context
     * @return
     */
    public static String getUnigueId(Context context) {
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        final String serial = getDeviceSerialNumber();
        String id = new UUID(androidId.hashCode(), serial.hashCode()).toString();
        Log.d(TAG, "getUnigueId(Context) : " + id.getBytes().length);
        return id;
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

    /**
     *
     * @param context
     * @return
     */
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

    public static String getHash(String stringToHash) {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] result = null;

        try {
            result = digest.digest(stringToHash.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();

        for (byte b : result) {
            sb.append(String.format("%02X", b));
        }

        String messageDigest = sb.toString();
        return messageDigest;
    }


}
