package hamk.project.Sensors;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

import hamk.project.LCD.LCDClass;
import hamk.project.Logic.ObstacleAvoidance;

/**
 * <h3>Class for the Ultrasonic Sensor</h3>
 * This class handles operations with the ultrasonic sensor.
 * <p>
 * Extends {@link Thread} so it can run without disturbing other more important functions of the robot.
 * </p>
 * @author Artjom Smorgulenko, Samuel Pasierb
 */
public class UltraSonic extends Thread {
    
    // Variables
    private static final EV3UltrasonicSensor ultraSonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
    public static final SampleProvider distance = ultraSonicSensor.getDistanceMode();
    public final static float[] sample = new float[distance.sampleSize()];

    private final ObstacleAvoidance obstacleAvoidance;

    /**
      * <h3>Constructor for the Ultrasonic sensor.</h3>
      * 
      * Sets up the sensor on port S2 and configures it for distance measurement.
      */
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

    /**
      * <h3>Round distance to centimeters</h3>
      * 
      * @param dist - distance value
      * @return  rounded distance value converted to centimeters
      * 
      * <p>
      * Multiplies {@code dist} by 10 000 and divides it by 100
      * </p>
      */
    private static float roundToCM(float dist) {
        return Math.round(dist * 10_000) / 100.0f;
    }

    /**
     * Method for getting the current distance.
     * @return {@code sample[0]} (distance)
     */
    public static float getDistance() {
        distance.fetchSample(sample, 0);
        return roundToCM(sample[0]);
    }

}