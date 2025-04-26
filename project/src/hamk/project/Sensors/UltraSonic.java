package hamk.project.Sensors;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

import hamk.project.LCD.LCDClass;
import hamk.project.Logic.ObstacleAvoidance;

public class UltraSonic extends Thread {
    
    // Variables
    private static final EV3UltrasonicSensor ultraSonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
    public static final SampleProvider distance = ultraSonicSensor.getDistanceMode();
    public final static float[] sample = new float[distance.sampleSize()];

    private final ObstacleAvoidance obstacleAvoidance;

    // Constructor
    public UltraSonic() {
        this.obstacleAvoidance = new ObstacleAvoidance();
    }

    @Override
    public void run() {

        while (!this.isInterrupted()) {

            // Get the current distance
            distance.fetchSample(sample, 0);

            // Obstacle avoidance
            this.obstacleAvoidance.avoid(roundToCM(sample[0]));

            // 30 cm from an obstacle
            if (sample[0] <= 0.3) {
                //Main.getPilot().avoidObstacle();
            }

            // Update atomic value
            LCDClass.distance.set("Distance: " + roundToCM(sample[0]) + " cm");

            // Delay by 25ms
            Delay.msDelay(25);

        }

    }

    private static float roundToCM(float dist) {
        return Math.round(dist * 10_000) / 100.0f;
    }

    public static float getDistance() {
        distance.fetchSample(sample, 0);
        return roundToCM(sample[0]);
    }

}