package org.gil.Trivia.resource;

import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.gil.Trivia.Helpers.DBManager;
import org.gil.Trivia.model.ScoreResponse;

/*
 * ScoreResource gets all the references for 'score' resource
 */

@Path("/score")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ScoreResource {
	
	@GET
	@Path("/{playerId}")
	/*
	 * returns the player's score
	 */
	public ScoreResponse getScore(@PathParam("playerId") int id){
		DBManager db = new DBManager();
		int row = -1;
		db.exQuery("SELECT playerId, score, wins, loses FROM data.score ORDER BY score DESC");//WHERE playerId = '" + id +"'"
		for(int i = 0; i < db.getRowCount() && row == -1; i++){
			if((int)db.getValueAt(i, 0) == id){
				row = i;
			}
		}
		int score = (int)db.getValueAt(row, 1);
		int wins = (int)db.getValueAt(row, 2);
		int loses = (int)db.getValueAt(row, 3);
		db.disconnectFromDB();
		System.out.println(new ScoreResponse(id, score, wins, loses, row+1));
		return new ScoreResponse(id, score, wins, loses, row+1);
	}
	
	
	@PUT
	@Path("/{playerId}/{score}/{status}")
	/*
	 * Updates the score of the player and returns it.
	 * status param is a string of 'win' or 'lose'
	 */
	public ScoreResponse addScore(@PathParam ("playerId") int id, @PathParam("score") int score, @PathParam("status") String status){
		DBManager db = new DBManager();
		db.exQuery("SELECT score,wins,loses FROM data.score WHERE playerId = '" + id + "'");
		int oldScore = (int)db.getValueAt(0, 0);
		int wins = (int)db.getValueAt(0, 1);
		int loses = (int)db.getValueAt(0, 2);
		if(status.equals("win"))wins++;
		else loses++;
		db.upQuery("UPDATE data.score SET score = '" + (score+oldScore) + "',wins = '" + wins + "',loses = '" + loses + "' WHERE playerId = '" + id + "'");
		db.disconnectFromDB();
		return new ScoreResponse(id,oldScore + score, wins, loses);
	}
	
	@GET
	/*
	 * Returns the score of all the players
	 */
	public ArrayList<ScoreResponse> globalScore(){
		DBManager db1 = new DBManager();
		DBManager db2 = new DBManager();
		ArrayList<ScoreResponse> array = new ArrayList<>();
		
		db1.exQuery("SELECT * FROM data.score ORDER BY score DESC");
		for(int i = 0; i < db1.getRowCount() && i < 10; i++){
			int id = (int)db1.getValueAt(i, 0);
			int score = (int)db1.getValueAt(i, 1);
			int wins = (int)db1.getValueAt(i, 2);
			int loses = (int)db1.getValueAt(i, 3);
			//get the name
			db2.exQuery("SELECT name FROM data.users WHERE id = '" + id + "'");
			String name = (String)db2.getValueAt(0, 0);
			array.add(new ScoreResponse(id, name, score, wins, loses, i+1));
		}
		
		db1.disconnectFromDB();
		db2.disconnectFromDB();
		
		System.out.println(array);
		
		return array;
	}
}
