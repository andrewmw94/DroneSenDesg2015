/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Stores all the information we need for a mesh-graph data structure. Not the
 * most efficient space-wise, but it works fairly quickly.
 *
 * @author Andrew Wells.
 */
public class Mesh {

    /* names are fairly self-explanatory except:
     * v stands for vertex,
     * f stands for face,
     * n stands for normal,
     * vfs is the list of faces adjacent to any given vertex.
     */
    public float[][] v;
    public int[][] f;
    public HashMap<Edge, Integer> edgeFaceMap = new HashMap<Edge, Integer>();
    public float[][] nv;
    public float[][] nf;
    public ArrayList<ArrayList<Integer>> vfs = new ArrayList<>();
    public float[] maxCorner = new float[]{-1000000.0f, -10000000.0f, -10000000.0f};
    public float[] minCorner = new float[]{10000000.0f, 100000000.0f, 100000000.0f};
    public float diagLength = 1.0f;
    public float[][] faceWeights;

    public int[][] faceNeighbors;

    public float maxPairDist = 0;
    public float minPairDist = Float.MIN_VALUE;

    public int[] colorGroups;
    public float[][] colors;
    
    public float x_offset;
    public float y_offset;
    public float z_offset;

    public Mesh(String s) {
        try {
            loadMesh(s);
            init();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to load mesh.");
            System.out.println(e.toString());
        }
    }

    // Calculates the face neighbor info.
    private void init() {
        faceNeighbors = new int[f.length][3];
        for (int i = 0; i < f.length; i++) {

            if (edgeFaceMap.containsKey(new Edge(f[i][1], f[i][0]))) {
                faceNeighbors[i][0] = edgeFaceMap.get(new Edge(f[i][1], f[i][0]));
            } else if (edgeFaceMap.containsKey(new Edge(f[i][0], f[i][1]))) {
                faceNeighbors[i][0] = edgeFaceMap.get(new Edge(f[i][0], f[i][1]));
            } else {
                System.out.println("uhoh");
            }
            if (edgeFaceMap.containsKey(new Edge(f[i][1], f[i][2]))) {
                faceNeighbors[i][0] = edgeFaceMap.get(new Edge(f[i][1], f[i][2]));
            } else if (edgeFaceMap.containsKey(new Edge(f[i][2], f[i][1]))) {
                faceNeighbors[i][0] = edgeFaceMap.get(new Edge(f[i][2], f[i][1]));
            } else {
                System.out.println("uhoh");
            }
            if (edgeFaceMap.containsKey(new Edge(f[i][2], f[i][0]))) {
                faceNeighbors[i][0] = edgeFaceMap.get(new Edge(f[i][2], f[i][0]));
            } else if (edgeFaceMap.containsKey(new Edge(f[i][0], f[i][2]))) {
                faceNeighbors[i][0] = edgeFaceMap.get(new Edge(f[i][0], f[i][2]));
            } else {
                System.out.println("uhoh");
            }
        }
    }

    // load a mesh
    private void loadMesh(String fileName) throws FileNotFoundException {
        Scanner inp = new Scanner(new File(fileName));
        if (!inp.hasNextLine()) {
            return;
        }
        ArrayList<float[]> vertices = new ArrayList<>();
        ArrayList<int[]> faces = new ArrayList<>();

        do {
            String s = inp.next();
            if (s.length() == 1 && s.charAt(0) == 'v') {
                float[] arr = new float[3];
                arr[0] = inp.nextFloat();
                arr[1] = inp.nextFloat();
                arr[2] = inp.nextFloat();
                vertices.add(arr);
            } else if (s.length() == 1 && s.charAt(0) == 'f') {
                int[] arr = new int[3];
                arr[0] = inp.nextInt() - 1;
                arr[1] = inp.nextInt() - 1;
                arr[2] = inp.nextInt() - 1;
                faces.add(arr);
                Edge e = new Edge(arr[0], arr[1]);
                edgeFaceMap.put(e, faces.size() - 1);
                e = new Edge(arr[1], arr[2]);
                edgeFaceMap.put(e, faces.size() - 1);
                e = new Edge(arr[2], arr[0]);
                edgeFaceMap.put(e, faces.size() - 1);
            } else {
                inp.nextLine();
            }
        } while (inp.hasNext());

        System.out.println(faces.size() + " faces loaded.");

        float[][] ver = new float[vertices.size()][3];
        int[][] fac = new int[faces.size()][3];
        for (int i = 0; i < ver.length; i++) {
            ver[i][0] = vertices.get(i)[0];
            ver[i][1] = vertices.get(i)[1];
            ver[i][2] = vertices.get(i)[2];
            vfs.add(new ArrayList<Integer>());
            if (ver[i][0] < minCorner[0]) {
                minCorner[0] = ver[i][0];
            }
            if (ver[i][1] < minCorner[1]) {
                minCorner[1] = ver[i][1];
            }
            if (ver[i][2] < minCorner[2]) {
                minCorner[2] = ver[i][2];
            }
            if (ver[i][0] > maxCorner[0]) {
                maxCorner[0] = ver[i][0];
            }
            if (ver[i][1] > maxCorner[1]) {
                maxCorner[1] = ver[i][1];
            }
            if (ver[i][2] > maxCorner[2]) {
                maxCorner[2] = ver[i][2];
            }
        }
        for (int i = 0; i < fac.length; i++) {
            fac[i][0] = faces.get(i)[0];
            fac[i][1] = faces.get(i)[1];
            fac[i][2] = faces.get(i)[2];
            vfs.get(fac[i][0]).add(i);
            vfs.get(fac[i][1]).add(i);
            vfs.get(fac[i][2]).add(i);
        }
        v = ver;
        f = fac;

        diagLength = (float) Math.sqrt((maxCorner[0] - minCorner[0]) * (maxCorner[0] - minCorner[0]) + (maxCorner[1] - minCorner[1]) * (maxCorner[1] - minCorner[1]) + (maxCorner[2] - minCorner[2]) * (maxCorner[2] - minCorner[2]));

        //System.out.println("Diagonal length = " + diagLength);
        computeNormals();
    }

    // computes normals
    private void computeNormals() {
        nv = new float[v.length][3];
        nf = new float[f.length][3];
        for (int i = 0; i < f.length; i++) {
            float ax = v[f[i][1]][0] - v[f[i][0]][0];
            float ay = v[f[i][1]][1] - v[f[i][0]][1];
            float az = v[f[i][1]][2] - v[f[i][0]][2];

            float bx = v[f[i][2]][0] - v[f[i][0]][0];
            float by = v[f[i][2]][1] - v[f[i][0]][1];
            float bz = v[f[i][2]][2] - v[f[i][0]][2];

            nf[i][0] = (ay * bz - by * az);
            nf[i][1] = -(ax * bz - bx * az);
            nf[i][2] = (ax * by - bx * ay);

            float sumSqur = nf[i][0] * nf[i][0] + nf[i][1] * nf[i][1] + nf[i][2] * nf[i][2];
            sumSqur = (float) Math.sqrt(sumSqur);
            nf[i][0] /= sumSqur;
            nf[i][1] /= sumSqur;
            nf[i][2] /= sumSqur;
        }
        for (int i = 0; i < v.length; i++) {
            float a = 0.0f;
            float b = 0.0f;
            float c = 0.0f;

            for (int j : vfs.get(i)) {
                a += nf[j][0];
                b += nf[j][1];
                c += nf[j][2];
            }

            float sumSqur = a * a + b * b + c * c;
            sumSqur = (float) Math.sqrt(sumSqur);
            nv[i][0] = a / sumSqur;
            nv[i][1] = b / sumSqur;
            nv[i][2] = c / sumSqur;
        }
    }
}