package org.gil.Trivia.model;

import java.util.ArrayList;
import java.util.Random;
import org.gil.Trivia.Helpers.DBManager;
import javax.xml.bind.annotation.XmlRootElement;
/*
 * This class represents a question in the game.
 * Each question has it's id, difficulty, the question it self, correct answer and 3 wrong ones.
 */
@XmlRootElement
public class Question {
	private int id;
	private int difficulty;
	private String question;
	private String correctAns;
	private String wrongAns1;
	private String wrongAns2;
	private String wrongAns3;
	
	//get a random question with a specific difficulty
	public Question(int diff, ArrayList<Integer> arrId){
		DBManager db = new DBManager();
		String idFilterStr = idFilter(arrId);
		String query = "SELECT * FROM data.questions WHERE difficulty = '" + diff + "'" + idFilterStr;
		db.exQuery(query);
		Random rand = new Random();
		int randomRow = 0;
		if(db.getRowCount() >= 2)
			randomRow = rand.nextInt(db.getRowCount());
		this.id = (int)db.getValueAt(randomRow, 0);
		this.difficulty = diff;
		this.question = (String)db.getValueAt(randomRow, 2);
		this.correctAns = (String)db.getValueAt(randomRow, 3);
		this.wrongAns1 = (String)db.getValueAt(randomRow, 4);
		this.wrongAns2 = (String)db.getValueAt(randomRow, 5);
		this.wrongAns3 = (String)db.getValueAt(randomRow, 6);
		db.disconnectFromDB();
	}

	/*
	 * Helper method for creating the filter in the query for getting a question
	 * that didn't used before.
	 */
	private String idFilter(ArrayList<Integer> arrId) {
		if(arrId == null || arrId.isEmpty())
			return "";
		String filter = new String();
		for(int id:arrId){
			filter += " AND id != '" + id + "'";
		}
		return filter;
	}
	//empty question
	public Question(){
		this.id = -1; //symbolize that it is an empty question
	}
	
	public int getId() {
		return id;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public String getQuestion() {
		return question;
	}

	public String getCorrectAns() {
		return correctAns;
	}

	public String getWrongAns1() {
		return wrongAns1;
	}

	public String getWrongAns2() {
		return wrongAns2;
	}

	public String getWrongAns3() {
		return wrongAns3;
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", difficulty=" + difficulty
				+ ", question=" + question + ", correctAns=" + correctAns
				+ ", wrongAns1=" + wrongAns1 + ", wrongAns2=" + wrongAns2
				+ ", wrongAns3=" + wrongAns3 + "]";
	}

	

	
}
