package hamk.project.LCD;

import java.util.concurrent.atomic.AtomicReference;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class LCDClass extends Thread {

    public static AtomicReference<String> distance;
    public static AtomicReference<String> reflection;
    public static AtomicReference<String> speed;
    private final int distanceY = 0;
    private final int reflectionY = 1;
    private final int speedY = 2;

    public LCDClass() {

        // Distance
        distance = new AtomicReference<>("");

        // Reflection
        reflection = new AtomicReference<>("");

        // Speed
        speed = new AtomicReference<>("");

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
            LCD.drawString(reflection.get(), 0, this.reflectionY);
            LCD.drawString(speed.get(), 0, this.speedY);

        }

    }

}
