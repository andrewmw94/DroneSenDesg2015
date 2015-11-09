/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author Andrew Wells
 */
public class Edge {
    public int vi1;
    public int vi2;
    
    public Edge(int n1, int n2) {
        vi1 = n1;
        vi2 = n2;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Edge))
            return false;
        Edge e = (Edge) o;
        if(vi1 == e.vi1 && vi2 == e.vi2)
            return true;
        return false;
    }
    
    @Override
    public int hashCode() {
        return vi1 + 10007 * vi2;
    }
}