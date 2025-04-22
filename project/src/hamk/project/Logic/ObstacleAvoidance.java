package hamk.project.Logic;

import hamk.project.Main;

public class ObstacleAvoidance {
    
    // Robot
    private final int WIDTH = 10;
    private final int SAFE_ZONE = 5;
    private final int FULL_WIDTH = SAFE_ZONE + WIDTH + SAFE_ZONE;

    // Zones
    private final int SLOW_DONE_ZONE = 50;
    private final int WILL_CRASH_ZONE = 30; // 30cm, because half of the robot's width is 7.5cm and the biggest angle is 75° so: 7.5/cos(75) ~= 30
    private final int DEAD_ZONE = 5;

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
            Main.getPilot().speedUp();
        }

    }

}
