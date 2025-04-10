package hamk.project.LCD;

import java.util.concurrent.atomic.AtomicReference;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class LCDClass extends Thread {

    public static AtomicReference<String> distance;
    private final int distanceY = 0;

    public LCDClass() {
        distance = new AtomicReference<>();
        distance.set("");
    }

    @Override
    public void run() {
    
        LCD.drawString("Starting robot.", 0, 0);

        while (!this.isInterrupted()) {
            
            // 500 ms delay
            Delay.msDelay(500);

            LCD.clear();
            LCD.drawString(distance.toString(), 0, this.distanceY);

        }

    }

}
