/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 *
 * @author awells
 */
public class OurUtils {
    // Utility function to easily create float buffers.
    public static FloatBuffer floatBuffer(float f1, float f2, float f3, float f4) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(4);
        fb.put(f1).put(f2).put(f3).put(f4).flip();
        return fb;
    }
    public static boolean isSquareInCircle(float squareX1,float squareY1,float squareX2,float squareY2,float circleCenterX,float circleCenterY,float circleRadius){
        float rs=circleRadius*circleRadius;
        if(distSquared(squareX1,squareY1,circleCenterX,circleCenterY)<=rs&&
                distSquared(squareX1,squareY2,circleCenterX,circleCenterY)<=rs&&
                distSquared(squareX2,squareY2,circleCenterX,circleCenterY)<=rs&&
                distSquared(squareX2,squareY1,circleCenterX,circleCenterY)<=rs){
            return true;
        } else {
            return false;
        }
    }
    public static boolean isSquareCovered(float sx1,float sy1,float sx2,float sy2,float cx1,float cy1,float cx2,float cy2,float r){
        if(isSquareInCircle(sx1,sy1,sx2,sy2,cx1,cy1,r)||isSquareInCircle(sx1,sy1,sx2,sy2,cx1,cy1,r)){
            return true;
        } else {
            return sx1>=cx1&&sx2<=cx2&&sy1>=cy1&&sy2<=cy2;
        }
    }
    public static float[] positionConvert(float theta,float dist,float x,float y){
        float[] f=new float[2];
        f[1]=dist*(float)Math.cos(theta)+x;
        f[2]=dist*(float)Math.sin(theta)+y;
        return f;
    }
    
    public static float distSquared(float x1,float y1,float x2,float y2){
        return (x2-x1)*(x2-x1)+(y2-y1)*(y2-y1);
    }
    
    public static float distSquared(float x1,float y1,float z1,float x2, float y2, float z2){
        return (x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)+(z2-z1)*(z2-z1);
    }
}
