package hamk.project.Logic;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import hamk.project.Main;

public class ObstacleAvoidance {
    
    // Robot
    private final int WIDTH = 10;
    private final int SAFE_ZONE = 5;
    private final int FULL_WIDTH = SAFE_ZONE + WIDTH + SAFE_ZONE;

    // Zones
    private final int SCAN_ZONE = 50;
    public static final int AVOID_ZONE = 35;
    private final int DEAD_ZONE = 8;

    private final static Runnable runnable = new Runnable() {
        @Override
        public void run() { Main.getPilot().avoid(); }
    };

    public void avoid(float distance) {
        
        // STOP!
        if (distance < DEAD_ZONE) {
            Main.getPilot().stopMotors();
        }

        // Will crash
        else if (distance < AVOID_ZONE) {
            Main.getPilot().avoidingObstacle();
        }

        // Slow down
        else if (distance < SCAN_ZONE) {
            // this.scanning = true;
            // this.middleDist = distance;
            // Main.getPilot().scan();
        }

        // Speed up
        else {
            if (!Main.getPilot().motorsRunning()) Main.getPilot().startMotors();
        }

    }

    // Thread for avoiding obstacle
    public static Thread avoidObstacle() {
        return new Thread(runnable);
    }

}
