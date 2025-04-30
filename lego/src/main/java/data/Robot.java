package data;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class Robot {
    
    // Motors
    private final EV3LargeRegulatedMotor leftMotor;
    private final EV3LargeRegulatedMotor rightMotor;

    public Robot() {

        // Motors
        this.leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
        this.rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);
    }

    public void turn() {
        int _speed = RobotValues.getSpeed();
        int _turn = RobotValues.getTurn();
        this.leftMotor.setSpeed(_speed + (_speed / 2 * _turn));
        this.rightMotor.setSpeed(_speed + (_speed / 2 * -(_turn)));
        System.out.println("Left: " + leftMotor.getSpeed() + "\nRight: " + rightMotor.getSpeed());
    }

}
