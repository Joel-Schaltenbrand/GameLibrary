/*
 * MIT License
 *
 * Copyright (c) 2023 Joel Schaltenbrand.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ch.schaltenbrand.gamelibrary.service.hangman;

import ch.schaltenbrand.gamelibrary.dao.hangman.ListReaderService;
import ch.schaltenbrand.gamelibrary.factory.hangman.HangmanGameFactory;
import ch.schaltenbrand.gamelibrary.model.hangman.CheckWordResponse;
import ch.schaltenbrand.gamelibrary.model.hangman.HangmanGame;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HangmanGamePlayServiceImpl implements HangmanGamePlayService {

	@Autowired
	private HangmanGameFactory hangmanGameFactory;

	@Autowired
	private ListReaderService listReaderServiceImpl;

	@Override
	public CheckWordResponse init(HttpSession session) {
		ArrayList<String> wordlist = listReaderServiceImpl.getWordlist();
		HangmanGame game = hangmanGameFactory.create(wordlist.get((int) (Math.random() * wordlist.size())).toUpperCase());
		session.setAttribute("hangmanGame", game);
		return hangmanGameFactory.createResponse(game);
	}

	@Override
	public ResponseEntity<CheckWordResponse> guess(Character letter, HttpSession session) {
		HangmanGame game = (HangmanGame) session.getAttribute("hangmanGame");
		if (game.getAlreadyGuessed().contains(letter)) {
			session.setAttribute("hangmanGame", game);
			return new ResponseEntity<>(hangmanGameFactory.createResponse(game), HttpStatus.BAD_REQUEST);
		}
		if (game.getWord().contains(letter.toString())) {
			char[] maskedWord = game.getMaskedWord().toCharArray();
			for (int i = 0; i < game.getWord().length(); i++) {
				if (game.getWord().charAt(i) == letter) {
					maskedWord[i] = letter;
				}
			}
			game.setMaskedWord(String.valueOf(maskedWord));
		} else {
			game.setCurrentGuess(game.getCurrentGuess() + 1);
		}
		List<Character> alreadyGuessed = game.getAlreadyGuessed();
		alreadyGuessed.add(letter);
		game.setAlreadyGuessed(alreadyGuessed);
		if (game.getMaskedWord().equals(game.getWord())) {
			session.setAttribute("hangmanGame", game);
			return new ResponseEntity<>(hangmanGameFactory.createResponse(game), HttpStatus.ACCEPTED);
		}
		if (game.getCurrentGuess() >= HangmanGame.GUESSES) {
			game.setMaskedWord(game.getWord());
			session.setAttribute("hangmanGame", game);
			return new ResponseEntity<>(hangmanGameFactory.createResponse(game), HttpStatus.CONFLICT);
		}
		session.setAttribute("hangmanGame", game);
		return new ResponseEntity<>(hangmanGameFactory.createResponse(game), HttpStatus.OK);
	}

	@Override
	public String cheat(String password, HttpSession session) {
		if (!"GameLibrary".equals(password)) {
			return "{\"msg\": \"BAD\"} ";
		}
		HangmanGame game = (HangmanGame) session.getAttribute("hangmanGame");
		return String.format("{\"word\": \"%s\"} ", game.getWord());
	}

}
