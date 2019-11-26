package swe.PotatoStreamer;

public class Media {
	/**
	 * shell class to contain song information, should make things easier for upload/download
	 */
	private String username;
	private String song_name;
	private String song_artist;
	private String song_album;

	public Media(String user, String name, String artist, String album) {
		setUsername(user);
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
	
	public void setName(String s) {
		song_name = s;
	}
	
	public void setArtist(String s) {
		song_artist = s;
	}
	
	public void setAlbum(String s) {
		song_album = s;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}