package services;

// Imports
import java.util.List;

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

    DatabaseService database = new DatabaseService();

	@Path("/get")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLego() {
        ResponseBuilder builder = Response.ok(RobotValues.stringify());
		return builder.build();
	}

    @Path("/speedup")
    @POST
    public void speedUp() {
        if (RobotValues.setSpeed(RobotValues.getSpeed() + 25)) {
            RobotValues robot = new RobotValues(RobotValues.getSpeed(), RobotValues.getTurn());
            database.save(robot);
            
        }
    }

    @Path("/slowdown")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String slowDown() {
        if (RobotValues.setSpeed(RobotValues.getSpeed() - 25)) return "Current speed: " + RobotValues.getSpeed();
        else return "Couldn't change the speed.\nCurrent speed: " + RobotValues.getSpeed(); 
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

    
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RobotValues> readAllPrey() {
        List<RobotValues> list = database.load("SELECT v.id, v.speed FROM RobotValues v");
		return list;
	}

    @GET
    @Path("/last")
    @Produces(MediaType.APPLICATION_JSON)
    public RobotValues readLastPrey() {
        List<RobotValues> list = database.load("SELECT v FROM RobotValues v ORDER BY v.id DESC", 1);
        return list.get(0);
    }

    @GET
	@Path("/testsave")
	@Consumes(MediaType.APPLICATION_JSON)
	public void save() {
        RobotValues robot = new RobotValues(RobotValues.getSpeed(), RobotValues.getTurn());
		database.save(robot);
	}

    @GET
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	public void saveByParams(@QueryParam("speed") int speed, @QueryParam("turn") String turn) {
        RobotValues robot = new RobotValues(speed, Turn.valueOf(turn.toUpperCase()));
		database.save(robot);
	}

    private void test() {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js"); //We can also have "js", "nashorn" instead of "javascript"
            engine.eval("var PIE = document.getElementById(\"#speedomter\")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @POST
	@Path("/setspeed")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public RobotValues postValues(@FormParam("setspeed") int speed) {
		RobotValues lego=new RobotValues(speed);
		EntityManagerFactory emf=Persistence.createEntityManagerFactory("lego");
		EntityManager em=emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(lego);
		em.getTransaction().commit();
		return lego;
	}

    @Path("/setspeed")
    @POST
    public void setSpeed(@FormParam("setspeed") int speed) {
        if (speed >= 0 && speed <= 500) {
            RobotValues robot = new RobotValues(speed);
            database.save(robot);
        }
    }
        
}