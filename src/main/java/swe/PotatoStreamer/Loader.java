package swe.PotatoStreamer;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

@SuppressWarnings("unused")
public class Loader {
	/**
	 * shell class to manage upload/download to/from server
	 */
/*
	public Loader() throws SQLException {
		DBInteract.makeConn();
	}

	/**
	 * 
	 * method to send a basic input audio file to the db
	 * 
	 * @param file
	 * @return boolean indicating whether the upload was successful or not

	public boolean uploadSong(AudioFile file, AudioMedia song) throws SQLException {
		try {
			//DBInteract.addNewMusic(file.file.getMp3file(), song.getUsername(), song.getTitle(), song.getArtist(),
			//		song.getAlbum());
			return true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public AudioFile downloadSong(AudioMedia song) throws IOException, TagException {
		/**
		 * song only needs to contain a username provided by the input

		AudioFile aF = null;
		try {
			//File test = DBInteract.getMusicFromUser(song);
			//MP3File file = new MP3File();
			//file.setMp3file(test);
			//aF = new AudioFile(file);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aF;

	}
			 */
		
}
