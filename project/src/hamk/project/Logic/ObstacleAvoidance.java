package hamk.project.Logic;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import hamk.project.Main;

/**
 * <h3>Class of Obstacle avoidance</h3>
 * This class contains logic for avoiding obstacles.
 * @author Samuel Pasierb
 */
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

    /**
     * <h3>Method for the obstacle avoiding</h3>
     * @param distance - distance value
     * 
     * Contains a {@code DEAD_ZONE} and {@code AVOID_ZONE} values.
     * <p>
     * If {@code distance} less that {@code DEAD_ZONE} value, then motors will be stopped.
     * </p>
     * Also If {@code distance} less that {@code AVOID_ZONE} value, then {@code avoidingObstacle()} method will be called.
     * <p>
     * Robot will speed up if {@code distance} more that {@code AVOID_ZONE} and {@code DEAD_ZONE} values.
     * </p>
     */
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

    /**
     * <h3>Thread for avoiding obstacle</h3>
     * 
     * @return the value of {@code runnable} thread
     */
    public static Thread avoidObstacle() {
        return new Thread(runnable);
    }

}
