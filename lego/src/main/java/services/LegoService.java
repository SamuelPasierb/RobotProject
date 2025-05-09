package services;

// Imports
import java.util.List;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

import data.RobotValues;

@Path("/lego")
public class LegoService {

    // Database
    DatabaseService database = new DatabaseService();

    /**
     * Used for reading the data from the webservice by the robot
     * @return String of all the important data
     */
	@Path("/get")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLego() {
        ResponseBuilder builder = Response.ok(RobotValues.stringify());
		return builder.build();
	}

    /**
     * Used for speed slider.
     * Updates the speed in {@link RobotValues}.
     * @param speed Speed from the slider
     */
    @Path("/changespeed")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void speedUp(@FormParam("speed") int speed) {
        RobotValues.setSpeed(speed);
    }

    /**
     * Go more left
     */
    @Path("/turn/left")
    @POST
    public void turnLeft() {
        RobotValues.setTurn(RobotValues.getTurn() - 1);
    }

    /**
     * Go more right
     */
    @Path("/turn/right")
    @POST
    public void turnRight() {
        RobotValues.setTurn(RobotValues.getTurn() + 1);
    }

    /**
     * Gets everything from the database
     */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RobotValues> readAllValues() {
        List<RobotValues> list = database.load("SELECT v.id, v.speed, v.turn, v.reflection, v.distance FROM RobotValues v");
		return list;
	}

    /**
     * Gets the last value inserted into the database
     */
    @GET
    @Path("/last")
    @Produces(MediaType.APPLICATION_JSON)
    public Object readLastValues() {
        List<Object> list = database.load("SELECT v.id, v.speed, v.turn, v.reflection, v.distance FROM RobotValues v ORDER BY v.id DESC", 1);
        return list.get(0);
    }

    /**
     * Saves the current values from the robot to the database
     * Used only by the robot
     *
     * @param speed Current sped of the robot
     * @param turn Direction of the robot
     * @param lightFollower Whether light folower is turned on
     */
    @GET
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	public void saveByParams(@QueryParam("turn") int turn, @QueryParam("speed") float speed, @QueryParam("reflection") float reflection, @QueryParam("distance") float distance, @QueryParam("avoid") String type, @QueryParam("light") boolean light, @QueryParam("time") long time) {
        RobotValues.setSpeed((int) speed);
        RobotValues.setTurn(turn);
        RobotValues.setDistance(distance);
        RobotValues.setReflection(reflection);
        RobotValues.setAvoidance(type);
        RobotValues.setLight(light);
        RobotValues.setTime(time);
        RobotValues robot = new RobotValues();
		database.save(robot);
	}

    /**
     * Manually setting speed
     * @param speed New speed
     */
    @Path("/setspeed")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setSpeed(@FormParam("setspeed") int speed) {
        if (speed >= 0 && speed <= 500) {
            RobotValues.setSpeed(speed);
        }
    }
        
    /**
     * Line follower switch
     * @param lineFollower Whether the swich is on or off
     */
    @Path("/line")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void followLine(@FormParam("linefollower") boolean lineFollower) {
        RobotValues.setLight(lineFollower);  
    }

    /**
     * Which type of obstacle avoidance to use (STOP, TURN AROUND, GO AROUND)
     * @param type Type of obstacle avoidance
     */
    @Path("/avoidance")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void avoidObstacles(@FormParam("avoidance-type") String type) {
        RobotValues.setAvoidance(type);
    }

    /**
     * Used in the iframe with sensor data and time alive
     * @return HTML for the iframe
     */
    @Path("/currentdata")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getCurrentData() {
        RobotValues robot = new RobotValues();
        // return "<h1>" + robot.toString() + "</h1>";
        return String.format("<html><head><style>body {color: white;background: transparent;font-family: sans-serif;margin: 0;padding: 1rem;font-size: 10px;}</style></head><body><h1>%s</h1></body></html>", robot.toString());
    }
}