package hamk.project.Motors;

import hamk.project.LCD.LCDClass;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class LeftMotor extends MotorClass {
    
    public LeftMotor() {
        super(Motor.D, "LEFT");
    }

    @Override
    public void run() {
        
        while (!this.isInterrupted()) {

            // Update speed for LCD
            LCDClass.leftSpeed.set("L: " + this.getSpeed());

            // Delay
            Delay.msDelay(100);

            // Turning
            if (turnDegrees.get() != 0) {
                this.turn();
            }

            // Move or stop
            this.movement();

        }

    }

    @Override
    public void turnLeft(int degrees) {

        int speedDifference = this.getSpeedDifference(degrees);

        this.changeSpeedBy(-speedDifference);
        Delay.msDelay(turnTime.get());
        this.changeSpeedBy(speedDifference);

        turnTime.set(3000);
    }

    @Override
    public void turnRight(int degrees) {

        int speedDifference = this.getSpeedDifference(degrees);

        this.changeSpeedBy(speedDifference);
        Delay.msDelay(turnTime.get());
        this.changeSpeedBy(-speedDifference);
    }

}
