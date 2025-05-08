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
    @SuppressWarnings("unused") private int speed = 0;
    @SuppressWarnings("unused") private String turn = "Straight";
    @SuppressWarnings("unused") private float distance;
    @SuppressWarnings("unused") private float reflection;
    @SuppressWarnings("unused") private boolean lightFollower = false;
    @SuppressWarnings("unused") private String avoidance = "Stop";
    
    private static int _speed = 0;
    private static Turn _turn = Turn.STRAIGHT;
    private static float _distance;
    private static float _reflection;
    private static boolean _lightFollower = false;
    private static String _avoidance = "Stop";

    private final static int MAX_SPEED = 500;
    private final static int MIN_SPEED = 0;


    public RobotValues() {
        super();
        this.speed = _speed;
        this.turn = _turn.name;
        this.lightFollower = _lightFollower;
        this.distance = _distance;
        this.reflection = _reflection;
        this.avoidance = _avoidance;
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

    public static String stringify() {
        return _speed + "#" + _turn.value + "#" + _lightFollower + "#" + _avoidance;
    } 

    @Override
    public String toString() {
        return String.format("Distance: %f%nReflection: %f", _distance, _reflection);
    }    

}
