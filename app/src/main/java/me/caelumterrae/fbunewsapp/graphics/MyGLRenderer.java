package me.caelumterrae.fbunewsapp.graphics;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Triangle mTriangle;
    private ArrayList<Hexagon> hexagons;


    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mTriangle  = new Triangle();
        hexagons = new ArrayList<>();
        hexagons.add(new Hexagon(0.3f, 0f, 0f)); //center
        hexagons.add(new Hexagon(0.3f, 0.3f, 0f)); //centerleft
        hexagons.add(new Hexagon(0.3f, -0.3f, 0f)); //centerRight
        hexagons.add(new Hexagon(0.3f, 0.15f, .27f)); //topleft
        hexagons.add(new Hexagon(0.3f, -0.15f, 0.27f)); //topright
        hexagons.add(new Hexagon(0.3f, -0.15f, -0.27f)); //bottomright
        hexagons.add(new Hexagon(0.3f, 0.15f, -0.27f)); //bottomleft


    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Draw shape
        for (int i = 0; i < hexagons.size(); i++){
            hexagons.get(i).draw(mMVPMatrix);
        }
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width/height;

        Matrix.frustumM(mProjectionMatrix,0,-ratio,ratio,-1,1,3,7);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

}
