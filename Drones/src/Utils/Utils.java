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
}
