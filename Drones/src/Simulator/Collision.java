/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import Drone.Drone;
import Utils.Mesh;
import java.util.ArrayList;

/**
 *
 * @author awells
 */
public class Collision {
    Drone d;
    Mesh b;
    ArrayList<Mesh> obs;
    public Collision(Drone drone, Mesh bounds, ArrayList<Mesh> obstacles) {
        d = drone;
        b = bounds;
        obs = obstacles;
    }
    
    public boolean fastIsInCollision() {
        if(d.ourState.x > b.maxCorner[0] || d.ourState.x < b.minCorner[0])
            return true;
        if(d.ourState.y > b.maxCorner[1] || d.ourState.y < b.minCorner[1])
            return true;
        if(d.ourState.z > b.maxCorner[2] || d.ourState.z < b.minCorner[2])
            return true;
        
        for(Mesh m : obs) {
            if(isCollided(d.mesh, m))
                return true;
        }
        return false;
    }
    
    private boolean isCollided(Mesh a, Mesh b) {
        
        //Is a.minCorner inside b?
        boolean overlap = true;
        for(int i=0; i < 3; i++) {
            if(a.minCorner[i] > b.maxCorner[i] || a.minCorner[i] < b.minCorner[i]) {
                overlap=false;
                break;
            }
        }
        if(overlap)
            return true;
        
        //Is a.maxCorner inside b?
        overlap = true;
        for(int i=0; i < 3; i++) {
            if(a.maxCorner[i] > b.maxCorner[i] || a.maxCorner[i] < b.minCorner[i]) {
                overlap=false;
                break;
            }
        }
        if(overlap)
            return true;
        
        //Is b.minCorner inside a?
        overlap = true;
        for(int i=0; i < 3; i++) {
            if(a.maxCorner[i] < b.minCorner[i] || a.minCorner[i] > b.minCorner[i]) {
                overlap=false;
                break;
            }
        }
        if(overlap)
            return true;
        
        //Is b.maxCorner inside a?
        overlap = true;
        for(int i=0; i < 3; i++) {
            if(a.maxCorner[i] < b.maxCorner[i] || a.minCorner[i] > b.maxCorner[i]) {
                overlap=false;
                break;
            }
        }
        if(overlap)
            return true;

        return false;
    }
}
