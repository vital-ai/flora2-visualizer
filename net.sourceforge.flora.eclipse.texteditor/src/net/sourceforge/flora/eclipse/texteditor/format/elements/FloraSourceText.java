/* File:      FloraSourceText.java
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

package net.sourceforge.flora.eclipse.texteditor.format.elements;

import java.util.ArrayList;

import net.sourceforge.flora.eclipse.texteditor.format.FloraIndentationHandler;

/**
 * @author Daniel Winkler
 *
 * Holds text splitted up in {@link FloraToken}s
 */
public class FloraSourceText {
	public ArrayList<FloraToken> text;

	protected FloraIndentationHandler indentationHandler;

	/**
	 * the constructor</br>
	 * creates new instances of {@link #text} and {@link #indentationHandler}
	 */
	public FloraSourceText() {
		text = new ArrayList<FloraToken>();
		indentationHandler = new FloraIndentationHandler();
	}

	/**
	 * adds the given token to {@link #text}
	 * 
	 * @param token the token to add
	 */
	public void addToken(FloraToken token) {
//		if (!((token instanceof FloraSourceToken)&&(token.getText().length() <= 0)))
		text.add(token);
	}

	/**
	 * adjusts the whitespaces, converts {@link #text} to a String and returns the String
	 * 
	 * @see #adjustWhitespaces()
	 * @return the formatted String held in {@link #text}
	 */
	public String getTextAsString() {
		adjustWhitespaces();
		StringBuffer buffer = new StringBuffer();

		for (FloraToken token : text)
			buffer.append(token.toString());

		return buffer.toString();
	}

	/**
	 * adjusts the whitespace tokens to format the text
	 */
	public void adjustWhitespaces() {
		indentationHandler = new FloraIndentationHandler();
		StringBuffer lineBuffer = new StringBuffer();

		FloraToken predecessorToken = null;
		FloraToken currentToken = null;
		FloraToken successorToken = null;

		// not needed any more
//		int lookAhead = 0;

		for (int i = 0; i < text.size(); ++i) {
			predecessorToken = currentToken;
			currentToken = successorToken;
			successorToken = text.get(i);

			if (currentToken != null) {
				if (currentToken instanceof FloraSourceToken) {
					// not needed any more
//					if (i > lookAhead)
						indentationHandler.handle(currentToken, lineBuffer, successorToken == null ? false
								: (successorToken instanceof FloraWhitespaceToken ? ((FloraWhitespaceToken) successorToken).hasNewline() : false));
					lineBuffer.append(currentToken.getText());
				} else if (currentToken instanceof FloraWhitespaceToken) {

					FloraWhitespaceToken whitespaceToken = (FloraWhitespaceToken) currentToken;

					if (successorToken instanceof FloraCommentToken) {
						// do nothing
					} else {
						if (whitespaceToken.hasNewline()) {
							lineBuffer = new StringBuffer();

							// not needed any more
							// look ahead (handle following decreasers)
//							FloraToken test;
//							for (int j = i; j < text.size(); ++j) {
//								test = text.get(j);
//
//								if (test instanceof FloraSourceToken) {
//									FloraSourceToken floraSourceToken = (FloraSourceToken) test;
//									if (indentationHandler.isDecreaser(floraSourceToken.getText())) {
//										indentationHandler.handle(floraSourceToken.getText(), new StringBuffer(), false);
//										lookAhead = j;
//									} else
//										break;
//								} else if (test instanceof FloraWhitespaceToken) {
//									FloraWhitespaceToken floraWhitespaceToken = (FloraWhitespaceToken) test;
//									if (floraWhitespaceToken.hasNewline())
//										break;
//								} else {
//									break;
//								}
//							}

							FloraToken test= text.get(i);
							if (test instanceof FloraSourceToken) {
								FloraSourceToken floraSourceToken = (FloraSourceToken) test;
								if (indentationHandler.isDecreaser(floraSourceToken.getText())) {
									indentationHandler.clean(floraSourceToken.getText());
								}
							}
							
							whitespaceToken.setText(whitespaceToken.getTrimmedText() + indentationHandler.getIndentation());
							lineBuffer.append(indentationHandler.getIndentation());
						} else {
							boolean whitespace = true;
							if (predecessorToken != null) {
								if (predecessorToken instanceof FloraSourceToken) {
									FloraSourceToken pred = (FloraSourceToken) predecessorToken;
									if (!pred.hasSucceedingWhitespace()) {
										whitespace = false;
									}
								}
							} else
								whitespace = false;
							
							if (successorToken != null) {
								if (successorToken instanceof FloraSourceToken) {
									FloraSourceToken succ = (FloraSourceToken) successorToken;
									if (!succ.hasPredeceedingWhitespace()) {
										whitespace = false;
									}
								}
							} else
								whitespace = false;
							
							if (whitespace)
								whitespaceToken.setText(whitespaceToken.getTrimmedText());
							else
								whitespaceToken.setText("");
							lineBuffer.append(whitespaceToken.getText());
						}
					}
				} else {
					lineBuffer.append(currentToken.getText());
				}
			}
		}
	}

	/**
	 * returns the {@link #text} as String
	 * 
	 * @see #getTextAsString()
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getTextAsString();
	}

	/**
	 * @param nextToken the next Token (may be null)
	 * @return the current indentation as String
	 */
	public String getIndentation(String nextToken) {
		FloraIndentationHandler tempHandler = new FloraIndentationHandler();
		StringBuffer lineBuffer = new StringBuffer();

		FloraToken currentToken = null;
		FloraToken successorToken = null;

		for (int i = 0; i <= text.size(); ++i) {
			currentToken = successorToken;
			if (i < text.size())
				successorToken = text.get(i);
			else
				successorToken = null;

			if (currentToken != null) {
				if (currentToken instanceof FloraSourceToken)
					tempHandler.handle(currentToken, lineBuffer, successorToken == null ? true
							: (successorToken instanceof FloraWhitespaceToken ? ((FloraWhitespaceToken) successorToken).hasNewline() : false));
				lineBuffer.append(currentToken.getText());
			}
		}
		
		if (nextToken != null)
			tempHandler.clean(nextToken);
		return tempHandler.getIndentation();
	}

	/**
	 * @return the current {@link FloraIndentationHandler}
	 */
	public FloraIndentationHandler getIndentationHandler() {
		return indentationHandler;
	}

	public ArrayList<FloraToken> getText() {
		return text;
	}
}
