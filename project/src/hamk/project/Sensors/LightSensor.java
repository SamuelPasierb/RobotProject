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

    public LightSensor() {
        
        this.colorSensor = new EV3ColorSensor(SensorPort.S3);
        this.light = colorSensor.getAmbientMode();
        this.sample = new float[light.sampleSize()];
    }

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

    private float rounding(float dist) {
        return Math.round((int)(dist*100));
    }
}