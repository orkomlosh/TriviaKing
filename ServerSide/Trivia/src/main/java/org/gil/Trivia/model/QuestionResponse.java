package org.gil.Trivia.model;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * This class represent the object sent back to the client: 
 * the Question object and the id of the player who need to answer.
 */

@XmlRootElement
public class QuestionResponse {
	private Question question;
	private int refferdId;
	/*
	 * create specific QuestionResponse instance
	 */
	public QuestionResponse(Question question, int refferdId) {
		super();
		this.question = question;
		this.refferdId = refferdId;
	}
	
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public int getRefferdId() {
		return refferdId;
	}

	public void setRefferdId(int refferdId) {
		this.refferdId = refferdId;
	}

	@Override
	public String toString() {
		return "QuestionResponse [question=" + question + ", refferdId="
				+ refferdId + "]";
	}

	/*
	 * create QuestionResponse instance that represent an error
	 */
	public QuestionResponse() {
		super();
		this.question = new Question();//empty question (id = -1)
		this.refferdId = -1;
	}
}
