package hamk.project.LCD;

import java.util.concurrent.atomic.AtomicReference;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class LCDClass extends Thread {

    public static AtomicReference<String> distance;
    public static AtomicReference<String> leftSpeed;
    public static AtomicReference<String> rightSpeed;
    public static AtomicReference<String> light;
    private final int distanceY = 0;
    private final int speedY = 1;
    private final int lightY = 2;

    public LCDClass() {

        // Distance
        distance = new AtomicReference<>();
        distance.set("");

        // Speeds
        leftSpeed = new AtomicReference<>(); rightSpeed = new AtomicReference<>();
        leftSpeed.set(""); rightSpeed.set("");

        // light
        light = new AtomicReference<>();
        light.set("");
    }

    @Override
    public void run() {
    
        LCD.drawString("Starting robot.", 0, 0);

        while (!this.isInterrupted()) {
            
            // 500 ms delay
            Delay.msDelay(500);

            // Update LCD
            LCD.clear();
            LCD.drawString(distance.get(), 0, this.distanceY);
            LCD.drawString(leftSpeed.get(), 0, this.speedY);
            LCD.drawString(rightSpeed.get(), 7, this.speedY);
            LCD.drawString(light.get(), 0, this.lightY);

        }

    }

}
