package me.caelumterrae.fbunewsapp.fragments;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.graphics.MyGLRenderer;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.singleton.BiasHashMap;


public class GraphicsFragment extends Fragment {


    private GLSurfaceView mGLView;
    private User user;
    private int userID;
    TopNewsClient client;

    public GraphicsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        client = new TopNewsClient(getContext());
        mGLView = new MyGLSurfaceView(this.getActivity());
        return mGLView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            user = Parcels.unwrap(getArguments().getParcelable("User"));
            userID = user.getUid();
        } catch (NullPointerException e){
            user = null;
        }
    }

    class MyGLSurfaceView extends GLSurfaceView {
        private final float TOUCH_SCALE_FACTOR = 180.0f/320;
        private float mPreviousX;
        private float mPreviousY;

        private final MyGLRenderer mRenderer;

        public MyGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(2);
            // Set the Renderer for drawing on the GLSurfaceView
            mRenderer = new MyGLRenderer(BiasHashMap.getSourceBias());
            setRenderer(mRenderer);

            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:

                    float dx = x - mPreviousX;
                    float dy = y - mPreviousY;

    //                    // reverse direction of rotation above the mid-line
    //                    if (y > getHeight() / 2) {
    //                        dx = dx * -1 ;
    //                    }
    //
    //                    // reverse direction of rotation to left of the mid-line
    //                    if (x < getWidth() / 2) {
    //                        dy = dy * -1 ;
    //                    }
    //                    mRenderer.setAngle(
    //                            mRenderer.getAngle() +
    //                                    ((dx + dy) * TOUCH_SCALE_FACTOR));

                    mRenderer.setMove( mRenderer.getX() + dx* TOUCH_SCALE_FACTOR*0.002f, mRenderer.getY() - dy * TOUCH_SCALE_FACTOR*0.002f);
                    requestRender();
                    mRenderer.translate(dx* TOUCH_SCALE_FACTOR*0.002f, -dy * TOUCH_SCALE_FACTOR*0.002f);
                    break;
                case MotionEvent.ACTION_DOWN:
                    mRenderer.setTime((new java.util.Date()).getTime());
                    break;
                case MotionEvent.ACTION_UP:
                    long dT = (new java.util.Date()).getTime() - mRenderer.getTime();

                    Log.i("TIME", Long.toString(dT));

                    if(dT < 100) {
                        Log.i("Touchevent", "touch event at " + (x * TOUCH_SCALE_FACTOR*0.002f) + " " + (y * TOUCH_SCALE_FACTOR*0.002f));
                        mRenderer.openHexagon(x * TOUCH_SCALE_FACTOR*0.002f - 0.59f, y * TOUCH_SCALE_FACTOR*0.002f-0.99f, getContext(), userID);
                    }
                    break;

            }

            mPreviousX = x;
            mPreviousY = y;
            return true;
        }
    }

}


