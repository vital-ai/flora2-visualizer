/* File:      FloraToken.java
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
 * This class is indented to be used to build a <code>FloraSourceText</code>.
 * @see FloraSourceText
 */
public abstract class FloraToken {

    private String text = "";

    /**
     * @return the current text
     * @see #text
     */
    public String getText() {
	return text;
    }

    /**
     * sets the text property with the given text
     * 
     * @param text the text to set 
     */
    public void setText(String text) {
	this.text = text;
    }

    /**
     * @return the text representation of this object
     * 
     * @see java.lang.Object#toString()
     * @see #getText()
     */
    public String toString() {
	return getText();
    }
    
    /**
     * tests for equality by using the <code>equals</code>
     * method of String
     * 
     * @param element the element to test for equality
     * 
     * @return true if the given element is equal to this one
     * 
     * @see #toString()
     * @see String#equals(Object)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object element) {
    	return getText().equals(element.toString());
    }
}
