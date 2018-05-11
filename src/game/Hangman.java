package game;

import java.util.ArrayList;

public class Hangman {
	private int roomID;
	private int peerID;
	private Connection[] peers;

	String wordToGuess;

	String currentWord;

	ArrayList<Character> wrongLetters;

	ArrayList<Character> guessedLetters;

	public Hangman(int roomID, int peerID, Connection[] peers) {
		this.roomID = roomID;
		this.peerID = peerID;
		this.peers = peers;
	}

	public void startGame(String wordToGuess) {
		this.wordToGuess = wordToGuess;
		wrongLetters = new ArrayList<Character>();
		guessedLetters = new ArrayList<Character>();
		currentWord = wordToGuess.replaceAll("[a-zA-Z]", "*");
	}

	// return true if right
	public boolean guessLetter(char letter) {
		boolean wordHasLetter = false;
		
		if (wrongLetters.contains(letter) || guessedLetters.contains(letter))
			return false;
		
		
		for(int i = 0; i < wordToGuess.length(); i++)
			if(wordToGuess.charAt(i)==letter){
				wordHasLetter = true;
				
				//TODO replace currentWord at pos i 
				//Have to use StringBuilder
				
			}
			
		if(wordHasLetter)
			guessedLetters.add(letter);
		else 
			wrongLetters.add(letter);

		return wordHasLetter;
	}

	public static void main(String[] args) {

	}

}
