package com.fakhrirasyids.heartratemonitor.ui.customview.view;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.fakhrirasyids.heartratemonitor.R;

public class HeartBeatView extends AppCompatImageView {
    private static final float DEFAULT_SCALE_FACTOR = 0.2f;
    private static final int DEFAULT_DURATION = 50;

    private boolean heartBeating = false;

    float scaleFactor = DEFAULT_SCALE_FACTOR;
    float reductionScaleFactor = -scaleFactor;
    int duration = DEFAULT_DURATION;

    public HeartBeatView(Context context) {
        super(context);
        init();
    }

    public HeartBeatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        populateFromAttributes(context, attrs);
        init();
    }

    public HeartBeatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        populateFromAttributes(context, attrs);
        init();
    }

    private void init() {
        Drawable heartDrawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_circle);
        setImageDrawable(heartDrawable);
    }

    private void populateFromAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HeartBeatView,
                0, 0
        );

        try {
            scaleFactor = a.getFloat(R.styleable.HeartBeatView_scaleFactor, DEFAULT_SCALE_FACTOR);
            reductionScaleFactor = -scaleFactor;
            duration = a.getInteger(R.styleable.HeartBeatView_duration, DEFAULT_DURATION);
        } finally {
            a.recycle();
        }
    }

    public void start() {
        heartBeating = true;
        animate().scaleXBy(scaleFactor).scaleYBy(scaleFactor).setDuration(duration).setListener(scaleUpListener);
    }

    public void stop() {
        heartBeating = false;
        clearAnimation();
    }

    private static final int milliInMinute = 60000;

    public void setDurationBasedOnBPM(int bpm) {
        if (bpm > 0) {
            duration = Math.round(((float) milliInMinute / bpm) / 3f);
        }
    }

    private final Animator.AnimatorListener scaleUpListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(@NonNull Animator animation) {
        }

        @Override
        public void onAnimationRepeat(@NonNull Animator animation) {

        }

        @Override
        public void onAnimationEnd(@NonNull Animator animation) {
            animate().scaleXBy(reductionScaleFactor).scaleYBy(reductionScaleFactor).setDuration(duration).setListener(scaleDownListener);
        }

        @Override
        public void onAnimationCancel(@NonNull Animator animation) {

        }
    };


    private final Animator.AnimatorListener scaleDownListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(@NonNull Animator animation) {
        }

        @Override
        public void onAnimationRepeat(@NonNull Animator animation) {
        }

        @Override
        public void onAnimationEnd(@NonNull Animator animation) {
            if (heartBeating) {
                animate().scaleXBy(scaleFactor).scaleYBy(scaleFactor).setDuration(duration * 2L).setListener(scaleUpListener);
            }
        }

        @Override
        public void onAnimationCancel(@NonNull Animator animation) {
        }
    };

}