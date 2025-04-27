package hamk.project.Motors;

// Import 
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

import hamk.project.LCD.LCDClass;
import hamk.project.Logic.ObstacleAvoidance;
import hamk.project.Sensors.Light;

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
    private float leftSpeed = 0;
    private float rightSpeed = 0;

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
     * <h4>Explanation: </h4>
     * <ol>
     *  <li>If the robot is currently avoiding an obstacle don't do anything</li>
     *  <li>If the robot should be running, but is not moving start moving</li>
     *  <li>If the robot is moving but shouldn't stop him</li>
     *  <li>If the robot needs to turn make him turn in a wanted direction othewise make him go straight</li>
     *  <li>If there is an obstacle that needs to be avoided, avoid it</li>
     * </ol>
     */
    @Override
    public void run() {
        
        this.PILOT.setLinearAcceleration(300);
        this.PILOT.setAngularAcceleration(300);

        while (!this.isInterrupted()) {

            if (this.avoidThread.get()) {
              continue;
            }

            // Should be running
            if (this.running.get() && !this.PILOT.isMoving()) {
                
                this.setSpeed(this.leftSpeed, this.rightSpeed);

                // Forward || Backward
                if (this.forward.get()) PILOT.forward();
                else PILOT.backward();

            } else if (!this.running.get() && this.PILOT.isMoving()) { // Stop moving
                PILOT.stop();
            }

            // Follow line
            if (!this.turning.get().isEmpty() && !this.avoiding.get()) {
                if (leftMotor.getSpeed() == rightMotor.getSpeed()) {
                    if (this.turning.get().equals("RIGHT")) this.turnRight();
                    else this.turnLeft();
                }
            } else {
                this.setSpeed(this.leftSpeed, this.rightSpeed);
            }

            // Avoid obstacle
            if (this.avoiding.get()) {
                this.avoiding.set(false);
                this.avoidThread.set(true);
                ObstacleAvoidance.avoidObstacle().start();
            }

            // Delay by 20ms
            // Delay.msDelay(20);

        }

    }

    /**
      * <h3>Start motors</h3>
      * <p>
      * Sets {@code running} to {@code true}
      * </p>
      */
    public void startMotors() {
        this.running.set(true);
    }

    /**
      * <h3>Stop motors</h3>
      * <p>
      * Sets {@code running} to {@code false}
      * </p>
      */
    public void stopMotors() {
        this.running.set(false);
        this.leftMotor.setSpeed(0); this.rightMotor.setSpeed(0);
    }

    /**
     * <h3>Are motors running method.</h3>
     * @return {@code running} value
     */
    public boolean motorsRunning() {
        return this.running.get();
    }

    /**
      * <h3>Go forward</h3>
      * <p>
      * Sets {@code forward} to {@code true}
      * </p>
      */
    public void goForward() {
        this.forward.set(true);
    }

    /**
      * <h3>Go backward</h3>
      * <p>
      * Sets {@code forward} to {@code false}
      * </p>
      */
    public void goBack() {
        this.forward.set(false);
    }

    /**
      * <h3>Turn</h3>
      * 
      * @param side - turning side, could be {@code "LEFT"} or {@code "RIGHT"}
      * 
      * <p>
      * Sets {@code turning} to the specific side
      * </p>
      */
    public void turn(String side) {
        this.turning.set(side);
    }

    /**
      * <h3>End turn</h3>
      * <p>
      * Sets {@code turning} to {@code ""} 
      * </p>
      * <p>
      * Resets the {@code turning} value
      * </p>
      */
    public void endTurn() {
        this.turning.set("");
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
    * <h3>Setting speed</h3>
    * 
    * @param leftSpeed  - speed for the left wheel
    * 
    * 
    * @param rightSpeed - speed for the right wheel
    * 
    * <p>
    * Sets {@code setSpeed} value with {@code leftSpeed} and {@code rightSpeed}
    * </p>
    * <p>
    * Also sets {@code update} value to {@code true}
    * </p>
    */
    public void setSpeed(float leftSpeed, float rightSpeed) {
        this.setSpeed(leftSpeed, rightSpeed, true);
    }

    /** 
      * <h3>Setting speed</h3>
      * 
      * @param leftSpeed - speed for the left wheel
      * @param rightSpeed - speed for the right wheel
      * @param update - whether to update the speed or the value is just temporary (for turning)
      * 
      * <p>
      * Sets speed for the wheels.
      * </p>
      * Updates LCD Screen with sum of {@code leftSpeed} and {@code rightSpeed} divided by 2
      * 
      * <p>
      * {@code LCDClass.speed.set("Speed: " + ((leftSpeed + rightSpeed) / 2));}
      * </p>
      * */ 
    public void setSpeed(float leftSpeed, float rightSpeed, boolean update) {
        
        if (update) {
            this.leftSpeed = leftSpeed;
            this.rightSpeed = rightSpeed;
        }

        LCDClass.speed.set("Speed: " + ((int) (leftSpeed + rightSpeed) / 2));
        
        leftMotor.setSpeed(leftSpeed);
        rightMotor.setSpeed(rightSpeed);
    }

    /**
      * <h3>Changing speed by 'speed'</h3>
      * @param speed - General speed for the motors.
      * 
      * <p>
      * Increases both left and right motors speed If sum of {@code leftSpeed} and {@code speed} and also {@code rightSpeed} and {@code speed} more than 0
      * </p>
      * 
      */
    public void changeSpeedBy(int speed) {
        if (this.leftSpeed + speed > 0 && this.rightSpeed + speed > 0) this.setSpeed(this.leftSpeed + speed, this.rightSpeed + speed);
    }

    /**
      * <h3>Turn left</h3>
      * 
      * Divides {@code leftSpeed} value by 1.4 and multiplies {@code rightSpeed} by 1.4 for the smooth turning left.
      * <p>
      * Sets {@code update} value to {@code false}
      * </p>
      */
    private void turnLeft() {
        this.setSpeed(this.leftSpeed / 1.4f, this.rightSpeed * 1.4f, false);
    }

    /**
      * <h3>Turn right</h3>
      * 
      * Multiplies {@code leftSpeed} value by 1.4 and divides {@code rightSpeed} by 1.4 for the smooth turning right.
      * <p>
      * Sets {@code update} value to {@code false}
      * </p>
      */
    private void turnRight() {
        this.setSpeed(this.leftSpeed * 1.4f, this.rightSpeed / 1.4f, false);
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
        this.PILOT.setAngularSpeed(200);
        this.PILOT.setLinearSpeed(500);

        // Around
        this.PILOT.arc(-200, 60);
        this.PILOT.travel(200);
        this.PILOT.arc(75, 105);

        // Go back towards the line
        this.PILOT.travel(200);

        // Look for the line
        this.PILOT.setLinearSpeed(75);
        this.PILOT.forward();

        // Until the robot finds the line
        while (Light.getCurrentReflection() > Light.BORDER && !Button.ESCAPE.isDown()) {
            
        }

        // Go back a little bit
        this.PILOT.stop();
        this.PILOT.arc(35, -40);
        
        // Finished avoiding
        this.avoidThread.set(false);
        this.avoiding.set(false);
    }

}
