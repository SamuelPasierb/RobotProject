package hamk.project.LCD;

// Imports
import java.util.concurrent.atomic.AtomicReference;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

/**
 * <h3>Class for LCD.</h3>
 * Extends {@link Thread} so it can run without disturbing other more important functions of the robot. 
 * @author Artjom Smorgulenko, Samuel Pasierb
 */
public class LCDClass extends Thread {

    // Atomic strings that show up on the display
    public static AtomicReference<String> distance;
    public static AtomicReference<String> reflection;
    public static AtomicReference<String> speed;

    // Row indexes
    private final int distanceY = 0;
    private final int reflectionY = 1;
    private final int speedY = 2;

    // Constructor
    public LCDClass() {

        // Start text
        LCD.drawString("Press a button", 2, 3);
        LCD.drawString("to start.", 4, 4);

        // Distance
        distance = new AtomicReference<>("");

        // Reflection
        reflection = new AtomicReference<>("");

        // Speed
        speed = new AtomicReference<>("");

    }


    @Override
    public void run() {
        
        // Until the progam ends
        while (!this.isInterrupted()) {
            
            // 50ms delay
            Delay.msDelay(50);

            // Update LCD
            LCD.clear();
            LCD.drawString(distance.get(), 0, this.distanceY);
            LCD.drawString(reflection.get(), 0, this.reflectionY);
            LCD.drawString(speed.get(), 0, this.speedY);

        }

    }

}
