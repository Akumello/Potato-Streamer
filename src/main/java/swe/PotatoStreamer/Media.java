package swe.PotatoStreamer;

public class Media {
	/**
	 * shell class to contain song information
	 */

	private String song_name;
	private String song_artist;
	private String song_album;

	public Media(String name, String artist, String album) {
		song_name = name;
		song_artist = artist;
		song_album = album;
	}

	public String getName() {
		return song_name;
	}
	
	public String getArtist() {
		return song_artist;
	}
	
	public String getAlbum() {
		return song_album;
	}

}
