package me.caelumterrae.fbunewsapp.graphics;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLES20;
import android.util.Log;

import org.parceler.Parcels;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import me.caelumterrae.fbunewsapp.activities.DetailsActivity;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.model.User;

public class Hexagon {

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private final int mProgram;

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private int mMVPMatrixHandle;


    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    private float pentagonCoords[] = {
            0.0f,0.5f, 0.0f, // 0
            -0.43301f,0.25f, 0.0f, // 1
            -0.43301f,-0.25f, 0.0f, // 2
            0.0f,0.5f, 0.0f, // 0
            -0.43301f,-0.25f, 0.0f, // 2
            0.0f,-0.5f, 0.0f, // 3
            0.0f,0.5f, 0.0f, // 0
            0.0f,-0.5f, 0.0f, // 3
            0.43301f,-0.25f, 0.0f, // 4
            0.0f,0.5f, 0.0f, // 0
            0.43301f,-0.25f, 0.0f, // 4
            0.43301f,0.25f, 0.0f // 5
    };

    private float origin[] = {0.f,0.f};
    private float radius;

    private final float[] modelMatrix = new float[16];
    private final float[] translateMatrix = new float[16];

    private short drawOrder[] = {0,1,2,3,4,5};

    // Set color with red, green, blue and alpha (opacity) values
    static float white[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    static float left[] = {239.f/255.f, 183.f/255.f, 66.f/255.f, 1.0f};
    static float leftcenter[] = {245.f/255.f, 210.f/255.f, 146.f/255.f, 1.0f};
    static float center[] = {252.f/255.f, 238.f/255.f, 227.f/255.f, 1.0f};
    static float rightcenter[] = {162.f/255.f, 195.f/255.f, 222.f/255.f, 1.0f};
    static float right[] = {73.f/255.f, 153.f/255.f, 218.f/255.f, 1.0f};
    static float altColor[] = {1.0f,0.0f,0.0f, 1.0f};

    private Post post;

    public Hexagon(float radius, float offsetX, float offsetY, Post post) {
        this.post = post;
        this.radius = radius/2;

        for (int i = 0; i < pentagonCoords.length; i++){
            pentagonCoords[i] *= radius;
        }
        for (int i = 0; i < pentagonCoords.length; i+=3){
            pentagonCoords[i] += offsetX;
            pentagonCoords[i+1] += offsetY;
        }
        origin[0] = offsetX;
        origin[1] = offsetY;

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                pentagonCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(pentagonCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);

        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = pentagonCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the hexagon depending on location
//        if(offScreen()){
//            GLES20.glUniform4fv(mColorHandle, 1, altColor, 0);
//        }else{
//            GLES20.glUniform4fv(mColorHandle, 1, white, 0);
//        }
        // Set color for drawing the hexagon depending on politicalAffiliation

        int bias = post.getPoliticalBias();
        switch(bias){
            case 100:
                GLES20.glUniform4fv(mColorHandle, 1, right, 0);
                break;
            case 75:
                GLES20.glUniform4fv(mColorHandle, 1, rightcenter, 0);
                break;
            case 50:
                GLES20.glUniform4fv(mColorHandle, 1, center, 0);
                break;
            case 25:
                GLES20.glUniform4fv(mColorHandle, 1, leftcenter, 0);
                break;
            case 0:
                GLES20.glUniform4fv(mColorHandle, 1, left, 0);
                break;
            default:
                GLES20.glUniform4fv(mColorHandle, 1, center, 0);
                break;
        }


        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void translate(float x, float y){
        origin[0] -= x;
        origin[1] += y;
    }

    public boolean offScreen(){
        return origin[0] < -0.75 || origin [0] > 0.75f || -1 * origin[1] > 1.0f || -1 * origin[1] < -1.0f;
    }

    public boolean inHexagon(float x, float y){
        float xDistance = Math.abs((-1 * origin[0]) - x);
        float yDistance = Math.abs((-1*origin[1]) - y);
        double originDistance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
        if(originDistance < radius) {
            Log.i("Touchevent", "Touch registered in origin x:" + origin[0] + "  y:" + origin[1]);
            Log.i("Touchevent", "with distance " + originDistance);
        }
        return originDistance < radius;
    }

    public void open(Context context, int userId){
        Intent i = new Intent(context, DetailsActivity.class);
        i.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
        i.putExtra("source","Login");
        i.putExtra(User.class.getSimpleName(), userId);
        context.startActivity(i);
    }

    public void logOrigin(){
        Log.i("Touchevent", "Origin at x:" + origin[0] + "  y:" + origin[1]);
    }

    public int getColor(){
        // TODO: convert a political affiliation to the proper color.
        return 0;
    }
}
