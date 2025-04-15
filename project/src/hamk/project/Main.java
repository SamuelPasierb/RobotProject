package hamk.project;

import hamk.project.LCD.LCDClass;
import hamk.project.Motors.LeftMotor;
import hamk.project.Motors.MotorClass;
import hamk.project.Motors.RightMotor;
import hamk.project.Sensors.LightSensor;
import hamk.project.Sensors.UltraSonic;
import lejos.hardware.Button;
import lejos.utility.Delay;

public class Main {

    private static UltraSonic ultraSonic;
    private static LCDClass lcd;
    private static LightSensor lightSensor;
    public static LeftMotor leftMotor;
    public static RightMotor rightMotor;

    public static void main(String[] args) {

        // Threads
        ultraSonic = new UltraSonic();
        lcd = new LCDClass();
        lightSensor = new LightSensor();
        leftMotor = new LeftMotor();
        rightMotor = new RightMotor();

        // Start threads
        ultraSonic.start();
        lcd.start();
        lightSensor.start();
        leftMotor.start();
        rightMotor.start();

        goForward(150);

        startMotors();

        Delay.msDelay(3000);
        goForward(600);
        

        while (!Button.ESCAPE.isDown()) {           

            // Speed up
            if (Button.UP.isDown()) {
                leftMotor.changeSpeedBy(10);
                rightMotor.changeSpeedBy(10);
            }

            // Slow down
            if (Button.DOWN.isDown()) {
                leftMotor.changeSpeedBy(-10);
                rightMotor.changeSpeedBy(-10);
            }

            // // Turn right
            // if (Button.RIGHT.isDown()) {
            //     turnRight(30, 1000);
            //     Delay.msDelay(750);
            // }

            // // Turn left
            // if (Button.LEFT.isDown()) {
            //     turnLeft(-30, 1000);
            //     Delay.msDelay(750);
            // }

            Delay.msDelay(250);

        }
    
        ultraSonic.interrupt();
        lcd.interrupt();
        lightSensor.interrupt();
        leftMotor.interrupt();
        rightMotor.interrupt();

    }

    public static void startMotors() {
        leftMotor.startMotor();
        rightMotor.startMotor();
    }

    public static void stopMotors() {
        leftMotor.stopMotor();
        rightMotor.stopMotor();
    }

    // Go forward in a straight line at 'speed' 
    private static void goForward(int speed) {
        leftMotor.changeSpeedTo(speed);
        rightMotor.changeSpeedTo(speed);
    }

    // Turn the robot to the left by 'degrees' 
    private static void turnLeft(int degrees) {
        leftMotor.turnDegrees.set(degrees);
        rightMotor.turnDegrees.set(degrees);
    }

    // Turn the robot to the left by 'degrees' for 'time'
    private static void turnLeft(int degrees, int time) {
        leftMotor.turnDegrees.set(degrees);
        rightMotor.turnDegrees.set(degrees);
        MotorClass.turnTime.set(time);
    }

    // Turn the robot to the left by 'degrees' 
    private static void turnRight(int degrees) {
        leftMotor.turnDegrees.set(degrees);
        rightMotor.turnDegrees.set(degrees);
    }

    // Turn the robot to the left by 'degrees' for 'time'
    private static void turnRight(int degrees, int time) {
        leftMotor.turnDegrees.set(degrees);
        rightMotor.turnDegrees.set(degrees);
        MotorClass.turnTime.set(time);
    }

    private static void rotation(int degrees) {
        leftMotor.turnDegrees.set(degrees);
        rightMotor.turnDegrees.set(-degrees);
    }
}