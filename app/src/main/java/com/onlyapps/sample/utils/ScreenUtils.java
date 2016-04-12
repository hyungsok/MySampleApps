package com.onlyapps.sample.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.onlyapps.sample.MainApplication;

public class ScreenUtils {
    /**
     * 실제 비트맵 크기를 단말 스크린 크기에 맞게 높이 크기 리턴함수 
     * @param bitmap
     * @return
     */
    public static int getScaleHeight(Bitmap bitmap) {
        return getScaleHeight(bitmap.getWidth(), bitmap.getHeight());
    }
    
    /**
     * 실제 width, height 단말 스크린 크기에 맞게 높이 크기 리턴함수 
     * @param width
     * @param height
     * @return
     */
    public static int getScaleHeight(int width, int height) {
        return ((height * getScreenWidth(MainApplication.getContext())) / width);
    }
    
    /**
     * 현재 디스플레이 화면에 비례한 DP단위를 픽셀 크기로 반환합니다.
     *
     * @param context
     * @param dip 픽셀
     * @return 변환된 값 (pixel)
     */
    public static int getPixelFromDip(Context context, float dip) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm);
    }
    
    /**
     * 현재 디스플레이 화면에 비례한 픽셀단위를 DIP단위로 반환합니다.
     *
     * @param context
     * @param px
     * @return 변환된 값 (dip)
     */
    public static int getDipFromPixel(Context context, int px) {
        if (context == null) {
            return 0;
        }
        float dns = context.getResources().getDisplayMetrics().density;
        int dip = (int) (px / dns);
        return dip;
    }
    
    /**
     * 인디게이터바 높이
     *
     * @param context
     * @return
     */
    public static int getStatusbarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        Rect CheckRect = new Rect();
        Window window = ((Activity) context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(CheckRect);
        int height = CheckRect.top;
        return height;
    }
    
    /**
     * 단말 가로 사이즈 
     * @return
     */
    public static int getScreenWidth(Context context) {
        int width;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width = display.getWidth();  // Deprecated
        }
        return width;
    }
    
    /**
     * 단말 세로 사이즈 
     * @return
     */
    public static int getScreenHeight(Context context) {
        int height;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
        } else {
            height = display.getHeight();  // Deprecated
        }
        return height;
    }

    /**
     * 단말 세로(클라이언트) 사이즈
     * @return
     */
    public static int getClientHeight() {
        return -1;
    }

    /**
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(View view) {
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredWidth(View view) {
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }

    /**
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    /**
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        if (context == null) {
            return false;
        }
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 비트맵 이미지 리사이징
     * @param bitmap 비트맵 이미지
     * @param rate 비율
     * @return 비트맵 이미지
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, float rate) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int newWidth = bitmapWidth;
        int newHeight = bitmapHeight;

        if (bitmapWidth > 0 && bitmapHeight > 0) {
            newWidth = (int) (bitmapWidth * rate);
            newHeight = (int) (bitmapHeight * rate);
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }
    // [get/set]===================================[END]==================================[get/set]
}
