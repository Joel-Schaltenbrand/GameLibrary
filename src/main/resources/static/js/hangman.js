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

const alphabetDiv = document.getElementById("button-list");

function createButtons() {
    const alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXY";
    const alphabetArray = alphabet.split("");
    alphabetArray.forEach(letter => {
        const button = document.createElement("button");
        button.id = letter;
        button.innerHTML = letter;
        button.onclick = () => guess(letter);
        button.className = "btn btn-primary letter-button";
        button.accessKey = letter;
        alphabetDiv.appendChild(button);
    });
    createRefreshButton();
    document.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            window.location.reload();
        }
        const letter = event.key.toUpperCase();
        if (alphabet.includes(letter) && !document.getElementById(letter).disabled) {
            guess(letter);
        }
    });
}

function initialize() {
    const requestOptions = {
        method: 'GET',
    };
    fetch("/api/hangman/init", requestOptions)
        .then(response => response.json())
        .then(data => {
            updateWord(data.maskedWord);
            updateButtons(data.alreadyGuessed);
            updateHangman(data.currentGuess);
        })
}

function guess(letter) {
    let statuscode;
    const formdata = new FormData();
    formdata.append("letter", letter);
    const requestOptions = {
        method: 'POST',
        body: formdata,
    };
    fetch("/api/hangman/guess", requestOptions)
        .then(response => {
            statuscode = response.status;
            return response.json();
        })
        .then(data => {
            updateWord(data.maskedWord);
            updateHangman(data.currentGuess);
            updateButtons(data.alreadyGuessed);
            if (statuscode === 409) {
                showEnd("You lost!");
                const audio = new Audio("/sounds/LOSE.wav");
                audio.play();
            } else if (statuscode === 202) {
                const image = document.getElementById("hangman");
                const audio = new Audio("/sounds/LEVELUP.wav");
                audio.play();
                image.src = "/images/hangman/firework.gif";
                showEnd("You won!");
            }
        })
}

function updateWord(maskedWord) {
    const formattedWord = maskedWord.split("").join(" ");
    const wordDiv = document.getElementById("word");
    wordDiv.innerHTML = formattedWord;
}

function updateButtons(alreadyGuessed) {
    alreadyGuessed.forEach(letter => {
        const button = document.getElementById(letter);
        button.disabled = true;
    });
}

function updateHangman(currentGuess) {
    const hangman = document.getElementById("hangman");
    hangman.src = "/images/hangman/hangman-" + currentGuess + ".png";
}

function showEnd(text) {
    $(':button').remove();
    createRefreshButton();
    const endDiv = document.getElementById("imagecontainer");
    const endText = document.createElement("h3");
    endText.innerHTML = text;
    const endText2 = document.createElement("h4");
    endText2.innerHTML = 'To play again, press Enter or click on the red button.';
    endDiv.append(endText, endText2)
}

function createRefreshButton() {
    const button = document.createElement("button");
    button.id = "refresh";
    button.innerHTML = '<i class="fa fa-refresh" aria-hidden="true"></i>';
    button.onclick = () => window.location.reload();
    button.className = "btn btn-danger letter-button";
    alphabetDiv.appendChild(button);
}

function dataLoading() {
    const titleElement = document.getElementById("gameTitle");
    let clickCount = 0;
    titleElement.addEventListener("click", function () {
            clickCount++;
            if (clickCount === 5) {
                clickCount = 0;
                const formdata = new FormData();
                formdata.append("password", prompt("Password: "));
                const requestOptions1 = {
                    method: 'POST',
                    body: formdata,
                };
                fetch("/api/hangman/cht", requestOptions1)
                    .then(response => response.json())
                    .then(data => {
                        if (data.msg !== "BAD") {
                            alert("The word is: " + data.word);
                        } else {
                            alert("Wrong password!");
                        }
                    })
            }
        }
    )
}

createButtons();
initialize();
dataLoading();
