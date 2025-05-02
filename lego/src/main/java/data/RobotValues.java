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
    
    private static int _speed = 0;
    private static Turn _turn = Turn.STRAIGHT;

    private final static int MAX_SPEED = 500;
    private final static int MIN_SPEED = 0;

    public RobotValues() {
        super();
        this.speed = _speed;
        this.turn = _turn.name;
    }

    public RobotValues(int speed, Turn turn) {
        super();
        this.speed = speed;
        this.turn = turn.name;
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

    public static String stringify() {
        return _speed + "#" + _turn.value;
    } 

}
