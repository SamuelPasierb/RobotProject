package hamk.project;

import hamk.project.LCD.LCDClass;
import hamk.project.Sensors.UltraSonic;
import lejos.hardware.port.SensorPort;

public class Main {
    public static void main(String[] args) {

        // Threads
        UltraSonic ultraSonic = new UltraSonic(SensorPort.S2);
        LCDClass lcd = new LCDClass();

        // Start threads
        ultraSonic.start();
        lcd.start();
    
    }
}