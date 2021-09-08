/* File:      FloraMethod.java
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
 * A rule to detect Flora-2 Methods
 */
public class FloraMethod implements IPredicateRule, IRule {
	private IToken token;

	private FloraMethodDetector methodDetector;

	/**
	 * the constructor
	 * 
	 * @param token
	 *            the token to be returned on success
	 */
	public FloraMethod(IToken token) {
		Assert.isNotNull(token);
		this.token = token;
		methodDetector = new FloraMethodDetector();
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
		previousChar = scanner.read();
		thisChar = scanner.read();
		++tokenReadCount;

		if (previousChar == ICharacterScanner.EOF
				|| Character.isWhitespace(previousChar)
				|| FloraKeywords.isDelimiterEnd((char) previousChar)) {
			if (methodDetector.isWordStart((char) thisChar)) {
				do {
					thisChar = scanner.read();
					++tokenReadCount;
				} while (thisChar != ICharacterScanner.EOF
						&& methodDetector.isWordPart((char) thisChar));
				++tailReadCount;
				--tokenReadCount;

				// Scanner ended reading method name
				// eliminate whitespaces
				while (Character.isWhitespace(thisChar)) {
					thisChar = scanner.read();
					++tailReadCount;
				}

				// No more whitespace, expecting valueReferenceConective
				StringBuffer tempConective = new StringBuffer();
				while (!(Character.isWhitespace(thisChar) || thisChar == ICharacterScanner.EOF)) {

					tempConective.append((char) thisChar);
					thisChar = scanner.read();
					++tailReadCount;

					if ((FloraKeywords.getValueReferenceConectives()
							.contains(tempConective.toString()))
							|| (FloraKeywords.COMMA.equals(tempConective
									.toString()))) {
						success = true;
						break;
					}
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
