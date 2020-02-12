package com.aht.business.kirti.pasitupusi.model.utils;

import android.content.Context;
import android.util.TypedValue;

public class AndroidUtils {

    public static int dpToPixel(final Context context, int dps) {
        //final float scale = context.getResources().getDisplayMetrics().density;
        //int pixels = (int) (dps * scale + 0.5f);

        int pixels = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps,
                context.getResources().getDisplayMetrics()) + 0.5f);

        return pixels;
    }
}
