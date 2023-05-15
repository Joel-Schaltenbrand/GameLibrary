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
import ch.schaltenbrand.gamelibrary.model.tictactoe.Game;
import ch.schaltenbrand.gamelibrary.model.tictactoe.GamePlay;
import ch.schaltenbrand.gamelibrary.model.tictactoe.Player;

public interface TicTacToeService {

	Game createGame(Player player);

	Game connectToGame(Player player2, String gameId) throws InvalidParamException, InvalidGameException;

	Game connectToRandomGame(Player player2) throws NotFoundException;

	Game gamePlay(GamePlay gamePlay) throws NotFoundException, InvalidGameException;

}
