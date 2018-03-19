package org.gil.Trivia.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.gil.Trivia.Helpers.DBManager;
import org.gil.Trivia.Helpers.SendMail;
import org.gil.Trivia.model.User;
/*
 * Contains methods and variables for UserResource to use.
 */
public class UserService {
	
	/*
	 * Adds a new user to the database
	 */
	static public User addUserToDB(String name, String email, String pass){
		DBManager db = new DBManager();
		
		//create the query and execute it
		String userQuery = "INSERT INTO users(name,email,password) VALUES(" + 
						"'" + name + "','" + email + "','" + pass + "')";
		db.upQuery(userQuery);
		//getting the given id of the new user
		db.exQuery("SELECT id FROM users WHERE email = '" + email + "'");
		int id = (int)db.getValueAt(0, 0);
		//setting the score to be 0
		String scoreQuery = "INSERT INTO score(playerId, score) VALUES('" + id + "','0')";
		db.upQuery(scoreQuery);
		db.disconnectFromDB();
		return new User(id, name, email, pass);
	}
	/*
	 * Checks if the "email" is used by any of the other users.
	 */
	static public boolean checkInDB(String email){
		DBManager db = new DBManager();
		//create the query and execute it
		String query = "SELECT id FROM users WHERE email = '" + email + "'";
		db.exQuery(query);
		int res = -1;
		try {
			res = db.getRowCount();
		} catch (Exception e) {
			System.out.println("Doesn't need to happen");
		}
		db.disconnectFromDB();
		return res != 0;
	}
	/*
	 * Checks if there a user with these (email and password) values in the database.
	 */
	static public String loginToDB(String email, String pass){
		DBManager db = new DBManager();
		//create the query and execute it
		String query = "SELECT id, name FROM users WHERE email = '" + email + "' AND password = '" + pass + "'";
		db.exQuery(query);
		int id = -1;
		String name = "";
		if(db.getRowCount() == 0){
			db.disconnectFromDB();
			return "{\"id\" : \"" + id + "\",\n\"name\" : \"" + name + "\"}" ;
		}
		else{
			id = (int)db.getValueAt(0, 0);
			name = (String)db.getValueAt(0, 1);
			db.disconnectFromDB();
			return "{\"id\" : \"" + id + "\",\n\"name\" : \"" + name + "\"}" ;
		}
		
	}
	/*
	 * Sends mail to the user with his new password and updates it in the database.
	 */
	static public boolean forgotPassword(String email){
		if(!checkInDB(email))
			return false;
		
		String rndPass = generateString(10);
		String hashed = hash(rndPass);
		
		DBManager db = new DBManager();
		db.upQuery("UPDATE data.users SET password='" + hashed + "' WHERE email='" + email + "'");
		SendMail.send(email, rndPass);
		db.disconnectFromDB();
		
		return true;
	}
	
	/*
	 * Hashes the string s and return it.
	 */
	private static String hash(String s)
	{
	    MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("SHA-256");
	    } catch (NoSuchAlgorithmException e1) {
	        e1.printStackTrace();
	    }
	    md.update(s.getBytes());

	    byte byteData[] = md.digest();

	    //convert the byte to hex format
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < byteData.length; i++) {
	        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	    }

	    System.out.println("Hex format:" + sb.toString());
	    return sb.toString();

	}
	
	/*
	 * Creates new string with length of 10. 
	 * Contains only small and big letters and numbers.
	 */
	private static String generateString(int length)
	{
		Random rng = new Random();
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; 
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}
}
