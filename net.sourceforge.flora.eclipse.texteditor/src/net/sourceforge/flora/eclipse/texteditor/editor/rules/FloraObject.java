/* File:      FloraObject.java
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
 * A Rule to detect Flora-2 Objects
 */
public class FloraObject implements IPredicateRule, IRule {
	private IToken token;

	private FloraObjectDetector objectDetector;

	/**
	 * the constructor
	 * 
	 * @param token
	 *            the token to be returned on success
	 */
	public FloraObject(IToken token) {
		Assert.isNotNull(token);
		this.token = token;
		objectDetector = new FloraObjectDetector();
	}

	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		return evaluate(scanner);
	}

	public IToken getSuccessToken() {
		return token;
	}

	public IToken evaluate(ICharacterScanner scanner) {
		int previousChar = 0;
		int thisChar = 0;
		int tokenReadCount = 0;
		int tailReadCount = 0;

		boolean success = false;

		scanner.unread();
		scanner.unread();
		previousChar = scanner.read();
		thisChar = scanner.read();

		if (thisChar == (int) ':') {
			if ((previousChar == ':')
					|| (new FloraInstanceDetector()
							.isWordPart((char) previousChar))
					|| (Character.isWhitespace((char) previousChar))) {
				previousChar = thisChar;
				thisChar = scanner.read();
				++tokenReadCount;
				if (objectDetector.isWordPart((char) thisChar)) {
					do {
						thisChar = scanner.read();
					} while (thisChar != ICharacterScanner.EOF
							&& objectDetector.isWordPart((char) thisChar));
					scanner.unread();
					return getSuccessToken();
				}
			} else {
				previousChar = thisChar;
				thisChar = scanner.read();
				++tokenReadCount;
			}
		} else {
			previousChar = thisChar;
			thisChar = scanner.read();
			++tokenReadCount;
		}

		if (previousChar == ICharacterScanner.EOF
				|| Character.isWhitespace(previousChar)
				|| FloraKeywords.isDelimiterEnd((char) previousChar)) {
			if (objectDetector.isWordStart((char) thisChar)) {
				do {
					thisChar = scanner.read();
					++tokenReadCount;
				} while (thisChar != ICharacterScanner.EOF
						&& objectDetector.isWordPart((char) thisChar));
				++tailReadCount;
				--tokenReadCount;

				// Scanner ended reading object name
				// eliminate whitespaces
				while (Character.isWhitespace(thisChar)) {
					thisChar = scanner.read();
					++tailReadCount;
				}
				// thisChar holds next sign (after whitespaces)

				// check for [ or ::
				if (thisChar == (int) '[') {
					success = true;
				} else if (thisChar == (int) ':') {
					thisChar = scanner.read();
					++tailReadCount;
				}
				if (thisChar == (int) ':') {
					success = true;
				}

				// goto last position of method name
				while (tailReadCount > 0) {
					scanner.unread();
					--tailReadCount;
				}

				if (success) {
					return getSuccessToken();
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
