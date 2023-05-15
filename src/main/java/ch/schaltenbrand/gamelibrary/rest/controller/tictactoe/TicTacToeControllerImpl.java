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

package ch.schaltenbrand.gamelibrary.rest.controller.tictactoe;

import ch.schaltenbrand.gamelibrary.dto.tictactoe.ConnectRequest;
import ch.schaltenbrand.gamelibrary.exeption.InvalidGameException;
import ch.schaltenbrand.gamelibrary.exeption.InvalidParamException;
import ch.schaltenbrand.gamelibrary.exeption.NotFoundException;
import ch.schaltenbrand.gamelibrary.model.tictactoe.Game;
import ch.schaltenbrand.gamelibrary.model.tictactoe.GamePlay;
import ch.schaltenbrand.gamelibrary.model.tictactoe.Player;
import ch.schaltenbrand.gamelibrary.service.tictactoe.TicTacToeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tictactoe")
public class TicTacToeControllerImpl implements TicTacToeController {

	@Autowired
	private final TicTacToeService ticTacToeServiceImpl;

	@Autowired
	private final SimpMessagingTemplate simpMessagingTemplate;

	@Override
	public ResponseEntity<Game> start(@RequestBody Player player) {
		return ResponseEntity.ok(ticTacToeServiceImpl.createGame(player));
	}

	@Override
	public ResponseEntity<Game> connect(@RequestBody ConnectRequest request) throws InvalidParamException, InvalidGameException {
		String json = String.format("{\"name\": \"%s\"}", request.getPlayer().getName());
		simpMessagingTemplate.convertAndSend("/topic/name/" + request.getGameId(), json);
		return ResponseEntity.ok(ticTacToeServiceImpl.connectToGame(request.getPlayer(), request.getGameId()));
	}

	@Override
	public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws NotFoundException {
		Game game = ticTacToeServiceImpl.connectToRandomGame(player);
		String json = String.format("{\"name\": \"%s\"}", player.getName());
		simpMessagingTemplate.convertAndSend("/topic/name/" + game.getGameId(), json);
		return ResponseEntity.ok(game);
	}

	@Override
	public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws NotFoundException, InvalidGameException {
		Game game = ticTacToeServiceImpl.gamePlay(request);
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
		return ResponseEntity.ok(game);
	}

}
