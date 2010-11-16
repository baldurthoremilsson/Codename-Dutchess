package is.nord.dutchess;

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
	
	/*
	 * Usage:	s=am.getCoinSound();
	 * Pre:		am is a AudioManager object
	 * Post:	s holds the coin sound 
	 */
	public Music getCoinSound()
	{
		return this.coinSound;
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
