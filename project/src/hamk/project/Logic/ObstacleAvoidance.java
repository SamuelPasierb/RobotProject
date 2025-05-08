package hamk.project.Logic;

// Imports
import hamk.project.Main;

/**
 * <h3>Class of Obstacle avoidance</h3>
 * This class contains logic for avoiding obstacles.
 * @author Samuel Pasierb
 */
public class ObstacleAvoidance {

    
    // Robot
    // private final int WIDTH = 10;
    // private final int SAFE_ZONE = 5;
    // private final int FULL_WIDTH = SAFE_ZONE + WIDTH + SAFE_ZONE;

    // Zones
    // private final int SCAN_ZONE = 50;
    public final float _AVOID_ZONE = 10;
    private final float _DEAD_ZONE = 5;

    private final int ACCELERATION = 100;

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
     * Robot will go at his usual speed if {@code distance} is more that {@code AVOID_ZONE} and {@code DEAD_ZONE} values.
     * </p>
     */
    public void avoid(float distance, String type, float speed) {

        float DEAD_ZONE = speed / this.ACCELERATION * this._DEAD_ZONE;
        float AVOID_ZONE = speed / this.ACCELERATION * this._AVOID_ZONE;

        // STOP!
        if (type.equals("STOP")) {
            if (distance < DEAD_ZONE) Main.getPilot().stopMotors();
            else if (!Main.getPilot().isMoving()) Main.getPilot().startMotors();
        } else if (distance < AVOID_ZONE && type.equals("TURN_AROUND")) {
            Main.getPilot().stopMotors();
            Main.getPilot().rotate(180);
            Main.getPilot().startMotors();
        } else if (distance < AVOID_ZONE && type.equals("GO_AROUND")) {
            Main.getPilot().avoid();
        }

        // Will crash
        // else if (distance < AVOID_ZONE) {
        //     Main.getPilot().avoidingObstacle();
        // }

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
