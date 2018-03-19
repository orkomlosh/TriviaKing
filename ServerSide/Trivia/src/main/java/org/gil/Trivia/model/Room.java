package org.gil.Trivia.model;

import java.util.ArrayList;
import java.util.Random;

import javax.xml.bind.annotation.XmlRootElement;

import org.gil.Trivia.service.GameService;

/*
 * This class represents a game room. 
 */

@XmlRootElement
public class Room {
	private int roomId;
	private int id1;
	private int id2;
	private boolean full = false;
	private ArrayList<Integer> questionsIds = new ArrayList<>();
	private Question curQuestion = null;
	private boolean turn = true; //true = first player, false = second player
	private int answerNum = -1; //-1: not answered yet,0:didn't answered in time, 1-4: chosen answer number 
	private boolean end = false;
	private boolean didntStart = true;
	
	/*
	 * The constructor. It opens a room with unique id.
	 */
	public Room(int id1) {
		super();
		this.id1 = id1;
		
		//setting the room id
		int curRoomId = -1;
		boolean ok = false;
		while(!ok){
			curRoomId = new Random().nextInt(GameService.MAX_ROOMS);
			ok = true;
			for(Room room:GameService.getRooms()){
				if(room.getRoomId() == curRoomId){
					ok = false;
					break;
				}
			}
		}
		this.roomId = curRoomId;
	}
	
	/*
	 * The method will return true when two players are in the room.
	 * It will return false if too much time past.
	 */
	public synchronized boolean start(){
		if(!full){
			try {
				wait(30000);//wait half a minute
			} catch (Exception e) {}
		}
		if(!full)//30 sec past
			return false;
		notify();
		return true;
	}
	/*
	 * Change the question of the game only if answered.
	 */
	public synchronized void newQuestion() {
		if(curQuestion == null){//first question
			//set randomly which player will get the first question
			turn = new Random().nextBoolean();
			//random question with difficulty = 1
			curQuestion = new Question(1, null);
		}
		else if(answerNum == -1)//already changed question
			return;
		else{//change the question
			turn = !turn;//nextOpponent
			int difficulty = (questionsIds.size()/4) + 1;
			curQuestion = new Question(difficulty, questionsIds);	
		}
		questionsIds.add(curQuestion.getId());
		answerNum = -1;
	}
	
	/*
	 * Sets the answer for the question and wakes up the player who waited for him to answer.
	 */
	public synchronized void answer(int answerNum){
		this.answerNum = answerNum;
		notify();
	}
	
	/*
	 * Waits for answer from the opponent.
	 */
	public synchronized int waitForAnswer() {
		if(this.answerNum == -1){
			try {
				wait(40000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return this.answerNum;
	}
	
	public synchronized void setDidntStart(boolean didntStart) {
		this.didntStart = didntStart;
		notifyAll();
	}
	
	
	
	public int getAnswerNum() {
		return answerNum;
	}
	/*
	 *  param id : your id
	 *  returns the opponent id
	 */
	public int getOpponent(int id){
		if(id == id1)
			return id2;
		return id1;
	}
	public boolean getTurn() {
		return turn;
	}
	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	

	public Question getCurQuestion() {
		return curQuestion;
	}

	public int getRoomId() {
		return roomId;
	}

	public boolean isEnd() {
		return end;
	}
	
	public boolean isDidntStart() {
		return didntStart;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public void setCurQuestion(Question curQuestion) {
		this.curQuestion = curQuestion;
	}

	
	public void setFull(boolean full) {
		this.full = full;
	}
	public boolean isFull() {
		return full;
	}
	public ArrayList<Integer> getQuestionsIds() {
		return questionsIds;
	}
	public void setQuestionsIds(ArrayList<Integer> questionsIds) {
		this.questionsIds = questionsIds;
	}
	public int getId1() {
		return id1;
	}
	public void setId1(int id1) {
		this.id1 = id1;
	}
	public int getId2() {
		return id2;
	}
	public void setId2(int id2) {
		this.id2 = id2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		if (roomId != other.roomId)
			return false;
		return true;
	}
	
	
	
}
