package is.nord.dutchess;

import java.util.ArrayList;
import java.util.List;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.sound.Sound;

/**
 * 
 * @author Hopur Eitt
 * 
 * This class manages music for the levels and sound effects
 * 
 * TODO Make sounds and music into a list to make a playist (for the music at least)
 */
public class AudioManager 
{
	private Music coinSound;
	private Music gameMusic;
	private Music currMusic;
	private List<Music> mPlayList;
	
	/* Usage:	AudioManager am = new AudioManager(coinSound, gameMusic);
	 * Pre:		coinSound is of type Sound and is the sound for agent-coin collision
	 * 			gameMusic is of type Music and is the in-game music.
	 * Post:	am is a AudioManager object
	 */
	public AudioManager(Music coinSound, Music gameMusic)
	{
		this.coinSound = coinSound;
		this.gameMusic = gameMusic;
	}
	
	public AudioManager()
	{
		mPlayList = new ArrayList<Music>();
	}
	
	public AudioManager(Music coin)
	{
		this.coinSound = coin;
		mPlayList = new ArrayList<Music>();
	}
	
	public void addToPlayList(Music music)
	{
		this.mPlayList.add(music);
	}
	
	public List<Music> getPlayList()
	{
		return mPlayList;
	}
	
	/*
	 * Usage:	s=am.getCoinSound();
	 * Pre:		am is a AudioManager object
	 * Post:	s holds the coin sound 
	 */
	public Music getCoinSound()
	{
		return this.coinSound;
	}
	
	public void setCoinSound(Music coinSound)
	{
		this.coinSound = coinSound;
	}
	
	/*
	 * Usage:	s=am.getGameMusic();
	 * Pre:		am is a AudioManager object
	 * Post:	s holds the game music 
	 */
	public Music getGameMusic()
	{
		return this.gameMusic;
	}
	
}
