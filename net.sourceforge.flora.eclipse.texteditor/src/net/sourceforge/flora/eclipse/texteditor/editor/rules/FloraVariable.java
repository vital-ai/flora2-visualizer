/* File:      FloraVariable.java
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

package net.sourceforge.flora.eclipse.texteditor.editor.rules;

import net.sourceforge.flora.eclipse.FloraKeywords;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.Assert;

/**
 * @author Daniel Winkler
 * 
 * A rule for detecting Flora Variables
 */
public class FloraVariable implements IPredicateRule, IRule {
	private IToken token;

	private FloraVariableDetector variableDetector;

	/**
	 * the constructor
	 * 
	 * @param token
	 *            the token that will be returned when a variable is detected
	 */
	public FloraVariable(IToken token) {
		Assert.isNotNull(token);
		this.token = token;
		variableDetector = new FloraVariableDetector();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.rules.IPredicateRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner,
	 *      boolean)
	 */
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		return evaluate(scanner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
	 */
	public IToken getSuccessToken() {
		return token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner) {
		int previousChar = 0;
		int tokenReadCount = 0;
		scanner.unread();
		previousChar = scanner.read();

		int thisChar = scanner.read();
		++tokenReadCount;

		if (previousChar == ICharacterScanner.EOF
				|| Character.isWhitespace(previousChar)
				|| FloraKeywords.isDelimiterEnd((char) previousChar)) {
			if (variableDetector.isWordStart((char) thisChar)) {
				thisChar = scanner.read();
				++tokenReadCount;
				// 2nd letter must be a letter
				if (Character.isLetter(thisChar)) {
					do {
						thisChar = scanner.read();
						++tokenReadCount;
					} while (thisChar != ICharacterScanner.EOF
							&& variableDetector.isWordPart((char) thisChar));
					scanner.unread();
					--tokenReadCount;

					if (Character.isWhitespace(thisChar)
							|| FloraKeywords.isDelimiterStart((char) thisChar)
							|| thisChar == ICharacterScanner.EOF)
						return token;
				}
			}
		}
		while (tokenReadCount > 0) {
			scanner.unread();
			--tokenReadCount;
		}
		return Token.UNDEFINED;
	}

}
