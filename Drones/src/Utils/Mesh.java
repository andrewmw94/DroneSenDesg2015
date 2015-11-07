package Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Mesh {
    ArrayList<float[]> vertices = new ArrayList<>();
    ArrayList<int[]> faces = new ArrayList<>();    

    public Mesh() {
        
    }
    
    public void loadMeshFromFile(String fileName) throws FileNotFoundException{
        Scanner inp = new Scanner(new File(fileName));
        if (!inp.hasNextLine()) {
            return;
        }
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
        }
        for (int i = 0; i < fac.length; i++) {
            fac[i][0] = faces.get(i)[0];
            fac[i][1] = faces.get(i)[1];
            fac[i][2] = faces.get(i)[2];

        }
    }
    
}