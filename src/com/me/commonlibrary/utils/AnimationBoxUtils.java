package com.me.commonlibrary.utils;

import com.me.commonlibrary.data.AppData;

import com.me.commonlibrary.R;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class AnimationBoxUtils {
    public static final int ANIMATION_DURATION=500;
    
    private static Animation mMakeInUpAnimation;

    private static Animation mMakeOutDownAnimation;

    private static Animation mMakeOutUpAnimation;

    private static Animation mFadeInAnimation;

    private static Animation mFadeOutAnimation;
    
    public static Animation getMakeInUpAnimation(AnimationListener listener)
    {
        if(mMakeInUpAnimation==null)
            mMakeInUpAnimation=AnimationUtils.loadAnimation(AppData.getContext(),R.anim.makein_up_anim);
        mMakeInUpAnimation.setAnimationListener(listener);
        return mMakeInUpAnimation;
    }
    public static Animation getMakeOutUpAnimation(AnimationListener listener) {
        if (mMakeOutUpAnimation == null) {
            mMakeOutUpAnimation = android.view.animation.AnimationUtils.loadAnimation(
                    AppData.getContext(),
                    R.anim.makeout_up_anim);
        }
        mMakeOutUpAnimation.setAnimationListener(listener);
        return mMakeOutUpAnimation;
    }

    public static Animation getMakeOutDownAnimation(AnimationListener listener) {
        if (mMakeOutDownAnimation == null) {
            mMakeOutDownAnimation = android.view.animation.AnimationUtils.loadAnimation(
                    AppData.getContext(),
                    R.anim.makeout_down_anim);
        }
        mMakeOutDownAnimation.setAnimationListener(listener);
        return mMakeOutDownAnimation;
    }

    public static Animation getFadeInAnimation(AnimationListener listener) {
        if (mFadeInAnimation == null) {
            mFadeInAnimation = new AlphaAnimation(0, 1);
        }
        mFadeInAnimation.setDuration(ANIMATION_DURATION);
        mFadeInAnimation.setAnimationListener(listener);
        return mFadeInAnimation;
    }

    public static Animation getFadeInAnimation(AnimationListener listener, long duration) {
        if (mFadeInAnimation == null) {
            mFadeInAnimation = new AlphaAnimation(0, 1);
        }
        mFadeInAnimation.setDuration(duration);
        mFadeInAnimation.setAnimationListener(listener);
        return mFadeInAnimation;
    }

    public static Animation getFadeOutAnimation(AnimationListener listener) {
        if (mFadeOutAnimation == null) {
            mFadeOutAnimation = new AlphaAnimation(1, 0);
        }
        mFadeOutAnimation.setDuration(ANIMATION_DURATION);
        mFadeOutAnimation.setAnimationListener(listener);
        return mFadeOutAnimation;
    }

    public static Animation getFadeOutAnimation(AnimationListener listener, long duration) {
        if (mFadeOutAnimation == null) {
            mFadeOutAnimation = new AlphaAnimation(1, 0);
        }
        mFadeOutAnimation.setDuration(duration);
        mFadeOutAnimation.setAnimationListener(listener);
        return mFadeOutAnimation;
    }
    
   
    
}

