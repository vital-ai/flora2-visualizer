/* File:      FloraCommentScanner.java
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

package net.sourceforge.flora.eclipse.texteditor.editor.scanners;

import java.util.ArrayList;

import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;
import net.sourceforge.flora.eclipse.texteditor.editor.FloraColorManager;
import net.sourceforge.flora.eclipse.texteditor.editor.rules.FloraMultiLineComment;
import net.sourceforge.flora.eclipse.texteditor.editor.rules.FloraSingleLineComment;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.Color;

/**
 * @author Daniel Winkler
 *
 * the Flora-2 comment scanner
 */
public class FloraCommentScanner extends RuleBasedScanner {

	/**
	 * the constructor</br>
	 * creates tokens and rules
	 */
	public FloraCommentScanner() {
		IToken singleLineCommentToken = createSingleLineCommentToken();
		IToken multiLineCommentToken = createMultiLineCommentToken();

		ArrayList<IRule> rules = new ArrayList<IRule>();
		rules.add(new FloraSingleLineComment(singleLineCommentToken));
		rules.add(new FloraMultiLineComment(multiLineCommentToken));

		setRules(rules.toArray(new IRule[rules.size()]));
	}

	/**
	 * @return a new token for single line comments
	 */
	private IToken createSingleLineCommentToken() {
		Color color = FloraColorManager
				.getColor(FloraPreferenceConstants.EDITOR_COLOR_SINGLE_LINE_COMMENT);
		IToken defaultToken = new Token(new TextAttribute(color));
		return defaultToken;
	}

	/**
	 * @return a new token for multi line comments
	 */
	private IToken createMultiLineCommentToken() {
		Color color = FloraColorManager
				.getColor(FloraPreferenceConstants.EDITOR_COLOR_MULTI_LINE_COMMENT);
		IToken defaultToken = new Token(new TextAttribute(color));
		return defaultToken;
	}
}