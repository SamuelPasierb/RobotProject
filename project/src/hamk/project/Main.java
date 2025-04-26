package hamk.project;

//import from package
import hamk.project.LCD.LCDClass;
import hamk.project.Motors.Pilot;
import hamk.project.Sensors.Light;
import hamk.project.Sensors.UltraSonic;
import lejos.hardware.Button;

import lejos.utility.Delay;

public class Main {

    private static UltraSonic ultraSonic;
    private static Light light;
    private static LCDClass lcd;
    private static Pilot pilot;

    public static void main(String[] args) {

        // Threads
        ultraSonic = new UltraSonic();
        light = new Light();
        lcd = new LCDClass();
        pilot = new Pilot();

        if (Button.waitForAnyPress() == Button.LEFT.getId()) pilot.left = -1;

        // Start threads
        ultraSonic.start();
        light.start();
        lcd.start();
        pilot.start();

        // Start movement
        pilot.startMotors();
        pilot.setSpeed(200, 200);

        while (!Button.ESCAPE.isDown()) {           

            // Speed up
            if (Button.UP.isDown()) {
                pilot.changeSpeedBy(10);
            }

            // Slow down
            if (Button.DOWN.isDown()) {
                pilot.changeSpeedBy(-10);
            }

            // Delay by 20ms
            Delay.msDelay(20);

        }
    
        ultraSonic.interrupt();
        light.interrupt();
        lcd.interrupt();
        pilot.interrupt();

    }

    public static Pilot getPilot() {
        return pilot;
    }

}
