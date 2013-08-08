package com.me.commonlibrary.utils;

import android.os.Handler;
import android.os.Message;

public class FrameAnimationController {
    
        private static final int MSG_ANIMATE=1000;
        public static final int ANIMATION_FRAME_DURATION = 1000 / 60;
        private static final Handler mHandler=new AnimationHandler();
        public static void requestAnimationFrame(int messageWhat,Runnable runnable)
        {
            Message message=new Message();
            message.what=messageWhat;
            message.obj=runnable;
            mHandler.sendMessageDelayed(message,ANIMATION_FRAME_DURATION);
        }
        public static void requestAnimationFrame(Runnable runnable) {
            requestAnimationFrame(MSG_ANIMATE, runnable);
        }
        public static void removeMessage(int messageWhat)
        {
            mHandler.removeMessages(messageWhat);
        }
        public static void requestFrameDelay(Runnable runnable, long delay) {
            Message message = new Message();
            message.what = MSG_ANIMATE;
            message.obj = runnable;
            mHandler.sendMessageDelayed(message, delay);
        }
        public static class AnimationHandler extends Handler
        {
            public void handleMessage(Message m)
            {
                if(m.obj!=null)
                {
                    ((Runnable)m.obj).run();
                }
            }
        }
    }

