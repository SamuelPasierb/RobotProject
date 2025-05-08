package hamk.project.Motors;

// Import 
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

import hamk.project.LCD.LCDClass;
import hamk.project.Logic.ObstacleAvoidance;
import hamk.project.Sensors.Light;
import hamk.project.Sensors.UltraSonic;

/**
  * <h3>Pilot class for the EV3 motors.</h3>
  * This class handles the movement and rotating operations. 
  * <p>
  * Extends {@link Thread} so it can run without disturbing other more important functions of the robot.
  * </p>
  * @author Samuel Pasierb
  */
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
    private float speed = 0;

    // Atomic values
    private AtomicBoolean running;
    private AtomicBoolean forward;
    private AtomicBoolean avoiding;
    private AtomicBoolean avoidThread;
    private AtomicReference<String> turning;

    /**
      * <h3>Constructor to initialize Pilot.</h3>
      * <ul>
      * <li>Sets up the motors on A and D ports</li>
      * <li>Assigning the wheels</li>
      * <li>Creating an PILOT object</li>
      * <li>Assigning the atomic values for {@code running}, {@code forward}, {@code avoid}, {@code turning}, {@code avoiding}, {@code avoidThread}</li>
      * </ul>
      */
    public Pilot() {
        
        // Motors
        this.leftMotor = new EV3LargeRegulatedMotor(MotorPort.D);
        this.rightMotor = new EV3LargeRegulatedMotor(MotorPort.A);
        
        // Motor acceleration
        this.leftMotor.setAcceleration(100); this.rightMotor.setAcceleration(100);

        // Synchonize the motors
        this.leftMotor.synchronizeWith(new RegulatedMotor[] {this.rightMotor});

        // Wheels
        this.leftWheel = WheeledChassis.modelWheel(leftMotor, WHEEL_DIAMETER).offset(-WHEELBASE / 2);
        this.rightWheel = WheeledChassis.modelWheel(rightMotor, WHEEL_DIAMETER).offset(WHEELBASE / 2); 

        // Pilot
        this.PILOT = new MovePilot(new WheeledChassis(new Wheel[] {leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL));

        // Atomic values
        this.running = new AtomicBoolean(false);
        this.forward = new AtomicBoolean(true);
        this.turning = new AtomicReference<String>("");
        this.avoiding = new AtomicBoolean(false);
        this.avoidThread = new AtomicBoolean(false);
    }

    /**
     * <h3>Main movement method</h3>
     * <p>Responsible for keeping robot on the line and avoding obstacles.</p>
     * <p>Runs infinitely</p>
     */
    @Override
    public void run() {

        while (!this.isInterrupted()) {

            // Decides wheter go turn or go straight
            if (!this.turning.get().isEmpty()) {
                if (this.turning.get().equals("LEFT")) this.turnLeft();
                else this.turnRight();
            } else this.goStraight();

        }

    }

    public boolean isMoving() {
        return this.leftMotor.isMoving() && this.rightMotor.isMoving();
    }
    
    /**
      * <h3>Start motors</h3>
      * <p>
      * Sets {@code running} to {@code true}
      * </p>
      */
    public void startMotors() {
        this.running.set(true);
        this.leftMotor.startSynchronization();
        this.leftMotor.forward();
        this.rightMotor.forward();
        this.leftMotor.endSynchronization();
    }

    /**
      * <h3>Stop motors</h3>
      * <p>
      * Sets {@code running} to {@code false}
      * </p>
      */
    public void stopMotors() {
        this.running.set(false);
        this.leftMotor.startSynchronization();
        this.leftMotor.stop();
        this.rightMotor.stop();
        this.leftMotor.endSynchronization();
    }

    public float getSpeed() {
        return speed;
    }

    /**
     * <h3>Update speed</h3>
     * Updates the speed to {@code speed}
     * 
     * @param speed New speed
     */
    public void updateSpeed(int speed) {
        
        // Update the speed
        this.speed = speed;
    }

    /** 
      * <h3>Setting speed</h3>
      * Sets the speed for the wheels.
      * Updates LCD Screen 
      *
      * @param leftSpeed - speed for the left wheel
      * @param rightSpeed - speed for the right wheel
      */ 
    public void setSpeed(float leftSpeed, float rightSpeed) {
        
        // Determine wheter the motors need to be started
        boolean needToStart = this.leftMotor.getSpeed() == 0 || this.rightMotor.getSpeed() == 0;
        
        // Change speed
        this.leftMotor.startSynchronization();
        this.leftMotor.setSpeed(leftSpeed);
        this.rightMotor.setSpeed(rightSpeed);
        this.leftMotor.endSynchronization();

        // Start motors if they need to be started
        if (needToStart) this.startMotors();

        // Update lcd
        LCDClass.speed.set("Speed: " + this.speed);
    }

    /**
      * <h3>Turn</h3>
      * Sets {@code turning} to the specific side
      *
      * @param side - turning side, could be {@code "LEFT"} or {@code "RIGHT"}
      */
    public void turn(String side) {
        this.turning.set(side);
    }

    /**
      * <h3>End turn</h3>
      * Resets the {@code turning} value
      */
    public void endTurn() {
        this.turning.set("");
    }

    /**
      * <h3>Turn left</h3>
      * Makes the robot turn left
      */
    private void turnLeft() {
        this.setSpeed(this.speed / 1.4f, this.speed * 1.4f);
    }

    /**
      * <h3>Turn right</h3>
      * Makes the robot turn right
      */
    private void turnRight() {
        this.setSpeed(this.speed * 1.4f, this.speed / 1.4f);
    }

    /**
     * <h3>Go straight</h3>
     * Makes the robot go straight
     */
    private void goStraight() {
        this.setSpeed(this.speed, this.speed);
    }


    
    /**
      * <h3>Avoid obstacle</h3>
      * <p>
      * sets {@code avoid} to {@code true} 
      * </p>
      */
      public void avoidingObstacle() {
        this.avoiding.set(true);
    }
  
    /**
     * <h3>Avoid an obstacle</h3>
     * <ul>
     * <li>Sets the angular speed to 200 and linear speed to 500</li>
     * <li>Going around, moves in an 60° arc, moves 250mm forward and rotates back towards the line </li>
     * <li>Looking for the line, sets linear speed to 75 and going forward until the robot finds the line</li>
     * <li>Going back a little bit, stopping PILOT and rotating back to it's initial position</li>
     * <li>When finishes avoiding, sets {@code avoiding} and {@code avoidThread} values to {@code false}</li>
     * </ul>
     * 
     */
    public void avoid() {

      
        // Speed
        this.PILOT.setAngularSpeed(this.speed);
        this.PILOT.setLinearSpeed(this.speed);

        // Around
        this.PILOT.arc(-200, 60);
        this.PILOT.travel(200);
        this.PILOT.arc(75, 105);

        // Go back towards the line
        this.PILOT.travel(200);

        // Look for the line
        this.PILOT.forward();

        // Go back a little bit
        this.PILOT.stop();
        this.PILOT.arc(35, -40);
        
        // Finished avoiding
        this.avoidThread.set(false);
        this.avoiding.set(false);
     
      
    
  } 

    public void rotate(int angle) {
        this.leftMotor.startSynchronization();
        this.leftMotor.rotate(angle);
        this.rightMotor.rotate(-angle);
        this.leftMotor.endSynchronization();
    }

}
