package hamk.project;

// Imports
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

    public static String avoidType = "STOP";

    public static void main(String[] args) {

        // Threads
        ultraSonic = new UltraSonic();
        light = new Light();
        lcd = new LCDClass();
        pilot = new Pilot();
        lego = new ReadData();
        writer = new WriteData();

        // Start motors
        pilot.setSpeed(0, 0);
        pilot.startMotors();

        // Wait
        Button.waitForAnyPress();

        // Start threads
        ultraSonic.start();
        light.start();
        lcd.start();
        pilot.start();
        lego.start();
        writer.start();

        while (!Button.ESCAPE.isDown()) {           

        }
    
        ultraSonic.interrupt();
        light.interrupt();
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
        light.followerOn = _light;
    }

    public static void avoidanceType(String type) {
        if (type.equals("Stop")) {

        }
    }

    public static String values() {
        return "speed=" + getPilot().getSpeed() + "&" + "reflection=" + Light.getCurrentReflection() + "&" + "distance=" + UltraSonic.getDistance() + "&" + "avoid=" + avoidType + "&" + "light=" + light.followerOn;
    }

}
