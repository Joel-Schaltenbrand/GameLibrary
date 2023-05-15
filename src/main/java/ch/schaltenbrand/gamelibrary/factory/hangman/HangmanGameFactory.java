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

package ch.schaltenbrand.gamelibrary.factory.hangman;

import ch.schaltenbrand.gamelibrary.model.hangman.CheckWordResponse;
import ch.schaltenbrand.gamelibrary.model.hangman.HangmanGame;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class HangmanGameFactory {

	public HangmanGame create(String word) {
		HangmanGame game = new HangmanGame();
		game.setWord(word);
		game.setMaskedWord(game.getWord().replaceAll(".", "_"));
		game.setCurrentGuess(0);
		game.setAlreadyGuessed(new ArrayList<>());
		return game;
	}

	public CheckWordResponse createResponse(HangmanGame game) {
		CheckWordResponse response = new CheckWordResponse();
		response.setAlreadyGuessed(game.getAlreadyGuessed());
		response.setCurrentGuess(game.getCurrentGuess());
		response.setMaskedWord(game.getMaskedWord());
		return response;
	}

}
