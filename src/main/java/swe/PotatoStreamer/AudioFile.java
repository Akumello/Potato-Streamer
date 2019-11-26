package swe.PotatoStreamer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.farng.mp3.*;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.*;

public class AudioFile implements Runnable
{
	// Song metadata
	int id;
	String track;
	String title;
	String artist;
	String album;
	
	// File properties
	String path;
	MP3File file = null;
	
	// 
	int pauseLoc = 0;
	Thread bgPlayer;
	FileInputStream in = null;
    AdvancedPlayer player = null;
    
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
			track = file.getID3v1Tag().getTrackNumberOnAlbum();
			title = file.getID3v1Tag().getTitle();
			album = file.getID3v1Tag().getAlbum();
			artist = file.getID3v1Tag().getArtist();
			
			in = new FileInputStream(path);
		    player = new AdvancedPlayer(in, FactoryRegistry.systemRegistry().createAudioDevice());
		    playState = PlayState.UNSTARTED;
		    
			bgPlayer = new Thread(this);
		    bgPlayer.start();
		} 
		catch (IOException | TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void play()
	{
		switch (playState)
		{
		case UNSTARTED:
			playState = PlayState.PLAYING;
			//player.notify();
			break;
		case PLAYING:
			player.stop();
			//player.notify();
			break;
		case PAUSED:
			//bgPlayer.start();
			break;
		default:
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void pause()
	{
		// Nothing is playing to be paused
		if(playState != PlayState.PLAYING) return;
		
		player.stop();
		playState = PlayState.PAUSED;
	}

	@Override
	public void run() 
	{
		
		
	    player.setPlayBackListener(new PlaybackListener() 
	    {
	        @Override
	        public void playbackFinished(PlaybackEvent event) 
	        {
	            pauseLoc = event.getFrame();
	        }
	    });
	    
	    while(true) 
	    {
		    try
		    {
		    	//player.wait();
		    	player.play();
			} catch (JavaLayerException e) {// | InterruptedException e) {
				e.printStackTrace();
			}
	    }
	}

	public int getId() {
		return id;
	}
	
	public String getTrack() {
		return track;
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
