/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import Drone.DroneState;
import Utils.Utils;
import java.util.ArrayList;

/**
 *
 * @author awells
 */
public class DroneMovement {
    
    public static final float stepSize = 0.1f;
    
    public static ArrayList<DroneState> moveTo(DroneState start, float[] p) {
        ArrayList<DroneState> path = new ArrayList<>();
        float xDiff = p[0] - start.x;
        float yDiff = p[1] - start.y;
        float zDiff = p[2] - start.z;
        
        float sum = (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
        
        //normalize to 1
        
        xDiff /= sum;
        yDiff /= sum;
        zDiff /= sum;
        
        xDiff*=stepSize;
        yDiff*=stepSize;
        zDiff*=stepSize;
        
        float x = start.x;
        float y = start.y;
        float z = start.z;
        
        while(true) {
            DroneState ds = new DroneState();
            ds.x = x;
            ds.y = y;
            ds.z = z;
            path.add(ds);
            
            if(Utils.distSquared(x, y, z, p[0], p[1], p[2]) < 0.1) {
                break;
            }
            
            x+=xDiff;
            y+=yDiff;
            z+=zDiff;
            
        }
        return path;
    }
}
