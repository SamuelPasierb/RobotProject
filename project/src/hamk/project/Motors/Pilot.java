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
    private boolean slowedDown = false;

    // Atomic values
    private AtomicBoolean running;
    private AtomicBoolean forward;
    private AtomicBoolean avoid;
    private AtomicBoolean turning;
    private AtomicBoolean avoiding;

    public Pilot() {
        
        // Wheels
        this.leftWheel = WheeledChassis.modelWheel(Motor.A, WHEEL_DIAMETER).offset(-50);
        this.rightWheel = WheeledChassis.modelWheel(Motor.D, WHEEL_DIAMETER).offset(50); 

        // Pilot
        this.PILOT = new MovePilot(new WheeledChassis(new Wheel[] {leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL));

        // Atomic values
        this.running = new AtomicBoolean(false);
        this.forward = new AtomicBoolean(true);
        this.avoid = new AtomicBoolean(false);
        this.avoiding = new AtomicBoolean(false);
        this.turning = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        
        this.PILOT.setLinearAcceleration(200);
        this.PILOT.setAngularAcceleration(200);

        while (!this.isInterrupted()) {

            // Running
            if (this.running.get() && !this.PILOT.isMoving()) {
                // Forward || Backward
                if (this.forward.get()) PILOT.forward();
                else PILOT.backward();

                this.setSpeed(this.speed);
            } else if (!this.running.get() && this.PILOT.isMoving() && !this.avoid.get()) { // Stop moving
                PILOT.stop();
            }

            // Turning in an arc
            if (this.turning.get() && !this.avoiding.get()) {
                if (Motor.A.getSpeed() == Motor.D.getSpeed()) this.turnRight();
            } else {
                this.setSpeed(this.speed);
            }

            // Avoid obstacle
            if (this.avoiding.get()) {
                
            }

            // Avoidance
            if (this.avoid.get()) {
                this.rotate(90);
                avoid.set(false);
            }

            // Delay by 100ms
            Delay.msDelay(100);

        }

    }

    // Start motors
    public void startMotors() {
        this.running.set(true);
    }

    // Stop motors
    public void stopMotors() {
        this.running.set(false);
    }

    // Go forward
    public void goForward() {
        this.forward.set(true);
    }

    // Go backward
    public void goBack() {
        this.forward.set(false);
    }

    // Turn
    public void turn() {
        this.turning.set(true);
    }

    // End turn
    public void endTurn() {
        this.turning.set(false);
    }

    public void avoidingObstacle() {
        this.avoiding.set(true);
    }

    // Avoid obstacle
    public void avoidObstacle() {
        this.running.set(false);
        this.avoid.set(true);
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

    // Slow down when obstacle getting close
    public void slowDown() {
        if (!slowedDown) {
            this.slowedDown = true;
            changeSpeedBy(-25);
        }
    }

    public void speedUp() {
        if (slowedDown) {
            this.slowedDown = false;
            changeSpeedBy(50);
        }
    }

    // Turn Right
    private void turnRight() {
        Motor.A.setSpeed(this.speed / 2f);
        Motor.D.setSpeed(this.speed * 2f);
    }

    // Turn Left
    private void turnLeft() {
        Motor.A.setSpeed(this.speed * 2f);
        Motor.D.setSpeed(this.speed / 2f);
    }

    // Rotates robot by 'degrees'
    private void rotate(int degrees) {
        this.PILOT.rotate(degrees);
        this.running.set(true);
    }

}
