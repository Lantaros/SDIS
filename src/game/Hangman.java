package game;

import client.Client;
import gui.Launcher;

import java.util.ArrayList;

public class Hangman {
    private int roomID;
    private int peerID;

    static int NUMBER_MAX_ERRORS = 8;

    String wordToGuess;

    String currentWord;

    ArrayList<Character> wrongLetters;

    ArrayList<String> wrongWords;

    ArrayList<Character> guessedLetters;

    boolean turn = false;

    public Hangman(int roomID) {
        this.roomID = roomID;
    }

    public Hangman(int roomID, int peerID) {
        this.roomID = roomID;
        this.peerID = peerID;
    }

    public void startGame(String wordToGuess) {
        this.wordToGuess = wordToGuess.toLowerCase();
        wrongLetters = new ArrayList<Character>();
        guessedLetters = new ArrayList<Character>();
        wrongWords = new ArrayList<String>();
        this.currentWord = wordToGuess.replaceAll(" ", "  ");
        this.currentWord = this.currentWord.replaceAll("[a-zA-Z]", "_ ");
        this.currentWord = this.currentWord.trim();

    }

    public String getWord() {
        return this.currentWord;
    }

    public boolean checkLetter(char letterArg) {
        char letter = Character.toLowerCase(letterArg);
        if (wrongLetters.contains(letter) || guessedLetters.contains(letter))
            return false;
        return true;
    }

    // return true if right
    public boolean guessLetter(char letterArg) {
        char letter = Character.toLowerCase(letterArg);
        boolean wordHasLetter = false;

        if (wrongLetters.contains(letter) || guessedLetters.contains(letter)) {
        	Client.incrementErrors();
        	return false;
        }
            

        for (int i = 0; i < wordToGuess.length(); i++)
            if (wordToGuess.charAt(i) == letter) {
                wordHasLetter = true;
                StringBuilder wordToGuessStrBuild = new StringBuilder(currentWord);
                wordToGuessStrBuild.setCharAt(i*2, letter);
                currentWord = wordToGuessStrBuild.toString();
                System.out.println(currentWord);
                Client.setWordInGUI(currentWord);
            }

        if (wordHasLetter)
            guessedLetters.add(letter);
        else{
        	wrongLetters.add(letter);
        	Client.incrementErrors();
        }
            

        return wordHasLetter;
    }

    public int getNumberOfWrongGuesses() {
        return wrongLetters.size();

    }

	/*public static void main(String[] args) {
		//just 4 testing
		Hangman hang = new Hangman(1, 1, null);
		hang.startGame("Qualquer  sdasadsa  dsadsa");
		System.out.println(hang.currentWord);
		hang.guessLetter('q');
		System.out.println(hang.currentWord);
		hang.guessLetter('u');
		System.out.println(hang.currentWord);
		hang.guessLetter('z');
		System.out.println(hang.currentWord);
		hang.guessLetter('x');
		System.out.println(hang.currentWord);
		hang.guessLetter('c');
		System.out.println(hang.currentWord);
		hang.guessLetter('v');
		System.out.println(hang.currentWord);
		if(hang.hasLost())
			System.out.println("lost");
		

	}*/

    public boolean hasWon() {
        String tokens = new String(wordToGuess.replaceAll("\\s+",""));
        String tokens2 = new String(currentWord.replaceAll("\\s+",""));
        return tokens.equals(tokens2);

    }

    public boolean hasLost() {
       // return (wrongLetters.size() + wrongWords.size()) >= NUMBER_MAX_ERRORS;
    	return Launcher.getFrame().gamePanel.getNumberOfErrors() == 8;

    }

    public boolean gameOver() {
        return hasWon() || hasLost();

    }

    public boolean getTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void guessWord(String word) {
        String tokens = new String(word.replaceAll("\\s+",""));
        String tokens2 = new String(wordToGuess.replaceAll("\\s+",""));
        if(tokens.equals(tokens2)) {
            currentWord = tokens;
        } else {
            wrongWords.add(tokens);
        }
    }


}
