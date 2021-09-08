/* File:      FloraCommentToken.java
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

/**
 * @author Daniel Winkler
 *
 * This Class represents a comment of the
 * a Flora-2 text
 * 
 * @see FloraSourceText
 */
public class FloraCommentToken extends FloraToken {

    /**
     * The constructor </br>
     * Sets the current text.
     * 
     * @see #setText(String)
     * @param text the text to set
     */
    public FloraCommentToken(String text) {
	setText(text);
    }
}
