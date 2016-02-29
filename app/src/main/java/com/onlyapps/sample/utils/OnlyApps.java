package com.onlyapps.sample.utils;

import android.content.UriMatcher;
import android.net.Uri;
import android.util.Log;

/**
 * Created by hyungsoklee on 2016. 2. 19..
 * - 스키마 처리 수정
 */
public class OnlyApps {
    private static final String TAG = OnlyApps.class.getSimpleName();

    private static UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int DO_COMMAND = 0;

    static {
        sMatcher.addURI("doCommand", null, DO_COMMAND);
    }

    public static void check(String uri, int expected) {
        int result = sMatcher.match(Uri.parse(uri));
        Log.d(TAG, "check() : " + result);
        if (result != expected) {
            String msg = "failed on " + uri;
            msg += " expected " + expected + " got " + result;
            throw new RuntimeException(msg);
        }
    }

    public static void testContentUris() {
        try {

            String url = "wemakeprice://doCommand?cmd_link_type=5&cmd_link=225555";

            check(url, DO_COMMAND);

            Uri uri = Uri.parse(url);
            Log.d(TAG, "getPath : " + uri.getPath());
            Log.d(TAG, "getAuthority : " + uri.getAuthority());
            Log.d(TAG, "getFragment : " + uri.getFragment());
            Log.d(TAG, "getHost : " + uri.getHost());
            Log.d(TAG, "getQuery : " + uri.getQuery());
            Log.d(TAG, "getSchemeSpecificPart : " + uri.getSchemeSpecificPart());
            Log.d(TAG, "getQueryParameter(cmd_link_type) : " + uri.getQueryParameter("cmd_link_type"));
            Log.d(TAG, "getQueryParameter(cmd_link) : " + uri.getQueryParameter("cmd_link"));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
