/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Simulator.Simulator;

/**
 *
 * @author awells
 */
public class DroneRunner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
        
        System.out.println("YO");
        
        new Simulator().run();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
}
