package hamk.project.Sensors;

import hamk.project.Main;
import hamk.project.LCD.LCDClass;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class Light extends Thread {

    private final EV3ColorSensor colorSensor;
    private static SampleProvider light;
    private static float[] sample;

    private final float BACKGROUND = 0.3f;
    public static final float BORDER = 0.1f;

    //Constructor to initialize the LightSensor with the EV3ColorSensor.
    //Sets up the sensor on port S3 and configures it for ambient light measurement.
    public Light() {
        
        this.colorSensor = new EV3ColorSensor(SensorPort.S3);
        light = colorSensor.getRedMode();
        sample = new float[light.sampleSize()];

    }

    // run a thread
    @Override
    public void run() {
        while (!this.isInterrupted()) {
            light.fetchSample(sample, 0);

            // Update atomic value
            LCDClass.reflection.set("Reflection: " + (sample[0] * 100.0f) + "%");

            // Follow the line
            if (sample[0] > BACKGROUND) { // RIGHT
                Main.getPilot().turn("LEFT");
            } else if (sample[0] > BORDER) { // LEFT
                Main.getPilot().endTurn();
            } else { // Move normally
                Main.getPilot().turn("RIGHT");
            }

            // Delay by 20ms
            // Delay.msDelay(20);
        }
    }

    public static float getCurrentReflection() {
        light.fetchSample(sample, 0);
        return sample[0];
    }

}