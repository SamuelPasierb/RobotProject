package hamk.project.Sensors;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import hamk.project.Main;
import hamk.project.LCD.LCDClass;

public class UltraSonic extends Thread {
    
    // Variables
    private final EV3UltrasonicSensor ultraSonicSensor;
    private final SampleProvider distance;
    private final float[] sample;

    // Constructor
    public UltraSonic() {
        this.ultraSonicSensor = new EV3UltrasonicSensor(SensorPort.S2);

        this.distance = this.ultraSonicSensor.getDistanceMode();
        this.sample = new float[this.distance.sampleSize()];
    }

    @Override
    public void run() {

        while (!this.isInterrupted()) {

            // Get the current distance
            distance.fetchSample(sample, 0);

            // TODO: do stuff with distance

            // Update atomic value
            LCDClass.distance.set("Distance: " + roundToCM(sample[0]) + " cm");

            // Delay by 100ms
            Delay.msDelay(100);

        }

    }

    private float roundToCM(float dist) {
        return Math.round(dist * 10_000) / 100.0f;
    }

}
