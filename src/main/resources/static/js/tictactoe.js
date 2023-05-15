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
let turns = [["#", "#", "#"], ["#", "#", "#"], ["#", "#", "#"]];
let turn = "";
let gameOn = false;
let name;

function playerTurn(turn, id) {
    const spotTaken = $("#" + id).text();
    if (spotTaken === "#") {
        makeAMove(playerType, id.split("_")[0], id.split("_")[1]);
    }
}

function makeAMove(type, xCoordinate, yCoordinate) {
    $.ajax({
        url: "/api/tictactoe/gameplay",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "type": type,
            "coordinateX": xCoordinate,
            "coordinateY": yCoordinate,
            "gameId": gameId
        }),
        error: function (error) {
            console.log(error);
        }
    })
}

function displayResponse(data) {
    gameOn = data.lastPlayer !== playerType;
    let board = data.board;
    for (let i = 0; i < board.length; i++) {
        for (let j = 0; j < board[i].length; j++) {
            let id = i + "_" + j;
            if (board[i][j] === 1) {
                turns[i][j] = 'X'
                $("#" + id).get(0).classList.add("x");
            } else if (board[i][j] === 2) {
                turns[i][j] = 'O';
                $("#" + id).get(0).classList.add("o");
            }
            $(`#${id}`).text(turns[i][j]);
        }
    }
    if (data.winner != null && data.status === "FINISHED") {
        alert("Winner is " + data.winner.name);
        opponent.innerHTML = "<hr>Winner is: " + data.winner.name;
    } else if (data.status === "FINISHED" && data.winner == null) {
        alert("Draw");
        opponent.innerHTML = "<hr>Draw";
    }
}

$(".tic").click(function () {
    if (gameOn) {
        const slot = $(this).attr('id');
        playerTurn(turn, slot);
    }
});

function reset() {
    turns = [["#", "#", "#"], ["#", "#", "#"], ["#", "#", "#"]];
    $(".tic").text("#");
}

function copyID() {
    let baseurl = window.location.href.split("?")[0];
    unsecuredCopyToClipboard(baseurl + "?id=" + gameId);
}

function unsecuredCopyToClipboard(text) {
    const textArea = document.createElement("textarea");
    textArea.value = text;
    document.body.appendChild(textArea);
    textArea.focus();
    textArea.select();
    try {
        document.execCommand('copy');
        alert("Copied to clipboard");
    } catch (err) {
        console.error('Unable to copy to clipboard', err);
    }
    document.body.removeChild(textArea);
}

function saveName() {
    name = document.getElementById("name").value;
    document.getElementById("buttonBox").hidden = false;
    document.getElementById("nameBox").hidden = true;
    document.getElementById("nameText").innerText = "Welcome " + name;
}
