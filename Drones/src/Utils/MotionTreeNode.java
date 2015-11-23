/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.ArrayList;

/**
 *
 * @author awells
 */
public class MotionTreeNode {
    MotionTreeNode parent;
    ArrayList<MotionTreeNode> children;
    float[] coords;
    
    public MotionTreeNode(float x, float y, float z) {
        coords = new float[3];
        coords[0] = x;
        coords[1] = y;
        coords[2] = z;
    }
}
