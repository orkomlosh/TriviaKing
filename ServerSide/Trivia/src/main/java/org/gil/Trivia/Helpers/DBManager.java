package org.gil.Trivia.Helpers;
import java.sql.*;

public class DBManager {
	private final Connection connection;
	private final Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;
	private boolean connectedToDB = false; //keep track of DB connection status
	final String DATABASE_URL = "jdbc:mysql://localhost:3306/data";
	
	public DBManager(){
		try{
			//connect to DB
			connection = DriverManager.getConnection(DATABASE_URL, "gilpas", "1234");
			//create Statement to query DB
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//update DB connection status
			connectedToDB = true;
		}catch(SQLException e){
			e.printStackTrace();
			throw new IllegalStateException("Fail connecting to DB.");
		}
	}
	
	public ResultSet getResultSet() throws IllegalStateException{
		if(!connectedToDB)
			throw new IllegalStateException("Not Connected to DB");
		
		return resultSet;
	}

	public ResultSetMetaData getMetaData() throws IllegalStateException{
		if(!connectedToDB)
			throw new IllegalStateException("Not Connected to DB");
		
		return metaData;
	}

	//execute a SELECT query
	public void exQuery(String query){
		//ensure DB connection is available
		if(!connectedToDB)
			throw new IllegalStateException("Not Connected to DB");
		try{
			//specify query and execute it
			resultSet = statement.executeQuery(query);
			
			//obtain metadata for ResultSet
			metaData = resultSet.getMetaData();
			
			//determine number of rows in ResultSet
			resultSet.last();//move to last row
			numberOfRows = resultSet.getRow();
		}catch(SQLException e){
			e.printStackTrace();
			throw new IllegalStateException("Invalid Execute Query.");
		}
	}
	
	//execute an update (non-select) query
	public void upQuery(String query){
		if(!connectedToDB)
			throw new IllegalStateException("Not Connected to DB");
		try{
			statement.executeUpdate(query);
		}catch(SQLException e){
			e.printStackTrace();
			throw new IllegalStateException("Illegal change in DB");
		}
	}
	
	//return number of rows
	public int getRowCount() throws IllegalStateException{
		if(!connectedToDB)
			throw new IllegalStateException("Not Connected to DB");
		
		return numberOfRows;
	}
	
	//obtain value in particular row and column
	public Object getValueAt(int row, int col) throws IllegalStateException{
		if(!connectedToDB)
			throw new IllegalStateException("Not Connected to DB");
		
		try{
			resultSet.absolute(row + 1);
			return resultSet.getObject(col + 1);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return ""; //if problem occur, return empty string
	}

	//close Statement and Connection
	public void disconnectFromDB(){
		if (connectedToDB){
			//close statement and connection
			try{
				if(resultSet != null){
					resultSet.close();
					statement.close();
				}
				connection.close();
			}
			catch (Exception e){
				throw new IllegalStateException("Fail disconnecting from DB");
			}
			finally{
				connectedToDB = false;
			}
		}
	}
}
