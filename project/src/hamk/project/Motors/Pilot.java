package hamk.project.Motors;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import hamk.project.LCD.LCDClass;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class Pilot extends Thread {
    
    // Constants
    private final int WHEEL_DIAMETER = 56;
    private final int WHEELBASE = 100; 
    private final Wheel leftWheel;
    private final Wheel rightWheel;
    private final MovePilot PILOT;

    // Speed
    private int speed = 0;

    // Atomic values
    private AtomicBoolean running;
    private AtomicBoolean forward;
    private AtomicInteger turnDegrees;

    public Pilot() {
        
        // Wheels
        this.leftWheel = WheeledChassis.modelWheel(Motor.A, WHEEL_DIAMETER).offset(-50);
        this.rightWheel = WheeledChassis.modelWheel(Motor.D, WHEEL_DIAMETER).offset(50); 

        // Pilot
        this.PILOT = new MovePilot(new WheeledChassis(new Wheel[] {leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL));

        // Atomic values
        this.running = new AtomicBoolean(false);
        this.forward = new AtomicBoolean(true);

        this.turnDegrees = new AtomicInteger(0);
    }

    @Override
    public void run() {
        
        this.PILOT.setLinearAcceleration(200);

        while (!this.isInterrupted()) {

            // Running
            if (this.running.get() && !this.PILOT.isMoving()) {
                // Forward || Backward
                if (this.forward.get()) PILOT.forward();
                else PILOT.backward();
            } else if (!this.running.get() && this.PILOT.isMoving()) { // Stop moving
                PILOT.stop();
            }

            // Turning in an arc
            if (turnDegrees.get() != 0) {
                PILOT.arc(50, turnDegrees.get());
                turnDegrees.set(0);
            }

            // Delay by 100ms
            Delay.msDelay(100);

        }

    }

    // Start motors
    public void startMotors() {
        this.running.set(true);
    }

    // Setting speed
    public void setSpeed(int speed) {
        LCDClass.speed.set("Speed: " + speed);
        this.speed = speed;
        // this.PILOT.setLinearSpeed(this.speed);
        Motor.A.setSpeed(this.speed);
        Motor.D.setSpeed(this.speed);
    }

    // Changing speed by 'speed'
    public void changeSpeedBy(int speed) {
        if (this.speed - speed > 0) this.setSpeed(this.speed + speed);
    }

    // Rotates robot by 'degrees'
    private void rotate(int degrees) {
        this.PILOT.rotate(degrees);
    }

}
