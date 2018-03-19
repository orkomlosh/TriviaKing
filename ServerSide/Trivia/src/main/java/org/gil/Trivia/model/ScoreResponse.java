package org.gil.Trivia.model;

import javax.xml.bind.annotation.XmlRootElement;
/*
 * This class represent objects that used as score response to the client.
 */
@XmlRootElement
public class ScoreResponse {

	private int id;
	private String name;
	private int score;
	private int wins;
	private int loses;
	private int rank;

	/*
	 * constructor that uses all the values.
	 */
	public ScoreResponse(int id, String name, int score, int wins, int loses, int rank) {
		super();
		this.id = id;
		this.name = name;
		this.score = score;
		this.wins = wins;
		this.loses = loses;
		this.rank = rank;
	}
	/*
	 * constructor without name.
	 */
	public ScoreResponse(int id, int score, int wins, int loses, int rank) {
		super();
		this.id = id;
		this.score = score;
		this.wins = wins;
		this.loses = loses;
		this.rank = rank;
	}
	
	/*
	 * constructor without name and rank
	 */
	public ScoreResponse(int id, int score, int wins, int loses) {
		super();
		this.id = id;
		this.score = score;
		this.wins = wins;
		this.loses = loses;
	}
	
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLoses() {
		return loses;
	}

	public void setLoses(int loses) {
		this.loses = loses;
	}
	@Override
	public String toString() {
		return "ScoreResponse [id=" + id + ", name=" + name + ", score="
				+ score + ", wins=" + wins + ", loses=" + loses + ", rank="
				+ rank + "]";
	}
	
	
}
