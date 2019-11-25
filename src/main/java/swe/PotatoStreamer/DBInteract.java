package swe.PotatoStreamer;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.farng.mp3.*;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

/**
 * Shell class to interact with and perform DB operations
 */
public class DBInteract{
    public static final String dbname = "potatobase";
    public static final String modify_user_stub = "update users set ";
    public static final String modify_storage_stub = "update music_storage set";

    public static Connection conn = null;
    public static PreparedStatement prepStat = null;



    public DBInteract() throws SQLException{
        conn = makeConn();
    }


    /**
     * TABLE MANIPULATION
     */
    
    //##################
    public static boolean authenticate(String username, String pwd) 
    {
    	// Query to retrieve the users password from the potatobase
        String sql = "select pwd from users where username = ?";
        
        ResultSet result;
        String storedPwd = "";
        try {
        	// Attempt query, may go to catch if the user does not exist
			prepStat = conn.prepareStatement(sql);
	        prepStat.setString(1, username);
	        result = prepStat.executeQuery();
	        
	        // Get the users password
	        result.next();
	        storedPwd = result.getString("pwd");
		} catch (SQLException e) {
			// The user was not in the database
			System.out.println(e);
			return false;
		}
        
        if(pwd.equals(storedPwd)) 
        	return true;
        else
        	return false;
    }

    /**
     * DELETE COMMANDS
     */

    public static void deleteUser(String username) throws SQLException
    {
        String sql = "delete from users where username = ?";
        prepStat = conn.prepareStatement(sql);
        prepStat.setString(1, username);
        prepStat.executeQuery();
    }

    public static void deleteSong(int song_id) throws SQLException{
        String sql = "delete from music_storage where song_id = " + Integer.toString(song_id);
        prepStat = conn.prepareStatement(sql);
        prepStat.executeQuery();
    }

    /**
     * RETRIEVING COMMANDS
     */
    
    public static File getMusicFromUser(int user_id) throws SQLException
    {
        String query = "select song_name, song_data from music_storage where user_id = " + Integer.toString(user_id);
        prepStat = conn.prepareStatement(query);
        ResultSet res = prepStat.executeQuery();
        File song = null;
        while(res.next()){
            String name = res.getString("song_name");

            song = new File(name);
            try (FileOutputStream fos = new FileOutputStream(song)) {
                byte[] buffer = new byte[1024];

                // Get the binary stream of our BLOB data
                InputStream is = res.getBinaryStream("image");
                while (is.read(buffer) > 0) {
                    fos.write(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return song;
    }
    

    /**
     * ADDING COMMANDS
     */
    
    public static boolean addUser(String username, String pwd) throws SQLException
    {
    	PreparedStatement p;
    	
        if(conn != null){
            // continue
            try
            {
            	java.sql.Date sqlDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
                String base_query = "insert into `users` (`username`, `pwd`,  `user_cdate`) values (?, ?, ?)";
                p = conn.prepareStatement(base_query);
                p.setString(1, username);
                p.setString(2, pwd);
                p.setString(3, sqlDate.toString());
                p.executeUpdate();
                return true;
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }
		return false;
    }

    public static void addNewMusic(File musicFile, int userID, String song_name, String song_artist, String song_album) throws Exception{
        FileInputStream input = new FileInputStream(musicFile);
        String query = "insert into `music_storage` (`user_id`, `song_name`, `song_artist`, `song_album`, `song_data`) values (?, ?, ?, ?, ?)";
        prepStat = conn.prepareStatement(query);
        prepStat.setInt(1, userID);
        prepStat.setString(2, song_name);
        prepStat.setString(3, song_artist);
        prepStat.setString(4, song_album);
        prepStat.setBinaryStream(5, input);
        prepStat.executeQuery();
    }

    /**
     * 
     * Modifying table commands
     * 
     */

    public static void updateUserFName(String newName, String oldName) throws SQLException{

        String query = "".concat(modify_user_stub).concat("user_fname = ").concat(newName).concat(" where user_fname = ").concat(oldName);
        prepStat = conn.prepareStatement(query);
        prepStat.executeQuery();
    }

    public static void updateUserLName(String newName, String oldName) throws SQLException{

        String query = "".concat(modify_user_stub).concat("user_lname = ").concat(newName).concat(" where user_lname = ").concat(oldName);
        prepStat = conn.prepareStatement(query);
        prepStat.executeQuery();
    }

    public static void updatePwd(String newPwd, String oldPwd) throws SQLException{

        String query = "".concat(modify_user_stub).concat("pwd = ").concat(newPwd).concat(" where pwd = ").concat(oldPwd);
        prepStat = conn.prepareStatement(query);
        prepStat.executeQuery();
    }



    /**
     * 
     * GENERAL SETUP COMMANDS/GETTER SETTER
     * 
     */


    public static int getUserID(String user_fname, String user_lname) throws SQLException{
        String query = "select user_id from users where user_fname = " + user_fname + " and user_lname = " + user_lname;
        prepStat = conn.prepareStatement(query);
        ResultSet res = prepStat.executeQuery();
        int uid = res.getInt("user_id");
        return uid;
    }

    public static int getSongID(int user_id) throws SQLException{
        String sql = "select song_id from music_storage where user_id = " + user_id;
        prepStat = conn.prepareStatement(sql);
        ResultSet r = prepStat.executeQuery();
        int sid = r.getInt("song_id");
        return sid;
    }

    public static Connection makeConn() throws SQLException
    {
/*        try{
            Class.forName("com.mysql.cj.jdbc.driver");
        }
        catch(Exception e){
            e.printStackTrace();
        }
*/
    	if(conn != null) 
    	{
    		System.out.println("We are already connected to the database.");
    		return conn;
    	}
    	
        try {
            /**
             * 
             * IMPORTANT: customize this to your user/password settings when operating on local machine
             * 
             */
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "password");
            if (conn != null){
            	System.out.println("Connection successful!");
                useDB();
                System.out.println("Using the potatobase!");
                return conn;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
    
	public static void printAllUsers() throws SQLException
	{
		// Form the query and execute it
		String query = "select * from users";
		PreparedStatement p = conn.prepareStatement(query);
		ResultSet result = p.executeQuery();
		
		System.out.println("USERS table:\nUser ID\tUsername\tPassword");
		// Print the results of the executed query
		while (result.next()) {
			System.out.println("User " + result.getString(1) + ":\t" + result.getString(2) + "\t\t" + result.getString(3));
		}
	}

    public static void setDB() throws SQLException{
        if (conn == null) return;
        String query = "set database swe_proj";
        prepStat = conn.prepareStatement(query);
        prepStat.executeQuery();
    }

    public static void useDB() throws SQLException{
        if (conn == null) return;
        String query = "use potatobase";
        prepStat = conn.prepareStatement(query);
        
        try {
        prepStat.execute();
        } catch (Exception e) {
        	System.out.println("Database does not exist!\nCreating the potatobase...");
        	dbSetup();
        }
    }
   
    /**
     * This method sets up the database and all constituent tables. Only run if you are operating this on your machine for the first time
     */
    public static void dbSetup() throws SQLException{
        //conn = makeConn();
        if (conn == null) return;
        String createdb = "create database potatobase";
        String setdb = "use potatobase";
        String create_user_table = "create table users( user_id int not null auto_increment, username varchar(100) not null unique, pwd varchar(100) not null, user_cdate date,primary key (user_id))";
        String create_music_table = "create table music_storage(song_id int not null auto_increment, song_name varchar(100) not null, song_artist varchar(100) not null, song_album varchar(100) not null, song_data blob not null, primary key (song_id))";
       
        prepStat = conn.prepareStatement(createdb);
        prepStat.execute();
        prepStat = conn.prepareStatement(setdb);
        prepStat.execute();
        prepStat = conn.prepareStatement(create_user_table);
        prepStat.execute();
        prepStat = conn.prepareStatement(create_music_table);
        prepStat.execute();
    }

    
}