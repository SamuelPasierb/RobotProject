package hamk.project.Sensors;

import hamk.project.Main;
import hamk.project.LCD.LCDClass;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Light extends Thread {

    private final EV3ColorSensor colorSensor;
    private final SampleProvider light;
    private final float[] sample;

    private final float THRESHOLD = 0.2f;

    public Light() {
        
        this.colorSensor = new EV3ColorSensor(SensorPort.S3);
        this.light = colorSensor.getRedMode();
        this.sample = new float[light.sampleSize()];
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            light.fetchSample(sample, 0);

            // Update atomic value
            LCDClass.reflection.set("Reflection: " + (sample[0] * 100.0f) + "%");

            if (sample[0] > THRESHOLD) {
                Main.getPilot().turn();
            } else { // Move normally
                Main.getPilot().endTurn();
            }

            // Delay by 100ms
            Delay.msDelay(100);
        }
    }

}