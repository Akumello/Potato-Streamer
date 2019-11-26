package swe.PotatoStreamer;
import java.io.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;


@SuppressWarnings("unused")
public class Loader {
	/**
	 * shell class to manage upload/download to/from server
	 */
	
	public Loader() {
		
	}
	
	/**
	 * 
	 * method to send a basic input audio file to the db
	 * 
	 * @param file
	 * @return boolean indicating whether the upload was successful or not
	 */
	public static boolean uploadSong(AudioFile file, String username, String name, String artist, String album) throws SQLException {
		try {
			Connection conn = DBInteract.makeConn();
			DBInteract.addNewMusic(file.file.getMp3file(), username, name, artist, album);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
