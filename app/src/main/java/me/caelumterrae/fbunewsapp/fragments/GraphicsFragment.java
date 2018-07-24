package me.caelumterrae.fbunewsapp.fragments;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

        public MyGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(2);
            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(new MyGLRenderer());
        }
    }

}


