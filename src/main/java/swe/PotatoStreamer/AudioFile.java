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
	int id;
	private Media song;
	String title;
	String artist;
	String album;
	String path;
	MP3File file = null;
	int pauseLoc = 0;
	FileInputStream in = null;
    AdvancedPlayer player = null;
	
    public AudioFile(MP3File file) {
    	this.file = file;
    }
	public AudioFile(String path)
	{
		try {
			file = new MP3File(path);
			this.path = path;
			song.setName(file.getID3v1Tag().getTitle());
			song.setAlbum(file.getID3v1Tag().getAlbum());
			song.setArtist(file.getID3v1Tag().getArtist());
			in = new FileInputStream(path);
		    player = new AdvancedPlayer(in, FactoryRegistry.systemRegistry().createAudioDevice());
			
			//System.out.println("\nsize: " + size + "\nbitrate: " + bitrate + "\n");
		} catch (IOException | TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void play()
	{
		run();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	    player.setPlayBackListener(new PlaybackListener() 
	    {
	        @Override
	        public void playbackFinished(PlaybackEvent event) 
	        {
	            pauseLoc = event.getFrame();
	        }
	    });
	    try {
			player.play();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
