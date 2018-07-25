package me.caelumterrae.fbunewsapp.fragments;

import android.content.Context;
import android.content.Intent;
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

import me.caelumterrae.fbunewsapp.activities.DetailsActivity;
import me.caelumterrae.fbunewsapp.graphics.MyGLRenderer;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.model.User;


public class GraphicsFragment extends Fragment {


    private GLSurfaceView mGLView;
    private int userID;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userID = getArguments().getInt("uid");
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
                    break;
                case MotionEvent.ACTION_DOWN:
                    mRenderer.setTime((new java.util.Date()).getTime());
                    break;
                case MotionEvent.ACTION_UP:
                    long dT = (new java.util.Date()).getTime() - mRenderer.getTime();

                    Log.i("TIME", Long.toString(dT));

                    if(dT < 100) {
                        Post post = new Post();
                        post.setUrl("https://www.reuters.com/article/us-facebook-fang/facebooks-disappointing-report-hits-rest-of-fang-idUSKBN1KF2X1");
                        post.setImageUrl("https://s3.reutersmedia.net/resources/r/?m=02&d=20180725&t=2&i=1287042306&r=LYNXMPEE6O20K&w=1280");
                        post.setTitle("test");
                        post.setBody("test");
                        Intent i = new Intent(getContext(), DetailsActivity.class);
                        i.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                        i.putExtra(User.class.getSimpleName(), userID);
                        getContext().startActivity(i);
                    }
                    break;

            }

            mPreviousX = x;
            mPreviousY = y;
            return true;
        }
    }

}


