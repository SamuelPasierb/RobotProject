package services;

// Imports
import java.util.List;
import java.util.Random;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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

import data.Turn;
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
        RobotValues robot = new RobotValues();
        database.save(robot);
    }

    @Path("/turn/left")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String turnLeft() {
        RobotValues.setTurn(Turn.LEFT);
        return "Dir: " + RobotValues.getTurn();
    }

    @Path("/turn/right")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String turnRight() {
        RobotValues.setTurn(Turn.RIGHT);
        return "Dir: " + RobotValues.getTurn();
    }

    /**
     * Gets everything from the database
     */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RobotValues> readAllValues() {
        List<RobotValues> list = database.load("SELECT v.id, v.speed FROM RobotValues v");
		return list;
	}

    /**
     * Gets the last value inserted into the database
     */
    @GET
    @Path("/last")
    @Produces(MediaType.APPLICATION_JSON)
    public RobotValues readLastValues() {
        List<RobotValues> list = database.load("SELECT v FROM RobotValues v ORDER BY v.id DESC", 1);
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
	public void saveByParams(@QueryParam("speed") int speed, @QueryParam("turn") String turn, @QueryParam("reflection") float reflection, @QueryParam("distance") float distance) {
        RobotValues robot = new RobotValues(speed, Turn.valueOf(turn.toUpperCase()), RobotValues.getLight(), distance);
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
            RobotValues robot = new RobotValues();
            database.save(robot);
        }
    }
        
    @Path("/line")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void followLine(@FormParam("linefollower") boolean lineFollower) {
        RobotValues.setLight(lineFollower);
        RobotValues robot = new RobotValues();
        database.save(robot);    
    }

    @Path("/currentdata")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getCurrentData() {
        int n = new Random().nextInt();
        return "<h1>" + n + "</h1>";
    }
}