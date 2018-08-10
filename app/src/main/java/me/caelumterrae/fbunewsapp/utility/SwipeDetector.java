package me.caelumterrae.fbunewsapp.utility;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SwipeDetector implements View.OnTouchListener {

    private Activity activity;
    static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;

    public SwipeDetector(final Activity activity) {
        this.activity = activity;
    }

    public final void onRightToLeftSwipe() {
        Log.e("Swipe Detector","RightToLeftSwipe!");
    }

    public void onLeftToRightSwipe(){
        Log.e( "Swipe Detector","LeftToRightSwipe!");
    }

    public void onTopToBottomSwipe(){
        Log.e( "Swipe Detector","onTopToBottomSwipe!");
    }

    public void onBottomToTopSwipe(){
        Log.e( "Swipe Detector","onBottomToTopSwipe!");
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) >= MIN_DISTANCE){
                    // left or right
                    if(deltaX < 0) {
                        this.onLeftToRightSwipe();
                        return true;
                    }
                    if(deltaX > 0) {
                        this.onRightToLeftSwipe();
                        return true;
                    }
                } else {
                    Log.e( "Swipe Detector", "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                    return true;
                }

                // swipe vertical?
                if(Math.abs(deltaY) >= MIN_DISTANCE){
                    // top or down
                    if(deltaY < 0) {
                        this.onTopToBottomSwipe();
                        return true;
                    }
                    if(deltaY > 0) {
                        this.onBottomToTopSwipe();
                        return true;
                    }
                } else {
                    Log.e( "Swipe Detector","Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                    return true;
                }
            }
        }
        return false;
    }
}