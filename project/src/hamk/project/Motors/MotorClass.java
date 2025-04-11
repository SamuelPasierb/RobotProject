package hamk.project.Motors;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.utility.Delay;

public class MotorClass extends Thread {
    
    private int speed;
    private final String side;
    public AtomicInteger turnDegrees;
    public static AtomicInteger turnTime;
    private NXTRegulatedMotor motor;
    private AtomicBoolean running;

    private static final int WHEEL_RADIUS = 28;
    private static final int WHEELBASE = 100;    

    public MotorClass(NXTRegulatedMotor m, String side) {

        this.side = side;
        this.motor = m;
        this.speed = 0;
        this.motor.setAcceleration(2000);
        this.turnDegrees = new AtomicInteger();
        turnTime = new AtomicInteger(3000);
        this.running = new AtomicBoolean(false);

    }

    @Override
    public void run() {
    
    }

    protected void movement() {

        // Left motor runs a bit sooner so small delay to even it out
        // if (this.side.equals("LEFT")) Delay.msDelay(50);

        // Is moving
        if (this.running.get()) {
            this.motor.forward();;
        } else { // Stops because of obstacle
            this.motor.stop();

            // Small delay
            Delay.msDelay(300);

            // Static speed
            this.motor.setSpeed(165);

            // Rotate
            if (this.side.equals("LEFT")) this.motor.backward();
            else this.motor.forward();

            // Wait a sec
            Delay.msDelay(1000);

            // Go back
            this.motor.setSpeed(this.speed);
            this.motor.stop();

            // Small delay
            Delay.msDelay(300);
        }
    }

    public void changeSpeedTo(int speed) {
        this.speed = speed;
        this.motor.setSpeed(this.speed);
    }

    public void changeSpeedBy(int speed) {
        this.changeSpeedTo(this.speed + speed);
    }

    public void startMotor() {
        this.running.set(true);
    }

    public void stopMotor() {
        this.running.set(false);
    }

    public boolean isRunning() {
        return this.running.get();
    }

    public int getSpeed() {
        return this.speed;
    }

    protected int getSpeedDifference(int degrees) {
        return (WHEELBASE * degrees) / (WHEEL_RADIUS * (turnTime.get() / 1000)) / 2;
    }

    protected void turn() {
        int degrees = turnDegrees.get();
        turnDegrees.set(0);
        if (degrees > 0) turnRight(degrees);
        else turnLeft(Math.abs(degrees));
    }

    public void turnLeft(int degrees) {}
    public void turnRight(int degrees) {}

}
