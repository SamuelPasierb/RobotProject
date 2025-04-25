package hamk.project.Motors;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

import hamk.project.LCD.LCDClass;
import hamk.project.Logic.ObstacleAvoidance;

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
    private float leftSpeed = 0;
    private float rightSpeed = 0;
    private boolean slowedDown = false;

    // Atomic values
    private AtomicBoolean running;
    private AtomicBoolean forward;
    private AtomicBoolean avoid;
    private AtomicBoolean avoiding;
    private AtomicBoolean avoidThread;
    private AtomicReference<String> turning;

    // Avoid direction
    public int left = 1;

    public Pilot() {
        
        // Motors
        this.leftMotor = new EV3LargeRegulatedMotor(MotorPort.D);
        this.rightMotor = new EV3LargeRegulatedMotor(MotorPort.A);

        // Wheels
        this.leftWheel = WheeledChassis.modelWheel(leftMotor, WHEEL_DIAMETER).offset(-WHEELBASE / 2);
        this.rightWheel = WheeledChassis.modelWheel(rightMotor, WHEEL_DIAMETER).offset(WHEELBASE / 2); 

        // Pilot
        this.PILOT = new MovePilot(new WheeledChassis(new Wheel[] {leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL));

        // Atomic values
        this.running = new AtomicBoolean(false);
        this.forward = new AtomicBoolean(true);
        this.avoid = new AtomicBoolean(false);
        this.turning = new AtomicReference<String>("");
        this.avoiding = new AtomicBoolean(false);
        this.avoidThread = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        
        this.PILOT.setLinearAcceleration(200);
        this.PILOT.setAngularAcceleration(200);

        while (!this.isInterrupted()) {

            // Running
            if (this.running.get() && !this.PILOT.isMoving() && !this.avoidThread.get()) {
                
                this.setSpeed(this.leftSpeed, this.rightSpeed);

                // Forward || Backward
                if (this.forward.get()) PILOT.forward();
                else PILOT.backward();

            } else if (!this.running.get() && this.PILOT.isMoving() && !this.avoid.get()) { // Stop moving
                PILOT.stop();
            }

            // Follow line
            if (!this.turning.get().isEmpty() && !this.avoiding.get() && !this.avoidThread.get()) {
                if (leftMotor.getSpeed() == rightMotor.getSpeed()) {
                    if (this.turning.get().equals("RIGHT")) this.turnRight();
                    else this.turnLeft();
                }
            } else if (!this.avoidThread.get()) {
                this.setSpeed(this.leftSpeed, this.rightSpeed);
            }

            // Avoid obstacle
            if (this.avoiding.get() && !this.avoidThread.get()) {
                this.avoiding.set(false);
                // TODO: decide whether to go left or right to go inside
                this.avoidThread.set(true);
                ObstacleAvoidance.avoidObstacle().start();
            }

            // Avoidance
            if (this.avoid.get()) {
                this.rotate(90);
                avoid.set(false);
            }

            // Delay by 20ms
            // Delay.msDelay(20);

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

    // Are motors running
    public boolean motorsRunning() {
        return this.running.get();
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

    public void avoidingObstacle() {
        this.avoiding.set(true);
    }

    // Setting speed
    public void setSpeed(float leftSpeed, float rightSpeed) {
        this.setSpeed(leftSpeed, rightSpeed, true);
    }

    // Setting speed
    public void setSpeed(float leftSpeed, float rightSpeed, boolean update) {
        
        if (update) {
            this.leftSpeed = leftSpeed;
            this.rightSpeed = rightSpeed;
        }

        LCDClass.speed.set("Speed: " + ((int) (leftSpeed + rightSpeed) / 2));
        
        leftMotor.setSpeed(leftSpeed);
        rightMotor.setSpeed(rightSpeed);
    }

    // Changing speed by 'speed'
    public void changeSpeedBy(int speed) {
        if (this.leftSpeed + speed > 0 && this.rightSpeed + speed > 0) this.setSpeed(this.leftSpeed + speed, this.rightSpeed + speed);
    }

    // Slow down when obstacle getting close
    public void slowDown() {
        if (!this.slowedDown) {
            this.slowedDown = true;
            setSpeed(150, 150, true);
        }
    }

    public void speedUp() {
        if (this.slowedDown) {
            this.slowedDown = false;
            setSpeed(200, 200, true);
        }
    }

    // Turn Left
    private void turnLeft() {
        this.setSpeed(this.leftSpeed / 1.4f, this.rightSpeed * 1.4f, false);
    }

    // Turn Right
    private void turnRight() {
        this.setSpeed(this.leftSpeed * 1.4f, this.rightSpeed / 1.4f, false);
    }

    // Rotates robot by 'degrees'
    public void rotate(int degrees) {
        this.PILOT.rotate(degrees);
        this.running.set(true);
    }

    public void avoid() {

        // Speed
        this.PILOT.setAngularSpeed(200);
        this.PILOT.setLinearSpeed(400);

        // Around
        // left is used to determine whether to make an arc to the left or to the right
        this.PILOT.arc(-250 * this.left, 45);
        this.PILOT.arc(250 * this.left, 90);
        this.PILOT.arc(-250 * this.left, 45);

        // Finished avoiding
        this.avoidThread.set(false);
        this.avoiding.set(false);
    }

}
