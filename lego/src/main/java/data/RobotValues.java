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
    private int speed;
    private int turn;
    private boolean run = true;

    private final int MAX_SPEED = 500;
    private final int MIN_SPEED = 0;

    public RobotValues() {
        super();
    }

    public RobotValues(int speed, int turn) {
        super();
        this.speed = speed;
        this.turn = turn;
    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        id = _id;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean setSpeed(int _speed) {
        
        // Can't use this speed
        if (_speed < MIN_SPEED || _speed > MAX_SPEED) {
            return false;
        }
        
        speed = _speed;
        return true;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(String _turn) {
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

    public boolean isRunning() {
        return run;
    }

    public void setRun(boolean _run) {
        run = _run;
    }

    public String stringify() {
        return speed + "#" + turn;
    } 

}
