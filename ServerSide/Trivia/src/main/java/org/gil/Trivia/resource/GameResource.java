package org.gil.Trivia.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.gil.Trivia.Helpers.DBManager;
import org.gil.Trivia.model.QuestionResponse;
import org.gil.Trivia.model.Room;
import org.gil.Trivia.service.GameService;

/*
 * GameResource gets all the references for 'game' resource
 */

@Path("/game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class GameResource {
	
	@POST
	@Path("/{userID}")
	/*
	 * First checks if there already a room with the user in it, if true, close that room.
	 * Then, finds a room with someone to play, starts the game.
	 * Returns room = {roomID} and foeName = {opponents id}
	 * roomID will be -1 if it waited too long for a player to join.
	 * roomID will be -2 if error occurred in the process.
	 */
	public String playRequest(@PathParam("userID") int id){
		if(GameService.checkDoubleRequest(id)){
			System.out.println("double request");
		}
		Room room = GameService.findARoom(id);
		System.out.print(id + " entered a room with roomId: " + room.getRoomId() + ", ");
		boolean success = room.start();
		if(!success){
			if(!room.isDidntStart()){
				GameService.closeRoom(room.getRoomId());
				return "{\"room\" : \"" + "-2" + "\", \"foeName\" : \"" + "-2" + "\"}";
			}
			System.out.println("waited too long");
			return "{\"room\" : \"-1\" , \"foeName\" : \"-1\"}";
		}
		//get the name of the opponent
		DBManager db = new DBManager();
		db.exQuery("SELECT name FROM data.users WHERE id = '" + room.getOpponent(id) +"'");
		String name = (String)db.getValueAt(0, 0);
		db.disconnectFromDB();
		return "{\"room\" : \"" + room.getRoomId() + "\", \"foeName\" : \"" + name + "\"}";
	}
	
	/*
	 * The method returns the question of the game.
	 */
	@GET
	@Path("/{roomID}/question")
	public QuestionResponse getQuestion(@PathParam("roomID") int roomID){
		System.out.print("In get quetion: roomID = " + roomID);
		Room room = GameService.getRoom(roomID);
		String check = (room == null)?"no" : "yes";
		System.out.println(" , room found: " + check);
		if(room == null)
			return new QuestionResponse();//return question response that indicates an error
		room.newQuestion();
		int refferedID = room.getTurn()?room.getId1():room.getId2();
		System.out.println(room.getCurQuestion());
		return new QuestionResponse(room.getCurQuestion(),refferedID);
	}

	
	@PUT
	@Path("/{roomID}/{answerNum}")
	/*
	 * The player that his turn to answer, answered.
	 */
	public void gotAnswer(@PathParam("roomID") int roomID, @PathParam("answerNum") int answerNum){
		System.out.println("got answer :" + answerNum ) ;
		GameService.getRoom(roomID).answer(answerNum);
	}
	
	@GET
	@Path("/{roomID}/answer")
	/*
	 * The player that his turn to wait, waits for an answer
	 */
	public String getAnswer(@PathParam("roomID") int roomID){
		int num = GameService.getRoom(roomID).waitForAnswer();
		if(GameService.getRoom(roomID).getAnswerNum() != 1) //if not answered correctly
			GameService.closeRoom(roomID);
		return "{\"answerNumber\":\"" + num + "\"}";
	}

	@PUT
	@Path("/{userID}")
	/*
	 * close room because the player exited the waiting before the game started 
	 */
	public String closeRoom(@PathParam("userID") int userID){
		System.out.println("in close room");
		GameService.getRoomByUserId(userID).setDidntStart(false);
		return "{\"success\":\"true\"}";
	}
}
