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
            DroneMovement.moveTo(new float[]{x_min+rad,y_min+rad,z_height});
            float currX = x_min+rad;
            float currY = y_min+rad;
            int dir = 0;
            float dist=0;
            while(dist<(x_min+x_max)/2){
                if(dir==0){
                    DroneMovement.moveTo(new float[]{x_max-rad-dist,y_min+rad+dist,z_height});
                    for (int j=(int)Math.floor((y_min+dist)*sim.y_resolution/(y_max-y_min));j<(y_min+dist+sweepWidth)*sim.y_resolution/(y_max-y_min);j++){
                        for(int i=(int)Math.floor((x_min+dist)*sim.x_resolution/(x_max-x_min));i<(x_max-dist)*sim.x_resolution/(x_max-x_min);i++){
                            if(Utils.isSquareCovered(i*(x_max-x_min)/sim.x_resolution,(j)*(y_max-y_min)/sim.y_resolution,
                                    (i+1)*(x_max-x_min)/sim.x_resolution,(j+1)*(y_max-y_min)/sim.y_resolution,
                                    currX,currY-sweepWidth,currX,currY,rad)){
                                coveredSquares[i][j]=true;
                            }
                        }
                    }
                    dir++;
                } else if(dir==1){
                    DroneMovement.moveTo(new float[]{x_max-rad-dist,y_max-rad-dist,z_height});
                    for (int j=(int)Math.floor((y_min+dist)*sim.y_resolution/(y_max-y_min));j<(y_max-dist)*sim.y_resolution/(y_max-y_min);j++){
                        for(int i=(int)Math.floor((x_max-dist-sweepWidth)*sim.x_resolution/(x_max-x_min));i<(x_max-dist)*sim.x_resolution/(x_max-x_min);i++){
                            if(Utils.isSquareCovered(i*(x_max-x_min)/sim.x_resolution,(j)*(y_max-y_min)/sim.y_resolution,
                                    (i+1)*(x_max-x_min)/sim.x_resolution,(j+1)*(y_max-y_min)/sim.y_resolution,
                                    currX,currY-sweepWidth,currX,currY,rad)){
                                coveredSquares[i][j]=true;
                            }
                        }
                    }
                    dir++;
                } else if(dir==2){
                    DroneMovement.moveTo(new float[]{x_min+rad+dist,y_max-rad-dist,z_height});
                    for (int j=(int)Math.floor((y_max-dist-sweepWidth)*sim.y_resolution/(y_max-y_min));j<(y_max-dist)*sim.y_resolution/(y_max-y_min);j++){
                        for(int i=(int)Math.floor((x_min+dist)*sim.x_resolution/(x_max-x_min));i<(x_max-dist)*sim.x_resolution/(x_max-x_min);i++){
                            if(Utils.isSquareCovered(i*(x_max-x_min)/sim.x_resolution,(j)*(y_max-y_min)/sim.y_resolution,
                                    (i+1)*(x_max-x_min)/sim.x_resolution,(j+1)*(y_max-y_min)/sim.y_resolution,
                                    currX,currY-sweepWidth,currX,currY,rad)){
                                coveredSquares[i][j]=true;
                            }
                        }
                    }
                    dir++;
                } else {
                    DroneMovement.moveTo(new float[]{x_min+rad+dist,y_min+3*rad+dist,z_height});
                    for (int j=(int)Math.floor((y_min+dist+sweepWidth)*sim.y_resolution/(y_max-y_min));j<(y_min+dist+sweepWidth)*sim.y_resolution/(y_max-y_min);j++){
                        for(int i=(int)Math.floor((x_min+dist)*sim.x_resolution/(x_max-x_min));i<(x_max-dist-sweepWidth)*sim.x_resolution/(x_max-x_min);i++){
                            if(Utils.isSquareCovered(i*(x_max-x_min)/sim.x_resolution,(j)*(y_max-y_min)/sim.y_resolution,
                                    (i+1)*(x_max-x_min)/sim.x_resolution,(j+1)*(y_max-y_min)/sim.y_resolution,
                                    currX,currY-sweepWidth,currX,currY,rad)){
                                coveredSquares[i][j]=true;
                            }
                        }
                    }
                    dir=0;
                    dist+=sweepWidth;
                }
            }
            
        } else {
            //move to center
            DroneMovement.moveTo(new float[]{(x_min + x_max) / 2, (y_min + y_max) / 2, z_height});
            float currY = (y_min + y_max) / 2;
            float currX = (x_min + x_max) / 2;
            boolean up = true;
            float theta=0;
            float dist=0;
            float dtheta=.1f;
            float ddist=.1f;
            while (currX <= x_max - sweepWidth) {
                if(up){
                    currX+=sweepWidth;
                    DroneMovement.moveTo(new float[]{currX,currY,z_height});
                    for (int j=(int)Math.floor((currY-sweepWidth/2)*sim.y_resolution/(y_max-y_min));j<(currY+sweepWidth/2)*sim.y_resolution/(y_max-y_min);j++){
                        for(int i=(int)Math.floor((currX-sweepWidth*3/2)*sim.x_resolution/(x_max-x_min));i<(currX+sweepWidth/2)*sim.x_resolution/(x_max-x_min);i++){
                            if(Utils.isSquareCovered(i*(x_max-x_min)/sim.x_resolution,(j)*(y_max-y_min)/sim.y_resolution,
                                    (i+1)*(x_max-x_min)/sim.x_resolution,(j+1)*(y_max-y_min)/sim.y_resolution,
                                    x_min+rad,currY,x_max-rad,currY,rad)){
                                coveredSquares[i][j]=true;
                            }
                        }
                    }
                } else {
                    dist+=sweepWidth;
                    float[] xy=new float[2];
                    for(int t=0;t<100;t++){
                        theta+=2*Math.PI/100;
                        xy=Utils.positionConvert(theta, dist, (x_min+x_max)/2, (y_min+y_max)/2);
                        DroneMovement.moveTo(new float[]{xy[0],xy[1],z_height});
                        for (int j=(int)Math.floor((xy[1]-sweepWidth/2)*sim.y_resolution/(y_max-y_min));j<(xy[1]+sweepWidth/2)*sim.y_resolution/(y_max-y_min);j++){
                            for(int i=(int)Math.floor((xy[0]-sweepWidth*3/2)*sim.x_resolution/(x_max-x_min));i<(xy[0]+sweepWidth/2)*sim.x_resolution/(x_max-x_min);i++){
                                if(Utils.isSquareInCircle(i*(x_max-x_min)/sim.x_resolution,(j)*(y_max-y_min)/sim.y_resolution,
                                        (i+1)*(x_max-x_min)/sim.x_resolution,(j+1)*(y_max-y_min)/sim.y_resolution,
                                        xy[0],xy[1],rad)){
                                    coveredSquares[i][j]=true;
                                }
                            }
                        }
                    }
                    currX=xy[0];
                    currY=xy[1];
                }
            }
        }

        return false;
    }

    public boolean planPathToGoal() {

        return false;
    }
}
