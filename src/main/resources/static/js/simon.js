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

let playable = false;
const gameBoard = document.getElementById("game-board");
const counter = document.getElementById("counter");
const startBtn = document.getElementById("start");
const trafficLight = document.getElementById("trafficLight");
let sequence = [];

function gameOver() {
    const audio = new Audio("/sounds/LOSE.wav");
    audio.play();
    const back = document.getElementById("back");
    const simonGame = document.getElementById("simon-game");
    const gameOver = document.createElement("h1");
    const refresh = document.createElement("a");
    gameBoard.remove();
    counter.remove();
    back.remove();
    trafficLight.classList.remove("trafficLightYellow");
    trafficLight.classList.remove("trafficLightGreen");
    trafficLight.classList.remove("trafficLightRed");
    trafficLight.classList.add("trafficLightFlashing")
    refresh.innerHTML = '<a class="btn btn-danger mt-3" th:href="@{/}"><i class="fa fa-refresh"></i> Try again</a>';
    refresh.onclick = () => window.location.reload();
    gameOver.innerHTML = "Game Over... You reached level " + counter.innerHTML + "!";
    simonGame.append(gameOver, refresh, back);
}

function lightUp(color) {
    const audio = new Audio("/sounds/" + color + ".mp3");
    audio.play();
    document.getElementById(color).classList.add("light");
    setTimeout(function () {
        document.getElementById(color).classList.remove("light");
    }, 500);
}

function checkColor(color) {
    if (color === sequence[0]) {
        sequence.shift();
        if (sequence.length === 0) {
            trafficLight.classList.remove("trafficLightGreen");
            trafficLight.classList.add("trafficLightYellow");
            getSeq();
        }
    } else {
        gameOver();
    }
}

function playSequence(sequence) {
    playable = false;
    trafficLight.classList.remove("trafficLightGreen");
    trafficLight.classList.remove("trafficLightYellow");
    trafficLight.classList.add("trafficLightRed");
    setTimeout(function () {
        sequence.forEach(
            function (color, index) {
                setTimeout(
                    function () {
                        lightUp(color);
                    }, 600 * index);
            });
        trafficLight.classList.add("trafficLightYellow");
        setTimeout(function () {
            playable = true;
            trafficLight.classList.remove("trafficLightRed");
            trafficLight.classList.remove("trafficLightYellow");
            trafficLight.classList.add("trafficLightGreen");
        }, (600 * sequence.length) + 500);
    }, 1000);
}

function getSeq() {
    const requestOptions = {
        method: 'GET',
    };
    fetch("/api/simon/getSeq",
        requestOptions)
        .then(response => response.json())
        .then(data => {
            counter.innerHTML = data.level;
            sequence = data.colors;
            setTimeout(function () {
                playSequence(data.colors);
            }, 1000);
        })
}

function startGame() {
    startBtn.remove();
    gameBoard.hidden = false;
    trafficLight.hidden = false;
    getSeq();
}

function initialize() {
    const requestOptions = {
        method: 'GET',
    };
    fetch("/api/simon/reset", requestOptions).then(() =>
        startBtn.disabled = false
    )
}

document.querySelectorAll('.game-tile').forEach(button => {
    button.addEventListener('click', function () {
        if (playable) {
            const color = this.id.toUpperCase();
            lightUp(color);
            checkColor(color);
        }
    });
});

initialize();
