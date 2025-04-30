package services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import data.RobotValues;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/lego")
public class LegoService {

    RobotValues robotValues = new RobotValues(0, 0);

	@Path("/get")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLego() {
        ResponseBuilder builder = Response.ok(robotValues.stringify());
		return builder.build();
	}

    @Path("speedup")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String speedUp() {
        if (robotValues.setSpeed(robotValues.getSpeed() + 10)) return "Current speed: " + robotValues.getSpeed();
        else return "Couldn't change the speed.\nCurrent speed: " + robotValues.getSpeed();
    }

    @Path("slowdown")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String slowDown() {
        if (robotValues.setSpeed(robotValues.getSpeed() - 10)) return "Current speed: " + robotValues.getSpeed();
        else return "Couldn't change the speed.\nCurrent speed: " + robotValues.getSpeed(); 
    }

    @Path("turn/left")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String turnLeft() {
        robotValues.setTurn("LEFT");
        return "Dir: " + robotValues.getTurn();
    }

    @Path("turn/right")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String turnRight() {
        robotValues.setTurn("RIGHT");
        return "Dir: " + robotValues.getTurn();
    }

    
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RobotValues> readAllPrey() {
	//Create an EntityManagerFactory with the settings from persistence.xml file
		EntityManagerFactory emf=Persistence.createEntityManagerFactory("lego");
		//And then EntityManager, which can manage the entities.
		EntityManager em=emf.createEntityManager();
		
		//Read all the rows from table prey. Here the Prey must start with capital, 
		//because class's name starts. This returns a List of Prey objects.
		List<RobotValues> list=em.createQuery("select a from RobotValues a").getResultList();
		return list;
	}

    @POST
	@Path("/addrobot")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public RobotValues postPreyByParams(@FormParam("speed") int speed, @FormParam("turning") int turning) {
		RobotValues lego=new RobotValues(speed, turning);
		EntityManagerFactory emf=Persistence.createEntityManagerFactory("lego");
		EntityManager em=emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(lego);
		em.getTransaction().commit();
		return lego;
	}
}