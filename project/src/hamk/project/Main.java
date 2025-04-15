package hamk.project;

import hamk.project.LCD.LCDClass;
import hamk.project.Motors.Pilot;
import hamk.project.Sensors.UltraSonic;
import lejos.hardware.Button;

import lejos.utility.Delay;

public class Main {

    private static UltraSonic ultraSonic;
    private static LCDClass lcd;
    private static Pilot pilot;

    public static void main(String[] args) {

        // Threads
        ultraSonic = new UltraSonic();
        lcd = new LCDClass();
        pilot = new Pilot();

        // Start threads
        ultraSonic.start();
        lcd.start();
        pilot.start();
        // leftMotor.start();
        // rightMotor.start();

        pilot.setSpeed(200);
        pilot.startMotors();

        Delay.msDelay(1000);

        pilot.setSpeed(400);
        

        while (!Button.ESCAPE.isDown()) {           

            // Speed up
            if (Button.UP.isDown()) {
                pilot.changeSpeedBy(10);
            }

            // Slow down
            if (Button.DOWN.isDown()) {
                pilot.changeSpeedBy(-10);
            }

            // Delay by 100ms
            Delay.msDelay(100);

        }
    
        ultraSonic.interrupt();
        lcd.interrupt();
        pilot.interrupt();

    }

}