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

package ch.schaltenbrand.gamelibrary.rest.controller.hangman;

import ch.schaltenbrand.gamelibrary.model.hangman.CheckWordResponse;
import ch.schaltenbrand.gamelibrary.service.hangman.HangmanGamePlayService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hangman")
public class HangManGameControllerImpl implements HangManGameController {

	@Autowired
	private HangmanGamePlayService hangmanGamePlayServiceImpl;

	@Override
	public ResponseEntity<CheckWordResponse> init(HttpSession session) {
		return new ResponseEntity<>(hangmanGamePlayServiceImpl.init(session), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> cheat(String password, HttpSession session) {
		return new ResponseEntity<>(hangmanGamePlayServiceImpl.cheat(password, session), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CheckWordResponse> guess(String letter, HttpSession session) {
		return hangmanGamePlayServiceImpl.guess(Character.toUpperCase(letter.charAt(0)), session);
	}

}
