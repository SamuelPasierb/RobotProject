package data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RobotValues {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @SuppressWarnings("unused")
    private int speed = 0;
    @SuppressWarnings("unused")
    private String turn = "Straight";
    @SuppressWarnings("unused")
    private boolean lightFollower = false;
    private float avoid;
    
    private static int _speed = 0;
    private static Turn _turn = Turn.STRAIGHT;
    private static boolean _lightFollower = false;
    private static float _avoid;

    private final static int MAX_SPEED = 500;
    private final static int MIN_SPEED = 0;


    public RobotValues() {
        super();
        this.speed = _speed;
        this.turn = _turn.name;
        this.lightFollower = _lightFollower;
        this.avoid = _avoid;
    }

    public RobotValues(int speed, Turn turn, boolean lightFollower, float avoid) {
        super();
        this.speed = speed;
        this.turn = turn.name;
        this.lightFollower = lightFollower;
        this.avoid = avoid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getSpeed() {
        return _speed;
    }

    public static boolean setSpeed(int speed) {
        
        // Can't use this speed
        if (speed < MIN_SPEED || speed > MAX_SPEED) {
            return false;
        }
        
        _speed = speed;
        return true;
    }

    public static Turn getTurn() {
        return _turn;
    }

    public static void setTurn(Turn turn) {
        _turn = turn;
    }

    public static void setLight(boolean lightFollower) {
        _lightFollower = lightFollower;
    }
    
    public static boolean getLight() {
        return _lightFollower;
    }

    public void setAvoid(float avoid) {
        _avoid = avoid;

    }
    public static float getAvoid() {
        return _avoid;
    }

    public static String stringify() {
        return _speed + "#" + _turn.value + "#" + _lightFollower;
    } 

    

}
