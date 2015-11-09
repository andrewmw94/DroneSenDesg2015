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
    float x_coord;
    float y_coord;
    float z_coord;
    
    public Drone(Mesh m, float x, float y, float z) {
        mesh = m;
        x_coord = x;
        y_coord = y;
        z_coord = z;
    }
}
