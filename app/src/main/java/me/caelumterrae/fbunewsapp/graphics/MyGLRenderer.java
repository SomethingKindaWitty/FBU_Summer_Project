package me.caelumterrae.fbunewsapp.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.utility.Format;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private ArrayList<Hexagon> hexagons;
    private ArrayList<ArrayList<Hexagon>> hexagonMap;
    private HashMap<String, String> sourceBias;


    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mTranslationMatrix = new float[16];

    private float X_OFF = 0.5f;//0th row
    private float Y_OFF = 0.45f;//multiply by difference to offset the y
    private float ODD_X_OFF = 0.25f; //add this to all x offsets if they are odd.

    public MyGLRenderer() {
    }

    public MyGLRenderer(HashMap<String, String> sourceBias) {
        this.sourceBias = sourceBias;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        hexagons = new ArrayList<>();
        //x and then y
        hexagonMap = new ArrayList<ArrayList<Hexagon>>();



        for(int x = 0; x <=10; x++){
            ArrayList<Hexagon> row = new ArrayList<>();
            for(int y = 0; y <= 14;y++){
                Post post = new Post();
                post.setTitle(Integer.toString(x) + " " + Integer.toString(y));
                post.setUrl("https://www.reuters.com/article/us-facebook-fang/facebooks-disappointing-report-hits-rest-of-fang-idUSKBN1KF2X1");
                post.setImageUrl("https://s3.reutersmedia.net/resources/r/?m=02&d=20180725&t=2&i=1287042306&r=LYNXMPEE6O20K&w=1280");
                post.setBody("test");
                String bias = sourceBias.get(Format.trimUrl(post.getUrl()));
                post.setPoliticalBias(Format.biasToNum(bias));

                //TESTING
                int randomNum = ThreadLocalRandom.current().nextInt(0, 6);
                post.setPoliticalBias(randomNum*25);
                if(Math.abs(y) % 2==0){
                    //EVEN ROW
                    row.add(new Hexagon(0.5f,x*X_OFF - (5*X_OFF),y*Y_OFF - (7*Y_OFF), post));
                    hexagons.add(new Hexagon(0.5f,x*X_OFF - (5*X_OFF),y*Y_OFF - (7*Y_OFF), post));
                }else{
                    row.add(new Hexagon(0.5f,x*X_OFF + ODD_X_OFF- (5*X_OFF),y*Y_OFF- (7*Y_OFF), post));
                    hexagons.add(new Hexagon(0.5f,x*X_OFF + ODD_X_OFF- (5*X_OFF),y*Y_OFF- (7*Y_OFF), post));
                }
            }
            hexagonMap.add(row);
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

        Matrix.translateM(mTranslationMatrix, 0, -mDistanceX, mDistanceY, 0);

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0 ,mTranslationMatrix,0);

        // Draw shape

        for(int x = 0; x < hexagonMap.size(); x++){
            for(int y = 0; y < hexagonMap.get(0).size(); y++){
                hexagonMap.get(x).get(y).draw(scratch);
            }
        }


//        for (int i = 0; i < hexagons.size(); i++){
//            //hexagons.get(i).translate(mDistanceX, mDistanceY);
//            Log.i("abc", "translation");
//            hexagons.get(i).draw(scratch);
//        }
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
    public float mDistanceX;
    public float mDistanceY;
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
//        for (int i = 0; i < hexagons.size(); i++){
//            hexagons.get(i).translate(distanceX, distanceY);
//        }

        for(int x = 0; x < hexagonMap.size(); x++){
            for(int y = 0; y < hexagonMap.get(0).size(); y++){
                hexagonMap.get(x).get(y).translate(distanceX, distanceY);
            }
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

    public void openHexagon(float x, float y, Context context, int userID){
        for (int i = 0; i < hexagons.size(); i++){
            if(hexagons.get(i).inHexagon(x,y)){
                hexagons.get(i).open(context, userID);
            }
        }
    }

}
