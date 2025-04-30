package services;

// Imports
import java.util.List;

import data.RobotValues;
import data.Turn;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/lego")
public class LegoService {

    RobotValues robotValues = new RobotValues(0, Turn.STRAIGHT);
    DatabaseService database = new DatabaseService();

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
        robotValues.setTurn(Turn.LEFT);
        return "Dir: " + robotValues.getTurn();
    }

    @Path("turn/right")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String turnRight() {
        robotValues.setTurn(Turn.RIGHT);
        return "Dir: " + robotValues.getTurn();
    }

    
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RobotValues> readAllPrey() {
        List<RobotValues> list = database.load("select a from RobotValues a");
		return list;
	}

    @GET
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postPreyByParams(@QueryParam("speed") int speed, @QueryParam("turn") String turn) {
        RobotValues robot = new RobotValues(speed, Turn.valueOf(turn.toUpperCase()));
		database.save(robot);
	}
}