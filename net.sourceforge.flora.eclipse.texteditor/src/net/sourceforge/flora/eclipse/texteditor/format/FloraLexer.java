/* File:      FloraLexer.java
 **
 ** Author(s): Daniel Winkler
 ** Contact:   flora-users@lists.sourceforge.net
 **
 ** Copyright (C) 2007 Digital Enterprise Research Insitute (DERI) Innsbruck
 **
 ** FLORA-2 Visualizer is free software; you can redistribute it and/or
 ** modify it under the terms of the GNU Lesser General Public License
 ** as published by the Free Software Foundation; either version 2
 ** of the License, or (at your option) any later version.
 ** This program is distributed in the hope that it will be useful,
 ** but WITHOUT ANY WARRANTY; without even the implied warranty of
 ** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 ** GNU Lesser General Public License for more details.
 ** You should have received a copy of the GNU General Public License
 ** along with this program; if not, write to the Free Software
 ** Foundation, Inc., 51 Franklin Street, 5th Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.flora.eclipse.texteditor.format;

import java.io.IOException;
import java.io.Reader;

import net.sourceforge.flora.eclipse.FloraKeywords;
import net.sourceforge.flora.eclipse.texteditor.format.elements.FloraCommentToken;
import net.sourceforge.flora.eclipse.texteditor.format.elements.FloraSourceText;
import net.sourceforge.flora.eclipse.texteditor.format.elements.FloraStringToken;
import net.sourceforge.flora.eclipse.texteditor.format.elements.FloraSourceToken;
import net.sourceforge.flora.eclipse.texteditor.format.elements.FloraWhitespaceToken;

/**
 * @author Daniel Winkler
 *
 * The Flora-2 Document Lexer splits the text into different Tokens.
 */
public class FloraLexer {

	protected Reader reader;

	/**
	 * The constructor. It sets the reader.
	 * 
	 * @see #setReader(Reader)
	 * 
	 * @param reader the {@link Reader} which reads the text to split into Tokens
	 */
	public FloraLexer(Reader reader) {
		setReader(reader);
	}

	/**
	 * @param reader sets the Reader
	 */
	public void setReader(Reader reader) {
		this.reader = reader;
	}

	/**
	 *  splits the text into tokens and adds the tokens to a
	 *  {@link FloraSourceText} instance. this will be returned when
	 *  the text is finished 
	 * 
	 * @return a <code>FloraSourceText</code> instance containing the text tokens
	 * @throws IOException
	 */
	protected FloraSourceText lex() throws IOException {
		FloraSourceText text = new FloraSourceText();
		while (reader.read() >= 0) {
			reader.reset();

			if (testForComment()) {
				text.addToken(new FloraWhitespaceToken(readWhitespaceToken()));
				text.addToken(new FloraCommentToken(readComment()));
			} else if (testForString()) {
				text.addToken(new FloraWhitespaceToken(readWhitespaceToken()));
				text.addToken(new FloraStringToken(readString()));
			} else {
				text.addToken(new FloraWhitespaceToken(readWhitespaceToken()));
				text.addToken(new FloraSourceToken(readToken()));
			}
			reader.mark(0);
		}
		return text;
	}

	/**
	 * reads whitespace characters and returns them
	 * 
	 * @return the whitespaces
	 * @throws IOException
	 * @see FloraDocumentFormatter#isWhitespace(char)
	 */
	protected String readWhitespaceToken() throws IOException {
		StringBuffer whitespaces = new StringBuffer();
		char c = (char) -1;
		int s;

		reader.mark(0);
		while ((s = reader.read()) >= 0) {
			c = (char) s;

			if (FloraDocumentFormatter.isWhitespace(c)) {
				whitespaces.append(c);
			} else {
				break;
			}
			reader.mark(0);
		}
		reader.reset();

		return whitespaces.toString();
	}

	/**
	 * reads characters until the String ends (<code> " | ' </code>) and returns them
	 * 
	 * @return the string
	 * @throws IOException
	 */
	protected String readString() throws IOException {
		int s;
		char c;
		char quoteSign;
		boolean isEscaped = false;
		StringBuffer temp = new StringBuffer();

		reader.mark(1);
		c = (char) reader.read();
		if (!(Character.toString(c).equals(FloraKeywords.SINGLE_QUOTE) || Character.toString(c).equals(FloraKeywords.DOUBLE_QUOTE))) {
			reader.reset();
			return "";
		}

		quoteSign = c;
		temp.append(c);

		while ((s = reader.read()) >= 0) {
			c = (char) s;
			temp.append(c);
			if (c == '\\') {
				isEscaped = !isEscaped;
			} else if (isEscaped) {
				isEscaped = !isEscaped;
			} else if (c == quoteSign) {
				isEscaped = !isEscaped;
				reader.mark(0);
				char test = (char) reader.read();
				reader.reset();

				if (test == quoteSign) // double quote escape
					continue;
				else
					break;
			}

		}

		return temp.toString();
	}

	/**
	 * reads comment characters until the end of the comment and returns them
	 * 
	 * @return the whitespaces
	 * @throws IOException
	 */
	protected String readComment() throws IOException {
		char c;
		StringBuffer buffer = new StringBuffer();

		reader.mark(2);

		c = (char) reader.read();
		buffer.append(c);

		if (c != '/') {
			reader.reset();
			return "";
		}

		c = (char) reader.read();
		buffer.append(c);
		if (c == '/') {
			do {
				reader.mark(0);
				c = (char) reader.read();
				if (!FloraDocumentFormatter.isLineDelimiterChar(c))
					buffer.append(c);
				else {
					reader.reset();
					break;
				}
			} while (c != (char) -1);
		} else if (c == '*') {
			do {
				reader.mark(0);
				c = (char) reader.read();
				buffer.append(c);

				if (c == '/')
					if (buffer.toString().endsWith("*/"))
						break;

			} while (c != (char) -1);
		} else {
			reader.reset();
			return "";
		}

		return buffer.toString();
	}

	/**
	 * reads a single text token
	 * 
	 * @return the text token
	 * @throws IOException
	 */
	protected String readToken() throws IOException {
		StringBuffer word = new StringBuffer();
		char c;
		int s;

		reader.mark(0);
		while ((s = reader.read()) >= 0) {
			c = (char) s;

			if ((FloraDocumentFormatter.isWhitespace(c)))
				break;

			if ((word.length() > 0)) {
				if (FloraKeywords.isDelimiterStart(word.toString())) { // reading delimiter
					
					if (FloraKeywords.DOT.startsWith(word.toString())){ // . read, do not break if no whitespace
						if (Character.isWhitespace(c))
							break;
						// else append the word
					} else if (FloraKeywords.QUERY_DEFINITION.startsWith(word.toString() + c)) {
						// do nothing (read e.g. ?X ?-)
					} else if (FloraKeywords.UNDERSCORE_HASH.startsWith(word.toString() + c)) {
						// do nothing (read e.g. _X _#)
					}
					else if (!FloraKeywords.isDelimiterStart(word.toString() + c)) { // end of delimiter detected
						break;
					}
				} else { // reading no delimiter
					if (FloraKeywords.isDelimiterStart(c)) { // test reading start of delimiter
						char nc = (char) reader.read();

						if (c == FloraKeywords.UNDERSCORE_HASH.charAt(0)) { // check for _#
							if ((nc == (char) -1) || nc == FloraKeywords.UNDERSCORE_HASH.charAt(1))
								break;
							else {
								word.append(c);
								word.append(nc);
								reader.mark(0);
								continue;
							}
						} else if (c == FloraKeywords.DOT.charAt(0)) { // check for .whitespace
							if ((nc == (char) -1) || FloraDocumentFormatter.isWhitespace(nc))
								break;
							else {
								word.append(c);
								word.append(nc);
								reader.mark(0);
								continue;
							}
						} else
							break;
					}
				}
			}
			word.append(c);
			
			reader.mark(0);
		}
		reader.reset();

		return word.toString();
	}

	/**
	 * tests if the next token is a comment
	 * 
	 * @return true if the next token is a comment
	 * @throws IOException
	 */
	protected boolean testForComment() throws IOException {
		boolean comment = false;
		char test;

		reader.mark(0);
		do {
			test = (char) reader.read();
		} while (FloraDocumentFormatter.isWhitespace(test));

		if (test == '/') {
			test = (char) reader.read();
			if (test == '/') {
				comment = true;
			} else if (test == '*') {
				comment = true;
			}
		}

		reader.reset();

		return comment;
	}

	/**
	 * tests if the next token is a string
	 * 
	 * @return true if the next token is a string
	 * @throws IOException
	 */
	protected boolean testForString() throws IOException {
		boolean comment = false;
		char test;

		reader.mark(0);
		do {
			test = (char) reader.read();
		} while (FloraDocumentFormatter.isWhitespace(test));

		if (test == '"') {
			comment = true;
		} else if (test == '\'') {
			comment = true;
		}

		reader.reset();

		return comment;
	}
}
