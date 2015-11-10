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

    float roll, pitch, yaw;

    public Drone(Mesh m, float x, float y, float z) {
        mesh = m;
        mesh.x_offset = x;
        mesh.x_offset = y;
        mesh.x_offset = z;
        roll = 0f;
        pitch = 0f;
        yaw = 0f;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getX() {
        return mesh.x_offset;
    }

    public float getY() {
        return mesh.y_offset;
    }

    public float getZ() {
        return mesh.z_offset;
    }

    public void setX(float x) {
        mesh.x_offset = x;
    }

    public void setY(float y) {
        mesh.y_offset = y;
    }

    public void setZ(float z) {
        mesh.z_offset = z;
    }
}
