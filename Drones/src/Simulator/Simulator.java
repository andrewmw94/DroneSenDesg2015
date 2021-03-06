/**
 * The Catholic University of America CSC 513: Fundamentals of Computer Graphics
 * Fall 2014
 *
 * Assignment 2 Starter Code
 *
 * Please see assignment handout for details.
 *
 * Patricio Simari, PhD Assistant Professor Electrical Engineering and Computer
 * Science The Catholic University of America http://faculty.cua.edu/simari/
 *
 * Andrew Wells. 2421713. 2014-12-04.
 *
 */
package Simulator;

import Drone.Drone;
import Drone.DroneState;
import Utils.Mesh;
import Utils.OurUtils;
import java.io.FileNotFoundException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Mouse;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.GL11.*;

public class Simulator {

    public static final boolean showGrid = true;

    // window title
    public static final String APP_TITLE = "Drone Simulator";

    // desired frame rate
    private static final int FRAMERATE = 60;

    // light position and attributes 
    private static final FloatBuffer lightPosition = Utils.OurUtils.floatBuffer(3.0f, 4.0f, 5.0f, 1.0f);
    private static final FloatBuffer lightAmbient = Utils.OurUtils.floatBuffer(0.2f, 0.2f, 0.2f, 1.0f);
    private static final FloatBuffer lightDiffuse = Utils.OurUtils.floatBuffer(0.5f, 0.5f, 0.5f, 1.0f);
    private static final FloatBuffer lightSpecular = Utils.OurUtils.floatBuffer(0.1f, 0.1f, 0.1f, 1.0f);

    // material properties
    private static final FloatBuffer materialAmbient = Utils.OurUtils.floatBuffer(1.0f, 1.0f, 1.0f, 1.0f);
    private static final FloatBuffer materialDiffuse = Utils.OurUtils.floatBuffer(1.0f, 1.0f, 1.0f, 1.0f);
    private static final FloatBuffer materialSpecular = Utils.OurUtils.floatBuffer(1.0f, 1.0f, 1.0f, 1.0f);
    private static final float materialShininess = 8.0f;

    // exit flag
    private static boolean finished;

    // camera positition
    private static float cameraAzimuth = 30.5f;
    private static float cameraElevation = -15.0f;
    private static float cameraDistance = 15.0f;

    // Meshes for obstacles
    ArrayList<Mesh> obstacles = new ArrayList<Mesh>();

    Drone me;

    //The bounding volume
    Mesh bounds;

    Mesh goal;

    final int x_resolution = 50;
    final int y_resolution = 50;

    boolean[][] grid = new boolean[x_resolution][y_resolution];

    // Which rendering mode do we have
    private static boolean renderWire = false;
    private static boolean renderSmooth = false;

    // no constructor needed - this class is static
    public Simulator() {
        bounds = new Mesh("assets/room.obj");
//        bounds.x_offset = -10.5f;
//        bounds.y_offset = -2.5f;
//        bounds.z_offset = -10.5f;
        bounds.x_offset = 0f;
        bounds.y_offset = 0f;
        bounds.z_offset = 0f;
        me = new Drone(new Mesh("assets/quadrotor.obj"), 3.0f, 1.0f, 1.0f);
        me.mesh.x_offset = 3f;
        me.mesh.y_offset = 2f;
        me.mesh.z_offset = 7f;

        goal = new Mesh("assets/cube.obj");
        goal.x_offset = 9f;
        goal.y_offset = 0f;
        goal.z_offset = 4f;

        Mesh o = new Mesh("assets/cube.obj");
        o.x_offset = 1f;
        o.y_offset = 0f;
        o.z_offset = 3f;
        obstacles.add(o);
    }

    // Initialize display and opengl properties.
    public void init() throws Exception {

        // initialize the display
        Display.setTitle(APP_TITLE);
        Display.setFullscreen(false);
        Display.setVSyncEnabled(true);
        Display.create();

        // set up light
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glLight(GL_LIGHT0, GL_AMBIENT, lightAmbient);
        glLight(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
        glLight(GL_LIGHT0, GL_SPECULAR, lightSpecular);

        // set up material
        glMaterial(GL_FRONT, GL_AMBIENT, materialAmbient);
        glMaterial(GL_FRONT, GL_DIFFUSE, materialDiffuse);
        glMaterial(GL_FRONT, GL_SPECULAR, materialSpecular);
        glMaterialf(GL_FRONT, GL_SHININESS, materialShininess);

        // allow changing colors while keeping the above material
        glEnable(GL_COLOR_MATERIAL);

        // set gl functionality
        glEnable(GL_DEPTH_TEST);

        // set background color
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        // get display size
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();

        // perspective transformation 
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        float aspectRatio = ((float) width) / height;
        gluPerspective(45.0f, aspectRatio, 0.1f, 100.0f);

        // set the viewport
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glViewport(0, 0, width, height);

    }

    // Main loop.
    public void run() throws FileNotFoundException {
        Planner p = new Planner(this);
        p.run();

        int counter = 1;
        int effectiveCounter = 0;
        int frmRt = 2;
        int pLength = p.path.size();

        while (!finished) {

            Display.update();

            if (Display.isCloseRequested()) {

                finished = true;

            } else if (Display.isActive()) {

                // The window is in the foreground, so we should play the game
                logic();
                render();

                //move the drone
                if (counter % frmRt == 0) {
                    DroneState ds = p.path.get(effectiveCounter % pLength);
                    me.mesh.x_offset = ds.x;
                    me.mesh.y_offset = ds.y;
                    me.mesh.z_offset = ds.z;
                    effectiveCounter++;
                    float rad = ds.y * (float) Math.tan(Math.PI / 7);
                    updateGrid(new float[] {ds.x, ds.y, ds.z}, rad);
                }

                counter++;
                //renderCoordinateFrame();

                Display.sync(FRAMERATE);

            } else {

                // The window is not in the foreground, so we can allow other 
                // stuff to run and infrequently update
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                logic();

                if (Display.isVisible() && Display.isDirty()) {
                    // Only bother rendering if the window is visible and dirty
                    render();
                    renderCoordinateFrame();
                }
            }
        }
    }
    
    // Clean up before exit.
    private void cleanup() {

        // Close the window
        Display.destroy();

    }

    // Handle input.
    private void logic() {

        // escape to quit
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            finished = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
            renderSmooth = renderWire = false;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
            renderSmooth = true;
            renderWire = false;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
            renderSmooth = false;
            renderWire = true;
        }

        // mouse event catcher
        while (Mouse.next()) {
            if (Mouse.isButtonDown(0)) {
                cameraElevation += 0.5f * Mouse.getEventDY();
            }
            if (Mouse.isButtonDown(0)) {
                cameraAzimuth -= 0.5f * Mouse.getEventDX();
            }
            if (Mouse.isButtonDown(1)) {
                cameraDistance += 0.1 * Mouse.getEventDY();
            }
        }
    }

    // Render model from current view.
    private void render() {

        // clear the screen and depth buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // viewing transformation (bottom of the modelview stack)
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // perform transformation according to viewing angle; i.e. undo camera 
        // transformation (in reverse order) and go into object space
        glPushMatrix();

        //glTranslatef((minCorner[0] - maxCorner[0])/2, (minCorner[1] - maxCorner[1])/2, (minCorner[2] - maxCorner[2])/2-cameraDistance);
        glTranslatef(0.0f, 0.0f, -cameraDistance);

        glRotatef(-cameraElevation, 1.0f, 0.0f, 0.0f);
        glRotatef(-cameraAzimuth, 0.0f, 1.0f, 0.0f);

        // set drawing color
        glColor3f(1.0f, 0.0f, 0.0f);

        // Scale to 25 since scaling to 1 causes weird results for the color or lighting or something.
        //renderMesh();
        //renderCoordinateFrame();
        renderBounds();
        renderObstacles();
        renderMyself();
        renderGoal();

        if (showGrid) {
            renderGrid();
        }

        // pop out of object space and back into camera space
        glPopMatrix();

        // place light in camera space
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);

        // flush the data
        glFlush();
    }

    private void renderBounds() {
        renderMesh(bounds, true, false, new float[]{0f, 0f, 0f});
    }

    private void renderObstacles() {
        for (Mesh m : obstacles) {
            renderMesh(m, false, false, new float[]{0f, 1f, 0f});
        }
    }

    private void renderMyself() {
        renderMesh(me.mesh, false, false, new float[]{0f, 0f, 1f});
    }

    private void renderGoal() {
        renderMesh(goal, true, false, new float[]{1f, 0f, 0f});
    }

    private void renderGrid() {
        float xOffset = 15.0f / x_resolution;
        float yOffset = 15.0f / y_resolution;

        for (int i = 0; i < x_resolution; i++) {
            for (int j = 0; j < y_resolution; j++) {
                float[] c;
                if (grid[i][j]) {
                    c = new float[]{1f, 1f, 0f};
                } else {
                    c = new float[]{0f, 1f, 1f};
                }
                //drawSquare(-10.5f + xOffset * i, -10.5f + yOffset * j, -10.5f + xOffset * i + xOffset, -10.5f + yOffset * j + yOffset, c);
                drawSquare(xOffset * i, yOffset * j, xOffset * i + xOffset, yOffset * j + yOffset, c);

            }
        }
    }

    private void drawSquare(float x1, float y1, float x2, float y2, float[] color) {
        float z = 0f;
        glColor3f(color[0], color[1], color[2]);
        //glTranslatef(x1, z, y1);
        //glPushMatrix();
        glBegin(GL_QUADS);
        glVertex3f(x1, z, y1);
        glVertex3f(x1, z, y2);
        glVertex3f(x2, z, y2);
        glVertex3f(x2, z, y1);
        glEnd();
        //glPopMatrix();
    }

    private void renderMesh(Mesh mg, boolean renderWire, boolean renderSmooth, float[] color) {

        glColor3f(color[0], color[1], color[2]);

        if (!renderWire && !renderSmooth) {

            // set flat shading 
            glShadeModel(GL_FLAT);

            //glTranslatef(mg.x_offset, mg.y_offset, mg.z_offset);
            //glPushMatrix();
            // draw triangles
            glBegin(GL_TRIANGLES);

            for (int i = 0; i < mg.f.length; i++) {
                glNormal3f(mg.nf[i][0] / mg.diagLength, mg.nf[i][1] / mg.diagLength, mg.nf[i][2] / mg.diagLength);
                glVertex3f(mg.x_offset + mg.v[mg.f[i][0]][0], mg.y_offset + mg.v[mg.f[i][0]][1], mg.z_offset + mg.v[mg.f[i][0]][2]);
                glVertex3f(mg.x_offset + mg.v[mg.f[i][1]][0], mg.y_offset + mg.v[mg.f[i][1]][1], mg.z_offset + mg.v[mg.f[i][1]][2]);
                glVertex3f(mg.x_offset + mg.v[mg.f[i][2]][0], mg.y_offset + mg.v[mg.f[i][2]][1], mg.z_offset + mg.v[mg.f[i][2]][2]);
            }

            glEnd();
            //glPopMatrix();
        } else if (renderWire) {

            // set smooth shading 
            glShadeModel(GL_SMOOTH);

            //glTranslatef(mg.x_offset, mg.y_offset, mg.z_offset);
            //glPushMatrix();
            // draw lines
            glBegin(GL_LINES);
            for (int i = 0; i < mg.f.length; i++) {
                glNormal3f(mg.nv[mg.f[i][0]][0] / mg.diagLength, mg.nv[mg.f[i][0]][1] / mg.diagLength, mg.nv[mg.f[i][0]][2] / mg.diagLength);
                glVertex3f(mg.x_offset + mg.v[mg.f[i][0]][0], mg.y_offset + mg.v[mg.f[i][0]][1], mg.z_offset + mg.v[mg.f[i][0]][2]);
                glNormal3f(mg.nv[mg.f[i][1]][0] / mg.diagLength, mg.nv[mg.f[i][1]][1] / mg.diagLength, mg.nv[mg.f[i][1]][2] / mg.diagLength);
                glVertex3f(mg.x_offset + mg.v[mg.f[i][1]][0], mg.y_offset + mg.v[mg.f[i][1]][1], mg.z_offset + mg.v[mg.f[i][1]][2]);
                glNormal3f(mg.nv[mg.f[i][1]][0] / mg.diagLength, mg.nv[mg.f[i][1]][1] / mg.diagLength, mg.nv[mg.f[i][1]][2] / mg.diagLength);
                glVertex3f(mg.x_offset + mg.v[mg.f[i][1]][0], mg.y_offset + mg.v[mg.f[i][1]][1], mg.z_offset + mg.v[mg.f[i][1]][2]);
                glNormal3f(mg.nv[mg.f[i][2]][0] / mg.diagLength, mg.nv[mg.f[i][2]][1] / mg.diagLength, mg.nv[mg.f[i][2]][2] / mg.diagLength);
                glVertex3f(mg.x_offset + mg.v[mg.f[i][2]][0], mg.y_offset + mg.v[mg.f[i][2]][1], mg.z_offset + mg.v[mg.f[i][2]][2]);
                glNormal3f(mg.nv[mg.f[i][2]][0] / mg.diagLength, mg.nv[mg.f[i][2]][1] / mg.diagLength, mg.nv[mg.f[i][2]][2] / mg.diagLength);
                glVertex3f(mg.x_offset + mg.v[mg.f[i][2]][0], mg.y_offset + mg.v[mg.f[i][2]][1], mg.z_offset + mg.v[mg.f[i][2]][2]);
                glNormal3f(mg.nv[mg.f[i][0]][0] / mg.diagLength, mg.nv[mg.f[i][0]][1] / mg.diagLength, mg.nv[mg.f[i][0]][2] / mg.diagLength);
                glVertex3f(mg.x_offset + mg.v[mg.f[i][0]][0], mg.y_offset + mg.v[mg.f[i][0]][1], mg.z_offset + mg.v[mg.f[i][0]][2]);
            }
            glEnd();
            //glPopMatrix();
        } else {
            // set smooth shading 
            glShadeModel(GL_SMOOTH);

            //glTranslatef(mg.x_offset, mg.y_offset, mg.z_offset);
            //glPushMatrix();
            glBegin(GL_TRIANGLES);
            glLoadIdentity();
            for (int i = 0; i < mg.f.length; i++) {
                glNormal3f(mg.nv[mg.f[i][0]][0] / mg.diagLength, mg.nv[mg.f[i][0]][1] / mg.diagLength, mg.nv[mg.f[i][0]][2] / mg.diagLength);
                glVertex3f(mg.x_offset + mg.v[mg.f[i][0]][0], mg.y_offset + mg.v[mg.f[i][0]][1], mg.z_offset + mg.v[mg.f[i][0]][2]);
                glNormal3f(mg.nv[mg.f[i][1]][0] / mg.diagLength, mg.nv[mg.f[i][1]][1] / mg.diagLength, mg.nv[mg.f[i][1]][2] / mg.diagLength);
                glVertex3f(mg.x_offset + mg.v[mg.f[i][1]][0], mg.y_offset + mg.v[mg.f[i][1]][1], mg.z_offset + mg.v[mg.f[i][1]][2]);
                glNormal3f(mg.nv[mg.f[i][2]][0] / mg.diagLength, mg.nv[mg.f[i][2]][1] / mg.diagLength, mg.nv[mg.f[i][2]][2] / mg.diagLength);
                glVertex3f(mg.x_offset + mg.v[mg.f[i][2]][0], mg.y_offset + mg.v[mg.f[i][2]][1], mg.z_offset + mg.v[mg.f[i][2]][2]);
            }

            glEnd();
            //glPopMatrix();
        }
    }

//    private void renderMesh(Mesh mg, boolean renderWire, boolean renderSmooth, float[] color) {
//
//        glColor3f(color[0], color[1], color[2]);
//
//        if (!renderWire && !renderSmooth) {
//
//            // set flat shading 
//            glShadeModel(GL_FLAT);
//
//            glTranslatef(mg.x_offset, mg.y_offset, mg.z_offset);
//            //glPushMatrix();
//
//            // draw triangles
//            glBegin(GL_TRIANGLES);
//
//            for (int i = 0; i < mg.f.length; i++) {
//                glNormal3f(mg.nf[i][0] / mg.diagLength, mg.nf[i][1] / mg.diagLength, mg.nf[i][2] / mg.diagLength);
//                glVertex3f(mg.v[mg.f[i][0]][0], mg.v[mg.f[i][0]][1], mg.v[mg.f[i][0]][2]);
//                glVertex3f(mg.v[mg.f[i][1]][0], mg.v[mg.f[i][1]][1], mg.v[mg.f[i][1]][2]);
//                glVertex3f(mg.v[mg.f[i][2]][0], mg.v[mg.f[i][2]][1], mg.v[mg.f[i][2]][2]);
//            }
//
//            glEnd();
//            //glPopMatrix();
//        } else if (renderWire) {
//
//            // set smooth shading 
//            glShadeModel(GL_SMOOTH);
//
//            glTranslatef(mg.x_offset, mg.y_offset, mg.z_offset);
//            //glPushMatrix();
//            // draw lines
//            glBegin(GL_LINES);
//            for (int i = 0; i < mg.f.length; i++) {
//                glNormal3f(mg.nv[mg.f[i][0]][0] / mg.diagLength, mg.nv[mg.f[i][0]][1] / mg.diagLength, mg.nv[mg.f[i][0]][2] / mg.diagLength);
//                glVertex3f(mg.v[mg.f[i][0]][0], mg.v[mg.f[i][0]][1], mg.v[mg.f[i][0]][2]);
//                glNormal3f(mg.nv[mg.f[i][1]][0] / mg.diagLength, mg.nv[mg.f[i][1]][1] / mg.diagLength, mg.nv[mg.f[i][1]][2] / mg.diagLength);
//                glVertex3f(mg.v[mg.f[i][1]][0], mg.v[mg.f[i][1]][1], mg.v[mg.f[i][1]][2]);
//                glNormal3f(mg.nv[mg.f[i][1]][0] / mg.diagLength, mg.nv[mg.f[i][1]][1] / mg.diagLength, mg.nv[mg.f[i][1]][2] / mg.diagLength);
//                glVertex3f(mg.v[mg.f[i][1]][0], mg.v[mg.f[i][1]][1], mg.v[mg.f[i][1]][2]);
//                glNormal3f(mg.nv[mg.f[i][2]][0] / mg.diagLength, mg.nv[mg.f[i][2]][1] / mg.diagLength, mg.nv[mg.f[i][2]][2] / mg.diagLength);
//                glVertex3f(mg.v[mg.f[i][2]][0], mg.v[mg.f[i][2]][1], mg.v[mg.f[i][2]][2]);
//                glNormal3f(mg.nv[mg.f[i][2]][0] / mg.diagLength, mg.nv[mg.f[i][2]][1] / mg.diagLength, mg.nv[mg.f[i][2]][2] / mg.diagLength);
//                glVertex3f(mg.v[mg.f[i][2]][0], mg.v[mg.f[i][2]][1], mg.v[mg.f[i][2]][2]);
//                glNormal3f(mg.nv[mg.f[i][0]][0] / mg.diagLength, mg.nv[mg.f[i][0]][1] / mg.diagLength, mg.nv[mg.f[i][0]][2] / mg.diagLength);
//                glVertex3f(mg.v[mg.f[i][0]][0], mg.v[mg.f[i][0]][1], mg.v[mg.f[i][0]][2]);
//            }
//            glEnd();
//            //glPopMatrix();
//        } else {
//            // set smooth shading 
//            glShadeModel(GL_SMOOTH);
//
//            glTranslatef(mg.x_offset, mg.y_offset, mg.z_offset);
//            //glPushMatrix();
//
//            glBegin(GL_TRIANGLES);
//            glLoadIdentity();
//            for (int i = 0; i < mg.f.length; i++) {
//                glNormal3f(mg.nv[mg.f[i][0]][0] / mg.diagLength, mg.nv[mg.f[i][0]][1] / mg.diagLength, mg.nv[mg.f[i][0]][2] / mg.diagLength);
//                glVertex3f(mg.v[mg.f[i][0]][0], mg.v[mg.f[i][0]][1], mg.v[mg.f[i][0]][2]);
//                glNormal3f(mg.nv[mg.f[i][1]][0] / mg.diagLength, mg.nv[mg.f[i][1]][1] / mg.diagLength, mg.nv[mg.f[i][1]][2] / mg.diagLength);
//                glVertex3f(mg.v[mg.f[i][1]][0], mg.v[mg.f[i][1]][1], mg.v[mg.f[i][1]][2]);
//                glNormal3f(mg.nv[mg.f[i][2]][0] / mg.diagLength, mg.nv[mg.f[i][2]][1] / mg.diagLength, mg.nv[mg.f[i][2]][2] / mg.diagLength);
//                glVertex3f(mg.v[mg.f[i][2]][0], mg.v[mg.f[i][2]][1], mg.v[mg.f[i][2]][2]);
//            }
//
//            glEnd();
//            //glPopMatrix();
//        }
//    }
    // Render the basis vectors of the coordinate frame: x (red), y (blue), and
    // z (green). Can be useful for debugging purposes. 
    private static void renderCoordinateFrame() {

        // temporarily disable lighting
        glDisable(GL_LIGHTING);

        // draw thicker lines for clarity
        glLineWidth(3.0f);

        glBegin(GL_LINES);

        // x basis vector (red)
        glColor3f(1.0f, 0.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, 0.0f);
        glVertex3f(1.0f, 0.0f, 0.0f);

        // y basis vector (green)
        glColor3f(0.0f, 1.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, 0.0f);
        glVertex3f(0.0f, 1.0f, 0.0f);

        // z basis vector (blue)
        glColor3f(0.0f, 0.0f, 1.0f);
        glVertex3f(0.0f, 0.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, 1.0f);

        glEnd();

        // renable lighting
        glEnable(GL_LIGHTING);
    }
    
    
    
    private void updateGrid(float[] p, float rad) {
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                if(!grid[i][j]) {
                    float x1, y1, x2, y2;
                    x1 = (float) (i*(15.0/x_resolution));
                    y1 = (float) (j*(15.0/x_resolution));
                    x2 = (float) ((i+1)*(15.0/x_resolution));
                    y2 = (float) ((j+1)*(15.0/x_resolution));

                    if(OurUtils.isSquareInCircle(x1, y1, x2, y2, p[0], p[2], rad)) {
                        grid[i][j] = true;
                    }
                }
            }            
        }
    }
}
