package com.onlyapps.sample.utils;

import android.util.SparseArray;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by hyungsoklee on 2015. 6. 18..
 */
public class Utils {
    public static String getCommaWon(int wonValue) {
        DecimalFormat Commas = new DecimalFormat("#,###");
        String result = (String)Commas.format(wonValue);
        return result;
    }

    public static String getSampleValue(int position) {
        return Utils.getCommaWon((new Random().nextInt(10) + 1) * 1000 );
    }

    private static final Random mRandom = new Random();
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public static void initRatio() {
        sPositionHeightRatios.clear();
    }

    public static double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    public static double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0;
    }
}
