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
    private String turn;
    private boolean run = true;

    private final int MAX_SPEED = 500;
    private final int MIN_SPEED = 0;

    public RobotValues() {
        super();
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

    public int getSpeed() {
        return speed;
    }

    public boolean setSpeed(int speed) {
        
        // Can't use this speed
        if (speed < MIN_SPEED || speed > MAX_SPEED) {
            return false;
        }
        
        this.speed = speed;
        return true;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn.name;
    }

    public boolean isRunning() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public String stringify() {
        return this.speed + "#" + Turn.valueOf(this.turn).value;
    } 

}
