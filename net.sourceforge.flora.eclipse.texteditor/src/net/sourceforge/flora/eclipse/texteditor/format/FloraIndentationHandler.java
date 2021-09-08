/* File:      FloraIndentationHandler.java
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

import java.util.Stack;

import net.sourceforge.flora.eclipse.FloraKeywords;
import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;
import net.sourceforge.flora.eclipse.texteditor.FloraSourceEditorPlugin;
import net.sourceforge.flora.eclipse.texteditor.format.elements.FloraSourceToken;
import net.sourceforge.flora.eclipse.texteditor.format.elements.FloraToken;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * @author Daniel Winkler
 *
 * This class manages the indentations of a Flora-2 source text
 */
public class FloraIndentationHandler {

	public static final String[] increasers = new String[] { FloraKeywords.RULE_DEFINITION, FloraKeywords.QUERY_DEFINITION,
			FloraKeywords.PARENTHESIS_OPEN, FloraKeywords.SQUARE_BRACKET_OPEN, FloraKeywords.CURLY_BRACKET_OPEN, FloraKeywords.ARROW,
			FloraKeywords.STAR_ARROW, FloraKeywords.ARROW2, FloraKeywords.STAR_ARROW2, FloraKeywords.PLUS_ARROW_ARROW,
			FloraKeywords.STAR_PLUS_ARROW_ARROW, FloraKeywords.DOUBLE_ARROW, FloraKeywords.STAR_DOUBLE_ARROW };

	public static final String[] decreasers = new String[] { FloraKeywords.DOT, FloraKeywords.COMMA, FloraKeywords.PARENTHESIS_CLOSE,
			FloraKeywords.SQUARE_BRACKET_CLOSE, FloraKeywords.CURLY_BRACKET_CLOSE };

	private Stack<FloraIndentationModification> indentations;

	private int tabSize;

	private String indentationType;

	/**
	 * the constructor</br>
	 * sets values for <code>tabSize</code> and <code>indentationType</code>
	 * with values from the PreferenceStore</br>
	 * initializes the indentations stack
	 */
	public FloraIndentationHandler() {
		IPreferenceStore store = FloraSourceEditorPlugin.getDefault().getPreferenceStore();
		tabSize = store.getInt(FloraPreferenceConstants.EDITOR_INDENTATION_SIZE);
		indentationType = store.getString(FloraPreferenceConstants.EDITOR_INDENTATION_TYPE);
		indentations = new Stack<FloraIndentationModification>();
	}

	/**
	 * pushes the given value on the {@link #indentations} Stack
	 * 
	 * @param indentation the value for the indentation
	 * @see Stack#push(Object)
	 */
	public void setIndentation(int indentation) {
		indentations.push(new FloraIndentationModification(indentation, new FloraSourceToken(""), ""));
	}

	/**
	 * analyzes the given token and either pushes to or pops from the {@link #indentations} Stack 
	 * 
	 * @param currentToken the token to handle
	 * @param buffer the current <code>StringBuffer</code>
	 * @param followedByNewline true if the given <code>FloraToken</code> is followed by a newline
	 */
	public void handle(FloraToken currentToken, StringBuffer buffer, boolean followedByNewline) {
		for (String inc : increasers)
			if (currentToken.equals(inc))
				push(currentToken, buffer, followedByNewline);
		for (String dec : decreasers)
			if (currentToken.equals(dec))
				pop(currentToken);
	}
	
	/**
	 * Pops all {@link FloraIndentationModification}s from Stack until a
	 * <code>FloraIndentationModification</code>
	 * {@link FloraIndentationModification#getIndentationDecreaser()} matches
	 * current Token
	 * 
	 * @param token the token to compare with
	 */
	public void clean(String token) {
		if (isDecreaser(token)) {
				FloraIndentationModification mod = null;

				while (indentations.size() > 0) {
					mod = indentations.peek();

					if (token.equals(mod.getIndentationDecreaser()))
						break;
					else
						indentations.pop();
				}
			}
	}

	/**
	 * tests if the given token is a increaser token
	 * 
	 * @param token the token to test
	 * @return true if the given token is a increaser token
	 */
	public boolean isIncreaser(String token) {
		for (String inc : increasers)
			if (token.equals(inc))
				return true;
		return false;
	}

	/**
	 * tests if the given token is a decreaser token
	 * 
	 * @param token the token to test
	 * @return true if the current token is a decreaser token
	 */
	public boolean isDecreaser(String token) {
		for (String dec : decreasers)
			if (token.equals(dec))
				return true;
		return false;
	}

	/**
	 * pushes the given token on the {@link #indentations} <code>Stack</code>
	 * 
	 * @param currentToken the token to push on <code>Stack</code>
	 * @param buffer the current <code>StringBuffer</code>
	 * @param followedByNewline true if the given token is followed by a newline
	 */
	protected void push(FloraToken currentToken, StringBuffer buffer, boolean followedByNewline) {
		int indentation = 0;
		if (followedByNewline) {
			indentation = getIndentationSize();
			indentation += tabSize;
		} else {
			int substringStart = buffer.lastIndexOf("\n") + 1;

			String line = buffer.substring(substringStart);
			if (line.indexOf('\r') > 0)
				line = line.substring(buffer.lastIndexOf("\r"));

			for (Character c : line.toCharArray()) {
				indentation += (c == '\t') ? tabSize : 1;
			}
			indentation += currentToken.getText().length();
			if (currentToken instanceof FloraSourceToken) {
				FloraSourceToken floraSourceToken = (FloraSourceToken) currentToken;
				indentation += (floraSourceToken.hasSucceedingWhitespace() ? 1 : 0);
			}
		}

		indentations.push(new FloraIndentationModification(indentation, currentToken, getDecreaser(currentToken)));
	}

	/**
	 * pops the matching increaser token for the given token from {@link Stack}
	 * 
	 * @param currentToken the token to pop matching increaser token
	 * @return the popped <code>FloraIndentationModification</code> or null if none matched
	 * @see FloraIndentationModification#getIndentationDecreaser()
	 */
	protected FloraIndentationModification pop(FloraToken currentToken) {
		FloraIndentationModification mod = null;

		if (currentToken.equals(FloraKeywords.COMMA)) {
			if (indentations.size() > 0) {
				mod = indentations.peek();
				if (currentToken.equals(mod.getIndentationDecreaser()))
					mod = indentations.pop();
			}
		} else {

			do {
				if (indentations.size() > 0)
					mod = indentations.pop();
				else {
					mod = null;
					break;
				}
			} while (!currentToken.equals( mod.getIndentationDecreaser()));
		}

		return mod;
	}

	/**
	 * returns the decreaser for a increaser
	 * 
	 * @param currentToken the token with the increaser
	 * @return the matching decreaser for the given token
	 */
	protected String getDecreaser(FloraToken currentToken) {
		if (currentToken.equals(FloraKeywords.RULE_DEFINITION))
			return FloraKeywords.DOT;

		if (currentToken.equals(FloraKeywords.QUERY_DEFINITION))
			return FloraKeywords.DOT;

		if (currentToken.equals(FloraKeywords.PARENTHESIS_OPEN))
			return FloraKeywords.PARENTHESIS_CLOSE;

		if (currentToken.equals(FloraKeywords.SQUARE_BRACKET_OPEN))
			return FloraKeywords.SQUARE_BRACKET_CLOSE;

		if (currentToken.equals(FloraKeywords.CURLY_BRACKET_OPEN))
			return FloraKeywords.CURLY_BRACKET_CLOSE;

		for (String inc : FloraKeywords.getValueReferenceConectives())
			if (currentToken.equals(inc))
				return FloraKeywords.COMMA;

		return null;
	}

	/**
	 * returns the current indentation as <code>String</code></br>
	 * 
	 * @return the current indentation as <code>String</code></br>
	 * when Preferences set to TAB the method will return a <code>String</code>
	 * containing tabs, otherwise space characters
	 */
	public String getIndentation() {
		StringBuffer indentation = new StringBuffer();
		if (indentationType.equals(FloraPreferenceConstants.EDITOR_INDENTATION_TYPE_TAB)) {
			for (int i = 0; i < getIndentationSize() / tabSize; ++i)
				indentation.append("\t");
			for (int i = 0; i < getIndentationSize() % tabSize; ++i)
				indentation.append(" ");
		} else {
			for (int i = 0; i < getIndentationSize(); ++i)
				indentation.append(" ");
		}
		return indentation.toString();

	}

	/**
	 * returns the current indentation size
	 * 
	 * @return the current indentation size
	 */
	protected int getIndentationSize() {
		if (indentations.size() > 0)
			return indentations.peek().getIndentation();
		return 0;
	}

	/**
	 * returns the tab size
	 * 
	 * @return the tab size
	 * @see #tabSize
	 */
	public int getTabSize() {
		return tabSize;
	}

	/**
	 * @author Daniel Winkler
	 *
	 * this class is a modification for the current indentation.</br>
	 * it is intended to be pushed on {@link FloraIndentationHandler#indentations}
	 */
	protected class FloraIndentationModification {
		private int fIndentation;

		private FloraToken fIndentationIncreaser;

		private String fIndentationDecreaser;

		/**
		 * the constructors</br>
		 * sets the field values to the given values
		 * 
		 * @param indentation the indentation size
		 * @param currentToken the current token
		 * @param indentationDecreaser the matching decreaser
		 */
		public FloraIndentationModification(int indentation, FloraToken currentToken, String indentationDecreaser) {
			fIndentation = indentation;
			fIndentationIncreaser = currentToken;
			fIndentationDecreaser = indentationDecreaser;
		}

		/**
		 * @return the indentation size
		 */
		public int getIndentation() {
			return fIndentation;
		}

		/**
		 * @return the indentation increaser
		 */
		public FloraToken getIndentationIncreaser() {
			return fIndentationIncreaser;
		}

		/**
		 * @return the matching indentation decreaser
		 */
		public String getIndentationDecreaser() {
			return fIndentationDecreaser;
		}
	}
}
