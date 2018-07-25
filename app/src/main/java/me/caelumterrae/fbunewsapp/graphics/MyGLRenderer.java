package me.caelumterrae.fbunewsapp.graphics;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Triangle mTriangle;
    private ArrayList<Hexagon> hexagons;


    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mTranslationMatrix = new float[16];

    private float X_OFF = 0.5f;//0th row
    private float Y_OFF = 0.45f;//multiply by difference to offset the y
    private float ODD_X_OFF = 0.25f; //add this to all x offsets if they are odd.


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mTriangle  = new Triangle();
        hexagons = new ArrayList<>();

        for(int x = -2; x <=2; x++){
            for(int y = -2; y <= 2;y++){
                if(y % 2==0){
                    //EVEN ROW
                    hexagons.add(new Hexagon(0.5f,x*X_OFF,y*Y_OFF));
                }else{
                    hexagons.add(new Hexagon(0.5f,x*X_OFF + ODD_X_OFF,y*Y_OFF));
                }
            }
        }
    }

    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //rotation transformation for the triangle(s)
        //long time = SystemClock.uptimeMillis() % 4000L;
//        float angle = 0.090f * ((int) time);
        //Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        //Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        // Translate the hexagons with a matrix
        Matrix.setIdentityM(mTranslationMatrix, 0);

        Matrix.translateM(mTranslationMatrix, 0, mDistanceX, mDistanceY, 0);

        Matrix.multiplyMM(scratch, 0, mTranslationMatrix, 0 ,mMVPMatrix,0);

        // Draw shape
        for (int i = 0; i < hexagons.size(); i++){
            //hexagons.get(i).translate(mDistanceX, mDistanceY);

            Log.i("abc", "translation");
            hexagons.get(i).draw(scratch);
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

    public volatile float mAngle;
    public volatile float mDistanceX;
    public volatile float mDistanceY;
    public volatile long time;

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    public void setMove(float distanceX, float distanceY) {
        mDistanceX = distanceX;
        mDistanceY = distanceY;
    }

    public void translate(float distanceX, float distanceY){
        for (int i = 0; i < hexagons.size(); i++){
            hexagons.get(i).translate(distanceX, distanceY);
        }
    }
    public float getX(){
        return mDistanceX;
    }

    public float getY(){
        return mDistanceY;
    }

    public void setTime(long time){
        this.time = time;
    }

    public long getTime(){
        return this.time;
    }

}
