package hamk.project.Sensors;

import hamk.project.LCD.LCDClass;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class LightSensor extends Thread {

    private final EV3ColorSensor colorSensor;
    private final SampleProvider light;
    private final float[] sample;

    //Constructor to initialize the LightSensor with the EV3ColorSensor.
    //Sets up the sensor on port S3 and configures it for ambient light measurement.
    public LightSensor() {
        
        this.colorSensor = new EV3ColorSensor(SensorPort.S3);
        this.light = colorSensor.getAmbientMode();
        this.sample = new float[light.sampleSize()];
    }

    // run a thread
    @Override
    public void run() {
        while (!this.isInterrupted()) {
            light.fetchSample(sample, 0);
            LCD.clear();
            LCDClass.reflection.set("Reflection: " + rounding(sample[0]) + "%");

            // Delay by 100ms
            Delay.msDelay(100);
        }
    }

    //Rounds a float value to the nearest integer and scales it by 100 for percentage representation.
    private float rounding(float dist) {
        return Math.round((int)(dist*100));
    }
}
