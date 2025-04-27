package hamk.project.Sensors;

// Imports
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

import hamk.project.LCD.LCDClass;
import hamk.project.Main;

/**
  * <h3>Class for the Light sensor</h3>
  * This class handles operations with the light sensor.
  * <p>
  * Extends {@link Thread} so it can run without disturbing other more important functions of the robot.
  * </p>
  * @author Artjom Smorgulenko, Samuel Pasierb
  */
public class Light extends Thread {

    private final EV3ColorSensor colorSensor;
    private static SampleProvider light;
    private static float[] sample;

    private final float BACKGROUND = 0.3f;
    public static final float BORDER = 0.1f;

    /**
      *  <h3>Constructor to initialize the LightSensor with the EV3ColorSensor.</h3>
      *  Sets up the sensor on port S3 and configures it for ambient light measurement.
      *  
      */
    public Light() {
        
        this.colorSensor = new EV3ColorSensor(SensorPort.S3);
        light = colorSensor.getRedMode();
        sample = new float[light.sampleSize()];

    }

    /**
     * <h3>Light Sensor Thread</h3>
     * <p>The robot is positioned such as the line is on its right side and the sensor is on the edge of the line</p>
     * <p>This works, because the place where the black line collides with white background has relefection rate that is in between black and white
     * <ol>
     *  <li>Updates light reflection value</li>
     *  <li>Determies if the robot should: 
     *      <ul>
     *          <li>Turn to right (is on the white background and should go towards the line)</li>
     *          <li>Go straight (is on the edge and therefore doesn't have to turn)</li>
     *          <li>Turn to left (is completely on the line and should go out of it)</li>
     *      </ul>
     *  <li>Updates reflection value for {@link LDCClass}</li>
     * </ul>
     */
    @Override
    public void run() {
        while (!this.isInterrupted()) {
            light.fetchSample(sample, 0);

            // Follow the line
            if (sample[0] > BACKGROUND) { // RIGHT
                Main.getPilot().turn("RIGHT");
            } else if (sample[0] > BORDER) { // STRAIGHT
                Main.getPilot().endTurn();
            } else { // LEFT
                Main.getPilot().turn("LEFT");
            }

            // Update atomic value
            LCDClass.reflection.set("Reflection: " + (sample[0] * 100.0f) + "%");

        }
    }

    /**
     * Method for getting the current reflection.
     * @return {@code sample[0]} (light)
     */
    public static float getCurrentReflection() {
        light.fetchSample(sample, 0);
        return sample[0];
    }

}