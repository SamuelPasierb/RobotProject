package hamk.project.Sensors;

import hamk.project.LCD.LCDClass;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class LightSensor extends Thread {
    private final EV3ColorSensor colorsensor;
    private final SampleProvider light;
    private final float[] sample;

    public LightSensor() {
        this.colorsensor = new EV3ColorSensor(SensorPort.S3);
        this.light = colorsensor.getAmbientMode();
        this.sample = new float[light.sampleSize()];
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            light.fetchSample(sample, 0);
            LCD.clear();
            LCDClass.light.set("Light intensity: " + rounding(sample[0]) + "%");
        }
    }

    private float rounding(float dist) {
        return Math.round((int)(dist*100));
    }
}