/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Drone.DroneState;
import java.util.ArrayList;

/**
 *
 * @author awells
 */
public class MotionTreeNode {
    MotionTreeNode parent;
    ArrayList<MotionTreeNode> children;
    DroneState ds;
    
    public MotionTreeNode(float x, float y, float z) {
        ds = new DroneState();
        ds.x = x;
        ds.y = y;
        ds.z = z;
    }
}
