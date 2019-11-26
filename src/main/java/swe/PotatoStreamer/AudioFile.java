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

@SuppressWarnings("unused")
public class AudioFile {
	// Song metadata
	int id;
	public AudioMedia song = null;

	// File properties
	MP3File file = null;

	//
	int pauseLoc = 0;
	Thread bgPlayer;
	FileInputStream in = null;
	AdvancedPlayer player = null;

	public AudioFile(MP3File file) {
		this.file = file;
	}

	// AdvancedPlayer player = null;

	private enum PlayState {
		UNSTARTED, PLAYING, PAUSED, FINISHED
	};

	private static PlayState playState;

	public AudioFile(String title, String artist, String album, String path) {
		song.setTitle(title);
		song.setAlbum(album);
		song.setArtist(artist);
		song.setPath(path);
	}

	public AudioFile(String path) throws JavaLayerException {
		try {
			// this.path = path
			file = new MP3File(path);
			song.setPath(path);
			song.setTitle(file.getID3v1Tag().getTitle());
			song.setAlbum(file.getID3v1Tag().getAlbum());
			song.setArtist(file.getID3v1Tag().getArtist());
			in = new FileInputStream(path);
			player = new AdvancedPlayer(in, FactoryRegistry.systemRegistry().createAudioDevice());

			System.out.println(file.hasID3v2Tag());

			if (file.hasID3v1Tag()) {
				// Fill in song metadata properties
				song.setTitle(file.getID3v1Tag().getTitle());
				song.setAlbum(file.getID3v1Tag().getAlbum());
				song.setArtist(file.getID3v1Tag().getArtist());
			} else if (file.hasID3v2Tag()) {
				song.setTitle(file.getID3v2Tag().getSongTitle());
				song.setAlbum(file.getID3v2Tag().getAlbumTitle());
				song.setArtist(file.getID3v2Tag().getLeadArtist());
			} else {
				System.out.println("The mp3 file has no tags");
			}

			if (song.getTitle().isEmpty())
				song.setTitle("Unknown");
			if (song.getArtist().isEmpty())
				song.setArtist("Unknown");
			if (song.getAlbum().isEmpty())
				song.setAlbum("Unknown");

			// String uriString = new File(path).toURI().toString();
			// in = new FileInputStream(path);

		} catch (IOException | TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void play() {
		switch (playState) {
		case UNSTARTED:
			FileInputStream in = null;
			try {
				in = new FileInputStream(song.getPath());
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
			// player.notify();
			break;
		case PAUSED:
			// bgPlayer.start();
			break;
		default:
			break;
		}
	}

	public void stop(AdvancedPlayer player) {
		// Nothing is playing to be paused
		if (playState != PlayState.PLAYING)
			return;

		player.stop();
		player.close();
		playState = PlayState.PAUSED;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
