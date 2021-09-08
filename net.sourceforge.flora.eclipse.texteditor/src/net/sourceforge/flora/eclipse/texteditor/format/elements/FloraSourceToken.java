/* File:      FloraSourceToken.java
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

import net.sourceforge.flora.eclipse.FloraKeywords;

/**
 * @author Daniel Winkler
 *
 * This Class represents a source-text token of the
 * a Flora-2 text
 * 
 * @see FloraSourceText
 */
public class FloraSourceToken extends FloraToken {

    /**
     * The constructor </br>
     * Sets the current text.
     * 
     * @see #setText(String)
     * @param text the text to set
     */
    public FloraSourceToken(String text) {
	setText(text);
    }
    
    /**
     * @return <code>true</code> if the token has a preceeding whitespace 
     */
    public boolean hasPredeceedingWhitespace() {
	if (getText().equals(FloraKeywords.COMMA))
	    return false;
	if (getText().equals(FloraKeywords.DOT))
	    return false;
	if (getText().equals(FloraKeywords.SEMICOLON))
	    return false;
	if (getText().equals(FloraKeywords.SQUARE_BRACKET_CLOSE))
	    return false;
	if (getText().equals(FloraKeywords.SQUARE_BRACKET_OPEN))
	    return false;
	if (getText().equals(FloraKeywords.PARENTHESIS_CLOSE))
	    return false;
	if (getText().equals(FloraKeywords.PARENTHESIS_OPEN))
	    return false;
	if (getText().equals(FloraKeywords.CURLY_BRACKET_OPEN))
	    return false;
	if (getText().equals(FloraKeywords.CURLY_BRACKET_CLOSE))
	    return false;
	if (getText().equals(FloraKeywords.COLON))
	    return false;
	if (getText().equals(FloraKeywords.DOUBLE_COLON))
	    return false;
	if (getText().equals(FloraKeywords.EQUALITY))
	    return false;
	if (getText().equals(FloraKeywords.AT))
	    return false;

	for (String arrow : FloraKeywords.getValueReferenceConectives())
	    if (getText().equals(arrow))
		return false;

	return true;
    }

    /**
     * @return <code>true</code> if the token has a succeeding whitespace 
     */
    public boolean hasSucceedingWhitespace() {
	if (getText().equals(FloraKeywords.SQUARE_BRACKET_OPEN))
	    return false;
	if (getText().equals(FloraKeywords.PARENTHESIS_OPEN))
	    return false;
	if (getText().equals(FloraKeywords.CURLY_BRACKET_OPEN))
	    return false;
	if (getText().equals(FloraKeywords.COLON))
	    return false;
	if (getText().equals(FloraKeywords.DOUBLE_COLON))
	    return false;
	if (getText().equals(FloraKeywords.EQUALITY))
	    return false;
	if (getText().equals(FloraKeywords.PERCENT))
	    return false;
	if (getText().equals(FloraKeywords.HASH))
	    return false;
	if (getText().equals(FloraKeywords.UNDERSCORE_HASH))
	    return false;
	if (getText().equals(FloraKeywords.AT))
	    return false;

	for (String arrow : FloraKeywords.getValueReferenceConectives())
	    if (getText().equals(arrow))
		return false;

	return true;
    }
}
