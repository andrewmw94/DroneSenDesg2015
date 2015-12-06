/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import Utils.MotionTreeNode;
import Utils.Utils;

/**
 *
 * @author awells
 */
public class Planner {

    private final static boolean lawnMower = true;
    private final static boolean spiral = !lawnMower;

    Simulator sim;
    MotionTreeNode root;

    public Planner(Simulator simulator) {
        sim = simulator;
    }

    public void run() {
        searchForGoal();
        //planPathToGoal();
    }

    public boolean searchForGoal() {
        //move drone to corner
        boolean[][] coveredSquares = new boolean[sim.x_resolution][sim.y_resolution];
        float x_min = 0.0f;
        float x_max = 15.0f;
        float y_min = 0.0f;
        float y_max = 15.0f;
        float z_height = 4.0f;

        float rad = z_height * (float) Math.tan(Math.PI / 8);

        float sweepWidth = 2 * rad;

        System.out.println("SW = " + sweepWidth);

        if (lawnMower) {
            //move to corner
            DroneMovement.moveTo(new float[]{x_min, y_min + sweepWidth/2, z_height});
            float currY = y_min + sweepWidth/2;
            float currX = x_min + rad;
            boolean up = true;
            boolean right = true;

            while (currY < y_max - sweepWidth) {
                if (up) {
                    if(right){
                        DroneMovement.moveTo(new float[]{x_max, currY, z_height});
                    } else {
                        DroneMovement.moveTo(new float[]{x_min, currY, z_height});
                    }
                    right=!right;
                    for (int j=(int)Math.floor((currY-sweepWidth/2)*sim.y_resolution/(y_max-y_min));j<(currY+sweepWidth/2)*sim.y_resolution/(y_max-y_min);j++){
                        for(int i=0;i<sim.x_resolution;i++){
                            if(Utils.isSquareCovered(i*(x_max-x_min)/sim.x_resolution,(j)*(y_max-y_min)/sim.y_resolution,
                                    (i+1)*(x_max-x_min)/sim.x_resolution,(j+1)*(y_max-y_min)/sim.y_resolution,
                                    x_min+rad,currY,x_max-rad,currY,rad)){
                                coveredSquares[i][j]=true;
                            }
                        }
                    }
                } else {
                    currY += sweepWidth;
                    if(right){
                        DroneMovement.moveTo(new float[]{x_min, currY, z_height});
                        currX=x_min+rad;
                    } else {
                        DroneMovement.moveTo(new float[]{x_max, currY, z_height});
                        currX=x_max-rad;
                    }
                    for (int j=(int)Math.floor((currY-sweepWidth*3/2)*sim.y_resolution/(y_max-y_min));j<(currY+sweepWidth/2)*sim.y_resolution/(y_max-y_min);j++){
                        for(int i=(int)Math.floor((currX-sweepWidth/2)*sim.x_resolution/(x_max-x_min));i<(currX+sweepWidth/2)*sim.x_resolution/(x_max-x_min);i++){
                            if(Utils.isSquareCovered(i*(x_max-x_min)/sim.x_resolution,(j)*(y_max-y_min)/sim.y_resolution,
                                    (i+1)*(x_max-x_min)/sim.x_resolution,(j+1)*(y_max-y_min)/sim.y_resolution,
                                    currX,currY-sweepWidth,currX,currY,rad)){
                                coveredSquares[i][j]=true;
                            }
                        }
                    }
                }
                //add squares in circle
                
                up = !up;
            }
        } else if (spiral) {
            //move to center
            DroneMovement.moveTo(new float[]{(x_min + x_max) / 2, (y_min + y_max) / 2, z_height});
            float currY = (y_min + y_max) / 2;
            float currX = (x_min + x_max) / 2;
            boolean up = true;

            while (currY < y_max - sweepWidth) {

            }
        }

        return false;
    }

    public boolean planPathToGoal() {

        return false;
    }
}
