/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Drone;

/**
 *
 * @author awells
 */
public class DroneState {
    public float x, y, z, vx, vy, vz;
    public float theta, phi, psi, vtheta, vphi, vpsi;
    
    public DroneState() {
        x=y=z=vx=vy=vz=theta=phi=psi=vtheta=vphi=vpsi=0.0f;
    }
}
