package org.gil.Trivia.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.gil.Trivia.Helpers.DBManager;
import org.gil.Trivia.model.User;
import org.gil.Trivia.service.UserService;

/*
 * UserResource gets all the references for 'users' resource
 */

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

	/*
	 * Creating new user and returns the user in jason format.
	 */
	@POST
	public User newUser(@QueryParam("name") String name, @QueryParam("email") String email, 
							@QueryParam("pass") String pass){
		return UserService.addUserToDB(name, email, pass);
	}
	
	/*
	 * Checks if the mail exists in the database and returns the answer.
	 */
	@GET
	public String checkEmail(@QueryParam("email") String email){
		boolean res = UserService.checkInDB(email);
		return "{\"exist\" : \"" + res + "\"}" ;
	}
	
	/*
	 * The login method checks if there an existing user with the given email and password and returns the id of that user.
	 * If there no user with the given parameters, the returned id is -1. 
	 */
	@GET
	@Path("/login")
	public String login(@QueryParam("email") String email, @QueryParam("pass") String pass){
		System.out.println("pass= " + pass);
		String idAndName = UserService.loginToDB(email,pass);
		return idAndName;
	}
	
	/*
	 * Creates a new password, update it in the data base and sends an email to the user with his new password.
	 */
	@GET
	@Path("/forgot")
	public String password(@QueryParam("email") String email){
		boolean res = UserService.forgotPassword(email);
		return "{ \"exist\" : \"" + res + "\" }";
	}
	
	/*
	 * First, the method checks if the old password is the same as the password in the database.
	 * Then, if true, updates the old password with the new one.
	 * Returns true if changed and false otherwise.
	 */
	@PUT
	public String renewPass(@QueryParam("id") int id, @QueryParam("newPass") String newPass, 
			@QueryParam("oldPass") String oldPass){
		DBManager db = new DBManager();
		db.exQuery("SELECT password FROM users WHERE id ='" + id + "'");
		String pass = (String)db.getValueAt(0, 0);
		if(pass.equals(oldPass)){
			db.upQuery("UPDATE data.users SET password='" + newPass + "' WHERE id='" + id + "'");			
			db.disconnectFromDB();
			return "{ \"success\" : \"true\" }";
		}
		else{
			db.disconnectFromDB();
			return "{ \"success\" : \"false\" }";
		}
		
		
	}
	
}
