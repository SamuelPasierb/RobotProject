package services;

import data.RobotValues;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/lego")
public class LegoService {
	
	@Path("/get")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLego() {
        ResponseBuilder builder = Response.ok(RobotValues.stringify());
		return builder.build();
	}

    @Path("speedup")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String speedUp() {
        if (RobotValues.setSpeed(RobotValues.getSpeed() + 10)) return "Current speed: " + RobotValues.getSpeed();
        else return "Couldn't change the speed.\nCurrent speed: " + RobotValues.getSpeed();
    }

    @Path("slowdown")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String slowDown() {
        if (RobotValues.setSpeed(RobotValues.getSpeed() - 10)) return "Current speed: " + RobotValues.getSpeed();
        else return "Couldn't change the speed.\nCurrent speed: " + RobotValues.getSpeed(); 
    }

    @Path("turn/left")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String turnLeft() {
        RobotValues.setTurn("LEFT");
        return "Dir: " + RobotValues.getTurn();
    }

    @Path("turn/right")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String turnRight() {
        RobotValues.setTurn("RIGHT");
        return "Dir: " + RobotValues.getTurn();
    }
}