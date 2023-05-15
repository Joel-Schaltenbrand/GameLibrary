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

package ch.schaltenbrand.gamelibrary.service.tictactoe;

import ch.schaltenbrand.gamelibrary.exeption.InvalidGameException;
import ch.schaltenbrand.gamelibrary.exeption.InvalidParamException;
import ch.schaltenbrand.gamelibrary.exeption.NotFoundException;
import ch.schaltenbrand.gamelibrary.model.tictactoe.*;
import ch.schaltenbrand.gamelibrary.storage.tictactoe.TicTacToeStorage;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TicTacToeServiceImpl implements TicTacToeService {

	@Override
	public Game createGame(Player player) {
		Game game = new Game();
		game.setBoard(new int[3][3]);
		game.setGameId(UUID.randomUUID().toString());
		game.setPlayerX(player);
		game.setStatus(GameStatus.NEW);
		TicTacToeStorage.getTicTacToeInstance().setGame(game);
		return game;
	}

	@Override
	public Game connectToGame(Player player2, String gameId) throws InvalidParamException, InvalidGameException {
		if (!TicTacToeStorage.getTicTacToeInstance().getGames().containsKey(gameId)) {
			throw new InvalidParamException("Game with provided id doesn't exist");
		}
		Game game = TicTacToeStorage.getTicTacToeInstance().getGames().get(gameId);
		if (game.getPlayerO() != null) {
			throw new InvalidGameException("Game is not valid anymore");
		}
		game.setPlayerO(player2);
		game.setStatus(GameStatus.IN_PROGRESS);
		TicTacToeStorage.getTicTacToeInstance().setGame(game);
		return game;
	}

	@Override
	public Game connectToRandomGame(Player player2) throws NotFoundException {
		Game game = TicTacToeStorage.getTicTacToeInstance().getGames().values().stream()
				.filter(it -> it.getStatus().equals(GameStatus.NEW))
				.findFirst().orElseThrow(() -> new NotFoundException("Game not found"));
		game.setPlayerO(player2);
		game.setStatus(GameStatus.IN_PROGRESS);
		TicTacToeStorage.getTicTacToeInstance().setGame(game);
		return game;
	}

	@Override
	public Game gamePlay(GamePlay gamePlay) throws NotFoundException, InvalidGameException {
		if (!TicTacToeStorage.getTicTacToeInstance().getGames().containsKey(gamePlay.getGameId())) {
			throw new NotFoundException("Game not found");
		}
		Game game = TicTacToeStorage.getTicTacToeInstance().getGames().get(gamePlay.getGameId());
		if (game.getStatus().equals(GameStatus.FINISHED)) {
			throw new InvalidGameException("Game is already finished");
		}
		int[][] board = game.getBoard();
		board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = gamePlay.getType().getValue();
		Boolean xWinner = checkWinner(game.getBoard(), TicToe.X);
		Boolean oWinner = checkWinner(game.getBoard(), TicToe.O);
		Boolean isDraw = checkDraw(game.getBoard());
		if (xWinner) {
			game.setWinner(game.getPlayerX());
			game.setStatus(GameStatus.FINISHED);
		} else if (oWinner) {
			game.setWinner(game.getPlayerO());
			game.setStatus(GameStatus.FINISHED);
		} else if (isDraw) {
			game.setStatus(GameStatus.FINISHED);
		}
		game.setLastPlayer(gamePlay.getType());
		TicTacToeStorage.getTicTacToeInstance().setGame(game);
		return game;
	}

	private Boolean checkDraw(int[][] board) {
		int[] boardArray = new int[9];
		int counterIndex = 0;
		for (int[] ints : board) {
			for (int anInt : ints) {
				boardArray[counterIndex] = anInt;
				counterIndex++;
			}
		}
		for (int j : boardArray) {
			if (j == 0) {
				return false;
			}
		}
		return true;
	}

	private Boolean checkWinner(int[][] board, TicToe ticToe) {
		int[] boardArray = new int[9];
		int counterIndex = 0;
		for (int[] ints : board) {
			for (int anInt : ints) {
				boardArray[counterIndex] = anInt;
				counterIndex++;
			}
		}
		int[][] winCombinations = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
		for (int[] winCombination : winCombinations) {
			int counter = 0;
			for (int i : winCombination) {
				if (boardArray[i] == ticToe.getValue()) {
					counter++;
					if (counter == 3) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
