package com.aht.business.kirti.pasitupusi.model.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.aht.business.kirti.pasitupusi.R;

public class AnimationUtil {

    public static void slide_down(Context ctx, final View view, final TextView sourceClick) {

        /*if (view != null) {

            view.setVisibility(View.VISIBLE);

            TranslateAnimation animate = new TranslateAnimation(
                    Animation.ABSOLUTE,           // fromXType
                    0,                 // fromXDelta
                    Animation.ABSOLUTE,           // toXType
                    0,                   // toXDelta
                    Animation.ABSOLUTE,           // fromYType
                    0,           // fromYDelta
                    Animation.ABSOLUTE,           // toYType
                    view.getHeight());           // toYDelta
            animate.setDuration(500);
            //animate.setFillAfter(true);

            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    sourceClick.setText("---");
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            view.startAnimation(animate);
        }*/

        Animation animate = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);

        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sourceClick.setText("-");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        if(animate != null) {
            animate.reset();
            if (view != null) {
                view.setVisibility(View.VISIBLE);
                view.clearAnimation();
                view.startAnimation(animate);
            }
        }
    }

    public static void slide_up(Context ctx, final View view, final TextView sourceClick) {

        /*if (view != null) {

            TranslateAnimation animate = new TranslateAnimation(
                    Animation.ABSOLUTE,           // fromXType
                    0,                 // fromXDelta
                    Animation.ABSOLUTE,           // toXType
                    0,                   // toXDelta
                    Animation.RELATIVE_TO_SELF,           // fromYType
                    view.getHeight(),           // fromYDelta
                    Animation.RELATIVE_TO_SELF,           // toYType
                    0);           // toYDelta
            animate.setDuration(500);
            //animate.setFillAfter(true);

            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                    sourceClick.setText("+++");
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            view.startAnimation(animate);

        }*/

        Animation animate = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);

        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                sourceClick.setText("+");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        if(animate != null) {
            animate.reset();
            if (view != null) {
                view.clearAnimation();
                view.startAnimation(animate);
            }
        }
    }
}
