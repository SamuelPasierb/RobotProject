package hamk.project.Logic;

import hamk.project.Main;

public class ObstacleAvoidance {
    
    // Robot
    private final int WIDTH = 10;
    private final int SAFE_ZONE = 5;
    private final int FULL_WIDTH = SAFE_ZONE + WIDTH + SAFE_ZONE;

    // Zones
    private final int SLOW_DONE_ZONE = 40;
    private final int WILL_CRASH_ZONE = 25;
    private final int DEAD_ZONE = 5;

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
        else if (distance < WILL_CRASH_ZONE) {
            Main.getPilot().avoidingObstacle();
        }

        // Slow down
        else if (distance < SLOW_DONE_ZONE) {
            Main.getPilot().slowDown();
        }

        // Speed up
        else {
            if (!Main.getPilot().motorsRunning()) Main.getPilot().startMotors();
            Main.getPilot().speedUp();
        }

    }

    // Thread for avoiding obstacle
    public static Thread avoidObstacle() {
        return new Thread(runnable);
    }

}
