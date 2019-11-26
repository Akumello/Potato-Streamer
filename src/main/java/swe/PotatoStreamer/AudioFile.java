package swe.PotatoStreamer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.farng.mp3.*;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.*;

public class AudioFile
{
	// Song metadata
	int id;
	String title;
	String artist;
	String album;
	
	// File properties
	String path;
	MP3File file = null;
	
	private Media song;
	
	// 
	int pauseLoc = 0;
	Thread bgPlayer;
	FileInputStream in = null;
    //AdvancedPlayer player = null;
    
    private enum PlayState
    {
    	UNSTARTED,
    	PLAYING,
    	PAUSED,
    	FINISHED
    };
    private static PlayState playState;
	
	public AudioFile(String path)
	{
		try
		{
			//this.path = path
			file = new MP3File(path);
			this.path = path;
			
			// Fill in song metadata properties
			title = file.getID3v1Tag().getTitle();
			album = file.getID3v1Tag().getAlbum();
			artist = file.getID3v1Tag().getArtist();
			
		    String uriString = new File(path).toURI().toString();
			//in = new FileInputStream(path);
		    

		} 
		catch (IOException | TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void play()
	{
		switch (playState)
		{
		case UNSTARTED:
			FileInputStream in = null;
			try {
				in = new FileInputStream(this.path);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		    AdvancedPlayer player;
			try {
				player = new AdvancedPlayer(in, FactoryRegistry.systemRegistry().createAudioDevice());
			} catch (JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			playState = PlayState.PLAYING;
		    bgPlayer.start();
			break;
		case PLAYING:
			//player.notify();
			break;
		case PAUSED:
			//bgPlayer.start();
			break;
		default:
			break;
		}
	}
	
	public void stop(AdvancedPlayer player)
	{
		// Nothing is playing to be paused
		if(playState != PlayState.PLAYING) return;
		
		player.stop();
		player.close();
		playState = PlayState.PAUSED;
	}

	public int getId() {
		return id;
	}
	

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getAlbum() {
		return album;
	}

	public String getPath() {
		return path;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
