package hamk.project.Motors;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import hamk.project.LCD.LCDClass;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class Pilot extends Thread {
    
    // Constants
    private final int WHEEL_DIAMETER = 56;
    private final int WHEELBASE = 100; 
    private final EV3LargeRegulatedMotor leftMotor;
    private final EV3LargeRegulatedMotor rightMotor;
    private final Wheel leftWheel;
    private final Wheel rightWheel;
    private final MovePilot PILOT;

    // Speed
    private int leftSpeed = 0;
    private int rightSpeed = 0;

    // Atomic values
    private AtomicBoolean running;
    private AtomicBoolean forward;
    private AtomicBoolean avoid;
    private AtomicReference<String> turning;
    private AtomicInteger turnDegrees;

    public Pilot() {
        
        // Motors
        this.leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
        this.rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);

        // Wheels
        this.leftWheel = WheeledChassis.modelWheel(leftMotor, WHEEL_DIAMETER).offset(-50);
        this.rightWheel = WheeledChassis.modelWheel(rightMotor, WHEEL_DIAMETER).offset(50); 

        // Pilot
        this.PILOT = new MovePilot(new WheeledChassis(new Wheel[] {leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL));

        // Atomic values
        this.running = new AtomicBoolean(false);
        this.forward = new AtomicBoolean(true);
        this.avoid = new AtomicBoolean(false);
        this.turning = new AtomicReference<String>("");
        this.turnDegrees = new AtomicInteger(0);
    }

    @Override
    public void run() {
        
        this.PILOT.setLinearAcceleration(200);
        this.PILOT.setAngularAcceleration(200);

        while (!this.isInterrupted()) {

            // Running
            if (this.running.get() && !this.PILOT.isMoving()) {
                
                this.setSpeed(this.leftSpeed, this.rightSpeed);

                // Forward || Backward
                if (this.forward.get()) PILOT.forward();
                else PILOT.backward();

            } else if (!this.running.get() && this.PILOT.isMoving() && !this.avoid.get()) { // Stop moving
                PILOT.stop();
            }

            // Turning in an arc
            if (!this.turning.get().isEmpty()) {
                if (leftMotor.getSpeed() == rightMotor.getSpeed()) {
                    if (this.turning.get().equals("RIGHT")) this.turnRight();
                    else this.turnLeft();
                }
            } else {
                this.setSpeed(this.leftSpeed, this.rightSpeed);
            }

            // Avoidance
            if (this.avoid.get()) {
                this.rotate(90);
                avoid.set(false);
            }

            // Delay by 20ms
            Delay.msDelay(20);

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
    public void turn(String side) {
        this.turning.set(side);
    }

    // End turn
    public void endTurn() {
        this.turning.set("");
    }

    // Avoid obstacle
    public void avoidObstacle() {
        this.running.set(false);
        this.avoid.set(true);
    }

    // Setting speed
    public void setSpeed(int leftSpeed, int rightSpeed) {
        this.setSpeed(leftSpeed, rightSpeed, true);
    }

    // Setting speed
    public void setSpeed(int leftSpeed, int rightSpeed, boolean update) {
        
        if (update) {
            this.leftSpeed = leftSpeed;
            this.rightSpeed = rightSpeed;
        }

        LCDClass.speed.set("Speed: " + ((leftSpeed + rightSpeed) / 2));
        
        leftMotor.setSpeed(leftSpeed);
        rightMotor.setSpeed(rightSpeed);
    }

    // Changing speed by 'speed'
    public void changeSpeedBy(int speed) {
        if (this.leftSpeed + speed > 0 && this.rightSpeed + speed > 0) this.setSpeed(this.leftSpeed + speed, this.rightSpeed + speed);
    }

    // Turn Right
    private void turnRight() {
        this.setSpeed(1, this.rightSpeed * 3, false);
    }

    // Turn Left
    private void turnLeft() {
        this.setSpeed(this.leftSpeed * 3, 1, false);
    }

    // Rotates robot by 'degrees'
    private void rotate(int degrees) {
        this.PILOT.rotate(degrees);
        this.running.set(true);
    }

}
