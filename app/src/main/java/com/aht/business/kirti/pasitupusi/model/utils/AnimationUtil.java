package com.aht.business.kirti.pasitupusi.model.utils;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aht.business.kirti.pasitupusi.R;

public class AnimationUtil {

    public static void slide_down(final Context ctx, final View view, final ImageView sourceClick) {

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
                sourceClick.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.arrow_up_float));
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

    public static void slide_up(final Context ctx, final View view, final ImageView sourceClick) {

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
                sourceClick.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.arrow_down_float));
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

    public static void shake_right_left(LinearLayout layout, final TextView view, final int repeatCount) {
        //view.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.shake_left_right));

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        TranslateAnimation animation = new TranslateAnimation(0.0f, layout.getMeasuredWidth() - view.getMeasuredWidth(), 0.0f, 0.0f); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)
        animation.setDuration(5000); // animation duration
        animation.setRepeatCount(repeatCount); // animation repeat count
        animation.setRepeatMode(Animation.REVERSE); // repeat animation (left to right, right to left)

        animation.setFillAfter(false);
        view.clearAnimation();
        view.startAnimation(animation);//your_view for mine is imageView
    }


}
