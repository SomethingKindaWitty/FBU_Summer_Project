package me.caelumterrae.fbunewsapp.fragments;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import me.caelumterrae.fbunewsapp.graphics.MyGLRenderer;


public class GraphicsFragment extends Fragment {


    private GLSurfaceView mGLView;

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
        mGLView = new MyGLSurfaceView(this.getActivity());
        return mGLView;
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
            mRenderer = new MyGLRenderer();
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
                    mRenderer.translate(dx* TOUCH_SCALE_FACTOR*0.002f, dy * TOUCH_SCALE_FACTOR*0.002f);
            }

            mPreviousX = x;
            mPreviousY = y;
            return true;
        }
    }

}


