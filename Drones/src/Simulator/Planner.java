/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import Utils.MotionTreeNode;

/**
 *
 * @author awells
 */
public class Planner {
    Simulator sim;
    MotionTreeNode root;
    public Planner(Simulator simulator) {
        sim = simulator;
    }
    
    public void run() {
        searchForGoal();
        planPathToGoal();
    }
    
    public boolean searchForGoal() {
        //move drone to corner
        boolean[][] coveredSquares = new boolean[sim.x_resolution][sim.y_resolution];
        float x_min = 0.0f;
        float x_max = 15.0f;
        float y_min = 0.0f;
        float y_max = 15.0f;
        float z_height = 4.0f;
        
        float rad = z_height * (float) Math.tan(Math.PI/8);
        
        float sweepWidth = 2*rad;
        
        System.out.println("SW = " + sweepWidth);
        
        //move to corner
        
        DroneMovement.moveTo(new float[] {x_min, y_min+sweepWidth, z_height});
        float currY = y_min+sweepWidth;
        boolean up = true;

        //Lawn mower
        while(currY < y_max-sweepWidth) {
            if(up) {
                DroneMovement.moveTo(new float[] {x_max, currY, z_height});
            } else {
                currY += sweepWidth;
                DroneMovement.moveTo(new float[] {x_max, currY, z_height});
                DroneMovement.moveTo(new float[] {x_min, currY, z_height});
            }
            up = !up;
        }
        
        
        
        return false;
    }
    
    public boolean planPathToGoal() {
        
        
        
        return false;
    }
}
