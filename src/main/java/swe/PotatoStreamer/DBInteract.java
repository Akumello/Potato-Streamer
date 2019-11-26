package swe.PotatoStreamer;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    public static final String databaseLocation = "/Users/Michael/data_storage/";

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
    
    public static User getUser(String username) throws SQLException
    {
        String sql = "select * from users where username = ?";
        prepStat = conn.prepareStatement(sql);
        prepStat.setString(1, username);
        ResultSet res = prepStat.executeQuery();
        res.next();
        User ret = new User();
        ret.setId(res.getString(2));
        ret.setPwd(res.getString(3));
        return ret;
    }

    public static void addNewMusic(String username, String song_name, String song_artist, String song_album, String song_path) throws Exception{
        String query = "insert into `music_storage` (`username`, `song_name`, `song_artist`, `song_album`, `song_path`) values (?, ?, ?, ?, ?)";
        prepStat = conn.prepareStatement(query);
        prepStat.setString(1, username);
        prepStat.setString(2, song_name);
        prepStat.setString(3, song_artist);
        prepStat.setString(4, song_album);
        
        File directory = new File(databaseLocation + "/" + username);
        if (!directory.exists()){
            System.out.println("dir created: " + directory.getAbsolutePath());
            directory.mkdir();
        }
        
        File source = new File(song_path);
        File dest = new File(directory.getAbsolutePath() + "/" + source.getName() + "/");
        System.out.println(dest.getAbsolutePath());


        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        prepStat.setString(5, dest.getAbsolutePath());
        prepStat.executeUpdate();
    }

    public static Connection makeConn() throws SQLException
    {
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
	
	public static ArrayList<User> getUserArray() throws SQLException
	{
		ArrayList<User> users = new ArrayList<User>();
		
		// Form the query and execute it
		String query = "select * from users";
		PreparedStatement p = conn.prepareStatement(query);
		ResultSet result = p.executeQuery();
		
		// Print the results of the executed query
		while (result.next()) {
			User user = new User();
			user.setId(result.getString(2));
			user.setPwd(result.getString(3));
			users.add(user);
		}
		
		return users;
	}
	
	public static ArrayList<AudioFile> getMusicArray(String username) throws SQLException
	{
		ArrayList<AudioFile> songs = new ArrayList<AudioFile>();
		
		// Form the query and execute it
		String query = "select * from music_storage where username = ?";
		PreparedStatement p = conn.prepareStatement(query);
		p.setString(1, username);
		ResultSet result = p.executeQuery();
		
		// Print the results of the executed query
		while (result.next()) {
			System.out.println(result.getString(3));
			songs.add(new AudioFile(result.getString(3), result.getString(4), result.getString(5), result.getString(6)));
		}
		
		return songs;
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
        String create_music_table = "create table music_storage(song_id int not null auto_increment, username varchar(100) not null, song_name varchar(100) not null, song_artist varchar(100) not null, song_album varchar(100) not null, song_path varchar(200) not null, primary key (song_id), foreign key(username) references users(username))";
       
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