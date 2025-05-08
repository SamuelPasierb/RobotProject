package services;

// Imports
import java.util.List;
import java.util.Random;

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

    @Path("/turn/left")
    @POST
    public void turnLeft() {
        RobotValues.setTurn(RobotValues.getTurn() - 1);
    }

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

    @GET
	@Path("/testsave")
	public void save() {
        RobotValues robot = new RobotValues();
		database.save(robot);
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

    // @POST
	// @Path("/setspeed")
	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	// public RobotValues postValues(@FormParam("setspeed") int speed) {
	// 	RobotValues lego=new RobotValues(speed);
	// 	EntityManagerFactory emf=Persistence.createEntityManagerFactory("lego");
	// 	EntityManager em=emf.createEntityManager();
	// 	em.getTransaction().begin();
	// 	em.persist(lego);
	// 	em.getTransaction().commit();
	// 	return lego;
	// }

    @Path("/setspeed")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setSpeed(@FormParam("setspeed") int speed) {
        if (speed >= 0 && speed <= 500) {
            RobotValues.setSpeed(speed);
        }
    }
        
    @Path("/line")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void followLine(@FormParam("linefollower") boolean lineFollower) {
        RobotValues.setLight(lineFollower);  
    }

    @Path("/avoidance")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void avoidObstacles(@FormParam("avoidance-type") String type) {
        RobotValues.setAvoidance(type);
    }

    @Path("/currentdata")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getCurrentData() {
        RobotValues robot = new RobotValues();
        return "<h1>" + robot.toString() + "</h1>";
    }
}