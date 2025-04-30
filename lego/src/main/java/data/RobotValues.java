package data;

public class RobotValues {

    private static int id;
    private static int speed;
    private static int turn;
    private static boolean run = true;

    private static final int MAX_SPEED = 500;
    private static final int MIN_SPEED = 0;

    public static int getId() {
        return id;
    }

    public static void setId(int _id) {
        id = _id;
    }

    public static int getSpeed() {
        return speed;
    }

    public static boolean setSpeed(int _speed) {
        
        // Can't use this speed
        if (_speed < MIN_SPEED || _speed > MAX_SPEED) {
            return false;
        }
        
        speed = _speed;
        return true;
    }

    public static int getTurn() {
        return turn;
    }

    public static void setTurn(String _turn) {
        switch (_turn) {
            case "LEFT":
                turn = 1;
                break;
            case "RIGHT":
                turn = -1;
                break;
            default:
                turn = 0;
                break;
        }
    }

    public static boolean isRunning() {
        return run;
    }

    public static void setRun(boolean _run) {
        run = _run;
    }

    public static String stringify() {
        return speed + "#" + turn;
    } 

}
