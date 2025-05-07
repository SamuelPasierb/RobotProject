package hamk.project;

// Imports
import lejos.utility.Delay;
import lejos.hardware.Button;

import hamk.project.LCD.LCDClass;
import hamk.project.Motors.Pilot;
import hamk.project.Sensors.Light;
import hamk.project.Sensors.UltraSonic;
import hamk.project.WebService.ReadData;
import hamk.project.WebService.WriteData;

public class Main {

    private static UltraSonic ultraSonic;
    private static Light light;
    private static LCDClass lcd;
    private static Pilot pilot;
    private static ReadData lego;
    private static WriteData writer;

    public static void main(String[] args) {

        // Threads
        ultraSonic = new UltraSonic();
        light = new Light();
        lcd = new LCDClass();
        pilot = new Pilot();
        lego = new ReadData();
        writer = new WriteData();

        // Wait
        Button.waitForAnyPress();

        // Start threads
        ultraSonic.start();
        
        
        // light.start();
        
        lcd.start();
        pilot.start();
        lego.start();
        writer.start();

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
        // light.interrupt();
        lcd.interrupt();
        pilot.interrupt();
        lego.interrupt();
        writer.interrupt();

    }

    /**
      * 
      * @return {@code pilot} value
      */
    public static Pilot getPilot() {
        return pilot;
    }

    public static void lineFollowerSwitch(boolean _light) {
        if (_light) {
            if (!light.isAlive()) {
                    light = new Light(); 
                    light.start();
                }
        } else {
            light.interrupt();
        }
    }

    public static String values() {
        return "reflection=" + Light.getCurrentReflection() + "&" + "distance=" + UltraSonic.getDistance();
    }
    
}
