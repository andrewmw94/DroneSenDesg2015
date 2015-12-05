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
public class Utils {
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
    
    public static float distSquared(float x1,float y1,float x2,float y2){
        return (x2-x1)*(x2-x1)+(y2-y1)*(y2-y1);
    }
}
