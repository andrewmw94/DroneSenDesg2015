/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Drone;

import Utils.Mesh;

/**
 *
 * @author awells
 */
public class Drone {

    public Mesh mesh;

    public DroneState ourState;

    public Drone(Mesh m, float x, float y, float z) {
        mesh = m;
        ourState = new DroneState();
        ourState.x = x;
        ourState.y = y;
        ourState.z = z;
    }
}
