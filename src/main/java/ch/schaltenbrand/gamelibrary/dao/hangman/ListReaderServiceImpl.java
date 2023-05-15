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

package ch.schaltenbrand.gamelibrary.dao.hangman;

import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

@Repository
public class ListReaderServiceImpl implements ListReaderService {

	public ArrayList<String> getWordlist() {
		InputStream is = ListReaderServiceImpl.class.getClassLoader().getResourceAsStream("static/words.txt");
		assert is != null;
		Scanner scn = new Scanner(is);
		ArrayList<String> wordList = new ArrayList<>();
		while (scn.hasNext()) {
			wordList.add(scn.next().toUpperCase());
		}
		scn.close();
		Collections.shuffle(wordList);
		return wordList;
	}

}
