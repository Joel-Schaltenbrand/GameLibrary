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

let stompClient;
let stompClient2;
let gameId;
let playerType;
const gameBoard = document.getElementById("gameBoard");
const startBox = document.getElementById("startBox");
const opponent = document.getElementById("oponentLogin");
const copyLink = document.getElementById("copylink");
const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

if (urlParams.has('id')) {
    name = window.prompt("Please enter your name");
    connectDirectToGame(urlParams.get('id'));
}

function connectToSocket(gameId) {
    let socket = new SockJS("/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            displayResponse(data);
        })
    })
}

function connectToSocketstartBox(gameId) {
    let socket = new SockJS("/gameplay");
    stompClient2 = Stomp.over(socket);
    stompClient2.connect({}, function () {
        stompClient2.subscribe("/topic/name/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            opponent.innerHTML = "<hr>Your opponent is: " + data.name;
            gameOn = true;
            copyLink.remove();
            alert("Your opponent is: " + data.name);
        })
    })
}

function create_game() {
    $.ajax({
        url: "/api/tictactoe/start",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "name": name,
        }),
        success: function (data) {
            copyLink.hidden = false;
            gameId = data.gameId;
            playerType = 'X';
            reset();
            connectToSocket(gameId);
            connectToSocketstartBox(gameId);
            gameBoard.hidden = false;
            startBox.hidden = true;
            opponent.innerHTML = "Waiting for opponent.<br><br> Game ID is: <br>" + data.gameId;
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function connected(data) {
    opponent.innerHTML = "<hr>Your opponent is: " + data.playerX.name;
    gameBoard.hidden = false;
    copyLink.remove();
    startBox.remove();
    gameId = data.gameId;
    playerType = 'O';
    reset();
    connectToSocket(gameId);
    alert("Congrats you're playing with: " + data.playerX.name);
}


function connectToRandom() {
    $.ajax({
        url: "/api/tictactoe/connect/random",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "name": name
        }),
        success: function (data) {
            connected(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function connectToSpecificGame() {
    gameId = document.getElementById("game_id").value;
    if (gameId == null || gameId === '') {
        alert("Please enter game id");
    }
    $.ajax({
        url: "/api/tictactoe/connect",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "player": {
                "name": name
            },
            "gameId": gameId
        }),
        success: function (data) {
            connected(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function connectDirectToGame(gameId) {
    $.ajax({
        url: "/api/tictactoe/connect",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "player": {
                "name": name
            },
            "gameId": gameId
        }),
        success: function (data) {
            connected(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}
