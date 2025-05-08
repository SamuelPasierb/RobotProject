package data;

import java.text.DecimalFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RobotValues {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @SuppressWarnings("unused") private int speed = 0;
    @SuppressWarnings("unused") private int turn = 0;
    @SuppressWarnings("unused") private float distance;
    @SuppressWarnings("unused") private float reflection;
    @SuppressWarnings("unused") private boolean lightFollower = false;
    @SuppressWarnings("unused") private String avoidance = "STOP";
    @SuppressWarnings("unused") private long time = 0;
    
    private static int _speed = 0;
    private static int _turn = 0;
    private static float _distance;
    private static float _reflection;
    private static boolean _lightFollower = false;
    private static String _avoidance = "STOP";
    private static long _time = 0;

    private final static int MAX_SPEED = 500;
    private final static int MIN_SPEED = 0;

    private final static DecimalFormat df = new DecimalFormat("#.##");

    public RobotValues() {
        super();
        this.speed = _speed;
        this.turn = _turn;
        this.lightFollower = _lightFollower;
        this.distance = _distance;
        this.reflection = _reflection;
        this.avoidance = _avoidance;
        this.time = _time;
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

    public static void setSpeed(int speed) {
        
        // Can use this speed
        if (speed > MIN_SPEED && speed < MAX_SPEED) {
            _speed = speed;
        }

    }

    public static int getTurn() {
        return _turn;
    }

    public static void setTurn(int turn) {

        // Can use
        if (turn >= -5 && turn <= 5) {
            _turn = turn;
        }

    }

    public static void setLight(boolean lightFollower) {
        _lightFollower = lightFollower;
    }
    
    public static boolean getLight() {
        return _lightFollower;
    }

    public static void setDistance(float distance) {
        _distance = distance;

    }
    public static float getDistance() {
        return _distance;
    }

    public static void setReflection(float reflection) {
        _reflection = reflection;
    }

    public static float getReflection() {
        return _reflection;
    }

    public static void setAvoidance(String avoidance) {
        _avoidance = avoidance;
    }

    public static String getAvoidance() {
        return _avoidance;
    }

    public static void setTime(long time) {
        _time = time;
    }

    public static long getTime() {
        return _time;
    }

    // Used for robot reading the data
    public static String stringify() {
        return _speed + "#" + _turn + "#" + _lightFollower + "#" + _avoidance;
    } 

    // Showing robot data from the sensors and time alive
    @Override
    public String toString() {
        return String.format("Distance: %s%nReflection: %s%nTime Alive: %s seconds", df.format(_distance), df.format(_reflection), String.valueOf(_time));
    }    

}
