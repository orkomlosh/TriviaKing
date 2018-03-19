package org.gil.Trivia.service;

import java.util.ArrayList;
import org.gil.Trivia.model.Room;

/*
 * Contains methods and variables for GameResource to use.
 */
public class GameService {

	private static ArrayList<Room> rooms = new ArrayList<>();
	public static final int MAX_ROOMS = 100;
	
	/*
	 * Looking for a room with one player in it.
	 * If there isn't one, it will open and return a new room.
	 */
	public static synchronized Room findARoom(int id){
		//look for a room
		for (Room room:rooms){
			if(!room.isFull()){
				room.setId2(id);
				room.setFull(true);
				return room;
			}
		}
		Room newRoom = new Room(id);
		rooms.add(newRoom);
		return newRoom;
	}
	public static ArrayList<Room> getRooms(){
		return rooms;
	}
	/*
	 * Checks if the "id" is of player that already in a room.
	 */
	public static boolean checkDoubleRequest(int id) {
		for(Room room:rooms){
			if(id == room.getId1() || id == room.getId2()){
				closeRoom(room.getRoomId());
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Finds a specific room in the rooms list and return it(null if not found).
	 */
	public static Room getRoom(int roomID) {
		for(Room room:rooms){
			if(room.getRoomId() == roomID)
				return room;
		}
		return null;
	}
	/*
	 * Closing a room.
	 */
	public static void closeRoom(int roomId) {
		rooms.remove(getRoom(roomId));
	}
	/*
	 * Returns a room that has a player with the given "userID".
	 */
	public static Room getRoomByUserId(int userID) {
		for(Room room:rooms){
			if(room.getId1() == userID)
				return room;
		}
		return null;
	}

	

	
	
	
}
